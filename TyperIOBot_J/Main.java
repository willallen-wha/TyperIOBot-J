package TyperIOBot_J;

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
        int typingSpeed = 0;

        try {
            //New robot object for virtual keyboard
            EnhancedRobot virt = new EnhancedRobot(nameOfFile, typingSpeed);
            
            virt.initiate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}