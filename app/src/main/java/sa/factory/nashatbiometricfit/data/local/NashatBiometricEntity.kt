package sa.factory.nashatbiometricfit.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import sa.factory.nashatbiometricfit.model.NashatBiometricItem

@Entity(tableName = "fitness_items_cache")
data class NashatBiometricEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val priceSar: Double,
    val badge: String,
    val category: String,
    val status: String
) {
    fun toDomain(): NashatBiometricItem = NashatBiometricItem(
        id = id,
        title = title,
        description = description,
        priceSar = priceSar,
        badge = badge,
        category = category,
        status = status
    )
}
