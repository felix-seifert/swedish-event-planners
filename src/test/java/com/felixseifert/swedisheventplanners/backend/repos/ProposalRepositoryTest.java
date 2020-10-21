package com.felixseifert.swedisheventplanners.backend.repos;

import com.felixseifert.swedisheventplanners.backend.model.Client;
import com.felixseifert.swedisheventplanners.backend.model.Proposal;
import com.felixseifert.swedisheventplanners.backend.model.enums.EventType;
import com.felixseifert.swedisheventplanners.backend.model.enums.ProposalStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class ProposalRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ProposalRepository proposalRepository;

    private Proposal proposal1;
    private Proposal proposal2;

    private Client client;

    @BeforeEach
    public void setDatabase() {

        client = new Client();
        client.setName("Orlean Dramp");

        proposal1 = new Proposal();
        proposal1.setRecordNumber("ABCDE12345");
        proposal1.setClient(client);
        proposal1.setEventType(EventType.CELEBRATION);
        proposal1.setFrom(LocalDateTime.now().plusDays(1));
        proposal1.setTo(LocalDateTime.now().plusDays(2));
        proposal1.setProposalStatus(ProposalStatus.UNDER_REVIEW_BY_MANAGER);
        proposal2 = new Proposal();
        proposal2.setRecordNumber("FFGGTT1234");
        proposal2.setClient(client);
        proposal2.setEventType(EventType.CONFERENCE);
        proposal2.setFrom(LocalDateTime.now().plusDays(20));
        proposal2.setTo(LocalDateTime.now().plusDays(22));

        testEntityManager.persist(client);
        testEntityManager.persist(proposal1);
        testEntityManager.persist(proposal2);
        testEntityManager.flush();
    }

    @Test
    public void findAllTest() {
        List<Proposal> proposalActual = proposalRepository.findAll();
        assertEquals(List.of(proposal1, proposal2), proposalActual);
    }

    @Test
    public void findByIdTest() {
        Optional<Proposal> proposalActual = proposalRepository.findById(proposal1.getId());
        Optional<Proposal> proposalActualEmpty = proposalRepository.findById(9999L);

        assertTrue(proposalActual.isPresent());
        assertEquals(proposal1, proposalActual.get());
        assertFalse(proposalActualEmpty.isPresent());
    }

    @Test
    public void findByProposalStatusTest() {
        List<Proposal> proposals = proposalRepository.findAllByProposalStatus(ProposalStatus.UNDER_REVIEW_BY_MANAGER);
        assertEquals(List.of(proposal1), proposals);
    }

    @Test
    public void saveTest() {
        Proposal proposalToSave = new Proposal();
        proposalToSave.setRecordNumber("GRHFKLM144");
        proposalToSave.setClient(client);
        proposalToSave.setEventType(EventType.TEAM_BUILDING);
        proposalToSave.setFrom(LocalDateTime.now().plusDays(40));
        proposalToSave.setTo(LocalDateTime.now().plusDays(41));

        Proposal proposalActual = proposalRepository.save(proposalToSave);
        List<Proposal> proposalListActual = proposalRepository.findAll();
        boolean proposalExists = proposalListActual.contains(proposalToSave);

        assertEquals(proposalToSave, proposalActual);
        assertTrue(proposalExists);
        assertNotNull(proposalActual.getId());
        assertEquals(3, proposalListActual.size());
        assertEquals(proposalToSave, proposalListActual.get(2));
        assertEquals(proposalToSave.getProposalStatus(), ProposalStatus.INITIATED);
    }

    @Test
    public void deleteTest() {
        proposalRepository.delete(proposal1);

        List<Proposal> proposalListActual = proposalRepository.findAll();
        boolean proposalExists = proposalListActual.contains(proposal1);

        assertFalse(proposalExists);
        assertEquals(1, proposalListActual.size());
    }
}
