package com.evbackend;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Persistence;

import com.evbackend.model.AppConstants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.util.StringUtils;

@SpringBootApplication
public class EvbackendApplication {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(EvbackendApplication.class, args);
    }

    @Bean
    public Mutiny.SessionFactory sessionFactory() {
        Map<String,Object> dbConfig = new HashMap<>();
        dbConfig.put("javax.persistence.jdbc.url", "jdbc:mysql://" + env.getProperty("MYSQL_URL"));
        dbConfig.put("javax.persistence.jdbc.user", env.getProperty("MYSQL_DB_USERNAME"));
        dbConfig.put("javax.persistence.jdbc.password", env.getProperty("MYSQL_DB_PASSWORD"));
        return Persistence.createEntityManagerFactory("ocpp1", dbConfig)
                .unwrap(Mutiny.SessionFactory.class);
    }

    @Bean
    public GroupedOpenApi usersOpenApi(@Value("${springdoc.version}") String appVersion) {
        String[] paths = { "/api/users/**", "/api/auth" };
        return GroupedOpenApi.builder().group("Users")
                .addOpenApiCustomiser(openApi -> openApi.info(new Info().title("Users API").version(appVersion)))
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi userRoleOpenApi(@Value("${springdoc.version}") String appVersion) {
        String[] paths = { "/api/userroles/**" };
        return GroupedOpenApi.builder().group("User Roles")
                .addOpenApiCustomiser(openApi -> openApi.info(new Info().title("User Roles API").version(appVersion)))
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi vehiclesOpenApi(@Value("${springdoc.version}") String appVersion) {
        String[] paths = { "/api/vehicle/**" };
        return GroupedOpenApi.builder().group("Vehicles")
                .addOpenApiCustomiser(openApi -> openApi.info(new Info().title("Vehicles API").version(appVersion)))
                .pathsToMatch(paths)
                .build();
    }
    
    @Bean
    public GroupedOpenApi chargeStationOpenApi(@Value("${springdoc.version}") String appVersion) {
        String[] paths = { "/api/chargestation/**" };
        return GroupedOpenApi.builder().group("Charge Stations")
                .addOpenApiCustomiser(openApi -> openApi.info(new Info().title("Charge Station APIs").version(appVersion)))
                .pathsToMatch(paths)
                .build();
    }
    
    @Bean
    public GroupedOpenApi chargeSiteOpenApi(@Value("${springdoc.version}") String appVersion) {
        String[] paths = { "/api/chargesite/**" };
        return GroupedOpenApi.builder().group("Charge Sites")
                .addOpenApiCustomiser(openApi -> openApi.info(new Info().title("Charge Sites APIs").version(appVersion)))
                .pathsToMatch(paths)
                .build();
    }
    

    @Bean
    public GroupedOpenApi healthOpenApi(@Value("${springdoc.version}") String appVersion) {
        String[] paths = { "/health/**" };
        return GroupedOpenApi.builder().group("Health")
                .addOpenApiCustomiser(openApi -> openApi.info(new Info().title("Health API").version(appVersion)))
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi userAuthenticationOpenApi(@Value("${springdoc.version}") String appVersion) {
        String[] paths = { "/api/login/**" };
        return GroupedOpenApi.builder().group("Authentication")
                .addOpenApiCustomiser(openApi -> openApi.info(new Info().title("Authentication").version(appVersion)))
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi accountOpenApi(@Value("${springdoc.version}") String appVersion) {
        String[] paths = { "/api/accounts/**" };
        return GroupedOpenApi.builder().group("Accounts")
                .addOpenApiCustomiser(openApi -> openApi.info(new Info().title("Accounts").version(appVersion)))
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi transactionOpenApi(@Value("${springdoc.version}") String appVersion) {
        String[] paths = { "/api/transactions/**" };
        return GroupedOpenApi.builder().group("Transactions")
                .addOpenApiCustomiser(openApi -> openApi.info(new Info().title("Transactions").version(appVersion)))
                .pathsToMatch(paths)
                .build();
    }
    
	@Bean
	public GroupedOpenApi faultsOpenApi(@Value("${springdoc.version}") String appVersion) {
		String[] paths = { "/api/faults/**" };
		return GroupedOpenApi.builder().group("Faults")
				.addOpenApiCustomiser(openApi -> openApi.info(new Info().title("Faults").version(appVersion)))
				.pathsToMatch(paths).build();
	}

    @Bean
    public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion, @Value("${springdoc.module}") String module) {
        final String apiTitle = String.format("%s API", StringUtils.capitalize(module));
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(AppConstants.SECURITY_SCHEME_NAME))
                .components(
                        new Components()
                                .addSecuritySchemes(AppConstants.SECURITY_SCHEME_NAME,
                                        new SecurityScheme()
                                                .name(AppConstants.SECURITY_SCHEME_NAME)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme(AppConstants.SCHEME)
                                                .bearerFormat(AppConstants.BEARER_FORMAT)
                                )
                )
                .info(new Info().title(apiTitle).version(appVersion));
    }

}

