package aoc.day2

import akka.actor.ActorSystem
import akka.stream.scaladsl.Framing
import akka.util.ByteString
import aoc.FileUtils.fileSource
import aoc.implicits._

import scala.util.parsing.combinator.RegexParsers

case class Password(policy: (Int, Int), validator: Char, pass: String) {

  //private val validChars: String = pass.filter(_ == validator)
  //lazy val isValid = validChars.length >= policy._1 && validChars.length <= policy._2

  def charAtValid(i: Int): Boolean = pass.charAt(i - 1) == validator
  lazy val isValid = charAtValid(policy._1) xor charAtValid(policy._2)
}

object Main extends App {

  implicit val system = ActorSystem("day2")
  import system.dispatcher

  val parser = new PasswordParser

  val invalidPasswords =
    fileSource("day2input.txt")
      .via(Framing.delimiter(ByteString("\n"), 256).map(_.utf8String))
      .map(parser.run)
      .collect { case Some(p) => p }
      .filter(_.isValid)
      .map{a => println(a) ; 1}
      .reduce((l, r) => l + r)
      .runForeach(println)
      .andThen { case _ => system.terminate() }
}

class PasswordParser extends RegexParsers {

  def pass: Parser[String] = """[a-z]+""".r ^^ { _.toString }
  def char: Parser[Char]   = """[a-z]""".r  ^^ { _.toCharArray.head }
  def number: Parser[Int]  = """\d+""".r     ^^ { _.toInt }
  def dash: Parser[Unit]   = """-""".r      ^^ { _ => () }
  def colon: Parser[Unit]  = """:""".r      ^^ { _ => () }

  def policy: Parser[(Int, Int)] = number ~ dash ~ number ^^ { case x ~ _ ~ y => (x, y) }

  def password: Parser[Password] = policy ~ char ~ colon ~ pass ^^ { case p ~ c ~ _ ~ pass => Password(p, c, pass) }

  def run(input: String): Option[Password] =
    parse(password, input) match {
      case Success(p, _)   => Some(p)
      case Failure(msg, _) =>
        println(s"FAILURE $msg")
        None
      case Error(msg, _)   =>
        println(s"ERROR $msg")
        None
    }
}
