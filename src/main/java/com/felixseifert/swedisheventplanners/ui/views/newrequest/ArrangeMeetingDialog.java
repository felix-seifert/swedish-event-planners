package com.felixseifert.swedisheventplanners.ui.views.newrequest;

import com.felixseifert.swedisheventplanners.backend.model.NewRequest;
import com.felixseifert.swedisheventplanners.backend.model.enums.RequestStatus;
import com.felixseifert.swedisheventplanners.backend.service.NewRequestService;
import com.felixseifert.swedisheventplanners.ui.common.AbstractDialog;
import com.felixseifert.swedisheventplanners.ui.common.ChangeHandlerWithEntity;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Label;
import lombok.Setter;

public class ArrangeMeetingDialog extends AbstractDialog {

    @Setter
    private ChangeHandlerWithEntity archieveRequestHandler;

    private DateTimePicker dateTimePicker = new DateTimePicker("Arranged Date");

    private NewRequest newRequest;

    public ArrangeMeetingDialog(NewRequestService newRequestService) {

        super("Meeting Date");

        getContent().add(new Label("When should the business meeting with the client happen?"));

        dateTimePicker.setRequiredIndicatorVisible(true);
        dateTimePicker.onEnabledStateChanged(true);
        dateTimePicker.addValueChangeListener(e -> getOkButton().setEnabled(!dateTimePicker.isEmpty()));
        getContent().add(dateTimePicker);

        getOkButton().setEnabled(false);
        getOkButton().setText("Confirm Meeting Date");
        getOkButton().addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        setChangeHandler(() -> {
            newRequest.setMeetingDate(dateTimePicker.getValue());
            newRequest.setRequestStatus(RequestStatus.MEETING_ARRANGED);
            archieveRequestHandler.onChange(newRequest);
        });
    }

    public void open(NewRequest newRequest) {
        this.newRequest = newRequest;
        dateTimePicker.clear();
        dateTimePicker.setMax(newRequest.getFrom().minusDays(1));
        this.open();
    }
}
