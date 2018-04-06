package com.bignerdranch.android.calculator

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {
    var color = ""
    val white = "#FFFFFF"
    val grey = "#B7BFC7"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val extras = intent.extras ?: return
        color = extras.getString("color")
        if(color == white)switchWhite.isChecked = true
        else switchGray.isChecked = true
        switchGray.setOnClickListener{
            if(switchWhite.isChecked){
                switchWhite.isChecked = false
            }
            switchGray.isChecked = true
            color = grey
        }

        switchWhite.setOnClickListener{
            if(switchGray.isChecked){
                switchGray.isChecked = false
                switchGray.invalidate()
            }
            switchWhite.isChecked = true
            color = white
        }
    }

    override fun finish() {
        val data = Intent()
        data.putExtra("color", color)
        setResult(Activity.RESULT_OK, data)
        super.finish()
    }
}
