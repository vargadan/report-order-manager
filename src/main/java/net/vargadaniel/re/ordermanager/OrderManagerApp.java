package net.vargadaniel.re.ordermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.*;

@SpringBootApplication
@EnableSwagger2
@EnableResourceServer
public class OrderManagerApp {

	public static void main(String... args) {
		String k8sNamespace = System.getenv("KUBERNETES_NAMESPACE");
		if (k8sNamespace != null) {
			String profile = k8sNamespace.substring(k8sNamespace.lastIndexOf("-") + 1);
			System.setProperty("spring.profiles.active", profile);
		}
		SpringApplication.run(OrderManagerApp.class, args);
	}

	@Bean 
	public Docket orderManagerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("order-manager-api")
                .apiInfo(apiInfo())
                .select()
                .paths(allPaths())
                .build();
	}

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Report Order API")
                .description("Elastic Report Engine")
                .build();
    }	
    
    private Predicate<String> allPaths() {
        return regex("/(order|product).*");
    }
  
	
}
