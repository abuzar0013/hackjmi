package com.example.agriculus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter

import androidx.navigation.fragment.findNavController
import com.example.agriculus.data.models.CurrentWeather
import com.example.agriculus.databinding.CropPredictionBinding
import com.example.agriculus.utils.RetrofitInstance
import com.example.agriculus.R
import com.example.agriculus.ml.ConvertedModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class CropPrediction : BaseFragment() {
    companion object{
        lateinit var cropName : String
    }
    lateinit var state: String
    private val binding by lazy { CropPredictionBinding.inflate(layoutInflater) }


    private val outputIndexes = arrayListOf(
        "apple",
        "banana",
        "blackgram",
        "chickpea",
        "coconut",
        "coffee",
        "cotton",
        "grapes",
        "jute",
        "kidneybeans",
        "lentil",
        "maize",
        "mango",
        "mothbeans",
        "mungbean",
        "muskmelon",
        "orange",
        "papaya",
        "pigeonpeas",
        "pomegranate",
        "rice",
        "watermelon "
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationVisibility(false)
        prepareStatesSpinner()
        binding.btnPredict.setOnClickListener {
            getStateWeather(state)
        }
    }

    private fun prepareStatesSpinner() {
        val indianStates = arrayListOf<String>("Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh","Delhi" , "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal")
        val adapter =
            ArrayAdapter(
                requireContext(),
               androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                indianStates
            )

        binding.spStates.adapter = adapter

        binding.spStates.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                  state = indianStates[position]

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

               }
    }


    private fun createByteBufferAndPredict(data: CurrentWeather) {

        if (binding.etNitrogen.text.toString().trim().isEmpty() ||
            binding.etPH.text.toString().trim().isEmpty() ||
            binding.etPhosphorus.text.toString().trim().isEmpty() ||
            binding.etRainfall.text.toString().trim().isEmpty() ||
            binding.etPotassium.text.toString().trim().isEmpty()
        ) {
            showToast("Please enter all values")
            return
        }

        val nitrogen = binding.etNitrogen.text.toString().toFloat()
        val phosphorus = binding.etPhosphorus.text.toString().toFloat()
        val potassium = binding.etPotassium.text.toString().toFloat()
        val temperature = data.main.temp.toFloat()
        val humidity = data.main.humidity.toFloat()
        val ph = binding.etPH.text.toString().toFloat()
        val rainfall = binding.etRainfall.text.toString().toFloat()

        val inputData =
            floatArrayOf(nitrogen, phosphorus, potassium, temperature, humidity, ph, rainfall)

        val numBytes = inputData.size * java.lang.Float.SIZE / java.lang.Byte.SIZE

        val byteBuffer = ByteBuffer.allocateDirect(numBytes).order(ByteOrder.nativeOrder())

        for (value in inputData) {
            byteBuffer.putFloat(value)
        }

        byteBuffer.rewind()

        val model = ConvertedModel.newInstance(requireContext())

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 7), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        model.close()

        val output = outputFeature0.floatArray

        val finalPrediction = output.indexOfMax()
        showToast(finalPrediction.toString())

        if (finalPrediction == null) {
            showToast("Something Went Wrong!!")
        } else {
            val crop = try {
                outputIndexes[finalPrediction]
            } catch (e: Exception) {
                "Index Out of Bounds"
            }
            cropName = crop
            showToast(crop)
            findNavController().navigate(R.id.action_camFragment2_to_resultScreen2)

        }
    }


    private fun getStateWeather(state: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = try {
                RetrofitInstance.api.getCurrentWeather(
                    state,
                    "in",
                    "metric",
                    "d1d76e6f74c25f60b602ac34a75cc0e5"
                )

            } catch (e: IOException) {
                showToast("app error ${e.message}")
                return@launch
            } catch (e: IOException) {
                showToast("http error ${e.message}")
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {

                    val data = response.body()!!

                    createByteBufferAndPredict(data)
                }
            }

        }
    }
    private fun FloatArray.indexOfMax(): Int? {
        if (isEmpty()) {
            return null
        }

        var maxIndex = 0
        var maxValue = this[0]

        for (i in 1 until size) {
            if (this[i] > maxValue) {
                maxIndex = i
                maxValue = this[i]
            }
        }

        return maxIndex
    }
}