package br.com.events.home.presentation.checkin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.events.home.domain.repository.CheckInRepository
import br.com.events.network.entity.Async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckInViewModel(
    private val checkInRepository: CheckInRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CheckInState())
    val state: StateFlow<CheckInState> = _state

    fun interact(interaction: CheckInInteractions) {
        when (interaction) {
            is CheckInInteractions.CheckInClicked -> sendCheckIn(
                interaction.eventId,
                interaction.name,
                interaction.email
            )
        }
    }

    private fun sendCheckIn(
        eventId: String,
        name: String,
        email: String
    ) = viewModelScope.launch {
        _state.update {
            it.copy(checkIn = Async.Loading)
        }
        checkInRepository.sendCheckIn(
            eventId, name, email,
            isSuccess = {
                _state.update {
                    it.copy(checkIn = Async.Success("Check-in realizado com sucesso"))
                }
            },
            isError = { error ->
                if (error != null){
                    _state.update {
                        it.copy(checkIn = Async.Error(error))
                    }
                }
            }
        )
    }
}