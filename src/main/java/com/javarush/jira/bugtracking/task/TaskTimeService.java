package com.javarush.jira.bugtracking.task;

import com.javarush.jira.common.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskTimeService {

    private final TaskRepository taskRepository;
    private final ActivityRepository activityRepository;

    /**
     * Возвращает время, которое задача находилась в работе
     * (от первого статуса 'in_progress' до первого статуса 'ready_for_review').
     * @param taskId идентификатор задачи
     * @return Duration (может быть null, если нет полного цикла)
     */
    public Duration getWorkDuration(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new NotFoundException("Task with id " + taskId + " not found");
        }

        List<Activity> activities = activityRepository.findAllByTaskIdOrderByUpdatedAsc(taskId);
        if (activities.isEmpty()) {
            return null;
        }

        LocalDateTime start = null;
        LocalDateTime end = null;

        // Ищем первую запись in_progress и следующую за ней ready_for_review
        for (Activity a : activities) {
            if (a.getStatusCode() == null) continue;
            if (start == null && "in_progress".equals(a.getStatusCode())) {
                start = a.getUpdated();
            } else if (start != null && end == null && "ready_for_review".equals(a.getStatusCode())) {
                end = a.getUpdated();
                break;
            }
        }

        if (start != null && end != null) {
            return Duration.between(start, end);
        }
        return null;
    }

    /**
     * Возвращает время, которое задача находилась в тестировании
     * (от первого статуса 'ready_for_review' до первого статуса 'done').
     * @param taskId идентификатор задачи
     * @return Duration (может быть null, если нет полного цикла)
     */
    public Duration getTestDuration(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new NotFoundException("Task with id " + taskId + " not found");
        }

        List<Activity> activities = activityRepository.findAllByTaskIdOrderByUpdatedAsc(taskId);
        if (activities.isEmpty()) {
            return null;
        }

        LocalDateTime start = null;
        LocalDateTime end = null;

        // Ищем первую запись ready_for_review и следующую за ней done
        for (Activity a : activities) {
            if (a.getStatusCode() == null) continue;
            if (start == null && "ready_for_review".equals(a.getStatusCode())) {
                start = a.getUpdated();
            } else if (start != null && end == null && "done".equals(a.getStatusCode())) {
                end = a.getUpdated();
                break;
            }
        }

        if (start != null && end != null) {
            return Duration.between(start, end);
        }
        return null;
    }

    // Вспомогательный метод, если нужно получить активности, отсортированные по updated
    private List<Activity> getActivitiesSortedByUpdated(Long taskId) {
        return activityRepository.findAllByTaskIdOrderByUpdatedAsc(taskId);
    }
}