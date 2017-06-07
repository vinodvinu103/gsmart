/*package com.gsmart.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gsmart.model.MessageDetails;
import com.gsmart.model.Profile;
import com.gsmart.services.BeanFactory;
import com.gsmart.services.ContactServices;
import com.gsmart.services.ProfileServices;
import com.gsmart.services.SearchService;
import com.gsmart.util.Loggers;


@Controller
@RequestMapping(value = "/contactSearch")
public class ContactStructureController {
	@Autowired
	private BeanFactory beanFactory;

	@Autowired
	private SearchService searchService;

	@Autowired
	private ProfileServices profileServices;

	@Autowired
	private ContactServices contactServices;

	


	@RequestMapping(value = "/info/{smartId}")
	public ResponseEntity<Map<String, Object>> employeeInfoController(@PathVariable("smartId") String smartId) {

		Loggers.loggerStart();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			Profile profile = profileServices.getProfileDetails(smartId);

			Map<String, Profile> beanFactory1 = beanFactory.getBeanFactory();

			ArrayList<Profile> childList = searchService.searchEmployeeInfo(smartId, beanFactory1);
			if (childList.size() != 0) {
				profile.setChildFlag(true);
			}

			List<MessageDetails> allMessages = contactServices.viewAllMessages();
		
			ArrayList<MessageDetails> childMessage=new ArrayList<>();
			for (int i = 0; i < childList.size(); i++){
				for (int j = 0; j < allMessages.size(); j++){
					if(childList.get(i).getSmartId().equals(allMessages.get(j).getSmartId())){
						childMessage.add(allMessages.get(j));
					}
				}
			}
			Map<String, ArrayList<MessageDetails>> msgMap=new HashMap<>();
			msgMap.put(smartId, childMessage);

			HashMap<String, ArrayList<MessageDetails>> childofchild = new HashMap<String, ArrayList<MessageDetails>>();
			Set<String> key = beanFactory1.keySet();
			ArrayList<MessageDetails> checking = null;
			int flag = 0;
			for (int i = 0; i < childList.size(); i++) 
			{
				checking = new ArrayList<MessageDetails>();
				for (String j : key) 
				{
					Profile p = (Profile) beanFactory1.get(j);
					if (p != null && p.getReportingManagerId() != null)
						if (p.getReportingManagerId() == childList.get(i).getSmartId() )
						{
							if (p.getSmartId()!= childList.get(i).getSmartId())
							{
								//childList.get(i).setChildFlag(true);
								for(int k=0;k<childMessage.size();k++)
								{
									if (childList.get(i).getSmartId()==childMessage.get(k).getSmartId())
									{
										childMessage.get(k).setChildFlag(1);
										checking.add(childMessage.get(k));
										flag = 1;
									}
								}
							}
						}
				}
				flag = 0; 
				if (flag != 0) {
					childofchild.put(childList.get(i).getSmartId(), checking);
					flag = 0; 
				}
			}

			Map<String, Object> parentInfo = searchService.getParentInfo(smartId);
			if (parentInfo.get("parentProfile") != null && parentInfo.get("reportingProfiles") != null) {
				profile.setParentFlag(true);
			}
			Loggers.loggerStart();
			Map<String, Object> resultmap = new HashMap<String, Object>();
			resultmap.put("selfProfile", profile);
			resultmap.put("parentInfo", parentInfo);
			resultmap.put("childList", childList);
			resultmap.put("childMessages", msgMap);
//		    resultmap.put("childOfChildList", childofchild);
			jsonMap.put("result", allMessages);
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
		} 
		catch (Exception e) 
		{
			Map<String, Object> jsonDetails = new HashMap<>();
			jsonDetails.put("result", null);
			return new ResponseEntity<Map<String, Object>>(jsonMap, HttpStatus.OK);
		}
	}
}
*/