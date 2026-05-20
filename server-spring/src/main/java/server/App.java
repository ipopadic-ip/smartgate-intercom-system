package server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        SpringApplication app = new SpringApplication(App.class);
        
        
        Map<String, Object> envProps = new HashMap<>();
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue()); 
            envProps.put(entry.getKey(), entry.getValue());
        });
        
        app.setDefaultProperties(envProps);
        app.run(args);

    }

}