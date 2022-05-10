package com.adl.bookstore.repo

import android.util.Log
import com.adl.bookstore.data.BookModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class BookRepo {
    private val firestore = FirebaseFirestore.getInstance()


    fun getBookDetails() = callbackFlow<BookResponse> {

        val collection = firestore.collection("book")
        val snapshotListener = collection.addSnapshotListener{value,error ->
            run {
                Log.d("Error", error.toString())
                val response:BookResponse
                if (error == null) {
                    response=OnSuccess(value)
                } else {
                    response=OnFailure(error)
                }

                trySend(response).isSuccess
            }


        }
        awaitClose{
            snapshotListener.remove()
        }

    }
}