package de.ur.location.execution;

import de.ur.location.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;
import java.util.Map;


@Named
@Slf4j
public class SubprocessEnd implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.info("Craftsmen arrived back home :)");
        Map<String, Object> variables = delegateExecution.getVariables();
        String assignedCraftsmen = variables.get("assignedCraftsmen").toString();
        HttpClient.post("job-finished", assignedCraftsmen);
    }
}
