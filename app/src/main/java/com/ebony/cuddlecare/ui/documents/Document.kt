package com.ebony.cuddlecare.ui.documents

import com.google.firebase.firestore.CollectionReference

enum class Document {
    Profile,
    Baby,
    Invite,
    BreastFeeding,
    BottleFeeding,
    Sleeping,
    DiaperCount,
    Diaper,
    Medicine,
    Vaccination
}

fun activeBabyCollection(collection: CollectionReference, activeBaby: Baby): CollectionReference {
    return collection.document(activeBaby.id).collection(activeBaby.id)
}