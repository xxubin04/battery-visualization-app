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
import com.xxubin04.batteryvisualizationapp.databinding.FragmentGraphBinding
import com.xxubin04.batteryvisualizationapp.ui.home.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GraphFragment : Fragment() {

    private var _binding: FragmentGraphBinding? = null
    private val binding get() = _binding!!

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

        val textView: TextView = binding.textGraph
        graphViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}