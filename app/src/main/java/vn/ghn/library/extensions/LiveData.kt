@file:Suppress("UNCHECKED_CAST")

package vn.ghn.library.extensions

import android.util.Log
import androidx.annotation.MainThread
import androidx.collection.ArraySet
import androidx.lifecycle.*
import java.util.ArrayList


/**
 * Live data extentions
 */

fun <T> LiveData<T>.nonNull(): NonNullLiveData<T> {
    val mediator: NonNullLiveData<T> = NonNullLiveData()
    mediator.addSource(this) { data ->
        data?.let {
            mediator.value = data
        }
    }
    return mediator
}

fun <T> MutableLiveData<ArrayList<T>>.delete(model: T) {
    val array = this.value
    array?.remove(model)
    this.postValue(array)
}

fun <T> MutableLiveData<ArrayList<T>>.updateList(index: Int, model: T) {
    val array = this.value
    array?.set(index, model)
    this.value = array
}

fun <R, T : LiveData<T>> T.single(): T {
    val result = SingleLiveData<R>()
    result.addSource(this) {
        result.value = it as R
    }
    return result as T
}


@Suppress("UNCHECKED_CAST")
fun <R, T : LiveData<R>> T.noneNull(): T {
    val result = NonNullLiveData<R>()
    result.addSource(this) {
        result.value = it as R
    }
    return result as T
}

inline fun <T> LiveData<T?>.observe(owner: LifecycleOwner, crossinline block: (t: T?) -> Unit) {
    this.observe(owner, Observer {
        block(it)
    })
}

fun <T> NonNullLiveData<T>.observe(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, Observer {
        it?.let(observer)
    })
}


/**
 *  LiveData only trigger 1 time when data change
 * */
open class SingleLiveData<T> : MediatorLiveData<T>() {

    private val observers = ArraySet<ObserverWrapper<in T>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        observers.find { it.observer === observer }?.let { _ -> // existing
            return
        }
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observe(owner, wrapper)
    }

    @MainThread
    override fun observeForever(observer: Observer<in T>) {
        observers.find { it.observer === observer }?.let { _ -> // existing
            return
        }
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observeForever(wrapper)
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        if (observer is ObserverWrapper && observers.remove(observer)) {
            super.removeObserver(observer)
            return
        }
        val iterator = observers.iterator()
        while (iterator.hasNext()) {
            val wrapper = iterator.next()
            if (wrapper.observer == observer) {
                iterator.remove()
                super.removeObserver(wrapper)
                break
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        observers.forEach { it.newValue() }
        super.setValue(t)
    }

    private class ObserverWrapper<T>(val observer: Observer<T>) : Observer<T> {

        private var pending = false

        override fun onChanged(t: T?) {
            if (pending) {
                pending = false
                observer.onChanged(t)
            }
        }

        fun newValue() {
            pending = true
        }
    }
}

/**
 * Live data only trigger when data change if value none null
 */
class NonNullLiveData<T> : MediatorLiveData<T>()