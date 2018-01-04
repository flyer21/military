package com.military.service.container;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;



public  class MilitaryHandlerMapping extends AbstractUrlHandlerMapping {

    @Autowired
    ModuleApplicationContainer sc;

        public  MilitaryHandlerMapping() {
            this.setOrder(-1);

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

            return sc.lookupHandler(urlPath, request);
        }

        private List<MediaType> getAcceptedMediaTypes(HttpServletRequest request) {
            String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
            return MediaType.parseMediaTypes(
                    StringUtils.hasText(acceptHeader) ? acceptHeader : "*/*");
        }

    }