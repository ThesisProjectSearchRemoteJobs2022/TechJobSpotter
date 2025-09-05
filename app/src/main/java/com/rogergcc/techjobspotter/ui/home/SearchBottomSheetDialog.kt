package com.rogergcc.techjobspotter.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rogergcc.techjobspotter.R
import com.rogergcc.techjobspotter.databinding.FragmentSearchBottomSheetDialogBinding


class SearchBottomSheetDialog(
    private val onSearchQuery: (String) -> Unit) : BottomSheetDialogFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentSearchBottomSheetDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_search_bottom_sheet_dialog, container, false)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.searchButton.setOnClickListener {
//            val query = binding.searchInput.text.toString().trim()
//            if (query.isNotEmpty()) {
//                onSearchQuery(query) // Llamada directa
//                dismiss()
//            }
//        }

        binding.searchButton.setOnTouchListener { v, event ->
            val query = binding.searchInput.text.toString().trim()
            if (query.isNotEmpty()) {
                onSearchQuery(query) // Llamada directa
                dismiss()
            }
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate()
                        .scaleX(0.9f)
                        .scaleY(0.9f)
                        .setDuration(120)
                        .start()
                }
                MotionEvent.ACTION_UP -> {
                    // vuelve a tamaño normal
                    v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(120)
                        .start()

                    // informa al sistema que hubo click (accesibilidad)
                    v.performClick()
                }
                MotionEvent.ACTION_CANCEL -> {
                    v.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
                }
            }
            true // indicamos que manejamos el touch completo
        }


        // Buscar al presionar enter en el teclado
        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchInput.text.toString().trim()
                if (query.isNotEmpty()) {
                    onSearchQuery(query)
                    dismiss()
                }
                return@setOnEditorActionListener true
            }
            false
        }
    }
    // Y en el HomeFragment:
//    private fun showSearchBottomSheet() {
//        val bottomSheet = SearchBottomSheetDialog { query ->
//            viewModel.filterJobs(query)
//        }
//        bottomSheet.show(parentFragmentManager, "SearchBottomSheet")
//    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {


    }
}