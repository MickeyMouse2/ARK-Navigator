package space.taran.arknavigator.mvp.model.repo.kind

import android.media.MediaMetadataRetriever
import android.net.Uri
import space.taran.arknavigator.ui.App
import java.nio.file.Path

object VideoKindFactory : ResourceKindFactory {
    override val acceptedExtensions: Set<String> =
        setOf("mp4", "avi", "mov", "wmv", "flv")

    override fun extract(path: Path): ResourceKind {
        val retriever = MediaMetadataRetriever()

        retriever.setDataSource(App.instance, Uri.fromFile(path.toFile()))
        val durationMillis = retriever
            .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val duration = durationMillis?.toLong()

        val width = retriever
            .extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            ?.toLong()
        val height = retriever
            .extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            ?.toLong()

        retriever.release()

        return ResourceKind.Video(height, width, duration)
    }
}
