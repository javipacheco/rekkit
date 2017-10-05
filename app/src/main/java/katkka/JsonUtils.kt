package katkka

import kategory.ListKW
import org.json.JSONArray

fun <T> JSONArray.toListKW(): ListKW<T>
        = ListKW((0 until length()).asSequence().map { get(it) as T }.toList())