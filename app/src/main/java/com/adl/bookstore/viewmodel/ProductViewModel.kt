package com.adl.bookstore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adl.bookstore.repo.ProductRepo
import com.adl.bookstore.repo.ProductResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(val productRepo: ProductRepo): ViewModel() {
    val productStateFlow = MutableStateFlow<ProductResponse?>(null)

    init {
        viewModelScope.launch {
            productRepo.getProductDetails().collect(){
                productStateFlow.value = it
            }
        }
    }

    fun getProductInfo()= productRepo.getProductDetails()

}