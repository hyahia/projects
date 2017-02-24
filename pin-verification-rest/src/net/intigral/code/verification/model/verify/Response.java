package net.intigral.code.verification.model.verify;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Response implements Serializable{
	private static final long serialVersionUID = 1L;
	private String result_code;
	private String valid;

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public Response(String result_code, String valid) {
		this.result_code = result_code;
		this.valid = valid;
	}

	public Response() {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Response [result_code=");
		builder.append(result_code);
		builder.append(", valid=");
		builder.append(valid);
		builder.append("]");
		return builder.toString();
	}
}