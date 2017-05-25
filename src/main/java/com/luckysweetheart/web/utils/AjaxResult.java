package com.luckysweetheart.web.utils;

import com.alibaba.fastjson.annotation.JSONField;
import com.luckysweetheart.utils.ResultInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Ajax结果
 *
 * @author luopeng
 */
public class AjaxResult {

	@JSONField(name = "success")
	private boolean success = false;

	@JSONField(name = "msg")
	private String msg;

	@JSONField(name = "result")
	private Object result;

	@JSONField(name = "resultCode")
	private int resultCode;

	public void success() {
		this.setSuccess(true);
	}

	public void success(Object result) {
		this.setSuccess(true);
		this.setResult(result);
	}

	public void from(ResultInfo<?> resultInfo) {
		this.setSuccess(resultInfo.isSuccess());
		this.setMsg(resultInfo.getMsg());
	}

	public void fail(String msg, int resultCode) {
		this.setSuccess(false);
		this.setResultCode(resultCode);
		this.setMsg(msg);
	}

	public void fail(String msg) {
		this.setSuccess(false);
		this.setMsg(msg);
	}

	@SuppressWarnings("unchecked")
	public void put(String key, Object value) {
		if (result == null) {
			result = new HashMap<String, Object>();
		}
		if (result instanceof Map) {
			((Map) result).put(key, value);
		} else {
			throw new RuntimeException("error ! result is not a instanceof Map ?");
		}
	}

	public static AjaxResult createFailedResult(String msg) {
		AjaxResult result = new AjaxResult();
		result.setSuccess(false);
		result.setMsg(msg);
		return result;
	}

	public static AjaxResult createDefault() {
		AjaxResult result = new AjaxResult();
		result.setSuccess(false);
		return result;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
}
