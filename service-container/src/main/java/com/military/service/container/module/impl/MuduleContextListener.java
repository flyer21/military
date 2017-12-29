package com.military.service.container.module.impl;

import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

public   class MuduleContextListener
            implements ApplicationListener<ApplicationEvent> {

        private final ApplicationContext parentContext;

        private final ConfigurableApplicationContext childContext;

        MuduleContextListener(ApplicationContext parentContext,
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
                    new MuduleContextListener(parentContext, childContext));
        }

    }