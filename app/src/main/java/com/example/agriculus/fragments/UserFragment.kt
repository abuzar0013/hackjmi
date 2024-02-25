package com.example.agriculus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.agriculus.databinding.UserFragmentBinding
import com.example.agriculus.utils.RetrofitInstance
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class UserFragment : BaseFragment() {
    private val binding by lazy { UserFragmentBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationVisibility(true)
        getCurrentWeather()
    }

    private fun getCurrentWeather() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getCurrentWeather(
                    "new delhi",
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
//                  binding.tvTemp.text = "${response.body()!!.main.temp} °C"
//                   binding.tvFeelsLike.text = "${response.body()!!.main.feels_like}"
//                   binding.tvMaxTemp.text = "${response.body()!!.main.temp_max}"

                    val data = response.body()!!

                    val iconId = data.weather[0].icon
                    val imgurl = "https://openweathermap.org/img/w/$iconId.png"
                    Picasso.get().load(imgurl).into(binding.imgWeather)

                    binding.tvSunrise.text =
                        SimpleDateFormat(
                            "hh:mm a",
                            Locale.ENGLISH
                        ).format(data.sys.sunrise * 1000)

                    binding.tvSunset.text =
                        SimpleDateFormat(
                            "hh:mm a",
                            Locale.ENGLISH
                        ).format(data.sys.sunset * 1000)

                    binding.apply {
                        tvStatus.text = data.weather[0].description
                        tvWind.text = "${data.wind.speed} KM/H"
                        tvLocation.text = "${data.name}\n${data.sys.country}"
                        tvTemp.text = "${data.main.temp.toInt()} °C"
                        tvFeelsLike.text = "Feels like: ${data.main.feels_like.toInt()}°C"
                        tvMinTemp.text = "Min temp: ${data.main.temp_min.toInt()}°C"
                        tvMaxTemp.text = "Max temp: ${data.main.temp_max.toInt()}°C"
                        tvHumidity.text = "${data.main.humidity}%"
                        tvPressure.text = "${data.main.pressure}hPa"
                        tvUpdateTime.text = "Last Update${
                            SimpleDateFormat(
                                "hh:mm a",
                                Locale.ENGLISH
                            ).format(data.dt * 1000)
                        }"
                    }
                }
            }

        }
    }
}