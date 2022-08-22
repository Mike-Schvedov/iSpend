package com.mikeschvedov.ispend.ui.home

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mikeschvedov.ispend.R
import com.mikeschvedov.ispend.data.database.entities.Expense
import com.mikeschvedov.ispend.databinding.FragmentHomeBinding
import com.mikeschvedov.ispend.utils.Category
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.addNewExpenseFab.setOnClickListener {
            createDialog()
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createDialog(){
        // Creating a dialog
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

        var selectedCategory = ""
        // When we select a category
        inputLayout.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, position, p3 ->
                // Store the category selected
                selectedCategory = categoryArray[position]
                       }

        // We want to hide the keyboard when clicking on the drop down menu
        inputLayout.setOnFocusChangeListener { v, hasFocus ->
         if (v != null){
             val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
             imm.hideSoftInputFromWindow(v.windowToken, 0)
         }
        }

        // Getting the current dates to set in DatePicker
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val currentHour = calendar[Calendar.HOUR]
        val currentYear = calendar[Calendar.YEAR]
        val currentMonth = calendar[Calendar.MONTH] + 1
        val currentDay = calendar[Calendar.DAY_OF_MONTH]

        val datePicker = dialog.findViewById<DatePicker>(R.id.datePicker)

        datePicker.updateDate(currentYear, currentMonth, currentDay)

        val saveBTN = dialog.findViewById<TextView>(R.id.save_button)
        saveBTN.setOnClickListener {

            val description = dialog.findViewById<EditText>(R.id.description_edittext).text.toString()
            val amount = dialog.findViewById<EditText>(R.id.amount_edittext).text.toString()
            // Getting the category as an Enum
            val categoryEnum = getCategoryEnum(selectedCategory)

            val pickerDay = datePicker.dayOfMonth
            val pickerMonth = datePicker.month
            val pickerYear = datePicker.year

            println("$currentHour | $currentDay | $currentMonth | $currentYear")

            if(!description.isNullOrBlank() && !amount.isNullOrEmpty() && categoryEnum != Category.ERROR){
                homeViewModel.saveNewExpenseInDB(Expense(
                    description = description,
                    amountSpent = amount.toInt(),
                    category = categoryEnum,
                    hour = currentHour,
                    day = pickerDay,
                    month = pickerMonth,
                    year = pickerYear
                ))

                dialog.dismiss()
            }else{
                showAlertDialog()
            }

        }

        val cancelBTN = dialog.findViewById<TextView>(R.id.cancel_button)
        cancelBTN.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun getCategoryEnum(selectedCategory: String): Category {
        when(selectedCategory){
        "מזון - סופרמרקט" -> return Category.FOOD_NORMAL
        "מזון - אוכל מהיר/שתייה" -> return Category.FOOD_JUNK
        "אירועים" -> return Category.EVENTS
        "דלק" -> return Category.FUEL
        "שכר דירה" -> return Category.RENT
        "תחזוקת רכב" -> return Category.CAR_MAINTENANCE
        "ניקיון/טואלטיקה" -> return Category.SHOPPING_CLEANING
        "בגדים" -> return Category.SHOPPING_CLOTHING
        "קניות - מאי" -> return Category.SHOPPING_MAY
        "קניות - שונות" -> return Category.SHOPPING_OTHER
        "תחבורה ציבורית" -> return Category.TRANSPORTATION
        else -> return Category.ERROR
        }
    }

    private fun showAlertDialog() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(requireContext())

        // set message of alert dialog
        dialogBuilder.setMessage("אין להשאיר שדות רקים!")
            .setCancelable(false)
            .setNegativeButton("בסדר", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("אזהרה")
        // show alert dialog
        alert.show()
    }


}