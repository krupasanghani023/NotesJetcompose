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
import com.note.compose.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: TodoViewModel

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var binding:ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        todoAdapter = TodoAdapter(listOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = todoAdapter
        binding.progressBar.visibility=View.VISIBLE
        viewModel.todos.observe(this) { todos ->
           Log.d("MyTesting","APiCall Success....")
            todoAdapter.updateData(todos)
            binding.progressBar.visibility=View.GONE
        }

        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            Log.d("MyTesting","Error:--${error.toString()}")
            binding.progressBar.visibility=View.GONE
        }


        viewModel.performApiCallIfNeeded()
    }
}