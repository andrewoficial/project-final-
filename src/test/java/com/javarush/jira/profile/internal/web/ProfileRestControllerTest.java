package com.javarush.jira.profile.internal.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.jira.AbstractControllerTest;
import com.javarush.jira.profile.ProfileTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

class ProfileRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ProfileRestController.REST_URL;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithUserDetails("user@gmail.com")
    void getSuccess() throws Exception {
        perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.mailNotifications").isArray())
                .andExpect(jsonPath("$.contacts").isArray());
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void updateSuccess() throws Exception {
        ProfileTo updateTo = ProfileTestData.USER_PROFILE_TO;
        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTo)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void updateInvalid() throws Exception {
        ProfileTo invalidTo = ProfileTestData.getInvalidTo();
        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void updateWithUnknownNotification() throws Exception {
        ProfileTo unknownNotificationTo = ProfileTestData.getWithUnknownNotificationTo();
        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unknownNotificationTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void updateWithUnknownContactCode() throws Exception {
        ProfileTo unknownContactTo = ProfileTestData.getWithUnknownContactTo();
        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unknownContactTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void updateWithContactHtmlUnsafe() throws Exception {
        ProfileTo unsafeContactTo = ProfileTestData.getWithContactHtmlUnsafeTo();
        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unsafeContactTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateUnauthorized() throws Exception {
        ProfileTo anyTo = ProfileTestData.USER_PROFILE_TO;
        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(anyTo)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}