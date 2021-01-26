
<#if (project.applicationName)??>
spring.application.name=${project.applicationName}
<#else>
spring.application.name=example-service
</#if>
server.port=8080

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.id.new_generator_mappings=false

<#-- define the jdbc driver -->
<#if (project.postgres)??>
# POSTGRES
# spring.datasource.driver-class-name=org.postgresql.Driver
<#else>
# H2
spring.datasource.driver-class-name=org.h2.Driver
</#if>

<#-- define the jdbc url -->
<#if (project.postgres)??>
    <#if (project.schema)??>
spring.datasource.url=jdbc:postgresql://localhost:5432/${project.schema}
    <#elseif (project.applicationName)??>
spring.datasource.url=jdbc:postgresql://localhost:5432/${project.applicationName}
    <#else>
spring.datasource.url=jdbc:postgresql://localhost:5432/testdb
    </#if>
<#else>
    <#if (project.schema)??>
spring.datasource.url=jdbc:h2:~/${project.schema}
    <#elseif (project.applicationName)??>
spring.datasource.url=jdbc:h2:~/${project.applicationName}
    <#else>
spring.datasource.url=jdbc:h2:~/testdb
    </#if>
</#if>


# MYSQL
# spring.datasource.driver-class-name=com.mysql.jdbc.Driver
# spring.datasource.url=jdbc:mysql://localhost:3306/appdb

spring.datasource.username=root
spring.datasource.password=secret
