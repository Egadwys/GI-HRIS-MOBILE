package com.egadwys.gi_employee.dashboard.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.egadwys.gi_employee.R
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val t = view.findViewById<TextView>(R.id.ror)
        t.setOnClickListener {
            t.text = "ppppppppppppp"
        }
    }
}