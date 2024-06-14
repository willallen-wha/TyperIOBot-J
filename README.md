TyperIOBot
==========

Java implementation of a "TAS" for typer.io. Relies on current (as of 06/15/24) predictable form in which typer.io delivers web content in a `Gameboard_container__DtBpp` div. Using the inspect tool in Chrome, simulates copying the content to a predifined text file, then parsing the HTML into words and passing the necessary keystrokes to a virtual keyboard using a modified version of the `java.awt.Robot` class.

**This project is considered complete.** Initially, image recognition to avoid the need to use right clicking and the inspect tool was intended, but this approach was abandoned. Instead, [the python version is being developed](https://github.com/willallen-wha/TyperIOBot-P).

## Getting it running

### Cloning repo & dependencies

All code relies on built-in Java tools, so no additional installation beyond a java install should be necessary. Only packages from `java.awt`, `java.io`, and `java.util` are used if any dependencies aren't present. The tool is intended to be run from within a code editor or on command-line once per each typer.io prompt. As such, an IDE, such as **VSCode** or **Eclipse**, is recommended.

Setting up the codebase requires simply cloing the repository.
```
git clone https://github.com/willallen-wha/TyperIOBot-J.git
```

After cloning the repository, you'll need to create a file named `raw.txt` in the base level (same level as `.gitignore`). Then, from within the cloned repository, run `TyperIOBot_J/Main.java` (instructions below).

### Running TyperIOBot in Context
There are comments within the code explaining the expected workflow, but the general idea is this:

1. Open [tpyer.io's solo play](https://typer.io/solo) in Chrome.
2. Open whatever you'll be using to run `Main.java`
3. Open `raw.txt` in a text editor. I prefer Notepad on Windows, but anything that respects control+v as paste, control+a as selecting everyting, and control+s as saving will work.
5. Prepare Typer.io for HTML ripping:
    * Right click and inspect the are of Typer.io which contains the text snippet to be typed
    * Select the div named `Gameboard_container__DtBpp`: `<div class="Gameboard_container__Dtbpp">...</div>`
    * This div is nested under the `Gameboard_root` div. The full HTML path is `body/__next/Play_root*/Play_container*/Gameboard_root*`
4. Ensure the windows are in the following focus order:
    * Window running the program on top
    * Chrome window containing Typer.io
    * `raw.txt`
    * The window order when alt-tabbing should look like this:
    ![img](.Assets/taborder)
5. Run the program
    * **Run** the file `Main.java`
    * **Alt-tab** into the Typer.io window, and click the `Gameboard_container` div, keeping your mouse over the div.
    * The program will right click, copy the HTML content, then alt-tab twice to reach the `raw.txt` file. It will then select all, backspace, paste, and save. It will then alt-tab back into the Typer.io window.
        - *This process will likely not be obviously visible* - instead, it will be flashes of dialogue menus and text files. **It is complete when Typer.io is the window in focus again.**
    * **Align your mouse** over the **"Press 'Space' here to begin..."** field. The program will wait about a second to allow time for positioning.
    * The program will wait a second, press space, then wait 3 seconds for the round to begin. After the 3 second wait, the program will type the string it parsed from the HTML to complete. **Do not press control, alt, or other modifier keys during this process.** The virtual keyboard and real keyboard share inputs, so these modifiers will apply to the letters being typed, leading to control+r refreshing page, control+s saving the page (if typed), etc.
    * Once the entire string has been typed, the program stops executing and no other keys are pressed.
    * If the wrong string is being typed or if you want to ensure the typing is complete, **open a new chrome tab using the mouse** (*not* control+t), and wait until the string is done typing.

All in all, the process looks like this:
![gif](.Assets/runex)

## Customizatoins

If desired, two options can be changed:

- **Typing speed and rhythm:**
    * The variable `typingDelay` in `Main.java` controls between keystrokes when the bot types the text. The default is 0, which usually results in WPM of ~3000+. However, if a less extreme speed is desired, the delay can be increased. As the delay increases, it also becomes increasingly random. The delay, specified in milliseconds, is a base value which the actual delay uses to create a range from 50% to 150% of the requested delay. For example, if the delay is set to `50`, then the actual delay will be randomly distributed across 25ms to 75ms. This both slows down the typing to more realistic speeds under 200 WPM, it also prevents consistent cadence, which makes the TAS less obvious.
- **Raw text file name and location**
    * The variable `nameOfFile` in `Main.java` contains the name/path to the `.txt` file containing the raw HTML. The file can be named anything as long as it's in the `.txt` format. The `Scanner` class is used, so any plaintext extension works, but `.txt` is the easiest.

## Known Issues

* On faster computers, it's possible for the built-in delay for the navigation during the copy/alt-tab/paste&save/alt-tab provess is too slow and keystrokes will be missed. This is attempted to be fixed by increasing the delay during that process (usually around 10ms is adequate) but line 165 in `EnhancedRobot.java` can be modified to increase delay further if inputs are still being dropped.
* This code is untested on Mac or Linux devices. Linux devices would presumptively work, but differences in Mac keyboards between `command+tab` versus `alt+tab` or `command+s` vs `control+s` means it likely wouldn't work.
* This code assumes a standard english ANSI/ISO keyboard. Other keyboard languages or keyboards with different character layouts (different number row, different symbols, etc.) will not work, as the key mapping is not accurate.

