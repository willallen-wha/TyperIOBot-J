import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
// import java.awt.image.BufferedImage;
// import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
// import javax.imageio.ImageIO;
// import java.awt.Rectangle;
// import java.awt.Toolkit;
// import java.awt.MouseInfo;

public class EnhancedRobot extends Robot{

    // final Rectangle screen;
    final String dumpFileName;
    private int typeSpeedConst;

    public EnhancedRobot(String dump, int speed, int delay) throws AWTException {
        super();
        // int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        // int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        // screen = new Rectangle(0, 0, screenWidth, screenHeight);
        // File where the raw data will be dumped
        dumpFileName = dump;
        // Speed between letters during typing
        typeSpeedConst = speed;
        // Speed between all actions including alt-tabs, etc.
        setAutoDelay(delay);
        // Initialize the decoder with a blank keycode request
        Decoder.getKeycode(' ');
    }

    //Press and release a key instantly
    public void keyType(int keycode) {
        keyPress(keycode);
        keyRelease(keycode);
    }

    public void keyType(char c) {
        //If it's uppercase or a special character press shift then type
        boolean needShiftPressed = Decoder.needsShift(c);
        if(needShiftPressed) {
            keyPress(KeyEvent.VK_SHIFT);
        }

        //Type the char
        keyType(Decoder.getKeycode(c));

        if(needShiftPressed) {
            keyRelease(KeyEvent.VK_SHIFT);
        }
    }

    public void mouseClick(int buttons) {
        mousePress(buttons);
        mouseRelease(buttons);
    }

    //Types a string input into the virtual keyboard
    public void typeString(String toType) {
        char[] keys = toType.toCharArray();
        for(char key: keys) {
            customDelay();
            keyType(key);
        }
    }

    // Custom delay function to allow random time between letter presses
    // This theoretically makes the botting less obvious.
    public void customDelay() {
        // If no delay is requested simply continue
        if(typeSpeedConst == 0) {
            return;
        }
        // Create a skew factor between 0 and half of the delay time
        int adjust = 1;
        adjust *= (Math.random() * (typeSpeedConst / 2));
        // Determine if skew is positive or negative
        if(Math.random() * 2 > 1) adjust *= -1;
        // Adjust by skew and delay
        delay(typeSpeedConst + adjust);
        System.out.println("Delayed for " + (typeSpeedConst + adjust) + " milliseconds.");
    }

    //Simulates an alt-tab action
    public void altTab() {
        keyPress(KeyEvent.VK_ALT);
        keyPress(KeyEvent.VK_TAB);
        keyRelease(KeyEvent.VK_TAB);
        keyRelease(KeyEvent.VK_ALT);
    }

    //Simulates multiple alt-tab actions
    public void altTab(int times) {
        keyPress(KeyEvent.VK_ALT);
        while(times > 0) {
            keyType(KeyEvent.VK_TAB);
            delay(10);
            times--;
        }
        keyRelease(KeyEvent.VK_ALT);
    }

    // public BufferedImage takeScreenshot() {
    //     BufferedImage screenshot = createScreenCapture(screen);
    //     //Write the screenshot to a file
    //     try {
    //         File out = new File("screen.jpg");
    //         ImageIO.write(screenshot, "jpg", out);
    //     } catch(Exception e) {
    //         e.printStackTrace();
    //     }
    //     return screenshot;
    // }

    // public BufferedImage takeScreenshotFromMouse() {
    //     Rectangle toCapture = new Rectangle(getMouseX(), getMouseY(), 750, 750);
    //     BufferedImage screenshot = createScreenCapture(toCapture);
    //     //Write the screenshot to a file
    //     try {
    //         File out = new File("screen.jpg");
    //         ImageIO.write(screenshot, "jpg", out);
    //     } catch(Exception e) {
    //         e.printStackTrace();
    //     }
    //     return screenshot;
    // }

    // public int getMouseX() {
    //     return (int) MouseInfo.getPointerInfo().getLocation().getX();
    // }

    // public int getMouseY() {
    //     return (int) MouseInfo.getPointerInfo().getLocation().getY();
    // }

    public void initiate() throws FileNotFoundException {
        // Wait a seconds to allow mouse to be placed
        delay(1000);
        // Now, right click and rip the HTML
        // Runs the series of keystrokes right-click, C, C,
        // Enter, Enter, Enter to navigate the right click menu to
        // "Copy Element" option and get just Gameboard_container data
        mouseClick(InputEvent.getMaskForButton(3));
        delay(60);
        keyType(KeyEvent.VK_C);
        delay(15);
        keyType(KeyEvent.VK_C);
        delay(15);
        keyType(KeyEvent.VK_ENTER);
        delay(15);
        keyType(KeyEvent.VK_ENTER);
        //Extra enter just in case
        delay(15);
        keyType(KeyEvent.VK_ENTER);
        delay(15);
        // Now that we've got it ripped, put it in the text file
        // Alt-tab into the open text file which should be "logically" behind
        // two alt-tabs: the first is whatever is running the code, the second
        // reaches the text file
        altTab(2);
        delay(25);
        // Now paste and save by selecting all, backspacing, pasting,
        // and saving the text file.
        all();
        delay(25);
        keyType(KeyEvent.VK_BACK_SPACE);
        delay(25);
        paste();
        delay(25);
        save();
        delay(25);
        // Now that it's been saved, parse that HTML and refocus the game window
        altTab(1);
        String toType = RawParser.parseRaw(dumpFileName).trim();
        System.out.println("String to be typed: " + toType);
        // Let the mouse get situated then get ready to go
        delay(1500);
        mouseClick(InputEvent.getMaskForButton(1));
        delay(10);
        // Type a space, wait 3 seconds for the round to start, then let er rip
        keyType(' ');
        // If you're trying to do anything other than the solo text snippets,
        // change this number to be the right length.
        delay(4500);
        typeString(toType);
    }

    // Shorthand function for pressing control and V to paste
    public void paste() {
        keyPress(KeyEvent.VK_CONTROL);
        keyType(KeyEvent.VK_V);
        keyRelease(KeyEvent.VK_CONTROL);
    }

    // Shorthand function for pressing control and S to save
    public void save() {
        keyPress(KeyEvent.VK_CONTROL);
        keyType(KeyEvent.VK_S);
        keyRelease(KeyEvent.VK_CONTROL);
    }

    // Shorthand function for pressing control and A to select all
    public void all() {
        keyPress(KeyEvent.VK_CONTROL);
        keyType(KeyEvent.VK_A);
        keyRelease(KeyEvent.VK_CONTROL);
    }

}

class Decoder {
    final static Character[] specialCharsArray =    {'~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', 'Q', 'W', 'E',
                                                     'R', 'T', 'Y', 'U', 'I', 'O', 'P', '{', '}', '|', 'A', 'S', 'D', 'F', 'G', 'H',
                                                     'J', 'K', 'L', ':', '\"', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '<', '>', '?'};
    final static Character[] nonSpecialCharsArray = {'`', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '=', 'q', 'w', 'e',
                                                     'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']', '\\', 'a', 's', 'd', 'f', 'g', 'h',
                                                     'j', 'k', 'l', ';', '\'', 'z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.', '/', ' ', '\n'};
    final static HashSet<Character> specialChars = new HashSet<>(Arrays.asList(specialCharsArray));
    final static HashSet<Character> nonSpecialChars = new HashSet<>(Arrays.asList(nonSpecialCharsArray));

    static HashMap<Character, Integer> keyCodes;
    static HashMap<Character, Character> specialToNon;

    public static int getKeycode(char c) {
        if(keyCodes == null) {
            setupKeyCodes();
        }
        return keyCodes.get(decode(c));
    }

    private static void setupKeyCodes() {
        keyCodes = new HashMap<>();
        keyCodes.put('`', KeyEvent.VK_BACK_QUOTE);
        keyCodes.put('1', KeyEvent.VK_1);
        keyCodes.put('2', KeyEvent.VK_2);
        keyCodes.put('3', KeyEvent.VK_3);
        keyCodes.put('4', KeyEvent.VK_4);
        keyCodes.put('5', KeyEvent.VK_5);
        keyCodes.put('6', KeyEvent.VK_6);
        keyCodes.put('7', KeyEvent.VK_7);
        keyCodes.put('8', KeyEvent.VK_8);
        keyCodes.put('9', KeyEvent.VK_9);
        keyCodes.put('0', KeyEvent.VK_0);
        keyCodes.put('-', KeyEvent.VK_MINUS);
        keyCodes.put('=', KeyEvent.VK_EQUALS);
        keyCodes.put('q', KeyEvent.VK_Q);
        keyCodes.put('w', KeyEvent.VK_W);
        keyCodes.put('e', KeyEvent.VK_E);
        keyCodes.put('r', KeyEvent.VK_R);
        keyCodes.put('t', KeyEvent.VK_T);
        keyCodes.put('y', KeyEvent.VK_Y);
        keyCodes.put('u', KeyEvent.VK_U);
        keyCodes.put('i', KeyEvent.VK_I);
        keyCodes.put('o', KeyEvent.VK_O);
        keyCodes.put('p', KeyEvent.VK_P);
        keyCodes.put('[', KeyEvent.VK_OPEN_BRACKET);
        keyCodes.put(']', KeyEvent.VK_CLOSE_BRACKET);
        keyCodes.put('\\', KeyEvent.VK_BACK_SLASH);
        keyCodes.put('a', KeyEvent.VK_A);
        keyCodes.put('s', KeyEvent.VK_S);
        keyCodes.put('d', KeyEvent.VK_D);
        keyCodes.put('f', KeyEvent.VK_F);
        keyCodes.put('g', KeyEvent.VK_G);
        keyCodes.put('h', KeyEvent.VK_H);
        keyCodes.put('j', KeyEvent.VK_J);
        keyCodes.put('k', KeyEvent.VK_K);
        keyCodes.put('l', KeyEvent.VK_L);
        keyCodes.put(';', KeyEvent.VK_SEMICOLON);
        keyCodes.put('\'', KeyEvent.VK_QUOTE);
        keyCodes.put('\n', KeyEvent.VK_ENTER);
        keyCodes.put('z', KeyEvent.VK_Z);
        keyCodes.put('x', KeyEvent.VK_X);
        keyCodes.put('c', KeyEvent.VK_C);
        keyCodes.put('v', KeyEvent.VK_V);
        keyCodes.put('b', KeyEvent.VK_B);
        keyCodes.put('n', KeyEvent.VK_N);
        keyCodes.put('m', KeyEvent.VK_M);
        keyCodes.put(',', KeyEvent.VK_COMMA);
        keyCodes.put('.', KeyEvent.VK_PERIOD);
        keyCodes.put('/', KeyEvent.VK_SLASH);
        keyCodes.put(' ', KeyEvent.VK_SPACE);
    }

    private static void setupDecode() {
        specialToNon = new HashMap<>();
        for(int i = 0; i < specialCharsArray.length; i++) {
            specialToNon.put(specialCharsArray[i], nonSpecialCharsArray[i]);
        }
    }

    private static char decode(char c) {
        if(specialToNon == null) setupDecode();
        if(nonSpecialChars.contains(c)) {
            return c;
        }
        else {
            return specialToNon.get(c);
        }
    }

    public static boolean needsShift(char c) {
        return specialChars.contains(c);
    }
}