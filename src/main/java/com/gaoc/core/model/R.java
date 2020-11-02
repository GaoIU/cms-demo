package com.gaoc.core.model;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class R {

	private int status;

	private String message;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object data;

	public static R ok() {
		return R.builder().status(HttpStatus.HTTP_OK).message("SUCCESS").build();
	}

	public static R ok(Object data) {
		return R.builder().status(HttpStatus.HTTP_OK).message("SUCCESS").data(data).build();
	}

	public static R fail(String message) {
		return R.builder().status(HttpStatus.HTTP_NO_CONTENT).message(message).build();
	}

	public static R fail(int code, String message) {
		return R.builder().status(code).message(message).build();
	}

	public static boolean isFail(R r) {
		if (r == null) {
			return true;
		}

		if (r.getStatus() != HttpStatus.HTTP_OK) {
			return true;
		}

		return false;
	}

	public static void printWriter(R r, HttpServletResponse response, String contentType) {
		Map<String, Object> data = new HashMap<>(2);
		data.put("status", r.getStatus());
		data.put("message", r.getMessage());
		response.setContentType(contentType);
		PrintWriter printWriter = null;

		try {
			printWriter = response.getWriter();
			printWriter.write(JSONUtil.toJsonStr(data));
			printWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
	}

}
