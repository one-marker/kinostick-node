package kinostick.web.configurations.swagger;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicUserApi() {
        return GroupedOpenApi.builder()
                .group("web-module")
                .packagesToScan("kinostick.web.controllers")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI(@Value("${app.title}") String title,
                @Value("${app.version}") String version) {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .version(version)
                );

    }

//


}
