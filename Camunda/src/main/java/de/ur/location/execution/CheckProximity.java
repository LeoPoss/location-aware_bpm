package de.ur.location.execution;

import com.google.gson.Gson;
import de.ur.location.HttpClient;
import de.ur.location.dto.CraftsmanDistance;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;
import java.util.List;

import static de.ur.location.Application.CRAFTSMANDISTANCES;

@Named
@Slf4j
public class CheckProximity implements JavaDelegate {
    private static final long MAX_DISTANCE_TO_CUSTOMER = 100_000;
    static Gson gson = new Gson();

    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.info("Checking distance of all craftsmen -> Logic in Camunda");
        String customerId = delegateExecution.getVariables().get("customerId").toString();

        String res = HttpClient.get("craftsmen-distances", customerId);
        List<CraftsmanDistance> craftsmanDistances = gson.fromJson(res, CRAFTSMANDISTANCES);

        boolean anyCloseCraftsmen = craftsmanDistances.parallelStream().anyMatch(d -> d.getDistanceToCustomer() < MAX_DISTANCE_TO_CUSTOMER);
        log.info("Any craftsmen near?: " + anyCloseCraftsmen);
        delegateExecution.setVariable("craftsmanAvailable", anyCloseCraftsmen);
    }
}
