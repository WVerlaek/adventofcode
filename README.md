# Advent of Code

:star: https://adventofcode.com/

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/wverlaek/adventofcode)

Advent of Code solutions, written in Kotlin.


## Solution framework

A small framework is created to speed up solving a day's puzzle:

* Input is automatically read from the AoC API
* Unit tests using each day's sample input to verify solutions
* Answers are automatically uploaded and outcome is shown
* Common package for utitilities and datastructures

Additionally, uses IntelliJ [live templates](https://www.jetbrains.com/help/idea/creating-and-editing-live-templates.html):
  * Template to solve a day's puzzle
  * Template to unit test a solution with sample input

A GitHub Action is configured to run all unit tests on each commit.

## Automatic answer submission

Using a session token for the Advent of Code API, the day's input is automatically read, and your solution is uploaded on each run with the outcome printed afterwards:

```console
$ gradle run
Answer for 2022/3 level 2: '2581' (took 28ms)
Response: right answer!
```

No need to manually download inputs and upload your answers :zap:

#### Set up API session token

The session token needed to read input and upload answers can be set up as follows:

* Log in on https://adventofcode.com/
* Read the `session` cookie
* Put its value in this file: `.secrets/session` (.gitignored)
  * Or set it as env var `AOC_SESSION`, e.g. in the Gitpod user settings to have it automatically used in Gitpod workspaces.
