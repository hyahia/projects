package net.intigral.code.verification.model.verify;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PinVerificationResponse implements Serializable{
	private static final long serialVersionUID = 1L;

	private Response response;

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public PinVerificationResponse(Response response) {
		this.response = response;
	}

	public PinVerificationResponse() {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(response.getResult_code());
		builder.append(",");
		builder.append(response.getValid());
		return builder.toString();
	}

}
