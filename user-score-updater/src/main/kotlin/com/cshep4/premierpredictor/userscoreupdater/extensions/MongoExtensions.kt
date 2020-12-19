package com.cshep4.premierpredictor.userscoreupdater.extensions

import org.bson.types.ObjectId
import org.bson.types.ObjectId.isValid

fun String.toObjectId(): ObjectId {
    if (!isValid(this)) {
        throw IllegalArgumentException("Invalid id")
    }

    return ObjectId(this)
}

