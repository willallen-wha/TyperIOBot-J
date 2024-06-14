TyperIOBot
==========

Java implementation of a "TAS" for typer.io. Relies on current (as of 06/15/24) predictable form in which typer.io delivers web content in a `Gameboard_container__DtBpp` div. Using the inspect tool in Chrome, simulates copying the content to a predifined text file, then parsing the HTML into words and passing the necessary keystrokes to a virtual keyboard using a modified version of the `java.awt.Robot` class.

## Getting it running

All code relies on built-in Java tools, so no additional installation beyond a java install should be necessary. Only packages from `java.awt`, `java.io`, and `java.util` are used if any dependencies aren't present. The tool is intended to be run from within a code editor or on command-line once per each typer.io prompt. As such, an IDE, such as **VSCode** or **Eclipse**, is recommended.

Setting up the codebase requires simply cloing the repository.
```
git clone https://github.com/willallen-wha/TyperIOBot-J.git
```

After cloning the repository, you'll need to create a file named `raw.txt` in the base level (same level as `.gitignore`). Then, from within the cloned repository, run `TyperIOBot_J/Main.java` (instructions below). There are comments within the code explaining the expected workflow, but the general idea is this:

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