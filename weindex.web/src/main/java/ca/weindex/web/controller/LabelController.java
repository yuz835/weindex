package ca.weindex.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ca.weindex.common.model.Blog;
import ca.weindex.common.model.Label;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;
import ca.weindex.services.LabelService;

@Controller
@RequestMapping("/label")
public class LabelController {
	@Autowired
	private LabelService labelService;

	@RequestMapping("/{id}/offer")
	public ModelAndView offer(@PathVariable int id, Pagination page) {
		SearchResult<Offer> result = labelService.getOfferByLabelId(id, page);
		ModelAndView view = new ModelAndView("label/offer");
		view.addObject("result", result);
		view.addObject("labelId", id);
		List<Label> labelList = labelService.getLabel();
		view.addObject("labels", labelList);
		return view;
	}

	@RequestMapping("/{id}/shop")
	public ModelAndView shop(@PathVariable int id, Pagination page) {
		SearchResult<Shop> result = labelService.getShopByLabelId(id, page);
		ModelAndView view = new ModelAndView("label/shop");
		view.addObject("result", result);
		view.addObject("labelId", id);
		List<Label> labelList = labelService.getLabel();
		view.addObject("labels", labelList);
		return view;
	}

	@RequestMapping("/{id}/blog")
	public ModelAndView blog(@PathVariable int id, Pagination page) {
		SearchResult<Blog> result = labelService.getBlogByLabelId(id, page);
		ModelAndView view = new ModelAndView("label/blog");
		view.addObject("result", result);
		view.addObject("labelId", id);
		List<Label> labelList = labelService.getLabel();
		view.addObject("labels", labelList);
		return view;
	}
	
	@RequestMapping("/index")
	public ModelAndView index() {
		List<Label> labelList = labelService.getLabel();
		ModelAndView view = new ModelAndView("label/index");
		view.addObject("labelList", labelList);
		return view;
	}
}
