package com.ricky.common.constant;

/**
 * @Author: ricky
 * @Date: 2019/7/1 10:59
 */
public enum MessageCode {

    FAILURE(500, "操作失败"),
    NO_DATA(1, "查询数据不存在"),
    SUCCESS(200, "操作成功"),
    PARAMETER_ERROR(1001, "提交参数不符合规范"),
    API_CALL_FAILED(3001, "接口调用失败"),
    SYSTEM_ERROR(5001, "系统错误！"),
    NETWORK_FAILURE(8001, "网络异常，请重试"),
    EMAIL_FAILURE(8002, "发送失败，请重新发送"),

    ;


    private final int code;
    private final String msg;

    MessageCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
