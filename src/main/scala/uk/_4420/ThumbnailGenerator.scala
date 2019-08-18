package uk._4420

import java.io.File
import java.time.Duration

import uk._4420.ThumbnailGenerator._

import scala.sys.process._

class ThumbnailGenerator(ffmpegExecutableName: String) {
  def generateThumbnail(inputVideoFile: File, outputThumbnailFile: File, offset: Duration): Boolean = {
    Seq(
      ffmpegExecutableName,
      "-i", inputVideoFile.getPath,
      "-vframes", "1", // output one frame
      "-an", // disable audio
      // -s 400x222 output size
      "-ss", secondsAsString(offset), // offset into video to grab thumbnail from (seconds)
      outputThumbnailFile.getPath,
    ).! == 0
  }
}

object ThumbnailGenerator {
  def secondsAsString(offset: Duration): String = f"${offset.getSeconds}%d.${offset.getNano}%09d"
}
