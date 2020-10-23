package com.felixseifert.swedisheventplanners.ui.views.budgetrequest;

import com.felixseifert.swedisheventplanners.backend.model.Proposal;
import com.felixseifert.swedisheventplanners.backend.model.enums.ProposalStatus;
import com.felixseifert.swedisheventplanners.backend.model.enums.Role;
import com.felixseifert.swedisheventplanners.backend.service.ProposalService;
import com.felixseifert.swedisheventplanners.ui.views.main.MainView;
import com.vaadin.flow.component.AbstractField;
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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route(value = "budget-request", layout = MainView.class)
@PageTitle("Budget Requests | Swedish Event Planners")
@Secured({Role.ForAnnotation.FINANCIAL_MANAGER_WITH_PREFIX})
public class BudgetRequestsView extends Div {

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
    private TextField productionStatusTextField = new TextField("Production Status");
    private TextField serviceStatusTextField = new TextField("Service Status");
    private TextArea decorationsTextArea = new TextArea("Decorations");
    private TextArea filmingPhotosTextArea = new TextArea("Filming/Photos");
    private TextArea postersArtWorkTextArea = new TextArea("Posters/Art Work");
    private TextArea foodDrinksTextArea = new TextArea("Food/Drinks");
    private TextArea musicTextArea = new TextArea("Music");
    private TextArea computerRelatedIssuesTextArea = new TextArea("Computer-related Issues");

    private Button confirmButton = new Button();

    public BudgetRequestsView(ProposalService proposalService) {

        this.proposalService = proposalService;
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.addToPrimary(createGridLayout());
        splitLayout.addToSecondary(createEditorLayout());

        grid.setItems(getItems());

        grid.asSingleSelect().addValueChangeListener(this::gridSingleSelectListener);

        confirmButton.addClickListener(this::confirmListener);

        this.setHeightFull();
        this.add(splitLayout);
    }

    private void gridSingleSelectListener(AbstractField.ComponentValueChangeEvent<Grid<Proposal>, Proposal> event) {
        if (event.getValue() != null) {
            recordNumberTextField.setValue(event.getValue().getRecordNumber());
            clientNameTextField.setValue(event.getValue().getClient().getName());
            clientContactTextField.setValue(event.getValue().getClient().getContactDetails());
            eventTypeTextField.setValue(event.getValue().getEventType().getName());
            fromDateTextField.setValue(event.getValue().getFrom().toString());
            toDateTextField.setValue(event.getValue().getTo().toString());
            productionStatusTextField.setValue(event.getValue().getProductionProposalStatus().toString());
            serviceStatusTextField.setValue(event.getValue().getServiceProposalStatus().toString());
            expectedAttendeesTextField.setValue(event.getValue().getExpectedNumberOfAttendees() != null ?
                    event.getValue().getExpectedNumberOfAttendees().toString() : "");
            expectedBudgetNumberField.setValue(event.getValue().getExpectedBudget());
            decorationsTextArea.setValue(event.getValue().getDecorations());
            filmingPhotosTextArea.setValue(event.getValue().getFilmingPhotos());
            postersArtWorkTextArea.setValue(event.getValue().getPostersArtWork());
            foodDrinksTextArea.setValue(event.getValue().getFoodDrinks());
            musicTextArea.setValue(event.getValue().getMusic());
            computerRelatedIssuesTextArea.setValue(event.getValue().getComputerRelatedIssues());

            binder.setBean(event.getValue());

            enableButtons(true);
        }
    }

    private void confirmListener(ClickEvent<Button> e) {
        Proposal proposal = binder.getBean();
        if(proposal.getId() == null) {
            Notification.show("An exception happened while trying to handle the budget request.");
            return;
        }
        if(ProposalStatus.EXTRA_BUDGET_REQUESTED.equals(proposal.getProductionProposalStatus())) {
            proposal.setProductionProposalStatus(ProposalStatus.BUDGET_REQUEST_HANDLED);
        }
        if(ProposalStatus.EXTRA_BUDGET_REQUESTED.equals(proposal.getServiceProposalStatus())) {
            proposal.setServiceProposalStatus(ProposalStatus.BUDGET_REQUEST_HANDLED);
        }
        proposalService.putProposal(proposal);
        clearForm();
        refreshGrid();
        Notification.show(String.format("Budget request for %s handled.", proposal.getRecordNumber()));
    }

    private Div createGridLayout() {
        Div gridLayout = new Div();
        gridLayout.setId("hr-grid");
        gridLayout.setWidthFull();

        grid.addColumn(Proposal::getRecordNumber).setHeader("Record Number");
        grid.addColumn(proposal -> proposal.getClient().getName()).setHeader("Client");
        grid.addColumn(proposal -> proposal.getEventType().getName()).setHeader("Event Type");
        grid.addColumn(proposal -> proposal.getFrom().toString()).setHeader("Start Date");
        grid.addColumn(proposal -> proposal.getProductionProposalStatus().toString())
                .setHeader("Production Status");
        grid.addColumn(proposal -> proposal.getServiceProposalStatus().toString())
                .setHeader("Service Status");
        grid.setHeightFull();

        gridLayout.add(grid);

        return gridLayout;
    }

    private Component createEditorLayout() {
        HorizontalLayout editorLayout = new HorizontalLayout();
        editorLayout.setId("hr-viewer");

        editorLayout.add(createFormLayout());

        return editorLayout;
    }

    private FormLayout createFormLayout() {

        recordNumberTextField.setReadOnly(true);
        clientNameTextField.setReadOnly(true);
        clientContactTextField.setReadOnly(true);
        eventTypeTextField.setReadOnly(true);
        fromDateTextField.setReadOnly(true);
        toDateTextField.setReadOnly(true);
        productionStatusTextField.setReadOnly(true);
        serviceStatusTextField.setReadOnly(true);
        expectedAttendeesTextField.setReadOnly(true);
        expectedBudgetNumberField.setReadOnly(true);
        decorationsTextArea.setReadOnly(true);
        filmingPhotosTextArea.setReadOnly(true);
        postersArtWorkTextArea.setReadOnly(true);
        foodDrinksTextArea.setReadOnly(true);
        musicTextArea.setReadOnly(true);
        computerRelatedIssuesTextArea.setReadOnly(true);

        confirmButton.setText("Budget request resolved");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        enableButtons(false);

        return new FormLayout(recordNumberTextField, clientNameTextField, eventTypeTextField, new Label(),
                fromDateTextField, toDateTextField, productionStatusTextField, serviceStatusTextField,
                expectedAttendeesTextField, expectedBudgetNumberField, decorationsTextArea, filmingPhotosTextArea,
                postersArtWorkTextArea, foodDrinksTextArea, musicTextArea, computerRelatedIssuesTextArea,
                confirmButton);
    }

    private void enableButtons(boolean enableButtons) {
        confirmButton.setEnabled(enableButtons);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.setItems(getItems());
    }

    private List<Proposal> getItems() {
        return Stream.concat(
                proposalService.getAllProposalsByProductionStatus(Set.of(ProposalStatus.EXTRA_BUDGET_REQUESTED)).stream(),
                proposalService.getAllProposalsByServiceStatus(Set.of(ProposalStatus.EXTRA_BUDGET_REQUESTED)).stream())
                .distinct().collect(Collectors.toList());
    }

    private void clearForm() {
        binder.setBean(null);
        recordNumberTextField.clear();
        clientNameTextField.clear();
        eventTypeTextField.clear();
        fromDateTextField.clear();
        toDateTextField.clear();
        productionStatusTextField.clear();
        serviceStatusTextField.clear();
        expectedAttendeesTextField.clear();
        expectedBudgetNumberField.clear();
        decorationsTextArea.clear();
        filmingPhotosTextArea.clear();
        postersArtWorkTextArea.clear();
        foodDrinksTextArea.clear();
        musicTextArea.clear();
        computerRelatedIssuesTextArea.clear();

        enableButtons(false);
    }
}
