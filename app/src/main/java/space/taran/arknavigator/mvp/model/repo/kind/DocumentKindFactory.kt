package space.taran.arknavigator.mvp.model.repo.kind

import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import space.taran.arknavigator.utils.RESOURCES_INDEX
import space.taran.arknavigator.utils.extension
import java.io.FileNotFoundException
import java.nio.file.Path

object DocumentKindFactory : ResourceKindFactory {
    override val acceptedExtensions: Set<String> =
        setOf("pdf", "txt", "doc", "docx", "odt", "ods", "md")

    override fun extract(path: Path): ResourceKind {
        if (extension(path) != "pdf") return ResourceKind.Document()

        var parcelFileDescriptor: ParcelFileDescriptor? = null

        try {
            parcelFileDescriptor = ParcelFileDescriptor.open(
                path.toFile(),
                ParcelFileDescriptor.MODE_READ_ONLY
            )
        } catch (e: FileNotFoundException) {
            Log.e(
                RESOURCES_INDEX,
                "Failed to find file at path: $path"
            )
        }
        parcelFileDescriptor ?: return ResourceKind.Document()
        val pdfRenderer = PdfRenderer(parcelFileDescriptor)
        val totalPages = pdfRenderer.pageCount
        val pages = if (totalPages > 0) totalPages else null

        return ResourceKind.Document(pages)
    }
}
