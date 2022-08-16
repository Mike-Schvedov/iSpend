package com.mikeschvedov.ispend.ui.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mikeschvedov.ispend.R
import com.mikeschvedov.ispend.databinding.FragmentHomeBinding
import org.w3c.dom.Text

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.addExpenseFab.setOnClickListener {
            createDialog()
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createDialog(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.dialog_animation

        // Array of Categories
        val categoryArray = resources.getStringArray(R.array.categories)
        // Setting up the category adapter
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item, categoryArray)
        val inputLayout = dialog.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        inputLayout.setAdapter(arrayAdapter)

        var selectedCategory: String
        var subInputLayout = AutoCompleteTextView(requireContext())
        var subCategoryArray : Array<out String> = arrayOf()
        // When we select a category
        inputLayout.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                // Store the category selected
                selectedCategory = categoryArray[position]
                // Get sub-categories list based on selectedCategory
                subCategoryArray = getSubcategory(selectedCategory)
                // Setup the sub-category adapter
                val subCategoryArrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item, subCategoryArray)
                subInputLayout = dialog.findViewById(R.id.autoComplete_sub_category)
                subInputLayout.setAdapter(subCategoryArrayAdapter)
                 }

        // When we select a sub-category
        var selectedSubCategory: String
        subInputLayout.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                // Store the sub-category selected
                selectedSubCategory = subCategoryArray[position]
            };

       /* val button = dialog.findViewById<TextView>(R.id.okay_text)
        button.setOnClickListener {
            Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_LONG).show()
        }
        val cancelBTN = dialog.findViewById<TextView>(R.id.cancel_text)
        cancelBTN.setOnClickListener {
            dialog.dismiss()
        }*/

        dialog.show()
    }

    private fun getSubcategory(selectedCategory: String?): Array<out String> {

        // Getting the sub-category string array list according to the category
        when(selectedCategory){
            "מזון" -> return resources.getStringArray(R.array.food_subcategories)
            else -> return arrayOf()

        }

    }

}