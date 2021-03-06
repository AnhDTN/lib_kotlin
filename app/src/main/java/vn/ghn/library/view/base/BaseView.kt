package vn.ghn.library.view.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlin.reflect.KClass

interface BaseView {


    val baseActivity: BaseActivity?

    fun add(fragment: Fragment, stack: Boolean = true) {
//        baseActivity?.add(fragment, stack)
    }

    fun replace(fragment: Fragment, stack: Boolean = true) {
//        baseActivity?.replace(fragment, stack)
    }

    fun <T : Fragment> remove(cls: Class<T>) {
//        baseActivity?.remove(cls)
    }

    abstract fun layoutResource(): Int

    abstract fun onViewCreated()

    abstract fun onLiveDataObserve()

    fun onViewClick(v : View?)


    fun addClickListener(vararg views: View?) {
        val listener = object : ViewClickListener() {
            override fun onClicks(v: View?) {
                onViewClick(v)
            }
        }
        views.forEach { it?.setOnClickListener(listener) }
    }

    fun <T : ViewModel> ViewModelStoreOwner.viewModel(cls : KClass<T>) : T {
        return ViewModelProvider(this).get(cls.java)
    }

    fun <T : ViewModel> ViewModelStoreOwner.newVM(cls : KClass<T>) : T {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[cls.java]
    }

    fun <T : ViewModel> Fragment.activityVM(cls : KClass<T>) : T {
        return ViewModelProvider(requireActivity()).get(cls.java)
    }

    fun <T : ViewModel> AppCompatActivity.activityVM(cls : KClass<T>) : T {
        return ViewModelProvider(this).get(cls.java)
    }

}