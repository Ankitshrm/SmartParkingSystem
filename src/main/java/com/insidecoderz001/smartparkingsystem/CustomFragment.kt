package com.insidecoderz001.smartparkingsystem

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class CustomFragment : DialogFragment() {

    lateinit var ok:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_custom, container, false)
        ok=view.findViewById(R.id.ok)
        ok.setOnClickListener {
            dismiss()
        }
        return view
    }

}