package com.bebound.template.listeners

import com.google.gson.annotations.SerializedName

class Failure(@field:SerializedName("error")
              var errorStatus: String?) : Response {

    class Builder {
        private lateinit var errorStatus: String

        fun withErrorStatus(errorStatus: String): Builder {
            this.errorStatus = errorStatus
            return this
        }

        fun build(): Failure {
            return Failure(errorStatus)
        }
    }
}
