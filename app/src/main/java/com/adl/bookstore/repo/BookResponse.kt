package com.adl.bookstore.repo

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot


sealed class BookResponse
data class OnSuccess(val querySnapshot: QuerySnapshot?):BookResponse()
data class OnFailure(val exeception: FirebaseFirestoreException?):BookResponse()