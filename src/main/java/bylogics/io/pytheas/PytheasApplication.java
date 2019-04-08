package bylogics.io.pytheas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class PytheasApplication {


    public static void main(String[] args) throws Exception{

        SpringApplication.run(PytheasApplication.class, args);
        //System.out.println("Initialied...now reading the planetery info...");



    }
    private static void readBook() throws Exception {

    }

}
