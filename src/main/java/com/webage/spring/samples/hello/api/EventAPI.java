package com.webage.spring.samples.hello.api;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.webage.spring.samples.hello.repo.EventRepo;

@RestController
@RequestMapping("/Event")
public class EventAPI
{
	@Autowired
	private EventRepo eventRepo;

	//GET @ http://localhost:8080/Event
	//returns all customers
	@GetMapping
	public Iterable<Event> iterate()
	{
		return eventRepo.findAll();
	}
	
	//GET @ http://localhost:8080/Event/title/ <insert title>
	//returns customers by name
	@GetMapping("/title/{title}")
	public Event getByTitle(@PathVariable("title") String eventTitle)
	{
		Iterable<Event> list = iterate();
		for(Event event : list)
		{
			if(event.getTitle().equalsIgnoreCase(eventTitle))
			{
				return event;
			}
		}
		return null;
	}
	
	//GET @ http://localhost:8080/Eevnt/ <insert id>
	
	@GetMapping("/{id}")
	public Event getById(@PathVariable("id") long eventID)
	{
		return eventRepo.findById(eventID).orElse(null);
	}
	
	
	//POST @ http://localhost:8080/Customer
	@PostMapping
	public ResponseEntity<?> addEvent(@RequestBody Event newEvent, UriComponentsBuilder uri)
	{
		if (newEvent.getTitle() == null || newEvent.getCode() == null)
		{
			return ResponseEntity.badRequest().build();
		}
		newEvent = eventRepo.save(newEvent);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{title}")
				.buildAndExpand(newEvent.getTitle()).toUri();
		ResponseEntity<?> response = ResponseEntity.created(location).build();
		return response;
	}
	
	//PUT @ http://localhost:8080/Customer/name/<insert name>
	@PutMapping("/title/{title}")
	public ResponseEntity<?> putEventByTitle(@RequestBody Event newEvent, @PathVariable("title") String eventTitle)
	{
		if (newEvent.getTitle() == eventTitle || newEvent.getTitle() == null || newEvent.getCode() == null)
		{
			return ResponseEntity.badRequest().build();
		}
		else
		{
			newEvent.setId(getByTitle(eventTitle).getId());
		}
		
		newEvent = eventRepo.save(newEvent);
		return ResponseEntity.ok().build();
	}
	
	//DELETE @ http://localhost:8080/Customer/name/<insert name>
	//id doesn't reset
	@DeleteMapping("/title/{title}")
	public ResponseEntity<?> deleteCustomer(@PathVariable String title)
	{
		eventRepo.delete(getByTitle(title));
		return new ResponseEntity<String>("Event " + title + " was deleted.", HttpStatus.OK);
	}
	
	/*
	 * //PUT @ http://localhost:8080/Customer/email/<insert email>
	 * 
	 * @PutMapping("/email/{email}") public ResponseEntity<?>
	 * putCustomerByEmail(@RequestBody Customer newCustomer, @PathVariable("email")
	 * String custEmail) { if (newCustomer.getEmail() == custEmail ||
	 * newCustomer.getEmail() == null || newCustomer.getEmail() == null) { return
	 * ResponseEntity.badRequest().build(); } newCustomer =
	 * custRepo.save(newCustomer); return ResponseEntity.ok().build(); }
	 */
}
