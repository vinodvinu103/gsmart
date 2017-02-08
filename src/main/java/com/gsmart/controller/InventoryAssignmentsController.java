package com.gsmart.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gsmart.model.InventoryAssignments;
import com.gsmart.model.InventoryAssignmentsCompoundKey;
import com.gsmart.model.RolePermission;
import com.gsmart.model.Token;
import com.gsmart.services.InventoryAssignmentsServices;
import com.gsmart.services.TokenService;
import com.gsmart.util.Constants;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping(Constants.INVENTORYASSIGN)
public class InventoryAssignmentsController {
	@Autowired
	InventoryAssignmentsServices inventoryAssignmentsServices;
	@Autowired
	GetAuthorization getAuthorization;
	@Autowired
	TokenService tokenService;

	/*
	 * public static Logger logger =
	 * Logger.getLogger(InventoryAssignmentsController.class);
	 */

	/*
	 * @RequestMapping(value="/view", method= RequestMethod.POST,consumes =
	 * MediaType.APPLICATION_JSON_VALUE)
	 */

	/*
	 * @RequestMapping(method = RequestMethod.GET) public ResponseEntity
	 * <Map<String, Object>> getInventory(@RequestHeader HttpHeaders token,
	 * HttpSession httpSession) throws GSmartBaseException {
	 * Loggers.loggerStart(); String tokenNumber =
	 * token.get("Authorization").get(0); String str =
	 * getAuthorization.getAuthentication(tokenNumber, httpSession);
	 * str.length();
	 * 
	 * List<InventoryAssignments> inventoryList = null;
	 * 
	 * RolePermission modulePermission =
	 * getAuthorization.authorizationForGet(tokenNumber, httpSession);
	 * 
	 * Map<String, Object> permission = new HashMap<>();
	 * permission.put("modulePermission", modulePermission); if
	 * (modulePermission != null) { inventoryList =
	 * inventoryAssignmentsServices.getInventoryList();
	 * permission.put("inventoryList", inventoryList);
	 * 
	 * return new ResponseEntity<Map<String, Object>>(permission,
	 * HttpStatus.OK); } else { Loggers.loggerEnd(); return new
	 * ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK); }
	 * 
	 * }
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getInventory(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		Loggers.loggerStart();
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		List<InventoryAssignments> inventoryList = null;
		RolePermission modulePermission = getAuthorization.authorizationForGet(tokenNumber, httpSession);

		Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
		Map<String, Object> permission = new HashMap<>();
		permission.put("modulePermission", modulePermission);
		if (modulePermission != null) {
			inventoryList = inventoryAssignmentsServices.getInventoryList(tokenObj.getRole(),tokenObj.getHierarchy());
			permission.put("inventoryList", inventoryList);

			return new ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK);
		} else {
			Loggers.loggerEnd();
			return new ResponseEntity<Map<String, Object>>(permission, HttpStatus.OK);
		}

	}

	/*
	 * @RequestMapping (value="/add/{updSmartId}", method=RequestMethod.POST
	 * ,consumes=MediaType.APPLICATION_JSON_VALUE)
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addInventory(@RequestBody InventoryAssignments inventoryAssignments,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {

		Loggers.loggerStart(inventoryAssignments);

		Loggers.loggerValue("InventoryAssignments quantity", inventoryAssignments.getQuantity());

		IAMResponse resp = new IAMResponse();

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {
			Token tokenObj=(Token) httpSession.getAttribute("hierarchy");
			inventoryAssignments.setHierarchy(tokenObj.getHierarchy());
			
			InventoryAssignmentsCompoundKey ch = inventoryAssignmentsServices.addInventoryDetails(inventoryAssignments);

			if (ch != null) {
				resp.setMessage("success");
			} else {
				resp.setMessage("Already exists");
			}
			return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
		} else {
			resp.setMessage("Permission Denied");

		}
		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK);
	}
	/*
	 * @RequestMapping( method = RequestMethod.POST) public
	 * ResponseEntity<IAMResponse> addInventory(@RequestBody
	 * InventoryAssignments inventoryAssignments ,@RequestHeader HttpHeaders
	 * token, HttpSession httpSession ) throws GSmartBaseException {
	 * Loggers.loggerStart(inventoryAssignments); IAMResponse resp = new
	 * IAMResponse();
	 * 
	 * String tokenNumber = token.get("Authorization").get(0); String str =
	 * getAuthorization.getAuthentication(tokenNumber, httpSession);
	 * 
	 * str.length();
	 * 
	 * if (getAuthorization.authorizationForPost(tokenNumber, httpSession)) {
	 * InventoryAssignmentsCompoundKey
	 * ch=inventoryAssignmentsServices.addInventoryDetails(inventoryAssignments)
	 * ; if(ch !=null) resp.setMessage("success"); else
	 * resp.setMessage("Already exists");
	 * 
	 * return new ResponseEntity<IAMResponse>(resp, HttpStatus.OK); } else {
	 * resp.setMessage("Permission Denied"); Loggers.loggerEnd(); return new
	 * ResponseEntity<IAMResponse>(resp, HttpStatus.OK); }
	 * 
	 * }
	 */

	/*
	 * @RequestMapping (value="/edit/{updSmartId}" , method=RequestMethod.POST ,
	 * consumes=MediaType.APPLICATION_JSON_VALUE)
	 */
	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)

	public ResponseEntity<IAMResponse> editdeleteInventory(@RequestBody InventoryAssignments inventoryAssignments,
			@PathVariable("task") String task, @RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		IAMResponse myResponse = null;

		try {
			String tokenNumber = token.get("Authorization").get(0);

			String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

			str.length();
			InventoryAssignments ch = null;

			if (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {

				if (task.equals("edit")) {
					ch = inventoryAssignmentsServices.editInventoryDetails(inventoryAssignments);
					if (ch != null) {
						myResponse = new IAMResponse("success");

					} else {

						myResponse = new IAMResponse("DATA IS ALREADY EXIST");

					}
				} else if (task.equals("delete")) {
					inventoryAssignmentsServices.deleteInventoryDetails(inventoryAssignments);
					myResponse = new IAMResponse("success");
					return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
				}
			} else {
				myResponse = new IAMResponse("Permission Denied");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);
	}

}

/*
 * @RequestMapping(value="/{task}", method = RequestMethod.PUT) public
 * ResponseEntity<IAMResponse> editdeleteInventory(@RequestBody
 * InventoryAssignments inventoryAssignments,
 * 
 * @PathVariable("task") String task, @RequestHeader HttpHeaders token,
 * HttpSession httpSession)throws GSmartBaseException {
 * Loggers.loggerStart(inventoryAssignments); IAMResponse myResponse=null;
 * 
 * String tokenNumber = token.get("Authorization").get(0); String str =
 * getAuthorization.getAuthentication(tokenNumber, httpSession);
 * 
 * str.length(); InventoryAssignments ch=null; if
 * (getAuthorization.authorizationForPut(tokenNumber, task, httpSession)) {
 * if(task.equals("edit")){
 * 
 * ch=inventoryAssignmentsServices.editInventoryDetails(inventoryAssignments); }
 * if(ch!=null){ myResponse = new IAMResponse("success");
 * 
 * } else { myResponse=new IAMResponse("DATA IS ALREADY EXIST");
 * 
 * } } else if(task.equals("delete")){
 * inventoryAssignmentsServices.deleteInventoryDetails(inventoryAssignments);
 * myResponse = new IAMResponse("success"); Loggers.loggerEnd(); return new
 * ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK); } else { myResponse =
 * new IAMResponse("Permission Denied"); return new
 * ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK); } }
 * 
 * }
 */

/*
 * @RequestMapping(value="/delete/{updSmartId}", method=
 * RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE) public
 * ResponseEntity <Map<String,String>> deleteInventory(@RequestBody
 * InventoryAssignments inventoryAssignments , @PathVariable ("updSmartId") int
 * updSmartId) { Map<String,String> jsonMap = new HashMap<>(); try {
 * inventoryAssignments.setUpdSmartId(updSmartId);
 * inventoryAssignmentsServices.deleteInventoryDetails(inventoryAssignments);
 * jsonMap.put("result","deleted"); return new
 * ResponseEntity<Map<String,String>>(jsonMap, HttpStatus.OK); }catch(Exception
 * e) { e.printStackTrace(); jsonMap.put("result","error"); return new
 * ResponseEntity<Map<String,String>>(jsonMap, HttpStatus.OK); } }
 * 
 * }
 * 
 */