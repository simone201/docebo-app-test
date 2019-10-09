package it.simonerenzo.docebotest.ui.item

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.materialize.holder.StringHolder
import it.simonerenzo.docebotest.R
import it.simonerenzo.docebotest.client.model.DoceboModels

const val EMPTY_IMAGE = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSdO1fLa1aGejKjvJrh7L2ywuQFp77SdMtDX3qHqAd8l-A8dPsF"

class CourseItem(private val course: DoceboModels.Item) : AbstractItem<CourseItem.ViewHolder>() {

    override val layoutRes: Int
        get() = R.layout.item_course

    override val type: Int
        get() = R.id.courseItem

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<CourseItem>(view) {

        private val thumb = view.findViewById<AppCompatImageView>(R.id.courseThumbnail)
        private val name = view.findViewById<AppCompatTextView>(R.id.courseName)
        private val typePrice = view.findViewById<AppCompatTextView>(R.id.courseTypePrice)
        private val descr = view.findViewById<AppCompatTextView>(R.id.courseDescription)

        override fun bindView(item: CourseItem, payloads: MutableList<Any>) {
            val course = item.course

            val transparent = itemView.resources.getDrawable(android.R.color.transparent, null)

            thumb.background = transparent
            name.background = transparent
            typePrice.background = transparent
            descr.background = transparent

            val imgUri = if (course.thumbnail.isBlank()) EMPTY_IMAGE else "https://${course.thumbnail}"

            Glide.with(itemView)
                .load(imgUri)
                .into(thumb)

            StringHolder.applyToOrHide(StringHolder(course.name), name)
            StringHolder.applyToOrHide(StringHolder("${course.type} | ${course.price}"), typePrice)
            StringHolder.applyToOrHide(
                StringHolder(HtmlCompat.fromHtml(course.description, HtmlCompat.FROM_HTML_MODE_COMPACT)),
                descr)
        }

        override fun unbindView(item: CourseItem) {
            thumb.setImageDrawable(null)
            name.text = null
            typePrice.text = null
            descr.text = null
        }

    }

}