package de.ur.location.execution;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
@Slf4j
public class InformCustomer implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        log.info("Inform customer using third party service");
        if (execution.getVariable("informCustomerNow") != null) {
            execution.removeVariable("informCustomerNow");
        }
        if (execution.getVariable("craftsmanAvailable") != null) {
            execution.removeVariable("craftsmanAvailable");
        }
    }
}