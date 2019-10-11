package it.simonerenzo.docebotest.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxbinding3.view.clicks
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import it.simonerenzo.docebotest.MainActivity
import it.simonerenzo.docebotest.R
import it.simonerenzo.docebotest.databinding.SearchFragmentBinding
import it.simonerenzo.docebotest.ui.viewmodel.SearchFormViewModel
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var dataBinding: SearchFragmentBinding
    private lateinit var viewModel: SearchFormViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.search_fragment, container, false)

        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SearchFormViewModel::class.java)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = viewModel

        setupListeners()
    }

    @SuppressLint("CheckResult")
    private fun setupListeners() {
        val activity = requireActivity() as MainActivity

        searchBtn.clicks()
            .bindToLifecycle(this)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                activity.searchCatalog(
                    resources.getStringArray(R.array.course_type_values)[courseTypeSpinner.selectedItemPosition],
                    viewModel.itemName.value ?: ""
                )
                    .bindToLifecycle(this)
                    .doOnSubscribe {
                        searchProgressBar.show()
                        searchBtn.isEnabled = false
                    }
                    .doOnTerminate {
                        searchProgressBar.hide()
                        searchBtn.isEnabled = true
                    }
                    .subscribe()
            }

        itemNameField.setOnEditorActionListener { _, actionId, _ ->
            if (EditorInfo.IME_ACTION_SEARCH == actionId) {
                searchBtn.performClick()
                true
            } else {
                false
            }
        }
    }

}
