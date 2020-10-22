package com.felixseifert.swedisheventplanners.ui.views.proposal;

import com.felixseifert.swedisheventplanners.backend.model.Client;
import com.felixseifert.swedisheventplanners.backend.model.Proposal;
import com.felixseifert.swedisheventplanners.backend.model.enums.EventType;
import com.felixseifert.swedisheventplanners.backend.model.enums.Role;
import com.felixseifert.swedisheventplanners.backend.service.ClientService;
import com.felixseifert.swedisheventplanners.backend.service.ProposalService;
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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

@Route(value = "proposal/create", layout = MainView.class)
@PageTitle("Create Proposal | Swedish Event Planners")
@Secured({Role.ForAnnotation.CUSTOMER_SERVICE_OFFICER_WITH_PREFIX,
        Role.ForAnnotation.SENIOR_CUSTOMER_SERVICE_OFFICER_WITH_PREFIX,
        Role.ForAnnotation.CLIENT_WITH_PREFIX})
public class CreateProposalView extends VerticalLayout {

    private ProposalService proposalService;

    private ClientService clientService;

    private TextField recordNumberTextField = new TextField("Record Number");
    private ComboBox<Client> clientComboBox = new ComboBox<>("Client");
    private ComboBox<EventType> eventTypeComboBox = new ComboBox<>("Event Type");
    private DateTimePicker fromDateTimePicker = new DateTimePicker("From");
    private DateTimePicker toDateTimePicker = new DateTimePicker("To");
    private TextField expectedAttendeesTextField = new TextField("Expected Number of Attendees");
    private NumberField expectedBudgetNumberField = new NumberField("Expected Budget");
    private TextArea decorationsTextArea = new TextArea("Decorations");
    private TextArea filmingPhotosTextArea = new TextArea("Filming/Photos");
    private TextArea postersArtWorkTextArea = new TextArea("Posters/Art Work");
    private TextArea foodDrinksTextArea = new TextArea("Food/Drinks");
    private TextArea musicTextArea = new TextArea("Music");
    private TextArea computerRelatedIssuesTextArea = new TextArea("Computer-Related Issues");

    private Button saveButton = new Button("Save");
    private Button clearButton = new Button("Clear Form");

    private Binder<Proposal> binder = new Binder<>();

    public CreateProposalView(ProposalService proposalService, ClientService clientService) {

        this.proposalService = proposalService;
        this.clientService = clientService;

        this.setId("create-proposal-view");

        add(new Label("A client creates a proposal. The client has to be registered."));

        this.add(createFormLayout());
        this.add(createButtonLayout());

        binder.setBean(new Proposal());
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

        return new FormLayout(recordNumberTextField, clientComboBox, eventTypeComboBox, new Label(),
                fromDateTimePicker, toDateTimePicker, expectedAttendeesTextField, expectedBudgetNumberField,
                decorationsTextArea, filmingPhotosTextArea, postersArtWorkTextArea, foodDrinksTextArea, musicTextArea,
                computerRelatedIssuesTextArea);
    }

    private Component createButtonLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return new HorizontalLayout(saveButton, clearButton);
    }

    private void bindFields() {
        binder.forField(recordNumberTextField).asRequired("Enter record number")
                .withValidator(rn -> rn.length() == 10, "Every record number has 10 digits")
                .bind(Proposal::getRecordNumber, Proposal::setRecordNumber);
        binder.forField(clientComboBox).asRequired("Select client")
                .bind(Proposal::getClient, Proposal::setClient);
        binder.forField(eventTypeComboBox).asRequired("Select type of event")
                .bind(Proposal::getEventType, Proposal::setEventType);
        binder.forField(fromDateTimePicker).asRequired("Select start date and time")
                .bind(Proposal::getFrom, Proposal::setFrom);
        binder.forField(toDateTimePicker).asRequired("Select end date and time")
                .withValidator(to -> to.isAfter(fromDateTimePicker.getValue()), "End date has to be after start date")
                .bind(Proposal::getTo, Proposal::setTo);
        binder.forField(expectedAttendeesTextField)
                .withNullRepresentation("0")
                .withConverter(new StringToIntegerConverter("Key in an integer"))
                .bind(Proposal::getExpectedNumberOfAttendees, Proposal::setExpectedNumberOfAttendees);
        binder.forField(expectedBudgetNumberField)
                .withNullRepresentation(0.0)
                .withValidator(b -> b== null || b >= 0, "Budget has to be positive")
                .bind(Proposal::getExpectedBudget, Proposal::setExpectedBudget);
        binder.forField(decorationsTextArea)
                .bind(Proposal::getDecorations, Proposal::setDecorations);
        binder.forField(filmingPhotosTextArea)
                .bind(Proposal::getFilmingPhotos, Proposal::setFilmingPhotos);
        binder.forField(postersArtWorkTextArea)
                .bind(Proposal::getPostersArtWork, Proposal::setPostersArtWork);
        binder.forField(foodDrinksTextArea)
                .bind(Proposal::getFoodDrinks, Proposal::setFoodDrinks);
        binder.forField(musicTextArea)
                .bind(Proposal::getMusic, Proposal::setMusic);
        binder.forField(computerRelatedIssuesTextArea)
                .bind(Proposal::getComputerRelatedIssues, Proposal::setComputerRelatedIssues);
    }

    private void addButtonFunctionality() {
        saveButton.addClickListener(e -> {
            saveButton.setEnabled(false);
            if(binder.validate().isOk()) {
                proposalService.postProposal(binder.getBean());
                Notification.show(String.format("Proposal for client %s saved.",
                        binder.getBean().getClient().getName()));
                clearForm();
            }
        });

        clearButton.addClickListener(e -> clearForm());
    }

    private void clearForm() {
        binder.setBean(new Proposal());
    }
}
