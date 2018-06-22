package io.opensource.trainingupdate;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.gmail.Gmail;

import io.opensource.trainingupdate.Settings.Setting;

/**
 * Helper app to automate training class schedule tasks.
 *
 */
public class App {

    public static void main(String... args) {
    	sendJenkinsScheduleToCloudBees();
    }
    
    private static void sendJenkinsScheduleToCloudBees() {
    	try {
    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    		StringBuffer message = new StringBuffer();
    		final String recipient = Settings.getSetting(Setting.JENKINS_EMAIL_RECIPIENT_NAME, "CloudBees Training");
    		final String sender = Settings.getSetting(Setting.JENKINS_EMAIL_SENDER_NAME, "Management Team");
    		message.append("<p>Hi " + recipient + ",</p>");
    		message.append("<p>We have updated our Jenkins training schedule. Here is our updated schedule:</p>");
    		message.append("<table cellspacing='0' cellpadding='5' border='1'>");
    		message.append("<thead><tr><th>SKU</th><th>Course</th><th>When</th><th>Where</th></thead>");
    		message.append("<tbody>");
	    	ObjectMapper mapper = new ObjectMapper();
	    	final URL url = new URL(Settings.getSetting(Setting.CLASS_SESSIONS));
	    	final TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
	    	Map<String, Object> schedule = mapper.readValue(url, typeRef);
	    	
	    	@SuppressWarnings("unchecked")
			Map<String, Object> embedded = (Map<String, Object>)schedule.get("_embedded");
	    	List<Object> classSessions = (List<Object>)embedded.get("class-sessions"); 
	    	for (Object cs : classSessions) {
	    		Map<String, Object> classSession = (Map<String, Object>)cs;
	    		Map<String, Object> classSessionSku = (Map<String, Object>)classSession.get("classSessionSku");
	    		String vendorSkuCode = (String)classSessionSku.get("vendorSkuCode");
	    		String skuName = (String)classSessionSku.get("skuName");
	    		Map<String, Object> skuVendor = (Map<String, Object>)classSessionSku.get("skuVendor");
	    		String vendorName = (String)skuVendor.get("vendorName");
	    		Map<String, Object> classSessionAddress = (Map<String, Object>)classSession.get("classSessionAddress");
	    		String city = (String)classSessionAddress.get("city");
	    		String state = (String)classSessionAddress.get("countrySubdivision");
	    		String classSessionStartDate = (String)classSession.get("classSessionStartDate");
	    		String classSessionStartTime = (String)classSession.get("classSessionStartTime");
	    		String classSessionEndTime = (String)classSession.get("classSessionEndTime");
	    		String classSessionTimeZone = (String)classSession.get("classSessionTimeZone");
	    		int classLength = (Integer)classSessionSku.get("classLength");
	    		LocalDate startDate = LocalDate.parse(classSessionStartDate, DateTimeFormatter.ISO_DATE);
	    		LocalDate endDate = startDate.plusDays(classLength - 1);
	    		
	    		if ("CloudBees".equals(vendorName)) {
		    		message.append("<tr>");
		    		message.append("<td>");
		    		message.append(vendorSkuCode);
		    		message.append("</td>");
		    		message.append("<td>");
		    		message.append(skuName);
		    		message.append("</td>");
		    		message.append("<td>");
		    		message.append(startDate.format(DateTimeFormatter.ofPattern("MMM dd")));
		    		message.append("-");
		    		message.append(endDate.format(DateTimeFormatter.ofPattern("MMM dd")));
		    		message.append(" ");
		    		message.append(classSessionStartTime);
		    		message.append("-");
		    		message.append(classSessionEndTime);
		    		message.append(" ");
		    		message.append(classSessionTimeZone);
		    		message.append("</td>");
		    		message.append("<td>");
		    		message.append(city);
		    		message.append(" ");
		    		message.append(state);
		    		message.append("</td>");
		    		message.append("</tr>");
	    		}
	    		
	    	}
	    	message.append("</tbody></table>");
	    	message.append("<p>Please let us know if you have any questions or concerns.</p>");
	    	message.append("Sincerly,<br>");
	    	message.append(sender);
	    	message.append("<br>");
    	    	
    		// Send Email
        	Gmail service = GMailUtils.getGmailService();
        	final String to = Settings.getSetting(Setting.JENKINS_EMAIL_TO);
        	final String from = Settings.getSetting(Setting.JENKINS_EMAIL_FROM);
        	final String subject = "Jenkins Training Schedule";
        	MimeMessage mimeMessage = GMailUtils.createEmail(to, from, subject, message.toString());
        	GMailUtils.sendMessage(service, "me", mimeMessage);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    }
    

}
