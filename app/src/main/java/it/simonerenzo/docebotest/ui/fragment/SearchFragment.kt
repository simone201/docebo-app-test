package it.simonerenzo.docebotest.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.jakewharton.rxbinding3.view.clicks
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import it.simonerenzo.docebotest.MainActivity
import it.simonerenzo.docebotest.R
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private val validators = AwesomeValidation(ValidationStyle.COLORATION)

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupValidations()
        setupListeners()
    }

    private fun setupValidations() {
        validators.addValidation(itemNameField,
            RegexTemplate.NOT_EMPTY,
            resources.getString(R.string.error_field_not_empty))
    }

    @SuppressLint("CheckResult")
    private fun setupListeners() {
        val activity = requireActivity() as MainActivity

        searchBtn.clicks()
            .bindToLifecycle(activity)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (validators.validate()) {
                    activity.searchCatalog(
                        resources.getStringArray(R.array.course_type_values)[courseTypeSpinner.selectedItemPosition],
                        itemNameField.text.toString()
                    )
                        .bindToLifecycle(activity)
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
            }
    }

}
