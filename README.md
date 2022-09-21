# ElevatorDemo

### Uruchomienie projektu
Projekt został napisany w Java 11 z wykorzystaniem framework'a Spring Boot. Można go zbudować za pomocą narzędzia Maven lub uruchomić ze środowiska programistycznego (np. IntelliJ IDEA). 

#### Uruchomienie z IntelliJ IDEA
Uruchomienie programu może nastąpić poprzez naciśnięcie zielonej strzełki po lewej od nazwy klasy DemoApplication. Przed uruchomieniem programu należy wejść do menu Run/Debug Configurations w celu dopisania wartości program arguments (np. 1). Podany argument programu oznacza ilość wind obsługiwanych przez program.

#### Uruchomienie za pomocą narzędzia Maven
Instrukcja zakłada posiadanie przez użytkownika zainstalowanego Maven'a i dodanie go do zmiennych środowiskowych systemu. Należy uruchomić terminal i przenieść się do folderu, w którym znajduje się projekt. Następnie uruchomić projekt za pomocą komendy:
```
mvn clean install
```
Następnie należy przenieść się do katalogu target i uruchomić serwer aplikacji za pomocą komendy:
```
java -jar demo-1.0.jar {argument wywołania aplikacji}
```
Jako argument wywołania aplikacji należy podać liczbę całkowitą wind obsługiwanych przez system.

### Funkcjonalności udostępniane przez projekt

```
POST /elevator - dodanie zlecenia przejazdu dla windy
PUT /elevator - aktualizacja aktualnego piętra windy i docelowego piętra windy (usuwa informacje na temat aktualnego przejazdu)
PUT /elevator/step - wykonanie kroku symulacji (zmiana stanu windy)
GET /elevator - wypisanie aktualnych informacji na temat wszystkich wind w systemie
```

W celu umożliwienia łatwej obsługi serwera dodano narzędzie SwaggerUI. Link do swagger'a dla serwera to (przy niezmienionym porcie aplikacji): http://localhost:8080/swagger-ui/index.html

### Opis działania programu

Serwer aplikacji nie jest połączony z bazą danych, w związku z czym stan aplikacji nie jest zapamiętywany pomiędzy uruchomieniami serwera. Dane są trzymane w klasie DataStore. Klasa jest inicjalizowana w momencie uruchomienia aplikacji. Dane są trzymane w mapie, której kluczem jest identyfikator windy zaś wartością jest klasa ElevatorManagement odpowiadająca za obsługę windy. Klasa ElevatorManagement zawiera w sobie obiekt windy (klasa Elevator) oraz dwie LinkedList odpowiadające za zbieranie zleceń przejazdu oraz aktualną trasę realizowaną przez winde. 

#### Opis algorytmu

Zlecenia przejazdu dla nieaktywnej windy trafia do listy elevatorCalls. W momencie rozpoczęcia symulacji zlecenia z elevatorCalls są zamieniane na listę currentRoute. LinkedList currentRoute jest listą pięter, na których musi stanąć winda. CurrentRoute jest optymalna, ponieważ realizuje wszystkie zlecenia z listy elevatorCalls, które mogą być wykonane podczas wykonywania pierwszego zlecenia z listy. Przykład:

```
elevatorCalls: (13, 2), (2, 4), (10, 8), (9, 1), (7, 3)

Dla powyższej listy zostanie wyznaczona trasa: 13 -> 10 -> 8 -> 7 -> 3 -> 2. 

Stan elevatorCalls po ustaleniu tej trasy: (2, 4), (9, 1)
```

Trasa (2, 4) nie została uwzględniona, ponieważ jest w przeciwnym kierunku niż pierwsze żądanie w elevatorCalls (czyli (13, 2)). Trasa (9, 1) nie została uwzględniona, ponieważ piętro 1 jest poniżej ostatniego odwiedzanego piętra pierwszego żądania (czyli 2). Priorytetem jest trasa pierwszego pasażera, ale "przy okazji" realizowane są trasy znajdujące się na trasie pierwszego pasażera. 

Ponadto jeżeli podczas przejazdu windy zdarzy się żądanie (7, 5) zanim winda będzie na 7 piętrze, to żądanie to zostanie obsłużone w trakcie tego przejazdu. Za tą funkcjonalność odpowiada metoda addCallToCurrentRoute(Call). Optymanle przejazdy dla poszczególnych kierunków są wyznaczane za pomocą metody calculateOptimalRoute(Call). 

W przypadku różnych kierunków:
```
elevatorCalls: (13, 2), (2, 4)
```
winda będzie zachowywała się "normalnie" tzn. żądania zostaną wykonane kolejno po sobie. 

#### Opis testów

Testy zostały umieszczone w trzech klasach:

- ElevatorControllerTest - testy kontrolerów udostępnianych przez aplikację
- ElevatorManagementTest - testy algorytmu (Zaimplementowane poprzez symulowanie przejazdów windy. Assercje sprawdzają, czy winda zatrzymuje się na odpowiednich piętrach.)
- ElevatorServiceTest - testy jednostkowe logiki w klasie serwisu

