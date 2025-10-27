package com.mongodb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
class ConverterCheck implements CommandLineRunner {

    @Autowired
    ApplicationContext context;

    @Override
    public void run(String... args) {
        var converters = context.getBeansOfType(HttpMessageConverter.class);
        System.out.println("=== Loaded Message Converters ===");
        converters.values().forEach(c -> System.out.println(" - " + c.getClass().getName()));
        System.out.println("================================");
    }
}
