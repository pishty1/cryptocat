package com.cryptocat.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@SpringBootApplication
public class FrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontApplication.class, args);
    }

    @Bean
    Web4Client getWebClient() {
        WebClient build = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();

        HttpServiceProxyFactory build1 = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(build)).build();
        return build1.createClient(Web4Client.class);
    }
}

@Controller
class FrontController {

	private final Web4Client web4Client;
    private final Storage storage;

    public FrontController(Web4Client web4Client, Storage storage) {
        this.web4Client = web4Client;
        this.storage = storage;
    }

    @GetMapping("/hi")
    String getHi(Map<String, Object> model) {
        System.out.println("printing count");
        model.put("blocks", web4Client.getBlocks());
        return "mytemplate";
    }

    @PostMapping("/furtherInfo")
    public String furtherInfo() {
        return "furtherInfo";
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Block> stream() {
        return Flux.interval(Duration.ofSeconds(5)).map(
                value -> storage.latestBlock
        );
    }
}

@Configuration
@Profile("local")
class LocalDevConfig {

    public LocalDevConfig(final TemplateEngine templateEngine) throws IOException {
        File sourceRoot = new ClassPathResource("application.yml").getFile().getParentFile();
        while (sourceRoot.listFiles((dir, name) -> name.equals("gradlew")).length != 1) {
            sourceRoot = sourceRoot.getParentFile();
        }
        final FileTemplateResolver fileTemplateResolver = new FileTemplateResolver();
        fileTemplateResolver.setPrefix(sourceRoot.getPath() + "/src/main/resources/templates/");
        fileTemplateResolver.setSuffix(".html");
        fileTemplateResolver.setCacheable(false);
        fileTemplateResolver.setCharacterEncoding("UTF-8");
        fileTemplateResolver.setCheckExistence(true);

        templateEngine.setTemplateResolver(fileTemplateResolver);
    }
}