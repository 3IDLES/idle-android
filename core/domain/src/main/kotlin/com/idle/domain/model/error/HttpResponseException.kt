package com.idle.domain.model.error

data class HttpResponseException(
    val status: HttpResponseStatus,
    val apiErrorCode: ApiErrorCode,
    val msg: String? = null,
    override val cause: Throwable? = null,
) : Exception(msg, cause) {
    fun print(): String {
        return apiErrorCode.displayMsg
    }
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
            return entries.firstOrNull { it.code == code } ?: Unknown
        }
    }

    override fun toString(): String {
        return "$code $msg"
    }
}

enum class ApiErrorCode(val serverCode: String, val description: String, val displayMsg: String) {
    // USER Errors
    InvalidVerificationNumber("USER-001", "회원가입에 필요한 인증번호를 잘못 입력한 경우 발생합니다.", "잘못된 인증번호를 입력하였습니다."),
    VerificationNumberNotFound(
        "USER-002",
        "회원가입에 필요한 인증번호가 만료되었거나, 존재하지 않는 경우 발생합니다.",
        "인증번호가 만료되었거나 존재하지 않습니다."
    ),
    ImageUploadNotCompleted(
        "USER-003",
        "프로필 이미지가 완전히 업로드 되지 않았거나, 업로드에 실패한 경우 발생합니다.",
        "프로필 이미지 업로드에 실패하였습니다."
    ),

    // CENTER Errors
    DuplicateIdentifier(
        "CENTER-001",
        "센터 ID 중복 검사 시, 이미 가입된 ID가 존재하는 경우 발생합니다.",
        "해당 ID로 이미 가입된 센터가 존재합니다."
    ),
    AlreadyExistCenterManager("CENTER-002", "이미 가입된 센터 관리자가 존재하는 경우 발생합니다.", "이미 가입된 센터 관리자 입니다."),
    AlreadyExistCenter("CENTER-003", "이미 등록된 센터 정보가 존재하는 경우 발생합니다.", "이미 등록된 센터 입니다."),
    CenterNotFound("CENTER-004", "조회 시, 센터가 존재하지 않는 경우 발생합니다.", "존재하지 않는 센터 입니다."),

    // CARER Errors
    AlreadyExistCarer("CARER-001", "이미 가입된 요양 보호사 정보가 존재하는 경우 발생합니다.", "이미 가입된 요양보호사 입니다."),

    // APPLY Errors
    AlreadyApplied("APPLY-001", "해당 공고에 같은 지원 수단으로 지원한 과거 이력이 존재하는 경우 발생합니다.", "이미 지원한 공고 입니다."),

    // API Errors
    InvalidParameter("API-001", "API 요청 시, 잘못된 parameter를 입력한 경우 발생합니다.", "잘못된 입력입니다."),

    // SECURITY Errors
    UnAuthorizedRequest(
        "SECURITY-001",
        "권한이 없는 사용자가 로그인이 필요한 API에 접근 요청 시 발생합니다.",
        "권한이 없는 사용자 입니다."
    ),
    InvalidLoginRequest(
        "SECURITY-002",
        "로그인 시, ID 혹은 비밀번호가 잘못된 경우 발생합니다.",
        "아이디 혹은 비밀번호가 잘못되었습니다."
    ),
    InvalidPassword("SECURITY-003", "입력한 비밀번호가 잘못된 경우 발생합니다.", "비밀번호가 잘못되었습니다."),
    UnregisteredUser("SECURITY-004", "등록되지 않은 사용자가 로그인을 시도한 경우 발생합니다.", "등록되지 않은 사용자입니다."),

    // SYSTEM Errors
    InternalServerError(
        "SYSTEM-001",
        "서버 내 모든 시스템 내부 에러를 포함합니다. 특히 시스템 내에서 처리하지 못한 예외가 존재하는 경우에도 발생할 수 있습니다.",
        "서버 내부 오류입니다."
    ),

    // JWT Errors
    TokenDecodeException(
        "JWT-001",
        "유효하지 않은 토큰, 토큰을 디코딩할 때, JWT의 형식에 맞지 않는 경우 발생합니다.",
        "유효하지 않은 토큰입니다."
    ),
    TokenNotValid(
        "JWT-002",
        "유효하지 않은 토큰, 토큰 내 값 검증에 실패한 경우 발생합니다.(ex. 알고리즘, 서명)",
        "토큰 검증에 실패하였습니다."
    ),
    TokenExpiredException(
        "JWT-003",
        "토큰이 만료된 경우 발생합니다. 재 로그인이 필요합니다.",
        "토큰이 만료되었습니다. 재로그인이 필요합니다."
    ),
    TokenNotFound(
        "JWT-004",
        "토큰을 찾을 수 없는 경우 발생합니다.",
        "토큰을 찾을 수 없습니다."
    ),
    NotSupportUserTokenType(
        "JWT-005",
        "지원하지 않는 유저 토큰 타입을 사용한 경우 발생할 수 있습니다(carer, center 제외)",
        "지원하지 않는 유저 토큰 타입입니다."
    ),

    // SMS Errors
    ClientException(
        "SMS-001",
        "SMS 문자 발송에 실패한 경우 발생합니다.",
        "SMS 발송에 실패하였습니다."
    ),

    // BUSINESS REGISTRATION Errors
    BusinessRegistrationResultNotFound(
        "BUSINESS-REGISTRATION-001",
        "사업자 등록번호 조회 결과가 없는 경우 발생합니다.",
        "사업자 등록번호 조회 결과가 없습니다."
    ),

    // GEO Code Errors
    GeoCodeResultNotFound(
        "GeoCode-001",
        "입력된 주소로 Geocode 를 검색한 결과가 없는 경우 발생합니다.",
        "해당 주소로 검색된 결과가 없습니다."
    ),

    UnknownError("UNKNOWN", "알 수 없는 오류가 발생했습니다.", "알 수 없는 오류가 발생하였습니다."),
    ;

    companion object {
        fun create(serverCode: String): ApiErrorCode {
            return entries.firstOrNull { it.serverCode == serverCode } ?: UnknownError
        }
    }

    override fun toString(): String {
        return "$serverCode: $description"
    }
}
