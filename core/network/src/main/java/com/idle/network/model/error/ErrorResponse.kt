package com.idle.network.model.error

data class HttpResponseException(
    val status: HttpResponseStatus,
    val rawCode: Int,
    val errorRequestUrl: String,
    val msg: String? = null,
    override val cause: Throwable? = null,
) : Exception(msg, cause) {
//    fun print(errorType: String? = null): String {
//        val errorMsg = errorType?.type?.let { "\n$it" } ?: ""
//        return if (errorMsg.isNotEmpty()) "$rawCode ${status.msg}$errorMsg" else status.toString()
//    }
}

enum class HttpResponseStatus(val code: Int, val msg: String) {
    Ok(200, "Ok"),
    Created(201, "Created"),
    Accepted(202, "Accepted"),
    NotAuthoritative(203, "Not Authoritative"),
    NoContent(204, "No Content"),
    Reset(205, "Reset"),
    Partial(206, "Partial"),
    MultChoice(300, "Mult Choice"),
    MovedPerm(301, "Moved Perm"),
    MovedTemp(302, "Moved Temp"),
    SeeOther(303, "See Other"),
    NotModified(304, "Not Modified"),
    UseProxy(305, "Use Proxy"),
    BadRequest(400, "Bad Request"),
    Unauthorized(401, "Unauthorized"),
    PaymentRequired(402, "Payment Required"),
    Forbidden(403, "Forbidden"),
    NotFound(404, "Not Found"),
    BadMethod(405, "Bad Method"),
    NotAcceptable(406, "Not Acceptable"),
    ProxyAuth(407, "Proxy Auth"),
    ClientTimeout(408, "Client Timeout"),
    Conflict(409, "Conflict"),
    Gone(410, "Gone"),
    LengthRequired(411, "Length Required"),
    PreconFailed(412, "Precon Failed"),
    EntityTooLarge(413, "Entity Too Large"),
    ReqTooLong(414, "Req Too Long"),
    UnsupportedType(415, "Unsupported Type"),
    ReqTooMany(429, "Req Too Many"),
    InternalError(500, "Internal Error"),
    NotImplemented(501, "Not Implemented"),
    BadGateway(502, "Bad Gateway"),
    Unavailable(503, "Unavailable"),
    GatewayTimeout(504, "Gateway Timeout"),
    Version(505, "Version"),
    UnknownError(520, "Unknown Error"),
    Unknown(-1, "Unknown"),
    ;

    companion object {
        fun create(code: Int): HttpResponseStatus {
            return values().firstOrNull { it.code == code } ?: Unknown
        }
    }

    override fun toString(): String {
        return "$code $msg"
    }
}
