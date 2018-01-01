package com.military.service.container;

import javax.servlet.http.HttpServletRequest;

public interface IModuleApplicationContainer {

    void init();

    void start() ;

    void stop();

    void refresh() ;

    Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception;
}
