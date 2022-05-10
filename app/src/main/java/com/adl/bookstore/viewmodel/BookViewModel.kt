package com.adl.bookstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adl.bookstore.repo.BookRepo
import com.adl.bookstore.repo.BookResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BookViewModel(val bookRepo: BookRepo):ViewModel() {
    val bookStateFlow = MutableStateFlow<BookResponse?>(null)

    init {
        viewModelScope.launch {
            bookRepo.getBookDetails().collect(){
                bookStateFlow.value = it
            }
        }
    }

    fun getBookInfo()= bookRepo.getBookDetails()

}