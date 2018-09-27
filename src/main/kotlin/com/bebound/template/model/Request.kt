package com.bebound.template.model

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

abstract class Request private constructor() {

    @SerializedName("transport")
    var transport: String? = null

    @SerializedName("moduleId")
    var appId: Long = 0

    @SerializedName("moduleName")
    var appName: String? = null

    @SerializedName("moduleVersion")
    var appVersion: Long = 0

    @SerializedName("operation")
    var operationName: String? = null

    abstract var parameters: Map<String, Any>

    val isSmsRequest: Boolean
        get() = transport!!.equals("sms", ignoreCase = true)

    val isWebRequest: Boolean
        get() = transport!!.equals("web", ignoreCase = true)

    abstract fun <T> getBodyAs(type: Class<T>): T

    class MapRequest : Request() {
        @SerializedName("params")
        override lateinit var parameters: Map<String, Any>

        @Expose(serialize = false, deserialize = false)
        var jsonParams: String? = null

        override fun <T> getBodyAs(type: Class<T>): T {
            return GsonBuilder().create().fromJson(jsonParams, type)
        }
    }

    class BodyRequest<T> : Request() {
        @SerializedName("params")
        var body: T? = null

        override var parameters: Map<String, Any>
            get() = throw UnsupportedOperationException()
            set(parameters) = throw UnsupportedOperationException()

        override fun <T> getBodyAs(type: Class<T>): T {
            throw UnsupportedOperationException()
        }
    }

    class Builder {
        private var transport: String? = null
        private var appId: Long = 0
        private var appName: String? = null
        private var appVersion: Long = 0
        private var operationName: String? = null

        private var body: Any? = null
        private val parameters = HashMap<String, Any>()

        fun withTransport(transport: String): Builder {
            this.transport = transport
            return this
        }

        fun withAppId(appId: Long): Builder {
            this.appId = appId
            return this
        }

        fun withAppName(appName: String): Builder {
            this.appName = appName
            return this
        }

        fun withAppVersion(appVersion: Long): Builder {
            this.appVersion = appVersion
            return this
        }

        fun withOperationName(operationName: String): Builder {
            this.operationName = operationName
            return this
        }

        fun put(name: String, value: Any): Builder {
            parameters[name] = value
            return this
        }

        fun withBody(body: Any): Builder {
            this.body = body
            return this
        }

        fun build(): Request {
            val request: Request

            if (body != null) {
                val bodyRequest = BodyRequest<Any>()
                bodyRequest.body = body
                request = bodyRequest
            } else {
                val mapRequest = MapRequest()
                mapRequest.parameters = parameters
                request = mapRequest
            }

            request.appVersion = appVersion
            request.appId = appId
            request.appName = appName
            request.operationName = operationName
            request.transport = transport

            return request
        }
    }
}
