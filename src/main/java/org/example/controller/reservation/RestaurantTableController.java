package org.example.controller.reservation;

import org.example.exception.RestaurantTableNotFoundException;
import org.example.exception.TablePriceNotFoundException;
import org.example.model.reservation.RestaurantTable;
import org.example.service.reservation.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurantTable")
public class RestaurantTableController {

    private final RestaurantTableService restaurantTableService;

    @Autowired
    public RestaurantTableController(RestaurantTableService restaurantTableService) {
        this.restaurantTableService = restaurantTableService;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantTable>> getAllRestaurantTables() {
        List<RestaurantTable> response = restaurantTableService.getAllRestaurantTables();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RestaurantTable> createRestaurantTable(@RequestBody RestaurantTable restaurantTable) {
        try {
            restaurantTableService.createRestaurantTable(restaurantTable);
            return new ResponseEntity<>(restaurantTable, HttpStatus.CREATED);
        }
        catch (TablePriceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantTable> updateRestaurantTable(@PathVariable long id, @RequestBody RestaurantTable restaurantTable) {
        try {
            restaurantTableService.updateRestaurantTable(id, restaurantTable);
            return new ResponseEntity<>(restaurantTable, HttpStatus.OK);
        }
        catch (TablePriceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (RestaurantTableNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestaurantTable> deleteRestaurantTable(@PathVariable long id) {
        try {
            RestaurantTable restaurantTable = restaurantTableService.deleteRestaurantTable(id);
            return new ResponseEntity<>(restaurantTable, HttpStatus.OK);
        }
        catch (RestaurantTableNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
