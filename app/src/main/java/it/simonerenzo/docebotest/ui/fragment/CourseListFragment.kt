package it.simonerenzo.docebotest.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.itemanimators.AlphaInAnimator
import it.simonerenzo.docebotest.R
import it.simonerenzo.docebotest.client.model.DoceboModels
import it.simonerenzo.docebotest.ui.item.CourseItem
import kotlinx.android.synthetic.main.course_list_fragment.*
import kotlinx.android.synthetic.main.course_list_header.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.sdk27.coroutines.onClick

class CourseListFragment(private val result: DoceboModels.APIData) : Fragment() {

    companion object {
        fun newInstance(response: DoceboModels.APIData) = CourseListFragment(response)
    }

    private val itemAdapter = ItemAdapter<CourseItem>()
    private val fastAdapter = FastAdapter.with(itemAdapter)

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

        courseListFilterBtn.onClick { courseListFilterBtn.snackbar(R.string.error_not_implemented) }
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

    private fun fillList(items: List<DoceboModels.Item>) {
        itemAdapter.clear()
        itemAdapter.add(items.map { CourseItem(it) })

        val itemsTemplate = resources.getString(R.string.text_course_items)
        courseListItems.text = String.format(itemsTemplate, itemAdapter.adapterItemCount)
    }

}