package com.felixseifert.swedisheventplanners.backend.service;

import com.felixseifert.swedisheventplanners.backend.exceptions.BlankValueNotAllowedException;
import com.felixseifert.swedisheventplanners.backend.exceptions.EntityAlreadyExistsException;
import com.felixseifert.swedisheventplanners.backend.exceptions.ValueNotAllowedException;
import com.felixseifert.swedisheventplanners.backend.model.Client;
import com.felixseifert.swedisheventplanners.backend.model.NewRequest;
import com.felixseifert.swedisheventplanners.backend.model.enums.EventType;
import com.felixseifert.swedisheventplanners.backend.model.enums.Preference;
import com.felixseifert.swedisheventplanners.backend.model.enums.RequestStatus;
import com.felixseifert.swedisheventplanners.backend.repos.NewRequestRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class NewRequestServiceImplTest {

    @Autowired
    private NewRequestServiceImpl newRequestService;

    @MockBean
    private NewRequestRepository newRequestRepository;

    private static Client client;

    private static NewRequest newRequest1;
    private static NewRequest newRequest2;
    private static NewRequest newNewRequest;

    private static final String VALID_RECORD_NUMBER_1 = "XXVVZY1234";
    private static final String VALID_RECORD_NUMBER_2 = "ABCDE12345";

    private static final LocalDateTime VALID_START_DATE = LocalDateTime.now().plusDays(10);
    private static final LocalDateTime VALID_END_DATE = LocalDateTime.now().plusDays(13);

    @BeforeAll
    public static void setup() {
        client = new Client();
        client.setName("Orlean Dramp");

        newRequest1 = new NewRequest();
        newRequest1.setId(1L);
        newRequest1.setRecordNumber(VALID_RECORD_NUMBER_2);
        newRequest1.setClient(client);
        newRequest1.setEventType(EventType.CELEBRATION);
        newRequest1.setFrom(VALID_START_DATE);
        newRequest1.setTo(VALID_END_DATE);
        newRequest1.addPreference(Preference.PHOTOS_FILMING);
        newRequest1.setRequestStatus(RequestStatus.UNDER_REVIEW_BY_FM);
        newRequest2 = new NewRequest();
        newRequest2.setId(2L);
        newRequest2.setRecordNumber("FFGGTT1234");
        newRequest2.setClient(client);
        newRequest2.setEventType(EventType.CONFERENCE);
        newRequest2.setFrom(LocalDateTime.now().plusDays(20));
        newRequest2.setTo(LocalDateTime.now().plusDays(22));

        newNewRequest = new NewRequest();
        newNewRequest.setRecordNumber(VALID_RECORD_NUMBER_1);
        newNewRequest.setClient(client);
        newNewRequest.setEventType(EventType.CONFERENCE);
        newNewRequest.setFrom(VALID_START_DATE);
        newNewRequest.setTo(VALID_END_DATE);
    }

    @Test
    public void getAllNewRequestsTest() {
        when(newRequestRepository.findAll()).thenReturn(List.of(newRequest1, newRequest2));
        List<NewRequest> actualNewRequests = newRequestService.getAllNewRequests();
        assertEquals(List.of(newRequest1, newRequest2), actualNewRequests);
    }

    @Test
    public void getNewRequestByIdTest() {
        when(newRequestRepository.findById(newRequest1.getId())).thenReturn(Optional.of(newRequest1));
        NewRequest actual = newRequestService.getNewRequestById(newRequest1.getId());
        assertEquals(newRequest1, actual);
    }

    @Test
    public void getNewRequestByIdTest_noNewRequestFound() {
        when(newRequestRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> newRequestService.getNewRequestById(anyLong()));
    }

    @Test
    public void getNewRequestByRequestStatusTest() {
        when(newRequestRepository.findAllByRequestStatus(RequestStatus.UNDER_REVIEW_BY_FM))
                .thenReturn(List.of(newRequest1));
        List<NewRequest> actualNewRequests = newRequestService
                .getAllNewRequestsByStatus(RequestStatus.UNDER_REVIEW_BY_FM);
        assertEquals(List.of(newRequest1), actualNewRequests);
    }

    @Test
    public void postNewRequestTest() {
        when(newRequestRepository.save(newNewRequest)).thenReturn(newNewRequest);
        assertEquals(newNewRequest, newRequestService.postNewRequest(newNewRequest));
        verify(newRequestRepository).save(newNewRequest);
    }

    @Test
    public void postNewRequestTest_idExists() {
        assertThrows(EntityAlreadyExistsException.class, () -> newRequestService.postNewRequest(newRequest1));
    }

    @Test
    public void postNewRequestTest_emptyRecordNumber() {
        newNewRequest.setRecordNumber(null);
        assertThrows(BlankValueNotAllowedException.class, () -> newRequestService.postNewRequest(newNewRequest));
        newNewRequest.setRecordNumber(VALID_RECORD_NUMBER_1);
    }

    @Test
    public void postNewRequestTest_blankRecordNumber() {
        newNewRequest.setRecordNumber("   ");
        assertThrows(BlankValueNotAllowedException.class, () -> newRequestService.postNewRequest(newNewRequest));
        newNewRequest.setRecordNumber(VALID_RECORD_NUMBER_1);
    }

    @Test
    public void postNewRequestTest_wrongRecordNumber() {
        newNewRequest.setRecordNumber("123");
        assertThrows(ValueNotAllowedException.class, () -> newRequestService.postNewRequest(newNewRequest));
        newNewRequest.setRecordNumber("123456789123");
        assertThrows(ValueNotAllowedException.class, () -> newRequestService.postNewRequest(newNewRequest));
        newNewRequest.setRecordNumber(VALID_RECORD_NUMBER_1);
    }

    @Test
    public void postNewRequestTest_clientNull() {
        newNewRequest.setClient(null);
        assertThrows(BlankValueNotAllowedException.class, () -> newRequestService.postNewRequest(newNewRequest));
        newNewRequest.setClient(client);
    }

    @Test
    public void postNewRequestTest_eventTypeNull() {
        newNewRequest.setEventType(null);
        assertThrows(BlankValueNotAllowedException.class, () -> newRequestService.postNewRequest(newNewRequest));
        newNewRequest.setEventType(EventType.TEAM_BUILDING);
    }

    @Test
    public void postNewRequestTest_fromNull() {
        newNewRequest.setFrom(null);
        assertThrows(BlankValueNotAllowedException.class, () -> newRequestService.postNewRequest(newNewRequest));
        newNewRequest.setFrom(VALID_START_DATE);
    }

    @Test
    public void postNewRequestTest_toNull() {
        newNewRequest.setTo(null);
        assertThrows(BlankValueNotAllowedException.class, () -> newRequestService.postNewRequest(newNewRequest));
        newNewRequest.setTo(VALID_END_DATE);
    }

    @Test
    public void postNewRequestTest_wrongDates() {
        newNewRequest.setFrom(LocalDateTime.now().plusDays(20));
        newNewRequest.setTo(LocalDateTime.now().plusDays(10));
        assertThrows(ValueNotAllowedException.class, () -> newRequestService.postNewRequest(newNewRequest));
        newNewRequest.setFrom(VALID_START_DATE);
        newNewRequest.setTo(VALID_END_DATE);
    }

    @Test
    public void putNewRequestTest() {
        when(newRequestRepository.save(newRequest1)).thenReturn(newRequest1);
        assertEquals(newRequest1, newRequestService.putNewRequest(newRequest1));
        verify(newRequestRepository).save(newRequest1);
    }

    @Test
    public void putNewRequestTest_idDoesNotExist() {
        assertThrows(EntityNotFoundException.class, () -> newRequestService.putNewRequest(newNewRequest));
    }

    @Test
    public void putNewRequestTest_emptyRecordNumber() {
        newRequest1.setRecordNumber(null);
        assertThrows(BlankValueNotAllowedException.class, () -> newRequestService.putNewRequest(newRequest1));
        newRequest1.setRecordNumber(VALID_RECORD_NUMBER_2);
    }

    @Test
    public void putNewRequestTest_blankRecordNumber() {
        newRequest1.setRecordNumber(null);
        assertThrows(BlankValueNotAllowedException.class, () -> newRequestService.putNewRequest(newRequest1));
        newRequest1.setRecordNumber(VALID_RECORD_NUMBER_2);
    }

    @Test
    public void putNewRequestTest_wrongRecordNumber() {
        newRequest1.setRecordNumber("123");
         assertThrows(ValueNotAllowedException.class, () -> newRequestService.putNewRequest(newRequest1));
        newRequest1.setRecordNumber("123456789123");
        assertThrows(ValueNotAllowedException.class, () -> newRequestService.putNewRequest(newRequest1));
        newRequest1.setRecordNumber(VALID_RECORD_NUMBER_2);
    }

    @Test
    public void putNewRequestTest_clientNull() {
        newRequest1.setClient(null);
        assertThrows(BlankValueNotAllowedException.class, () -> newRequestService.putNewRequest(newRequest1));
        newRequest1.setClient(client);
    }

    @Test
    public void putNewRequestTest_eventTypeNull() {
        newRequest1.setEventType(null);
        assertThrows(BlankValueNotAllowedException.class, () -> newRequestService.putNewRequest(newRequest1));
        newRequest1.setEventType(EventType.TEAM_BUILDING);
    }

    @Test
    public void putNewRequestTest_fromNull() {
        newRequest1.setFrom(null);
        assertThrows(BlankValueNotAllowedException.class, () -> newRequestService.putNewRequest(newRequest1));
        newRequest1.setFrom(VALID_START_DATE);
    }

    @Test
    public void putNewRequestTest_toNull() {
        newRequest1.setTo(null);
        assertThrows(BlankValueNotAllowedException.class, () -> newRequestService.putNewRequest(newRequest1));
        newRequest1.setTo(VALID_END_DATE);
    }

    @Test
    public void putNewRequestTest_wrongDates() {
        newRequest1.setFrom(LocalDateTime.now().plusDays(20));
        newRequest1.setTo(LocalDateTime.now().plusDays(10));
        assertThrows(ValueNotAllowedException.class, () -> newRequestService.putNewRequest(newRequest1));
        newRequest1.setFrom(VALID_START_DATE);
        newRequest1.setTo(VALID_END_DATE);
    }

    @Test
    public void deleteNewRecordTest() {
        newRequestService.deleteNewRequest(newRequest1);
        verify(newRequestRepository).delete(newRequest1);
    }

    @Test
    public void deleteNewRecordTest_IdDoesNotExist() {
        assertThrows(EntityNotFoundException.class, () -> newRequestService.deleteNewRequest(newNewRequest));
    }
}
