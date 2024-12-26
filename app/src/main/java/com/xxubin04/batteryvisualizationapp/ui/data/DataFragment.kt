package com.xxubin04.batteryvisualizationapp.ui.data

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.xxubin04.batteryvisualizationapp.databinding.FragmentDataBinding
import com.xxubin04.batteryvisualizationapp.ui.home.HomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class DataFragment : Fragment() {

    private var _binding: FragmentDataBinding? = null
    private val binding get() = _binding!!
    private val updateInterval: Long = 100
    private var updateJob: Job? = null
    private var elapsedTime: Int = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataViewModel =
            ViewModelProvider(this).get(DataViewModel::class.java)

        _binding = FragmentDataBinding.inflate(inflater, container, false)
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

//        val textView: TextView = binding.textData
//        dataViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        startDataUpdates()

        return root
    }

    private fun startDataUpdates() {
        val tableLayout: TableLayout = binding.batteryDataTable
        updateJob = CoroutineScope(Dispatchers.IO).launch {
            while (elapsedTime <= 1000) {
                fetchAndUpdateData(tableLayout)
                delay(updateInterval)
                elapsedTime += 100
            }
        }
    }

    private suspend fun fetchAndUpdateData(tableLayout: TableLayout) {
//        CoroutineScope(Dispatchers.IO).launch {
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
                    val soc = batteryData.getInt("soc")
                    val temperature = batteryData.getInt("temperature")

                    withContext(Dispatchers.Main) {
                        val newRow = TableRow(requireContext())
                        newRow.layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )

                        val timeCell = createTextView("$elapsedTime")
                        val currentCell = createTextView("$current")
                        val voltageCell = createTextView("$voltage")
                        val socCell = createTextView("$soc")
                        val temperatureCell = createTextView("$temperature")

                        newRow.addView(timeCell)
                        newRow.addView(currentCell)
                        newRow.addView(voltageCell)
                        newRow.addView(socCell)
                        newRow.addView(temperatureCell)

                        tableLayout.addView(newRow)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                }
            }
    }

    private fun createTextView(text: String): TextView {
        val textView = TextView(requireContext())
        textView.text = text
        textView.textSize = 13f
        textView.setTextColor(resources.getColor(android.R.color.black))
        textView.gravity = View.TEXT_ALIGNMENT_CENTER
        textView.setPadding(8, 8, 8, 8)
        return textView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}