package uk._4420

import spray.json._
import spray.json.DefaultJsonProtocol._

case class MetadataJson(
    files: Seq[MetadataFileJson],
)

object MetadataJson {
  implicit val rootJsonFormat: RootJsonFormat[MetadataJson] = jsonFormat1(MetadataJson.apply)
  implicit def jsonFormat: JsonFormat[MetadataJson] = rootJsonFormat
}

case class MetadataFileJson(
    name: String,
    size: Long,
    md5: String,
    sha1: String,
)

object MetadataFileJson {
  implicit val rootJsonFormat: RootJsonFormat[MetadataFileJson] = jsonFormat4(MetadataFileJson.apply)
  implicit def jsonFormat: JsonFormat[MetadataFileJson] = rootJsonFormat
}
