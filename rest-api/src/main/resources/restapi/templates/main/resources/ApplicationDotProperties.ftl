spring.application.name=${project.applicationName}
server.port=8080

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.id.new_generator_mappings=false

# POSTGRES
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/${project.applicationName}

# MYSQL
# spring.datasource.driver-class-name=com.mysql.jdbc.Driver
# spring.datasource.url=jdbc:mysql://localhost:3306/appdb

spring.datasource.username=root
spring.datasource.password=secret
