package vn.ghn.library.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlin.reflect.KClass

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