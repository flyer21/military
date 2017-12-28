package com.military.service.container;

import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.actuate.autoconfigure.EndpointWebMvcChildContextConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Properties;

public class ModuleInstance implements IModuleInstance {
//    Properties   properties;

    private String name;
    private String version;
    private ModuleContainer container;
    private ModuleContext context;
    private URLClassLoader classLoader;

    public ModuleInstance(ModuleContainer container) {
        this.container = container;

    }

    @Override
    public String getName() {
        return name;
    }

    public void init(URL url) throws IOException {

        Properties properties = new Properties();
        properties.load(url.openStream());
        name = properties.getProperty("Module-Name");
        version = properties.getProperty("Module-Version");
        String baseDir  = url.toString().replace("META-INF/military.properties","");
        System.out.print("base Dir: " + baseDir);
        URL[] urls = new URL[]{new URL(baseDir)};
        this.classLoader = new URLClassLoader(urls,container.getContext().getClassLoader());

    }

    private static class CloseManagementContextListener
            implements ApplicationListener<ApplicationEvent> {

        private final ApplicationContext parentContext;

        private final ConfigurableApplicationContext childContext;

        CloseManagementContextListener(ApplicationContext parentContext,
                                       ConfigurableApplicationContext childContext) {
            this.parentContext = parentContext;
            this.childContext = childContext;
        }

        @Override
        public void onApplicationEvent(ApplicationEvent event) {
            if (event instanceof ContextClosedEvent) {
                onContextClosedEvent((ContextClosedEvent) event);
            }
            if (event instanceof ApplicationFailedEvent) {
                onApplicationFailedEvent((ApplicationFailedEvent) event);
            }
        };

        private void onContextClosedEvent(ContextClosedEvent event) {
            propagateCloseIfNecessary(event.getApplicationContext());
        }

        private void onApplicationFailedEvent(ApplicationFailedEvent event) {
            propagateCloseIfNecessary(event.getApplicationContext());
        }

        private void propagateCloseIfNecessary(ApplicationContext applicationContext) {
            if (applicationContext == this.parentContext) {
                this.childContext.close();
            }
        }

        public static void addIfPossible(ApplicationContext parentContext,
                                         ConfigurableApplicationContext childContext) {
            if (parentContext instanceof ConfigurableApplicationContext) {
                add((ConfigurableApplicationContext) parentContext, childContext);
            }
        }

        private static void add(ConfigurableApplicationContext parentContext,
                                ConfigurableApplicationContext childContext) {
            parentContext.addApplicationListener(
                    new CloseManagementContextListener(parentContext, childContext));
        }

    }

    void start() {
        this.context = new ModuleContext();
        this.context.setParent(container.getContext());

        this.context.setNamespace("Module");
        this.context.setId(this.getName() +":Module");
        this.context.setClassLoader(this.classLoader);
        context.register(EndpointWebMvcChildContextConfiguration.class,
                EmbeddedServletContainerAutoConfiguration.class,
                DispatcherServletAutoConfiguration.class);

        registerEmbeddedServletContainerFactory(context);
        CloseManagementContextListener.addIfPossible(container.getContext(),
                context);

        this.context.refresh();
        String[] beanNames = context.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }
    private Class<?> determineEmbeddedServletContainerFactoryClass()
            throws NoSuchBeanDefinitionException {
        Class<?> servletContainerFactoryClass = container.getContext()
                .getBean(EmbeddedServletContainerFactory.class).getClass();
        if (cannotBeInstantiated(servletContainerFactoryClass)) {
            throw new FatalBeanException("EmbeddedServletContainerFactory implementation "
                    + servletContainerFactoryClass.getName() + " cannot be instantiated. "
                    + "To allow a separate management port to be used, a top-level class "
                    + "or static inner class should be used instead");
        }
        return servletContainerFactoryClass;
    }
    private boolean cannotBeInstantiated(Class<?> clazz) {
        return clazz.isLocalClass()
                || (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers()))
                || clazz.isAnonymousClass();
    }

    private void registerEmbeddedServletContainerFactory(
            AnnotationConfigEmbeddedWebApplicationContext childContext) {
        try {
            ConfigurableListableBeanFactory beanFactory = childContext.getBeanFactory();
            if (beanFactory instanceof BeanDefinitionRegistry) {
                BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
                registry.registerBeanDefinition("embeddedServletContainerFactory",
                        new RootBeanDefinition(
                                determineEmbeddedServletContainerFactoryClass()));
            }
        }
        catch (NoSuchBeanDefinitionException ex) {
            // Ignore and assume auto-configuration
        }
    }
    void stop() {
        this.context.stop();

    }

    void refresh() {
        this.context.refresh();

    }


    @Override
    public String getVersion() {
        return version;
    }


//    public void setBaseURL(URL baseURL) {
//        URL[] urls = new URL[]{baseURL};
//        this.classLoader = new URLClassLoader(urls,container.getContext().getClassLoader());
//    }

    @Override
    public ModuleContext getContext() {
        return context;
    }

}
