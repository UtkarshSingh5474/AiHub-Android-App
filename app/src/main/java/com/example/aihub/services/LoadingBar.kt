package com.example.aihub.services

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.aihub.R
import com.google.android.material.snackbar.Snackbar

class LoadingBar(context: Context, str: String) : Dialog(context) {
    val make =
        Snackbar.make(findViewById(android.R.id.content), "$str", Snackbar.LENGTH_INDEFINITE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.loading_bar)
        setCancelable(false)
        make.show()
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onBackPressed() {
        // do nothing
    }

    override fun dismiss() {
        super.dismiss()
        val anim = findViewById<LottieAnimationView>(R.id.animView)
        make.dismiss()
        anim.cancelAnimation()
    }
}