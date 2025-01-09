package com.note.compose.APICall1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.note.compose.APICall1.adapter.TodoAdapter
import com.note.compose.APICall1.utils.Result
import com.note.compose.APICall1.viewModel.TodoViewModel
import com.note.compose.R

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: TodoViewModel

    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

//        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[TodoViewModel::class.java]
        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        todoAdapter = TodoAdapter(listOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = todoAdapter
        progressBar.visibility=View.GONE
        viewModel.todos.observe(this) { todos ->
           Log.d("MyTesting","APiCall Success....")
        }

        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
        viewModel.localTodos.observe(this){localTodos->
            todoAdapter.updateData(localTodos)
        }

        viewModel.performApiCallIfNeeded()
    }
}