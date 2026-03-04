# ⚙️ Konfiguracja Aplikacji

## Spis Treści

1. [application.yaml](#applicationyaml)
2. [build.gradle](#buildgradle)
3. [Zmienne Systemowe](#zmienne-systemowe)
4. [Profilowanie (Profiles)](#profilowanie-profiles)
5. [Limity i Timeout'y](#limity-i-timeouty)
6. [Logging](#logging)

---

## application.yaml

### Plik Konfiguracyjny

**Lokalizacja:** `src/main/resources/application.yaml`

```yaml
spring:
  application:
    name: docsCenterGradleGenerated
  
  servlet:
    multipart:
      max-file-size: 2500MB
      max-request-size: 2500MB
  
  datasource:
    url: jdbc:sqlite:/data/storage.db
    driver-class-name: org.sqlite.JDBC
  
  jpa:
    database-platform: org.hibernate.community.dialect.SQLiteDialect
    hibernate:
      ddl-auto: update
    show-sql: true
```

### Sekcja: Application

```yaml
spring:
  application:
    name: docsCenterGradleGenerated
```

| Parametr | Wartość | Opis |
|----------|---------|------|
| `name` | `docsCenterGradleGenerated` | Nazwa aplikacji (widoczna w logach) |

### Sekcja: Servlet (Multipart)

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 2500MB
      max-request-size: 2500MB
```

| Parametr | Wartość | Opis |
|----------|---------|------|
| `max-file-size` | `2500MB` | Maks. rozmiar jednego pliku |
| `max-request-size` | `2500MB` | Maks. rozmiar całego requesta |

**Notatka:** Wartości muszą być identyczne, inaczej będą konflikty.

### Sekcja: DataSource (Database)

```yaml
spring:
  datasource:
    url: jdbc:sqlite:/data/storage.db
    driver-class-name: org.sqlite.JDBC
```

| Parametr | Wartość | Opis |
|----------|---------|------|
| `url` | `jdbc:sqlite:/data/storage.db` | Ścieżka do bazy SQLite |
| `driver-class-name` | `org.sqlite.JDBC` | JDBC driver SQLite |

**Ścieżka Bazy Danych:**
```
/data/storage.db
│
├─ Relative path: ./data/storage.db (uruchamianie z głównego folderu)
├─ Absolute path: C:\Users\User\Projects\docsCenterGradleGenerated\data\storage.db
└─ URL: jdbc:sqlite:/data/storage.db (dla Spring Boot)
```

### Sekcja: JPA/Hibernate

```yaml
spring:
  jpa:
    database-platform: org.hibernate.community.dialect.SQLiteDialect
    hibernate:
      ddl-auto: update
    show-sql: true
```

| Parametr | Wartość | Opis |
|----------|---------|------|
| `database-platform` | `SQLiteDialect` | Dialect Hibernate dla SQLite |
| `ddl-auto` | `update` | Automatyczne tworzenie/aktualizowanie tabel |
| `show-sql` | `true` | Wypisywanie SQL w logach (DEBUG) |

**DDL-AUTO Opcje:**
```
- update    : Zmienia schemat (dodaje nowe kolumny/tabele) ✅ BEZPIECZNY
- create    : Usuwa i odtwarza schemat (NIEBEZPIECZNY - zgubisz dane!)
- drop      : Usuwa schemat (BARDZO NIEBEZPIECZNY!)
- validate  : Waliduje schemat (bez zmian)
- none      : Brak zmian (wymaga ręcznych migracji)
```

---

## build.gradle

### Gradle Build File

**Lokalizacja:** `build.gradle` (root directory)

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '4.0.2'
    id 'io.spring.dependency-management' version '1.1.7'
}

group = 'qbsss.docsCenter'
version = '0.0.1-SNAPSHOT'
description = 'Demo project for Spring Boot'

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starters
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    
    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
    
    // ORM
    implementation 'org.hibernate.orm:hibernate-community-dialects:7.2.1.Final'
    
    // Markdown Parser
    implementation 'org.commonmark:commonmark:0.22.0'
    
    // Database Driver
    implementation 'org.xerial:sqlite-jdbc:3.42.0.0'
}

tasks.named('test') {
    useJUnitPlatform()
}

task dockerRun(type: Exec) {
    group = 'docker'
    description = 'Build JAR, stop previous container, start new Docker container'
    
    dependsOn 'bootJar'
    
    commandLine 'cmd', '/c', """
        docker-compose -f ${projectDir}/docker-compose.yml down && 
        docker-compose -f ${projectDir}/docker-compose.yml up --build -d
    """
}
```

### Plugins

| Plugin | Wersja | Opis |
|--------|--------|------|
| `java` | built-in | Java language support |
| `org.springframework.boot` | 4.0.2 | Spring Boot plugin |
| `io.spring.dependency-management` | 1.1.7 | Dependency management |

### Project Metadata

```groovy
group = 'qbsss.docsCenter'              // Group ID (Maven)
version = '0.0.1-SNAPSHOT'              // Semantic versioning
description = 'Demo project for Spring Boot'
```

### Repositories

```groovy
repositories {
    mavenCentral()                      // Maven Central Repository
}
```

### Dependencies

#### Sekcja: Web & MVC
```groovy
implementation 'org.springframework.boot:spring-boot-starter-web'
```
- Spring MVC
- Embedded Tomcat
- HTTP Server

#### Sekcja: Templates
```groovy
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
```
- Thymeleaf template engine
- HTML templates support

#### Sekcja: Data Access
```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.hibernate.orm:hibernate-community-dialects:7.2.1.Final'
```
- Spring Data JPA
- Hibernate ORM
- SQLite dialect

#### Sekcja: Markdown
```groovy
implementation 'org.commonmark:commonmark:0.22.0'
```
- CommonMark parser
- Markdown → HTML conversion

#### Sekcja: Database Driver
```groovy
implementation 'org.xerial:sqlite-jdbc:3.42.0.0'
```
- SQLite JDBC driver
- Database connectivity

#### Sekcja: Testing
```groovy
testImplementation 'org.springframework.boot:spring-boot-starter-test'
testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
```
- JUnit 5
- Mockito
- AssertJ

### Custom Tasks

#### dockerRun Task
```groovy
task dockerRun(type: Exec) {
    description = 'Build JAR, stop previous container, start new Docker container'
    dependsOn 'bootJar'
    commandLine 'cmd', '/c', """
        docker-compose down && 
        docker-compose up --build -d
    """
}
```

**Użycie:**
```bash
./gradlew dockerRun
```

---

## Zmienne Systemowe

### Java System Properties

```bash
# Ustawianie przy uruchomieniu
java -Dkey=value -jar application.jar

# Przykład
java -Djava.io.tmpdir=/tmp/storage -jar docsCenterGradleGenerated.jar
```

### Environment Variables

```bash
# Linux/Mac
export SPRING_DATASOURCE_URL=jdbc:sqlite:/custom/path/storage.db
./gradlew bootRun

# Windows
set SPRING_DATASOURCE_URL=jdbc:sqlite:/custom/path/storage.db
gradlew.bat bootRun
```

### Application Properties Override

**Hierarchia (od najwyższego do najniższego priorytetu):**

1. Command line arguments
2. Environment variables (`SPRING_*`)
3. `application-{profile}.yaml`
4. `application.yaml`
5. Default values

**Przykład:**
```bash
# Override database URL
java -jar app.jar --spring.datasource.url=jdbc:sqlite:custom.db

# Override port
java -jar app.jar --server.port=9090
```

---

## Profilowanie (Profiles)

### Profil Development

**Plik:** `application-dev.yaml`

```yaml
spring:
  datasource:
    url: jdbc:sqlite:./dev-storage.db
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: create-drop  # Recreate on startup

server:
  port: 8080
```

**Uruchomienie:**
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Profil Production

**Plik:** `application-prod.yaml`

```yaml
spring:
  datasource:
    url: jdbc:sqlite:/var/lib/app/storage.db
  jpa:
    show-sql: false         # Don't log SQL in production
    hibernate:
      ddl-auto: validate    # Only validate, don't modify

server:
  port: 8080
  
logging:
  level:
    root: INFO
```

**Uruchomienie:**
```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
```

---

## Limity i Timeout'y

### Upload File Limits

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 2500MB          # Max size per file
      max-request-size: 2500MB       # Max size per request
      file-size-threshold: 1MB       # Size above which files are written to disk
```

### Connection Timeout

```yaml
spring:
  datasource:
    hikari:
      connection-timeout: 30000      # 30 seconds
      idle-timeout: 600000           # 10 minutes
      max-lifetime: 1800000          # 30 minutes
      maximum-pool-size: 10          # Connection pool size
```

### Request Timeout

```yaml
server:
  tomcat:
    connection-timeout: 60000        # 60 seconds
    keep-alive-timeout: 60000
```

---

## Logging

### Logging Configuration

```yaml
logging:
  level:
    root: INFO                        # Global level
    qbsss.docsCenter: DEBUG           # Application level
    org.springframework: INFO         # Spring level
    org.hibernate: DEBUG              # Hibernate level
  
  pattern:
    console: "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  
  file:
    name: logs/application.log
    max-size: 10MB
    max-history: 10
```

### Log Levels

| Level | Opis |
|-------|------|
| `TRACE` | Very detailed information (DEBUG < TRACE) |
| `DEBUG` | Detailed information for developers |
| `INFO` | General informational messages |
| `WARN` | Warning messages (potential issues) |
| `ERROR` | Error messages (something went wrong) |
| `FATAL` | Fatal errors (application crash) |

### Loggers

```yaml
# Application logs
logging:
  level:
    qbsss.docsCenter.docsCenterGradleGenerated: DEBUG
    qbsss.docsCenter.docsCenterGradleGenerated.service: DEBUG
    qbsss.docsCenter.docsCenterGradleGenerated.contoller: DEBUG

# Spring Framework logs
logging:
  level:
    org.springframework: INFO
    org.springframework.web: DEBUG
    org.springframework.boot: INFO

# Hibernate logs
logging:
  level:
    org.hibernate: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

---

## Configuration by Environment

### Development Environment

```yaml
# application-dev.yaml
spring:
  datasource:
    url: jdbc:sqlite:./dev-storage.db
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: create-drop

logging:
  level:
    root: DEBUG
```

### Testing Environment

```yaml
# application-test.yaml
spring:
  datasource:
    url: jdbc:h2:mem:test  # In-memory H2
  jpa:
    hibernate:
      ddl-auto: create-drop

logging:
  level:
    root: INFO
```

### Production Environment

```yaml
# application-prod.yaml
spring:
  datasource:
    url: jdbc:sqlite:/var/lib/docscenter/storage.db
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate

logging:
  level:
    root: WARN
```

---

## Best Practices

### 1. Bezpieczeństwo

```yaml
# ❌ NIE rób tego
spring:
  datasource:
    username: admin
    password: admin123
    url: jdbc:sqlite:/data/storage.db

# ✅ Rób to - użyj zmiennych środowiskowych
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${DB_USER}
    password: ${DB_PASSWORD}
```

### 2. Profilowanie

```bash
# Zawsze używaj profili dla różnych środowisk
./gradlew bootRun --args='--spring.profiles.active=dev'
./gradlew bootRun --args='--spring.profiles.active=prod'
```

### 3. Logging

```yaml
# Development: DEBUG
# Production: WARN lub ERROR
logging:
  level:
    root: ${LOGGING_LEVEL:WARN}
```

### 4. Database

```yaml
# Nigdy nie używaj ddl-auto: create-drop w produkcji!
# Zawsze backupuj storage.db przed zmianami
```

---

**Koniec: KONFIGURACJA.md**

