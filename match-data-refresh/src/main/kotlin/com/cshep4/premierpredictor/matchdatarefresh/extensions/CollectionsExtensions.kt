package com.cshep4.premierpredictor.matchdatarefresh.extensions

inline fun <E : Any, T : Collection<E>> T?.whenNullOrEmpty(func: () -> Unit): T? {
    if (this == null || this.isEmpty()) {
        func()
    }

    return this
}

inline fun <E : Any, T : Collection<E>> T?.otherwise(func: (T : Collection<E>?) -> Unit) {
    func(this)
}

inline fun <E : Any, T : Collection<E>> T?.whenNotNullNorEmpty(func: (T : Collection<E>) -> Unit) {
    if (this != null && this.isNotEmpty()) {
        func(this)
    }
}