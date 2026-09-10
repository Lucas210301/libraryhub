package com.ufape.libraryhub.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ReservationControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postReservationForAnAvailableItemReturnsConflict() throws Exception {
        long memberId = createMember("Ana Souza", "ana.souza@ufape.edu.br");
        long itemId = createBook("Clean Code", "9780132350884");

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody(memberId, itemId)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void postReservationForAnItemOnLoanReturnsCreated() throws Exception {
        long borrowerId = createMember("Bruno Lima", "bruno.lima@ufape.edu.br");
        long readerId = createMember("Carla Dias", "carla.dias@ufape.edu.br");
        long itemId = createBook("Refactoring", "9780134757599");
        createLoan(borrowerId, itemId);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody(readerId, itemId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.member.name").value("Carla Dias"))
                .andExpect(jsonPath("$.item.title").value("Refactoring"));
    }

    @Test
    void postReservationWithoutTheMemberReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/reservations").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void cancelReservationTwiceReturnsConflict() throws Exception {
        long reservationId = createReservation();

        mockMvc.perform(put("/reservations/" + reservationId + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.active").value(false));

        mockMvc.perform(put("/reservations/" + reservationId + "/cancel"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void cancelReservationThatDoesNotExistReturnsNotFound() throws Exception {
        mockMvc.perform(put("/reservations/9999/cancel"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void getReservationsReturnsTheCreatedReservation() throws Exception {
        createReservation();

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    private long createReservation() throws Exception {
        long borrowerId = createMember("Diego Alves", "diego.alves@ufape.edu.br");
        long readerId = createMember("Elisa Rocha", "elisa.rocha@ufape.edu.br");
        long itemId = createBook("Domain Driven Design", "9780321125217");
        createLoan(borrowerId, itemId);

        String body = mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody(readerId, itemId)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return idOf(body);
    }

    private long createMember(String name, String email) throws Exception {
        String body = mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"" + name + "\", \"email\": \"" + email + "\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return idOf(body);
    }

    private long createBook(String title, String isbn) throws Exception {
        String body = mockMvc.perform(post("/items/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"" + title + "\", \"author\": \"Author\", \"isbn\": \"" + isbn + "\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return idOf(body);
    }

    private void createLoan(long memberId, long itemId) throws Exception {
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"memberId\": " + memberId + ", \"itemId\": " + itemId + "}"))
                .andExpect(status().isCreated());
    }

    private String reservationBody(long memberId, long itemId) {
        return "{\"memberId\": " + memberId + ", \"itemId\": " + itemId + "}";
    }

    private long idOf(String body) {
        return ((Number) JsonPath.read(body, "$.id")).longValue();
    }
}
