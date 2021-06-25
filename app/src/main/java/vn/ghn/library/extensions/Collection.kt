package vn.ghn.library.extensions

fun <T> Collection<T>?.isNullOrEmpty() : Boolean {
    return this == null || isEmpty()
}

fun <T> Collection<T>?.isNotNullOrEmpty() : Boolean {
    return this != null && isNotEmpty()
}

fun <T> join(vararg collection : Collection<T>) : Collection<T>{
    val list = mutableListOf<T>()
    collection.forEach {
        if(it.isNotNullOrEmpty()) {
            list.addAll(it)
        }
    }
    return list
}


fun <T, R> Collection<T>.transform(block: (T) -> R?): List<R> {
    val list = mutableListOf<R>()
    for (item in this) {
        block(item)?.also {
            list.add(it)
        }
    }
    return list
}


fun <T> Collection<T?>?.filters(block: (T) -> T?): List<T>? {
    this ?: return null
    val list = mutableListOf<T>()
    for (item in this) {
        item ?: continue
        val filterItem = block(item) ?: continue
        list.add(filterItem)
    }
    return list
}

/**
 * Typed T should be override method toString() : String
 */

fun <T> Collection<T>?.search(s: String?): Collection<T>? {
    if (s.isNullOrEmpty() || this.isNullOrEmpty()) return this
    val list = mutableListOf<T>()
    for (model in this!!) {
        if (s.contains(model.toString())) list.add(model)
    }
    return list
}