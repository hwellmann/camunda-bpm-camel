package org.camunda.bpm.camel.cdi.itest;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.camunda.bpm.application.ProcessApplicationInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;

@RunWith(PaxExam.class)
public class SmokeTest {

    @Inject
    private ProcessApplicationInterface appl;

    @Inject
    CamelContext context;
    

    @Test
    public void test() {
        System.out.println(appl.toString());
        System.out.println(context.toString());

    }
}
