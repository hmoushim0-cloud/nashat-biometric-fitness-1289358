package sa.factory.nashatbiometricfit.domain.repository

import sa.factory.nashatbiometricfit.model.NashatBiometricItem
import sa.factory.nashatbiometricfit.model.NashatBiometricOrder
import sa.factory.nashatbiometricfit.model.NashatBiometricTelemetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NashatBiometricRepository {
    val catalogFlow: Flow<List<NashatBiometricItem>>
    val activeCart: StateFlow<List<NashatBiometricItem>>
    val telemetryFlow: Flow<NashatBiometricTelemetry>
    
    fun addToCart(item: NashatBiometricItem)
    fun removeFromCart(item: NashatBiometricItem)
    fun clearCart()
    suspend fun checkout(paymentMethod: String): NashatBiometricOrder
    suspend fun refreshCatalog()
}
