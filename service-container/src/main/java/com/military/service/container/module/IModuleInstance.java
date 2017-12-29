package com.military.service.container.module;

import org.springframework.context.support.AbstractApplicationContext;

public interface IModuleInstance {
    String getName();

    String getVersion();

    AbstractApplicationContext getContext();
}
