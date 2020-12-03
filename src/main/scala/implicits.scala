package aoc

object implicits {

  implicit class BooleanOp(val b: Boolean) extends AnyVal {

    def xor(other: Boolean): Boolean = (b || other) && (!b || !other)
  }
}