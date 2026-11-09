package sa.factory.nashatbiometricfit.di

import sa.factory.nashatbiometricfit.data.repository.NashatBiometricRepositoryImpl
import sa.factory.nashatbiometricfit.domain.repository.NashatBiometricRepository
import sa.factory.nashatbiometricfit.domain.usecase.*
import sa.factory.nashatbiometricfit.hardware.HardwareSensorDriver
import sa.factory.nashatbiometricfit.ui.viewmodel.NashatBiometricViewModel

class DiContainer {
    val sensorDriver = HardwareSensorDriver()
    val repository: NashatBiometricRepository = NashatBiometricRepositoryImpl(sensorDriver)

    val getCatalogUseCase = GetCatalogUseCase(repository)
    val manageCartUseCase = ManageCartUseCase(repository)
    val processCheckoutUseCase = ProcessCheckoutUseCase(repository)
    val observeHardwareTelemetryUseCase = ObserveHardwareTelemetryUseCase(repository)

    fun provideViewModel(): NashatBiometricViewModel = NashatBiometricViewModel(
        getCatalogUseCase,
        manageCartUseCase,
        processCheckoutUseCase,
        observeHardwareTelemetryUseCase
    )
}
