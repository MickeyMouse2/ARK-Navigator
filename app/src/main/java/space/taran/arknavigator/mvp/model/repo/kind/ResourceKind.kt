package space.taran.arknavigator.mvp.model.repo.kind

import space.taran.arknavigator.mvp.model.dao.ResourceExtra
import space.taran.arknavigator.utils.extension
import java.nio.file.Path
import kotlin.reflect.full.isSubclassOf

sealed class ResourceKind {
    val kindCode = sealedSubClasses.indexOf(this::class)

    object Image : ResourceKind()

    class Video(
        val height: Long? = null,
        val width: Long? = null,
        val duration: Long? = null
    ) : ResourceKind()

    class Document(val pages: Int? = null) : ResourceKind()

    class Link(
        val title: String? = null,
        val description: String? = null,
        val url: String? = null
    ) : ResourceKind()

    companion object {
        private val sealedSubClasses = ResourceKind::class.sealedSubclasses.filter {
            it.isSubclassOf(ResourceKind::class)
        }
        private val kindFactory = listOf(
            DocumentKindFactory,
            ImageKindFactory,
            LinkKindFactory,
            VideoKindFactory
        )

        fun fromRoom(
            kindCode: Int?,
            extras: List<ResourceExtra>
        ): ResourceKind? {
            val data = extras.map {
                MetaExtraTag.values()[it.ordinal] to it.value
            }.toMap()

            if (kindCode == null) return null

            val kClass = sealedSubClasses[kindCode]

            return when (kClass) {
                Video::class ->
                    Video(
                        data[MetaExtraTag.HEIGHT]?.toLong(),
                        data[MetaExtraTag.WIDTH]?.toLong(),
                        data[MetaExtraTag.DURATION]?.toLong()
                    )
                Document::class ->
                    Document(data[MetaExtraTag.PAGES]?.toInt())
                Link::class -> Link(
                    data[MetaExtraTag.TITLE],
                    data[MetaExtraTag.DESCRIPTION],
                    data[MetaExtraTag.URL]
                )
                Image::class -> Image
                else -> null
            }
        }

        fun fromPath(path: Path): ResourceKind? {
            val extension = extension(path)
            val factory = kindFactory.find { extension in it.acceptedExtensions }
            return factory?.extract(path)
        }
    }
}

enum class MetaExtraTag {
    DURATION, WIDTH, HEIGHT, PAGES, TITLE, DESCRIPTION, URL
}
