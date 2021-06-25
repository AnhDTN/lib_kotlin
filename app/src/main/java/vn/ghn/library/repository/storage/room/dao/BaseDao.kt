package vn.ghn.library.repository.storage.room.dao

import androidx.room.*

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(model: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(array: Array<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(collection: Collection<T>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(model: T)

    @Delete
    suspend fun delete(model: T)

}