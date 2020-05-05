package com.cshep4.premierpredictor.userscoreupdater.extensions

inline fun <E : Any, T : Collection<E>> T?.whenNullOrEmpty(func: () -> Unit): T? {
    if (this == null || this.isEmpty()) {
        func()
    }

    return this
}

