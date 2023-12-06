package com.company.jmixpm.screen.project;

import com.company.jmixpm.entity.User;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.ui.screen.*;
import com.company.jmixpm.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Project.edit")
@UiDescriptor("project-edit.xml")

@EditedEntityContainer("projectDc")
//@PrimaryEditorScreen(Project.class)
public class ProjectEdit extends StandardEditor<Project> {

    @Autowired
    private CurrentAuthentication currentAuthentication;

    @Subscribe
    public void onInitEntity(final InitEntityEvent<Project> event) {
        final User user = (User) currentAuthentication.getUser();
        event.getEntity().setManager(user);
    }
}