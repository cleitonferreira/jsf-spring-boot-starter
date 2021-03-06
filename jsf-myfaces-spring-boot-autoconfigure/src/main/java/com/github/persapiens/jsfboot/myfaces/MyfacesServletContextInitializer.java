package com.github.persapiens.jsfboot.myfaces;

import com.github.persapiens.jsfboot.JsfAnnotatedClassFactory;
import com.github.persapiens.jsfboot.JsfAnnotatedClassFactoryConfiguration;
import java.util.Set;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.apache.myfaces.ee6.MyFacesContainerInitializer;
import org.springframework.boot.context.embedded.ServletContextInitializer;

public class MyfacesServletContextInitializer implements ServletContextInitializer, JsfAnnotatedClassFactoryConfiguration {
	
	private MyfacesProperties myfacesProperties;

    public MyfacesServletContextInitializer(MyfacesProperties myfacesProperties) {
        this.myfacesProperties = myfacesProperties;
    }
    
    private ServletContainerInitializer servletContainerInitializer;
    
    @Override
    public ServletContainerInitializer getServletContainerInitializer()
    {
        if (servletContainerInitializer == null)
        {
            servletContainerInitializer = new MyFacesContainerInitializer();
        }
        return servletContainerInitializer;
    }
    
    @Override
    public String getAnotherFacesConfig() {
        return "META-INF/standard-faces-config.xml";
    }
    
    @Override
    public boolean isExcludeScopedAnnotations() {
        return true;
    }
    
    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        MyfacesServletContextConfigurer.builder()
            .myfacesProperties(myfacesProperties)
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
