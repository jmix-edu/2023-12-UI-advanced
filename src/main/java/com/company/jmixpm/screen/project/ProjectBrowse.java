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
}