package edu.sru.cpsc.webshopping.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;

import edu.sru.cpsc.webshopping.domain.user.Applicant;
import edu.sru.cpsc.webshopping.domain.user.Message;
import edu.sru.cpsc.webshopping.repository.applicant.ApplicantRepository;
import edu.sru.cpsc.webshopping.repository.user.UserRepository;

@Controller
public class IndexController {
	private UserRepository userRepository;
	private ApplicantRepository appRepo;

	public IndexController(UserRepository userRepository,ApplicantRepository appRepo) {
		this.userRepository = userRepository;
		this.appRepo = appRepo;
	}
	
    //Mapping for the /index URL when initiated through Tomcat
    @RequestMapping({"/index"})
    public String showUserList(Model model) {
        model.addAttribute("users", userRepository.findAll());

	
        return "index";
    }
    
    @RequestMapping({"/"})
    public String showIndex(Model model) {
        model.addAttribute("users", userRepository.findAll());
    	Message mymessage = new Message();
		
		model.addAttribute("Message", mymessage);
        return "index";
    }
    
    @RequestMapping({"/emailverification"})
    public String showverify(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "emailverification";
    }
    
    @RequestMapping({"/login"})
	public String showLoginPage()
	{
		return "login";
	}
    @RequestMapping({"/missionStatement"})
	public String showMission()
	{
		return "missionStatement";
	}
    @RequestMapping({"/FAQ"})
	public String showFAQ()
	{
		return "FAQ";
	}
    @RequestMapping({"/application"})
	public String showApplication(Model model)
	{
		Applicant applicant = new Applicant();
		
		model.addAttribute("applicant", applicant);
		return "application";
	}
    @PostMapping("/apply")
	public String addApplication(@Valid Applicant applicant,BindingResult result, Model model){
    	if (result.hasErrors()) {
        return "application" ;
}
    	appRepo.save(applicant);
	    return "redirect:index";
	}
}
	

	
