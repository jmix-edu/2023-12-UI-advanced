package com.company.jmixpm.screen.main;

import com.company.jmixpm.app.TaskService;
import com.company.jmixpm.entity.Project;
import com.company.jmixpm.entity.Task;
import com.company.jmixpm.event.TasksCounterChangedEvent;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.core.Metadata;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.ScreenTools;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.*;
import io.jmix.ui.component.mainwindow.Drawer;
import io.jmix.ui.component.mainwindow.SideMenu;
import io.jmix.ui.icon.JmixIcon;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;

@UiController("MainScreen")
@UiDescriptor("main-screen.xml")
@Route(path = "main", root = true)
public class MainScreen extends Screen implements Window.HasWorkArea {

    @Autowired
    private ScreenTools screenTools;

    @Autowired
    private AppWorkArea workArea;
    @Autowired
    private Drawer drawer;
    @Autowired
    private Button collapseDrawerButton;
    @Autowired
    private CollectionLoader<Project> projectsDl;
    @Autowired
    private CollectionLoader<Task> tasksDl;
    @Autowired
    private EntityComboBox<Project> projectSelector;
    @Autowired
    private TextField<String> nameSelector;
    @Autowired
    private DateField<LocalDateTime> dateSelector;
    @Autowired
    private TaskService taskService;
    @Autowired
    private Notifications notifications;
    @Autowired
    private MessageBundle messageBundle;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private Metadata metadata;
    @Autowired
    private SideMenu sideMenu;

    @Override
    public AppWorkArea getWorkArea() {
        return workArea;
    }

    @EventListener
    private void taskChanged(TasksCounterChangedEvent event){
        updateTasksCount();
    }

    @Subscribe("collapseDrawerButton")
    private void onCollapseDrawerButtonClick(Button.ClickEvent event) {
        drawer.toggle();
        if (drawer.isCollapsed()) {
            collapseDrawerButton.setIconFromSet(JmixIcon.CHEVRON_RIGHT);
        } else {
            collapseDrawerButton.setIconFromSet(JmixIcon.CHEVRON_LEFT);
        }
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        screenTools.openDefaultScreen(
                UiControllerUtils.getScreenContext(this).getScreens());

        screenTools.handleRedirect();
        updateTasksCount();
    }

    private void updateTasksCount() {
        long count = dataManager.getCount(new LoadContext<>(metadata.getClass(Task.class)));
        sideMenu.getMenuItemNN("Task_.browse").setBadgeText(count + "");
    }

    @Subscribe("refresh")
    public void onRefresh(final Action.ActionPerformedEvent event) {
        projectsDl.load();
        tasksDl.load();

    }

    @Subscribe("addTask")
    public void onAddTask(final Action.ActionPerformedEvent event) {
        if (projectSelector.getValue() == null
                || nameSelector.getValue() == null
                || dateSelector.getValue() == null) {
            notifications.create()
                    .withCaption(messageBundle.getMessage("validation.fieldsNotFilled.message"))
                    .withType(Notifications.NotificationType.WARNING)
                    .show();

            return;
        }

        taskService.createTask(projectSelector.getValue(),
                nameSelector.getValue(),
                dateSelector.getValue());
        tasksDl.load();

        projectSelector.setValue(null);
        nameSelector.setValue(null);
        dateSelector.setValue(null);
    }

    @Subscribe("tasksCalendar")
    public void onTasksCalendarCalendarEventClick(final Calendar.CalendarEventClickEvent<LocalDateTime> event) {
        Task task = (Task) event.getEntity();
        if (task == null) {
            return;
        }

        Screen screen = screenBuilders.editor(Task.class, this)
                .editEntity(task)
                .withOpenMode(OpenMode.DIALOG)
                .build();


        screen.addAfterCloseListener(afterCloseEvent -> {
            if (afterCloseEvent.closedWith(StandardOutcome.COMMIT)) {
                tasksDl.load();
            }
        });

        screen.show();

    }
}
