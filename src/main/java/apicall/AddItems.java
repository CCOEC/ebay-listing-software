package ebaytool.apicall;

import com.mongodb.*;
import com.mongodb.util.*;
import ebaytool.apicall.ApiCall;
import java.io.*;
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

public class AddItems extends ApiCall {
	
	private String userid;
	private String site;
	private String chunkidx;
	private String[] itemids;
	private String requestxml;
	
	public AddItems() throws Exception {
	}
	
	public String call() throws Exception {
		
		String userid;
		String site;
		HashMap<String,String> tokenmap = getUserIdToken();
		
		BasicDBObject query = new BasicDBObject();
		query.put("ext.deleted", new BasicDBObject("$exists", 0));
		query.put("ext.status", "(re)list");
		query.put("ItemID", new BasicDBObject("$exists", 0));
		
		BasicDBObject update = new BasicDBObject();
		update.put("$set", new BasicDBObject("ext.status", "(re)listing"));
		
		DBCollection coll = db.getCollection("items");
		WriteResult result = coll.update(query, update, false, true);
		
		query.put("ext.status", "(re)listing");
		
		LinkedHashMap<String,LinkedHashMap> lhm = new LinkedHashMap<String,LinkedHashMap>();
		DBCursor cur = coll.find(query);
		while (cur.hasNext()) {
			DBObject item = cur.next();
			
			userid = ((BasicDBObject) item.get("ext")).get("UserID").toString();
			site   = item.get("Site").toString();
			
			/* todo: remove more fields */
			//item.removeField("_id"); // if delete here, can't mapping result data.
			item.removeField("BuyerProtection");
			item.removeField("SellingStatus");
			item.removeField("ext");
			((BasicDBObject) item.get("ShippingDetails")).removeField("SalesTax");
			
			if (!lhm.containsKey(userid)) {
				lhm.put(userid, new LinkedHashMap<String,LinkedHashMap>());
			}
			
			if (!lhm.get(userid).containsKey(site)) {
				((LinkedHashMap) lhm.get(userid)).put(site, new LinkedHashMap<Integer,ArrayList>());
				((LinkedHashMap) lhm.get(userid).get(site)).put(0, new ArrayList<DBObject>());
			}
			
			int curidx = ((LinkedHashMap) lhm.get(userid).get(site)).size();
			int size = ((List) ((LinkedHashMap) lhm.get(userid).get(site)).get(curidx-1)).size();
			if (size >= 5) {
				((LinkedHashMap) lhm.get(userid).get(site)).put(curidx,
																new ArrayList<DBObject>());
				curidx = ((LinkedHashMap) lhm.get(userid).get(site)).size();
			}
			
			// add item data to each userid.site.chunk array.
			((List) ((LinkedHashMap) lhm.get(userid).get(site)).get(curidx-1)).add(item);
		}		
		
		// each userid
		for (String tmpuserid : lhm.keySet()) {
			LinkedHashMap lhmuserid = lhm.get(tmpuserid);
			
			// each site
			for (Object tmpsite : lhmuserid.keySet()) {
				LinkedHashMap lhmsite = (LinkedHashMap) lhmuserid.get(tmpsite);
				
				// each chunk
				for (Object tmpchunk : lhmsite.keySet()) {
					List litems = (List) lhmsite.get(tmpchunk);
					
					BasicDBObject requestdbo = new BasicDBObject();
					requestdbo.append("WarningLevel", "High");
					requestdbo.append("RequesterCredentials",
									  new BasicDBObject("eBayAuthToken", tokenmap.get(tmpuserid)));
					
					String[] itemids = new String[5];
					int messageid = 0;
					List<DBObject> ldbo = new ArrayList<DBObject>();
					for (Object tmpidx : litems) {
						
						itemids[messageid] = ((BasicDBObject) tmpidx).get("_id").toString();
						String id = ((BasicDBObject) tmpidx).get("_id").toString();
						System.out.println(tmpuserid
										   +" "+tmpsite
										   +" "+tmpchunk+"."+messageid
										   +":"+itemids[messageid]);
						
						((BasicDBObject) tmpidx).removeField("_id"); // remove _id here, not before.
						
						ldbo.add(new BasicDBObject("MessageID", id).append("Item", tmpidx));
						
						String title = ((BasicDBObject) tmpidx).get("Title").toString();
					}
					
					requestdbo.append("AddItemRequestContainer", ldbo);
					
					JSONObject jso = JSONObject.fromObject(requestdbo.toString());
					JSONArray tmpitems = jso.getJSONArray("AddItemRequestContainer");
					for (Object tmpitem : tmpitems) {
						JSONObject tmpi = ((JSONObject) tmpitem).getJSONObject("Item");
						
						/* expand array elements */
						if (tmpi.has("PaymentAllowedSite") && tmpi.get("PaymentAllowedSite")
							.getClass().toString().equals("class net.sf.json.JSONArray")) {
							tmpi.getJSONArray("PaymentAllowedSite").setExpandElements(true);
						}
						
						if (tmpi.has("PaymentMethods") && tmpi.get("PaymentMethods")
							.getClass().toString().equals("class net.sf.json.JSONArray")) {
							tmpi.getJSONArray("PaymentMethods").setExpandElements(true);
						}
						
						if (((JSONObject) tmpi.get("PictureDetails")).has("PictureURL")
							&& ((JSONObject) tmpi.get("PictureDetails")).get("PictureURL")
							.getClass().toString().equals("class net.sf.json.JSONArray")) {
							((JSONObject) tmpi.get("PictureDetails"))
								.getJSONArray("PictureURL").setExpandElements(true);
						}

						if (((JSONObject) tmpi.get("ShippingDetails")).has("ShippingServiceOptions")
							&& ((JSONObject) tmpi.get("ShippingDetails")).get("ShippingServiceOptions")
							.getClass().toString().equals("class net.sf.json.JSONArray")) {
							((JSONObject) tmpi.get("ShippingDetails"))
								.getJSONArray("ShippingServiceOptions").setExpandElements(true);
						}
					}			
					jso.getJSONArray("AddItemRequestContainer").setExpandElements(true);
					
					XMLSerializer xmls = new XMLSerializer();
					xmls.setObjectName("AddItemsRequest");
					xmls.setNamespace(null, "urn:ebay:apis:eBLBaseComponents");
					xmls.setTypeHintsEnabled(false);
					
					writelog("AIs.jso"
							 +"."+((String) tmpuserid)
							 +"."+((String) tmpsite)
							 +"."+new Integer(Integer.parseInt(tmpchunk.toString())).toString()
							 +".xml", jso.toString());
					
					String requestxml = xmls.write(jso);

					String requestxmlfilename = "AIs.req"
						+"."+((String) tmpuserid)
						+"."+((String) tmpsite)
						+"."+new Integer(Integer.parseInt(tmpchunk.toString())).toString()
						+".xml";
					
					writelog(requestxmlfilename, requestxml);
					
					validatexml(requestxmlfilename);
					
					ecs18.submit(new ApiCallTask(0, requestxml, "AddItems"));
					
				}
			}
		}
		
		// each userid
		for (String tmpuserid : lhm.keySet()) {
			LinkedHashMap lhmuserid = lhm.get(tmpuserid);
			
			// each site
			for (Object tmpsite : lhmuserid.keySet()) {
				LinkedHashMap lhmsite = (LinkedHashMap) lhmuserid.get(tmpsite);
				
				// each chunk
				for (Object tmpchunk : lhmsite.keySet()) {
					List litems = (List) lhmsite.get(tmpchunk);
					
					String responsexml = ecs18.take().get();
					
					writelog("AIs.res"
							 +"."+((String) tmpuserid)
							 +"."+((String) tmpsite)
							 +"."+new Integer(Integer.parseInt(tmpchunk.toString())).toString()
							 +".xml", responsexml);
					
					parseresponse(responsexml);
				}
			}
		}
		
		return "OK";
	}
	
	public BasicDBObject parseresponse(String responsexml) throws Exception {
		
		BasicDBObject responsedbo = convertXML2DBObject(responsexml);
		
		String ack = responsedbo.get("Ack").toString();
		System.out.println("Ack:"+ack);
		
		DBCollection coll = db.getCollection("items");
		
		String classname = responsedbo.get("AddItemResponseContainer").getClass().toString();
		
		BasicDBList dbl = new BasicDBList();
		if (classname.equals("class com.mongodb.BasicDBObject")) {
			dbl.add((BasicDBObject) responsedbo.get("AddItemResponseContainer"));
		} else if (classname.equals("class com.mongodb.BasicDBList")) {
			dbl = (BasicDBList) responsedbo.get("AddItemResponseContainer");
		} else {
			System.out.println("Class Error:"+classname);
			return responsedbo;
		}
		
		for (Object oitem : dbl) {
			BasicDBObject item = (BasicDBObject) oitem;
			
			String id        = item.getString("CorrelationID");
			String itemid    = item.getString("ItemID");
			String starttime = item.getString("StartTime");
			String endtime   = item.getString("EndTime");
			
			BasicDBObject upditem = new BasicDBObject();
			upditem.put("ext.status", "");
			if (itemid != null) {
				upditem.put("ItemID", itemid);
				upditem.put("ListingDetails.StartTime", starttime);
				upditem.put("ListingDetails.EndTime", endtime);
				upditem.put("SellingStatus.ListingStatus", "Active");
			}
			
			if (item.get("Errors") != null) {
				String errorclass = item.get("Errors").getClass().toString();
				BasicDBList errors = new BasicDBList();
				if (errorclass.equals("class com.mongodb.BasicDBObject")) {
					errors.add((BasicDBObject) item.get("Errors"));
				} else if (errorclass.equals("class com.mongodb.BasicDBList")) {
					errors = (BasicDBList) item.get("Errors");
				} else {
					System.out.println("Class Error:"+errorclass);
					continue;
				}
				upditem.put("ext.errors", errors);
			}
			
			BasicDBObject query = new BasicDBObject();
			query.put("_id", new ObjectId(id));
			
			BasicDBObject update = new BasicDBObject();
			update.put("$set", upditem);
			
			WriteResult result = coll.update(query, update);
			System.out.println("WriteResult:"+id+" : "+itemid+" : "+result);
			
		}
		
		return responsedbo;
	}
	
	private HashMap<String,String> getUserIdToken() throws Exception {
		
		HashMap<String,String> hashmap = new HashMap<String,String>();
		
		DBCursor cur = db.getCollection("users").find();
		while (cur.hasNext()) {
			DBObject user = cur.next();
			
			if (user.containsKey("userids")) {
				BasicDBObject userids = (BasicDBObject) user.get("userids");
				for (Object userid : userids.keySet()) {
					
					String ebaytkn = 
						((BasicDBObject) userids.get(userid.toString())).get("eBayAuthToken").toString();
					
					hashmap.put(userid.toString(), ebaytkn);
				}
			}
		}
		
		return hashmap;
	}
	
	/* XML Validation */
	// ref: http://java.sun.com/developer/technicalArticles/xml/validationxpath/
	private void validatexml(String filename) throws Exception {
		
		// todo: why Country error occures?
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder parser = dbf.newDocumentBuilder();
		Document document = parser.parse(new File("/var/www/ebaytool.jp/logs/apixml/"+filename));
		
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		Source schemaFile = new StreamSource(new File("/var/www/ebaytool.jp/data/ebaySvc.xsd"));
		Schema schema = factory.newSchema(schemaFile);
		
		Validator validator = schema.newValidator();
		
		try {
			validator.validate(new DOMSource(document));
		} catch (SAXException e) {
			System.out.println(e.toString());
		}
		
		return;
	}
	
}