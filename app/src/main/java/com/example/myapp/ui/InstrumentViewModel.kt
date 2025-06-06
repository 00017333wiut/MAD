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
        Log.d("ViewModel", "Getting instruments")
        instrumentUiState = InstrumentUiState.Loading
        viewModelScope.launch {
            instrumentUiState = try {
                val instruments = instrumentRepository.getInstruments()
                Log.d("ViewModel", "Got ${instruments.size} instruments")
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

    fun addInstrument(instrumentRequest: Instrument) {
        _operationState.update { OperationState.Loading }
        viewModelScope.launch {
            try {
                val response = instrumentRepository.insertNewInstrument(instrumentRequest)
                if (response.status == "OK") {
                    _operationState.update { OperationState.Success(response.message) }
                    // refresh the instrument list
                    getInstruments()
                } else {
                    _operationState.update { OperationState.Error(response.message) }
                }
            } catch (e: Exception) {
                _operationState.update { OperationState.Error("Error adding instrument: ${e.message}") }
            }
        }
    }

    fun updateInstrument(instrumentId: Int?, instrumentRequest: Instrument) {
        _operationState.update { OperationState.Loading }
        viewModelScope.launch {
            try {
                Log.d("ViewModel", "Updating instrument $instrumentId: $instrumentRequest")
                val response = instrumentRepository.updateInstrument(instrumentId, instrumentRequest)
                Log.d("ViewModel", "Update response: ${response.status} - ${response.message}")

                if (response.status == "OK") {
                    _operationState.update { OperationState.Success(response.message) }
                    Log.d("ViewModel", "Refreshing instruments after update")
                    getInstruments()
                } else {
                    _operationState.update { OperationState.Error(response.message) }
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error updating instrument: ${e.message}")
                _operationState.update { OperationState.Error("Error updating instrument: ${e.message}") }
            }
        }
    }

    fun deleteInstrument(instrumentId: Int?) {
        _operationState.update { OperationState.Loading }
        viewModelScope.launch {
            try {
                Log.d("ViewModel", "Deleting instrument: $instrumentId")
                val response = instrumentRepository.deleteInstrumentById(instrumentId)
                Log.d("ViewModel", "Delete response: ${response.status} - ${response.message}")

                if (response.status == "OK") {
                    _operationState.update { OperationState.Success(response.message) }
                    Log.d("ViewModel", "Refreshing instruments after delete")
                    getInstruments()
                } else {
                    _operationState.update { OperationState.Error(response.message) }
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error deleting instrument: ${e.message}")
                _operationState.update { OperationState.Error("Error deleting instrument: ${e.message}") }
            }
        }
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