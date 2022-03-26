package space.taran.arknavigator.mvp.model.repo.kind

import com.beust.klaxon.Klaxon
import java.nio.file.Path
import java.util.zip.ZipFile

object LinkKindFactory : ResourceKindFactory {
    override val acceptedExtensions = setOf("link")
    private const val JSON_FILE = "link.json"
    private val klaxon = Klaxon()

    override fun extract(path: Path): ResourceKind {
        val zip = ZipFile(path.toFile())
        val jsonEntry = zip
            .entries()
            .asSequence()
            .find { entry -> entry.name == JSON_FILE }
            ?: return ResourceKind.Link()

        val link = klaxon.parse<JsonLink>(zip.getInputStream(jsonEntry))
            ?: return ResourceKind.Link()

        return ResourceKind.Link(link.title, link.desc, link.url)
    }
}

private data class JsonLink(val url: String, val title: String, val desc: String)
