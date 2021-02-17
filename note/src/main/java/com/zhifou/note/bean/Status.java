package com.zhifou.note.bean;

import lombok.Getter;

@Getter
public enum  Status {
    /**
     *  响应状态码
    * */
    OK(200,"OK"),
    CREATED(201,"新资源被创建"),
    ACCEPTED(202,"已接受处理请求但尚未完成"),
    NOT_AUTHORITATIVE(203,"没有授权"),
    NO_CONTENT(204,"无内容"),
    RESET_CONTENT(205,"内容重置"),
    PARTIAL_CONTENT(206,"部分内容"),
    MULTIPLE_CHOICES(300,"选择多项"),
    MOVED_PERMANENTLY(301,"永久移动"),
    MOVED_TEMP(302,"临时重定向"),
    SEE_OTHER(303,"查看其他"),
    BAD_REQUEST(400,"错误的请求"),
    UNAUTHORIZED(401,"未经授权"),
    NOT_FOUND(404,"找不到"),
    METHOD_NOT_ALLOWED(405, "不允许的方法"),
    NOT_ACCEPTABLE(406,"不能接受"),
    REQUEST_TIMEOUT(408,"请求超时"),
    CONFLICT(409, "发生冲突"),
    PRECONDITION_FAILED(412, "前提条件失败(条件更新时的冲突)"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),
    INTERNAL_SERVER_ERROR(500, "内部服务器错误"),
    SERVICE_UNAVAILABLE(503, "暂停服务"),
    /**
     *  前后端分离时的状态码以及信息
     *  #1000～1999 区间表示参数错误
     *  #2000～2999 区间表示用户错误
     *  #3000～3999 区间表示接口异常
     **/
    CLIENT_SUCCESS(20000,"成功"),
    CLIENT_FAIL(20001,"失败"),
    CLIENT_USERNAME_PASSWORD(20002,"用户名或密码错误"),
    CLIENT_NULL_AUTHORIZED(20003,"权限不足"),
    CLIENT_FAR_FAIL(20004,"远程调用失败"),
    CLIENT_REPEAT(20005,"重复操作");
    private  Integer code;
    private  String message;

    private Status(){}

    Status(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}