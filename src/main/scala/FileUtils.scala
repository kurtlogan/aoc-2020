package aoc

import java.nio.file.Paths

import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Framing, Source}
import akka.util.ByteString

import scala.concurrent.Future

object FileUtils {

  private def filePath(input: String) = Paths.get(s"src/main/resources/$input")

  def fileSource(input: String): Source[String, Future[IOResult]] =
    FileIO.fromPath(filePath(input)).via(Framing.delimiter(ByteString("\n"), 256).map(_.utf8String))
}