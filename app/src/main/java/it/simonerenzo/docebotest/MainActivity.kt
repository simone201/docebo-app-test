package it.simonerenzo.docebotest

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mikepenz.materialize.MaterializeBuilder
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import it.simonerenzo.docebotest.client.model.DoceboModels
import it.simonerenzo.docebotest.client.service.DoceboService
import it.simonerenzo.docebotest.ui.fragment.CourseListFragment
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

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            onSupportNavigateUp()
        } else {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (supportFragmentManager.popBackStackImmediate()) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            true
        } else {
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_about -> {
                LibsBuilder()
                    .withAboutAppName(resources.getString(R.string.app_name))
                    .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                    .start(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
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
                        Log.i("API Result", "Response Items Count: ${res.data.count}")
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

    private fun showSearchResultFragment(response: DoceboModels.APIResponse) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container, CourseListFragment.newInstance(response.data))
            .commit()
    }

}
