package ikhwan.binar.binarchallengeempat.database.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun registerUser(user: User):Long

    @Query("SELECT * FROM User WHERE User.email = :email")
    fun getUserRegistered(email:String): User

}