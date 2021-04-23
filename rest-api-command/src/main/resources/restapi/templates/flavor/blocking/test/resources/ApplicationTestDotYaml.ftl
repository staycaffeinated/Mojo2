spring:
    profiles: test
<#if (project.postgres)??>
    datasource:
        driver-class-name: org.testcontainers.jdbc.ContainerDatabaseDriver
    <#noparse>
        url: ${embedded.postgresql.url}
        username: ${embedded.postgresql.user}
        password: ${embedded.postgresql.password}
        initialization-mode: embedded
    </#noparse>
    jpa:
        database-platform: org.hibernate.dialect.PostgreSQLDialect
        generate-ddl: true
<#else> <#-- H2 configuration -->
    datasource:
        driver-class-name: org.h2.Driver
        url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
        username: sa
        password: password
        initialization-mode: embedded
    jpa:
        database-platform: org.hibernate.dialect.H2Dialect
        generate-ddl: true
</#if>
    resources:
        add-mappings: true
    h2:
        console:
            enabled: true
            path: /h2-console
            settings:
                trace: false
                web-allow-others: false
