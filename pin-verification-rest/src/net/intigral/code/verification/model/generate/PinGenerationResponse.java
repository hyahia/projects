package net.intigral.code.verification.model.generate;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PinGenerationResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	private Response response;

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public PinGenerationResponse(Response response) {
		this.response = response;
	}

	public PinGenerationResponse() {
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(response.getResult_code());
		builder.append(",");
		builder.append(response.getMessage());
		return builder.toString();
	}

}
