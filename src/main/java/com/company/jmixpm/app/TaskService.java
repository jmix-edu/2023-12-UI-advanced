package com.company.jmixpm.app;

import com.company.jmixpm.entity.Project;
import com.company.jmixpm.entity.Task;
import com.company.jmixpm.entity.User;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TaskService {

    private final CurrentAuthentication currentAuthentication;
    @Autowired
    private DataManager dataManager;

    public TaskService(CurrentAuthentication currentAuthentication) {
        this.currentAuthentication = currentAuthentication;
    }

    public User findLeastBusyUser() {
        return dataManager.loadValues("select u, count(t.id) " +
                        "from User u left outer join Task_ t " +
                        "on u = t.assignee " +
                        "group by u order by count(t.id)")
                .properties("user", "tasks")
                .list().stream()
                .map(e -> e.<User>getValue("user"))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public void createTask(Project project, String taskName, LocalDateTime startDare) {
        Task task = dataManager.create(Task.class);
        task.setProject(project);
        task.setName(taskName);
        task.setStartDate(startDare);
        final User user = (User) currentAuthentication.getUser();
        task.setAssignee(user);

        dataManager.save(task);

    }
}