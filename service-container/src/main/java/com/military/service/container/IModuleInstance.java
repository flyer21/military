package com.military.service.container;

public interface IModuleInstance {
    String getName();

    String getVersion();

    ModuleContext getContext();
}
