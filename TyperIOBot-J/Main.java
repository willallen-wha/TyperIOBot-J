public class Main {
    
    public static void main(String[] args) {

        String nameOfFile = "raw.txt";

        try {
            //New robot object for virtual keyboard
            EnhancedRobot virt = new EnhancedRobot(nameOfFile);
            //Make sure mapping of characters to keycodes is set
            virt.ensureSet();
            //Set specific variables:
            //The default time, in miliseconds, between each keypress. Setting to 0
            //results in no random delay between keypresses, any number higher will
            //result in a uniform distribution of X length centered at X. For example,
            //setTypeSpeed(60) will create random waits between 30 and 90 ms (60ms wide
            //centered at 60)
            virt.setTypeSpeed(20);
            //Amount of time the bot takes between each action, i.e. each individual key
            //down and key up event. 0 recommended, but can be increased to avoid missed strokes.
            virt.setAutoDelay(0);
            
            virt.initiate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}