package com.egadwys.gi_employee

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailEmp : AppCompatActivity(){

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_emp)

//        val nameTextView: TextView = findViewById(R.id.nameTextView)
//        val nikTextView: TextView = findViewById(R.id.nikTextView)
//        val positionTextView: TextView = findViewById(R.id.positionTextView)
//        val deptTextView: TextView = findViewById(R.id.deptTextView)
//        val tinTextView: TextView = findViewById(R.id.tinTextView)
//        val toutTextView: TextView = findViewById(R.id.toutTextView)
//
//        val name = intent.getStringExtra("name")
//        val nik = intent.getIntExtra("nik", 0)
//        val position = intent.getStringExtra("position")
//        val dept = intent.getStringExtra("dept")
//        val div = intent.getStringExtra("div")
//        val wg = intent.getStringExtra("wg")
//        val birth = intent.getStringExtra("birth")
//        val kec = intent.getStringExtra("kec")
//        val kel = intent.getStringExtra("kel")
//        val kota = intent.getStringExtra("kota")
//        val prov = intent.getStringExtra("prov")
//        val negara = intent.getStringExtra("negara")
//        val tin = intent.getStringExtra("tin")
//        val tout = intent.getStringExtra("tout")
//
//        Toast.makeText(this, "Name: $name, NIK: $nik", Toast.LENGTH_LONG).show()
//
//        nameTextView.text = name
//        nikTextView.text = nik.toString()
//        positionTextView.text = position
//        deptTextView.text = dept
//        tinTextView.text = tin
//        toutTextView.text = tout
    }
}