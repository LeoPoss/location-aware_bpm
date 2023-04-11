package de.ur.location.execution;

import de.ur.location.HttpClient;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;
import java.util.Map;

@Named
public class CheckOrder implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        Map<String, Object> variables = delegateExecution.getVariables();
        System.out.println(variables.toString());
        String latitude = variables.get("latitude").toString();
        String longitude = variables.get("longitude").toString();
        String customerName = variables.get("customerName").toString();
        String customerId = variables.get("customerId").toString();
        HttpClient.post("new-order", null, Map.of("customerId", customerId, "latitude", latitude, "longitude", longitude, "customerName", customerName));
    }
}