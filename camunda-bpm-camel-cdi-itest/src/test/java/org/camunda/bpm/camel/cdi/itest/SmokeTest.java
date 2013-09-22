package org.camunda.bpm.camel.cdi.itest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.ServiceStatus;
import org.camunda.bpm.application.ProcessApplicationInterface;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;

@RunWith(PaxExam.class)
public class SmokeTest {

    @Inject
    @Rule
    public BootstrapRule bootstrap;

    @Inject
    private ProcessApplicationInterface appl;

    @Inject
    private CamelContext context;
    

    @Test
    public void test() {
        assertThat(context.getStatus(), is(ServiceStatus.Started));
        assertThat(appl.getName(), is("Process Application"));
    }
}
