package org.camunda.bpm.camel.cdi.itest;

import static org.camunda.bpm.camel.component.CamundaBpmConstants.CAMUNDA_BPM_PROCESS_INSTANCE_ID;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.camunda.bpm.application.ProcessApplicationInterface;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;

@RunWith(PaxExam.class)
public class ReceiveFromCamelTest {

  @Inject
  private ProcessApplicationInterface appl;

  @Inject
  private RuntimeService runtimeService;
  
  @Inject
  private HistoryService historyService;
  
  @Inject
  private CamelContext context;
  

//  @Inject
//  @Mock
  MockEndpoint receiveEndpoint;

  @Test
  public void doTest() throws InterruptedException {
      
      System.out.println(appl.toString());
      System.out.println(context.toString());
      
      receiveEndpoint = context.getEndpoint("mock:receiveEndpoint", MockEndpoint.class);

      Map<String, Object> processVariables = new HashMap<String, Object>();
      processVariables.put("var1", "foo");
      processVariables.put("var2", "bar");
      ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("receiveFromCamelProcess", processVariables);

      // Verify that a process instance has executed and there is one instance executing now
      assertThat(historyService.createHistoricProcessInstanceQuery().processDefinitionKey("receiveFromCamelProcess").count()).isEqualTo(1);
      assertThat(runtimeService.createProcessInstanceQuery().processDefinitionKey("receiveFromCamelProcess").count()).isEqualTo(1);

      /*
       * We need the process instance ID to be able to send the message to it
       *
       * FIXME: we need to fix this with the process execution id or even better with the Activity Instance Model
       * http://camundabpm.blogspot.de/2013/06/introducing-activity-instance-model-to.html
       */
      ProducerTemplate tpl = context.createProducerTemplate();
      tpl.sendBodyAndProperty("direct:sendToCamundaBpm", null, CAMUNDA_BPM_PROCESS_INSTANCE_ID, processInstance.getId());

      // Assert that the camunda BPM process instance ID has been added as a property to the message
      assertThat(receiveEndpoint.assertExchangeReceived(0).getProperty(CAMUNDA_BPM_PROCESS_INSTANCE_ID)).isEqualTo(processInstance.getId());

      // Assert that the process instance is finished
      assertThat(runtimeService.createProcessInstanceQuery().processDefinitionKey("receiveFromCamelProcess").count()).isEqualTo(0);
            
  }
}