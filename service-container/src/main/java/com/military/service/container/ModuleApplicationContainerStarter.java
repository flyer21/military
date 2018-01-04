package com.military.service.container;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class ModuleApplicationContainerStarter implements ApplicationRunner{

    @Autowired
    ModuleApplicationContainer sc;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.printf("==== begin load Modules. \n");
        long st = System.currentTimeMillis();
        sc.init();
        sc.start();

        System.out.printf("==== sucessed Load Modules, used %s ms.\n",(System.currentTimeMillis()-st));

    }
}
