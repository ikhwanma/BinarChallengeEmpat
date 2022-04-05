package ikhwan.binar.binarchallengeempat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [User::class, Note::class],version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao() : AppDao

    companion object{
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if(AppDatabase.INSTANCE == null){
                synchronized(AppDatabase::class){
                    AppDatabase.INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "App.db").build()
                }
            }
            return AppDatabase.INSTANCE
        }

        fun destroyInstance(){
            AppDatabase.INSTANCE = null
        }
    }
}