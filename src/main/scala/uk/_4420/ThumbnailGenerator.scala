package uk._4420

import java.io.File

import scala.sys.process._

class ThumbnailGenerator(ffmpegExecutableName: String) {
  def generateThumbnail(inputVideoFile: File, outputThumbnailFile: File): Unit = {
    Seq(
      ffmpegExecutableName,
      "-i", inputVideoFile.getPath,
      "-vframes", "1", // output one frame
      "-an", // disable audio
      // -s 400x222 output size
      "-ss", "2", // offset into video to grab thumbnail from (seconds)
      outputThumbnailFile.getPath,
    ).!
  }
}
