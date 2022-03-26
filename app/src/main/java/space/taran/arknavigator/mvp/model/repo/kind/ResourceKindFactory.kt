package space.taran.arknavigator.mvp.model.repo.kind

import java.nio.file.Path

interface ResourceKindFactory {
    val acceptedExtensions: Set<String>
    fun extract(path: Path): ResourceKind
}
