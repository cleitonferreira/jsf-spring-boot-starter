package com.github.persapiens.jsfboot.mojarra;

import com.github.persapiens.jsfboot.JsfAnnotatedClassFactory;
import com.github.persapiens.jsfboot.JsfAnnotatedClassFactoryConfiguration;
import com.sun.faces.config.FacesInitializer;
import java.util.Set;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.springframework.boot.context.embedded.ServletContextInitializer;

public class MojarraServletContextInitializer implements ServletContextInitializer, JsfAnnotatedClassFactoryConfiguration {
	
	private MojarraProperties mojarraProperties;

    public MojarraServletContextInitializer(MojarraProperties mojarraProperties) {
        this.mojarraProperties = mojarraProperties;
    }
    
    private ServletContainerInitializer servletContainerInitializer;
    
    @Override
    public ServletContainerInitializer getServletContainerInitializer()
    {
        if (servletContainerInitializer == null)
        {
            servletContainerInitializer = new FacesInitializer();
        }
        return servletContainerInitializer;
    }
    
    @Override
    public String getAnotherFacesConfig() {
        return "com/sun/faces/jsf-ri-runtime.xml";
    }
    
    @Override
    public boolean isExcludeScopedAnnotations() {
        return true;
    }
    
    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        MojarraServletContextConfigurer.builder()
            .mojarraProperties(mojarraProperties)
            .servletContext(sc)
            .build()
            .configure();
        
        ServletContainerInitializer servletContainerInitializer = getServletContainerInitializer();
        Set<Class<?>> classes = JsfAnnotatedClassFactory.builder()
            .jsfAnnotatedClassFactoryConfiguration(this)
            .build().find();
        servletContainerInitializer.onStartup(classes, sc);
    }            
}
