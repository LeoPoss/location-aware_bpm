package de.ur.location;

import com.google.gson.reflect.TypeToken;
import de.ur.location.dto.CraftsmanDistance;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
public class Application {
  public static void main(String... args) {
    SpringApplication.run(Application.class, args);
  }

  public static void updateCustomerNodeRed(DelegateTask delegateTask) {
    String assignedCraftsmen = delegateTask.getExecution().getProcessBusinessKey();
    delegateTask.setAssignee(assignedCraftsmen);
    String customerId = delegateTask.getVariables().get("customerId").toString();
    HttpClient.post("update-customer", delegateTask.getId(), Map.of("customerId", customerId, "assignedCraftsmen", assignedCraftsmen));
  }

  public static final Type CRAFTSMANDISTANCES = new TypeToken<ArrayList<CraftsmanDistance>>() {}.getType();
}