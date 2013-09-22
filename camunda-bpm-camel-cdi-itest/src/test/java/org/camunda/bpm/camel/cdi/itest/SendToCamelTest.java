package org.camunda.bpm.camel.cdi.itest;

import static org.camunda.bpm.camel.component.CamundaBpmConstants.CAMUNDA_BPM_PROCESS_INSTANCE_ID;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.component.mock.MockEndpoint;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;

@RunWith(PaxExam.class)
public class SendToCamelTest {

    @Inject
    @Rule
    public BootstrapRule bootstrap;

    @Inject
    private RuntimeService runtimeService;

    @Inject
    private HistoryService historyService;

    @Inject
    private CamelContext context;

    private MockEndpoint resultEndpoint;

    @Test
    public void doTest() throws InterruptedException {

        resultEndpoint = context.getEndpoint("mock:resultEndpoint", MockEndpoint.class);

        Map<String, Object> processVariables = new HashMap<String, Object>();
        processVariables.put("var1", "foo");
        processVariables.put("var2", "bar");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
            "sendToCamelProcess", processVariables);

        // Verify that a process instance was executed and there are no instances executing now
        assertThat(
            historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey("sendToCamelProcess").count()).isEqualTo(1);
        assertThat(
            runtimeService.createProcessInstanceQuery().processDefinitionKey("sendToCamelProcess")
                .count()).isEqualTo(0);

        // Assert that the camunda BPM process instance ID has been added as a property to the
        // message
        assertThat(
            resultEndpoint.assertExchangeReceived(0).getProperty(CAMUNDA_BPM_PROCESS_INSTANCE_ID))
            .isEqualTo(processInstance.getId());

        // Assert that the body of the message received by the endpoint contains a hash map with the
        // value of the process variable 'var1' sent from camunda BPM
        assertThat(resultEndpoint.assertExchangeReceived(0).getIn().getBody(String.class))
            .isEqualTo("{var1=foo}");

        // FIXME: check that var2 is also present as a property!
    }
}
