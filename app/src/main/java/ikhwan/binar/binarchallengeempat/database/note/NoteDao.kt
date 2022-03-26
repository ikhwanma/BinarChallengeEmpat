package ikhwan.binar.binarchallengeempat.database.note

import androidx.room.*
import ikhwan.binar.binarchallengeempat.database.user.User

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote(note: Note): Long

    @Update
    fun updateNote(note: Note): Int

    @Delete
    fun deleteNote(note: Note): Int

    @Query("SELECT * FROM Note WHERE Note.email = :email")
    fun getNote(email: String) : List<Note>
}