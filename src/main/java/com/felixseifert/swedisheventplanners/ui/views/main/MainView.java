package com.felixseifert.swedisheventplanners.ui.views.main;

import com.felixseifert.swedisheventplanners.security.SecurityUtils;
import com.felixseifert.swedisheventplanners.ui.views.about.AboutView;
import com.felixseifert.swedisheventplanners.ui.views.cardlist.CardListView;
import com.felixseifert.swedisheventplanners.ui.views.client.ClientView;
import com.felixseifert.swedisheventplanners.ui.views.helloworld.HelloWorldView;
import com.felixseifert.swedisheventplanners.ui.views.masterdetail.MasterDetailView;
import com.felixseifert.swedisheventplanners.ui.views.newrequest.CreateNewRequestView;
import com.felixseifert.swedisheventplanners.ui.views.newrequest.NewRequestsGridView;
import com.felixseifert.swedisheventplanners.ui.views.personform.PersonFormView;
import com.felixseifert.swedisheventplanners.ui.views.proposal.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
@JsModule("./styles/shared-styles.js")
@CssImport("./styles/views/main/main-view.css")
//@PWA(name = "Swedish Event Planners", shortName = "SEP", enableInstallPrompt = false)
public class MainView extends AppLayout {

    private final Tabs menu;
    private H1 viewTitle;

    public MainView() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.setPadding(true);

        layout.add(new DrawerToggle());

        viewTitle = new H1();
        layout.add(viewTitle);
        layout.expand(viewTitle);

        Anchor logout = new Anchor("logout", "Log out ");
        layout.add(logout);

        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "Swedish Event Planners logo"));
        logoLayout.add(new H1("Swedish Event Planners"));
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {

        final List<Tab> tabs = new ArrayList<>();

        if(SecurityUtils.isAccessGranted(CreateNewRequestView.class)) {
            tabs.add(createTab("Create New Event Request", CreateNewRequestView.class));
        }
        if(SecurityUtils.isAccessGranted(CreateProposalView.class)) {
            tabs.add(createTab("Create Proposal", CreateProposalView.class));
        }
        if(SecurityUtils.isAccessGranted(NewRequestsGridView.class)) {
            tabs.add(createTab("View New Event Requests", NewRequestsGridView.class));
        }
        if(SecurityUtils.isAccessGranted(ProposalsGridView.class)) {
            tabs.add(createTab("Proposals", ProposalsGridView.class));
        }
        if(SecurityUtils.isAccessGranted(ProductionManagerProposalsGridView.class)) {
            tabs.add(createTab("Your proposals", ProductionManagerProposalsGridView.class));
        }
        if(SecurityUtils.isAccessGranted(ProductionSubTeamProposalsGridView.class)) {
            tabs.add(createTab("Production sub-team proposals", ProductionSubTeamProposalsGridView.class));
        }
        if(SecurityUtils.isAccessGranted(ClosedProposalsGridView.class)) {
            tabs.add(createTab("Closed proposals", ClosedProposalsGridView.class));
        }
        if(SecurityUtils.isAccessGranted(ClientView.class)) {
            tabs.add(createTab("Clients", ClientView.class));
        }
        tabs.add(createTab("Hello World", HelloWorldView.class));
        tabs.add(createTab("About", AboutView.class));
        if(SecurityUtils.isAccessGranted(MasterDetailView.class)) {
            tabs.add(createTab("Master-Detail", MasterDetailView.class));
        }
        tabs.add(createTab("Card List", CardListView.class));
        tabs.add(createTab("Person Form", PersonFormView.class));

        return tabs.toArray(new Tab[tabs.size()]);
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren()
                .filter(tab -> ComponentUtil.getData(tab, Class.class)
                        .equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}
