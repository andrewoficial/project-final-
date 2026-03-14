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
        // Проверка успешного получения профиля авторизованным пользователем
        perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.mailNotifications").isArray())
                .andExpect(jsonPath("$.contacts").isArray());
    }

    @Test
    void getUnauthorized() throws Exception {
        // Проверка, что неавторизованный пользователь не может получить профиль (401)
        perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("user@gmail.com")
    void updateSuccess() throws Exception {
        // Проверка успешного обновления профиля с валидными данными
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
        // Проверка, что при отправке некорректных данных (пустые строки) возвращается 422
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
        // Проверка, что указание несуществующего типа уведомления приводит к ошибке 422
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
        // Проверка, что указание несуществующего кода контакта приводит к ошибке 422
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
        // Проверка, что попытка сохранить HTML-код в контакте приводит к ошибке 422 (защита от XSS)
        ProfileTo unsafeContactTo = ProfileTestData.getWithContactHtmlUnsafeTo();
        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(unsafeContactTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateUnauthorized() throws Exception {
        // Проверка, что неавторизованный пользователь не может обновить профиль (401)
        ProfileTo anyTo = ProfileTestData.USER_PROFILE_TO;
        perform(put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(anyTo)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}