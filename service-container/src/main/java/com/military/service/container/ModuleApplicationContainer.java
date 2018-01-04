package  com.military.service.container;

import com.military.service.container.module.impl.ModuleInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
public class ModuleApplicationContainer implements IModuleApplicationContainer {
    public static final String MODULE_PROPERTIES = "META-INF/module.properties";
    public static final String MILITARY_CONTAINER_PATH = "military.container.path";
    public static final String MILITARY_CONTAINER_DIR = "military.container.dir";
    public static final String MODULE_SPRING_XML = "module-base-spring.xml";
    public static final String MILITARY_CONTAINER_BASE_PACKAGE = "military.container.base-package";
    @Autowired
    private ApplicationContext context;

    private Map <String ,ModuleInstance> services= new HashMap<String ,ModuleInstance>();

    public ModuleApplicationContainer() {
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
            Enumeration<URL> rss = cl.getResources(MODULE_PROPERTIES);
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
        List<URL> urls = new ArrayList<>();
        String path = ctx.getEnvironment().getProperty(MILITARY_CONTAINER_PATH);

        if (path!=null && path.length()>0) {
            String[] paths = path.split(";");

            for (int i = 0; i < paths.length; i++) {
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
        }
        String dir = ctx.getEnvironment().getProperty(MILITARY_CONTAINER_DIR);
        if (dir!=null && dir.length()>0) {
            File dirf = new File(dir);
            if (dirf.exists()) {
                File[] files = dirf.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        if (pathname.getName().endsWith(".jar")) {
                            return true;
                        }
                        ;
                        return false;
                    }
                });
                for (int i = 0; i < files.length; i++) {
                    try {
                        urls.add(files[i].toURI().toURL());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
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

    @Override
    public Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception {

        Iterator<String> it = services.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            ModuleInstance service = services.get(key);
            Object handler = service.lookupHandler(urlPath,request);
            if (handler!=null){
                return handler;
            }
        }
        return null;

    }

}
