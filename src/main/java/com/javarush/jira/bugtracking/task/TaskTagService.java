package com.javarush.jira.bugtracking.task;

import com.javarush.jira.common.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskTagService {

    private final TaskRepository taskRepository;

    @Transactional
    public void addTag(Long taskId, String tag) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Задача с id " + taskId + " не найдена"));
        Set<String> currentTags = task.getTags();
        if (currentTags.contains(tag)) {
            throw new IllegalArgumentException("Тег '" + tag + "' уже существует у задачи " + taskId);
        }
        // Создаём изменяемое множество
        Set<String> newTags = new HashSet<>(currentTags);
        newTags.add(tag);
        task.setTags(newTags);
    }

    @Transactional
    public void removeTag(Long taskId, String tag) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Задача с id " + taskId + " не найдена"));
        Set<String> currentTags = task.getTags();
        if (!currentTags.contains(tag)) {
            // Если тега нет, ничего не делаем (идемпотентность)
            return;
        }
        Set<String> newTags = new HashSet<>(currentTags);
        newTags.remove(tag);
        task.setTags(newTags);
    }

    public List<String> getTagsForTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Задача с id " + taskId + " не найдена"));
        return List.copyOf(task.getTags());
    }
}