package com.company.jmixpm.screen.project;

import com.company.jmixpm.entity.Project;
import io.jmix.core.FetchPlan;
import io.jmix.core.FetchPlans;
import io.jmix.core.Messages;
import io.jmix.ui.Facets;
import io.jmix.ui.UiComponents;
import io.jmix.ui.UiScreenProperties;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.component.DataLoadCoordinator;
import io.jmix.ui.component.Filter;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.component.data.table.ContainerGroupTableItems;
import io.jmix.ui.icon.JmixIcon;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.model.DataComponents;
import io.jmix.ui.model.DataContext;
import io.jmix.ui.screen.*;
import io.jmix.ui.settings.facet.ScreenSettingsFacet;
import org.springframework.beans.factory.annotation.Autowired;

@PrimaryLookupScreen(Project.class)
@DialogMode(height = "600px", width = "800px")
@UiController("ProgrammaticProjectBrowse")
public class ProgrammaticProjectBrowse extends StandardLookup<Project> {
    @Autowired
    private MessageBundle messageBundle;
    @Autowired
    private DataComponents dataComponents;

    private CollectionContainer<Project> projectsDc;
    private CollectionLoader<Project> projectsDl;
    private GroupTable<Project> projectsTable;
    @Autowired
    private FetchPlans fetchPlans;
    @Autowired
    private Facets facets;
    @Autowired
    private Messages messages;
    @Autowired
    private UiScreenProperties uiScreenProperties;
    @Autowired
    private UiComponents uiComponents;

    @Subscribe
    public void onInit(final InitEvent event) {
        getWindow().setCaption(messageBundle.getMessage("programmaticProjectBrowse.caption"));

        initData();
        initFacets();
        initActions();
        initLayout();

        getWindow().setFocusComponent(projectsTable.getId());
    }

    private void initData() {
        DataContext dataContext = dataComponents.createDataContext();
        getScreenData().setDataContext(dataContext);

        projectsDc = dataComponents.createCollectionContainer(Project.class);
        getScreenData().registerContainer("projectsDc", projectsDc);

        projectsDl = dataComponents.createCollectionLoader();
        projectsDl.setContainer(projectsDc);
        projectsDl.setDataContext(dataContext);
        projectsDl.setQuery("select e from Project e");
        projectsDl.setFetchPlan(createProjectsFetchPlan());
        getScreenData().registerLoader("projectsDl", projectsDl);

    }

    private FetchPlan createProjectsFetchPlan() {
        return fetchPlans.builder(Project.class)
                .addFetchPlan(FetchPlan.BASE)
                .add("manager", fetchPlanBuilder ->
                        fetchPlanBuilder.addFetchPlan(FetchPlan.BASE))
                .build();
    }

    private void initFacets() {
        DataLoadCoordinator dataLoadCoordinator = facets.create(DataLoadCoordinator.class);
        getWindow().addFacet(dataLoadCoordinator);
        dataLoadCoordinator.configureAutomatically();

        ScreenSettingsFacet screenSettingsFacet = facets.create(ScreenSettingsFacet.class);
        getWindow().addFacet(screenSettingsFacet);
        screenSettingsFacet.setAuto(true);

    }

    private void initActions() {
        BaseAction lookupSelectAction = new BaseAction(LOOKUP_SELECT_ACTION_ID)
                .withCaption(messages.getMessage("actions.Select"))
                .withIcon(JmixIcon.LOOKUP_OK.source())
                .withPrimary(true)
                .withShortcut(uiScreenProperties.getCommitShortcut())
                .withHandler(this::select);
        getWindow().addAction(lookupSelectAction);

        BaseAction lookupCancelAction = new BaseAction(LOOKUP_CANCEL_ACTION_ID)
                .withCaption(messages.getMessage("actions.Cancel"))
                .withIcon(JmixIcon.LOOKUP_CANCEL.source())
                .withHandler(this::cancel);
        getWindow().addAction(lookupCancelAction);

    }

    private void initLayout() {
        initFilter();
        initTable();
        initWindowButtonsPanel();

        getWindow().setSpacing(true);
        getWindow().expand(projectsTable);
    }

    private void initFilter() {
        Filter filter = uiComponents.create(Filter.class);
        filter.setId("filter");
        filter.setDataLoader(projectsDl);
        filter.loadConfigurationsAndApplyDefault();

        getWindow().add(filter);

    }

    private void initTable() {
        projectsTable = uiComponents.create(GroupTable.of(Project.class));
        projectsTable.setId("projectsTable");
        projectsTable.setWidthFull();

        initTableActions();
        initTableColumns();
        initTableButtonsPanel();
        initTablePagination();

        projectsTable.setItems(new ContainerGroupTableItems<>(projectsDc));

        getWindow().add(projectsTable);
    }

    private void initTableActions() {
    }

    private void initTableColumns() {

    }

    private void initTableButtonsPanel() {

    }


    private void initTablePagination() {

    }

    private void initWindowButtonsPanel() {
    }


}
