package server;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(App.class);
        

        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        
        Map<String, Object> envProps = new HashMap<>();
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue()); 
            envProps.put(entry.getKey(), entry.getValue());
        });
        
        app.setDefaultProperties(envProps);
        app.run(args);

    }

}