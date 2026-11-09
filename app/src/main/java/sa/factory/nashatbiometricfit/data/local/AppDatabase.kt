package sa.factory.nashatbiometricfit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NashatBiometricEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fitnessDao(): NashatBiometricDao
}
