package com.military.service.container.module;

import org.springframework.context.support.AbstractApplicationContext;

import javax.servlet.http.HttpServletRequest;

public interface IModuleInstance {
    String getName();

    String getVersion();

    AbstractApplicationContext getContext();

    Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception;
}
