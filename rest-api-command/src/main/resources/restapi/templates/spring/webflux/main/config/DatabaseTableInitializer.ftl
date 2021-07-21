<#include "/common/Copyright.ftl">
package ${endpoint.basePackage}.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

/**
 * This is an example of how to create and initialize database tables.
 * There are one of these classes per entity type, for the sake of keeping the code generation simple.
 */
@Configuration
@EnableR2dbcRepositories
@EnableR2dbcAuditing
public class ${endpoint.entityName}TableInitializer {

	@Bean
	public ConnectionFactoryInitializer databaseInitializer(ConnectionFactory connectionFactory) {
		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectionFactory);

		CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
		populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("database/${endpoint.entityVarName}-schema.sql")));
		populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("database/${endpoint.entityVarName}-data.sql")));
		initializer.setDatabasePopulator(populator);
		return initializer;
	}
}