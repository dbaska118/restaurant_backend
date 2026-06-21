# Warstwa logiki biznesowej aplikacji webowej restauracji Pałac Smaku

## Wykorzystane technologie:
- **Java 23**
- **Spring Boot**
- **Kontener Docker** z bazą danych **PostgreSQL**

## Uwierzytelnianie:
- Uwierzytelnianie użytkowników odbywa się za pomocą adresu e-mail oraz hasła.
- Hasła kont użytkowników przechowywane są w bazie danych w formie niejawnej po wcześniejszym obliczeniu wartościa skrótu za pomocą algorytmu **BCrypt**.

## Autoryzacja:
- Dostęp do wybranych operacji  w systemie jest kontrolowany na podstawie przypisanej **roli konta użytkownika** (Administrator Główny, Administrator, Pracownik, Klient).
- Bezpieczeństwo sesji zrealizowano z wykorzystaniem mechanizmu **JSON Web Token** z podziałem na dwa tokeny:
  - **Access Token** - Ważny 15 minut.
    - Odpowiedzialny za identyfikacje użytkowników podczas próby wykonania operacji w systemie
  - Refresh Token - Ważny 7 dni (lub do momentu zamknięcia przeglądarki).
    - Przechowywany w ciasteczkach z flagami 'Secure' oraz 'HttpOnly'.
    - Służy do automatycznego i niezauważalnego przez użytkownika odświeżania tokenu dostępu (**Access Token**).

## Wymagania funkcjonalne realizowane przez system:
- Użytkownik niezalogowany (Gość):
  - Tworzenie nowego konta oraz logowanie do istniejącego.
  - Dostęp do karty dań restauracji (Menu).
  - Dostęp do informacji o kosztach rezerwacji stolików.
  - Sprawdzenie niezarezerowanych stolików w wybranym dniu i czasie.
  - Formularz kontaktowy.
  - Dostęp do informacji o godzinach otwarcia restauracji.
- Użytkownik zalogowany (Operacje wspólne):
  - Wylogowanie z konta.
  - Edycja profilu (Imię i Nazwisko).
  - Zmiana hasła.
- Klient:
  - Doładowanie salda konta.
  - Śledzenie operacji salda konta.
  - Dokonanie rezerwacji.
  - Anulowanie rezerwacji.
- Pracownik:
  - Zarządzanie stanem stolików (Zajęty / Wolny).
  - Powierdzanie odbycia się rezerwacji, na podstawie adresu e-mail klienta oraz kodu rezerwacji.
  - Tworzenie rezerwacji na prośbę klientów bez konta.
- Administrator:
  - Modyfikacja karty dań (Menu).
  - Modyfikacja kont użytkowników (Klientów oraz Pracowników).
  - Zarządzanie godzinami otwarcia restauracji.
  - Modyfikacja cennika rezerwacji.
  - Modyfikacja stolików w restauracji.
- Administator główny:
  - Dostęp do operacji administratora.
  - Modyfikacja kont administratorów.


## Mechanizmy obsługi współbieżności:
- Blokady pesymistyczne:
  - Wykorzystywane podczas próby dokonania rezerwacji przez użytkowników, uniemożliwiając dokonanie rezerwacji przez dwóch lub więcej klientów w tym samym terminie.
- Blokady Optymistyczne:
    - Wykorzystujące pole wersji w encji stolików w restauracji, co zapobiega sytuacji, w której dwóch pracowników w tym samym czasie spróbuje zająć (zmienić stan) stolik dla dwóch różnych grup klientów.


## Testowanie:
- Testy jednostkowe - mające na celu sprawdzenie poprawności metod pojedyńczych klas.
- Testy integracyjne - mające na celu sprawdzenie poprawnego współdziałania wielu warstw systemu (od kontrolera, przez serwisy, aż po bazę danych) z wykorzystaniem kontenera **Docker** z testową bazą danych.
- Testy bezpieczeństwa - mające na celu sprawdzenie dostępu do wybranych endpointów na podstawie roli kont użytkowników. (W PRZYSZŁOŚCI).

## Uruchamianie projektu na systemie Windows
### Wymagania wstępne
- **Java 23** 
- **Docker Desktop** (uruchomiony w tle)
### Krok 1: Uruchomienie bazy danych (Docker)
1. Otwórz terminal w katalogu głównym projektu (`...\restaurant_backend\`).
2. Przejdź do folderu z konfiguracją Dockera:
```
cd Docker
docker compose up -d
```
* Jeśli chcesz uruchomić odizolowaną bazę przeznaczoną do testów:
```
docker compose -f test-docker-compose.yml up -d
```

### Krok 2: Uruchomienie aplikacji Spring Boot
1. Wróć do głównego katalogu projektu:
```
cd ..
.\mvnw.cmd spring-boot:run
```

