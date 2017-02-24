package net.intigral.code.verification.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.intigral.code.verification.model.generate.PinGenerationResponse;
import net.intigral.code.verification.model.generate.Response;
import net.intigral.code.verification.model.verify.PinVerificationResponse;
import net.intigral.code.verification.util.DBHelper;

@Controller
@RequestMapping("/")
public class PinController {
	
	@RequestMapping(value = "/verification_code/{user_id}.{format}", method = RequestMethod.POST/*, produces = {MediaType.ALL_VALUE ,MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}*/)
	public @ResponseBody PinGenerationResponse generatePin(HttpServletResponse response, HttpServletRequest request, @PathVariable String user_id, @PathVariable String format) throws Exception {
		System.out.println(String.format("generatePin() called with UserId = '%s' and format = '%s'", user_id, format));
		PinGenerationResponse pgr = null;
		
		try {
				DBHelper.getInstance().insertPin(user_id);
				pgr = new PinGenerationResponse(new Response("1","successful"));
		} catch (Throwable e) {
			pgr = new PinGenerationResponse(new Response("0","error"));
			e.printStackTrace();
		}

		System.out.println(String.format("generatePin() completed for UserId = '%s' and format = '%s'", user_id, format));

		return pgr;
	}
	
	@RequestMapping(value = "/verification_code/{user_id}/{code}.{format}", method = RequestMethod.GET/*, produces = {MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}*/)
	public @ResponseBody PinVerificationResponse verifyPin(HttpServletResponse response, HttpServletRequest request, @PathVariable String user_id, @PathVariable String format, @PathVariable String code) throws Exception {
		System.out.println(String.format("verifyPin() called with UserId = '%s' and format = '%s' and code = '%s'", user_id, format, code));

		PinVerificationResponse pvr = null;
		try {
			if (DBHelper.getInstance().verifyPin(user_id, code)) {
				pvr = new PinVerificationResponse(new net.intigral.code.verification.model.verify.Response("1","true"));
			}else {
				pvr = new PinVerificationResponse(new net.intigral.code.verification.model.verify.Response("1","false"));
			}
		} catch (Throwable e) {
			pvr = new PinVerificationResponse(new net.intigral.code.verification.model.verify.Response("0",""));
			e.printStackTrace();
		}
		
		System.out.println(String.format("verifyPin() completed for UserId = '%s' and format = '%s' and code = '%s'", user_id, format, code));

		return pvr;
	}
	
}
