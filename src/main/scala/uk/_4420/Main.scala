package uk._4420

import java.io.File
import java.time.Duration

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object Main {
  def main(args: Array[String]): Unit = {
    val config = Config.fromArgsOrThrow(args)
    implicit val system = ActorSystem("itv")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher
    try {
      val program = new Main(
        integrityChecker = new IntegrityChecker(config.internetArchiveMetadataApiRoot, config.timeout),
        thumbnailGenerator = new ThumbnailGenerator(config.ffmpegExecutableName),
      )
      program.run(config.inputVideoFile, config.outputThumbnailFile, config.mediaId, config.thumbnailOffset)
    }
    finally {
      system.terminate()
    }
  }
}

class Main(integrityChecker: IntegrityChecker, thumbnailGenerator: ThumbnailGenerator) {
  def run(inputVideoFile: File, outputThumbnailFile: File, id: MediaId, thumbnailOffset: Duration): Unit = {
    if (integrityChecker.hasIntegrity(inputVideoFile, id)) {
      val ok = thumbnailGenerator.generateThumbnail(inputVideoFile, outputThumbnailFile, thumbnailOffset)
      if (!ok) {
        Console.err.println(s"Failed to generate thumbnail")
        System.exit(1)
      }
    }
    else {
      Console.err.println(s"$inputVideoFile is corrupt")
      System.exit(2)
    }
  }
}
