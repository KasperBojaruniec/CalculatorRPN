package com.bignerdranch.android.calculator

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var color = "#FFFFFF"
    val REQ_CODE = 1000
    val NUMBERS_STATE: String = "1000"
    private var numbers: Stack<String> = Stack<String>()
    private var numbersSave: Stack<String> = Stack<String>()
    private var tmp: String = ""
    private var tmp2: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null) {
            numbers.clear()
            numbers.addAll(savedInstanceState.getStringArrayList(NUMBERS_STATE))
        }
        setContentView(R.layout.activity_main)
        val buttons = ArrayList<Button>(mutableListOf(button0, button1, button2, button3, button4, button5, button6, button7, button8, button9))
        var adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, numbers)
        listViewOutput.adapter = adapter
        if(numbers.isEmpty()) {
            numbers.add("0")
            adapter.notifyDataSetChanged()
        }
        buttons.forEach{
            var textTmp = it.text
            it.setOnClickListener {
                if(numbers.peek() == "0" || numbers.peek() == "-0") {
                    numbers.pop()
                    numbers.push(textTmp.toString())
                }
                else{
                    numbers.push(numbers.pop().plus(textTmp))
                }
                adapter.notifyDataSetChanged()
            }
        }
        buttonDot.setOnClickListener{
            if(!numbers.peek().contains(".")) {
                tmp = numbers.push(numbers.pop().plus("."))
                adapter.notifyDataSetChanged()
            }
        }
        buttonPlusMinus.setOnClickListener{
            if(numbers.peek()[0] == '-') {
                numbers.push( numbers.pop().removePrefix("-"))
            }
            else {
                numbers.push("-".plus(numbers.pop()))
            }
            adapter.notifyDataSetChanged()
        }
        buttonPlus.setOnClickListener{
            if(numbers.size > 1) {
                saveNumbers()
                numbers.push((numbers.pop().toDouble() + numbers.pop().toDouble()).toString())
            }
            adapter.notifyDataSetChanged()
        }
        buttonMinus.setOnClickListener{
            if(numbers.size > 1){
                saveNumbers()
                numbers.push((numbers.pop().toDouble() - numbers.pop().toDouble()).toString())
            }
            adapter.notifyDataSetChanged()
        }
        buttonMulti.setOnClickListener{
            if(numbers.size > 1){
                saveNumbers()
                numbers.push((numbers.pop().toDouble() * numbers.pop().toDouble()).toString())
            }
            adapter.notifyDataSetChanged()
        }
        buttonDiv.setOnClickListener{
            if(numbers.size > 1) {
                tmp = numbers.pop()
                tmp2 = numbers.pop()
                if (!(tmp2 == "0" || tmp2 == "0.0")) {
                    saveNumbers()
                    numbers.push((tmp.toDouble() / tmp2.toDouble()).toString())
                }
                else {
                    numbers.push(tmp2)
                    numbers.push(tmp)
                }
            }
            adapter.notifyDataSetChanged()
        }
        buttonRoot.setOnClickListener{
            saveNumbers()
            numbers.push(Math.pow(numbers.pop().toDouble(),0.5).toString())
            adapter.notifyDataSetChanged()
        }
        buttonPow.setOnClickListener{
            if(numbers.size > 1) {
                saveNumbers()
                numbers.push((Math.pow(numbers.pop().toDouble(), numbers.pop().toDouble())).toString())
            }
            adapter.notifyDataSetChanged()
        }
        buttonDel.setOnClickListener{
            numbers.push( numbers.pop().dropLast(1))
            if(numbers.peek().isBlank() || numbers.peek() == "-") {
                numbers.pop()
                numbers.push( "0")
            }
            adapter.notifyDataSetChanged()
        }
        buttonSwap.setOnClickListener{
            if(numbers.size > 1) {
                saveNumbers()
                tmp = numbers.pop()
                tmp2 = numbers.pop()
                numbers.push( tmp)
                numbers.push( tmp2)
                adapter.notifyDataSetChanged()
            }
        }
        buttonDrop.setOnClickListener{
            saveNumbers()
            if(numbers.size > 1) {
                numbers.pop()
            }
            else if(numbers.size == 1){
                numbers.set(0, "0")
            }
            adapter.notifyDataSetChanged()
        }
        buttonEnter.setOnClickListener{
            saveNumbers()
            if(numbers.peek().get(numbers.peek().length -1) == '.') numbers.push(numbers.pop().dropLast(1))
            numbers.push("0")
            adapter.notifyDataSetChanged()
        }

        buttonSettings.setOnClickListener{
            showActivity()
        }

        buttonClear.setOnClickListener{
            saveNumbers()
            numbers.pop()
            numbers.push("0")
            adapter.notifyDataSetChanged()
        }
        buttonUndo.setOnClickListener{
            if(numbersSave != null) {
                numbers.clear()
                numbers.addAll(numbersSave)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            putStringArrayList(NUMBERS_STATE, ArrayList(numbers))
        }
        super.onSaveInstanceState(outState)
    }

    fun saveNumbers(){
        numbersSave.clear()
        numbersSave.addAll(numbers)
    }

    fun showActivity(){
        val i = Intent(this, SettingActivity::class.java)
        i.putExtra("color", color)
        startActivityForResult(i, REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(data!=null){
            if(data.hasExtra("color")){
                color = data.extras.getString("color")
                Log.i("we", color.toString())
                listViewOutput.setBackgroundColor(Color.parseColor(color))
                constraintInput.setBackgroundColor(Color.parseColor(color))
            }
        }
    }
}
