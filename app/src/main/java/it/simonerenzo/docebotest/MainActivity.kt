package it.simonerenzo.docebotest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mikepenz.materialize.MaterializeBuilder
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import com.trello.rxlifecycle3.kotlin.bindToLifecycle
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import it.simonerenzo.docebotest.client.model.DoceboModels
import it.simonerenzo.docebotest.client.service.DoceboService
import it.simonerenzo.docebotest.ui.fragment.SearchFragment
import kotlinx.android.synthetic.main.main_activity.*
import org.jetbrains.anko.design.snackbar

class MainActivity : AppCompatActivity() {

    private val doceboService by lazy { DoceboService.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        MaterializeBuilder().withActivity(this).build()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SearchFragment.newInstance())
                .commitNow()
        }
    }

    fun searchCatalog(courseType: String, itemName: String) : Completable {
        return Completable.create {
            doceboService.getCatalog(courseType, itemName)
                .bindToLifecycle(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { res ->
                        Log.i("API Result", "getCatalog: $res")
                        it.onComplete()
                        showSearchResultFragment(res)
                    },
                    { err ->
                        Log.e("API Error", "getCatalog", err)
                        container.snackbar(R.string.error_request_failed)
                        it.onError(err)
                    }
                )
        }
    }

    private fun showSearchResultFragment(results: DoceboModels.APIResponse) {
        // TODO: change fragment to list
    }

}
