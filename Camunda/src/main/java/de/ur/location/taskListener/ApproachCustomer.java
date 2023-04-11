package de.ur.location.taskListener;

import de.ur.location.Application;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

import javax.inject.Named;


@Named
public class ApproachCustomer implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        Application.updateCustomerNodeRed(delegateTask);
    }
}
