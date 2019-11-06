package com.ricky.common.utils;

import com.ricky.common.constant.MessageCode;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @Author: ricky
 * @Date: 2019/7/1 10:59
 */
public class R<T> extends HashMap<String, Object> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Integer SUCCESS = MessageCode.SUCCESS.getCode();
	private static final String SUCCESS_MSG = MessageCode.SUCCESS.getMsg();
	private static final Integer FAILURE = MessageCode.FAILURE.getCode();
	private static final String FAILURE_MSG = MessageCode.FAILURE.getMsg();
	public static final String CODE_VALUE = "code";
	public static final String MSG_VALUE = "msg";
	public static final String DATA_VALUE = "data";

	public static R ok(int code, String msg) {
		R r = new R();
		r.put(CODE_VALUE, code);
		r.put(MSG_VALUE, msg);
		r.put(DATA_VALUE, null);
		return r;
	}

	public static R ok() {
		return ok(SUCCESS, SUCCESS_MSG);
	}

	public static R ok(String msg) {
		return ok().put(MSG_VALUE, msg);
	}

	public static R ok(int code) {
		return ok().put(CODE_VALUE, code);
	}

	public static R ok(Object data) {
		return ok().put(DATA_VALUE, data);
	}

	public static R ok(MessageCode msg) {
		return ok().put(MSG_VALUE, msg.getMsg()).put(CODE_VALUE, msg.getCode());
	}

	public static R failure(int code, String msg) {
		R r = new R();
		r.put(CODE_VALUE, code);
		r.put(MSG_VALUE, msg);
		return r;
	}

	public static R failure() {
		return failure(FAILURE, FAILURE_MSG);
	}

	public static R failure(String msg) {
		return failure(FAILURE, msg);
	}

	public static R failure(Object data) {
		return failure().put(DATA_VALUE, data);
	}

	public static R failure(String msg, Object data) {
		return failure().put(MSG_VALUE, msg).put(DATA_VALUE, data);
	}

	public static R failure(MessageCode msg) {
		return failure().put(MSG_VALUE, msg.getMsg()).put(CODE_VALUE, msg.getCode());
	}

	public static R putData(Object value) {
		return  ok().put(DATA_VALUE, value);
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
