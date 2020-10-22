package com.felixseifert.swedisheventplanners.backend.service;

import com.felixseifert.swedisheventplanners.backend.exceptions.BlankValueNotAllowedException;
import com.felixseifert.swedisheventplanners.backend.exceptions.EntityAlreadyExistsException;
import com.felixseifert.swedisheventplanners.backend.exceptions.ValueNotAllowedException;
import com.felixseifert.swedisheventplanners.backend.model.Client;
import com.felixseifert.swedisheventplanners.backend.model.Proposal;
import com.felixseifert.swedisheventplanners.backend.model.enums.EventType;
import com.felixseifert.swedisheventplanners.backend.model.enums.ProposalStatus;
import com.felixseifert.swedisheventplanners.backend.repos.ProposalRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ProposalServiceImplTest {

    @Autowired
    private ProposalServiceImpl proposalService;

    @MockBean
    private ProposalRepository proposalRepository;

    private static Client client;

    private static Proposal proposal1;
    private static Proposal proposal2;
    private static Proposal newProposal;

    private static final String VALID_RECORD_NUMBER_1 = "XXVVZY1234";
    private static final String VALID_RECORD_NUMBER_2 = "ABCDE12345";

    private static final LocalDateTime VALID_START_DATE = LocalDateTime.now().plusDays(10);
    private static final LocalDateTime VALID_END_DATE = LocalDateTime.now().plusDays(13);

    @BeforeAll
    public static void setup() {
        client = new Client();
        client.setName("Orlean Dramp");

        proposal1 = new Proposal();
        proposal1.setId(1L);
        proposal1.setRecordNumber(VALID_RECORD_NUMBER_2);
        proposal1.setClient(client);
        proposal1.setEventType(EventType.CELEBRATION);
        proposal1.setFrom(VALID_START_DATE);
        proposal1.setTo(VALID_END_DATE);
        proposal1.setProductionProposalStatus(ProposalStatus.UNDER_REVIEW_BY_MANAGER);
        proposal2 = new Proposal();
        proposal2.setId(2L);
        proposal2.setRecordNumber("FFGGTT1234");
        proposal2.setClient(client);
        proposal2.setEventType(EventType.CONFERENCE);
        proposal2.setFrom(LocalDateTime.now().plusDays(20));
        proposal2.setTo(LocalDateTime.now().plusDays(22));

        newProposal = new Proposal();
        newProposal.setRecordNumber(VALID_RECORD_NUMBER_1);
        newProposal.setClient(client);
        newProposal.setEventType(EventType.CONFERENCE);
        newProposal.setFrom(VALID_START_DATE);
        newProposal.setTo(VALID_END_DATE);
    }

    @Test
    public void getAllProposalsTest() {
        when(proposalRepository.findAll()).thenReturn(List.of(proposal1, proposal2));
        List<Proposal> actualProposals = proposalService.getAllProposals();
        assertEquals(List.of(proposal1, proposal2), actualProposals);
    }

    @Test
    public void getProposalsByIdTest() {
        when(proposalRepository.findById(proposal1.getId())).thenReturn(Optional.of(proposal1));
        Proposal actual = proposalService.getProposalById(proposal1.getId());
        assertEquals(proposal1, actual);
    }

    @Test
    public void getProposalByIdTest_noProposalFound() {
        when(proposalRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> proposalService.getProposalById(anyLong()));
    }

    @Test
    public void getProposalsByProposalStatusTest() {
        when(proposalRepository.findAllByProposalStatusIn(Set.of(ProposalStatus.PROCESSING)))
                .thenReturn(List.of(proposal1));
        List<Proposal> actualProposals = proposalService
                .getAllProposalsByStatus(Set.of(ProposalStatus.PROCESSING));
        assertEquals(List.of(proposal1), actualProposals);
    }

    @Test
    public void getProposalsByProductionProposalStatusTest() {
        when(proposalRepository.findAllByProductionProposalStatusIn(Set.of(ProposalStatus.UNDER_REVIEW_BY_MANAGER)))
                .thenReturn(List.of(proposal1));
        List<Proposal> actualProposals = proposalService
                .getAllProposalsByProductionStatus(Set.of(ProposalStatus.UNDER_REVIEW_BY_MANAGER));
        assertEquals(List.of(proposal1), actualProposals);
    }

    @Test
    public void getProposalsByServiceProposalStatusTest() {
        when(proposalRepository.findAllByServiceProposalStatusIn(Set.of(ProposalStatus.INITIATED)))
                .thenReturn(List.of(proposal1, proposal2));
        List<Proposal> actualProposals = proposalService
                .getAllProposalsByServiceStatus(Set.of(ProposalStatus.INITIATED));
        assertEquals(List.of(proposal1, proposal2), actualProposals);
    }

    @Test
    public void postProposalTest() {
        when(proposalRepository.save(newProposal)).thenReturn(newProposal);
        assertEquals(newProposal, proposalService.postProposal(newProposal));
        verify(proposalRepository).save(newProposal);
    }

    @Test
    public void postProposalTest_idExists() {
        assertThrows(EntityAlreadyExistsException.class, () -> proposalService.postProposal(proposal1));
    }

    @Test
    public void postProposalTest_emptyRecordNumber() {
        newProposal.setRecordNumber(null);
        assertThrows(BlankValueNotAllowedException.class, () -> proposalService.postProposal(newProposal));
        newProposal.setRecordNumber(VALID_RECORD_NUMBER_1);
    }

    @Test
    public void postProposalTest_blankRecordNumber() {
        newProposal.setRecordNumber("   ");
        assertThrows(BlankValueNotAllowedException.class, () -> proposalService.postProposal(newProposal));
        newProposal.setRecordNumber(VALID_RECORD_NUMBER_1);
    }

    @Test
    public void postProposalTest_wrongRecordNumber() {
        newProposal.setRecordNumber("123");
        assertThrows(ValueNotAllowedException.class, () -> proposalService.postProposal(newProposal));
        newProposal.setRecordNumber("123456789123");
        assertThrows(ValueNotAllowedException.class, () -> proposalService.postProposal(newProposal));
        newProposal.setRecordNumber(VALID_RECORD_NUMBER_1);
    }

    @Test
    public void postProposalTest_clientNull() {
        newProposal.setClient(null);
        assertThrows(BlankValueNotAllowedException.class, () -> proposalService.postProposal(newProposal));
        newProposal.setClient(client);
    }

    @Test
    public void postProposalTest_eventTypeNull() {
        newProposal.setEventType(null);
        assertThrows(BlankValueNotAllowedException.class, () -> proposalService.postProposal(newProposal));
        newProposal.setEventType(EventType.TEAM_BUILDING);
    }

    @Test
    public void postProposalTest_fromNull() {
        newProposal.setFrom(null);
        assertThrows(BlankValueNotAllowedException.class, () -> proposalService.postProposal(newProposal));
        newProposal.setFrom(VALID_START_DATE);
    }

    @Test
    public void postProposalTest_toNull() {
        newProposal.setTo(null);
        assertThrows(BlankValueNotAllowedException.class, () -> proposalService.postProposal(newProposal));
        newProposal.setTo(VALID_END_DATE);
    }

    @Test
    public void postProposalTest_wrongDates() {
        newProposal.setFrom(LocalDateTime.now().plusDays(20));
        newProposal.setTo(LocalDateTime.now().plusDays(10));
        assertThrows(ValueNotAllowedException.class, () -> proposalService.postProposal(newProposal));
        newProposal.setFrom(VALID_START_DATE);
        newProposal.setTo(VALID_END_DATE);
    }

    @Test
    public void putProposalTest() {
        when(proposalRepository.save(proposal1)).thenReturn(proposal1);
        assertEquals(proposal1, proposalService.putProposal(proposal1));
        verify(proposalRepository).save(proposal1);
    }

    @Test
    public void putProposalTest_idDoesNotExist() {
        assertThrows(EntityNotFoundException.class, () -> proposalService.putProposal(newProposal));
    }

    @Test
    public void putProposalTest_emptyRecordNumber() {
        proposal1.setRecordNumber(null);
        assertThrows(BlankValueNotAllowedException.class, () -> proposalService.putProposal(proposal1));
        proposal1.setRecordNumber(VALID_RECORD_NUMBER_2);
    }

    @Test
    public void putProposalTest_blankRecordNumber() {
        proposal1.setRecordNumber(null);
        assertThrows(BlankValueNotAllowedException.class, () -> proposalService.putProposal(proposal1));
        proposal1.setRecordNumber(VALID_RECORD_NUMBER_2);
    }

    @Test
    public void putProposalTest_wrongRecordNumber() {
        proposal1.setRecordNumber("123");
        assertThrows(ValueNotAllowedException.class, () -> proposalService.putProposal(proposal1));
        proposal1.setRecordNumber("123456789123");
        assertThrows(ValueNotAllowedException.class, () -> proposalService.putProposal(proposal1));
        proposal1.setRecordNumber(VALID_RECORD_NUMBER_2);
    }

    @Test
    public void putProposalTest_clientNull() {
        proposal1.setClient(null);
        assertThrows(BlankValueNotAllowedException.class, () -> proposalService.putProposal(proposal1));
        proposal1.setClient(client);
    }

    @Test
    public void putProposalTest_eventTypeNull() {
        proposal1.setEventType(null);
        assertThrows(BlankValueNotAllowedException.class, () -> proposalService.putProposal(proposal1));
        proposal1.setEventType(EventType.TEAM_BUILDING);
    }

    @Test
    public void putProposalTest_fromNull() {
        proposal1.setFrom(null);
        assertThrows(BlankValueNotAllowedException.class, () -> proposalService.putProposal(proposal1));
        proposal1.setFrom(VALID_START_DATE);
    }

    @Test
    public void putProposalTest_toNull() {
        proposal1.setTo(null);
        assertThrows(BlankValueNotAllowedException.class, () -> proposalService.putProposal(proposal1));
        proposal1.setTo(VALID_END_DATE);
    }

    @Test
    public void putProposalTest_wrongDates() {
        proposal1.setFrom(LocalDateTime.now().plusDays(20));
        proposal1.setTo(LocalDateTime.now().plusDays(10));
        assertThrows(ValueNotAllowedException.class, () -> proposalService.putProposal(proposal1));
        proposal1.setFrom(VALID_START_DATE);
        proposal1.setTo(VALID_END_DATE);
    }

    @Test
    public void deleteProposalTest() {
        proposalService.deleteProposal(proposal1);
        verify(proposalRepository).delete(proposal1);
    }

    @Test
    public void deleteProposalTest_IdDoesNotExist() {
        assertThrows(EntityNotFoundException.class, () -> proposalService.deleteProposal(newProposal));
    }

}
