package space.taran.arknavigator.mvp.model.repo.kind

import space.taran.arknavigator.utils.extension
import java.nio.file.Path

object ImageKindFactory : ResourceKindFactory {
    override val acceptedExtensions: Set<String> =
        setOf("jpg", "jpeg", "png")

    override fun extract(path: Path): ResourceKind = ResourceKind.Image

    fun isImage(path: Path): Boolean =
        acceptedExtensions.contains(extension(path))
}
