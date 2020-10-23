package com.felixseifert.swedisheventplanners.ui.views.proposal;

import com.felixseifert.swedisheventplanners.backend.model.Proposal;
import com.felixseifert.swedisheventplanners.backend.model.enums.ProposalStatus;
import com.felixseifert.swedisheventplanners.backend.model.enums.Role;
import com.felixseifert.swedisheventplanners.backend.service.ProposalService;
import com.felixseifert.swedisheventplanners.ui.views.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.Set;

@Route(value = "proposals/service-subteam", layout = MainView.class)
@PageTitle("Sub-team Proposals | Swedish Event Planners")
@Secured({Role.ForAnnotation.SERVICES_SUB_TEAM_WITH_PREFIX})
public class ServiceSubTeamProposalsGridView extends Div {

    private ProposalService proposalService;

    private Binder<Proposal> binder = new Binder<>();

    private Grid<Proposal> grid = new Grid<>();

    private TextField recordNumberTextField = new TextField("Record Number");
    private TextField clientNameTextField = new TextField("Client Name");
    private TextField clientContactTextField = new TextField("Client Contact Details");
    private TextField eventTypeTextField = new TextField("Event Type");
    private TextField fromDateTextField = new TextField("From");
    private TextField toDateTextField = new TextField("To");
    private TextField expectedAttendeesTextField = new TextField("Expected Number of Attendees");
    private NumberField expectedBudgetNumberField = new NumberField("Expected Budget");
    private TextField serviceStatusTextField = new TextField("Service Status");
    private TextArea foodDrinksTextArea = new TextArea("Food/Drinks");

    private Button readyButton = new Button();
    private Button extraBudgetButton = new Button();

    public ServiceSubTeamProposalsGridView(ProposalService proposalService) {

        this.proposalService = proposalService;

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.addToSecondary(createEditorLayout());
        splitLayout.addToPrimary(createGridLayout());

        grid.setItems(proposalService.getAllProposalsByServiceStatus(Set.of(ProposalStatus.UNDER_REVIEW_BY_SUBTEAMS)));


        grid.asSingleSelect().addValueChangeListener(event -> {
            if(event.getValue() != null) {
                recordNumberTextField.setValue(event.getValue().getRecordNumber());
                clientNameTextField.setValue(event.getValue().getClient().getName());
                clientContactTextField.setValue(event.getValue().getClient().getContactDetails());
                eventTypeTextField.setValue(event.getValue().getEventType().getName());
                fromDateTextField.setValue(event.getValue().getFrom().toString());
                toDateTextField.setValue(event.getValue().getTo().toString());
                serviceStatusTextField.setValue(event.getValue().getServiceProposalStatus().toString());
                expectedAttendeesTextField.setValue(event.getValue().getExpectedNumberOfAttendees() != null ?
                        event.getValue().getExpectedNumberOfAttendees().toString() : "");
                expectedBudgetNumberField.setValue(event.getValue().getExpectedBudget());
                foodDrinksTextArea.setValue(event.getValue().getFoodDrinks());

                binder.setBean(event.getValue());

                showButtons();
            }
        });

        readyButton.addClickListener(this::readyButtonListener);

        extraBudgetButton.addClickListener(this::extraBudgetListener);

        this.setHeightFull();
        this.add(splitLayout);
    }

    private void readyButtonListener(ClickEvent<Button> e) {
        Proposal proposalToForward = binder.getBean();
        if(proposalToForward.getId() == null) {
            Notification.show("An exception happened while trying to finalize the proposal.");
            return;
        }
        proposalToForward.setServiceProposalStatus(ProposalStatus.UNDER_REVIEW_BY_MANAGER);
        proposalService.putProposal(proposalToForward);
        clearForm();
        refreshGrid();
        Notification.show(String.format("Proposal %s approved.", proposalToForward.getRecordNumber()));
    }

    private void extraBudgetListener(ClickEvent<Button> e) {
        Proposal extraStaffProposal = binder.getBean();
        if(extraStaffProposal.getId() == null) {
            Notification.show("An exception happened while trying to request extra budget.");
            return;
        }
        extraStaffProposal.setServiceProposalStatus(ProposalStatus.EXTRA_BUDGET_REQUESTED_BY_SUBTEAM);
        proposalService.putProposal(extraStaffProposal);
        clearForm();
        refreshGrid();
        Notification.show(String.format("Extra budget requested for %s.", extraStaffProposal.getRecordNumber()));
    }

    private Component createEditorLayout() {
        HorizontalLayout editorLayout = new HorizontalLayout();
        editorLayout.setId("proposal-viewer");

        readyButton.setText("Ready");
        readyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        extraBudgetButton.setText("Request extra budget");
        extraBudgetButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        hideButtons();

        editorLayout.add(createFormLayout());

        return editorLayout;
    }

    private void showButtons() {
        readyButton.setEnabled(true);
        extraBudgetButton.setEnabled(true);
    }

    private void hideButtons() {
        readyButton.setEnabled(false);
        extraBudgetButton.setEnabled(false);
    }

    private FormLayout createFormLayout() {

        recordNumberTextField.setReadOnly(true);
        clientNameTextField.setReadOnly(true);
        clientContactTextField.setReadOnly(true);
        eventTypeTextField.setReadOnly(true);
        fromDateTextField.setReadOnly(true);
        toDateTextField.setReadOnly(true);
        serviceStatusTextField.setReadOnly(true);
        expectedAttendeesTextField.setReadOnly(true);
        expectedBudgetNumberField.setReadOnly(true);
        foodDrinksTextArea.setReadOnly(true);

        return new FormLayout(recordNumberTextField, clientNameTextField, eventTypeTextField, serviceStatusTextField,
                fromDateTextField, toDateTextField, expectedAttendeesTextField, expectedBudgetNumberField,
                foodDrinksTextArea, new Label(), readyButton, extraBudgetButton);
    }

    private Div createGridLayout() {
        Div gridLayout = new Div();
        gridLayout.setId("proposal-grid");
        gridLayout.setWidthFull();

        grid.addColumn(Proposal::getRecordNumber).setHeader("Record Number");
        grid.addColumn(proposal -> proposal.getClient().getName()).setHeader("Client");
        grid.addColumn(proposal -> proposal.getEventType().getName()).setHeader("Event Type");
        grid.addColumn(proposal -> proposal.getFrom().toString()).setHeader("Start Date");
        grid.addColumn(proposal -> proposal.getServiceProposalStatus().toString())
                .setHeader("Service Status");
        grid.setHeightFull();

        gridLayout.add(grid);

        return gridLayout;
    }

    private void refreshGrid() {
        grid.select(null);
        grid.setItems(proposalService.getAllProposalsByServiceStatus(Set.of(ProposalStatus.UNDER_REVIEW_BY_SUBTEAMS)));
    }

    private void clearForm() {
        binder.setBean(null);
        recordNumberTextField.clear();
        clientNameTextField.clear();
        eventTypeTextField.clear();
        fromDateTextField.clear();
        toDateTextField.clear();
        serviceStatusTextField.clear();
        expectedAttendeesTextField.clear();
        expectedBudgetNumberField.clear();
        foodDrinksTextArea.clear();

        hideButtons();
    }
}
