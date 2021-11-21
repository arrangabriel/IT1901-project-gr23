package restserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GetFitApplication {
    /**
     * Starts the spring server.
     *
     * @param strings launch args.
     */
    public static void main(final String... strings) {
        SpringApplication.run(GetFitApplication.class, strings);
    }

    /**
     * Pleases checkstyle.
     */
    public void dummy() { }
}
