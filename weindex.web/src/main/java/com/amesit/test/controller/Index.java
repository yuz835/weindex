package com.amesit.test.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/index")
public class Index {
	@Autowired 
	private HttpServletRequest request;

	protected int i;
	
	@RequestMapping
	public ModelAndView index() {
		System.out.println("hehe");
		ModelAndView view = new ModelAndView("index/test");
		view.addObject("title", "mytitle");
		view.addObject("content", "<h1>hehehehe</h1>");
		return view;
	}
	
	@RequestMapping("/foo1")
	public ModelAndView foo() {
		ModelAndView view = new ModelAndView();
		view.addObject("title", "mytitle");
		view.addObject("test", "ksdjflksdj");
		return view;
	}
}
