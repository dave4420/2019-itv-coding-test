package uk._4420

import java.io.{File, FileInputStream, IOException}
import java.security.{DigestInputStream, MessageDigest}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.stream.ActorMaterializer
import spray.json.JsonParser

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.FiniteDuration

class IntegrityChecker(apiRoot: String, timeout: FiniteDuration)(
  implicit system: ActorSystem,
  materializer: ActorMaterializer,
  ec: ExecutionContext
) {
  def hasIntegrity(file: File, id: MediaId): Boolean = Await.result(futureHasIntegrity(file, id), timeout)

  def futureHasIntegrity(file: File, id: MediaId): Future[Boolean] = for {
    response <- Http().singleRequest(
      HttpRequest(
        method = HttpMethods.GET,
        uri = s"$apiRoot/${id.unMediaId}", // in production code should uri-escape the media id
      )
    )
    result <- response.status.intValue match {
      case 200 => for {
        representation <- response.entity.toStrict(timeout)
        responseText = new String(representation.data.toArray[Byte], "UTF-8")
        json <- Future(JsonParser(responseText))
      } yield metadataIndicatesFileHasIntegrity(json.convertTo[MetadataJson], file)
      case 404 => Future.successful(false)
      case code => Future.failed(new IOException(s"metadata status $code (expected 200 or 404)"))
    }
  } yield result

  def metadataIndicatesFileHasIntegrity(metadata: MetadataJson, file: File): Boolean = {
    metadata.files.filter(_.name == file.getName) match {
      case Seq() => false
      case Seq(fileMetadata) =>
        fileMetadata.size.map(_.toLong).forall(_ == file.length) &&
          fileMetadata.md5.forall(_ == md5(file)) &&
          fileMetadata.sha1.forall(_ == sha1(file))
      case seq => throw new Exception(s"file appears ${seq.length} times in metadata response (expected once)")
    }
  }

  private def md5(file: File): String = hash(file, MessageDigest.getInstance("MD5"))

  private def sha1(file: File): String = hash(file, MessageDigest.getInstance("SHA-1"))

  private def hash(file: File, messageDigest: MessageDigest): String = {
    // adapted from <https://stackoverflow.com/a/41643076/86622>
    val buffer = new Array[Byte](64 * 1024)
    val inputStream = new DigestInputStream(new FileInputStream(file), messageDigest)
    try { while (inputStream.read(buffer) != -1) { } } finally { inputStream.close() }
    messageDigest.digest.map("%02x".format(_)).mkString
  }
}
