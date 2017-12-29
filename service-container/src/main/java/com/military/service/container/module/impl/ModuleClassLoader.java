package com.military.service.container.module.impl;

import java.net.URL;
import java.net.URLClassLoader;

public class ModuleClassLoader extends URLClassLoader {
    public ModuleClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
