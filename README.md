Advent of Code 2020 solutions in Kotlin.

## Run from the command line

[Babashka](https://babashka.org/) scripts exist to make it possible to build and run the solutions easily from the command line, without the need for IntelliJ IDEA.

1. Install [Kotlin command-line compiler](https://kotlinlang.org/docs/command-line.html#install-the-compiler)
1. On Windows, configure the PowerShell:

    ```ps1
    # Add Kotlin executables to PATH env var
    $env:PATH = "$PATH_TO_KOTLINC\bin\;$env:PATH"

    # Use BOM-less UTF-8
    $OutputEncoding = [Text.UTF8Encoding]::new($false)
    ```
1. Build the solution of Day X, Part Y:

    ```ps1
    bb build -d $X -p $Y
    ```

1. Run it!

    ```ps1
    cat $PATH_TO_INPUT_TXT | bb run -d $X -p $Y
    ```

1. Other commands:
    - Format Babashka scripts: `bb fmt`
    - Clean the build output directory: `bb clean`

