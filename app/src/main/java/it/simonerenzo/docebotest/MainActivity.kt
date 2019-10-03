package it.simonerenzo.docebotest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import it.simonerenzo.docebotest.ui.fragment.SearchFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SearchFragment.newInstance())
                .commitNow()
        }
    }

}
