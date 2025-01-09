package com.note.compose.APICall1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.note.compose.APICall1.localData.TodoLocalData
import com.note.compose.R

class TodoAdapter(private var todos: List<TodoLocalData>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todos[position]
        holder.bind(todo)
    }

    override fun getItemCount(): Int = todos.size
    fun updateData(newTodos: List<TodoLocalData>) {
        this.todos = newTodos
        notifyDataSetChanged()
    }
    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val todoTitle: TextView = itemView.findViewById(R.id.todoTitle)
        private val todoStatus: CheckBox = itemView.findViewById(R.id.todoStatus)

        fun bind(todo: TodoLocalData) {
            // Set the values for each view manually
            todoTitle.text = todo.title
            todoStatus.isChecked = todo.isCompleted
        }
    }

}