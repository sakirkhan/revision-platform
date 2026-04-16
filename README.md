# Revision Platform

A Spring Boot application designed to help users revise Data Structures and Algorithms (DSA) questions by sending daily email notifications based on their preferences.

## 🏗 System Architecture

The application follows a standard layered architecture:
- **Controller Layer**: Handling HTTP requests (Future scope).
- **Service Layer**: Business logic (`QuestionService`, `UserService`, `EmailNotificationSender`).
- **Repository Layer**: Data access using Spring Data JPA.
- **Database**: MySQL, schema managed by Flyway.

## 🔄 Core Workflows

### 1. Data Initialization (`DataSeeder`)
- **When**: On application startup.
- **What**: Checks if the database is empty. If so, it seeds:
    - **Topics**: e.g., "Arrays", "DP".
    - **Questions**: Pre-defined list (e.g., NeetCode 150) stored in `DataSeeder.java`.
    - **Source Lists**: Questions are tagged with source identifiers (e.g., `neetcode_150`) stored as a comma-separated string.

### 2. User Configuration (`UserConfig`)
- Users have preferences stored in `user_configs` table:
    - `preferred_topics`: Comma-separated string (e.g., "Arrays,Graph").
    - `preferred_difficulties`: Comma-separated string (e.g., "MEDIUM,HARD").
    - `question_count_per_day`: How many questions to send daily (default: 1).
    - `notification_time`: When to send the email (default: 10:00 AM).

### 3. Daily Scheduler (`DailyNotificationScheduler`)
- **Trigger**: Runs every minute (`@Scheduled(cron = "0 * * * * *")`).
- **Logic**:
    1.  Iterates through all users.
    2.  Checks if `current_time == user.notification_time`.
    3.  If matched, calls `processUserWithRetries`.

### 4. Question Selection (`QuestionService`)
- **Logic (`getQuestionsForUser`)**:
    1.  **Fetch**: Gets all questions matching the user's preferred source list (e.g., "neetcode_150").
    2.  **Filter**:
        - Matches User's **Topics** (if set).
        - Matches User's **Difficulty** (if set).
        - **Exclude**: Removes questions sent in the last `X` days (User's revision cycle, default 30 days).
    3.  **Randomize**: Shuffles the eligible list.
    4.  **Limit**: Picks top `N` questions (based on config).

### 5. Notification (`EmailNotificationSender`)
- Uses **Spring Boot Starter Mail**.
- Sends an HTML/Text email with the selected questions, identifying the Topic and Difficulty for each.
- **Setup**: Requires `application.properties` to have valid SMTP credentials (AWS SES, Gmail, etc.).

## 🗄 Database Schema

- **`users`**: Core user profiles.
- **`user_configs`**: Preferences for revision.
- **`topics`**: Master list of topics.
- **`questions`**: DSA questions with metadata (URL, Difficulty, Source Lists).
    - *Optimization*: `source_lists` is a denormalized string column for fast reads.
- **`user_question_history`**: Logs which questions were sent to whom and when, to prevent repetition.
