package com.sas.restcrud;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@RestController
public class MapController {

	private static HashMap<String, String> resources = new HashMap<String, String>();

	// Use POST for creation.
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public HttpEntity<MapResource> create(
			@RequestParam(value = "key", required = true) String key,
			@RequestParam(value = "value", required = true) String value) {

		// Does the key exist?
		if (resources.get(key) == null) {
			MapResource mapResource = new MapResource(key, value);
			mapResource.add(linkTo(methodOn(MapController.class).create(key, value)).withSelfRel());
			
			// Add the valude to the map.
			resources.put(key, value);
			return new ResponseEntity<>(mapResource, HttpStatus.CREATED);
		}

		// resolve error return
		return new ResponseEntity<>(new MapResource(key, "exists"), HttpStatus.OK);
	}
	
	// Use GET for read.
	@RequestMapping(value="/read", method=RequestMethod.GET)
	public HttpEntity<MapResource> read(
			@RequestParam(value = "key", required = true) String key) {

		// Does the key exist?
		if (resources.get(key) != null) {
			MapResource mapResource = new MapResource(key, resources.get(key));
			mapResource.add(linkTo(methodOn(MapController.class).read(key)).withSelfRel());
			return new ResponseEntity<>(mapResource, HttpStatus.OK);
		}

		// resolve error return
		return new ResponseEntity<>(new MapResource(key, "doesn't exist"), HttpStatus.NOT_FOUND);
	}

	// Use PUT for updating.
	@RequestMapping(value="/update", method=RequestMethod.PUT)
	public HttpEntity<MapResource> update(
			@RequestParam(value = "key", required = true) String key,
			@RequestParam(value = "value", required = true) String value) {

		// Does the key exist?
		if (resources.get(key) != null) {
			// Update the value in the map.
			resources.put(key, value);
			
			MapResource mapResource = new MapResource(key, resources.get(key));
			mapResource.add(linkTo(methodOn(MapController.class).update(key, value)).withSelfRel());
			return new ResponseEntity<>(mapResource, HttpStatus.OK);
		}

		// resolve error return
		return new ResponseEntity<>(new MapResource(key, "doesn't exist"), HttpStatus.NOT_FOUND);
	}

	// Use DELETE for deleting.
	@RequestMapping(value="/delete", method=RequestMethod.DELETE)
	public HttpEntity<MapResource> delete(
			@RequestParam(value = "key", required = true) String key) {

		// Does the key exist?
		if (resources.get(key) != null) {
			// Update the value in the map.
			resources.remove(key);
			
			MapResource mapResource = new MapResource(key, "removed");
			mapResource.add(linkTo(methodOn(MapController.class).delete(key)).withSelfRel());
			return new ResponseEntity<>(mapResource, HttpStatus.OK);
		}

		// resolve error return
		return new ResponseEntity<>(new MapResource(key, "doesn't exist"), HttpStatus.NOT_FOUND);
	}

}
