package com.felixseifert.swedisheventplanners.repos;

import com.felixseifert.swedisheventplanners.model.Client;
import com.felixseifert.swedisheventplanners.model.NewRequest;
import com.felixseifert.swedisheventplanners.model.enums.EventType;
import com.felixseifert.swedisheventplanners.model.enums.Preference;
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
public class NewRequestRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private NewRequestRepository newRequestRepository;

    private NewRequest newRequest1;
    private NewRequest newRequest2;

    private Client client;

    @BeforeEach
    public void setDatabase() {

        client = new Client();
        client.setName("Orlean Dramp");

        newRequest1 = new NewRequest();
        newRequest1.setRecordNumber("ABCDE12345");
        newRequest1.setClient(client);
        newRequest1.setEventType(EventType.CELEBRATION);
        newRequest1.setFrom(LocalDateTime.now().plusDays(1));
        newRequest1.setTo(LocalDateTime.now().plusDays(2));
        newRequest1.addPreference(Preference.PHOTOS_FILMING);
        newRequest2 = new NewRequest();
        newRequest2.setRecordNumber("FFGGTT1234");
        newRequest2.setClient(client);
        newRequest2.setEventType(EventType.CONFERENCE);
        newRequest2.setFrom(LocalDateTime.now().plusDays(20));
        newRequest2.setTo(LocalDateTime.now().plusDays(22));

        testEntityManager.persist(client);
        testEntityManager.persist(newRequest1);
        testEntityManager.persist(newRequest2);
        testEntityManager.flush();
    }

    @Test
    public void findAllTest() {
        List<NewRequest> newRequestActual = newRequestRepository.findAll();
        assertEquals(List.of(newRequest1, newRequest2), newRequestActual);
    }

    @Test
    public void findByIdTest() {
        Optional<NewRequest> newRequestActual = newRequestRepository.findById(newRequest1.getId());
        Optional<NewRequest> newRequestActualEmpty = newRequestRepository.findById(9999L);

        assertTrue(newRequestActual.isPresent());
        assertEquals(newRequest1, newRequestActual.get());
        assertFalse(newRequestActualEmpty.isPresent());
    }

    @Test
    public void saveTest() {
        NewRequest newRequestToSave = new NewRequest();
        newRequestToSave.setRecordNumber("GRHFKLM144");
        newRequestToSave.setClient(client);
        newRequestToSave.setEventType(EventType.TEAM_BUILDING);
        newRequestToSave.setFrom(LocalDateTime.now().plusDays(40));
        newRequestToSave.setTo(LocalDateTime.now().plusDays(41));

        NewRequest newRequestActual = newRequestRepository.save(newRequestToSave);
        List<NewRequest> newRequestListActual = newRequestRepository.findAll();
        boolean newRequestExists = newRequestListActual.contains(newRequestToSave);

        assertEquals(newRequestToSave, newRequestActual);
        assertTrue(newRequestExists);
        assertNotNull(newRequestActual.getId());
        assertEquals(3, newRequestListActual.size());
        assertEquals(newRequestToSave, newRequestListActual.get(2));
    }

    @Test
    public void deleteTest() {
        newRequestRepository.delete(newRequest1);

        List<NewRequest> newRequestListActual = newRequestRepository.findAll();
        boolean newRequestExists = newRequestListActual.contains(newRequest1);

        assertFalse(newRequestExists);
        assertEquals(1, newRequestListActual.size());
    }
}
