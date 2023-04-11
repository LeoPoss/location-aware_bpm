package de.ur.location.taskListener;

import de.ur.location.Application;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;


@Slf4j
public class ReplaceHeating implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("Started working at customer");
        Application.updateCustomerNodeRed(delegateTask);
    }
}