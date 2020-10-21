package com.felixseifert.swedisheventplanners.backend.model;

import com.felixseifert.swedisheventplanners.backend.model.enums.EventType;
import com.felixseifert.swedisheventplanners.backend.model.enums.EventTypeConverter;
import com.felixseifert.swedisheventplanners.backend.model.enums.ProposalStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "proposals")
@Getter
@Setter
public class Proposal extends AbstractEntity{

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

    private String decorations;

    private String filmingPhotos;

    private String postersArtWork;

    private String foodDrinks;

    private String music;

    private String computerRelatedIssues;

    private ProposalStatus proposalStatus;

    public Proposal() {
        super();
        this.proposalStatus = ProposalStatus.INITIATED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return 44;
    }

    @Override
    public String toString() {
        return "Proposal{" +
                "recordNumber='" + recordNumber + '\'' +
                ", client=" + (client != null ? client.getName() : "") + '\'' +
                ", eventType=" + eventType + '\'' +
                ", from=" + from + '\'' +
                ", to=" + to + '\'' +
                ", expectedNumberOfAttendees=" + expectedNumberOfAttendees + '\'' +
                ", expectedBudget=" + expectedBudget + '\'' +
                ", status=" + proposalStatus +
                '}';
    }
}
