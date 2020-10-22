package com.felixseifert.swedisheventplanners.backend.model;

import com.felixseifert.swedisheventplanners.backend.model.enums.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return 33;
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
