package org.example.service.reservation;

import org.example.exception.RestaurantTableNotFoundException;
import org.example.exception.TablePriceNotFoundException;
import org.example.model.reservation.RestaurantTable;
import org.example.model.reservation.TablePrice;
import org.example.repository.reservation.RestaurantTableRepository;
import org.example.repository.reservation.TablePriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;
    private final TablePriceRepository tablePriceRepository;

    @Autowired
    public RestaurantTableService(RestaurantTableRepository restaurantTableRepository, TablePriceRepository tablePriceRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
        this.tablePriceRepository = tablePriceRepository;
    }

    @Transactional
    public RestaurantTable createRestaurantTable(RestaurantTable restaurantTable) {
        TablePrice tablePrice = tablePriceRepository.findByNumberOfChairs(restaurantTable.getNumberOfChairs()).orElseThrow(TablePriceNotFoundException::new);
        return restaurantTableRepository.save(restaurantTable);
    }

    public List<RestaurantTable> getAllRestaurantTables() {
        return restaurantTableRepository.findAllByActiveTrueOrderByNumberOfChairsAsc();
    }

    public RestaurantTable deleteRestaurantTable(long id) {
        RestaurantTable restaurantTable = restaurantTableRepository.findById(id).orElseThrow(RestaurantTableNotFoundException::new);
        restaurantTable.setActive(false);
        return restaurantTableRepository.save(restaurantTable);
    }

    @Transactional
    public RestaurantTable updateRestaurantTable(long id, RestaurantTable restaurantTable) {
        TablePrice tablePrice = tablePriceRepository.findByNumberOfChairs(restaurantTable.getNumberOfChairs()).orElseThrow(TablePriceNotFoundException::new);

        return restaurantTableRepository.findById(id).map(restaurantTableDB -> {
            restaurantTableDB.setName(restaurantTable.getName());
            restaurantTableDB.setNumberOfChairs(restaurantTable.getNumberOfChairs());
            return restaurantTableRepository.save(restaurantTableDB);
        })
                .orElseThrow(RestaurantTableNotFoundException::new);
    }
}
