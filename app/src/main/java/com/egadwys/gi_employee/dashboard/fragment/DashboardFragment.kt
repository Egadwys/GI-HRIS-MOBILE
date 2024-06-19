package com.egadwys.gi_employee.dashboard.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.egadwys.gi_employee.R
import com.egadwys.gi_employee.payroll.Payroll
import com.egadwys.gi_employee.spl.Spl

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private lateinit var rekapspl: LinearLayout
    private lateinit var payroll: LinearLayout
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rekapspl = view.findViewById(R.id.rekapspl)
        payroll = view.findViewById(R.id.payroll)

        rekapspl.setOnClickListener {
            vibrate()
            val intent = Intent(requireContext(), Spl::class.java)
            startActivity(intent)
        }

        payroll.setOnClickListener {
            vibrate()
            val intent = Intent(requireContext(), Payroll::class.java)
            startActivity(intent)
        }
    }

    private fun vibrate() {
        val vibrator = ContextCompat.getSystemService(requireContext(), Vibrator::class.java) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Vibrate for 100 milliseconds
            val vibrationEffect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            // Deprecated in API 26
            vibrator.vibrate(100)
        }
    }
}