package com.xxubin04.batteryvisualizationapp.ui.graph

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.xxubin04.batteryvisualizationapp.databinding.FragmentGraphBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.abs

class GraphFragment : Fragment() {

    private var lineChart: LineChart? = null
    private var _binding: FragmentGraphBinding? = null
    private val binding get() = _binding!!
    private val values = mutableListOf<Entry>()
    private var currentTime = 600f
    private val maxEntryCount = 8
    private var updateJob: Job? = null
    private val updateInterval: Long = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGraphBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lineChart = binding.currentGraph
        setupLineChart()
        startDataUpdates()
    }

    private fun setupLineChart() {
        lineChart?.apply {
            setBackgroundColor(Color.WHITE)
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                labelCount = maxEntryCount
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${value.toInt()}"
                    }
                }
                textColor = Color.BLACK
            }

            axisLeft.apply {
                setLabelCount(6, true)
                axisMinimum = 0f
                axisMaximum = 10f
                textColor = Color.BLACK
            }
            axisRight.isEnabled = false

            setExtraOffsets(8f, 16f, 16f, 16f)
        }
    }

    private fun startDataUpdates() {
        updateJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                fetchAndUpdateData()
                delay(updateInterval)
            }
        }
    }

    private suspend fun fetchAndUpdateData() {
        try {
            val url = URL("http://192.168.36.80:8080/data")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                Log.d("GraphFragment", "Raw Response: $response") // 응답 데이터 로그 추가
                val jsonResponse = JSONObject(response)

                val batteryData = jsonResponse.getJSONObject("battery")
                val current = abs(batteryData.getDouble("current").toFloat())
                Log.d("GraphFragment", "Current Value: $current") // 전류 값 로그 추가

                withContext(Dispatchers.Main) {
                    addDataToGraph(current)
                }
            } else {
                withContext(Dispatchers.Main) {
                    showErrorOnGraph("서버 오류: $responseCode")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                showErrorOnGraph("데이터를 가져오는데 실패했습니다.")
            }
        }
    }

    private fun addDataToGraph(current: Float) {
        values.add(Entry(currentTime, current))
        currentTime += 200

        if (values.size > maxEntryCount) {
            values.removeAt(0)
        }

        val dataSet = lineChart?.data?.getDataSetByIndex(0) as? LineDataSet
        if (dataSet != null) {
            dataSet.values = values
            lineChart?.data?.notifyDataChanged()
        } else {
            val newDataSet = LineDataSet(values, "전류 (A)").apply {
                color = Color.BLUE
                lineWidth = 2f
                setCircleColor(Color.RED)
                circleRadius = 4f
                setDrawCircleHole(false)
                valueTextSize = 10f
                valueTextColor = Color.BLACK
            }
            lineChart?.data = LineData(newDataSet)
        }

        lineChart?.notifyDataSetChanged()
        lineChart?.invalidate()
    }

    private fun showErrorOnGraph(message: String) {
        lineChart?.apply {
            clear()
            setNoDataText(message)
            setNoDataTextColor(Color.RED)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        updateJob?.cancel()
        _binding = null
    }
}
