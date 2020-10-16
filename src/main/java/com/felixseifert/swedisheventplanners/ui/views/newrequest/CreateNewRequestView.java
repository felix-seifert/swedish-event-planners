package com.felixseifert.swedisheventplanners.ui.views.newrequest;

import com.felixseifert.swedisheventplanners.backend.model.Client;
import com.felixseifert.swedisheventplanners.backend.model.NewRequest;
import com.felixseifert.swedisheventplanners.backend.model.enums.EventType;
import com.felixseifert.swedisheventplanners.backend.model.enums.Preference;
import com.felixseifert.swedisheventplanners.backend.model.enums.Role;
import com.felixseifert.swedisheventplanners.backend.service.ClientService;
import com.felixseifert.swedisheventplanners.backend.service.NewRequestService;
import com.felixseifert.swedisheventplanners.ui.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.gatanaso.MultiselectComboBox;

@Route(value = "new-requests/create", layout = MainView.class)
@PageTitle("Create New Request | Swedish Event Planners")
@Secured({Role.ForAnnotation.CUSTOMER_SERVICE_OFFICER_WITH_PREFIX,
        Role.ForAnnotation.SENIOR_CUSTOMER_SERVICE_OFFICER_WITH_PREFIX})
public class CreateNewRequestView extends VerticalLayout {

    private NewRequestService newRequestService;

    private ClientService clientService;

    private TextField recordNumberTextField = new TextField("Record Number");
    private ComboBox<Client> clientComboBox = new ComboBox<>("Client");
    private ComboBox<EventType> eventTypeComboBox = new ComboBox<>("Event Type");
    private MultiselectComboBox<Preference> preferencesMultiComboBox = new MultiselectComboBox<>("Preferences");
    private DateTimePicker fromDateTimePicker = new DateTimePicker("From");
    private DateTimePicker toDateTimePicker = new DateTimePicker("To");
    private TextField expectedAttendeesTextField = new TextField("Expected Number of Attendees");
    private NumberField expectedBudgetNumberField = new NumberField("Expected Budget");

    private Button saveButton = new Button("Save");
    private Button clearButton = new Button("Clear Form");

    private Binder<NewRequest> binder = new Binder<>();

    public CreateNewRequestView(NewRequestService newRequestService, ClientService clientService) {

        this.newRequestService = newRequestService;
        this.clientService = clientService;

        this.setId("create-new-request-view");

        add(new Label("Create a new request when a client phones. The client has to be registered."));

        this.add(createFormLayout());
        this.add(createButtonLayout());

        binder.setBean(new NewRequest());
        bindFields();
        addButtonFunctionality();

        binder.addStatusChangeListener(status -> saveButton.setEnabled(true));
        saveButton.setEnabled(false);
    }

    private FormLayout createFormLayout() {

        recordNumberTextField.setMinLength(10);
        recordNumberTextField.setMaxLength(10);

        clientComboBox.setItemLabelGenerator(Client::getName);
        clientComboBox.setItems(clientService.getAllClients());

        eventTypeComboBox.setItemLabelGenerator(EventType::getName);
        eventTypeComboBox.setItems(EventType.values());

        preferencesMultiComboBox.setItemLabelGenerator(Preference::getName);
        preferencesMultiComboBox.setItems(Preference.values());

        fromDateTimePicker.setDatePlaceholder("Date");
        fromDateTimePicker.setTimePlaceholder("Time");
        fromDateTimePicker.addValueChangeListener(e -> {
            if(e.getValue() != null) toDateTimePicker.setMin(e.getValue());
        });
        toDateTimePicker.setDatePlaceholder("Date");
        toDateTimePicker.setTimePlaceholder("Time");

        expectedAttendeesTextField.setPrefixComponent(new Span("#"));

        expectedBudgetNumberField.setStep(0.01);
        expectedBudgetNumberField.setMin(0);
        expectedBudgetNumberField.setPrefixComponent(new Span("€"));

        return new FormLayout(recordNumberTextField, clientComboBox, eventTypeComboBox, preferencesMultiComboBox,
                fromDateTimePicker, toDateTimePicker, expectedAttendeesTextField, expectedBudgetNumberField);
    }

    private Component createButtonLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return new HorizontalLayout(saveButton, clearButton);
    }

    private void bindFields() {
        binder.forField(recordNumberTextField).asRequired("Enter record number")
                .withValidator(rn -> rn.length() == 10, "Every record number has 10 digits")
                .bind(NewRequest::getRecordNumber, NewRequest::setRecordNumber);
        binder.forField(clientComboBox).asRequired("Select client")
                .bind(NewRequest::getClient, NewRequest::setClient);
        binder.forField(eventTypeComboBox).asRequired("Select type of event")
                .bind(NewRequest::getEventType, NewRequest::setEventType);
        binder.forField(preferencesMultiComboBox).bind(NewRequest::getPreferences, NewRequest::setPreferences);
        binder.forField(fromDateTimePicker).asRequired("Select start date and time")
                .bind(NewRequest::getFrom, NewRequest::setFrom);
        binder.forField(toDateTimePicker).asRequired("Select end date and time")
                .withValidator(to -> to.isAfter(fromDateTimePicker.getValue()), "End date has to be after start date")
                .bind(NewRequest::getTo, NewRequest::setTo);
        binder.forField(expectedAttendeesTextField)
                .withNullRepresentation("0")
                .withConverter(new StringToIntegerConverter("Key in an integer"))
                .bind(NewRequest::getExpectedNumberOfAttendees, NewRequest::setExpectedNumberOfAttendees);
        binder.forField(expectedBudgetNumberField)
                .withNullRepresentation(0.0)
                .withValidator(b -> b== null || b >= 0, "Budget has to be positive")
                .bind(NewRequest::getExpectedBudget, NewRequest::setExpectedBudget);
    }

    private void addButtonFunctionality() {
        saveButton.addClickListener(e -> {
            saveButton.setEnabled(false);
            if(binder.validate().isOk()) {
                newRequestService.postNewRequest(binder.getBean());
                Notification.show(String.format("New request for client %s saved.",
                        binder.getBean().getClient().getName()));
                clearForm();
            }
        });

        clearButton.addClickListener(e -> clearForm());
    }

    private void clearForm() {
        binder.setBean(new NewRequest());
    }
}
