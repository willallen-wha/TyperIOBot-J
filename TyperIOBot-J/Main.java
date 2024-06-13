public class Main {
    
    public static void main(String[] args) {

        // Location and name of file that stores raw HTML data ripped from the site.
        String nameOfFile = "raw.txt";
        /*
         * Base time, in miliseconds, between each keypress before randomization. 
         * Setting to 0 results in no delay between keypresses, any number higher
         * will result in a uniform distribution of X length centered at X. E.G.:
         * 60 will create random waits between 30 and 90 ms (60ms wide centered at 60)
        */
        int typingSpeed = 20;
        /*
         * The amount of time, in milliseconds, between every action the robot takes.
         * This includes alt-tabs, keydown vs keyup events, everything. The recommended
         * value is 0, but can be increased for an even slower robot.
         */
        int robotDelay = 0;

        try {
            //New robot object for virtual keyboard
            EnhancedRobot virt = new EnhancedRobot(nameOfFile, typingSpeed, robotDelay);
            
            virt.initiate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}