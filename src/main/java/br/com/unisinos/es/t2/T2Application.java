package br.com.unisinos.es.t2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
public class T2Application {

    public static void main(String[] args) {
        SpringApplication.run(T2Application.class, args);
    }
}
