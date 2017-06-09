package com.gsmart.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gsmart.model.Attendance;
import com.gsmart.model.Profile;
import com.gsmart.util.Loggers;


@Repository

@Transactional
public class PushNotificationDaoImpl implements PushNotificationDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Query query;

	

	final static private String FCM_URL = "https://fcm.googleapis.com/fcm/send";
//	final static private String message = "You are present today";
    final static private String server_key    = "AAAA6TaABk4:APA91bHEizH2xhOf5reGWwpTlh0WpMLCz6zUCzdW-BD_9IdhTQiY9Gx6orK_dHeZNQlNs7jRsTVDS4Fqc2Jzm9qyEGJuCgF9Ebh_ct7JA4zk4p80bx_ak7UR7uRimjgsdetyCqOcsNJ7";
//  final static private String tokenId =  "fuaR1H72DCs:APA91bG3pDU_2v1U4ikUtpBoGo_Xt3xCRj5FN0chKR5tkR2CX05nw1NcbHaiqCfqpzGtEsmmXCQIamdYjlOfhAUrSa8zQ9nlRMrnxKjcEqc-PH6pzWuEFV-c8XduiT1jZ1VAAKoFPjgb";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Scheduled(cron = "0 00 11 * * ?")
	public void sendNotificationpresent() {
		List<Attendance> token = null;
	query=sessionFactory.getCurrentSession().createQuery("select firstName, finalToken, inDate from Attendance where status='PRESENT'");
		token = query.list();
		System.out.println("Total Number Of Records : "+token.size());
		int i;
		Iterator it = token.iterator();		 
		for(i=0;i<token.size(); i++)
		{
		while(it.hasNext())
		{
			Object obj[] = (Object[])it.next();
			Date date = new Date((long) obj[2]);
			System.out.println("FirstName : "+obj[0]+ "\n" + "Token : "+obj[1]+ "\n" +"In Time: "+obj[2]);
			DateFormat formatter = new SimpleDateFormat("hh:mm a");
			String dateFormatted = formatter.format(date);
			System.out.println(dateFormatted);
		try{

		URL url = new URL(FCM_URL);
		HttpURLConnection conn;
		conn = (HttpURLConnection) url.openConnection();
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization","key="+server_key);
		conn.setRequestProperty("Content-Type","application/json");
		JSONObject infoJson = new JSONObject();
		infoJson.put("title","GSMART");
		infoJson.put("body", obj[0]+" is present today.. In Time: "+dateFormatted+"");
		JSONObject json = new JSONObject();
		json.put("to", obj[1]);
		json.put("notification", infoJson);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(json.toString());
		wr.flush();
		int status = 0;

		if( null != conn ){
		status = conn.getResponseCode();
		}

		if( status != 0){

		if( status == 200 ){
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		System.out.println("Android Notification Response : " + reader.readLine());
		}else if(status == 401){
		System.out.println("Notification Response : TokenId : " + obj[1] + " Error occurred :");
		}else if(status == 501){
		System.out.println("Notification Response : [ errorCode=ServerError ] TokenId : " + obj[1]);
		}else if( status == 503){
		System.out.println("Notification Response : FCM Service is Unavailable  TokenId : " + obj[1]);
		}
		}
		}catch(MalformedURLException mlfexception){
		System.out.println("Error occurred while sending push Notification!.." + mlfexception.getMessage());
		}catch(IOException mlfexception){
		System.out.println("Reading URL, Error occurred while sending push Notification!.." + mlfexception.getMessage());
		}catch(JSONException jsonexception){
		System.out.println("Message Format, Error occurred while sending push Notification!.." + jsonexception.getMessage());
		}catch (Exception exception) {
		System.out.println("Error occurred while sending push Notification!.." + exception.getMessage());
		}
		
		}
		
		}

    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Scheduled(cron = "0 01 11 * * ?")
	public void sendNotificationabsent() {
		
		
		List<Attendance> token = null;
		query=sessionFactory.getCurrentSession().createQuery("select firstName, finalToken from Attendance where status='ABSENT'");
		token = query.list();
		System.out.println("Total Number Of Records : "+token.size());
		Loggers.loggerStart(token);
		int i;
		Iterator it = token.iterator();		 
		for(i=0;i<token.size(); i++)
		{
		while(it.hasNext())
		{
			Object obj[] = (Object[])it.next();
			System.out.println("FirstName : "+obj[0]+ "\n" + "Token : "+obj[1]);
		try{

		URL url = new URL(FCM_URL);
		HttpURLConnection conn;
		conn = (HttpURLConnection) url.openConnection();
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization","key="+server_key);
		conn.setRequestProperty("Content-Type","application/json");
		JSONObject infoJson = new JSONObject();
		infoJson.put("title","GSMART");
		infoJson.put("body", obj[0]+" is absent today..");
		JSONObject json = new JSONObject();
		json.put("to", obj[1]);
		json.put("notification", infoJson);
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(json.toString());
		wr.flush();
		int status = 0;

		if( null != conn ){
		status = conn.getResponseCode();
		}

		if( status != 0){

		if( status == 200 ){
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		System.out.println("Android Notification Response : " + reader.readLine());
		}else if(status == 401){
		System.out.println("Notification Response : TokenId : " + obj[1] + " Error occurred :");
		}else if(status == 501){
		System.out.println("Notification Response : [ errorCode=ServerError ] TokenId : " + obj[1]);
		}else if( status == 503){
		System.out.println("Notification Response : FCM Service is Unavailable  TokenId : " + obj[1]);
		}
		}
		}catch(MalformedURLException mlfexception){
		System.out.println("Error occurred while sending push Notification!.." + mlfexception.getMessage());
		}catch(IOException mlfexception){
		System.out.println("Reading URL, Error occurred while sending push Notification!.." + mlfexception.getMessage());
		}catch(JSONException jsonexception){
		System.out.println("Message Format, Error occurred while sending push Notification!.." + jsonexception.getMessage());
		}catch (Exception exception) {
		System.out.println("Error occurred while sending push Notification!.." + exception.getMessage());
		}
		
		}
		
		}

    }

	@Override
	public void storeDeviceToken(Profile profile) {
		
		Loggers.loggerStart();
		query=sessionFactory.getCurrentSession().createQuery("update Profile set  finalToken=:finalToken where isActive='Y' and  smartId=:smartId  ");
		query.setParameter("finalToken", profile.getFinalToken());
		query.setParameter("smartId", profile.getSmartId());
		query.executeUpdate();
		
	}

}
