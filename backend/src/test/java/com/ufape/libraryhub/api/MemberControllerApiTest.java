package com.ufape.libraryhub.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class MemberControllerApiTest {

    private static final String NEW_MEMBER = """
            {"name": "Ana Souza", "email": "ana.souza@ufape.edu.br"}
            """;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postMemberReturnsCreatedAndBody() throws Exception {
        mockMvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).content(NEW_MEMBER))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Ana Souza"))
                .andExpect(jsonPath("$.email").value("ana.souza@ufape.edu.br"));
    }

    @Test
    void postMemberWithDuplicatedEmailReturnsConflict() throws Exception {
        mockMvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).content(NEW_MEMBER))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).content(NEW_MEMBER))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void postMemberWithInvalidDataReturnsBadRequest() throws Exception {
        String invalidMember = """
                {"name": "", "email": "not-an-email"}
                """;

        mockMvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).content(invalidMember))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void getMemberThatDoesNotExistReturnsNotFound() throws Exception {
        mockMvc.perform(get("/members/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void getMembersReturnsRegisteredMembers() throws Exception {
        mockMvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).content(NEW_MEMBER))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("ana.souza@ufape.edu.br"));
    }
}
