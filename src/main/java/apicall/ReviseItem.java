package ebaytool.apicall;

import com.mongodb.*;
import com.mongodb.util.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import javax.xml.parsers.*;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import javax.xml.XMLConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.bson.types.ObjectId;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class ReviseItem extends ApiCall {
	
	private String email;
	private String taskid;
	
	public ReviseItem() throws Exception {
	}
	
	public ReviseItem(String[] args) throws Exception {
		this.email  = args[0];
		this.taskid = args[1];
	}
	
	public String call() throws Exception {
		
		HashMap<String,String> tokenmap = getUserIdToken(email);
		
		BasicDBObject userdbo =
			(BasicDBObject) db.getCollection("users").findOne(new BasicDBObject("email", email));
		
		String user_id = userdbo.getString("_id");
		String uuidprefix = user_id.substring(user_id.length()-8);
		
		/* set intermediate status */
		BasicDBObject query = new BasicDBObject();
		query.put("org.ItemID", new BasicDBObject("$exists", 1));
		query.put("status",     taskid);
		
		BasicDBObject update = new BasicDBObject();
		update.put("$set", new BasicDBObject("status", taskid+"_processing"));
		
		DBCollection coll = db.getCollection("items."+userdbo.getString("_id"));
		WriteResult result = coll.update(query, update, false, true);
		
		/* re-query */
		query.put("status", taskid+"_processing");
		DBCursor cursor = coll.find(query);
		Integer count = cursor.count();
		Integer currentnum = 0;
    
		updatemessage(email, true, "Revising " + count + " items...");
    
		while (cursor.hasNext()) {
			
			DBObject item = cursor.next();
			DBObject mod = (DBObject) item.get("mod");
			DBObject org = (DBObject) item.get("org");
			
			String userid = item.get("UserID").toString();
			String site   = mod.get("Site").toString();
			
			/* ScheduleTime */
			if (mod.containsField("ScheduleTime")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00.000");
				Date scheduletime = (Date) mod.get("ScheduleTime");
				String scheduletimestr = sdf.format(scheduletime);
				scheduletimestr = scheduletimestr.replace(" ", "T") + "Z";
				mod.put("ScheduleTime", scheduletimestr);
			}
			
			// todo: don't use user _id for prefix.
			String uuid = uuidprefix + item.get("_id").toString();
			uuid = uuid.toUpperCase();
			mod.put("UUID", uuid);
			mod.put("ItemID", org.get("ItemID").toString());
						
			BasicDBObject reqdbo = new BasicDBObject();
			reqdbo.append("ErrorLanguage", "en_US");
			reqdbo.append("WarningLevel", "High");
			reqdbo.append("RequesterCredentials",
										new BasicDBObject("eBayAuthToken", tokenmap.get(userid)));
			reqdbo.append("MessageID", userdbo.getString("_id")+" "+item.get("_id").toString());
			reqdbo.append("Item", mod);
			
			// copy from AddItems
			String jss = reqdbo.toString();
					
			JSONObject jso = JSONObject.fromObject(jss);
			JSONObject tmpitem = jso.getJSONObject("Item");
			expandElements(tmpitem);
			
			XMLSerializer xmls = new XMLSerializer();
			xmls.setObjectName("ReviseItemRequest");
			xmls.setNamespace(null, "urn:ebay:apis:eBLBaseComponents");
			xmls.setTypeHintsEnabled(false);
					
			String requestxml = xmls.write(jso);
			
			writelog("ReviseItem/req.xml", requestxml);
      
			updatemessage(email, true, "Revising " + (currentnum+1) + " of " + count + " items...");
			currentnum++;
					
			Future<String> future = pool18.submit
				(new ApiCallTask(userid, getSiteID(site), requestxml, "ReviseItem"));
			future.get(); // wait
		}
    
		updatemessage(email, false, "Revising finished.");
		
		return "";
	}
	
	public String callback(String responsexml) throws Exception {
		
		writelog("ReviseItem/res.xml", responsexml);
		
		BasicDBObject responsedbo = convertXML2DBObject(responsexml);
		log("Ack:"+responsedbo.get("Ack").toString());
		
		// todo: almost same as AddItems callback function.
		String[] messages = responsedbo.getString("CorrelationID").split(" ");
		String itemcollectionname_id = messages[0];
		
		String id = messages[1];
		String itemid    = responsedbo.getString("ItemID");
		String starttime = responsedbo.getString("StartTime");
		String endtime   = responsedbo.getString("EndTime");
		
		DBCollection coll = db.getCollection("items."+itemcollectionname_id);
		
		BasicDBObject upditem = new BasicDBObject();
		upditem.put("status", "");
		/*
		if (itemid != null) {
			upditem.put("org.ItemID", itemid);
			upditem.put("org.ListingDetails.StartTime", starttime);
			upditem.put("org.ListingDetails.EndTime", endtime);
			upditem.put("org.SellingStatus.ListingStatus", "Active");
		}
		*/
		
		// todo: aware <SeverityCode>Warning</SeverityCode>
		if (responsedbo.get("Errors") != null) {
			String errorclass = responsedbo.get("Errors").getClass().toString();
			BasicDBList errors = new BasicDBList();
			if (errorclass.equals("class com.mongodb.BasicDBObject")) {
				errors.add((BasicDBObject) responsedbo.get("Errors"));
			} else if (errorclass.equals("class com.mongodb.BasicDBList")) {
				errors = (BasicDBList) responsedbo.get("Errors");
			} else {
				log("Class Error:"+errorclass);
			}
			
			upditem.put("error", errors);
			
		} else {
			
			/* No error! verified. */
			upditem.put("error", null);
			
		}
		
		WriteResult result = coll.update(new BasicDBObject("_id", new ObjectId(id)),
																		 new BasicDBObject("$set", upditem));
		
		return "";
	}
	
	// todo: not copy from AddItems
	private void expandElements(JSONObject item) throws Exception {
		
		for (Object key : item.keySet()) {
			
			String classname = item.get(key).getClass().toString();
			
			if (classname.equals("class net.sf.json.JSONObject")) {
				
				expandElements((JSONObject) item.get(key));
				
			} else if (classname.equals("class net.sf.json.JSONArray")) {
				
				((JSONArray) item.get(key)).setExpandElements(true);
				
				for (Object elm : (JSONArray) item.get(key)) {
					if (elm.getClass().toString().equals("class net.sf.json.JSONObject")) {
						expandElements((JSONObject) elm);
					}
				}
			}
		}
		
		return;
	}
	
	// todo: move to super class?
  /*
	private int getSiteID(String site) throws Exception {

		Integer siteid = null;
		
		DBObject row = db.getCollection("US.eBayDetails")
			.findOne(null, new BasicDBObject("SiteDetails", 1));
		BasicDBList sitedetails = (BasicDBList) row.get("SiteDetails");
		for (Object sitedbo : sitedetails) {
			if (site.equals(((BasicDBObject) sitedbo).getString("Site"))) {
				siteid = Integer.parseInt(((BasicDBObject) sitedbo).getString("SiteID"));
				break;
			}
		}
		
		return siteid;
	}
  */
}
