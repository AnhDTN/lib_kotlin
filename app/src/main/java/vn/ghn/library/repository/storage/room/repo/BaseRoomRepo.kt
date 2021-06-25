package vn.ghn.library.repository.storage.room.repo

import vn.ghn.library.repository.storage.room.dao.BaseDao

interface BaseRoomRepo<T> {
    abstract val dao: BaseDao<T>

    fun insert(model: T) {
        dao.insert(model)
    }

    fun insert(collection: Collection<T>) {
        dao.insert(collection)
    }

    fun insert(array: Array<T>) {
        dao.insert(array)
    }

    fun update(model: T) {
        dao.update(model)
    }

    fun delete(model: T) {
        dao.delete(model)
    }

    fun getAll(block : () -> Collection<T>) : Collection<T>{
        return block()
    }

    fun deleteAll(block: () -> Unit) {
        block()
    }

    fun updateAll(block : () -> Collection<T>) : Collection<T>{
        return block()
    }
}

