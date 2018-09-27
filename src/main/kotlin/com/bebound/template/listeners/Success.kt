package com.bebound.template.listeners

open class Success : Response {

    class BodySuccess(var body: Any?) : Success()

    class MapSuccess(var params: Map<String, Any>?) : Success()

    class Builder {

        private val params = HashMap<String, Any>()
        private var body: Any? = null

        fun withParameter(name: String, value: Any): Builder {
            params[name] = value
            return this
        }

        fun withBody(body: Any): Builder {
            this.body = body
            return this
        }

        fun build(): Success {
            return if (body != null)
                BodySuccess(body)
            else
                MapSuccess(params)
        }
    }
}
