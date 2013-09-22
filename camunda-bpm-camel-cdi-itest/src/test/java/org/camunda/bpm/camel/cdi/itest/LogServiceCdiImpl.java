package org.camunda.bpm.camel.cdi.itest;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("log")
public class LogServiceCdiImpl {

    final Logger log = LoggerFactory.getLogger(this.getClass());

    public void debug(Object msg) {
      log.debug("LogService: {}", msg.toString());
    }

    public void info(Object msg) {
      log.debug("LogService: {}", msg.toString());
    }
}
