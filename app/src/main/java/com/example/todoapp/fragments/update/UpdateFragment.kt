package com.example.todoapp.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.data.TodoViewModel
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.viewmodel.SharedViewModel
import com.example.todoapp.databinding.FragmentUpdateBinding


class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding
    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val mViewModel: TodoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        binding?.currentTitleEt?.setText(args.currentItem.title)
        binding?.currentDescriptionEt?.setText(args.currentItem.description)
        binding?.currentPrioritiesSpinner?.setSelection(sharedViewModel.parsePriorityToInt(args.currentItem.priority))
        binding?.currentPrioritiesSpinner?.onItemSelectedListener = sharedViewModel.listener
        return binding?.root
    }

    //inflate menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    //do something when menu item is selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun updateItem() {
        val title = binding?.currentTitleEt?.text.toString()
        val description = binding?.currentDescriptionEt?.text.toString()
        val getPriority = binding?.currentPrioritiesSpinner?.selectedItem.toString()

        val validation = sharedViewModel.verifyDataFromUser(title, description)

        if (validation) {
            //update current item
            val updateData = ToDoData(
                args.currentItem.id,
                title,
                sharedViewModel.parsePriority(getPriority),
                description
            )
            mViewModel.updateData(updateData)
            Toast.makeText(requireContext(), "Update successful", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill all details", Toast.LENGTH_SHORT).show()
        }
    }

    //alert dialog to confirm removal
    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mViewModel.deleteData(args.currentItem)
            Toast.makeText(
                requireContext(),
                "Successfully Removed : '${args.currentItem.title}'",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete '${args.currentItem.title}'?")
        builder.setMessage("Are you sure you want to delete '${args.currentItem.title}'?")
        builder.create().show()
    }
}