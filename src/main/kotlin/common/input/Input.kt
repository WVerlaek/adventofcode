package common.input

import common.client.Client

object Input {
    fun load(year: Int, day: Int): String {
        return Client().getInput(year, day)
    }
}
