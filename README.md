## [REST API](http://localhost:8080/doc)

## Концепция:

- Spring Modulith
    - [Spring Modulith: достигли ли мы зрелости модульности](https://habr.com/ru/post/701984/)
    - [Introducing Spring Modulith](https://spring.io/blog/2022/10/21/introducing-spring-modulith)
    - [Spring Modulith - Reference documentation](https://docs.spring.io/spring-modulith/docs/current-SNAPSHOT/reference/html/)

```
  url: jdbc:postgresql://localhost:5432/jira
  username: jira
  password: JiraRush
```

- Есть 2 общие таблицы, на которых не fk
    - _Reference_ - справочник. Связь делаем по _code_ (по id нельзя, тк id привязано к окружению-конкретной базе)
    - _UserBelong_ - привязка юзеров с типом (owner, lead, ...) к объекту (таска, проект, спринт, ...). FK вручную будем
      проверять

## Аналоги

- https://java-source.net/open-source/issue-trackers

## Тестирование

- https://habr.com/ru/articles/259055/

Список выполненных задач:
- Разобраться со структурой проекта (onboarding)

- Удалить социальные сети vk, yandex

- Вынести чувствительную информацию в отдельный properties-файл, считывать из переменных окружения

- Написать тесты для всех публичных методов ProfileRestController

- Рефакторинг метода FileUtil.upload

- Добавить функционал тегов к задаче (REST API + сервис)

- Добавить подсчёт времени задачи в работе и тестировании

- Написать Dockerfile для основного сервера

- Написать docker-compose для запуска сервера с БД и nginx

- Добавить локализацию минимум на двух языках для шаблонов писем и стартовой страницы

- Заменить механизм аутентификации с JSESSIONID на JWT