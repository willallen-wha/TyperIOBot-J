package TyperIOBot_J;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
// import java.awt.image.BufferedImage;
// import java.io.File;
// import javax.imageio.ImageIO;
// import java.awt.Rectangle;
// import java.awt.Toolkit;
// import java.awt.MouseInfo;

/**
 * Public class to implement specific functions using the {@link java.awt.Robot}
 * class as a baseline. Specifically implements certain features such as alt-tabbing,
 * converting strings into a series of keystrokes, etc. Requires a location to
 * get raw HTML text from and has customizable delay to make the TAS less obvious.
 * <p>
 * Also self-contains a custom Decoder class, which takes a character and decodes
 * it into a series of keystrokes to tpye the character. Supports most keys which
 * tpye a character on a keyboard. Additionally, checks if character would require
 * shift to type, such as a capital letter or the '^' symbol typed with shift and 6.
 * Decoder is, in fact, just a hashmap between characters and keystrokes with a
 * couple added features.
 * <p>
 * The two main errors you would expect to see are {@link AWTException}, which is
 * inherited from {@link java.awt.Robot}, and {@link FileNotFoundException}, which
 * it recieves from its connection to the {@link RawParser} class.
 * 
 * @see RawParser
 * @see java.awt.Robot
 */
public class EnhancedRobot extends Robot{

    // final Rectangle screen;
    final String dumpFileName;
    private final int typeSpeedConst;

    /**
     * Constructor for EnhancedRobot which requires a delay speed (in ms) and
     * text dump location. The delay speed can either be 0 or a positive integer.
     * If not 0, the delay speed causes the EnhancedRobot to take a break with
     * length 50-150% of the specified delay to make TASing less obvious. If a
     * delay speed of 50 is specified, the wait between events will be from
     * 25 to 75 ms.
     * 
     * @param dump - Path to file where HTML rip is dumped
     * @param speed - Delay, in ms, to use as base for random delay speed
     * @throws AWTException Throws inherited from robot -  if the platform
     * configuration does not allow low-level input control. This exception
     * is always thrown when GraphicsEnvironment.isHeadless() returns true
     */
    public EnhancedRobot(String dump, int speed) throws AWTException {
        super();
        // int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        // int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        // screen = new Rectangle(0, 0, screenWidth, screenHeight);
        // File where the raw data will be dumped
        dumpFileName = dump;
        // Speed between letters during typing
        typeSpeedConst = speed;
        setAutoDelay(0);
        // Initialize the decoder with a blank keycode request
        Decoder.getKeycode(' ');
    }

    /**
     * Types a keycode by pressing and releasing a key instantly.
     * 
     * @param keycode - Keycode for key to be typed
     */
    public void keyType(int keycode) {
        keyPress(keycode);
        keyRelease(keycode);
    }

    /**
     * Types a character. First checks if shift is needed, such as capital
     * letters or special characters above numkeys like '$' or '*'. If so,
     * presses shift, decodes the character into a keycode to press and release
     * the appropriate key, then releases shift.
     * 
     * @param c - Character to be typed
     */
    public void keyType(char c) {
        // If it's uppercase or a special character press shift then type
        boolean needShiftPressed = Decoder.needsShift(c);
        if(needShiftPressed) {
            keyPress(KeyEvent.VK_SHIFT);
        }

        // Type the char
        keyType(Decoder.getKeycode(c));

        // Release shift if needed
        if(needShiftPressed) {
            keyRelease(KeyEvent.VK_SHIFT);
        }
    }

    /**
     * Clicks a button on the mouse by pressing and releasing it.
     * 
     * @param buttons - Keycode for button to be pressed (right click, etc.)
     */
    public void mouseClick(int buttons) {
        mousePress(buttons);
        mouseRelease(buttons);
    }

    /**
     * Types a string on the virtual keyboard using repeated calls to
     * the keyType function after splitting the string into individual
     * characters.
     * 
     * @param toType - The string to type on the vitrual keyboard
     */
    public void typeString(String toType) {
        // Split the string into characters
        char[] keys = toType.toCharArray();
        // Send each one to keyType individually
        for(char key: keys) {
            customDelay();
            keyType(key);
        }
    }

    /**
     * Delays by a custom time between each keystroke when typing a string.
     * In theory, this makes TASing less obvious, but can be bypassed by
     * setting typeSpeedConst to 0.
     */
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

    /**
     * Main driving function of EnhancedRobot. Completes the full action set
     * required to obtain HTML section from Typer.io, save to the dump file,
     * parse the HTML, and then type it out in its entirety. Intentional
     * delay is added at the beginning to prevent moving too fast and missing
     * navigation strokes like alt-tabs or saves.
     * 
     * @throws FileNotFoundException If the filepath specified in the constructor
     * is not found, throws as no text can be parsed or typed.
     */
    public void initiate() throws FileNotFoundException {
        // Set delay to be large to avoid missed navigation keystrokes
        setAutoDelay(10);
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
        altTab();
        String toType = RawParser.parseRaw(dumpFileName).trim();
        System.out.println("String to be typed: " + toType);
        // Let the mouse get situated then get ready to go
        delay(1500);
        mouseClick(InputEvent.getMaskForButton(1));
        delay(10);
        // Now that navigation is done, remove delay
        setAutoDelay(0);
        // Type a space, wait 3 seconds for the round to start, then let er rip
        keyType(' ');
        // If you're trying to do anything other than the solo text snippets,
        // change this number to be the right length.
        delay(4500);
        typeString(toType);
    }

    /**
     * Simulate alt-tab action by pressing alt, pressing and releasing
     * tab, then releasing alt.
     */
    public void altTab() {
        keyPress(KeyEvent.VK_ALT);
        keyPress(KeyEvent.VK_TAB);
        keyRelease(KeyEvent.VK_TAB);
        keyRelease(KeyEvent.VK_ALT);
    }

    /**
     * Simulates multiple alt-tab actions by pressing alt, then pressing
     * and releasing tab the number of times required to reach a window
     * that many tabs away.
     * 
     * @param numWindows - The number of times to press tab before releasing alt
     */
    public void altTab(int numWindows) {
        keyPress(KeyEvent.VK_ALT);
        for(int i = 0; i < numWindows; i++) {
            keyPress(KeyEvent.VK_TAB);
            keyRelease(KeyEvent.VK_TAB);
        }
        keyRelease(KeyEvent.VK_ALT);
    }

    /**
     * Function to simulate pasting by pressing down control, pressing
     * V, then lifting control.
     */
    public void paste() {
        keyPress(KeyEvent.VK_CONTROL);
        keyType(KeyEvent.VK_V);
        keyRelease(KeyEvent.VK_CONTROL);
    }

    /**
     * Function to simulate saving by pressing down control, pressing
     * s, then lifting control.
     */
    public void save() {
        keyPress(KeyEvent.VK_CONTROL);
        keyType(KeyEvent.VK_S);
        keyRelease(KeyEvent.VK_CONTROL);
    }

    /**
     * Function to simulate selecting all by pressing down control,
     * pressing S, then lifting control.
     */
    public void all() {
        keyPress(KeyEvent.VK_CONTROL);
        keyType(KeyEvent.VK_A);
        keyRelease(KeyEvent.VK_CONTROL);
    }

    // ---------------------------- UNUSED CODE ----------------------------
    // This code is unused. It formed the basis of attempting to screen capture
    // and pass to text detection, but this approach was abandoned for a Python
    // approach.

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

}

/**
 * Private class to act as a decoder of keycodes. Has list of all expected
 * characters to be typed in a HashMap, where characters map to their respective
 * keycodes forfaster lookups and therefore less lost time while typing.
 */
class Decoder {
    // Array of all characters which require 'Shift' to be pressed in order
    // to be typed
    final static Character[] specialCharsArray =    {'~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', 'Q', 'W', 'E',
                                                     'R', 'T', 'Y', 'U', 'I', 'O', 'P', '{', '}', '|', 'A', 'S', 'D', 'F', 'G', 'H',
                                                     'J', 'K', 'L', ':', '\"', 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '<', '>', '?'};
    // Array of all characters which do not require 'Shift' to be pressed
    // in order to be typed.
    final static Character[] nonSpecialCharsArray = {'`', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '=', 'q', 'w', 'e',
                                                     'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']', '\\', 'a', 's', 'd', 'f', 'g', 'h',
                                                     'j', 'k', 'l', ';', '\'', 'z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.', '/', ' ', '\n'};
    // Hashsets containing all special and nonspecial characters
    final static HashSet<Character> specialChars = new HashSet<>(Arrays.asList(specialCharsArray));
    final static HashSet<Character> nonSpecialChars = new HashSet<>(Arrays.asList(nonSpecialCharsArray));

    // Hash maps which map nonspecial characters to keycodes, and special
    // characters to nonspecial characters
    static HashMap<Character, Integer> keyCodes;
    static HashMap<Character, Character> specialToNon;

    /**
     * Function which takes as input any typeable character 'c' and converts it 
     * to a keyCode to be typed.
     * 
     * @param c - The character to be typed
     * @return the value of the keycode to type that character
     */
    public static int getKeycode(char c) {
        // Ensure the hashmaps have been set up
        if(keyCodes == null) {
            setupKeyCodes();
        }
        // Get the keycode and return it
        return keyCodes.get(decode(c));
    }

    /**
     * Sets up the keycodes hash map. Maps all non-special characters to the
     * keycode which types them on the keyboard. Should only be run once
     * per program execution.
     */
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

    /**
     * Sets up the decoding from special characters to non-special characters.
     * Matches each character which requires shift with the un-shifted key to
     * get that character - 'a' for 'A' or '4' for '$' for example.
     */
    private static void setupDecode() {
        specialToNon = new HashMap<>();
        for(int i = 0; i < specialCharsArray.length; i++) {
            specialToNon.put(specialCharsArray[i], nonSpecialCharsArray[i]);
        }
    }

    /**
     * Takes a character c and transforms it to the actual key to be pressed.
     * Specifically, checks if the requested character is in the special characters
     * hash, and if so returns the un-shifted character to be typed. Otherwise,
     * returns the character unmodified.
     * 
     * @param c - The character to be decoded
     * @return Character of type char to be typed on the keyboard.
     */
    private static char decode(char c) {
        if(specialToNon == null) setupDecode();
        if(nonSpecialChars.contains(c)) {
            return c;
        }
        else {
            return specialToNon.get(c);
        }
    }

    /**
     * Checks if a character requires shift to be typed on a keyboard, such as
     * a capital letter or the percent symbol.
     * 
     * @param c - The character to be typed
     * @return True if shift is required, else false.
     */
    public static boolean needsShift(char c) {
        return specialChars.contains(c);
    }
}