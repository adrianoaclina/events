package br.com.events.home.presentation.homefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.events.home.databinding.FragmentHomeBinding
import br.com.events.home.domain.entity.Event
import br.com.events.home.presentation.adapter.HomeListAdapter
import br.com.events.network.entity.Async
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    private val viewModel by viewModel<HomeViewModel>()

    private val listAdapter by lazy {
        HomeListAdapter(
            homeDelegate = homeDelegate()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHomeBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycler()
        setUpObservers()
        viewModel.interact(HomeInteractions.LoadEvents)
    }

    private fun setUpObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state.events) {
                    is Async.Success -> loadingEvents(state.events.value)
                    is Async.Error -> setUpError(state.events.message)
                    is Async.Loading -> setUpLoading(true)
                }
            }
        }
    }

    private fun setUpError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun setUpLoading(visible: Boolean) {
        if (visible) {
            binding.pbLoadingFragmentHome.visibility = View.VISIBLE
        } else {
            binding.pbLoadingFragmentHome.visibility = View.GONE
        }

    }

    private fun setUpRecycler() {
        binding.rvEventsFragmentHome.apply {
            val listLayoutManager = LinearLayoutManager(requireContext())
            layoutManager = listLayoutManager
            setHasFixedSize(true)
            adapter = listAdapter
        }
    }

    private fun loadingEvents(events: List<Event>) {
        listAdapter.refresh(events)
        setUpLoading(false)
    }

    private fun homeDelegate() = object : HomeDelegate {
        override fun navToEventDetails(eventId: String, eventName: String, eventPrice: String) {
            findNavController().navigate(HomeFragmentDirections.navToEventDetails(eventId, eventName, eventPrice))
        }
    }
}
