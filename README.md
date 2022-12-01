# Advent of Code

:star: https://adventofcode.com/

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/wverlaek/adventofcode)

## Kotlin

Solutions are written in Kotlin.


## Puzzle framework

A small framework is created to speed up solving a day's puzzle:

* Input is automatically read from the AoC API
* Unit tests using each day's sample input to verify solutions
* Answers are automatically uploaded and outcome is shown
* Common package for utitilities and datastructures

Additionally, uses IntelliJ [live templates](https://www.jetbrains.com/help/idea/creating-and-editing-live-templates.html):
  * Template to solve a day's puzzle
  * Template to unit test a solution with sample input

A GitHub Action is configured to run all unit tests on each commit.


### AoC API session token

A session token is needed to make API requests to AoC (for reading input and uploading answers).

To get this token:

* Log in on https://adventofcode.com/
* Read the `session` cookie
* Put its value in this file: `.secrets/session` (.gitignored)
  * Or set it as env var `AOC_SESSION`, e.g. in the Gitpod user settings.
