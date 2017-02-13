package net.readify.knockknock;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Hossam Yahya
 * The REST controller class for Readify triangle type problem.
 */
@Controller
@RequestMapping("/api/")
public class KnockKnockController {
	
	/**
	 * @param a first triangle side
	 * @param b second triangle side
	 * @param c third triangle side
	 * @return the triangle type (one of "Not A Triangle" or "Equilateral" or "Isosceles" or "Scalene")
	 */
	@RequestMapping(value = "/TriangleType", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody String getTriangleType(@RequestParam int a, @RequestParam int b, @RequestParam int c) {
		System.out.println(String.format("getTriangleType() called with A = '%s', B = '%s', and C = '%s'", a, b, c));
		
		String triangleType = "";
		
		if(a+b <= c || a+c <= b || b+c <= a){
			triangleType = "Not A Triangle";
		}else if(a == b && b == c){
			triangleType = "Equilateral";
		}else if (a == b || b == c || a == c){
			triangleType = "Isosceles";
		}else{
			triangleType = "Scalene";
		}

		System.out.println(String.format("getTriangleType() completed for A = '%s', B = '%s', and C = '%s'", a, b, c));

		return triangleType;
	}
}
