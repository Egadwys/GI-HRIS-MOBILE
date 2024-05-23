package com.egadwys.gi_employee

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.egadwys.gi_employee.R

class DialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.TransparentDialogTheme)
    }

//    override fun onStart() {
//        super.onStart()
//        // Make the dialog full width
//        dialog?.window?.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT
//        )
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the custom layout for this fragment
        return inflater.inflate(R.layout.activity_detail_emp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val anotherLayout = layoutInflater.inflate(R.layout.activity_detail_emp, null)

        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val nikTextView: TextView = view.findViewById(R.id.nikTextView)
        val positionTextView: TextView = view.findViewById(R.id.positionTextView)
        val deptTextView: TextView = view.findViewById(R.id.deptTextView)
        val tinTextView: TextView = view.findViewById(R.id.tinTextView)
        val toutTextView: TextView = view.findViewById(R.id.toutTextView)

        val name = arguments?.getString("name")
        val nik = arguments?.getString("nik")
        val position = arguments?.getString("position")
        val dept = arguments?.getString("dept")
        val div = arguments?.getString("div")
        val wg = arguments?.getString("wg")
        val birth = arguments?.getString("birth")
        val kec = arguments?.getString("kec")
        val kel = arguments?.getString("kel")
        val kota = arguments?.getString("kota")
        val prov = arguments?.getString("prov")
        val negara = arguments?.getString("negara")
        val tin = arguments?.getString("tin")
        val tout = arguments?.getString("tout")

        nameTextView.text = name
        nikTextView.text = nik.toString()
        positionTextView.text = position
        deptTextView.text = dept
        tinTextView.text = tin
        toutTextView.text = tout
    }
}
