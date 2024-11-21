Описание
Система для управления задачами с использованием Java Spring, PostgreSQL, и Docker Compose.

Запуск
Клонируйте репозиторий:
(https://github.com/nursinhio/task_manager_backend.git)

POSTGRES_USERNAME=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DB=tm_data
Запустите проект:

bash
docker-compose up --build
Доступ:

Backend: http://localhost:8081


Frontend: https://github.com/nursinhio/frontend_task_manager.git
Остановка:

bash
Copy code
docker-compose down
Структура проекта
Backend: Java Spring
Database: PostgreSQL (порт 5432)
