package com.productivity.backend.task.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TaskEventListener {

    private static final Logger log = LoggerFactory.getLogger(TaskEventListener.class);

    @EventListener
    public void onTaskCompleted(TaskCompletedEvent event) {
        log.info("Task completed: '{}' by user {}", event.taskTitle(), event.userId());
    }
}
