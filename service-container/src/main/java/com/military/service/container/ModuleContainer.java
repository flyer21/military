package  com.military.service.container;

import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class ModuleContainer implements IModuleContainer {
    private final ApplicationContext context;
    private String path;

    private Map <String ,ModuleInstance> services= new HashMap<String ,ModuleInstance>();

    public ModuleContainer(ApplicationContext ctx) {
        this.context = ctx;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void init() {
        List<URL> urls = getUrls(context);
        Map<String, ModuleInstance> ss = getModuleInstance(urls);
        this.services = ss;


    }

    private Map<String, ModuleInstance> getModuleInstance(List<URL> urls) {
        Map <String ,ModuleInstance> ss= new HashMap<String ,ModuleInstance>();
        URLClassLoader cl = new URLClassLoader(urls.toArray(new URL[urls.size()]));
        try {
            Enumeration<URL> rss = cl.getResources("META-INF/military.properties");
            while (rss.hasMoreElements()){
                URL rs = rss.nextElement();
                ModuleInstance si = new ModuleInstance(this);
                si.init(rs);

                if(ss.containsKey(si.getName())){
                    continue;
                }
                ss.put(si.getName(),si);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ss;
    }

    private List<URL> getUrls(ApplicationContext ctx) {
        String path = ctx.getEnvironment().getProperty("military.container.path");
        String dir = ctx.getEnvironment().getProperty("military.container.dir");
        String[] paths = path.split(";");
        List<URL> urls = new ArrayList<>();
        for(int i=0;i<paths.length;i++){
            try {
                urls.add(new URL(paths[i]));
            } catch (MalformedURLException e) {
                try {
                    urls.add(new File(paths[i]).toURI().toURL());
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }
//                e.printStackTrace();
            }
        }
        File[] files = new File(dir).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".jar")){
                    return true;
                };
                return false;
            }
        }) ;
        for(int i=0;i<files.length;i++){
            try {
                urls.add(files[i].toURI().toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    @Override
    public void start() {
        Iterator<String> it = services.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            ModuleInstance service = services.get(key);
            service.start();
        }


    }

    @Override
    public void stop() {
        Iterator<String> it = services.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            ModuleInstance service = services.get(key);
            service.stop();
        }


    }

    @Override
    public void refresh() {
        Iterator<String> it = services.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            ModuleInstance service = services.get(key);
            service.refresh();
        }



    }

}
