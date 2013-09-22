package org.camunda.bpm.camel.cdi.itest;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.camunda.bpm.camel.component.CamundaBpmComponent;
import org.camunda.bpm.engine.ProcessEngine;


public class CamelContextProducer {

    @Produces @ApplicationScoped
    public CamelContext camelContext(RouteBuilder routeBuilder, ProcessEngine engine) {
        DefaultCamelContext context = new DefaultCamelContext();
        CamundaBpmComponent component = new CamundaBpmComponent(engine);
        component.setCamelContext(context);
        context.addComponent("camunda-bpm", component);
        try {
            context.addRoutes(routeBuilder);
            context.start();
        }
        catch (Exception exc) {
            throw new RuntimeException(exc);
        }
        return context;
    }
    
    public void stopContext(@Disposes CamelContext context) throws Exception {
        context.stop();
    }
}
