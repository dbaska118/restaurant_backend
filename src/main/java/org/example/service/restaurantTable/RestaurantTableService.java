package org.example.service.restaurantTable;

import org.example.dto.restaurantTable.RestaurantTableStatusRequest;
import org.example.exception.RestaurantTableNotFoundException;
import org.example.exception.RestaurantTableStateConflict;
import org.example.exception.TablePriceNotFoundException;
import org.example.model.restaurantTable.RestaurantTable;
import org.example.model.restaurantTable.TablePrice;
import org.example.repository.restaurantTable.RestaurantTableRepository;
import org.example.repository.restaurantTable.TablePriceRepository;
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
        TablePrice tablePrice = tablePriceRepository.findWithLockByNumberOfChairs(restaurantTable.getNumberOfChairs()).orElseThrow(TablePriceNotFoundException::new);
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
        TablePrice tablePrice = tablePriceRepository.findWithLockByNumberOfChairs(restaurantTable.getNumberOfChairs()).orElseThrow(TablePriceNotFoundException::new);

        return restaurantTableRepository.findById(id).map(restaurantTableDB -> {

            if (!restaurantTableDB.getVersion().equals(restaurantTable.getVersion())) {
                throw new RestaurantTableStateConflict();
            }

            restaurantTableDB.setName(restaurantTable.getName());
            restaurantTableDB.setNumberOfChairs(restaurantTable.getNumberOfChairs());
            try {
                RestaurantTable saved = restaurantTableRepository.save(restaurantTableDB);
                restaurantTableRepository.flush();
                return saved;
            } catch (Exception e) {
                throw new RestaurantTableStateConflict();
            }
        }).orElseThrow(RestaurantTableNotFoundException::new);
    }

    @Transactional
    public RestaurantTable updateStatus(long id, RestaurantTableStatusRequest request) {

        RestaurantTable restaurantTableDB = restaurantTableRepository.findById(id)
                .orElseThrow(RestaurantTableNotFoundException::new);

        if(!restaurantTableDB.getVersion().equals(request.getVersion())) {
            throw new RestaurantTableStateConflict();
        }

        if (restaurantTableDB.getStatus() == request.getStatus()) {
            return restaurantTableDB;
        }

        restaurantTableDB.setStatus(request.getStatus());

        try {
            RestaurantTable saved = restaurantTableRepository.save(restaurantTableDB);
            restaurantTableRepository.flush();
            return saved;
        } catch (Exception e) {
            throw new RestaurantTableStateConflict();
        }
    }


}
