package vn.ghn.library.view.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import vn.ghn.library.R
import vn.ghn.library.extensions.addFragment
import vn.ghn.library.extensions.removeFragment
import vn.ghn.library.extensions.replaceFragment

abstract class BaseActivity : AppCompatActivity(), BaseView {
    override val baseActivity: BaseActivity
        get() = this

    override fun onViewClick(v: View?) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource())
        onViewCreated()
        onLiveDataObserve()
    }

    fun requestActivityResult(intent: Intent) {
        resultLauncher.launch(intent)
    }

    abstract  fun didActivityResult(data: Intent?)

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            didActivityResult(data)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit)
    }

    /**
     *  add, replace, remove fragment
    * */

    final override fun add(fragment: Fragment, stack: Boolean) {
        addFragment(fragment, fragmentContainerId(), stack)
    }

    final override fun replace(fragment: Fragment, stack: Boolean) {
        replaceFragment(fragment, fragmentContainerId(), stack)
    }

    final override fun <T : Fragment> remove(cls: Class<T>) {
        removeFragment(cls)
    }

    /**
     * Get nav controller
   * */

    val nav get() = findNavController(navHostId())

    /**
     * Fragment properties
    * */
    protected open fun navHostId(): Int {
        throw NullPointerException("navigationHostId no has implement")
    }

    protected open fun fragmentContainerId() : Int {
        throw NullPointerException("fragmentContainerId no has implement")
    }

    fun <T> LiveData<T>.observe(block: (T) -> Unit) {
        observe(this@BaseActivity, Observer(block))
    }
}