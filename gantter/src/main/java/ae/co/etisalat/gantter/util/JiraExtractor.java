package ae.co.etisalat.gantter.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

public class JiraExtractor {
   private static String proposalIdSearch = "";
   private static final String OFF_DEV_FILTER_ID = "13954";
   private static final String OFF_QA_FILTER_ID = "13955";
   private static final String OFF_UAT_FILTER_ID = "13956";
   private static final String ON_DEV_FILTER_ID = "13957";
   private static final String ON_QA_FILTER_ID = "13958";
   private static final String ON_UAT_FILTER_ID = "13959";
   private static final String MOBILE_FILTER_ID = "13918";
   private static final String FIXED_FILTER_ID = "13801";
   private static final String COMS_FILTER_ID = "13927";
   private static final String DATE_DEV_FILTER_ID = "13920";

   public static void main2(String[] args) throws Exception {
	   System.setProperty("javax.net.ssl.trustStore", "/Users/Hossam/git/gantter/gantter/RTC.jks");
//	   reload("Fixed");
//	   getEpicAssignees("CBCMCOMS-2784");
	   System.out.println(System.currentTimeMillis());
	   String in = "111009";
//	   System.out.println(bugExists("119975"));
	   System.out.println(createJiraTask("205095"));
/*for (String i : in.split(",")) {
	try {
		//	   getEpicDetails("103977");
		//	   search("117687");
		//	   extractReport();
		createJiraTask(i);
	} catch (Exception e) {
		e.printStackTrace();
	}
}*/
	//	   System.out.println(getProductionIssues());
	   System.out.println(System.currentTimeMillis());

   }
   
   public static String reload(String domain) throws IOException{
	   String content = "";
	   switch (domain) {
		case "Search":
			content = searchProject(proposalIdSearch);
			break;
		case "Production-CQs":
			content = getProductionIssues();
		break;
		default:
			content = extract(domain);
			break;
		}
	   updateFile(domain, content);
	   return content;
   }
   
   private static String getProductionIssues() {
	   
	   HashMap<String,String> titlesMap = new HashMap<>();
	   titlesMap.put("IT_DEV_CBCM-Interface","Billing Interface");
	   titlesMap.put("IT_DEV_COMS","COMS");
	   titlesMap.put("IT_DEV_CBCM-Fixed","Fixed");
	   titlesMap.put("IT_DEV_CBCM-Data","Data");
	   titlesMap.put("IT_DEV_CBCM-Wireless","Mobile");
	   titlesMap.put("Unassigned","No Category");
		
	   RTCExtractor.init();
	   ArrayList<Defect> defects = new ArrayList<>();
	   HashMap<String,ArrayList<Defect>> defectsMap = RTCExtractor.getProductionDefects();
	   String tasks = "";
	   String task = "{\"id\":%d,\"type\":\"%s\",\"name\":\"Production CQs\",\"link\":\"\",\"dellead\":\"\",\"rfqa\":\"\",\"rfa\":\"\",\"rfl\":\"\",\"qa\":\"0\",\"uat\":\"0\",\"prod\":\"%d\",\"progress\":0,\"description\":\"\",\"code\":\"P.CQs\",\"level\":0,\"status\":\"STATUS_ACTIVE\",\"depends\":\"\",\"canWrite\":true,\"start\":1475971200000,\"duration\":2,\"end\":1476144000000,\"startIsMilestone\":false,\"endIsMilestone\":false,\"collapsed\":false,\"assigs\":[{\"id\":\"0\", \"resourceId\":\"0\", \"roleId\":\"Developer\", \"effort\":0}],\"defects\":[%s],\"ptasks\":[],\"hasChild\":true}";
	   Iterator it = defectsMap.keySet().iterator();
	   int i = 0;
	   while (it.hasNext()) {
		   String key = (String)it.next();
		   defects = defectsMap.get(key);
		   String defectsStr = "";
		   for(Defect defect : defects) {
			   defectsStr += "{ \"id\": \"" + defect.getId() + "\", \"title\": \"" + defect.getTitle() + "\","
					   + " \"status\": \"" + defect.getStatus() + "\", \"environment\": \"" + defect.getEnvironment() +"\" }" + ",";
		   }
		   defectsStr = defectsStr.replaceAll("\n", "");
		   defectsStr = defectsStr.replaceAll("\r", "");
		   System.out.println(defectsStr);
		   tasks += String.format(task,(i+1), titlesMap.get(key)==null ? key : titlesMap.get(key), defects.size(), defectsStr.substring(0,defectsStr.length()-1)) + ",";
		   i++;
	   }
	   
	   
	   String text = "{\"tasks\":[%s],\"selectedRow\":0,\"deletedTaskIds\":[],\"resources\":[{\"id\": \"0\", \"name\": \"\"}],\"canWrite\":true,\"canWriteOnParent\":true,\"splitterPosition\":95,\"zoom\":\"m\"}";
	   RTCExtractor.destroy();
	   return String.format(text,tasks.substring(0,tasks.length()-1));
   }

public static String search(String proposalId) throws IOException{
	   proposalIdSearch = proposalId;
	   String content = searchProject(proposalId);
	   updateFile("Search", content);
	   return content;
   }
   
   private static String searchProject(String proposalId) {
	String epicDetails = getEpicDetails(proposalId);
	if ("".equalsIgnoreCase(epicDetails) ) {
		return "{\"tasks\":[],\"deletedTaskIds\":[],\"canWrite\":true,\"canWriteOnParent\":true,\"splitterPosition\":53.631694790902415,\"zoom\":\"m\"}";
	}
	String content = "";
	try {

         SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		 ft.setTimeZone(TimeZone.getTimeZone("GMT"));
		 String text = "{\"tasks\":[";
		 String endText = "],\"selectedRow\":0,\"deletedTaskIds\":[],\"resources\":[$RESOURCES],\"canWrite\":true,\"canWriteOnParent\":true,\"splitterPosition\":95,\"zoom\":\"m\"}";
		 String project = "{\"id\":-%d,\"type\":\"%s\",\"projStatus\":\"%s\",\"name\":\"%s\",\"link\":\"%s\",\"pm\":\"%s\",\"dellead\":\"%s\",\"rfqa\":\"%s\",\"rfa\":\"%s\",\"rfl\":\"%s\",\"qa\":\"%s\",\"uat\":\"%s\",\"prod\":\"%s\",\"progress\":%d,\"description\":\"\",\"code\":\"%s\",\"level\":0,\"status\":\"%s\",\"depends\":\"\",\"canWrite\":true,\"start\":%d,\"duration\":%d,\"end\":%d,\"startIsMilestone\":false,\"endIsMilestone\":false,\"collapsed\":false,\"assigs\":[%s],\"defects\":[%s],\"ptasks\":[%s],\"hasChild\":true}";
		 long duration = 0;
		 long days = 0;
		 
		 ArrayList<String> resources = new ArrayList<>();
		 ArrayList<Defect> defects = new ArrayList<>();
		 ArrayList<Task> tasks = new ArrayList<>();
		 
		 RTCExtractor.init();
		int i = 0;
        for (String projectDetails : epicDetails.split(";;;")) {
        	String[] rowItems = projectDetails.split(",",-1);
			String status = "";
			
			String defectsCounts ="0,0,0";
			try {
			 	int qa = 0, uat = 0, prod = 0;
			 	defects = RTCExtractor.getProjectDefects(proposalId);
			 	tasks = RTCExtractor.getProjectTasks(proposalId);
			 	for (Defect defect : defects) {
			 		if ("PROD".equalsIgnoreCase(defect.getEnvironment())) {
			 		 prod++;
			 		}else if ("UAT".equalsIgnoreCase(defect.getEnvironment())) {
			 		 uat++;
			 		}else {
			 		 qa++;
			 		}
			 	}
			 	System.out.println(defects);
			 	defectsCounts = qa + "," + uat + "," + prod;
			 } catch (Throwable e) {
			 	e.printStackTrace();
			 }
			
			String startStr = ft.format(new Date());
			
			if (rowItems[0]  != null && !"".equalsIgnoreCase(rowItems[0])) {
			 	startStr =   rowItems[0]; 
			 	status = !"PLC".equalsIgnoreCase(rowItems[2]) ? "STATUS_ACTIVE" : "STATUS_SUSPENDED";
			 }else {
			 	status = "STATUS_FAILED";
			 }
			 
			 String endStr =  ft.format(new Date(ft.parse(startStr).getTime() + 5*1000*60*60*24));

			 if (rowItems[1] != null && !"".equalsIgnoreCase(rowItems[1])) {
			 	endStr = rowItems[1];
			 	status = !"PLC".equalsIgnoreCase(rowItems[2]) ? "STATUS_ACTIVE" : "STATUS_SUSPENDED";
			 }else {
			 	status = "STATUS_FAILED";
			 }
			
			duration = ft.parse(endStr).getTime() - ft.parse(startStr).getTime();
			long start = ft.parse(startStr).getTime();
			long end = ft.parse(endStr).getTime();
			days = (int) ((duration / (1000*60*60*24)));
			
			String assigneesNames = getEpicAssignees(rowItems[3]);
			System.out.println("assigneesNames====>"+assigneesNames);
			
			String resourcesList = "";
			
			if (StringUtils.isEmpty(assigneesNames)) {
				assigneesNames = "Unassigned";
			}
			for (String assignee : assigneesNames.split(",")) {
				
				if(!StringUtils.isEmpty(assignee))
					if(!resources.contains(assignee))
						resources.add(assignee);
				
				resourcesList += "{\"id\":\"" + resources.indexOf(assignee) + "\", \"resourceId\":\"" + resources.indexOf(assignee) + "\", \"roleId\":\"Developer\", \"effort\":0},";

			}
			resourcesList = resourcesList.substring(0,resourcesList.length()-1);
//			System.out.println("=====resourcesList------->" + resourcesList);
			int progress = 0;
			if (!"New Design-Development In Progress	-Design in Progress	-Design Completed-On Hold-New Development".contains(rowItems[11])) {
				progress = 120;
			}
			System.out.println("=====Status------->" + rowItems[11] + " - " + progress);

			// Defects
			String defectsStr = "";
			for(Defect defect : defects) {
				defectsStr += "{ \"id\": \"" + defect.getId() + "\", \"title\": \"" + defect.getTitle() + "\","
						+ " \"status\": \"" + defect.getStatus() + "\", \"environment\": \"" + defect.getEnvironment() +"\" }" + ",";
			}
			defectsStr = defectsStr.replaceAll("\n", "");
			defectsStr = defectsStr.replaceAll("\r", "");
			System.out.println(defectsStr);
			
			// Tasks
			String tasksStr = "";
			for(Task task : tasks) {
				tasksStr += "{ \"id\": \"" + task.getId() + "\", \"title\": \"" + task.getTitle() + "\","
						+ " \"raisedby\": \"" + task.getRaisedBy() + "\", \"hpsmcrno\": \"" + task.getHspmCRNumber() 
						+ "\", \"hpsmcrtno\": \"" + task.getHspmCRTaskNumber() 
						+"\" }" + ",";
			}
			tasksStr = tasksStr.replaceAll("\n", "");
			tasksStr = tasksStr.replaceAll("\r", "");
			System.out.println(tasksStr);						
									
			
			String projectName = rowItems[4].replaceAll(":", "").replaceAll("\"", "");
			String finalText = String.format(project, (i+1),rowItems[9],rowItems[11], projectName,rowItems[3], rowItems[10]
					 ,rowItems[5],rowItems[6] ,rowItems[7] ,rowItems[8]
			 		, defectsCounts.split(",")[0], defectsCounts.split(",")[1], defectsCounts.split(",")[2]
			 		, progress, rowItems[3], status,start,days,end, resourcesList
			 		,"".equals(defectsStr)?"": defectsStr.substring(0,defectsStr.length()-1)
			 		,"".equals(tasksStr)?"": tasksStr.substring(0,tasksStr.length()-1)
			 		);
			
			System.out.println(endStr + " , " + startStr + "," + duration +"," + days + "," + end + " , " + start);
			System.out.println(finalText);
			text += finalText + ",";
			i++;
	         }
	         
        	RTCExtractor.destroy();
        
        	if(resources.size() > 0) {
        		StringBuilder sb = new StringBuilder();
        		for(int x = 0; x < resources.size(); ++x) {
        			if(x != 0)
        				sb.append(",");
        			sb.append("{\"id\": \"" + x + "\", \"name\": \"" + resources.get(x) + "\"}");
        		}
        		endText = endText.replace("$RESOURCES", sb.toString());
        	} else {
        		endText = endText.replace("$RESOURCES", "");
        	}
        
	         text = text.substring(0,text.length()-1) + endText;
			 System.out.println(text);
			 content = text;

	      } catch (Exception e) {
	         e.printStackTrace();
	   }
     return content;
   }

private static String extract(String domain){
	  String content = "";
	  String html = "http://jiracrm.etisalat.corp.ae/issues/?filter=";

	  switch (domain) {
		case "Mobile":
				html += MOBILE_FILTER_ID;
			break;
		case "COMS":
			html += COMS_FILTER_ID;
		break;
		case "Fixed":
			html += FIXED_FILTER_ID;
		break;
		case "Data":
			html += DATE_DEV_FILTER_ID;
		break;
		case "Off-Dev":
			html += OFF_DEV_FILTER_ID;
		break;
		case "Off-QA":
			html += OFF_QA_FILTER_ID;
		break;
		case "Off-UAT":
			html += OFF_UAT_FILTER_ID;
		break;
		case "On-Dev":
			html += ON_DEV_FILTER_ID;
		break;
		case "On-QA":
			html += ON_QA_FILTER_ID;
		break;
		case "On-UAT":
			html += ON_UAT_FILTER_ID;
		break;
		default:
			html += "13918";
			break;
		}
	  
      try {
    	 String encoding = new sun.misc.BASE64Encoder().encode("geothomas:Etisalat@007".getBytes());
         Document doc = Jsoup.connect(html).timeout(0).header("Authorization", "Basic " + encoding).get();
//         System.out.println(doc.toString());
         if (doc.toString().indexOf("id=\"issuetable\"") < 0) {
     		return "{\"tasks\":[],\"deletedTaskIds\":[],\"canWrite\":true,\"canWriteOnParent\":true,\"splitterPosition\":53.631694790902415,\"zoom\":\"m\"}";
     	 }
         doc.removeAttr("div");
         Elements tableElements = doc.select("table");

         Elements tableHeaderEles = tableElements.select("thead tr th");

         Elements tableRowElements = tableElements.select(":not(thead) tr");

         SimpleDateFormat ft = new SimpleDateFormat("dd/MMM/yy");
		 ft.setTimeZone(TimeZone.getTimeZone("GMT"));
		 String text = "{\"tasks\":[";
		 String endText = "],\"selectedRow\":0,\"deletedTaskIds\":[],\"resources\":[$RESOURCES],\"canWrite\":true,\"canWriteOnParent\":true,\"splitterPosition\":95,\"zoom\":\"m\"}";
		 String project = "{\"id\":-%d,\"type\":\"%s\",\"projStatus\":\"%s\",\"name\":\"%s\",\"link\":\"%s\",\"pm\":\"%s\",\"dellead\":\"%s\",\"rfqa\":\"%s\",\"rfa\":\"%s\",\"rfl\":\"%s\",\"qa\":\"%s\",\"uat\":\"%s\",\"prod\":\"%s\",\"progress\":%d,\"description\":\"\",\"code\":\"%s\",\"level\":0,\"status\":\"%s\",\"depends\":\"\",\"canWrite\":true,\"start\":%d,\"duration\":%d,\"end\":%d,\"startIsMilestone\":false,\"endIsMilestone\":false,\"collapsed\":false,\"assigs\":[%s],\"defects\":[%s],\"ptasks\":[%s],\"hasChild\":true}";
		 long duration = 0;
		 long days = 0;
		 
		 ArrayList<String> resources = new ArrayList<>();
		 ArrayList<Defect> defects = new ArrayList<>();
		 ArrayList<Task> tasks = new ArrayList<>();
		 
		 RTCExtractor.init();
		 
         for (int i = 0; i < tableRowElements.size(); i++) {
            Element row = tableRowElements.get(i);
            System.out.println(row);
            Elements rowItems = row.select("td");
			String status = "";
			
			String defectsCounts ="0,0,0";
			try {
				int qa = 0, uat = 0, prod = 0;
				defects = RTCExtractor.getProjectDefects(rowItems.get(14).text().replaceAll(",",""));
				tasks = RTCExtractor.getProjectTasks(rowItems.get(14).text().replaceAll(",",""));
				for (Defect defect : defects) {
					if ("PROD".equalsIgnoreCase(defect.getEnvironment())) {
						prod++;
					}else if ("UAT".equalsIgnoreCase(defect.getEnvironment())) {
						uat++;
					}else {
						qa++;
					}
				}
				System.out.println(defects);
				defectsCounts = qa + "," + uat + "," + prod;
			} catch (Throwable e) {
				e.printStackTrace();
			}
			
			String startStr = ft.format(new Date());
			
			if (rowItems.get(9).text()  != null && !"".equalsIgnoreCase(rowItems.get(9).text())) {
				startStr =   rowItems.get(9).text(); 
				status = !"PLC".equalsIgnoreCase(rowItems.get(8).text()) ? "STATUS_ACTIVE" : "STATUS_SUSPENDED";
			}else {
				status = "STATUS_FAILED";
			}
			
			String endStr =  ft.format(new Date(ft.parse(startStr).getTime() + 5*1000*60*60*24));

			if (rowItems.get(10).text() != null && !"".equalsIgnoreCase(rowItems.get(10).text())) {
				endStr =  rowItems.get(10).text();
				status = !"PLC".equalsIgnoreCase(rowItems.get(8).text()) ? "STATUS_ACTIVE" : "STATUS_SUSPENDED";
			}else {
				status = "STATUS_FAILED";
			}
			
			duration = ft.parse(endStr).getTime() - ft.parse(startStr).getTime();
			long start = ft.parse(startStr).getTime();
			long end = ft.parse(endStr).getTime();
			days = (int) ((duration / (1000*60*60*24)));
			
			String assigneesNames = getEpicAssignees(rowItems.get(1).text());
			System.out.println("assigneesNames====>"+assigneesNames);
			System.out.println("Project Status====>"+rowItems.get(6).text());
			
			int progress = 0;
			if (!"New Design-Development In Progress	-Design in Progress	-Design Completed-On Hold-New Development".contains(rowItems.get(6).text())) {
				progress = 120;
			}

			String resourcesList = "";
			
			if (StringUtils.isEmpty(assigneesNames)) {
				assigneesNames = "Unassigned";
			}
			for (String assignee : assigneesNames.split(",")) {
				
				if(!StringUtils.isEmpty(assignee))
					if(!resources.contains(assignee))
						resources.add(assignee);
				
				resourcesList += "{\"id\":\"" + resources.indexOf(assignee) + "\", \"resourceId\":\"" + resources.indexOf(assignee) + "\", \"roleId\":\"Developer\", \"effort\":0},";

			}
			resourcesList = resourcesList.substring(0,resourcesList.length()-1);
//			System.out.println("=====resourcesList------->" + resourcesList);
			
			// Defects
			String defectsStr = "";
			for(Defect defect : defects) {
				defectsStr += "{ \"id\": \"" + defect.getId() + "\", \"title\": \"" + defect.getTitle() + "\","
						+ " \"status\": \"" + defect.getStatus() + "\", \"environment\": \"" + defect.getEnvironment() +"\" }" + ",";
			}
			defectsStr = defectsStr.replaceAll("\n", "");
			defectsStr = defectsStr.replaceAll("\r", "");
			System.out.println(defectsStr);
			
			// Tasks
			String tasksStr = "";
			for(Task task : tasks) {
				tasksStr += "{ \"id\": \"" + task.getId() + "\", \"title\": \"" + task.getTitle() + "\","
						+ " \"raisedby\": \"" + task.getRaisedBy() + "\", \"hpsmcrno\": \"" + task.getHspmCRNumber() 
						+ "\", \"hpsmcrtno\": \"" + task.getHspmCRTaskNumber() 
						+"\" }" + ",";
			}
			tasksStr = tasksStr.replaceAll("\n", "");
			tasksStr = tasksStr.replaceAll("\r", "");
			System.out.println(tasksStr);						
									
			
			String projectName = rowItems.get(2).text().replaceAll(":", "").replaceAll("\"", "");
			String finalText = String.format(project, (i+1), rowItems.get(16).text(),rowItems.get(6).text(), projectName,rowItems.get(1).text(), rowItems.get(15).text()
					,rowItems.get(7).text(),rowItems.get(11).text() , rowItems.get(12).text()  ,  rowItems.get(13).text()
					,defectsCounts.split(",")[0], defectsCounts.split(",")[1], defectsCounts.split(",")[2]
					,progress , rowItems.get(1).text(), status,start,days,end, resourcesList
					,"".equals(defectsStr)?"": defectsStr.substring(0,defectsStr.length()-1)
					,"".equals(tasksStr)?"": tasksStr.substring(0,tasksStr.length()-1)
					);
			
			System.out.println(endStr + " , " + startStr + "," + duration +"," + days + "," + end + " , " + start);
			System.out.println(finalText);
			text += finalText + ",";
	         }
	         
         	RTCExtractor.destroy();
         
         	if(resources.size() > 0) {
         		StringBuilder sb = new StringBuilder();
         		for(int x = 0; x < resources.size(); ++x) {
         			if(x != 0)
         				sb.append(",");
         			sb.append("{\"id\": \"" + x + "\", \"name\": \"" + resources.get(x) + "\"}");
         		}
         		endText = endText.replace("$RESOURCES", sb.toString());
         	} else {
         		endText = endText.replace("$RESOURCES", "");
         	}
         
	         text = text.substring(0,text.length()-1) + endText;
			 System.out.println(text);
			 content = text;

	      } catch (Exception e) {
	         e.printStackTrace();
	   }
      return content;
   }
   
	private static void updateFile(String domain, String content) throws IOException {
		File f = new File(domain + ".proj");
		if (f.exists()) {
			f.delete();
		}
		f.createNewFile();
		
		FileWriter fw = new FileWriter(domain + ".proj");
		BufferedWriter bw = new BufferedWriter(fw);

		bw.write(content);
		bw.close();
	}
	
	private static String getEpicAssignees(String epic){
		String url = "http://jiracrm.etisalat.corp.ae/rest/api/2/search?jql=%22Epic%20Link%22%3D"+epic+"&fields=assignee";
		StringBuilder assignees = new StringBuilder();
		ArrayList<String> assigneesNames = new ArrayList<>();
		String assignee = "";
		try {
	    	String encoding = new sun.misc.BASE64Encoder().encode("geothomas:Etisalat@007".getBytes());
	        Document doc = Jsoup.connect(url).timeout(0).header("Authorization", "Basic " + encoding).ignoreContentType(true).get();
	        if (doc.text().length() < 300) {
				return "";
			}
//	        System.out.println(doc.text());
			assignees.append(doc.text());
			while (assignees.indexOf("\"displayName\":") > 0) {
				assignees.delete(0,assignees.indexOf("\"displayName\":")+13);
				assignee = assignees.substring(1,assignees.indexOf("\",")+1).replaceAll(" \"", "\"");
				if (!assigneesNames.contains(assignee) && assignee != null && !assignee.trim().equals("")) {
					assigneesNames.add(assignee);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(assigneesNames);
		assignees = new StringBuilder();
		for (String item:assigneesNames) {
			assignees.append(item).append(",");
		}
		String result = assignees.toString().substring(0,assignees.toString().length()-1).replaceAll("\"", "").replaceAll(":", "");
		System.out.println("getEpicAssignees===>" + result );
		return result;
	}
	
	private static String getEpicDetails(String proposalId){//+and+cf[11218]=CBCM
		String url = "http://jiracrm.etisalat.corp.ae/rest/api/2/search?jql=cf[10800]="+proposalId+"+and+cf[11214]+is+not+EMPTY&fields=status,key,customfield_10800,customfield_11011,customfield_11107,customfield_11008,customfield_10802,customfield_11214,customfield_11112,customfield_11116,customfield_11103,summary,customfield_11601";
		StringBuilder details = new StringBuilder();
		String deliveryLead = "";
		String key = "";
		String devStart = "";
		String devEnd = "";
		String summary = "";
		String domain = "";
		String proposalType = "";
		String rfqa = "";
		String rfa = "";
		String rfl = "";
		String pm = "";
		String status = "";
		
		StringBuilder ret = new StringBuilder();
		try {
	    	String encoding = new sun.misc.BASE64Encoder().encode("geothomas:Etisalat@007".getBytes());
	        Document doc = Jsoup.connect(url).timeout(0).header("Authorization", "Basic " + encoding).ignoreContentType(true).get();
	        if (doc.text().length() < 200) {
				return "";
			}
	        details.append(doc.text());
	        Pattern p = Pattern.compile("CBCMCOMS-");
	        Matcher m = p.matcher(details);
	        int i = 0;
	        while (m.find()) {
	            i++;
	        }
	        if (i == 0) {
				return ",,,,,,,,,,";
			}
	        String[] projects = details.toString().split("operations,editmeta,changelog,",-1);
	        int j = 0;
	        for(String project: projects){
	        	if (j++ == 0) {
					continue;
				}
	        	StringBuilder projectDetails = new StringBuilder(project);
		        projectDetails.delete(0, projectDetails.indexOf("\"key\":\"CBCMCOMS-")+7);
		        key = projectDetails.substring(0,projectDetails.indexOf("\""));
	
		        projectDetails.delete(0, projectDetails.indexOf("\"summary\":\"")+11);
		        summary = projectDetails.substring(0,projectDetails.indexOf("\","));
		        
		        projectDetails.delete(0,projectDetails.indexOf("\"customfield_11116\":") + 20);
		        if (!"null".equals(projectDetails.substring(0, 4))) {
					rfa = projectDetails.substring(1,projectDetails.indexOf("\","));
					rfa = rfa.substring(0, rfa.indexOf("T"));
				}
		        
		        projectDetails.delete(0,projectDetails.indexOf("\"customfield_11601\":") + 20);
		        if (!"null".equals(projectDetails.substring(0, 4))) {
		        	pm = projectDetails.substring(1,projectDetails.indexOf("\","));
				}
		        
		        projectDetails.delete(0,projectDetails.indexOf("\"customfield_11008\":") + 20);
		        if (!"null".equals(projectDetails.substring(0, 4))) {
		        	devEnd = projectDetails.substring(1,projectDetails.indexOf("\","));
				}
		        
		        projectDetails.delete(0,projectDetails.indexOf("\"displayName\":\"") + 15);
		        deliveryLead = projectDetails.substring(0,projectDetails.indexOf("\""));
		        
		        projectDetails.delete(0,projectDetails.indexOf("\"value\":\"") + 9);
		        proposalType = projectDetails.substring(0,projectDetails.indexOf("\""));
		        
		        projectDetails.delete(0,projectDetails.indexOf("\"customfield_11011\":") + 20);
		        if (!"null".equals(projectDetails.substring(0, 4))) {
		        	devStart = projectDetails.substring(1,projectDetails.indexOf("\","));
				}
		        
		        projectDetails.delete(0,projectDetails.indexOf("\"customfield_11112\":") + 20);
		        if (!"null".equals(projectDetails.substring(0, 4))) {
					rfqa = projectDetails.substring(1,projectDetails.indexOf("\","));
					rfqa = rfqa.substring(0, rfqa.indexOf("T"));
				}
		        
		        projectDetails.delete(0,projectDetails.indexOf("\"customfield_11103\":") + 20);
		        if (!"null".equals(projectDetails.substring(0, 4))) {
					rfl = projectDetails.substring(1,projectDetails.indexOf("\","));
					rfl = rfl.substring(0, rfl.indexOf("T"));
				}
		        
		        projectDetails.delete(0,projectDetails.indexOf("\"value\":\"") + 9);
		        domain = projectDetails.substring(0,projectDetails.indexOf("\""));

		        projectDetails.delete(0,projectDetails.indexOf("\"name\":\"") + 8);
		        status = projectDetails.substring(0,projectDetails.indexOf("\""));
		        
		        ret.append(devStart).append(",").append(devEnd).append(",").append(proposalType).append(",")
		        	.append(key).append(",").append(summary.replace(",", "-")).append(",")
		        	.append(deliveryLead).append(",").append(rfqa).append(",").append(rfa)
		        	.append(",").append(rfl).append(",").append(domain).append(",").append(pm).append(",").append(status).append(";;;");
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(ret.toString());
		return ret.toString();
	}

	public static String extractReport(){
		  String content = "";
		  String html = "http://jiracrm.etisalat.corp.ae/issues/?filter=";
		  int offDev = 0, offQA = 0, offUAT = 0, onDev = 0, onQA = 0, onUAT = 0;
	      try {
	    	  final String key = "<td class=\"issuekey\">";
	    	  String encoding = new sun.misc.BASE64Encoder().encode("geothomas:Etisalat@007".getBytes());
	    	  
	    	  Document doc = Jsoup.connect(html + OFF_DEV_FILTER_ID).timeout(0).header("Authorization", "Basic " + encoding).get();
	    	  offDev = StringUtils.countOccurrencesOf(doc.toString(), key);
	    	  
	    	  doc = Jsoup.connect(html + OFF_QA_FILTER_ID).timeout(0).header("Authorization", "Basic " + encoding).get();
	    	  offQA = StringUtils.countOccurrencesOf(doc.toString(), key);
	    	  
	    	  doc = Jsoup.connect(html + OFF_UAT_FILTER_ID).timeout(0).header("Authorization", "Basic " + encoding).get();
	    	  offUAT = StringUtils.countOccurrencesOf(doc.toString(), key);
	    	  
	    	  doc = Jsoup.connect(html + ON_DEV_FILTER_ID).timeout(0).header("Authorization", "Basic " + encoding).get();
	    	  onDev = StringUtils.countOccurrencesOf(doc.toString(), key);
	    	  
	    	  doc = Jsoup.connect(html + ON_QA_FILTER_ID).timeout(0).header("Authorization", "Basic " + encoding).get();
	    	  onQA = StringUtils.countOccurrencesOf(doc.toString(), key);
	    	  
	    	  doc = Jsoup.connect(html + ON_UAT_FILTER_ID).timeout(0).header("Authorization", "Basic " + encoding).get();
	    	  onUAT = StringUtils.countOccurrencesOf(doc.toString(), key);

	    	  content = ""+offDev+","+offQA+","+offUAT+","+onDev+","+onQA+","+onUAT;
	    	  System.out.println(content);
	      }catch (Exception e) {
	    	  e.printStackTrace();
	      }
	    return content;
	}
	
	public static String createJiraTask(String defectId) throws Exception{
		if (bugExists(defectId)) {
			throw new Exception("Bug Already Exists:" + defectId);
		}
		String html = "http://jiracrm.etisalat.corp.ae/rest/api/2/issue/";
		String content = "{"+
							"\"fields\": {"+
						   		"\"project\":"+
						   		"{"+ 
						      		"\"key\": \"CBCMCOMS\""+
						   		"},"+
						   		"\"summary\": \"%s\","+
						   		"\"description\": \"Task for defect: https://rtcserver.etisalat.corp.ae:9443/ccm/web/projects/CBCM#action=com.ibm.team.workitem.viewWorkItem&id=" + defectId + "\","+
						   		"\"duedate\": \"%s\","+
						   		"\"labels\":[\"%s\",\"%s\"],"+
						   		"\"components\":[{\"name\":\"CBCM - Development\"}],"+
						   		"\"issuetype\": {"+
						   			"\"name\": \"Bug\""+
						   		"}"+
						   	"}"+
						"}";
		String taskId = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		if(c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
			c.add(Calendar.DATE, 3);
		else {
			c.add(Calendar.DATE, 1);
		}
		String date = sdf.format(c.getTime());
		
		RTCExtractor.init();
		ArrayList<Defect> defect = RTCExtractor.getDefectById(defectId);
		System.out.println(defect);
		String summary = defect.get(0).getTitle().replace("\\", "-");
		summary = summary.length() > 200 ? summary.substring(0,200): summary; 
		
		String request = String.format(content,defect.get(0).getEnvironment() + "-" + defectId + "-" + summary,date,defect.get(0).getProjectId(),defectId);
		System.out.println(request);
		sendPost(html, request);
			
		return taskId;
	}
	
	private static boolean bugExists(String defectId){//+and+cf[11218]=CBCM
		String url = "http://jiracrm.etisalat.corp.ae/rest/api/2/search?jql=issueType=Bug+and+labels=" + defectId;
		try {
	    	String encoding = new sun.misc.BASE64Encoder().encode("geothomas:Etisalat@007".getBytes());
	        Document doc = Jsoup.connect(url).timeout(0).header("Authorization", "Basic " + encoding).ignoreContentType(true).get();
	        if (doc.text().length() < 200) {
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return true;	
	}
	
	 public static void sendPost(String url, String content) {
			String encoding = new sun.misc.BASE64Encoder().encode("hoyahya:Hyadamsf10551".getBytes());

	        try {

	            URL obj = new URL(url);
	            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
	            conn.setReadTimeout(5000);
	            conn.addRequestProperty("Content-Type", "application/json");
	            conn.addRequestProperty("Authorization", "Basic " + encoding);

	            conn.setDoOutput(true);

	            OutputStreamWriter w = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

	            w.write(content);
	            w.close();

	            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String inputLine;
	            StringBuffer html = new StringBuffer();

	            while ((inputLine = in.readLine()) != null) {
	                html.append(inputLine);
	            }

	            in.close();
	            conn.disconnect();

	            System.out.println("URL Content... \n" + html.toString());

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}