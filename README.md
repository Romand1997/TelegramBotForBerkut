# Berkut Project

Berkut Project — это backend-приложение на Spring Boot, которое предоставляет REST API для работы с пользователями, сообщениями и JWT-аутентификацией, с поддержкой базы данных MySQL и запуском через Docker Compose.

## Стек технологий

- Java 17
- Spring Boot
- Spring Security + JWT
- MySQL
- Docker + Docker Compose
- Liquibase

## Запуск проекта

Скачайте и установите Docker
Установите образ MySQL
Запустите docker-compose
docker-compose up -d
Backend будет доступен по адресу: http://localhost:8888
MySQL будет доступен по порту: localhost:2345

## Проверка функционала

-----Регистрация-----

POST /auth/register
Content-Type: application/json

{
  "login": "test",
  "name": "Test User",
  "password": "123",
  "rePassword": "123"
}

-----Авторизация-----

POST /auth/login
Content-Type: application/json

{
  "login": "test",
  "password": "123"
}
Ответ:
{
  "fullName": "Test User",
  "login": "user1",
  "token": "eyJhbGciOiJIUzI1NiIs..."
}
Скопируйте token — он потребуется для следующих запросов.

-----получение токена для телеграм бота-----

GET /auth/getToken
Headers:
Authorization: Bearer <ваш_токен>
Content-Type: application/json

Ответ:
"token": "8dec7fb7-5637-..."

этот токен отправляем в сообщении боту https://web.telegram.org/k/#@berkut_message_bot
получаем ответ "Token привязан к вашему аккаунту."


----- Отправка сообщения -----

POST /api/messages
Content-Type: application/json

Headers:
Authorization: Bearer <ваш_токен>
Content-Type: application/json
Body:{
  "body": "Привет, это моё сообщение!"
}

Ответ: Сообщение отправлено

При это в Телеграмм придет сообщение:
"Test user, я получил от тебя сообщение:
Привет, это моё сообщение!"


----- Получение списка всех своих сообщений -----

GET /api/messages
Content-Type: application/json

Headers:
Authorization: Bearer <ваш_токен>
Content-Type: application/json

Ответ: [
    {
        "text": "------",
        "createdAt": "------"
    },
    {
        "text": "-----",
        "createdAt": "-------"
    }
]


