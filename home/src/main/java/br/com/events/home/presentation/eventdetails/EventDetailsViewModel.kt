package br.com.events.home.presentation.eventdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.events.home.domain.repository.EventResultRepository
import br.com.events.network.entity.Async
import br.com.events.network.entity.onError
import br.com.events.network.entity.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventDetailsViewModel(
    private val eventResultRepository: EventResultRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EventDetailsState(Async.Loading))
    val state: StateFlow<EventDetailsState> = _state

    fun interact(interaction: EventDetailsInteractions) {
        when (interaction) {
            is EventDetailsInteractions.Opened -> getEvent(interaction.eventId)
        }
    }

    private fun getEvent(eventId: String) = viewModelScope.launch {
        eventResultRepository.getEvent(eventId)
            .onSuccess { event ->
                _state.update {
                    it.copy(event = Async.Success(event))
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(event = Async.Error(error.message))
                }
            }
    }
}