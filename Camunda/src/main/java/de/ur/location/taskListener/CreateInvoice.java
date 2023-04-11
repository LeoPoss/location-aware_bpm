package de.ur.location.taskListener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.history.HistoricTaskInstance;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Named
@Slf4j
public class CreateInvoice implements TaskListener {
    @Inject
    HistoryService historyService;

    @Override
    public void notify(DelegateTask delegateTask) {
        Map<String, Object> variables = delegateTask.getVariables();
        String assignedCraftsmen = variables.get("assignedCraftsmen").toString();

        List<HistoricTaskInstance> relevant = historyService.createHistoricTaskInstanceQuery().list().parallelStream()
                .filter(h -> h.getProcessInstanceId().equals(delegateTask.getProcessInstanceId()))
                .filter(c -> c.getAssignee().equals(assignedCraftsmen))
                .toList();

        for (HistoricTaskInstance i : relevant) {
            log.info(i.toString());
        }

        // FIXME: Hardcoded access to tasks + Access if not finished with correct user
        Duration travelTime = Duration.ofMillis(relevant.get(0).getDurationInMillis() + relevant.get(2).getDurationInMillis());
        Duration workingTime = Duration.ofMillis(relevant.get(1).getDurationInMillis());

        String travelTimeString = String.format("%02d:%02d:%02d", travelTime.toHoursPart(), travelTime.toMinutesPart(), travelTime.toSecondsPart());
        String workTimeString = String.format("%02d:%02d:%02d", workingTime.toHoursPart(), workingTime.toMinutesPart(), workingTime.toSecondsPart());

        delegateTask.setVariableLocal("travelDuration", travelTimeString);
        delegateTask.setVariableLocal("workDuration", workTimeString);
        log.info("Travel time: " + travelTimeString);
        log.info("Work time: " + workTimeString);
    }
}

