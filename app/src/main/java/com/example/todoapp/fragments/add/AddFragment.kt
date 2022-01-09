package com.example.todoapp.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.data.TodoViewModel
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.viewmodel.SharedViewModel
import com.example.todoapp.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding
    private val mViewModel: TodoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        binding?.prioritiesSpinner?.onItemSelectedListener = sharedViewModel.listener
        return binding?.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add){
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val mTitle = binding?.titleEt?.text.toString()
        val mPriority = binding?.prioritiesSpinner?.selectedItem.toString()
        val mDescription = binding?.descriptionEt?.text.toString()

        val validation = sharedViewModel.verifyDataFromUser(mTitle, mDescription)

        if (validation) {
            val newData = ToDoData(
                0,
                mTitle,
                sharedViewModel.parsePriority(mPriority),
                mDescription
            )
            //insert data in database
            mViewModel.insertData(newData)
            Toast.makeText(requireContext(), "add successful", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill all details", Toast.LENGTH_SHORT).show()
        }
    }
}