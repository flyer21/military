package com.military.service.container.module.impl;

import com.military.service.container.ModuleApplicationContainer;
import com.military.service.container.module.IModuleInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

public class ModuleInstance implements IModuleInstance {
    public static final String MODULE_SPRING_XML = "META-INF/module-spring.xml";
    public static final String MILITARY_CONTAINER_BASE_PACKAGE = "military.container.base-package";
    //    Properties   properties;

    private String name;
    private String version;
    private ModuleApplicationContainer container;
    private ModuleApplicationContext context;
    private ModuleClassLoader classLoader;
    private static Logger logger = LoggerFactory.getLogger(ModuleInstance.class);
    private String baseDir;
    private String basePackage;

    public ModuleInstance(ModuleApplicationContainer container) {
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
        basePackage = properties.getProperty("Base-Package");
        baseDir  = url.toString().replace(ModuleApplicationContainer.MODULE_PROPERTIES,"");
        logger.info("base Dir: {}" , baseDir);
        URL[] urls = new URL[]{new URL(baseDir)};
        this.classLoader = new ModuleClassLoader(urls,container.getContext().getClassLoader());

    }


    public void start() {

        logger.info("start {} from {}", this.getName(), this.baseDir);
        this.context = new ModuleApplicationContext();
        this.context.setParent(container.getContext());
        this.context.getEnvironment().getSystemProperties().put(MILITARY_CONTAINER_BASE_PACKAGE, this.basePackage);

//        this.context.re
        this.context.setConfigLocation(MODULE_SPRING_XML);

//        this.context.setNamespace("Module");
        this.context.setId(this.getName() +":Module");
        this.context.setClassLoader(this.classLoader);
//        this.context.scan("com");
//        context.register(EndpointWebMvcChildContextConfiguration.class,
//                EmbeddedServletContainerAutoConfiguration.class,
//                DispatcherServletAutoConfiguration.class);

//        registerEmbeddedServletContainerFactory(context);
        MuduleContextListener.addIfPossible(container.getContext(),
                context);

        this.context.refresh();
        String[] beanNames = context.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }
//    private Class<?> determineEmbeddedServletContainerFactoryClass()
//            throws NoSuchBeanDefinitionException {
//        Class<?> servletContainerFactoryClass = container.getContext()
//                .getBean(EmbeddedServletContainerFactory.class).getClass();
//        if (cannotBeInstantiated(servletContainerFactoryClass)) {
//            throw new FatalBeanException("EmbeddedServletContainerFactory implementation "
//                    + servletContainerFactoryClass.getName() + " cannot be instantiated. "
//                    + "To allow a separate management port to be used, a top-level class "
//                    + "or static inner class should be used instead");
//        }
//        return servletContainerFactoryClass;
//    }
//    private boolean cannotBeInstantiated(Class<?> clazz) {
//        return clazz.isLocalClass()
//                || (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers()))
//                || clazz.isAnonymousClass();
//    }

//    private void registerEmbeddedServletContainerFactory(
//            AnnotationConfigEmbeddedWebApplicationContext childContext) {
//        try {
//            ConfigurableListableBeanFactory beanFactory = childContext.getBeanFactory();
//            if (beanFactory instanceof BeanDefinitionRegistry) {
//                BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
//                registry.registerBeanDefinition("embeddedServletContainerFactory",
//                        new RootBeanDefinition(
//                                determineEmbeddedServletContainerFactoryClass()));
//            }
//        }
//        catch (NoSuchBeanDefinitionException ex) {
//            // Ignore and assume auto-configuration
//        }
//    }
    public void stop() {
        this.context.stop();

    }

    public  void refresh() {
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
    public AbstractApplicationContext getContext() {
        return context;
    }

}
