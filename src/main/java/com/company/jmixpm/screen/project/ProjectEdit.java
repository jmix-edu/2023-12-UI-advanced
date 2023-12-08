package com.company.jmixpm.screen.project;

import com.company.jmixpm.entity.Task;
import com.company.jmixpm.entity.User;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.component.TabSheet;
import io.jmix.ui.component.Table;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.model.DataContext;
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

    private Table<Task> tasksTable;

    @Autowired
    private DataManager dataManager;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private CollectionLoader<Task> tasksDl;
    @Autowired
    private Notifications notifications;

    @Subscribe
    public void onInitEntity(final InitEntityEvent<Project> event) {
        final User user = (User) currentAuthentication.getUser();
        event.getEntity().setManager(user);
    }

    @Subscribe("tabSheet")
    public void onTabSheetSelectedTabChange(final TabSheet.SelectedTabChangeEvent event) {
        if ("tasksTab".equals(event.getSelectedTab().getName())) {
            initTasks();
        }

    }

    private void initTasks() {
        if (tasksTable != null) {
            // It means that we've already opened this tab and initialized table
            return;
        }

        tasksTable = ((Table<Task>) getWindow().getComponentNN("tasksTable"));
        ((BaseAction) tasksTable.getActionNN("create"))
                .addActionPerformedListener(this::onTasksTableCreate);
        ((BaseAction) tasksTable.getActionNN("edit"))
                .addActionPerformedListener(this::onTasksTableEdit);

        tasksDl.setParameter("project", getEditedEntity());
        tasksDl.load();

    }

//    @Subscribe
//    public void onInit(final InitEvent event) {
//        tasksTable.setEnabled(false);
//    }
//
//    @Subscribe(target = Target.DATA_CONTEXT)
//    public void onPostCommit(final DataContext.PostCommitEvent event) {
//        tasksTable.setEnabled(true);
//    }
    
    

//    @Install(to = "tasksTable.create", subject = "initializer")
//    private void tasksTableCreateInitializer(Task task) {
//        task.setProject(getEditedEntity());
//    }

    public void onTasksTableCreate(final Action.ActionPerformedEvent event) {
        Task newTask = dataManager.create(Task.class);
        newTask.setProject(getEditedEntity());

        screenBuilders.editor(tasksTable)
                .newEntity(newTask)
                .withParentDataContext(getScreenData().getDataContext())
                .show();
    }


    public void onTasksTableEdit(final Action.ActionPerformedEvent event) {
        Task selected = tasksTable.getSingleSelected();
        if (selected == null) {
            return;
        }

        screenBuilders.editor(tasksTable)
                .editEntity(selected)
                .withParentDataContext(getScreenData().getDataContext())
                .show();
        
    }

    @Subscribe(id = "tasksDc", target = Target.DATA_CONTAINER)
    public void onTasksDcCollectionChange(CollectionContainer.CollectionChangeEvent<Task> event){
        notifications.create()
                .withCaption("[tasksDc] CollectionChangeEvent")
                .withDescription(event.getChangeType() + "")
                .withType(Notifications.NotificationType.TRAY)
                .show();
    }
}