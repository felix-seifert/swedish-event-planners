package com.felixseifert.swedisheventplanners.backend.model;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.felixseifert.swedisheventplanners.backend.model.enums.EventType;
import com.felixseifert.swedisheventplanners.backend.model.enums.EventTypeConverter;
import com.felixseifert.swedisheventplanners.backend.model.enums.ProposalStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "proposals")
@Getter
@Setter
public class Proposal extends AbstractEntity {

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

    @Transient
    @Setter(AccessLevel.NONE)
    private ProposalStatus proposalStatus;

    private ProposalStatus productionProposalStatus;

    private ProposalStatus serviceProposalStatus;

    public Proposal() {
        this.proposalStatus = ProposalStatus.INITIATED;
        this.productionProposalStatus = ProposalStatus.INITIATED;
        this.serviceProposalStatus = ProposalStatus.INITIATED;
    }

    public ProposalStatus getProposalStatus() {
        if (ProposalStatus.INITIATED.equals(productionProposalStatus)
                && ProposalStatus.INITIATED.equals(serviceProposalStatus)) {
            return ProposalStatus.INITIATED;
        }
        if (ProposalStatus.CLOSED.equals(productionProposalStatus)
                && ProposalStatus.CLOSED.equals(serviceProposalStatus)) {
            return ProposalStatus.CLOSED;
        }
        return ProposalStatus.PROCESSING;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Proposal)) {
            return false;
        }
        Proposal proposal = (Proposal) obj;
        return Objects.equals(this.getId(), proposal.getId()) &&
                Objects.equals(recordNumber, proposal.recordNumber) &&
                Objects.equals(client, proposal.client) &&
                eventType == proposal.eventType &&
                Objects.equals(from, proposal.from) &&
                Objects.equals(to, proposal.to) &&
                Objects.equals(expectedNumberOfAttendees, proposal.expectedNumberOfAttendees) &&
                Objects.equals(expectedBudget, proposal.expectedBudget) &&
                Objects.equals(decorations, proposal.decorations) &&
                Objects.equals(filmingPhotos, proposal.filmingPhotos) &&
                Objects.equals(postersArtWork, proposal.postersArtWork) &&
                Objects.equals(foodDrinks, proposal.foodDrinks) &&
                Objects.equals(music, proposal.music) &&
                Objects.equals(computerRelatedIssues, proposal.computerRelatedIssues) &&
                proposalStatus == proposal.proposalStatus &&
                productionProposalStatus == proposal.productionProposalStatus &&
                serviceProposalStatus == proposal.serviceProposalStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), recordNumber, client, eventType, from, to,
                expectedNumberOfAttendees, expectedBudget, decorations, filmingPhotos,
                postersArtWork, foodDrinks, music, computerRelatedIssues,
                proposalStatus, productionProposalStatus, serviceProposalStatus);
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
                ", productionStatus=" + productionProposalStatus +
                ", serviceStatus=" + serviceProposalStatus +
                '}';
    }
}
