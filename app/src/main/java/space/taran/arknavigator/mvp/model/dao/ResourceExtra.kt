package space.taran.arknavigator.mvp.model.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import space.taran.arknavigator.mvp.model.repo.kind.MetaExtraTag
import space.taran.arknavigator.mvp.model.repo.index.ResourceId
import space.taran.arknavigator.mvp.model.repo.kind.ResourceKind

@Entity(
    primaryKeys = ["resource", "ordinal"],
    foreignKeys = [
        ForeignKey(
            entity = Resource::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("resource"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ResourceExtra(
    @ColumnInfo(index = true)
    val resource: ResourceId,

    val ordinal: Int,

    val value: String
) {
    companion object {
        fun fromMetaExtra(id: ResourceId, kind: ResourceKind?):
            List<ResourceExtra> = when (kind) {
            is ResourceKind.Image -> emptyList()
            is ResourceKind.Document -> mapDocument(id, kind)
            is ResourceKind.Video -> mapVideo(id, kind)
            is ResourceKind.Link -> mapLink(id, kind)
            else -> emptyList()
        }

        private fun mapDocument(
            id: ResourceId,
            document: ResourceKind.Document
        ): List<ResourceExtra> {
            val extras = mutableListOf<ResourceExtra>()
            document.pages?.let { pages ->
                extras.add(
                    ResourceExtra(
                        id,
                        MetaExtraTag.PAGES.ordinal,
                        pages.toString()
                    )
                )
            }
            return extras
        }

        private fun mapVideo(
            id: ResourceId,
            video: ResourceKind.Video
        ): List<ResourceExtra> {
            val valueMap = mutableMapOf<Int, String>()
            video.height?.let {
                valueMap[MetaExtraTag.HEIGHT.ordinal] = it.toString()
            }
            video.width?.let { valueMap[MetaExtraTag.WIDTH.ordinal] = it.toString() }
            video.duration?.let {
                valueMap[MetaExtraTag.DURATION.ordinal] = it.toString()
            }

            val extras = mutableListOf<ResourceExtra>()
            valueMap.forEach { (ordinal, value) ->
                extras.add(
                    ResourceExtra(
                        id,
                        ordinal,
                        value
                    )
                )
            }
            return extras
        }

        private fun mapLink(
            id: ResourceId,
            link: ResourceKind.Link
        ): List<ResourceExtra> {
            val valueMap = mutableMapOf<Int, String>()
            link.url?.let {
                valueMap[MetaExtraTag.URL.ordinal] = it
            }
            link.description?.let {
                valueMap[MetaExtraTag.DESCRIPTION.ordinal] = it
            }
            link.title?.let {
                valueMap[MetaExtraTag.TITLE.ordinal] = it
            }

            val extras = mutableListOf<ResourceExtra>()
            valueMap.forEach { (ordinal, value) ->
                extras.add(
                    ResourceExtra(
                        id,
                        ordinal,
                        value
                    )
                )
            }
            return extras
        }
    }
}
