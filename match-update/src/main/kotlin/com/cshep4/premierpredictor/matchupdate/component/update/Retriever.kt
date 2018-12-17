package com.cshep4.premierpredictor.matchupdate.component.update

interface Retriever<T> {
    fun getLatest(id: String): T?
}