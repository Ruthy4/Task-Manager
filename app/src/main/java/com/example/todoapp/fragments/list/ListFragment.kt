package com.example.todoapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.ListAdapter
import com.example.todoapp.R
import com.example.todoapp.data.TodoViewModel
import com.example.todoapp.data.viewmodel.SharedViewModel
import com.example.todoapp.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private var _binding:FragmentListBinding? = null
    private val binding get() = _binding
    private val todoViewModel: TodoViewModel by viewModels()
    val sharedViewModel: SharedViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding?.floatingActionButton?.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        setHasOptionsMenu(true)
        val recyclerView = binding?.recyclerView
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        todoViewModel.getAllData.observe(viewLifecycleOwner, {
            sharedViewModel.checkIfDatabaseEmpty(it)
            adapter.setData(it)
        })

        sharedViewModel.emptyDatabase.observe(viewLifecycleOwner, {
            showEmptyDatabaseViews(it)
        })


        return binding?.root
    }

    //handle visibilities of view when recycler view contains data or not
    private fun showEmptyDatabaseViews(emptyDatabase: Boolean) {
        if (emptyDatabase) {
            binding?.noDataImageView?.visibility = View.VISIBLE
            binding?.noDataTextView?.visibility = View.VISIBLE
        } else {
            binding?.noDataImageView?.visibility = View.INVISIBLE
            binding?.noDataTextView?.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_delete_all -> confirmRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    //alert dialog to confirm removal of all items
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            todoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully Removed Everything",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete EveryThing?")
        builder.setMessage("Are you sure you want to delete everything?")
        builder.create().show()
    }

}