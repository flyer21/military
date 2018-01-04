package com.military.service.container.module.impl;

import com.military.service.container.ModuleApplicationContainer;
import com.military.service.container.module.IModuleInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import static com.military.service.container.ModuleApplicationContainer.MILITARY_CONTAINER_BASE_PACKAGE;
import static com.military.service.container.ModuleApplicationContainer.MODULE_SPRING_XML;

public class ModuleInstance implements IModuleInstance {


    private String name;
    private String version;
    private ModuleApplicationContainer container;
    private ModuleApplicationContext context;
    private ModuleClassLoader classLoader;
    private static Logger logger = LoggerFactory.getLogger(ModuleInstance.class);
    private String baseDir;
    private String basePackage;
    private String configFile;

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
        configFile = properties.getProperty("Config-File");
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

        if (this.configFile!=null) {
            this.context.setConfigLocations(MODULE_SPRING_XML, this.configFile);
        }
        else{
            this.context.setConfigLocations(MODULE_SPRING_XML);
        }


        this.context.setId(this.getName() +":Module");
        this.context.setClassLoader(this.classLoader);
        MuduleContextListener.addIfPossible(container.getContext(),
                context);

        this.context.refresh();
        logger.info("success start {}", this.getName());
    }
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


    @Override
    public AbstractApplicationContext getContext() {
        return context;
    }

    @Override
    public Object lookupHandler(String urlPath, HttpServletRequest request)  throws Exception {
        Map<String, RequestMappingHandlerMapping> mappings = context.getBeansOfType(RequestMappingHandlerMapping.class);
        for (RequestMappingHandlerMapping mp : mappings.values()) {
            HandlerExecutionChain handler = mp.getHandler(request);
            if (handler!=null){
                return handler;
            }
        }
        return null;
    }

}
