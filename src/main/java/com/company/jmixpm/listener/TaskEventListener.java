package com.company.jmixpm.listener;

import com.company.jmixpm.entity.Task;
import com.company.jmixpm.event.TasksCounterChangedEvent;
import io.jmix.core.event.EntityChangedEvent;
import io.jmix.ui.UiEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TaskEventListener {

    private final UiEventPublisher uiEventPublisher;

    public TaskEventListener(UiEventPublisher uiEventPublisher) {
        this.uiEventPublisher = uiEventPublisher;
    }

    @TransactionalEventListener
    public void onTaskChangedAfterCommit(final EntityChangedEvent<Task> event) {
        EntityChangedEvent.Type type = event.getType();
        if (type == EntityChangedEvent.Type.CREATED || type == EntityChangedEvent.Type.DELETED) {
            uiEventPublisher.publishEvent(new TasksCounterChangedEvent(this));
        }
    }
}