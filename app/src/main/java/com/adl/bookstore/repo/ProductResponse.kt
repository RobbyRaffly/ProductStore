package com.adl.bookstore.repo

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

sealed class ProductResponse
data class OnSuccessProduct(val querySnapshot: QuerySnapshot?):ProductResponse()
data class OnFailureProduct(val exeception: FirebaseFirestoreException?):ProductResponse()