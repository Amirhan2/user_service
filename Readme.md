# Использование Swagger
## Определение 

OpenApi (ранее известный как Swagger) - это спецификация для создания и документирования RESTfull API.

Она позволяет описывать операции, входные данные и т.д. Эта документация может использоваться разработчиками и пользователями дял понимания и взаимодействия с API.

В приложении Spring Boot OpenAPI может использоваться для автоматической генерации документации для REST API

## Внедрение зависимости

Используем аннотацию: 
implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2)
Внедрив эту аннотацию мы уже можем открыть сваггер (при условии, что контроллер готов, и микросервис запущен) по этой ссылке - http://localhost:8080/swagger-ui/index.html 

## Настройка описания 

*1 вариант*

Можем повесить аннотацию @OpenAPIDefinition() на главный класс и заполнить его по примеру ниже (такой вариант уже есть в user_service)
```java
@OpenAPIDefinition(
    info = @Info(
        title = "User Service",
        version = "1.0.0")
)
```

*2 вариант (необязательно)*

Либо создать отдельный класс с конфигурацией 
```java
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("user_service")
                        .description("Микросервис для работы с пользователями")
                        .version("v0.1")
                        .contact(new Contact()
                                .name("имя")
                                .email("почта")
                                .url("почта"))
                        .license(new License()
                                .name("имя")
                                .url("почта")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local server"),
                        new Server().url("https://api.company.com").description("Production server")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
```

info - добавляем информацию о вашем API, включая название, описанеи, версию, контактные данные и лицензию.

servers - указываем серверы, на которых будет доступен ваш API. Это может быть токальный сервер для разработки и продакшн сервер

security - добавляем конфигурацию безопасности для авторизации

components - добавляем схему безопасности для использования в API

## Настройка контроллера

1) На класс вешаем аннотацию @Tag с двумя параметрами
```java
@Tag(name = "Пример контроллера", description = "Описание контроллера")
public class MentorshipRequestController {}
```
2) На каждый метод вешаем аннотацию @Operation с двумя параметрами. И аннотацию @ApiOperation, где в параметре указываем код ошибки и сообщение
```java
@PostMapping
    @Operation(summary = "Приветственный метод 111", description = "Возвращает приветственное сообщение 1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция успешно заершена"),
            @ApiResponse(responseCode = "404", description = "Не найдено")
    })
    public ResponseEntity<MentorshipRequestDto> requestMentorship(@RequestBody @Valid MentorshipRequestDto mentorshipRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mentorshipRequestService.requestMentorship(mentorshipRequestDto));
    }
```

## Преимущества работы со swagger

Работа с Swagger (и его современной версией OpenAPI) имеет множество преимуществ, особенно в контексте разработки и документирования RESTful API. Вот основные из них:

1) Автоматическая генерация документации:
Swagger позволяет автоматически генерировать подробную документацию для ваших API на основе аннотаций или конфигурационных файлов. Это упрощает процесс поддержания документации в актуальном состоянии.

2) Интерактивная документация:
Swagger UI предоставляет удобный веб-интерфейс, где разработчики могут взаимодействовать с вашим API. Они могут отправлять запросы и просматривать ответы прямо из браузера, что значительно упрощает тестирование и понимание API.

3) Повышенная ясность и стандартизация:
Swagger использует стандартный формат для описания API, что упрощает понимание и интеграцию для других разработчиков. Четкая документация помогает избежать недоразумений и ошибок.

4) Автоматическая генерация кода:
С помощью Swagger можно автоматически генерировать клиентский код для различных языков программирования. Это ускоряет процесс интеграции вашего API с другими системами и приложениями.

5) Легкость тестирования:
Swagger UI позволяет быстро протестировать API, отправляя различные запросы и проверяя ответы. Это облегчает процесс выявления и устранения ошибок на ранних стадиях разработки.

6) Улучшенная коммуникация между командами:
Подробная документация, генерируемая Swagger, облегчает коммуникацию между командами разработки, тестирования и документации. Все участники проекта имеют доступ к единому источнику информации о функциональности API.

7) Поддержка версионирования:
Swagger поддерживает версионирование API, что позволяет легко управлять различными версиями вашего API и обеспечивать обратную совместимость.

8) Удобство для разработчиков:
Наличие хорошо структурированной и подробной документации снижает порог входа для новых разработчиков, позволяя им быстрее начать работу с вашим API.

9) Интеграция с CI/CD:
Swagger можно интегрировать с системами непрерывной интеграции и непрерывного развертывания (CI/CD), что позволяет автоматически генерировать и обновлять документацию при каждом развертывании.

10) Снижение числа ошибок:
Благодаря стандартизированному описанию API, вероятность возникновения ошибок при интеграции значительно снижается. Разработчики четко понимают, какие запросы можно отправлять и какие ответы ожидать.

Использование Swagger (OpenAPI) в разработке API приносит значительные преимущества, повышая эффективность разработки, улучшая качество документации и облегчая интеграцию с другими системами.