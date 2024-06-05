# CarRental
### Autorzy: Piotr Branewski, Ernest Szlamczyk

Celem tego projektu jest stworzenie integracji między 
bazą danych (MongoDB) a serwisem backendowym (przy użyciu 
frameworku Springframework)

## 1. Model bazy danych

Jako model przyjeliśmy prosty pięco dokumentowy system:
### Kolekcja Cars:

```json
{
    "carID": 1,
    "brand": "Polonez",
    "model": "1.6",
    "year": 2000,
    "registration": "KR 12345",
    "pricePerDay": 1000000,
    "category": "economy/basic/premium",
    "availability": "A/R/S",
    "description": "panie takiego poloneza to by każdy",
    "ratings": {
        "bad": 0,
        "good": 0,
        "excellent": 0
    },
    "deposit": 1000,
    "photo": "link"
}
```
kolekcja `Cars` odpowiada za przetrzymywanie danych dotyczących
samochodów dostępnych do wypożyczenia, znajdują się tam głównie
parametry danego samochodu, ale też przydatne informacje takie
jak cena za dzień, kaucja, oceny użytkowników tak jak i dostępność.
Dostępność posiada kod `A/R/S`, który jest tłumaczony na:

- A - available
- R - rented
- S - (in) service

### Kolekcja Users:

```json
{
    "clientID": 1,
    "firstname": "Janusz",
    "lastname": "Kowalski",
    "phone": "+48 123451674",
    "email": "janusz.kowal523@poczta.onet",
    "password": "Nie ma hashowania ze względu na rozmiar projektu",
    "driverslicence": "numerporzątkowy/rokwydania/symbole terytorialne GUS przykładowo: 00645/24/1221",
    "status": "A/B",
    "isEmployee": false
}
```
kolekcja `Users` odpowiada za przetrzymywanie informacji o klientach (lub pracownikach, za co odpowiada flaga `isEmployee`).
Każda z danych jest generowana w sposób możliwie najbardziej realistyczny (o tym więcej w sekcji ``Generowanie danych``).
Każdy z Klientów posiada także status:
- A - available
- B - blocked
- O - occupied (już wypożycza samochód)

### Kolekcja Hires:

```json
{
    "hireID": 1,
    "clientID": 1,
    "rentDate": {
        "start": "YYYY-MM-DDThh:mm skrócone ISO 8601",
        "end": "YYYY-MM-DDThh:mm"
    },
    "price": 100,
    "carID": 1,
    "fuel": {
        "start": 10.21,
        "end": 8.21
    },
    "status": "A/C"
}
```
Kolekcja `Hires` odpowiada za informacje dotyczące wypożyczeń (tworząc przy okazji relacje między `Cars` i `Clients`).
Znajdują się tu informacje dotyczące daty, paliwa i łacznej zapłaconej ceny (w przypadku praktycznym może się przydać
do ewentualnych zwrotów). Pozstaje także informacja o statusie wypożyczenia:
- A - active (jezeli czas zakończenia jest już wpisany, to znaczy, że wszystko zostało zakończone pomyślnie)
- C - canceled

### Kolekcja Reservations:

```json
{
    "reservationID": 1,
    "clientID": 1,
    "carID": 1,
    "additionalRequests": "fotelik/GPS/inne usługi",
    "reservationDate": {
        "start": "YYYY-MM-DDThh:mm ISO 8601",
        "end": "YYYY-MM-DDThh:mm"
    },
    "status": "A/C"
}
```
Kolekcja `Reservations` zajmuje się przechowywaniem danych na temat rezerwacji,
peryferiów, o które klient prosi, daty początku i końca danej rezerwacji i jej status, który jest tak samo zakodowany,
jak w przypadku ``Hires``.

### Kolekcja Opinions:

```json
{
    "opinionID": 1,
    "clientID": 1,
    "carID": 1,
    "description": "Fajne auto, szkoda tylko że tyle pali"
}
```
Bardzo redundantna kolekcja w celach projektowych, ale została dodana w celu zademonstrowania elastyczności bazy mongo

### Wnioski dot. bazy danych
Po zaprojektowaniu bazy danych zauważyliśmy plusy, które dostarcza mniej sztywna implementacja modelu bazy danych
niż w bardziej klasycznym podejściu relacyjnym:
- możliwość zagnieżdżania dokumentów w innych dokumentach umożliwia usunięcie niepotrzebnych tabelek, a dodaje struktury i porządku

## 2. Generacja Danych
Do wygenerowania danych użyliśmy języka python oraz biblioteki do obsługi JSON-ów i czasu.

Sam kod jest dosyć obszerny i zawiera się w pliku `population.py`, ale polega ona głównie na dobieraniu realistycznych 
wartości losowych zapisywanie je w liście słowników a nastepnie dumpować te słowniki do pliku `.json`
### Wnioski dot. bazy danych
Przy generowaniu danych można było dostrzec pare istotnych argumentów za i przeciw:

- (+) nigdy nie pojawił się problem nieodpowiedniej kolejności wkładania dokumentów do bazy, jako że pliki nie tworzą faktycznych relacji
- (+) generowanie danych jest bardziej linearne niż w przypadku baz relacyjnych.
- (-) zdecydowanie bardziej trzeba uważać na integralność danych - baza nie powiem nam, co jest nie tak z danymi, co sprawia, że łatwo nie zauwazyć ewentualnych błedów

## 3. Backend

### Reprezentacja Danych

Jako że postanowiliśmy wybrać Jave wraz ze Springiem, początkowo musieliśmy stworzyć klasy odpowiadające danym w bazie
(które znajdują się w folderze `databaseModel`). Do tego bardzo przydał się `lombok` i dependency do MongoDB.

### Połączenie danych do backend'u

Dzięki temu udało się uniknąć bardzo dużo pracy związanej z łaczeniem i wykonywaniem danych, jako że Spring umożliwia
stworzenie `Repozytorium`, które będzie wykonywało większość pracy pod maską. Jedynie co musieliśmy zrobić to opisać,
jakie dane i w jaki sposób chcielibyśmy wyciągnąć z bazy (i w przypadkach bardziej skomplikowanych użyć adnotacji
`@Querry`).

### Serwisy
Serwisy zostały utworzone jako bardziej systematyczny sposób wykorzystania metod wskazanych w interfejsach.
Dodatkowo dzięki adnotacji `@Autowired` wiemy, że każde z repozytoriów jest `Singletonem`, dzięki czemu pozbywamy się
problemów związanych z potrzebą łączenia tabelek przy użyciu Aggregation Pipeline, co w sprawozdaniu okazało się problemem.
Serwisy także odpowiadają za utrzymanie warunków integralności.

Tutaj pojawia się jeden z większych problemów, które napotkaliśmy w trakcie realizowania tego projektu, a mianowicie
utrzymywanie odpowiednich numerów ID. Dlatego stworzyliśmy dodatkowy Serwis ``IdMakerService`` odpowiadający tylko i 
wyłącznie za wyznaczanie nowych ID i zapewnienie ich niepowtarzalności.

### Controllery
Controllery odpowiadają za wywoływanie funkcji w serwisach i za operacje CRUD, które zostały adekwatnie poukładane w kodzie.

## 4. Rozwiązywanie problemów

Na przestrzeni projektu pojawiło się kilka problemów, które były związane z używaniem mongodb jako bazy danych.

### Generacja indeksów
Jako że większość naszych danych używa własnych indeksów, aby tworzyć relacje w bazie, pojawił się problem
automatycznej indeksacji. W odróżnieniu od rozwiązań sqlowych nie jesteśmy w stanie rzucić autoincrement na PK, ale
dalej chcemy gwarancji integralności danych. W tym miejscu przychodzi nam z pomocą wzorzec projektowy
`Singleton`, dzięki któremu możemy trzymać informacje dotyczące każdego ID,
a następnie używając rozwiązania Javowego — używając bloku synchronizowanego. Dzięki temu wiemy, że w danym momencie
tylko 1 request może dostać własne ID.
Przykładowo dla samochodów:
```java
    public synchronized Integer getAndIncrementCarId() {
        this.carId += 1;
        return carId;
    }
```

### Konwersja danych
Kolejnym problemem okazała się konwersja danych spowodowana elastycznością mongodb. Jako że nie wszystkie wypożyczenia
muszą być zakończone (niektóre mogą być w trakcie), nastała potrzeba zaznaczenia tego. My przyjeliśmy, że struktura:
```json
{
  "rentDate": {
    "start": "YYYY-MM-DDThh:mm",
    "end": ""
  }
}
```
`"end": ""` oznacza, że wypożyczenie nie zostało jeszcze zakończone. Niestety domyślny konwerter stringa na 
biblioteczną klasę `LocaleDateTime` nie wiedział co zbytnio z tym zrobić, dlatego nastała potrzeba napisania własnej
konwersji, która została napisana w `converters.StringToLocalDateTimeConverter` a następnie dodana do listy konwerterów
w pliku konfiguracyjnym `MongoConfig`

## 5. Testy

Ta sekcja została przeznaczona na testy poszczególnych operacji CRUD:

Dla skrócenia ilości tekstu CRUD został zaimplementowany przez odpowiednie
metody http

### Users

#### Create
Nie ma tutaj nic specjalnego, request przekazuje Ciało, które jest
zamieniane na klasę `User` a następnie dodawane (z poprawianiem informacji
takich jak status na aktywny, odpowiednie, generowane ID oraz status pracownika
na `false`). Dodatkowo sprawdzana jest przy okazji unikalność emaila.

#### Read
Stworzyliśmy kilka przykładowych sposobów wyszukiwania użytkowników:

 - wszyscy
 - po ID klienta
 - po imieniu
 - po nazwisku
 - po numerze telefonu
 - wszyscy aktywni
 - wszyscy, którzy wypożyczają już samochód
 - wszyscy zablokowani
 - pracownicy
 - nie pracownicy

Każdy z tych Mappingów ma swój odpowiedni ruter

#### Update
Aktualizacja przeprwodzana jest w podobny sposób co dodawanie, aczkolwiek
dodatkowo podawane jest ID klienta (lub pracownika) oraz ciało, które
chcemy zaktualizować. Następnie serwis sprawdza, czy istnieje użytkownik
z takim ID oraz, czy ewentualnie zmieniany email jest dalej autentyczny.

#### Delete
Usuwanie jest dostępne po ID użytkownika. Dodatkowo usunięcie użytkownika sprawdza,
czy użytkownik nie ma wypożyczonego jakiego samochodu, oraz ustawia wszystkie rezerwacje na
odwołane

### Rezerwacje
Jedyne co specjalnego w `Create` `Update` i `Delete` to integralność, a mianowicie
sprawdzane jest, czy samochód w danym fragmencie czasu jest dostępny do wyporzyczenia 
(oraz czy istnieje samochód o takim ID). Dodatkowo została stworzona metoda bezpośredniego
odwoływania rezerwacji poprzez routing `/cancel/{id}`

Jeżeli chodzi o `Read`, dostępne są wyszukiwania po:

 - wszystkim
 - ID rezerwacji
 - ID klienta
 - ID samochodu
 - przedziału czasowego (które rezerwacje są między dniem A, a dniem B)

### Cars
W przypadku samochodów zastosowaliśmy jedynie operację `Read`.
Można wyszkiwać samochody:
- wszystkie
- pojedyńczy poprzez ID samochodu
- po cenie (niższej/wyższej niż podana, pomiędzy)
### Opinions
Jeśli chodzi o opinie, to wszystkie operacje `CRUD` są możliwe.
#### Create
Tak jak w poprzednich operacjach `Create` przekazujemy ciało, któe jest zamieniane na klasę `Opinion`.
#### Read
Operacje `Read` można wykonać na kilka sposobów:
- odczytanie wszystkich
- po ID samochodu
- po ID klienta
- po ID opinii
#### Update, Delete
Analogicznie jak w Users

### Hires
W tym przypadku również wykonujemy wszystkie operacje `CRUD`.
#### Create, Update, Delete 
Analogicznie jak w Users.
#### Read
Możemy uzyskać informacje o wypożyczeniach:
- wszystkich
- po ID samochodu
- po ID clienta
- po cenie niższej niż X
- po cenie pomiędzy (X,Y)
- które zostały zrealizowane w w określonych okresie czasowym
- po statusie wypożyczenia
