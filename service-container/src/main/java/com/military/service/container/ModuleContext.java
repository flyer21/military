package com.military.service.container;

import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

public class ModuleContext extends AnnotationConfigEmbeddedWebApplicationContext  {
      AnnotatedBeanDefinitionReader reader;
      ClassPathBeanDefinitionScanner scanner;

    public ModuleContext(){
        super();
//        super(parent);
//        this.reader = new AnnotatedBeanDefinitionReader(this);
//        this.scanner = new ClassPathBeanDefinitionScanner(this);
    }
//    public void setEnvironment(ConfigurableEnvironment environment) {
//        super.setEnvironment(environment);
//        this.reader.setEnvironment(environment);
//        this.scanner.setEnvironment(environment);
//    }

//    @Override
//    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
//        this.getListableBeanFactory().registerBeanDefinition(beanName,beanDefinition);
//    }
//
//    @Override
//    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
//        this.getListableBeanFactory().removeBeanDefinition(beanName);
//    }
//
//    @Override
//    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
//        return this.getListableBeanFactory().getBeanDefinition(beanName);
//    }
//
//    @Override
//    public boolean isBeanNameInUse(String beanName) {
//        return this.getListableBeanFactory().isBeanNameInUse(beanName);
//    }
//
//    @Override
//    public void registerAlias(String name, String alias) {
//        this.getListableBeanFactory().registerAlias(name,alias);
//
//    }
//
//    @Override
//    public void removeAlias(String alias) {
//        this.getListableBeanFactory().removeAlias(alias);
//
//    }
//
//    @Override
//    public boolean isAlias(String beanName) {
//        return this.getListableBeanFactory().isAlias(beanName);
//    }
//
//    public DefaultListableBeanFactory getListableBeanFactory() {
//        return (DefaultListableBeanFactory) this.getBeanFactory();
//    }
}
