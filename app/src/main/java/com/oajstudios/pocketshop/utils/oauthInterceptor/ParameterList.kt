package com.oajstudios.pocketshop.utils.oauthInterceptor

import android.util.Log

class ParameterList {
    private val params: ArrayList<Parameter>

    constructor() {
        params = ArrayList()
    }

    internal constructor(params: List<Parameter>) {
        this.params = ArrayList(params)
    }

    constructor(map: Map<String, String>) : this() {
        for (entry in map.entries) {
            params.add(Parameter(entry.key, entry.value))
        }
    }

    fun add(key: String, value: String) {
        params.add(Parameter(key, value))
    }


    fun asOauthBaseString(): String {
        return OAuthEncoder.encode(asFormUrlEncodedString())
    }

    fun asFormUrlEncodedString(): String {
        if (params.size == 0) return EMPTY_STRING
        val builder = StringBuilder()
        for (p in params) {
            builder.append('&').append(p.asUrlEncodedPair())
        }
        return builder.toString().substring(1)
    }

    fun addAll(other: ParameterList) {
        params.addAll(other.params)
    }

    fun addQuerystring(queryString: String) {
        if (queryString.isNotEmpty()) {
            for (param in queryString.split((PARAM_SEPARATOR).toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
                val pair = param.split((PAIR_SEPARATOR).toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val key = OAuthEncoder.decode(pair[0])
                val value = if (pair.size > 1) OAuthEncoder.decode(pair[1]) else EMPTY_STRING
                params.add(Parameter(key, value))
            }
        }
    }

    fun contains(param: Parameter): Boolean {
        return params.contains(param)
    }

    fun size(): Int {
        return params.size
    }

    fun sort(): ParameterList {
        val sorted = ParameterList(params)
        Log.d("saoted", sorted.params.get(0).asUrlEncodedPair())
        Log.d("saoted", sorted.params.get(1).asUrlEncodedPair())
        sorted.params.sort()
        Log.d("saoted", sorted.params.get(0).asUrlEncodedPair())
        Log.d("saoted", sorted.params.get(1).asUrlEncodedPair())
        return sorted
    }

    companion object {
        private val QUERY_STRING_SEPARATOR = '?'
        private val PARAM_SEPARATOR = "&"
        private val PAIR_SEPARATOR = "="
        private val EMPTY_STRING = ""
    }
}