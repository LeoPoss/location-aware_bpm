package de.ur.location.execution;

import com.google.gson.Gson;
import de.ur.location.HttpClient;
import de.ur.location.dto.CraftsmanDistance;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;
import java.util.Comparator;
import java.util.List;

import static de.ur.location.Application.CRAFTSMANDISTANCES;


@Named
@Slf4j
public class AssignCraftsman implements JavaDelegate {
    static Gson gson = new Gson();

    @Override
    public void execute(DelegateExecution execution) {
        String customerId = execution.getVariables().get("customerId").toString();

        String res = HttpClient.get("craftsmen-assignment", customerId);
        List<CraftsmanDistance> craftsmanDistances = gson.fromJson(res, CRAFTSMANDISTANCES);

        // TODO  Do we really have to sort the list again?
        //  Can the error even happen, already checked in NodeRED -> Where is the logic
        CraftsmanDistance candidate = craftsmanDistances.parallelStream()
                .min(Comparator.comparingDouble(CraftsmanDistance::getDistanceToCustomer))
                .orElseThrow(() -> new BpmnError("Looks like all craftsmen are busy"));

        execution.setProcessBusinessKey(candidate.getId());
        execution.setVariable("currentDistanceToCustomer", candidate.getDistanceToCustomer());
        execution.setVariable("assignedCraftsmen", candidate.getId());
        log.info("Craftsman with id {} is assigned to customer {}", candidate.getId(), customerId);
    }
}
