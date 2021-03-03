package com.example.note_android.util

enum class Status {
    /* 成功 */
    SUCCESS(200, "成功"),  /* 默认失败 */
    COMMON_FAIL(999, "失败"),  /* 参数错误：1000～1999 */
    PARAM_NOT_VALID(1001, "参数无效"), NOT_FOUND_NOTE(1002, "未找到指定笔记"), PARAM_TYPE_ERROR(
        1003,
        "参数类型错误"
    ),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),  /* 用户错误 */
    USER_NOT_LOGIN(2001, "用户未登录"), TOKEN_EXPIRED(2002, "Token已过期"), USER_CREDENTIALS_ERROR(
        2003,
        "账号或密码错误"
    ),
    USER_ACCOUNT_ALREADY_EXIST(2004, "账号已存在"), USER_CREDENTIALS_EXPIRED(
        2008,
        "密码过期"
    ),
    USER_ACCOUNT_DISABLE(2005, "账号不可用"), USER_ACCOUNT_LOCKED(2006, "账号被锁定"), USER_ACCOUNT_NOT_EXIST(
        2007,
        "账号不存在"
    ),
    USER_ACCOUNT_USE_BY_OTHERS(2009, "账号下线"),  /* 业务错误 */
    NO_PERMISSION(3001, "没有权限"), NOT_FOUND(3002, "未找到指定资源");

    private var code: Int? = null
    private var message: String? = null

    constructor()

    constructor(code: Int?, message: String?) {
        this.code = code
        this.message = message
    }
}