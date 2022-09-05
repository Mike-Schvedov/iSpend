package com.mikeschvedov.ispend.ui.home

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
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
import androidx.recyclerview.widget.LinearLayoutManager
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

    private var currentHour: Int = 0
    private var currentYear: Int = 0
    private var currentMonth: Int = 0
    private var currentDay: Int= 0

    private lateinit var homeDatePicker: DatePicker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /* ViewModel */
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        /* Binding */
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /* Adapter */
        val adapter = homeViewModel.getRecyclerAdapter()
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter

        /* Initialize Main Date Picker */
        homeDatePicker = binding.datePickerHome

        /* onClickListeners */
        onClickListeners()

        /* Live Data Observers */
        observers()

        /* Other Methods */
        initializeDate()
        populateDatePickerAtStart()

        return root
    }

    private fun onClickListeners() {
        binding.searchButton.setOnClickListener{
            // Loading the expenses after picking another date
            populateRecyclerList()
        }
        binding.addNewExpenseFab.setOnClickListener {
            createDialog()
        }
    }

    private fun observers() {
        homeViewModel.isListEmpty.observe(viewLifecycleOwner){
            hideRecyclerView(it)
        }
    }

    private fun hideRecyclerView(b: Boolean) {
        if (b){
            println("making recycler invisible")
            binding.recyclerview.visibility = View.INVISIBLE
            binding.nothingToShowTextview.visibility = View.VISIBLE
        }else{
            println("making recycler visible")

            binding.recyclerview.visibility = View.VISIBLE
            binding.nothingToShowTextview.visibility = View.INVISIBLE
        }
    }

    private fun populateRecyclerList() {
        homeViewModel.populateRecyclerList(
            homeDatePicker.dayOfMonth,
            homeDatePicker.month + 1,
            homeDatePicker.year)
    }

    private fun populateDatePickerAtStart() {
        homeDatePicker.updateDate(currentYear, currentMonth, currentDay)
        // Loading the expenses with this date
        populateRecyclerList()
    }

    private fun initializeDate() {
        // Getting the current dates to set in DatePicker
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        currentHour = calendar[Calendar.HOUR]
        currentYear = calendar[Calendar.YEAR]
        currentMonth = calendar[Calendar.MONTH]
        currentDay = calendar[Calendar.DAY_OF_MONTH]
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

        val dialogDatePicker = dialog.findViewById<DatePicker>(R.id.datePicker)

        dialogDatePicker.updateDate(currentYear, currentMonth, currentDay)

        val saveBTN = dialog.findViewById<TextView>(R.id.save_button)
        saveBTN.setOnClickListener {

            val description = dialog.findViewById<EditText>(R.id.description_edittext).text.toString()
            val amount = dialog.findViewById<EditText>(R.id.amount_edittext).text.toString()
            // Getting the category as an Enum
            val categoryEnum = getCategoryEnum(selectedCategory)

            val pickerDay = dialogDatePicker.dayOfMonth
            val pickerMonth = dialogDatePicker.month + 1
            val pickerYear = dialogDatePicker.year

            println("$description | $amount | $categoryEnum")

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
                // Hiding the soft keyboard
                if (it != null){
                    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(it.windowToken, 0)
                }
                // Closing the dialog
                dialog.dismiss()
            }else{
                showAlertDialog()
            }
        }

        val cancelBTN = dialog.findViewById<TextView>(R.id.cancel_button)
        cancelBTN.setOnClickListener {
            // Hiding the soft keyboard
            if (it != null){
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }
            // Closing the dialog
            dialog.dismiss()
        }
        dialog.show()
    }

    fun getCategoryEnum(selectedCategory: String): Category {
        return when(selectedCategory){
            Category.FOOD_NORMAL.hebrew -> Category.FOOD_NORMAL
            Category.FOOD_JUNK.hebrew -> Category.FOOD_JUNK
            Category.EVENTS.hebrew -> Category.EVENTS
            Category.FUEL.hebrew -> Category.FUEL
            Category.RENT.hebrew -> Category.RENT
            Category.PARKING.hebrew -> Category.PARKING
            Category.PETS.hebrew -> Category.PETS
            Category.CAR_MAINTENANCE.hebrew -> Category.CAR_MAINTENANCE
            Category.SHOPPING_CLEANING.hebrew -> Category.SHOPPING_CLEANING
            Category.SHOPPING_CLOTHING.hebrew -> Category.SHOPPING_CLOTHING
            Category.SHOPPING_MAY.hebrew -> Category.SHOPPING_MAY
            Category.SHOPPING_OTHER.hebrew -> Category.SHOPPING_OTHER
            Category.TRANSPORTATION.hebrew -> Category.TRANSPORTATION
            Category.GAME.hebrew -> Category.GAME
            else -> Category.ERROR
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