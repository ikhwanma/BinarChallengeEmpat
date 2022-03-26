package ikhwan.binar.binarchallengeempat.database.note

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(entities = [Note::class],version = 1)
abstract class NoteDatabase : RoomDatabase(){
    abstract fun noteDao(): NoteDao

    companion object{
        private var INSTANCE: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase? {
            if(NoteDatabase.INSTANCE == null){
                synchronized(NoteDatabase::class){
                    NoteDatabase.INSTANCE = Room.databaseBuilder(context.applicationContext, NoteDatabase::class.java, "Note.db").build()
                }
            }
            return NoteDatabase.INSTANCE
        }

        fun destroyInstance(){
            NoteDatabase.INSTANCE = null
        }
    }
}