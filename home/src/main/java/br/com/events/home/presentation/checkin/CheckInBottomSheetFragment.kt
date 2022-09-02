package br.com.events.home.presentation.checkin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import br.com.events.home.databinding.FragmentBottomSheetCheckInBinding
import br.com.events.network.entity.Async
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckInBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetCheckInBinding? = null
    private val binding: FragmentBottomSheetCheckInBinding get() = _binding!!
    private val args: CheckInBottomSheetFragmentArgs by navArgs()
    private val viewModel by viewModel<CheckInViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentBottomSheetCheckInBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservables()
        setUpValidatorButton()
        setUpClickListener()
        setUpData()
    }

    private fun setUpData() {
        binding.tvTitleFragmentBottomSheetCheckIn.text = args.eventTitle
        binding.tvPriceFragmentBottomSheetCheckIn.text = args.eventPrice
    }

    private fun setUpObservables() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state.checkIn) {
                    is Async.Success -> loadingCheckInSuccess(state.checkIn.value)
                    is Async.Error -> setUpError(state.checkIn.message)
                    is Async.Loading -> setUpLoading(true)
                    else -> {}
                }
            }
        }

        binding.etNameFragmentBottomSheetCheckIn.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setUpValidatorButton()
            }
        })
        binding.etEmailFragmentBottomSheetCheckIn.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                setUpValidatorButton()
            }
        })
    }

    private fun loadingCheckInSuccess(success: String) {
        Toast.makeText(requireContext(), success, Toast.LENGTH_LONG).show()
        requireDialog().dismiss()
    }

    private fun setUpLoading(visible: Boolean) {
        if (visible) {
            binding.pbLoadingFragmentBottomSheetCheckIn.visibility = View.VISIBLE
        } else {
            binding.pbLoadingFragmentBottomSheetCheckIn.visibility = View.GONE
        }
    }

    private fun setUpError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun setUpValidatorButton() {
        val name = binding.etNameFragmentBottomSheetCheckIn.text.toString()
        val email = binding.etEmailFragmentBottomSheetCheckIn.text.toString()
        binding.btnFragmentBottomSheetCheckIn.isEnabled = name.isNotEmpty() && email.isNotEmpty()
    }

    private fun setUpClickListener() {
        binding.btnFragmentBottomSheetCheckIn.setOnClickListener {
            validatorForm()
        }
    }

    private fun validatorForm() {
        val name = binding.etNameFragmentBottomSheetCheckIn.text.toString()
        val email = binding.etEmailFragmentBottomSheetCheckIn.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty()) {
            viewModel.interact(
                CheckInInteractions.CheckInClicked(
                    args.eventId,
                    name,
                    email
                )
            )
        }
    }
}