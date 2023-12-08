package com.company.jmixpm.screen.task;

import io.jmix.ui.screen.*;
import com.company.jmixpm.entity.Task;

@UiController("Task_.browse")
@UiDescriptor("task-browse.xml")
@LookupComponent("tasksTable")
public class TaskBrowse extends StandardLookup<Task> {
    @Install(to = "tasksTable", subject = "styleProvider")
    private String tasksTableStyleProvider(final Task entity, final String property) {
        if (entity.getEstimatedEfforts() == null
                && ("estimatedEfforts".equals(property) || property == null)) {
            return "no-estimated-efforts";
        }
        return null;
    }
}