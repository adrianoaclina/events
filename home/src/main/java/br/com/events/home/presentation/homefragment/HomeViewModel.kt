package br.com.events.home.presentation.homefragment

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

class HomeViewModel(
    private val eventResultRepository: EventResultRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState(Async.Loading))
    val state: StateFlow<HomeState> = _state

    fun interact(interaction: HomeInteractions) {
        when (interaction) {
            is HomeInteractions.LoadEvents -> getEvents()
        }
    }

    private fun getEvents() = viewModelScope.launch {
        eventResultRepository.getEvents()
            .onSuccess { events ->
                _state.update {
                    it.copy(
                        events = Async.Success(events)
                    )
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        events = Async.Error(message = error.message)
                    )
                }
            }
    }

}