package ae.co.etisalat.gantter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ae.co.etisalat.gantter.util.JiraExtractor;

@RestController
@RequestMapping("/gantter")
public class GantterProjectWS {

	@RequestMapping(value = "/projects/{domain}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getDomainProjects(@PathVariable String domain) throws Exception {
		System.out.println("getDomainProjects() called Domain:" + domain);
		BufferedReader br = null;
		String defaultString = "{\"tasks\":[],\"deletedTaskIds\":[],\"canWrite\":true,\"canWriteOnParent\":true,\"splitterPosition\":53.631694790902415,\"zoom\":\"m\"}";
		StringBuilder allText = new StringBuilder();

		try {
			if (!new File(domain + ".proj").exists()) {
				return defaultString;
			}
			String sCurrentLine;
			br = new BufferedReader(new FileReader(domain + ".proj"));
			while ((sCurrentLine = br.readLine()) != null) {
				allText.append(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println(allText.toString());
		return allText.toString();
	}

	@RequestMapping(value = "/projects/reload/{domain}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String reloadDomainProjects(@PathVariable String domain) throws Exception {
		System.out.println("reloadDomainProjects() called Domain:" + domain);
		BufferedReader br = null;
		String allText = "{\"tasks\":[],\"deletedTaskIds\":[],\"canWrite\":true,\"canWriteOnParent\":true,\"splitterPosition\":53.631694790902415,\"zoom\":\"m\"}";

		try {
			 allText = JiraExtractor.reload(domain);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println(allText);
		return allText;
	}
	
	@RequestMapping(value = "/projects/search/{proposalId}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String searchProjects(@PathVariable String proposalId) throws Exception {
		System.out.println("searchProjects() called for:" + proposalId);
		BufferedReader br = null;
		String allText = "{\"tasks\":[],\"deletedTaskIds\":[],\"canWrite\":true,\"canWriteOnParent\":true,\"splitterPosition\":53.631694790902415,\"zoom\":\"m\"}";

		try {
			 allText = JiraExtractor.search(proposalId);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println(allText);
		return allText;
	}
	
	@RequestMapping(value = "/projects/{domain}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String setDomainProjects(@PathVariable String domain, @RequestBody String projects) throws Exception {
		String projectsString = projects;
		System.out.println(String.format("setDomainProjects() called Domain: %s, Projects: %s", domain, projects));
		projectsString = java.net.URLDecoder.decode(projects, "UTF8");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");
		
		File f = new File(domain + ".proj");
		if (!f.exists()) {
			f.createNewFile();
		}else {
			f.renameTo(new File(domain + "-" + sdf.format(new Date()) + ".proj"));
		}
		
		FileWriter fw = new FileWriter(domain + ".proj");
		BufferedWriter bw = new BufferedWriter(fw);
		
		if (projectsString.endsWith("=")) {
			projectsString = projectsString.substring(0,projectsString.length()-1);
		}
		
		bw.write(projectsString);
		bw.close();
		
		return "{\"DOMAIN\": \"" + domain + "\",{\"ACTION\": \"SAVE\", \"STATUS\": \"SUCCESS\"}";
	}
	
	@RequestMapping(value = "/report/", method = RequestMethod.GET, produces = "text/plain")
	@ResponseBody
	public String getReport() throws Exception {
		System.out.println("getReport() called.");
		return JiraExtractor.extractReport();
	}
	
	@RequestMapping(value = "/rtcjira/{input}", method = RequestMethod.GET, produces = "text/plain")
	@ResponseBody
	public String extractFromRTCToJira(@PathVariable String input) throws Exception {
		System.out.println("extractFromRTCToJira() called: " + input);
		String succeeded = " ";
		String failed = " ";
		for (String i : input.split(",")) {
			try {
				JiraExtractor.createJiraTask(i);
				succeeded += i + ", ";
			} catch (Exception e) {
				failed += i + ", ";
			}
		}
		System.out.println("extractFromRTCToJira() finished: " + input);
		return "Succeeded:" + succeeded.substring(0, succeeded.length()-1) + "\nFailed:" + failed.substring(0, failed.length()-1);
	}
}
