package edu.sru.cpsc.webshopping.controller;

import edu.sru.cpsc.webshopping.domain.market.MarketListing;
import edu.sru.cpsc.webshopping.domain.widgets.Appliance_Dryers;
import edu.sru.cpsc.webshopping.domain.widgets.Appliance_Microwave;
import edu.sru.cpsc.webshopping.domain.widgets.Appliance_Refrigerator;
import edu.sru.cpsc.webshopping.domain.widgets.Appliance_Washers;
import edu.sru.cpsc.webshopping.domain.widgets.Electronics_Computers;
import edu.sru.cpsc.webshopping.domain.widgets.Electronics_VideoGames;
import edu.sru.cpsc.webshopping.domain.widgets.LawnCare_LawnMower;
import edu.sru.cpsc.webshopping.domain.widgets.Vehicle_Car;
import edu.sru.cpsc.webshopping.domain.widgets.Widget;
import edu.sru.cpsc.webshopping.domain.widgets.Widget_Appliance;
import edu.sru.cpsc.webshopping.domain.widgets.Widget_Electronics;
import edu.sru.cpsc.webshopping.domain.widgets.Widget_LawnCare;
import edu.sru.cpsc.webshopping.domain.widgets.Widget_Vehicles;
import edu.sru.cpsc.webshopping.repository.market.MarketListingRepository;
import edu.sru.cpsc.webshopping.repository.user.UserRepository;
import edu.sru.cpsc.webshopping.repository.widgets.ApplianceDryersRepository;
import edu.sru.cpsc.webshopping.repository.widgets.*;
import edu.sru.cpsc.webshopping.repository.widgets.WidgetRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.JpaSort.Path;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Controller for creating widgets and listings.
 * @author NICK
 *
 */
@Controller
public class AddWidgetController {
	
	WidgetRepository widgetRepository;
	ApplianceDryersRepository dryerRepository;
	ApplianceMicrowaveRepository microwaveRepository;
	ApplianceRefrigeratorRepository fridgeRepository;
	ApplianceWashersRepository washerRepository;
	ElectronicsComputersRepository computerRepository;
	ElectronicsVideoGamesRepository videoGameRepository;
	VehicleCarRepository carRepository;
	LawnCareLawnMowerRepository mowerRepository;
	WidgetLawnCareRepository lawnCareRepository;
	WidgetApplianceRepository applianceRepository;
	WidgetElectronicsRepository electronicsRepository;
	WidgetVehiclesRepository vehicleRepository;
	MarketListingRepository marketListingRepos;
	WidgetController widgetController;
	UserController userController;
	MarketListingDomainController marketListingController;
	UserRepository userRepo;
	Widget widget;
	Widget_Appliance appliances;
	Widget_Electronics electronics;
	Widget_Vehicles vehicle;
	Widget_LawnCare lawnCare;
	Appliance_Dryers dryer;
	Appliance_Washers washer;
	Appliance_Microwave microwave;
	Appliance_Refrigerator refridgerator;
	Electronics_Computers computer;
	Electronics_VideoGames videoGame;
	Vehicle_Car car;
	LawnCare_LawnMower mower;
	MarketListing marketListing;
	private String category;
	private String subCategory;
	private String tempImageName;
	private final String UPLOAD_DIR = "/static/images/userImages";
	
	AddWidgetController(WidgetRepository widgetRepository, ApplianceDryersRepository dryerRepository, 
			ApplianceMicrowaveRepository microwaveRepository, ApplianceRefrigeratorRepository fridgeRepository, 
			ApplianceWashersRepository washerRepository, ElectronicsComputersRepository computerRepository, 
			ElectronicsVideoGamesRepository videoGameRepository, VehicleCarRepository carRepository, 
			WidgetApplianceRepository applianceRepository, WidgetLawnCareRepository lawnCareRepository, 
			WidgetElectronicsRepository electronicsRepository, WidgetVehiclesRepository vehicleRepository, 
			MarketListingRepository marketListingRepos, WidgetController widgetController, UserController userController, 
			MarketListingDomainController marketListingController, UserRepository userRepo, LawnCareLawnMowerRepository mowerRepository)
	{
		this.widgetRepository = widgetRepository;
		this.applianceRepository = applianceRepository;
		this.electronicsRepository = electronicsRepository;
		this.vehicleRepository = vehicleRepository;
		this.dryerRepository = dryerRepository;
		this.microwaveRepository = microwaveRepository;
		this.fridgeRepository = fridgeRepository;
		this.washerRepository = washerRepository;
		this.computerRepository = computerRepository;
		this.videoGameRepository = videoGameRepository;
		this.carRepository = carRepository;
		this.mowerRepository = mowerRepository;
		this.marketListingRepos = marketListingRepos;
		this.widgetController = widgetController;
		this.userController = userController;
		this.marketListingController = marketListingController;
		this.userRepo = userRepo;
		this.mowerRepository = mowerRepository;
		this.lawnCareRepository = lawnCareRepository;
	}
	
	@RequestMapping("/addWidget")
	public String addWidget(Model model)
	{
		return "addWidget";
	}
	
	@RequestMapping("/createWidget")
	public String createWidget(Model model, @RequestParam("category") String category)
	{
		this.category = category;
		String url = "";
		if(category.contentEquals("appliance")) {
			url = "redirect:addAppliance";
		}
		else if (category.contentEquals("electronic"))
		{
			url = "redirect:addElectronic";
		}
		else if (category.contentEquals("lawnCare"))
		{
			url = "redirect:addLawnCare";
		}
		else if (category.contentEquals("vehicle"))
		{
			url = "redirect:addVehicle";
		}
		
		return url;
	}
	
	@RequestMapping("/addAppliance")
	public String addAppliance(Model model)
	{
		return "addAppliance";
	}
	
	@RequestMapping("/addElectronic")
	public String addElectronic(Model model)
	{
		return "addElectronic";
	}
	
	@RequestMapping("/addLawnCare")
	public String addLawnCare(Model model)
	{
		return "addLawnCare";
	}
	
	@RequestMapping("/addVehicle")
	public String addVehicle(Model model)
	{
		return "addVehicle";
	}
	
	@RequestMapping("/createAppliance")
	public String createAppliance(Model model, @RequestParam("subCategory") String subCategory)
	{
		this.subCategory = subCategory;
		String url = "";
		if(subCategory.contentEquals("dryer"))
		{
			url = "redirect:addDryer";
		}
		else if (subCategory.contentEquals("microwave"))
		{
			url = "redirect:addMicrowave";
		}
		else if (subCategory.contentEquals("refridgerator"))
		{
			url = "redirect:addFridge";
		}
		else if (subCategory.contentEquals("washer"))
		{
			url = "redirect:addWasher";
		}
		return url;
	}
	
	@RequestMapping("/createElectronic")
	public String createElectronic(Model model, @RequestParam("subCategory") String subCategory)
	{
		this.subCategory = subCategory;
		String url = "";
		
		if (subCategory.contentEquals("computer"))
		{
			url = "redirect:addComputer";
		}
		else if (subCategory.contentEquals("videoGame"))
		{
			url = "redirect:addVideoGame";
		}
		return url;
	}
	
	@RequestMapping("/createLawnCare")
	public String createLawnCare(Model model, @RequestParam("subCategory") String subCategory)
	{
		this.subCategory = subCategory;
		String url = "";
		if (subCategory.contentEquals("lawnMower"))
		{
			url = "redirect:addLawnMower";
		}
		return url;
	}
	
	@RequestMapping("/createVehicle")
	public String createVehicle(Model model, @RequestParam("subCategory") String subCategory)
	{
		this.subCategory = subCategory;
		String url = "";
		if(subCategory.contentEquals("car"))
		{
			url = "redirect:addCar";
		}
		return url;
	}
	
	@RequestMapping("/addDryer")
	public String addDryer(Model model)
	{
		dryer = new Appliance_Dryers();
		model.addAttribute("dryer", dryer);
		return "addDryer";
	}
	
	@RequestMapping("addMicrowave")
	public String addMicrowave(Model model)
	{
		microwave = new Appliance_Microwave();
		model.addAttribute("microwave", microwave);
		return "addMicrowave";
	}
	
	@RequestMapping("addFridge")
	public String addFridge(Model model)
	{
		refridgerator = new Appliance_Refrigerator();
		model.addAttribute("refridgerator", refridgerator);
		return "addFridge";
	}
	
	@RequestMapping("addWasher")
	public String addWasher(Model model)
	{
		washer = new Appliance_Washers();
		model.addAttribute("washer", washer);
		return "addWasher";
	}
	
	@RequestMapping("addComputer")
	public String addComputer(Model model)
	{
		computer = new Electronics_Computers();
		model.addAttribute("computer", computer);
		return "addComputer";
	}
	
	@RequestMapping("addVideoGame")
	public String addVideoGame(Model model)
	{
		videoGame = new Electronics_VideoGames();
		model.addAttribute("videoGame", videoGame);
		return "addVideoGame";
	}
	
	@RequestMapping("addLawnMower")
	public String addLawnMower(Model model)
	{
		mower = new LawnCare_LawnMower();
		model.addAttribute("mower", mower);
		return "addLawnMower";
	}
	
	@RequestMapping("addCar")
	public String addCar(Model model)
	{
		car = new Vehicle_Car();
		model.addAttribute("car", car);
		return "addCar";
	}
	
	@RequestMapping("createDryer")
	public String createDryer(Model model, @ModelAttribute Appliance_Dryers dryer, BindingResult result)
	{
		model.addAttribute("name", dryer);
		model.addAttribute("description", dryer);
		dryer.setCategory(category);
		dryer.setSubCategory(subCategory);
		model.addAttribute("length", dryer);
		model.addAttribute("width", dryer);
		model.addAttribute("height", dryer);
		model.addAttribute("color", dryer);
		model.addAttribute("itemCondition", dryer);
		model.addAttribute("model", dryer);
		model.addAttribute("brand", dryer);
		model.addAttribute("material", dryer);
		widgetController.addDryer(dryer, result);
		this.dryer = dryer;
		widget = dryer;
		return "redirect:createListing";
	}
	
	@RequestMapping("createMicrowave")
	public String createMicrowave(Model model, @ModelAttribute Appliance_Microwave microwave, BindingResult result)
	{
		model.addAttribute("name", microwave);
		model.addAttribute("description", microwave);
		microwave.setCategory(category);
		microwave.setSubCategory(subCategory);
		model.addAttribute("length", microwave);
		model.addAttribute("width", microwave);
		model.addAttribute("height", microwave);
		model.addAttribute("color", microwave);
		model.addAttribute("itemCondition", microwave);
		model.addAttribute("model", microwave);
		model.addAttribute("brand", microwave);
		model.addAttribute("material", microwave);
		widgetController.addMicrowave(microwave, result);
		this.microwave = microwave;
		widget = microwave;
		return "redirect:createListing";
	}
	@RequestMapping("createFridge")
	public String createFridge(Model model, @ModelAttribute Appliance_Refrigerator fridge, BindingResult result)
	{
		model.addAttribute("name", fridge);
		model.addAttribute("description", fridge);
		fridge.setCategory(category);
		fridge.setSubCategory(subCategory);
		model.addAttribute("length", fridge);
		model.addAttribute("width", fridge);
		model.addAttribute("height", fridge);
		model.addAttribute("color", fridge);
		model.addAttribute("itemCondition", fridge);
		model.addAttribute("model", fridge);
		model.addAttribute("brand", fridge);
		model.addAttribute("material", fridge);
		widgetController.addRefrigerator(fridge, result);
		this.refridgerator = fridge;
		widget = fridge;
		return "redirect:createListing";
	}
	@RequestMapping("createWasher")
	public String createWasher(Model model, @ModelAttribute Appliance_Washers washer, BindingResult result)
	{
		model.addAttribute("name", washer);
		model.addAttribute("description", washer);
		washer.setCategory(category);
		washer.setSubCategory(subCategory);
		model.addAttribute("length", washer);
		model.addAttribute("width", washer);
		model.addAttribute("height", washer);
		model.addAttribute("color", washer);
		model.addAttribute("itemCondition", washer);
		model.addAttribute("model", washer);
		model.addAttribute("brand", washer);
		model.addAttribute("material", washer);
		widgetController.addWasher(washer, result);
		this.washer = washer;
		widget = washer;
		return "redirect:createListing";
	}
	
	@RequestMapping("createComputer")
	public String createComputer(Model model, @RequestParam("storageSize") String storageSize, @ModelAttribute Electronics_Computers computer, BindingResult result)
	{
		model.addAttribute("name", computer);
		model.addAttribute("description", computer);
		computer.setCategory(category);
		computer.setSubCategory(subCategory);
		model.addAttribute("officeUse", computer);
		model.addAttribute("entertainmentUse", computer);
		model.addAttribute("memory", computer);
		model.addAttribute("storage", computer);
		computer.setStorage(computer.getStorage() + storageSize);
		model.addAttribute("processor", computer);
		model.addAttribute("gpu", computer);
		widgetController.addComputer(computer, result);
		this.computer = computer;
		widget = computer;
		return "redirect:createListing";
	}
	
	@RequestMapping("createVideoGame")
	public String createVideoGame(Model model, @ModelAttribute Electronics_VideoGames videoGame, BindingResult result)
	{
		model.addAttribute("name", videoGame);
		model.addAttribute("description", videoGame);
		videoGame.setCategory(category);
		videoGame.setSubCategory(subCategory);
		model.addAttribute("officeUse", videoGame);
		model.addAttribute("entertainmentUse", videoGame);
		model.addAttribute("developer", videoGame);
		model.addAttribute("title", videoGame);
		model.addAttribute("console", videoGame);
		model.addAttribute("itemCondition", videoGame);
		widgetController.addVideoGame(videoGame, result);
		this.videoGame = videoGame;
		widget = videoGame;
		return "redirect:createListing";
	}
	
	@RequestMapping("createLawnMower")
	public String createLawnMower(Model model, @ModelAttribute LawnCare_LawnMower mower, BindingResult result)
	{
		model.addAttribute("name", mower);
		model.addAttribute("description", mower);
		mower.setCategory(category);
		mower.setSubCategory(subCategory);
		model.addAttribute("yardSize", mower);
		model.addAttribute("toolType", mower);
		model.addAttribute("brand", mower);
		model.addAttribute("powerSource", mower);
		model.addAttribute("bladeWidth", mower);
		widgetController.addLawnMower(mower, result);
		this.mower = mower;
		widget = mower;
		return "redirect:createListing";
	}
	
	@RequestMapping("createCar")
	public String createCar(Model model, @ModelAttribute Vehicle_Car car, BindingResult result)
	{
		model.addAttribute("name", car);
		model.addAttribute("description", car);
		car.setCategory(category);
		car.setSubCategory(subCategory);
		model.addAttribute("terrain", car);
		model.addAttribute("roadSafe", car);
		model.addAttribute("itemCondition", car);
		model.addAttribute("wheelDrive", car);
		model.addAttribute("transmisionType", car);
		model.addAttribute("model", car);
		model.addAttribute("make", car);
		model.addAttribute("year", car);
		widgetController.addCar(car, result);
		this.car = car;
		widget = car;
		return "redirect:createListing";
	}
	
	@RequestMapping("/createListing")
	public String createListing(Model model)
	{
		marketListing = new MarketListing();
		model.addAttribute("listing", marketListing);
		return "createListing";
	}
	
	@RequestMapping("/addListing")
	public String addListing(Model model, @RequestParam("imageName") MultipartFile file, RedirectAttributes attributes, @ModelAttribute MarketListing marketListing, BindingResult result)
	{
		model.addAttribute("pricePerItem", marketListing);
		model.addAttribute("qtyAvailable", marketListing);
		marketListing.setSeller(userController.getCurrently_Logged_In());
		marketListing.setWidgetSold(widget);
		marketListing.setDeleted(false);
		/*if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a file to upload.");
			return "redirect:/";
		}
		tempImageName = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			java.nio.file.Path path = Paths.get(UPLOAD_DIR + tempImageName);
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		widget.setImageName(tempImageName);*/
		marketListingController.addMarketListing(marketListing);
		return "redirect:homePage";
	}
}

