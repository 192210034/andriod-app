package com.simats.aspirebridge.ui.auth

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.simats.aspirebridge.R
import com.simats.aspirebridge.databinding.FragmentOtpVerificationBinding

class OtpVerificationFragment : Fragment() {

    private var _binding: FragmentOtpVerificationBinding? = null
    private val binding get() = _binding!!

    private var countDownTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOtpVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setupOtpInputs()
        startResendTimer()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnVerify.setOnClickListener {
            if (validateOtp()) {
                // Navigate to onboarding or main screen
                findNavController().navigate(R.id.action_otp_verification_to_onboarding)
            }
        }

        binding.textResendTimer.setOnClickListener {
            if (binding.textResendTimer.text.toString().contains("Resend")) {
                // Resend OTP logic
                startResendTimer()
            }
        }
    }

    private fun setupOtpInputs() {
        val otpInputs = listOf(
            binding.otpDigit1,
            binding.otpDigit2,
            binding.otpDigit3,
            binding.otpDigit4,
            binding.otpDigit5,
            binding.otpDigit6
        )

        otpInputs.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && index < otpInputs.size - 1) {
                        otpInputs[index + 1].requestFocus()
                    } else if (s?.isEmpty() == true && index > 0) {
                        otpInputs[index - 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun startResendTimer() {
        countDownTimer?.cancel()
        
        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                binding.textResendTimer.text = "in ${seconds}s"
                binding.textResendTimer.isClickable = false
            }

            override fun onFinish() {
                binding.textResendTimer.text = "Resend OTP"
                binding.textResendTimer.isClickable = true
            }
        }.start()
    }

    private fun validateOtp(): Boolean {
        val otp = buildString {
            append(binding.otpDigit1.text.toString())
            append(binding.otpDigit2.text.toString())
            append(binding.otpDigit3.text.toString())
            append(binding.otpDigit4.text.toString())
            append(binding.otpDigit5.text.toString())
            append(binding.otpDigit6.text.toString())
        }

        return if (otp.length == 6) {
            // TODO: Verify OTP with backend
            true
        } else {
            // Show error
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        _binding = null
    }
}