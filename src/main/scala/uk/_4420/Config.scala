package uk._4420

import java.io.File

import scala.concurrent.duration._

case class Config(
    internetArchiveMetadataApiRoot: String,
    ffmpegExecutableName: String,
    inputVideoFile: File,
    outputThumbnailFile: File,
    mediaId: MediaId,
    timeout: FiniteDuration,
)

object Config {
  def fromArgsOrThrow(args: Array[String]): Config = args match { // DAVE: add tests, use proper option parsing lib
    case Array(inputVideoFile, outputThumbnailFile, mediaId) =>
      Config(
        internetArchiveMetadataApiRoot = "https://archive.org/metadata",
        ffmpegExecutableName = "ffmpeg",
        inputVideoFile = new File(inputVideoFile),
        outputThumbnailFile = new File(outputThumbnailFile),
        mediaId = MediaId(mediaId),
        timeout = 10.seconds,
      )
  }
}