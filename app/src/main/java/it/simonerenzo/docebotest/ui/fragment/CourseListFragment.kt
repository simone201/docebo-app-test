package it.simonerenzo.docebotest.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.FrameLayout
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.itemanimators.AlphaInAnimator
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import it.simonerenzo.docebotest.R
import it.simonerenzo.docebotest.client.model.DoceboModels
import it.simonerenzo.docebotest.ui.item.CourseItem
import kotlinx.android.synthetic.main.course_list_fragment.*
import kotlinx.android.synthetic.main.course_list_header.*

class CourseListFragment(private val result: DoceboModels.APIData) : Fragment(),
    PopupMenu.OnMenuItemClickListener {

    companion object {
        fun newInstance(response: DoceboModels.APIData) = CourseListFragment(response)
    }

    private val itemAdapter = ItemAdapter<CourseItem>()
    private val fastAdapter = FastAdapter.with(itemAdapter)

    private var sortBy = 0
    private var filterBy = 0

    private var filterByType = "all"
    private var filterByPrice = 0f

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.course_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)

        val dividerItemDecoration = DividerItemDecoration(
            courseList.context,
            layoutManager.orientation
        )
        courseList.addItemDecoration(dividerItemDecoration)

        courseList.layoutManager = layoutManager
        courseList.overScrollMode = View.OVER_SCROLL_NEVER
        courseList.itemAnimator = AlphaInAnimator()
        courseList.adapter = fastAdapter

        setupListeners()
    }

    override fun onResume() {
        super.onResume()

        if (result.count == 0) {
            courseList.visibility = View.GONE
            courseListShimmer.visibility = View.VISIBLE
            courseListShimmer.startShimmer()
        } else {
            fillList(result.items)

            if (courseListShimmer.isShimmerStarted) {
                courseListShimmer.stopShimmer()
                courseListShimmer.visibility = View.GONE
            }
            courseList.visibility = View.VISIBLE
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (hasToSkip(item?.itemId)) return false

        sortBy = when (item?.itemId) {
            R.id.menu_sort_none -> 0
            R.id.menu_sort_az -> 1
            R.id.menu_sort_za -> 2
            else -> 0
        }

        filterBy = when (item?.itemId) {
            R.id.menu_filter_none -> 0
            R.id.menu_filter_type -> 1
            R.id.menu_filter_price -> 2
            else -> 0
        }

        if (item?.groupId == R.id.menu_group_sort || filterBy == 0) {
            fillList(result.items)
        } else if (item?.groupId == R.id.menu_group_filter) {
            showFilterDialog(item.itemId)
        }

        item?.isChecked = true

        return true
    }

    @SuppressLint("CheckResult")
    private fun setupListeners() {
        courseListFilterBtn.clicks()
            .bindToLifecycle(this)
            .subscribe {
                val popupMenu = PopupMenu(context, courseListFilterBtn, Gravity.BOTTOM)
                popupMenu.menuInflater.inflate(R.menu.filter_list_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener(this)
                popupMenu.show()
            }
    }

    private fun fillList(items: List<DoceboModels.Item>) {
        itemAdapter.clear()

        val listItems = items
            .filter {
                when (filterBy) {
                    0 -> true
                    1 -> it.type == filterByType
                    2 -> it.price.toFloat() == filterByPrice
                    else -> true
                }
            }
            .sortedBy {
                when (sortBy) {
                    0 -> it.id
                    1, 2 -> it.name
                    else -> it.id
                }
            }
            .map { CourseItem(it) }

        itemAdapter.add(if (sortBy == 2) listItems.asReversed() else listItems)

        val itemsTemplate = resources.getString(R.string.text_course_items)
        courseListItems.text = String.format(itemsTemplate, itemAdapter.adapterItemCount)
    }

    private fun showFilterDialog(itemId: Int) {
        val valueDialogBuilder = AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setTitle(getValueDialogTitle(itemId))

        var priceField : AppCompatEditText? = null

        if (filterBy == 1) {
            valueDialogBuilder.setSingleChoiceItems(R.array.course_type_items, 0) { _, i ->
                filterByType = resources.getStringArray(R.array.course_type_values)[i]

                if (i == 0) {
                    filterBy = 0
                }
            }
        } else if (filterBy == 2) {
            priceField = AppCompatEditText(requireContext())
            priceField.id = ViewCompat.generateViewId()
            priceField.hint = resources.getString(R.string.field_price_hint)
            priceField.maxLines = 1
            priceField.inputType = InputType.TYPE_CLASS_NUMBER

            val layout = FrameLayout(requireContext())
            layout.setPaddingRelative(45,15,45,0)
            layout.addView(priceField)

            valueDialogBuilder.setView(layout)
        }

        valueDialogBuilder.setPositiveButton(R.string.btn_search_text) { di, _ ->
            filterByPrice = try {
                priceField?.text?.toString()?.toFloat() ?: 0f
            } catch (e: NumberFormatException) {
                0f
            }

            di.dismiss()
            fillList(result.items)
        }

        valueDialogBuilder.show()
    }

    private fun hasToSkip(itemId: Int?) : Boolean {
        return when (itemId) {
            R.id.menu_sort_none,
            R.id.menu_sort_az,
            R.id.menu_sort_za,
            R.id.menu_filter_none,
            R.id.menu_filter_type,
            R.id.menu_filter_price -> false
            else -> true
        }
    }

    private fun getValueDialogTitle(itemId: Int) : Int {
        return when (itemId) {
            R.id.menu_filter_type -> R.string.dialog_title_type
            R.id.menu_filter_price -> R.string.dialog_title_price
            else -> 0
        }
    }

}