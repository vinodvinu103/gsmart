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

import com.gsmart.model.Banners;
import com.gsmart.services.ProfileServices;
import com.gsmart.util.GSmartBaseException;
import com.gsmart.util.GSmartServiceException;
import com.gsmart.util.GetAuthorization;
import com.gsmart.util.IAMResponse;
import com.gsmart.util.Loggers;

@Controller
@RequestMapping("/banner")
public class BannerController {

	@Autowired
	private GetAuthorization getAuthorization;

	@Autowired
	private ProfileServices profileServices;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getBanner(@RequestHeader HttpHeaders token, HttpSession httpSession)
			throws GSmartBaseException {

		Loggers.loggerStart("getBanner api started in Banner controller  ");
		String tokenNumber = null;
		List<Banners> bannerList = null;
		Map<String, Object> permissions = new HashMap<>();
		if (token.get("Authorization") != null) {
			tokenNumber = token.get("Authorization").get(0);
		} else {
			bannerList = profileServices.getBannerList();
			permissions.put("bannerList", bannerList);
			return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
		}

		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);
		str.length();

		bannerList = profileServices.getBannerList();
		permissions.put("bannerList", bannerList);
		Loggers.loggerEnd(
				"getBanner api ended in Banner controller  with bannerList with size of " + bannerList.size());
		return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);

	}

	/* consumes = MediaType.APPLICATION_JSON_VALUE */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<IAMResponse> addBannner(@RequestBody Banners banner, @RequestHeader HttpHeaders token,
			HttpSession httpSession) throws GSmartServiceException {

		Loggers.loggerStart(banner);
		IAMResponse rsp = null;

		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		profileServices.addBanner(banner);

		Loggers.loggerEnd();
		return new ResponseEntity<IAMResponse>(rsp, HttpStatus.OK);

	}

	@RequestMapping(value = "/{task}", method = RequestMethod.PUT)
	public ResponseEntity<IAMResponse> editDeleteBanner(@RequestBody Banners banner, @PathVariable("task") String task,
			@RequestHeader HttpHeaders token, HttpSession httpSession) throws GSmartBaseException {
		Loggers.loggerStart(banner);
		IAMResponse myResponse = null;
		String tokenNumber = token.get("Authorization").get(0);
		String str = getAuthorization.getAuthentication(tokenNumber, httpSession);

		str.length();

		if ("delete".equals(task)) {
			profileServices.deleteBanner(banner);
			myResponse = new IAMResponse("DATA IS ALREADY EXIST.");
		}
		Loggers.loggerEnd();

		return new ResponseEntity<IAMResponse>(myResponse, HttpStatus.OK);

	}

}
