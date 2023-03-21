package com.oajstudios.pocketshop.utils.oauthInterceptor

class Parameter(private val key: String, private val value: String) : Comparable<Parameter> {

    fun asUrlEncodedPair(): String {
        return OAuthEncoder.encode(key) + "=" + OAuthEncoder.encode(
            value
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other === this) return true
        if (other !is Parameter) return false

        val otherParam = other as Parameter?
        return otherParam!!.key == key && otherParam.value == value
    }

    override fun hashCode(): Int {
        return key.hashCode() + value.hashCode()
    }

    override fun compareTo(parameter: Parameter): Int {
        val keyDiff = key.compareTo(parameter.key)

        return if (keyDiff != 0) keyDiff else value.compareTo(parameter.value)
    }

    companion object {
        private val UTF = "UTF8"
    }
}
