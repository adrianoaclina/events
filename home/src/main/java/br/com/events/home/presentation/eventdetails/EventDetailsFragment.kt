package br.com.events.home.presentation.eventdetails

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.events.home.databinding.FragmentEventDetailsBinding
import br.com.events.home.domain.entity.Event
import br.com.events.network.entity.Async
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class EventDetailsFragment : Fragment() {

    private var _binding: FragmentEventDetailsBinding? = null
    private val binding: FragmentEventDetailsBinding get() = _binding!!
    private val args: EventDetailsFragmentArgs by navArgs()
    private val viewModel by viewModel<EventDetailsViewModel>()
    private var loadedEvent: Event?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentEventDetailsBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservables()
        setUpClickListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.interact(EventDetailsInteractions.Opened(args.eventId))
    }

    private fun setUpClickListener() {
        binding.btnCheckInFragmentEventDetails.setOnClickListener {
            findNavController().navigate(
                EventDetailsFragmentDirections.openCheckInBottomSheet(
                    args.eventId,
                    args.eventTitle,
                    args.eventPrice
                )
            )
        }
        binding.fabShareCheckInFragmentEventDetails.setOnClickListener {
            if (loadedEvent != null){
                setShareIntent(loadedEvent!!)
            }else{
                setUpError("Aguarde o carregamento do evento para poder compartilhar!")
            }
        }
    }

    private fun setShareIntent(event: Event) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareMessage(event))
        startActivity(Intent.createChooser(sharingIntent, "Compartilhando evento ${event.title}"))
    }

    private fun shareMessage(event: Event): String? {
        return "${event.title} no dia ${getDateTime(event.date)} por ${args.eventPrice}"
    }

    private fun setUpObservables() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state.event) {
                    is Async.Success -> loadingEvent(state.event.value)
                    is Async.Error -> setUpError(state.event.message)
                    is Async.Loading -> setUpLoading(true)
                }
            }
        }
    }

    private fun setUpError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun loadingEvent(event: Event) {
        loadedEvent = event
        val moeda = DecimalFormat.getCurrencyInstance(Locale("pt", "BR"))
        setUpLoading(false)
        Picasso.get().load(event.image).into(binding.ivLogoFragmentEventDetails)
        binding.tvTitleFragmentEventDetails.text = event.title
        binding.tvDescriptionFragmentEventDetails.text = event.description
        binding.tvTextDataFragmentEventDetails.text = getDateTime(event.date)
        binding.tvTextPriceFragmentEventDetails.text = moeda.format(event.price)
    }

    private fun setUpLoading(visible: Boolean) {
        if (visible) {
            binding.pbLoadingFragmentEventDetails.visibility = View.VISIBLE
        } else {
            binding.pbLoadingFragmentEventDetails.visibility = View.GONE
        }
    }

    private fun getDateTime(date: Long): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getDateTimeApiMajor26(date)
        } else {
            getDateTimeApiMinor26(date)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateTimeApiMajor26(date: Long): String? {
        return try {
            val dateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.of("America/Sao_Paulo"))
            val month = if (dateTime.monthValue < 10) {
                "0" + dateTime.monthValue
            } else {
                dateTime.monthValue
            }
            """${dateTime.dayOfMonth}/$month/${dateTime.year} ${dateTime.hour}:${dateTime.minute}${dateTime.second}"""
        } catch (e: Exception) {
            e.toString()
        }
    }

    private fun getDateTimeApiMinor26(date: Long): String {
        return try {
            val dateTime = org.threeten.bp.LocalDateTime.ofInstant(
                org.threeten.bp.Instant.ofEpochMilli(date),
                org.threeten.bp.ZoneId.of("America/Sao_Paulo")
            )

            val month = if (dateTime.monthValue < 10) {
                "0" + dateTime.monthValue
            } else {
                dateTime.monthValue
            }
            """${dateTime.dayOfMonth}/$month/${dateTime.year} ${dateTime.hour}:${dateTime.minute}${dateTime.second}"""
        } catch (e: Exception) {
            e.toString()
        }
    }
}