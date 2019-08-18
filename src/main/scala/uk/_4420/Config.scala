package uk._4420

import java.io.File
import java.time.Duration

import scala.concurrent.duration._

case class Config(
    internetArchiveMetadataApiRoot: String,
    ffmpegExecutableName: String,
    inputVideoFile: File,
    outputThumbnailFile: File,
    thumbnailOffset: Duration,
    mediaId: MediaId,
    timeout: FiniteDuration,
)

object Config {
  //TODO if we add any options: use a proper option parsing library
  def fromArgsOrThrow(args: Array[String]): Config = args match {
    case Array(inputVideoFile, outputThumbnailFile, mediaId, thumbnailOffset) =>
      Config(
        internetArchiveMetadataApiRoot = "https://archive.org/metadata",
        ffmpegExecutableName = "ffmpeg",
        inputVideoFile = new File(inputVideoFile),
        outputThumbnailFile = new File(outputThumbnailFile),
        thumbnailOffset = Duration.parse(thumbnailOffset),
        mediaId = MediaId(mediaId),
        timeout = 10.seconds,
      )
  }
}