package com.felixseifert.swedisheventplanners.backend.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.felixseifert.swedisheventplanners.backend.model.enums.EventType;
import com.felixseifert.swedisheventplanners.backend.model.enums.EventTypeConverter;
import com.felixseifert.swedisheventplanners.backend.model.enums.Preference;
import com.felixseifert.swedisheventplanners.backend.model.enums.PreferencesConverter;
import com.felixseifert.swedisheventplanners.backend.model.enums.RequestStatus;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "new_requests")
@Getter
@Setter
public class NewRequest extends AbstractEntity {

    @Column(nullable = false, length = 10)
    private String recordNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(columnDefinition = "smallint", nullable = false)
    @Convert(converter = EventTypeConverter.class)
    private EventType eventType;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime from;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime to;

    private Integer expectedNumberOfAttendees;

    private Double expectedBudget;

    @ElementCollection
    @CollectionTable(name = "event_preferences", joinColumns = @JoinColumn(name = "new_request_id"))
    @Column(columnDefinition = "smallint")
    @Convert(converter = PreferencesConverter.class)
    private Set<Preference> preferences = new HashSet<>();

    private RequestStatus requestStatus;

    private LocalDateTime meetingDate;

    public NewRequest() {
        super();
        this.requestStatus = RequestStatus.UNDER_REVIEW_BY_SCSO;
    }

    public void addPreference(Preference preference) {
        preferences.add(preference);
    }

    public void removePreference(Preference preference) {
        preferences.remove(preference);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NewRequest)) {
            return false;
        }
        NewRequest request = (NewRequest) obj;
        return Objects.equals(this.getId(), request.getId()) &&
                Objects.equals(recordNumber, request.recordNumber) &&
                Objects.equals(client, request.client) &&
                eventType == request.eventType &&
                Objects.equals(from, request.from) &&
                Objects.equals(to, request.to) &&
                Objects.equals(expectedNumberOfAttendees, request.expectedNumberOfAttendees) &&
                Objects.equals(expectedBudget, request.expectedBudget) &&
                Objects.equals(preferences, request.preferences) &&
                requestStatus == request.requestStatus &&
                Objects.equals(meetingDate, request.meetingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), recordNumber, client,
                eventType, from, to, expectedNumberOfAttendees,
                expectedBudget, preferences, requestStatus, meetingDate);
    }

    @Override
    public String toString() {
        return "NewRequest{" +
                "recordNumber='" + recordNumber + '\'' +
                ", client=" + (client != null ? client.getName() : "") + '\'' +
                ", eventType=" + eventType + '\'' +
                ", from=" + from + '\'' +
                ", to=" + to + '\'' +
                ", expectedNumberOfAttendees=" + expectedNumberOfAttendees + '\'' +
                ", expectedBudget=" + expectedBudget + '\'' +
                ", preferences=" + preferences + '\'' +
                ", status=" + requestStatus + '\'' +
                ", meetingDate=" + meetingDate +
                '}';
    }
}
