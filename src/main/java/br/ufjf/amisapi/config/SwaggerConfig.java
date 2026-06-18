package br.ufjf.amisapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()

                .apis(RequestHandlerSelectors.basePackage("br.ufjf.amisapi.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("AMIS API - Gestão Jurídica")
                .description("API para gerenciamento de advogados, clientes, processos e tarefas.")
                .version("1.0")
                .contact(contact())
                .build();
    }

    private Contact contact(){

        return new Contact("Alvaro Domingues de Freitas",
                "https://github.com/alvaro",
                "alvaro.domingues@estudante.ufjf.br");
    }
}