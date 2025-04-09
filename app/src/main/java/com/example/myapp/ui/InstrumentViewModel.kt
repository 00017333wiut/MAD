package com.example.myapp.ui
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapp.data.Instrument
import com.example.myapp.data.InstrumentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import com.example.myapp.ui.InstrumentViewModel

sealed interface InstrumentUiState {
    data class Success(val instruments: List<Instrument>) : InstrumentUiState
    data class Error(val message: String = "Error!") : InstrumentUiState
    object Loading : InstrumentUiState
}


class InstrumentViewModel(private val instrumentRepository: InstrumentRepository) : ViewModel() {

    // UI state for the UI
    var instrumentUiState: InstrumentUiState by mutableStateOf(InstrumentUiState.Loading)
        private set

    // State to track crud
    private val _operationState = MutableStateFlow<OperationState>(OperationState.Idle)
    val operationState: StateFlow<OperationState> = _operationState.asStateFlow()

    init {
        getInstruments()
    }

    fun getInstruments() {
        instrumentUiState = InstrumentUiState.Loading
        viewModelScope.launch {
            instrumentUiState = try {
                val instruments = instrumentRepository.getInstruments()
                Log.d("ViewModel", "Got ${instruments.size} instruments")
                if (instruments.isEmpty()) {
                    Log.d("ViewModel", "Instruments list is empty")
                } else {
                    Log.d("ViewModel", "First instrument: ${instruments[0]}")
                }
                InstrumentUiState.Success(instruments)
            } catch (e: IOException) {
                Log.e("ViewModel", "Network error: ${e.message}")
                InstrumentUiState.Error("Network error: ${e.message}")
            } catch (e: Exception) {
                Log.e("ViewModel", "Error: ${e.message}")
                InstrumentUiState.Error("Error: ${e.message}")
            }
        }
    }

//    fun addInstrument(instrumentRequest: InstrumentRequest) {
//        _operationState.update { OperationState.Loading }
//        viewModelScope.launch {
//            try {
//                val response = instrumentRepository.insertNewInstrument(instrumentRequest)
//                if (response.status == "OK") {
//                    _operationState.update { OperationState.Success(response.message) }
//                    // refresh the instrument list
//                    getInstruments()
//                } else {
//                    _operationState.update { OperationState.Error(response.message) }
//                }
//            } catch (e: Exception) {
//                _operationState.update { OperationState.Error("Error adding instrument: ${e.message}") }
//            }
//        }
//    }

//    fun updateInstrument(instrumentId: Int, instrumentRequest: InstrumentRequest) {
//        _operationState.update { OperationState.Loading }
//        viewModelScope.launch {
//            try {
//                val response = instrumentRepository.updateInstrument(instrumentId, instrumentRequest)
//                if (response.status == "OK") {
//                    _operationState.update { OperationState.Success(response.message) }
//                    // Refresh the instrument list
//                    getInstruments()
//                } else {
//                    _operationState.update { OperationState.Error(response.message) }
//                }
//            } catch (e: Exception) {
//                _operationState.update { OperationState.Error("Error updating instrument: ${e.message}") }
//            }
//        }
//    }
    fun deleteInstrument(instrumentId: Int?) {
//        _operationState.update { OperationState.Loading }
//        viewModelScope.launch {
//            try {
//                val response = instrumentRepository.deleteInstrumentById(instrumentId)
//                if (response.status == "OK") {
//                    _operationState.update { OperationState.Success(response.message) }
//                    // Refresh the instrument list
//                    getInstruments()
//                } else {
//                    _operationState.update { OperationState.Error(response.message) }
//                }
//            } catch (e: Exception) {
//                _operationState.update { OperationState.Error("Error deleting instrument: ${e.message}") }
//            }
//        }
   }

    fun resetOperationState() {
        _operationState.update { OperationState.Idle }
    }

    //creating viewmodel
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                InstrumentViewModel(InstrumentRepository())
            }
        }
    }
}

sealed class OperationState {
    object Idle : OperationState()
    object Loading : OperationState()
    data class Success(val message: String) : OperationState()
    data class Error(val message: String) : OperationState()
}