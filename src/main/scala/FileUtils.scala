package aoc

import java.nio.file.Paths

import akka.stream.IOResult
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString

import scala.concurrent.Future

object FileUtils {

  private val classLoader = Thread.currentThread().getContextClassLoader
  private def resource(input: String) = getClass.getClassLoader.getResource(input)
  private def filePath(input: String) = Paths.get(s"src/main/resources/$input")

  def fileSource(input: String): Source[ByteString, Future[IOResult]] = FileIO.fromPath(filePath(input))
}