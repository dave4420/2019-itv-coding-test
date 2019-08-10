package uk._4420

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.FreeSpec
import spray.json.JsonParser

import scala.util.{Success, Try}

class MetadataJsonTest extends FreeSpec with TypeCheckedTripleEquals {

  "MetadataJson" - {
    "parses all fields" in {
      val result = Try {
        JsonParser {
          """ {
            |   "files": [
            |     {
            |       "name": "a.file",
            |       "size": "12345",
            |       "md5": "abcde",
            |       "sha1": "abcdef"
            |     }
            |   ]
            | }
          """.stripMargin
        }.convertTo[MetadataJson]
      }

      val expectedResult = Success {
        MetadataJson(
          files = Seq(
            MetadataFileJson(
              name = "a.file",
              size = Some("12345"),
              md5 = Some("abcde"),
              sha1 = Some("abcdef"),
            )
          ),
        )
      }

      assert(result === expectedResult)
    }

    "doesn't require optional fields" in {
      val result = Try {
        JsonParser {
          """ {
            |   "files": [
            |     {
            |       "name": "a.file"
            |     }
            |   ]
            | }
          """.stripMargin
        }.convertTo[MetadataJson]
      }

      val expectedResult = Success {
        MetadataJson(
          files = Seq(
            MetadataFileJson(
              name = "a.file",
              size = None,
              md5 = None,
              sha1 = None,
            )
          ),
        )
      }

      assert(result === expectedResult)
    }
  }
}
