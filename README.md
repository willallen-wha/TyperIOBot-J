TyperIOBot
==========

Java implementation of a "TAS" for typer.io. Relies on current (as of 06/12/24) predictable form in which typer.io delivers web content in a `Gameboard_container_DtBpp` div. Using the inspect tool in Chrome, simulates copying the content to a predifined text file, then parsing the HTML into words and passing the necessary keystrokes to a virtual keyboard using a modified version of the `java.awt.Robot` class.