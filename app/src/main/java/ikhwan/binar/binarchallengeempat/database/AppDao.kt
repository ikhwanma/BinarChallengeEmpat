package ikhwan.binar.binarchallengeempat.database

import androidx.room.*

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote(note: Note): Long

    @Update
    fun updateNote(note: Note): Int

    @Delete
    fun deleteNote(note: Note): Int

    @Query("SELECT * FROM Note WHERE Note.email = :email")
    fun getNote(email: String) : List<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun registerUser(user: User):Long

    @Query("SELECT * FROM User WHERE User.email = :email")
    fun getUserRegistered(email:String): User
}