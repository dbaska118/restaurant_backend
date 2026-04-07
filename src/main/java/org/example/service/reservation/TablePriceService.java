package org.example.service.reservation;

import org.example.exception.DishNotFoundException;
import org.example.exception.TablePriceExistException;
import org.example.exception.TablePriceNotFoundException;
import org.example.exception.TablesExistException;
import org.example.model.reservation.TablePrice;
import org.example.repository.reservation.RestaurantTableRepository;
import org.example.repository.reservation.TablePriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TablePriceService {

    private final TablePriceRepository tablePriceRepository;

    private final RestaurantTableRepository restaurantTableRepository;

    @Autowired
    public TablePriceService(TablePriceRepository tablePriceRepository, RestaurantTableRepository restaurantTableRepository) {
        this.tablePriceRepository = tablePriceRepository;
        this.restaurantTableRepository = restaurantTableRepository;
    }

    public TablePrice createTablePrice(TablePrice tablePrice) {
        try {
            return tablePriceRepository.save(tablePrice);
        }
        catch (DataIntegrityViolationException e) {
            throw new TablePriceExistException();
        }
    }

    public List<TablePrice> getAllTablePrices() {
        return tablePriceRepository.findAllByOrderByNumberOfChairsAsc();
    }

    @Transactional
    public TablePrice deleteTablePrice(int numberOfChairs){
        TablePrice tablePrice = tablePriceRepository.findByNumberOfChairs(numberOfChairs).orElseThrow(TablePriceNotFoundException::new);

        if(restaurantTableRepository.existsByNumberOfChairsAndActiveTrue(numberOfChairs)){
            throw new TablesExistException();
        }
        tablePriceRepository.delete(tablePrice);
        return tablePrice;
    }

    @Transactional
    public TablePrice updateTablePrice(int numberOfChairs, TablePrice tablePrice) {
        return tablePriceRepository.findByNumberOfChairs(numberOfChairs).map(tablePriceDB -> {
            tablePriceDB.setPrice(tablePrice.getPrice());
            return tablePriceRepository.save(tablePriceDB);
        })
                .orElseThrow(TablePriceNotFoundException::new);
    }
}
