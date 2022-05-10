package com.adl.bookstore.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class ProductRepo {
    private val firestore = FirebaseFirestore.getInstance()


    fun getProductDetails() = callbackFlow<ProductResponse> {

        val collection = firestore.collection("product")
        val snapshotListener = collection.addSnapshotListener{value,error ->
            run {
                Log.d("Error", error.toString())
                val response:ProductResponse
                if (error == null) {
                    response=OnSuccessProduct(value)
                } else {
                    response=OnFailureProduct(error)
                }

                trySend(response).isSuccess
            }


        }
        awaitClose{
            snapshotListener.remove()
        }

    }
}