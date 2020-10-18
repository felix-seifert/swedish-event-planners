package com.felixseifert.swedisheventplanners.ui.views.newrequest;

import com.felixseifert.swedisheventplanners.backend.model.NewRequest;
import com.felixseifert.swedisheventplanners.backend.model.enums.Preference;
import com.felixseifert.swedisheventplanners.backend.model.enums.RequestStatus;
import com.felixseifert.swedisheventplanners.backend.model.enums.Role;
import com.felixseifert.swedisheventplanners.backend.service.NewRequestService;
import com.felixseifert.swedisheventplanners.ui.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "reviewed-requests", layout = MainView.class)
@PageTitle("Reviewed Event Requests | Swedish Event Planners")
@Secured(Role.ForAnnotation.SENIOR_CUSTOMER_SERVICE_OFFICER_WITH_PREFIX)
public class ReviewedRequestsGridView extends Div {

    private NewRequestService newRequestService;

    private Binder<NewRequest> binder = new Binder<>();

    private Grid<NewRequest> grid = new Grid<>();

    private TextField recordNumberTextField = new TextField("Record Number");
    private TextField clientNameTextField = new TextField("Client Name");
    private TextField clientContactTextField = new TextField("Client Contact Details");
    private TextField eventTypeTextField = new TextField("Event Type");
    private TextField preferencesTextField = new TextField("Preferences");
    private TextField fromDateTextField = new TextField("From");
    private TextField toDateTextField = new TextField("To");
    private TextField expectedAttendeesTextField = new TextField("Expected Number of Attendees");
    private NumberField expectedBudgetNumberField = new NumberField("Expected Budget");
    private TextField requestStatusTextField = new TextField("Status");
    private Button customerContactedButton = new Button();

    public ReviewedRequestsGridView(NewRequestService newRequestService) {

        this.newRequestService = newRequestService;

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.addToSecondary(createEditorLayout());
        splitLayout.addToPrimary(createGridLayout());

        grid.setItems(newRequestService.getAllNewRequestsByStatus(Set.of(RequestStatus.APPROVED,
                                                                    RequestStatus.REJECTED_BY_SCSO,
                                                                    RequestStatus.REJECTED_BY_FM,
                                                                    RequestStatus.REJECTED_BY_AM)));

        grid.asSingleSelect().addValueChangeListener(event -> {
            if(event.getValue() != null) {
                recordNumberTextField.setValue(event.getValue().getRecordNumber());
                clientNameTextField.setValue(event.getValue().getClient().getName());
                clientContactTextField.setValue(event.getValue().getClient().getContactDetails());
                eventTypeTextField.setValue(event.getValue().getEventType().getName());
                preferencesTextField.setValue(event.getValue().getPreferences().stream()
                        .map(Preference::getName).collect(Collectors.joining(", ")));
                fromDateTextField.setValue(event.getValue().getFrom().toString());
                toDateTextField.setValue(event.getValue().getTo().toString());
                expectedAttendeesTextField.setValue(event.getValue().getExpectedNumberOfAttendees() != null ?
                        event.getValue().getExpectedNumberOfAttendees().toString() : "");
                expectedBudgetNumberField.setValue(event.getValue().getExpectedBudget());
                requestStatusTextField.setValue(event.getValue().getRequestStatus().getStatus());
            }

            if (event.getValue() != null) {
                binder.setBean(event.getValue());
            }

            customerContactedButton.setVisible(true);
        });

        customerContactedButton.addClickListener(e -> {
            NewRequest requestToApprove = binder.getBean();
            if(requestToApprove.getId() == null) {
                Notification.show("An exception happened while trying to handle the request.");
                return;
            }
            if(requestToApprove.getRequestStatus() == RequestStatus.APPROVED){
                requestToApprove.setRequestStatus(RequestStatus.MEETING_ARRANGED);
            } else {
                requestToApprove.setRequestStatus(RequestStatus.REJECTION_NOTIFICATION);
            }
            newRequestService.putNewRequest(requestToApprove);
            clearForm();
            refreshGrid();
            Notification.show(String.format("Request %s archived.", requestToApprove.getRecordNumber()));
        });

        this.setHeightFull();
        this.add(splitLayout);
    }

    private Component createEditorLayout() {
        VerticalLayout editorLayout = new VerticalLayout();
        editorLayout.setId("new-request-viewer");

        customerContactedButton.setText("Customer contacted");
        customerContactedButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        customerContactedButton.setVisible(false);

        editorLayout.add(createFormLayout());
        editorLayout.add(customerContactedButton);

        return editorLayout;
    }

    private FormLayout createFormLayout() {

        recordNumberTextField.setReadOnly(true);
        clientNameTextField.setReadOnly(true);
        clientContactTextField.setReadOnly(true);
        eventTypeTextField.setReadOnly(true);
        preferencesTextField.setReadOnly(true);
        fromDateTextField.setReadOnly(true);
        toDateTextField.setReadOnly(true);
        expectedAttendeesTextField.setReadOnly(true);
        expectedBudgetNumberField.setReadOnly(true);
        requestStatusTextField.setReadOnly(true);

        return new FormLayout(recordNumberTextField, clientNameTextField, eventTypeTextField, preferencesTextField,
                fromDateTextField, toDateTextField, expectedAttendeesTextField, expectedBudgetNumberField,
                requestStatusTextField);
    }

    private Div createGridLayout() {
        Div gridLayout = new Div();
        gridLayout.setId("new-request-grid");
        gridLayout.setWidthFull();

        grid.addColumn(NewRequest::getRecordNumber).setHeader("Record Number");
        grid.addColumn(request -> request.getClient().getName()).setHeader("Client");
        grid.addColumn(request -> request.getEventType().getName()).setHeader("Event Type");
        grid.addColumn(request -> request.getFrom().toString()).setHeader("Start Date");
        grid.addColumn(NewRequest::getExpectedNumberOfAttendees).setHeader("#Attendees");
        grid.addColumn(request -> request.getRequestStatus().getStatus()).setHeader("Status");
        grid.setHeightFull();

        gridLayout.add(grid);

        return gridLayout;
    }

    private void refreshGrid() {
        grid.select(null);
        grid.setItems(newRequestService.getAllNewRequestsByStatus(Set.of(RequestStatus.APPROVED,
                RequestStatus.REJECTED_BY_SCSO,
                RequestStatus.REJECTED_BY_FM,
                RequestStatus.REJECTED_BY_AM)));    }

    private void clearForm() {
        binder.setBean(null);
        recordNumberTextField.setValue("");
        clientNameTextField.setValue("");
        eventTypeTextField.setValue("");
        preferencesTextField.setValue("");
        fromDateTextField.setValue("");
        toDateTextField.setValue("");
        expectedAttendeesTextField.setValue("");
        expectedBudgetNumberField.setValue(null);
        requestStatusTextField.setValue("");

        customerContactedButton.setVisible(false);
    }
}
