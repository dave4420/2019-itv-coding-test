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
    size: Option[String], // it's a string in the JSON, even though it should be a number
    md5: Option[String],
    sha1: Option[String],
)

object MetadataFileJson {
  implicit val rootJsonFormat: RootJsonFormat[MetadataFileJson] = jsonFormat4(MetadataFileJson.apply)
  implicit def jsonFormat: JsonFormat[MetadataFileJson] = rootJsonFormat
}
