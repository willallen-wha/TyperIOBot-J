TyperIOBot
==========

Java implementation of a "TAS" for typer.io. Relies on current (as of 06/12/24) predictable form in which typer.io delivers web content in a `Gameboard_container_DtBpp` div. Using the inspect tool in Chrome, simulates copying the content to a predifined text file, then parsing the HTML into words and passing the necessary keystrokes to a virtual keyboard using a modified version of the `java.awt.Robot` class.

## Getting it running

All code relies on built-in Java tools, so no additional installation beyond a java install should be necessary. Only packages from `java.awt`, `java.io`, and `java.util` are used if any dependencies aren't present. The tool is intended to be run from within a code editor or on command-line once per each typer.io prompt. As such, an IDE, such as **VSCode** or **Eclipse**, is recommended.

Setting up the codebase should be as easy as cloing the repository.
```
    git clone https://github.com/willallen-wha/TyperIOBot-J.git
```