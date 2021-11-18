package ui;

/**
 * This class is responsible for starting the application.
 * The reason for a separate class for this is that the maven
 * shading plugin can't shade for a main class that extands another.
 * Thus we have to have a separate class that runs the real main class.
 */
public class AppBoot {
    /**
     * Start the application.
     * @param args
     */
    public static void main(String[] args) {
        App.launch();
    }
}