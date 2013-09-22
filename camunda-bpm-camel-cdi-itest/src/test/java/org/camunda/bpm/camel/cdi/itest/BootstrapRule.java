package org.camunda.bpm.camel.cdi.itest;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.camunda.bpm.application.ProcessApplicationInterface;
import org.junit.rules.ExternalResource;

/**
 * Application scoped beans do not get initialized eagerly by CDI. We use this JUnit rule to eagerly
 * initialize some beans by calling their toString() method.
 * 
 * @author hwellmann
 * 
 */
public class BootstrapRule extends ExternalResource {

    @Inject
    private CamelContext context;

    @Inject
    private ProcessApplicationInterface appl;

    @Override
    protected void before() throws Throwable {
        appl.toString();
        context.toString();
    }
}
