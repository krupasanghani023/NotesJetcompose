package com.note.compose.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.compose.dataModels.User
import com.note.compose.repository.UserRepository
import com.note.compose.util.ResultState
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModel @Inject constructor (private val userRepository: UserRepository) : ViewModel() {
    private val _loginState = mutableStateOf<ResultState<User>>(ResultState.Initial)
    val loginState: State<ResultState<User>> get() = _loginState
    private val _userState = mutableStateOf<ResultState<User>>(ResultState.Initial)
    val userState: State<ResultState<User>> get() = _userState

    fun addUser(user: User) {
        viewModelScope.launch {
            _userState.value = ResultState.Loading
            try {
                 userRepository.addUser(user)
                _userState.value = ResultState.Success(user)
            } catch (e: Exception) {
                _userState.value = ResultState.Error("Failed to add user: ${e.localizedMessage}")
            }
        }
    }


    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = ResultState.Loading
            try {
                val user = userRepository.loginUser(email, password)
                if (user != null) {
                    _loginState.value = ResultState.Success(user)
                } else {
                    _loginState.value = ResultState.Error("Invalid email or password")
                }
            } catch (e: Exception) {
                _loginState.value = ResultState.Error("An error occurred: ${e.localizedMessage}")
            }
        }
    }
}