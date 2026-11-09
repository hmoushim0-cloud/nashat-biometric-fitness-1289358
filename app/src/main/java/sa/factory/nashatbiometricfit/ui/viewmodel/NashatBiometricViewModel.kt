package sa.factory.nashatbiometricfit.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import sa.factory.nashatbiometricfit.domain.usecase.*
import sa.factory.nashatbiometricfit.model.NashatBiometricItem
import sa.factory.nashatbiometricfit.model.NashatBiometricOrder
import sa.factory.nashatbiometricfit.model.NashatBiometricTelemetry
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class NashatBiometricUiState(
    val catalog: List<NashatBiometricItem> = emptyList(),
    val filteredCatalog: List<NashatBiometricItem> = emptyList(),
    val selectedCategory: String = "الكل",
    val cart: List<NashatBiometricItem> = emptyList(),
    val telemetry: NashatBiometricTelemetry? = null,
    val lastOrder: NashatBiometricOrder? = null,
    val isCheckingOut: Boolean = false,
    val activeTab: String = "home"
)

class NashatBiometricViewModel(
    private val getCatalogUseCase: GetCatalogUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    private val processCheckoutUseCase: ProcessCheckoutUseCase,
    private val observeHardwareTelemetryUseCase: ObserveHardwareTelemetryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NashatBiometricUiState())
    val uiState: StateFlow<NashatBiometricUiState> = _uiState.asStateFlow()

    init {
        // Observe Catalog
        getCatalogUseCase().onEach { list ->
            _uiState.update { it.copy(catalog = list, filteredCatalog = list) }
        }.launchIn(viewModelScope)

        // Observe Cart
        manageCartUseCase.cart.onEach { cartItems ->
            _uiState.update { it.copy(cart = cartItems) }
        }.launchIn(viewModelScope)

        // Observe Telemetry
        observeHardwareTelemetryUseCase().onEach { telem ->
            _uiState.update { it.copy(telemetry = telem) }
        }.launchIn(viewModelScope)
    }

    fun selectCategory(category: String) {
        _uiState.update { state ->
            val filtered = if (category == "الكل") state.catalog else state.catalog.filter { it.category == category }
            state.copy(selectedCategory = category, filteredCatalog = filtered)
        }
    }

    fun switchTab(tab: String) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun addToCart(item: NashatBiometricItem) = manageCartUseCase.add(item)
    fun removeFromCart(item: NashatBiometricItem) = manageCartUseCase.remove(item)

    fun checkout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingOut = true) }
            val order = processCheckoutUseCase()
            _uiState.update { it.copy(isCheckingOut = false, lastOrder = order, activeTab = "checkout") }
        }
    }
}
