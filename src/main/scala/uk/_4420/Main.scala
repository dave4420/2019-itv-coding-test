package uk._4420

import java.io.File

object Main {
  def main(args: Array[String]): Unit = {
    val config = Config.fromArgsOrThrow(args)
    val program = new Main(
      integrityChecker = new IntegrityChecker(config.internetArchiveMetadataApiRoot),
      thumbnailGenerator = new ThumbnailGenerator(config.ffmpegExecutableName),
    )
    program.run(config.inputVideoFile, config.outputThumbnailFile, config.mediaId)
  }
}

class Main(integrityChecker: IntegrityChecker, thumbnailGenerator: ThumbnailGenerator) {
  def run(inputVideoFile: File, outputThumbnailFile: File, id: MediaId): Unit = {
    if (integrityChecker.hasIntegrity(inputVideoFile, id)) {
      thumbnailGenerator.generateThumbnail(inputVideoFile, outputThumbnailFile)
    }
    else {
      Console.err.println(s"$inputVideoFile is corrupt")
      System.exit(2)
    }
  }
}
