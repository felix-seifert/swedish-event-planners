package com.felixseifert.swedisheventplanners.views.client;

import com.felixseifert.swedisheventplanners.model.Client;
import com.felixseifert.swedisheventplanners.service.ClientService;
import com.felixseifert.swedisheventplanners.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "clients", layout = MainView.class)
@PageTitle("Clients | Swedish Event Planners")
public class ClientView extends Div {

    private ClientService clientService;

    private Grid<Client> grid;

    private Binder<Client> binder;

    private TextField clientNameField;

    private TextField contactDetailsField;

    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;
    private Button createButton;

    public ClientView(ClientService clientService) {

        this.clientService = clientService;

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.addToSecondary(createEditorLayout());
        splitLayout.addToPrimary(createGridLayout());

        this.setHeightFull();
        this.add(splitLayout);

        bindFields();
        createFunctions();
    }

    private Div createEditorLayout() {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("client-editor");

        editorLayoutDiv.add(createFormLayout());
        editorLayoutDiv.add(createButtonLayout());

        return editorLayoutDiv;
    }

    private FormLayout createFormLayout() {

        clientNameField = new TextField();
        clientNameField.setLabel("Name");
        clientNameField.setPlaceholder("Example Client Name");

        contactDetailsField = new TextField();
        contactDetailsField.setLabel("Contact Details");
        contactDetailsField.setPlaceholder("test@example.se");

        return new FormLayout(clientNameField, contactDetailsField);
    }

    private HorizontalLayout createButtonLayout() {

        saveButton = new Button();
        saveButton.setText("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        deleteButton = new Button();
        deleteButton.setText("Delete");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        setButtonsEnabled(false);

        return new HorizontalLayout(saveButton, cancelButton, deleteButton);
    }

    private Div createGridLayout() {
        Div gridLayout = new Div();
        gridLayout.setId("client-grid");
        gridLayout.setWidthFull();

        grid = new Grid<>();
        grid.addColumn(Client::getName).setHeader("Name");
        grid.addColumn(Client::getContactDetails).setHeader("Contact Details");
        grid.setHeightFull();

        gridLayout.add(grid);

        return gridLayout;
    }

    private void bindFields() {
        binder = new Binder<>();
        binder.forField(clientNameField).asRequired("Enter client name").bind(Client::getName, Client::setName);
        binder.forField(contactDetailsField).bind(Client::getContactDetails, Client::setContactDetails);
    }

    private void createFunctions() {

        grid.setItems(clientService.getAllClients());

        grid.asSingleSelect().addValueChangeListener(event -> {
            if(event.getValue() != null) {
                Client clientFromBackend = clientService.getClientById(event.getValue().getId());
                if(clientFromBackend != null) {
                    binder.setBean(clientFromBackend);
                    setButtonsEnabled(true);
                    return;
                }
                refreshGrid();
                return;
            }
            clearForm();
        });

        saveButton.addClickListener(e -> {
            Client clientToUpdate = binder.getBean();
            setButtonsEnabled(false);
            if(clientToUpdate.getId() == null) {
                Notification.show("An exception happened while trying to store the clients details.");
                return;
            }
            clientService.putClient(clientToUpdate);
            clearForm();
            refreshGrid();
            Notification.show(String.format("Client %s updated.", clientToUpdate.getName()));
        });

        cancelButton.addClickListener(e -> {
            setButtonsEnabled(false);
            clearForm();
            refreshGrid();
        });

        deleteButton.addClickListener(e -> {
            Client clientToDelete = binder.getBean();
            setButtonsEnabled(false);
            if(clientToDelete.getId() == null) {
                Notification.show("An exception happened while trying to delete the client.");
                return;
            }
            clientService.deleteClient(clientToDelete);
            clearForm();
            refreshGrid();
            Notification.show(String.format("Client %s deleted.", clientToDelete.getName()));
        });
    }

    private void refreshGrid() {
        grid.select(null);
        grid.setItems(clientService.getAllClients());
    }

    private void clearForm() {
        binder.setBean(null);
    }

    private void setButtonsEnabled(boolean enabled) {
        saveButton.setEnabled(enabled);
        cancelButton.setEnabled(enabled);
        deleteButton.setEnabled(enabled);
    }
}
