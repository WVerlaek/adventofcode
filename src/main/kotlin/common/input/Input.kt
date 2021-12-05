package common.input

import common.client.Client

fun loadInput(year: Int, day: Int): String = Client().getInput(year, day)
