package sa.factory.nashatbiometricfit.data.remote

import sa.factory.nashatbiometricfit.model.NashatBiometricItem
import sa.factory.nashatbiometricfit.model.NashatBiometricOrder

interface NashatBiometricApiService {
    suspend fun fetchCatalog(): List<NashatBiometricItem>
    suspend fun submitOrder(order: NashatBiometricOrder): Boolean
}
