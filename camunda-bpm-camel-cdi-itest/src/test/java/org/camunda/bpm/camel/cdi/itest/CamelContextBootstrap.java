package org.camunda.bpm.camel.cdi.itest;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.cdi.CdiCamelContext;
import org.apache.camel.cdi.ContextName;
import org.camunda.bpm.camel.component.CamundaBpmComponent;
import org.camunda.bpm.engine.ProcessEngine;

@ApplicationScoped
public class CamelContextBootstrap {

    @Inject
    private CdiCamelContext context;
    
    @Inject @ContextName("sendToCamel")
    private RoutesBuilder builder;
    
    @Inject
    private ProcessEngine engine;

    @PostConstruct
    public void start() {
        
        CamundaBpmComponent component = new CamundaBpmComponent(engine);
        component.setCamelContext(context);
        context.addComponent("camunda-bpm", component);
        try {
            context.addRoutes(builder);
            context.start();
        }
        catch (Exception exc) {
            throw new RuntimeException(exc);
        }
   }
    
    public CamelContext getCamelContext() {
        return context;
    }
}
