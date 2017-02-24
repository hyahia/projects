package net.intigral.code.verification.model.generate;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Response implements Serializable{
	private static final long serialVersionUID = 1L;
	private String result_code;
	private String message;

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Response(String result_code, String message) {
		this.result_code = result_code;
		this.message = message;
	}

	public Response() {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Response [result_code=");
		builder.append(result_code);
		builder.append(", message=");
		builder.append(message);
		builder.append("]");
		return builder.toString();
	}
}