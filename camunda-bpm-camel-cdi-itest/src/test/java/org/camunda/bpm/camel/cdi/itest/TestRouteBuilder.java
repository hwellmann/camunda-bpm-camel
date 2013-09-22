package org.camunda.bpm.camel.cdi.itest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Mock;
import org.apache.camel.component.mock.MockEndpoint;

@ApplicationScoped
public class TestRouteBuilder extends RouteBuilder {

    @Inject @Mock
    MockEndpoint resultEndpoint;

    @Inject @Mock
    MockEndpoint receiveEndpoint;

    @Override
    public void configure() throws Exception {
        from("timer://smoke-message?repeatCount=1")
        .routeId("smoke-test-route")
        .to("log:org.camunda.bpm.camel.cdi?level=INFO&showAll=true&multiline=true")
        .to(resultEndpoint)
      ;

        
        from("direct:sendToCamelServiceTask")
        .routeId("send-to-camel-route")
        .to("log:org.camunda.bpm.camel.cdi?level=INFO&showAll=true&multiline=true")
        .to(resultEndpoint)
      ;
        
        from("direct:sendToCamundaBpm")
        .routeId("receive-from-camel-route")
        .to("log:org.camunda.bpm.camel.cdi?level=INFO&showAll=true&multiline=true")
        .to("camunda-bpm://signal?processDefinitionKey=receiveFromCamelProcess&activityId=waitForCamel")
        .to("log:org.camunda.bpm.camel.cdi?level=INFO&showAll=true&multiline=true")
        .to(receiveEndpoint)
      ;
        
    }

}
