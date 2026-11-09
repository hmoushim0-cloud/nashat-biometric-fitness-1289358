package sa.factory.nashatbiometricfit.domain.usecase

import sa.factory.nashatbiometricfit.domain.repository.NashatBiometricRepository
import sa.factory.nashatbiometricfit.model.NashatBiometricItem
import sa.factory.nashatbiometricfit.model.NashatBiometricOrder
import sa.factory.nashatbiometricfit.model.NashatBiometricTelemetry
import kotlinx.coroutines.flow.Flow

class GetCatalogUseCase(private val repository: NashatBiometricRepository) {
    operator fun invoke(): Flow<List<NashatBiometricItem>> = repository.catalogFlow
}

class ManageCartUseCase(private val repository: NashatBiometricRepository) {
    val cart = repository.activeCart
    fun add(item: NashatBiometricItem) = repository.addToCart(item)
    fun remove(item: NashatBiometricItem) = repository.removeFromCart(item)
    fun clear() = repository.clearCart()
}

class ProcessCheckoutUseCase(private val repository: NashatBiometricRepository) {
    suspend operator fun invoke(paymentMethod: String = "Mada Contactless EMV"): NashatBiometricOrder =
        repository.checkout(paymentMethod)
}

class ObserveHardwareTelemetryUseCase(private val repository: NashatBiometricRepository) {
    operator fun invoke(): Flow<NashatBiometricTelemetry> = repository.telemetryFlow
}
