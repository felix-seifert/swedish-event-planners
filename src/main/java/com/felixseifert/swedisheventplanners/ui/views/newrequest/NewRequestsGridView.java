package com.felixseifert.swedisheventplanners.ui.views.newrequest;

import com.felixseifert.swedisheventplanners.backend.model.NewRequest;
import com.felixseifert.swedisheventplanners.backend.model.enums.Preference;
import com.felixseifert.swedisheventplanners.backend.model.enums.Role;
import com.felixseifert.swedisheventplanners.backend.service.NewRequestService;
import com.felixseifert.swedisheventplanners.ui.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.stream.Collectors;

@Route(value = "new-request", layout = MainView.class)
@PageTitle("New Event Requests | Swedish Event Planners")
@Secured(Role.ForAnnotation.SENIOR_CUSTOMER_SERVICE_OFFICER_WITH_PREFIX)
public class NewRequestsGridView extends Div {

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

    public NewRequestsGridView(NewRequestService newRequestService) {

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.addToSecondary(createEditorLayout());
        splitLayout.addToPrimary(createGridLayout());

        grid.setItems(newRequestService.getAllNewRequests());

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
            }
        });

        this.setHeightFull();
        this.add(splitLayout);
    }

    private Component createEditorLayout() {
        HorizontalLayout editorLayout = new HorizontalLayout();
        editorLayout.setId("new-request-viewer");

        editorLayout.add(createFormLayout());

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

        return new FormLayout(recordNumberTextField, clientNameTextField, eventTypeTextField, preferencesTextField,
                fromDateTextField, toDateTextField, expectedAttendeesTextField, expectedBudgetNumberField);
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
        grid.setHeightFull();

        gridLayout.add(grid);

        return gridLayout;
    }
}
