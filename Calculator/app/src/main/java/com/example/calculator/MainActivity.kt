package com.example.calculator


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot = false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    fun onClearClick(view: View){

        binding.inputTv.text = ""
        binding.resultTv.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        binding.resultTv.visibility = View.GONE
        binding.inputTv.text = "0"

    }


    fun onEqualClick(view: View){

        Calculate()
        binding.inputTv.text = binding.resultTv.text.toString().drop(1)

    }


    fun onDigitClick(view: View){

        if (stateError){

            binding.inputTv.text = (view as Button).text
            stateError = false
        }
        else{

            binding.inputTv.append((view as Button).text)

        }

        lastNumeric = true
        Calculate()

    }



    fun onOperatorClick(view: View){

        if (!stateError && lastNumeric){

            binding.inputTv.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            Calculate()

        }

    }


    fun onBackClick(view: View){

        binding.inputTv.text = binding.inputTv.text.toString().dropLast(1)

        try {
            val lastChar = binding.inputTv.text.toString().last()

            if (lastChar.isDigit()){
                Calculate()
            }
        }
        catch (e : Exception){

            binding.resultTv.text = ""
            binding.resultTv.visibility = View.GONE
            Log.e("Last char error", e.toString())

        }
    }


    fun Calculate(){

        if(lastNumeric && !stateError){

            val txt = binding.inputTv.text.toString()

            expression = ExpressionBuilder(txt).build()

            try {

                val result = expression.evaluate()

                binding.resultTv.visibility = View.VISIBLE

                binding.resultTv.text = "= " + result.toString()
            }

            catch (ex : ArithmeticException){

                Log.e("Evaluate Error", ex.toString())
                binding.resultTv.text = "NaN"
                stateError = true
                lastNumeric = false

            }
        }
    }
}