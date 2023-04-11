package de.ur.location.taskListener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

@Slf4j
public class ApproachDepot implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("Finished working at customer, driving home now");
        delegateTask.setAssignee(delegateTask.getExecution().getProcessBusinessKey());
    }
}
