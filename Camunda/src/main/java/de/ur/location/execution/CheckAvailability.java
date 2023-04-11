package de.ur.location.execution;

import de.ur.location.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named
@Slf4j
public class CheckAvailability implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.info("Checking craftsmen availability -> Logic in NodeRED");
        String res = HttpClient.get("is-any-available");
        log.info("Craftsmen available? " + res);
        delegateExecution.setVariable("craftsmanAvailable", Boolean.parseBoolean(res));
    }
}
