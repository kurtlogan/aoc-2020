package aoc.day2

import org.scalatest.{FreeSpec, MustMatchers}


class PasswordSpec extends FreeSpec with MustMatchers {

  "isValid should" - {
    "return false" - {
      Password((19, 20), 'z', "zzzznzzzzzzzzzzzzzzz").isValid mustBe false
    }
  }
}