package org.camunda.bpm.camel.cdi.itest;

import static java.util.Collections.singletonList;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.application.impl.EmbeddedProcessApplication;
import org.camunda.bpm.container.RuntimeContainerDelegate;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.cdi.CdiStandaloneProcessEngineConfiguration;
import org.camunda.bpm.engine.cdi.impl.event.CdiEventSupportBpmnParseListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;


public class TestApplicationProducer {
    
    @Produces @ApplicationScoped @Named("testConfig")
    public ProcessEngineConfiguration processEngineConfig() {
        CdiStandaloneProcessEngineConfiguration config = new CdiStandaloneProcessEngineConfiguration();
        config.setJdbcUrl("jdbc:h2:mem:camunda");
        BpmnParseListener listener = new CdiEventSupportBpmnParseListener();
        config.setCustomPostBPMNParseListeners(singletonList(listener));
        config.setDatabaseSchemaUpdate("true");
        config.setProcessEngineName("default");
        return config;
    }
    
    @Produces @ApplicationScoped 
    public EmbeddedProcessApplication testApplication(@Named("testConfig") ProcessEngineConfiguration config) {
        
        ProcessEngine engine = config.buildProcessEngine();

        if (BpmPlatform.getProcessEngineService().getDefaultProcessEngine() == null) {
            RuntimeContainerDelegate.INSTANCE.get().registerProcessEngine(engine);
        }

        EmbeddedProcessApplication appl = new EmbeddedProcessApplication();
        appl.deploy();
        return appl;
    }
    
    public void undeploy(@Disposes EmbeddedProcessApplication appl) {
        appl.undeploy();
    }
}
