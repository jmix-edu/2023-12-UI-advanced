package com.company.jmixpm.event;

import org.springframework.context.ApplicationEvent;

public class TasksCounterChangedEvent extends ApplicationEvent {
    public TasksCounterChangedEvent(Object source) {
        super(source);
    }
}
