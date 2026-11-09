package sa.factory.nashatbiometricfit.data.repository

import sa.factory.nashatbiometricfit.domain.repository.NashatBiometricRepository
import sa.factory.nashatbiometricfit.hardware.HardwareSensorDriver
import sa.factory.nashatbiometricfit.model.NashatBiometricItem
import sa.factory.nashatbiometricfit.model.NashatBiometricOrder
import sa.factory.nashatbiometricfit.model.NashatBiometricTelemetry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NashatBiometricRepositoryImpl(
    private val sensorDriver: HardwareSensorDriver
) : NashatBiometricRepository {

    private val _catalog = MutableStateFlow<List<NashatBiometricItem>>(listOf(
        NashatBiometricItem("1", "مسح جسم ثلاثي الأبعاد", "تحليل دقيق لتركيب الجسم عبر ميزان BLE الذكي مع نمذجة ديناميكية للثوب السعودي", 180.0, "دقيق", "تحليل تركيب الجسم"),
        NashatBiometricItem("2", "تخصيص شخصية واقعية", "تعديل ملامح الوجه والجسم مع محاكاة فيزيائية للثوب باستخدام بيانات GATT 0x181D", 290.0, "متميز", "تدريبات مخصصة"),
        NashatBiometricItem("3", "تحدي نبضات القلب", "تمارين تفاعلية مع شخصية ثلاثية الأبعاد متزامنة مع حزام معدل ضربات القلب", 115.0, "جديد", "تطوير الشخصية الافتراضية")
    ))
    override val catalogFlow: Flow<List<NashatBiometricItem>> = _catalog.asStateFlow()

    private val _cart = MutableStateFlow<List<NashatBiometricItem>>(emptyList())
    override val activeCart: StateFlow<List<NashatBiometricItem>> = _cart.asStateFlow()

    override val telemetryFlow: Flow<NashatBiometricTelemetry> = sensorDriver.observePeripheralStream()
        .map { frame ->
            NashatBiometricTelemetry(
                peripheralName = frame.peripheralName,
                status = frame.connectionState,
                liveReading = frame.livePayload,
                batteryPct = 96,
                signalDbm = -54
            )
        }

    override fun addToCart(item: NashatBiometricItem) {
        val current = _cart.value.toMutableList()
        current.add(item)
        _cart.value = current
    }

    override fun removeFromCart(item: NashatBiometricItem) {
        val current = _cart.value.toMutableList()
        current.remove(item)
        _cart.value = current
    }

    override fun clearCart() {
        _cart.value = emptyList()
    }

    override suspend fun checkout(paymentMethod: String): NashatBiometricOrder {
        val currentItems = _cart.value.toList()
        val subtotal = currentItems.sumOf { it.priceSar }
        val vat = subtotal * 0.15
        val total = subtotal + vat
        val order = NashatBiometricOrder(
            orderId = "ORD-${System.currentTimeMillis() % 100000}",
            items = currentItems,
            subtotalSar = subtotal,
            vatSar = vat,
            totalSar = total,
            paymentMethod = paymentMethod
        )
        clearCart()
        return order
    }

    override suspend fun refreshCatalog() {
        // Offline-first sync logic
    }
}
