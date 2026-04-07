package org.example.controller.reservation;


import org.example.exception.TablePriceExistException;
import org.example.exception.TablePriceNotFoundException;
import org.example.exception.TablesExistException;
import org.example.model.reservation.TablePrice;
import org.example.service.reservation.TablePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tablePrice")
public class TablePriceController {

    private final TablePriceService tablePriceService;
    private int numberOfChairs;

    @Autowired
    public TablePriceController(TablePriceService tablePriceService) {
        this.tablePriceService = tablePriceService;
    }

    @GetMapping
    public ResponseEntity<List<TablePrice>> getAllTablePrice() {
        List<TablePrice> response = tablePriceService.getAllTablePrices();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TablePrice> createTablePrice(@RequestBody TablePrice tablePrice) {
        try {
            TablePrice response = tablePriceService.createTablePrice(tablePrice);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (TablePriceExistException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{numberOfChairs}")
    public ResponseEntity<TablePrice> updateTablePrice(@PathVariable int numberOfChairs, @RequestBody TablePrice tablePrice) {
        try {
            TablePrice response = tablePriceService.updateTablePrice(numberOfChairs, tablePrice);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (TablePriceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{numberOfChairs}")
    public ResponseEntity<TablePrice> deleteTablePrice(@PathVariable int numberOfChairs) {
        try {
            TablePrice response = tablePriceService.deleteTablePrice(numberOfChairs);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (TablePriceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (TablesExistException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
