package de.ur.location.execution;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
public class Subprocess implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        delegateExecution.setVariableLocal("currentDistanceToCustomer", 0);
        delegateExecution.setVariable("assignedCraftsmen", 0);
        delegateExecution.removeVariable("craftsmanAvailable");
    }
}
