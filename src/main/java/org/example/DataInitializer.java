package org.example;

import jakarta.annotation.PostConstruct;
import org.example.model.dish.Dish;
import org.example.model.dish.DishType;
import org.example.model.reservation.OpeningHours;
import org.example.model.user.Admin;
import org.example.model.user.User;
import org.example.repository.reservation.OpeningHoursRepository;
import org.example.repository.user.UserRepository;
import org.example.service.dish.DishService;
import org.example.service.reservation.OpeningHoursService;
import org.example.service.user.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@Profile("default")
public class DataInitializer {

    private final DishService dishService;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final OpeningHoursRepository openingHoursRepository;

    @Autowired
    public DataInitializer(DishService dishService, UserRepository userRepository, AuthService authService, OpeningHoursRepository openingHoursRepository) {
        this.dishService = dishService;
        this.userRepository = userRepository;
        this.authService = authService;
        this.openingHoursRepository = openingHoursRepository;
    }

    @PostConstruct
    public void init() {
        if(dishService.readAllDishes().isEmpty()) {
            Dish soup1 = new Dish("Kremowa Pomidorowa", "Aksamitny krem z pomidorów pelati z domowym makaronem jajecznym i świeżą bazylią.", 24.00, DishType.SOUP);
            Dish soup2 = new Dish("Staropolski Żurek", "Tradycyjny żur na domowym zakwasie z białą kiełbasą, jajkiem i aromatycznym majerankiem.", 28.00, DishType.SOUP);
            Dish soup3 = new Dish("Bulion Szlachecki", "Klarowny wywar z trzech mięs serwowany z ręcznie lepionymi kołdunami i lubczykiem.", 22.00, DishType.SOUP);

            Dish main1 = new Dish("Pizza Pałacowa", "Chrupiące ciasto, dojrzewająca mozzarella fior di latte, szynka Prosciutto Cotto i świeże pieczarki.", 52.00, DishType.MAIN);
            Dish main2 = new Dish("Stek z Polędwicy", "Soczysta wołowina w sosie z zielonego pieprzu, podawana z opiekanymi ziemniakami i warzywami.", 89.00, DishType.MAIN);
            Dish main3 = new Dish("Łosoś Cytrusowy", "Filet z łososia pieczony w marynacie z cytryn i rozmarynu na purée z zielonego groszku.", 64.00, DishType.MAIN);

            Dish dessert1 = new Dish("Wachlarz Lodów", "Trzy gałki rzemieślniczych lodów (wanilia, czekolada) z gorącym musem malinowym.", 18.00, DishType.DESSERT);
            Dish dessert2 = new Dish("Sernik Królewski", "Puszysty sernik na kruchym spodzie z białą czekoladą i owocami leśnymi.", 26.00, DishType.DESSERT);
            Dish dessert3 = new Dish("Fondant Czekoladowy", "Gorące ciastko z płynnym wnętrzem z gorzkiej czekolady, serwowane z sorbetem z marakui.", 29.00, DishType.DESSERT);

            Dish drink1 = new Dish("Sok Pomarańczowy", "Świeżo wyciskany nektar z dojrzałych pomarańczy sycylijskich (250 ml).", 16.00, DishType.DRINK);
            Dish drink2 = new Dish("Domowa Lemoniada", "Orzeźwiający napój cytrynowy z dodatkiem miodu wielokwiatowego i świeżej miętą.", 14.00, DishType.DRINK);
            Dish drink3 = new Dish("Kawa Pałacowa", "Aromatyczne espresso z nutą ciemnej czekolady i aksamitną pianką.", 12.00, DishType.DRINK);

            dishService.createDish(soup1);
            dishService.createDish(soup2);
            dishService.createDish(soup3);

            dishService.createDish(main1);
            dishService.createDish(main2);
            dishService.createDish(main3);

            dishService.createDish(dessert1);
            dishService.createDish(dessert2);
            dishService.createDish(dessert3);

            dishService.createDish(drink1);
            dishService.createDish(drink2);
            dishService.createDish(drink3);
        }
        if(userRepository.findByEmail("admin").isEmpty()){
            User admin = new Admin("admin", "admin", "admin", "admin");
            admin.setRole("headAdmin");
            authService.register(admin);
        }
        if(openingHoursRepository.count() == 0){
            LocalTime start = LocalTime.of(8,0);
            LocalTime end = LocalTime.of(20,0);
            OpeningHours monday = new OpeningHours("MONDAY", 1, start, end);
            OpeningHours tuesday = new OpeningHours("TUESDAY", 2, start, end);
            OpeningHours wednesday = new OpeningHours("WEDNESDAY", 3, start, end);
            OpeningHours thursday = new OpeningHours("THURSDAY", 4, start, end);
            OpeningHours friday = new OpeningHours("FRIDAY", 5, start, end);
            OpeningHours saturday = new OpeningHours("SATURDAY", 6, start, end);
            OpeningHours sunday = new OpeningHours("SUNDAY", 7, start, end);

            openingHoursRepository.save(monday);
            openingHoursRepository.save(tuesday);
            openingHoursRepository.save(wednesday);
            openingHoursRepository.save(thursday);
            openingHoursRepository.save(friday);
            openingHoursRepository.save(saturday);
            openingHoursRepository.save(sunday);
        }
    }
}
