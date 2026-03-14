package com.javarush.jira.bugtracking.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = TaskTagController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Task Tags", description = "API для управления тегами задач")
public class TaskTagController {
    public static final String REST_URL = "/api/tasks/{taskId}/tags";

    private final TaskTagService taskTagService;

    @GetMapping
    @Operation(summary = "Получить все теги задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список тегов успешно получен"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public List<String> getTags(@PathVariable Long taskId) {
        return taskTagService.getTagsForTask(taskId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавить тег к задаче")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Тег успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос (например, тег уже существует)"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public void addTag(@PathVariable Long taskId, @RequestBody String tag) {
        taskTagService.addTag(taskId, tag);
    }

    @DeleteMapping("/{tag}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить тег у задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Тег успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public void deleteTag(@PathVariable Long taskId, @PathVariable String tag) {
        taskTagService.removeTag(taskId, tag);
    }
}