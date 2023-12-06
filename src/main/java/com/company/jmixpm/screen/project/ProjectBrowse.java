package com.company.jmixpm.screen.project;

import com.company.jmixpm.entity.User;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.ui.action.Action;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.company.jmixpm.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@UiController("Project.browse")
@UiDescriptor("project-browse.xml")
//@MultipleOpen
//@DialogMode(forceDialog = false)
@LookupComponent("projectsTable")
//@PrimaryLookupScreen(Project.class)
public class ProjectBrowse extends StandardLookup<Project> {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private CollectionLoader<Project> projectsDl;

    @Subscribe("projectsTable.generateData")
    public void onProjectsTableGenerateData(final Action.ActionPerformedEvent event) {
        List<Project> projects = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            Project project = dataManager.create(Project.class);
            project.setManager((User) currentAuthentication.getUser());
            project.setName("Generated №" + i);
            projects.add(project);
        }
        dataManager.save(projects.toArray());
        projectsDl.load();
    }
}