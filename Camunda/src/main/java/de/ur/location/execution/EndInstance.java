package de.ur.location.execution;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

@Slf4j
public class EndInstance implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        log.info("Finished process instance");
    }
}