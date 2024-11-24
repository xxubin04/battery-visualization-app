package com.xxubin04.batteryvisualizationapp.ui.tracking

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.xxubin04.batteryvisualizationapp.databinding.FragmentTrackingBinding
import com.xxubin04.batteryvisualizationapp.ui.home.HomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class TrackingFragment : Fragment() {

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!
    private val updateInterval: Long = 1000

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val goalViewModel =
            ViewModelProvider(this).get(TrackingViewModel::class.java)

        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val toolbar: Toolbar = binding.toolbar
        toolbar.setNavigationIcon(com.xxubin04.batteryvisualizationapp.R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val intent = Intent(requireContext(), HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                withContext(Dispatchers.Main) {
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }

        startDataUpdates()

//        val textView: TextView = binding.textTracking
//        goalViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    private fun startDataUpdates() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                fetchAndUpdateUI()
                delay(updateInterval)
            }
        }
    }

    private fun fetchAndUpdateUI() {
        val container4Subtitle: TextView = binding.container4Subtitle
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

                    val sohValue = jsonResponse.getJSONObject("battery").getString("soh")
//                    val batteryHealth =

                    val mode = jsonResponse.getJSONObject("system").getString("mode")
                    val modeText = when (mode) {
                        "Normal" -> "일반 모드"
                        "Turbo" -> "터보 모드"
                        "Strong" -> "강력 모드"
                        else -> "알 수 없음"
                    }

                    val warnings = jsonResponse.getJSONObject("warnings")
                    val overTemp = warnings.getBoolean("overTemp")
                    val tempText = if (overTemp) "적정 온도 초과" else "적정 온도"

                    withContext(Dispatchers.Main) {
                        binding?.let {
                            it.container1Subtitle.text = "${sohValue}%"
                            it.container5Subtitle.text = modeText
                            it.container4Subtitle.text = tempText
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        binding?.let {
                            it.container4Subtitle.text = "데이터를 가져올 수 없음"
                            it.container5Subtitle.text = "모드 확인 불가 "
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding?.let {
                        it.container4Subtitle.text = "연결 실패: ${e.message}"
                        it.container5Subtitle.text = "모드 확인 불가"
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}