package project.diary;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Diary API")
                .version("1.0.0")
                .description("Diary Management API for CRUD operations");

        return new OpenAPI().info(info);
    }
}
