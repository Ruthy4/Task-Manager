package com.example.todoapp

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.databinding.RowLayoutBinding
import com.example.todoapp.fragments.list.ListFragmentDirections

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {


    var dataList = emptyList<ToDoData>()

    class ViewHolder(val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.titleText.text = dataList[position].title
        holder.binding.descriptionTxt.text = dataList[position].description
        holder.binding.rowBackground.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
            holder.itemView.findNavController().navigate(action)
        }


        when (dataList[position].priority) {
            Priority.HIGH -> holder.binding.priorityIndicator.background.setColorFilter(
                Color.parseColor(
                    "#FF0000"
                ), PorterDuff.Mode.SRC_ATOP
            )
            Priority.MEDIUM -> holder.binding.priorityIndicator.background.setColorFilter(
                Color.parseColor(
                    "#FFC114"
                ), PorterDuff.Mode.SRC_ATOP
            )
            Priority.LOW -> holder.binding.priorityIndicator.background.setColorFilter(
                Color.parseColor(
                    "#00C980"
                ), PorterDuff.Mode.SRC_ATOP
            )
        }
    }

    override fun getItemCount() = dataList.size


    fun setData(todoData: List<ToDoData>) {
        this.dataList = todoData
        notifyDataSetChanged()
    }
}