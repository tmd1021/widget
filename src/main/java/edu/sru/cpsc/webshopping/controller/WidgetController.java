package edu.sru.cpsc.webshopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.sru.cpsc.webshopping.domain.widgets.Widget;
import edu.sru.cpsc.webshopping.repository.widgets.WidgetRepository;
import edu.sru.cpsc.webshopping.repository.widgets.*;
import edu.sru.cpsc.webshopping.domain.widgets.*;

/**
 * Controller for interacting with the Widgets database table
 */
@RestController
public class WidgetController {
	private WidgetRepository<Widget> widgetRepository;
	private ApplianceDryersRepository<Appliance_Dryers> dryerRepository;
	private ApplianceMicrowaveRepository<Appliance_Microwave> microwaveRepository;
	private ApplianceRefrigeratorRepository<Appliance_Refrigerator> refrigeratorRepository;
	private ApplianceWashersRepository<Appliance_Washers> washerRepository;
	private ElectronicsComputersRepository<Electronics_Computers> computerRepository;
	private ElectronicsVideoGamesRepository<Electronics_VideoGames> videoGameRepository;
	private LawnCareLawnMowerRepository<LawnCare_LawnMower> mowerRepository;
	private VehicleCarRepository<Vehicle_Car> carRepository;
	private WidgetApplianceRepository<Widget_Appliance> applianceRepository;
	private WidgetElectronicsRepository<Widget_Electronics> electronicRepository;
	private WidgetLawnCareRepository<Widget_LawnCare> lawnCareRepository;
	private WidgetVehiclesRepository<Widget_Vehicles> vehicleRepository;
	
	public WidgetController(WidgetRepository widgetRepository, ApplianceDryersRepository dryerRepository, ApplianceMicrowaveRepository microwaveRepository,
			ApplianceRefrigeratorRepository refrigeratorRepository, ApplianceWashersRepository washerRepository, ElectronicsComputersRepository computerRepository, 
			ElectronicsVideoGamesRepository videoGameRepository, LawnCareLawnMowerRepository mowerRepository, VehicleCarRepository carRepository, WidgetApplianceRepository applianceRepository
			,WidgetElectronicsRepository electronicRepository, WidgetLawnCareRepository lawnCareRepository, WidgetVehiclesRepository vehicleRepository) { 
		this.widgetRepository = widgetRepository; 
		this.dryerRepository = dryerRepository;
		this.microwaveRepository = microwaveRepository;
		this.refrigeratorRepository = refrigeratorRepository;
		this.washerRepository = washerRepository;
		this.computerRepository = computerRepository;
		this.videoGameRepository = videoGameRepository;
		this.mowerRepository = mowerRepository;
		this.carRepository = carRepository;
		this.applianceRepository = applianceRepository;
		this.electronicRepository = electronicRepository;
		this.lawnCareRepository = lawnCareRepository;
		this.vehicleRepository = vehicleRepository;
		}
	
	// Widget CRUD functions
	@RequestMapping("add-widget") 
	public Widget addWidget(@Validated Widget widget, BindingResult result) {
		return widgetRepository.save(widget);
	}
	
	@RequestMapping({"get-widget"})
	public Widget getWidget(@PathVariable("id") long id)  {
		Widget widget = widgetRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID specified to get-widget"));
		return widget;
	}
	
	@RequestMapping({"/get-all-widgets"}) 
	public Iterable<Widget> getAllWidgets()
	{
		Iterable<Widget> widgets = widgetRepository.findAll();
		return widgets;
	}
	
	@PostMapping({"update-widget/{id}"}) 
	public void updateWidget(@Validated Widget widget, BindingResult result, @PathVariable("id") long id) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException("BindResult error in WidgetController.updateWidget");
		}
		widgetRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for updating widget in WidgetController.updateWidget"));
		widgetRepository.save(widget);
	}
	
	@GetMapping({"delete-widget/{id}"}) 
	public void deleteWidget(@PathVariable("id") long id) {
		Widget widget = widgetRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for deleting widget."));
		widgetRepository.delete(widget);
	}
	
	@GetMapping({"add-dryer"})
	public void addDryer(@Validated Appliance_Dryers dryer, BindingResult result)
	{
		if (result.hasErrors()) {
			throw new IllegalArgumentException("Error adding new widget in WidgetController.addWidget");
		}
		dryerRepository.save(dryer);
	}
	@RequestMapping({"get-dryer"})
	public Appliance_Dryers getDryer(@PathVariable("id") long id)  {
		Appliance_Dryers dryer = dryerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID specified to get-widget"));
		return dryer;
	}
	
	@RequestMapping({"/get-all-dryers"}) 
	public Iterable<Appliance_Dryers> getAllDryers()
	{
		Iterable<Appliance_Dryers> dryers = dryerRepository.findAll();
		return dryers;
	}
	
	@PostMapping({"update-dryer/{id}"}) 
	public void updateDryer(@Validated Appliance_Dryers dryer, BindingResult result, @PathVariable("id") long id) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException("BindResult error in WidgetController.updateWidget");
		}
		dryerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for updating widget in WidgetController.updateWidget"));
		dryerRepository.save(dryer);
	}
	
	@GetMapping({"delete-dryer/{id}"}) 
	public void deleteDryer(@PathVariable("id") long id) {
		Appliance_Dryers dryer = dryerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for deleting widget."));
		dryerRepository.delete(dryer);
	}
	@GetMapping({"add-microwave"})
	public void addMicrowave(@Validated Appliance_Microwave microwave, BindingResult result)
	{
		if (result.hasErrors()) {
			throw new IllegalArgumentException("Error adding new widget in WidgetController.addWidget");
		}
		microwaveRepository.save(microwave);
	}
	@RequestMapping({"get-microwave"})
	public Appliance_Microwave getMicrowave(@PathVariable("id") long id)  {
		Appliance_Microwave microwave = microwaveRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID specified to get-widget"));
		return microwave;
	}
	
	@RequestMapping({"/get-all-microwaves"}) 
	public Iterable<Appliance_Microwave> getAllMicrowaves()
	{
		Iterable<Appliance_Microwave> microwaves = microwaveRepository.findAll();
		return microwaves;
	}
	
	@PostMapping({"update-microwave/{id}"}) 
	public void updateMicrowave(@Validated Appliance_Microwave microwave, BindingResult result, @PathVariable("id") long id) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException("BindResult error in WidgetController.updateWidget");
		}
		microwaveRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for updating widget in WidgetController.updateWidget"));
		microwaveRepository.save(microwave);
	}
	
	@GetMapping({"delete-microwave/{id}"}) 
	public void deleteMicrowave(@PathVariable("id") long id) {
		Appliance_Microwave microwave = microwaveRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for deleting widget."));
		microwaveRepository.delete(microwave);
	}
	@GetMapping({"add-refrigerator"})
	public void addRefrigerator(@Validated Appliance_Refrigerator fridge, BindingResult result)
	{
		if (result.hasErrors()) {
			throw new IllegalArgumentException("Error adding new widget in WidgetController.addWidget");
		}
		refrigeratorRepository.save(fridge);
	}
	@RequestMapping({"get-refrigerator"})
	public Appliance_Refrigerator getRefrigerator(@PathVariable("id") long id)  {
		Appliance_Refrigerator refrigerator = refrigeratorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID specified to get-widget"));
		return refrigerator;
	}
	
	@RequestMapping({"/get-all-refrigerators"}) 
	public Iterable<Appliance_Refrigerator> getAllRefrigerators()
	{
		Iterable<Appliance_Refrigerator> refrigerators = refrigeratorRepository.findAll();
		return refrigerators;
	}
	
	@PostMapping({"update-refrigerator/{id}"}) 
	public void updateRefrigerator(@Validated Appliance_Refrigerator refrigerator, BindingResult result, @PathVariable("id") long id) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException("BindResult error in WidgetController.updateWidget");
		}
		refrigeratorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for updating widget in WidgetController.updateWidget"));
		refrigeratorRepository.save(refrigerator);
	}
	
	@GetMapping({"delete-refrigerator/{id}"}) 
	public void deleteRefrigerator(@PathVariable("id") long id) {
		Appliance_Refrigerator refrigerator = refrigeratorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for deleting widget."));
		refrigeratorRepository.delete(refrigerator);
	}
	@GetMapping({"add-washer"})
	public void addWasher(@Validated Appliance_Washers washer, BindingResult result)
	{
		if (result.hasErrors()) {
			throw new IllegalArgumentException("Error adding new widget in WidgetController.addWidget");
		}
		washerRepository.save(washer);
	}
	@RequestMapping({"get-washer"})
	public Appliance_Washers getWasher(@PathVariable("id") long id)  {
		Appliance_Washers washer = washerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID specified to get-widget"));
		return washer;
	}
	
	@RequestMapping({"/get-all-washers"}) 
	public Iterable<Appliance_Washers> getAllWashers()
	{
		Iterable<Appliance_Washers> washers = washerRepository.findAll();
		return washers;
	}
	
	@PostMapping({"update-washer/{id}"}) 
	public void updateWasher(@Validated Appliance_Washers washer, BindingResult result, @PathVariable("id") long id) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException("BindResult error in WidgetController.updateWidget");
		}
		washerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for updating widget in WidgetController.updateWidget"));
		washerRepository.save(washer);
	}
	
	@GetMapping({"delete-washer/{id}"}) 
	public void deleteWasher(@PathVariable("id") long id) {
		Appliance_Washers washer = washerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for deleting widget."));
		washerRepository.delete(washer);
	}
	
	@GetMapping({"add-comptuer"})
	public void addComputer(@Validated Electronics_Computers computer, BindingResult result)
	{
		if (result.hasErrors()) {
			throw new IllegalArgumentException("Error adding new widget in WidgetController.addWidget");
		}
		computerRepository.save(computer);
	}
	@RequestMapping({"get-computer"})
	public Electronics_Computers getComputer(@PathVariable("id") long id)  {
		Electronics_Computers computer = computerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID specified to get-widget"));
		return computer;
	}
	
	@RequestMapping({"/get-all-computers"}) 
	public Iterable<Electronics_Computers> getAllComputers()
	{
		Iterable<Electronics_Computers> computers = computerRepository.findAll();
		return computers;
	}
	
	@PostMapping({"update-computer/{id}"}) 
	public void updateComputer(@Validated Electronics_Computers computer, BindingResult result, @PathVariable("id") long id) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException("BindResult error in WidgetController.updateWidget");
		}
		computerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for updating widget in WidgetController.updateWidget"));
		computerRepository.save(computer);
	}
	
	@GetMapping({"delete-computer/{id}"}) 
	public void deleteComputer(@PathVariable("id") long id) {
		Electronics_Computers computer = computerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for deleting widget."));
		computerRepository.delete(computer);
	}
	@GetMapping({"add-videoGame"})
	public void addVideoGame(@Validated Electronics_VideoGames videoGame, BindingResult result)
	{
		if (result.hasErrors()) {
			throw new IllegalArgumentException("Error adding new widget in WidgetController.addWidget");
		}
		videoGameRepository.save(videoGame);
	}
	@RequestMapping({"get-videoGame"})
	public Electronics_VideoGames getVideoGame(@PathVariable("id") long id)  {
		Electronics_VideoGames videoGame = videoGameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID specified to get-widget"));
		return videoGame;
	}
	
	@RequestMapping({"/get-all-videoGames"}) 
	public Iterable<Electronics_VideoGames> getAllVideoGames()
	{
		Iterable<Electronics_VideoGames> videoGames = videoGameRepository.findAll();
		return videoGames;
	}
	
	@PostMapping({"update-videoGame/{id}"}) 
	public void updateVideoGame(@Validated Electronics_VideoGames videoGame, BindingResult result, @PathVariable("id") long id) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException("BindResult error in WidgetController.updateWidget");
		}
		videoGameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for updating widget in WidgetController.updateWidget"));
		videoGameRepository.save(videoGame);
	}
	
	@GetMapping({"delete-videoGame/{id}"}) 
	public void deleteVideoGame(@PathVariable("id") long id) {
		Electronics_VideoGames videoGame = videoGameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for deleting widget."));
		videoGameRepository.delete(videoGame);
	}
	
	@GetMapping({"add-mower"})
	public void addLawnMower(@Validated LawnCare_LawnMower mower, BindingResult result)
	{
		if (result.hasErrors()) {
			throw new IllegalArgumentException("Error adding new widget in WidgetController.addWidget");
		}
		mowerRepository.save(mower);
	}
	@RequestMapping({"get-mower"})
	public LawnCare_LawnMower getMower(@PathVariable("id") long id)  {
		LawnCare_LawnMower mower = mowerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID specified to get-widget"));
		return mower;
	}
	
	@RequestMapping({"/get-all-mowers"}) 
	public Iterable<LawnCare_LawnMower> getAllMowers()
	{
		Iterable<LawnCare_LawnMower> mowers = mowerRepository.findAll();
		return mowers;
	}
	
	@PostMapping({"update-mower/{id}"}) 
	public void updateMower(@Validated LawnCare_LawnMower mower, BindingResult result, @PathVariable("id") long id) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException("BindResult error in WidgetController.updateWidget");
		}
		mowerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for updating widget in WidgetController.updateWidget"));
		mowerRepository.save(mower);
	}
	
	@GetMapping({"delete-mower/{id}"}) 
	public void deleteMower(@PathVariable("id") long id) {
		LawnCare_LawnMower mower = mowerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for deleting widget."));
		mowerRepository.delete(mower);
	}
	
	@GetMapping({"add-car"})
	public void addCar(@Validated Vehicle_Car car, BindingResult result)
	{
		if (result.hasErrors()) {
			throw new IllegalArgumentException("Error adding new widget in WidgetController.addWidget");
		}
		carRepository.save(car);
	}
	@RequestMapping({"get-car"})
	public Vehicle_Car getCar(@PathVariable("id") long id)  {
		Vehicle_Car car = carRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID specified to get-widget"));
		return car;
	}
	
	@RequestMapping({"/get-all-car"}) 
	public Iterable<Vehicle_Car> getAllCars()
	{
		Iterable<Vehicle_Car> car = carRepository.findAll();
		return car;
	}
	
	@PostMapping({"update-car/{id}"}) 
	public void updateCar(@Validated Vehicle_Car car, BindingResult result, @PathVariable("id") long id) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException("BindResult error in WidgetController.updateWidget");
		}
		carRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for updating widget in WidgetController.updateWidget"));
		carRepository.save(car);
	}
	
	@GetMapping({"delete-car/{id}"}) 
	public void deleteCar(@PathVariable("id") long id) {
		Vehicle_Car car = carRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for deleting widget."));
		carRepository.delete(car);
	}
	
	@GetMapping({"add-appliance"})
	public void addAppliance(@Validated Widget_Appliance appliance, BindingResult result)
	{
		if (result.hasErrors()) {
			throw new IllegalArgumentException("Error adding new widget in WidgetController.addWidget");
		}
		applianceRepository.save(appliance);
	}
	@RequestMapping({"get-appliance"})
	public Widget_Appliance getAppliance(@PathVariable("id") long id)  {
		Widget_Appliance appliance = applianceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID specified to get-widget"));
		return appliance;
	}
	
	@RequestMapping({"/get-all-appliances"}) 
	public Iterable<Widget_Appliance> getAllAppliances()
	{
		Iterable<Widget_Appliance> appliances = applianceRepository.findAll();
		return appliances;
	}
	
	@PostMapping({"update-appliance/{id}"}) 
	public void updateAppliance(@Validated Widget_Appliance appliance, BindingResult result, @PathVariable("id") long id) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException("BindResult error in WidgetController.updateWidget");
		}
		applianceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for updating widget in WidgetController.updateWidget"));
		applianceRepository.save(appliance);
	}
	
	@GetMapping({"delete-appliance/{id}"}) 
	public void deleteAppliance(@PathVariable("id") long id) {
		Widget_Appliance appliance = applianceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for deleting widget."));
		applianceRepository.delete(appliance);
	}
	
	@GetMapping({"add-electronic"})
	public void addElectronic(@Validated Widget_Electronics electronic, BindingResult result)
	{
		if (result.hasErrors()) {
			throw new IllegalArgumentException("Error adding new widget in WidgetController.addWidget");
		}
		electronicRepository.save(electronic);
	}
	@RequestMapping({"get-electronic"})
	public Widget_Electronics getElectronic(@PathVariable("id") long id)  {
		Widget_Electronics electronic = electronicRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID specified to get-widget"));
		return electronic;
	}
	
	@RequestMapping({"/get-all-electronics"}) 
	public Iterable<Widget_Electronics> getAllElectronics()
	{
		Iterable<Widget_Electronics> electronics = electronicRepository.findAll();
		return electronics;
	}
	
	@PostMapping({"update-electronic/{id}"}) 
	public void updateElectronic(@Validated Widget_Electronics electronic, BindingResult result, @PathVariable("id") long id) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException("BindResult error in WidgetController.updateWidget");
		}
		electronicRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for updating widget in WidgetController.updateWidget"));
		electronicRepository.save(electronic);
	}
	
	@GetMapping({"delete-electronic/{id}"}) 
	public void deleteElectronic(@PathVariable("id") long id) {
		Widget_Electronics electronic = electronicRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for deleting widget."));
		electronicRepository.delete(electronic);
	}
	
	@GetMapping({"add-lawnCare"})
	public void addLawnCare(@Validated Widget_LawnCare lawnCare, BindingResult result)
	{
		if (result.hasErrors()) {
			throw new IllegalArgumentException("Error adding new widget in WidgetController.addWidget");
		}
		lawnCareRepository.save(lawnCare);
	}
	@RequestMapping({"get-lawnCare"})
	public Widget_LawnCare getLawnCare(@PathVariable("id") long id)  {
		Widget_LawnCare lawnCare = lawnCareRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID specified to get-widget"));
		return lawnCare;
	}
	
	@RequestMapping({"/get-all-lawnCares"}) 
	public Iterable<Widget_LawnCare> getAllLawnCares()
	{
		Iterable<Widget_LawnCare> lawnCares = lawnCareRepository.findAll();
		return lawnCares;
	}
	
	@PostMapping({"update-lawnCare/{id}"}) 
	public void updateLawnCare(@Validated Widget_LawnCare lawnCare, BindingResult result, @PathVariable("id") long id) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException("BindResult error in WidgetController.updateWidget");
		}
		lawnCareRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for updating widget in WidgetController.updateWidget"));
		lawnCareRepository.save(lawnCare);
	}
	
	@GetMapping({"delete-lawnCare/{id}"}) 
	public void deleteLawnCare(@PathVariable("id") long id) {
		Widget_LawnCare lawnCare = lawnCareRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for deleting widget."));
		lawnCareRepository.delete(lawnCare);
	}
	
	@GetMapping({"add-vehicle"})
	public void addVehicle(@Validated Widget_Vehicles vehicle, BindingResult result)
	{
		if (result.hasErrors()) {
			throw new IllegalArgumentException("Error adding new widget in WidgetController.addWidget");
		}
		vehicleRepository.save(vehicle);
	}
	@RequestMapping({"get-vehicle"})
	public Widget_Vehicles getVehicle(@PathVariable("id") long id)  {
		Widget_Vehicles vehicle = vehicleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID specified to get-widget"));
		return vehicle;
	}
	
	@RequestMapping({"/get-all-vehicles"}) 
	public Iterable<Widget_Vehicles> getAllVehicles()
	{
		Iterable<Widget_Vehicles> vehicles = vehicleRepository.findAll();
		return vehicles;
	}
	
	@PostMapping({"update-vehicle/{id}"}) 
	public void updateVehicle(@Validated Widget_Vehicles vehicle, BindingResult result, @PathVariable("id") long id) {
		if (result.hasErrors()) {
			throw new IllegalArgumentException("BindResult error in WidgetController.updateWidget");
		}
		vehicleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for updating widget in WidgetController.updateWidget"));
		vehicleRepository.save(vehicle);
	}
	
	@GetMapping({"delete-vehicle/{id}"}) 
	public void deleteVehicle(@PathVariable("id") long id) {
		Widget_Vehicles vehicle = vehicleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID provided for deleting widget."));
		vehicleRepository.delete(vehicle);
	}
}

