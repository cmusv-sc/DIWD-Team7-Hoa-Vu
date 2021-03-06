package neo4j.controller;

import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import neo4j.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Service
public class QueryInterceptor extends HandlerInterceptorAdapter   {
	
	@Autowired UserService userService;
	HashMap<String,String> map;
	
	public QueryInterceptor(){
		 map = new HashMap<String,String>();
		 map.put("coAuthor", "getCoAuthor.html");
		 map.put("getcategorize", "categorize.html");
		 map.put("getsmallWorldTheory", "smallWorldTheory.html");
		 map.put("authorNetwork", "authornetwork.html");
		 map.put("graphTest", "QueryTest2.html");
		 map.put("paperCitation", "getCitation.html");
		 map.put("getcategorize2", "categorize2.html");
		 map.put("depthNetwork", "multiDepth.html");
		 map.put("topCitedPaper", "getTopCitedPaper.html");
		 map.put("topKrelated", "topKrelatedPapers.html");
		 map.put("followedself", "followedself");
		 map.put("followedother", "followedother");
		 map.put("gettimeline", "timeline.html");
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		System.out.println("---Before Method Execution---");
		String user = request.getUserPrincipal().getName();
		String temppath = request.getRequestURL().toString();
		int index = temppath.lastIndexOf('/');
		String lastpath = temppath.substring(index+1);
		
		if(map.containsKey(lastpath)){
			String path = temppath.substring(0,index+1) + map.get(lastpath) + "?" + URLDecoder.decode(request.getQueryString(), "UTF-8");;
			System.out.println(user);
	    	System.out.println(path);
	    	userService.removelastpath(user);
	    	userService.setlastpath(user, path);
	    }
		return true;
	}
} 