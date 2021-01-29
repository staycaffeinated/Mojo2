version: '3.8'

services:
    application:
        container_name: ${project.applicationName}_svc
        image: ${project.applicationName}:0.0.1
        ports:
            - "8080:8080"
        healthcheck:
<#if (project.basePath)??>
            test: ["CMD", "curl", "localhost:8080${project.basePath}/_internal/health" ]
<#else>
            test: ["CMD", "curl", "localhost:8080/_internal/health" ]
</#if>
            interval: 2m
            timeout: 10s
            retries: 3
<#if (project.postgres)??>
        depends_on:
            - dbms
        environment:
            - SPRING_DATASOURCE_URL=jdbc:postgresql://dbms:5432/
            - SPRING_DATASOURCE_USERNAME=postgres
            - SPRING_DATASOURCE_PASSWORD=postgres

    dbms:
        image: 'postgres:latest'
        container_name: ${project.applicationName}_dbms
        environment:
            - POSTGRES_USER=postgres
            - POSTGRES_PASSWORD=postgres
        healthcheck:
            test: [ "CMD", "pg_isready" ]
            interval: 5s
            timeout: 3s
            retries: 30
</#if>
