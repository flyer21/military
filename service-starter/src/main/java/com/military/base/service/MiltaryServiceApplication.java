package com.military.base.service;

import com.military.service.container.ModuleApplicationContainer;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@EnableEurekaClient
@SpringBootApplication
@RestController
@EnableHystrix
public class MiltaryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiltaryServiceApplication.class, args);
    }
    @RequestMapping("/ok")
    public String ok(){
//        LOG.info("calling trace service-hi ");
        return "i'm service-hi1111";

    }
    ModuleApplicationContainer sc;
    @Bean()
    public MilitrayHandlerMapping militrayHandlerMapping(
            ResourceProperties resourceProperties) {
        return new MilitrayHandlerMapping(resourceProperties.getWelcomePage(),
       "/**");
    }
    final class MilitrayHandlerMapping extends AbstractUrlHandlerMapping {



        private MilitrayHandlerMapping(Resource welcomePage,
                                       String staticPathPattern) {
            this.setOrder(-1);
            if (welcomePage != null && "/**".equals(staticPathPattern)) {
                logger.info("Adding welcome page: " + welcomePage);
                ParameterizableViewController controller = new ParameterizableViewController();
                controller.setViewName("forward:index.html");
                setRootHandler(controller);
                setOrder(0);
            }
        }



        @Override
        public Object getHandlerInternal(HttpServletRequest request) throws Exception {
            for (MediaType mediaType : getAcceptedMediaTypes(request)) {
                if (mediaType.includes(MediaType.TEXT_HTML)) {
                    return super.getHandlerInternal(request);
                }
            }
            return null;
        }

        @Override
        protected Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception {

//            return super.lookupHandler(urlPath, request);
            return sc.lookupHandler(urlPath, request);
        }

        private List<MediaType> getAcceptedMediaTypes(HttpServletRequest request) {
            String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
            return MediaType.parseMediaTypes(
                    StringUtils.hasText(acceptHeader) ? acceptHeader : "*/*");
        }

    }
    @Bean
    public ApplicationRunner applicationRunner(ApplicationContext ctx){
        return  cc ->{
            System.out.printf("==== begin load Modules. \n");
            long st = System.currentTimeMillis();

            sc= new ModuleApplicationContainer(ctx);
            sc.init();
            sc.start();

            System.out.printf("==== sucessed Load Modules, used %s ms.\n",(System.currentTimeMillis()-st));


        };
    }

}
