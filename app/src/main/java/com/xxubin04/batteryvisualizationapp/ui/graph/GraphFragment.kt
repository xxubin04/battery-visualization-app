package com.xxubin04.batteryvisualizationapp.ui.graph

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.xxubin04.batteryvisualizationapp.R
import com.xxubin04.batteryvisualizationapp.databinding.FragmentGraphBinding
import com.xxubin04.batteryvisualizationapp.ui.home.HomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.jsoup.Jsoup
import java.net.HttpURLConnection
import java.net.URL

class GraphFragment : Fragment() {

    private var _binding: FragmentGraphBinding? = null
    private val binding get() = _binding!!
    private val updateInterval: Long = 100
    private var updateJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val graphViewModel =
            ViewModelProvider(this).get(GraphViewModel::class.java)

        _binding = FragmentGraphBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val toolbar: Toolbar = binding.toolbar
//        toolbar.setNavigationIcon(com.xxubin04.batteryvisualizationapp.R.drawable.ic_arrow_back)
//        toolbar.setNavigationOnClickListener {
//            lifecycleScope.launch(Dispatchers.IO) {
//                val intent = Intent(requireContext(), HomeActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
//                withContext(Dispatchers.Main) {
//                    startActivity(intent)
//                    requireActivity().finish()
//                }
//            }
//        }

        fetchAndUpdateSOH()

        startDataUpdates()

        val textView: TextView = binding.textGraph
        graphViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    private fun fetchAndUpdateSOH() {
        val button3Subtitle: TextView = binding.button3Subtitle
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("http://192.168.185.80:8080/data")  // IP 주소 다시
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val responseCode = connection.responseCode
                if (responseCode == 200) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)

                    val batteryState = jsonResponse.getJSONObject("warnings").getBoolean("overTemp")

                    withContext(Dispatchers.Main) {
                        if (batteryState) {
                            button3Subtitle.text = "적정 온도 초과"
                        }
                        else {
                            button3Subtitle.text = "적정 온도"
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        button3Subtitle.text = "데이터를 가져올 수 없음"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    button3Subtitle.text = "연결 실패: ${e.message}"
                }
            }
        }
    }

    private fun startDataUpdates() {
        updateJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                fetchAndUpdateData()
                delay(updateInterval)
            }
        }
    }

    private fun fetchAndUpdateData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("http://192.168.185.80:8080/data")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()


                val responseCode = connection.responseCode
                if (responseCode == 200) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)

                    val batteryData = jsonResponse.getJSONObject("battery")
                    val current = batteryData.getDouble("current")
                    val voltage = batteryData.getDouble("voltage")
                    val temperature = batteryData.getDouble("temp")
                    val chargeAmount = batteryData.getDouble("soc")

                    withContext(Dispatchers.Main) {
                        binding?.let {
                            it.current.text = "$current A"
                            it.voltage.text = "$voltage V"
                            it.temperature.text = "$temperature °C"
                            it.chargeAmount.text = "$chargeAmount %"
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        binding.current.text = "-"
                        binding.voltage.text = "-"
                        binding.temperature.text = "-"
                        binding.chargeAmount.text = "-"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.current.text = "전류: 오류"
                    binding.voltage.text = "전압: 오류"
                    binding.temperature.text = "온도: 오류"
                    binding.chargeAmount.text = "충전량: 오류"
                }
            }
        }
    }

    private fun fetchAndExtractJsonFromHtml() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // URL에서 HTML 문서 가져오기
                val url = "http://192.168.185.18"
                val document = Jsoup.connect(url).get()

                // JSON 데이터가 포함된 특정 태그를 선택
                // 예: <script id="json-data">{"battery":{...}}</script>
                val jsonDataElement = document.select("pre#data").first()

                if (jsonDataElement != null) {
                    val jsonText = jsonDataElement.text() // 태그 내부의 JSON 텍스트

                    // JSON 객체로 변환
                    val jsonObject = JSONObject(jsonText)

                    // JSON 데이터 추출
                    val batteryObject = jsonObject.getJSONObject("battery")
                    val current = batteryObject.getDouble("current")
                    val voltage = batteryObject.getDouble("voltage")
                    val temperature = batteryObject.getInt("temperature")
                    val chargeAmount = batteryObject.getInt("soc")

                    // UI 업데이트
                    withContext(Dispatchers.Main) {
                        updateUI(current, voltage, temperature, chargeAmount)
                    }
                } else {
                    // JSON 데이터가 없을 경우
                    withContext(Dispatchers.Main) {
                        println("JSON 데이터를 찾을 수 없습니다.")
                    }
                }
            } catch (e: Exception) {
                // 오류 처리
                withContext(Dispatchers.Main) {
                    println("오류 발생: ${e.message}")
                }
            }
        }
    }

    private fun updateUI(current: Double, voltage: Double, temperature: Int, chargeAmount: Int) {
        binding.current.text = "전류: $current A"
        binding.voltage.text = "전압: $voltage V"
        binding.temperature.text = "온도: $temperature °C"
        binding.chargeAmount.text = "충전량: $chargeAmount %"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        updateJob?.cancel()
        _binding = null
    }
}