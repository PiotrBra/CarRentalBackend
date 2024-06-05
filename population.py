from dateutil.relativedelta import relativedelta
from datetime import datetime, timedelta
import json
import random
import secrets
import string
from datetime import datetime
from dateutil.relativedelta import relativedelta

# mownit my beloved
import json


def save_dicts_to_file(dictionaries, filename):
    with open(filename, 'w') as file:
        for dictionary in dictionaries:
            json.dump(dictionary, file)
            file.write('\n')


# parameteres
# clients
clientsAmount = 50
activeAmount = 0.9
maleToFemaleRatio = 0.7  # mężczyźni częściej robią rzeczy związane z samochodami

# cars
carsAmount = 20
economyRatio = 0.5
basicRatio = 0.8
serviceRatio = 0.9
maxRatings = 5
maxDeposit = 10000  # deposit will scale between classes

# hires
carsRented = 0.6
priceRanges = (1000, 5000)
maxFuel = 50
canceledAmount = 0.9

# opinions
opinionsAmount = 60

# reservations
reservationsAmount = 100
canceledReservationsAmount = 0.9

# kocham ejaj
femaleNames = [
    "Anna", "Maria", "Katarzyna", "Małgorzata", "Agnieszka",
    "Barbara", "Ewa", "Krystyna", "Elżbieta", "Magdalena"
]

maleNames = [
    "Piotr", "Krzysztof", "Andrzej", "Tomasz", "Paweł",
    "Jan", "Marek", "Michał", "Stanisław", "Adam"
]

lastNames = [
    "Nowak", "Kowalski", "Wiśniewski", "Wójcik", "Kowalczyk",
    "Kamiński", "Lewandowski", "Zieliński", "Szymański", "Woźniak",
    "Dąbrowski", "Kozłowski", "Jankowski", "Mazur", "Wojciechowski",
    "Kwiatkowski", "Krawczyk", "Kaczmarek", "Piotrowski", "Grabowski",
    "Zając", "Pawłowski", "Michalski", "Król", "Nowakowski",
    "Wieczorek", "Wróbel", "Jabłoński", "Stępień", "Adamczyk"
]

emailDomains = [
    "@gmail.com", "@wp.pl", "@onet.pl", "@o2.pl", "@interia.pl"
]


carBrands = [
    {
        "brand": "Toyota",
        "models": ["Corolla", "Camry", "RAV4", "Prius", "Yaris"]
    },
    {
        "brand": "Ford",
        "models": ["Focus", "Mustang", "Fiesta", "Explorer", "Escape"]
    },
    {
        "brand": "BMW",
        "models": ["3 Series", "5 Series", "X5", "X3", "7 Series"]
    },
    {
        "brand": "Mercedes-Benz",
        "models": ["C-Class", "E-Class", "S-Class", "GLC", "GLE"]
    },
    {
        "brand": "Audi",
        "models": ["A3", "A4", "A6", "Q5", "Q7"]
    }
]

car_descriptions = [
    "Szybki samochód idealny dla miłośników dynamicznej jazdy i adrenaliny.",
    "Wygodny samochód zaprojektowany z myślą o komforcie podróży na długie dystanse.",
    "Ekonomiczny samochód, który zapewnia oszczędność paliwa i niskie koszty eksploatacji.",
    "Sportowy samochód charakteryzujący się agresywnym designem i doskonałą dynamiką.",
    "Rodzinny samochód oferujący dużą przestrzeń i bezpieczeństwo dla wszystkich pasażerów.",
    "Terenowy samochód stworzony do pokonywania trudnych warunków drogowych i przygód na szlakach off-road.",
    "Luksusowy samochód, który definiuje prestiż i elegancję, oferując najwyższy poziom wygody i wyposażenia.",
    "Kompaktowy samochód idealny do miejskiej nawigacji, łatwy w prowadzeniu i parkowaniu.",
    "Praktyczny samochód dostawczy, który zapewnia dużą przestrzeń ładunkową i funkcjonalność.",
    "Elektryczny samochód, będący ekologiczną alternatywą, charakteryzujący się cichą jazdą i zerowymi emisjami spalin."
]

car_reviews = [
    "Ten samochód ma świetne osiągi i świetnie się prowadzi, idealny dla miłośników szybkiej jazdy.",
    "Jestem pod wrażeniem komfortu podróży w tym samochodzie, nawet na długich trasach.",
    "Oszczędność paliwa tego samochodu jest imponująca, co sprawia, że jest to idealny wybór dla osób ceniących ekonomię.",
    "Design sportowego samochodu jest po prostu niesamowity, a jego moc pozwala na prawdziwą frajdę za kierownicą.",
    "Samochód rodzinny, który oferuje mnóstwo miejsca dla pasażerów i bagażu, idealny na rodzinne wypady.",
    "Ten terenowy samochód sprawdził się doskonale podczas naszej wyprawy po górach, bez problemu pokonał trudne tereny.",
    "Luksusowy samochód, który dostarcza nie tylko niezrównanego komfortu, ale także prestiżu i elegancji.",
    "Kompaktowy samochód doskonale sprawdza się w zatłoczonym mieście, łatwo go parkować i manewrować nim.",
    "Ten samochód dostawczy zapewnia wszystko, czego potrzebuję do prowadzenia mojego biznesu, dużą przestrzeń ładunkową i niezawodność.",
    "Jestem pod wrażeniem cichej jazdy i braku emisji spalin w tym elektrycznym samochodzie.",
    "Jako miłośnik szybkich aut, muszę powiedzieć, że ten model naprawdę spełnia moje oczekiwania pod względem osiągów.",
    "Wygodny samochód, który sprawia, że podróżowanie jest przyjemnością, nawet na długich dystansach.",
    "Cieszę się z oszczędności paliwa, jakie zapewnia ten ekonomiczny samochód, szczególnie przy obecnych cenach.",
    "Nie mogę się doczekać kolejnej jazdy tym sportowym samochodem, jego dynamika jest niezwykła.",
    "Dla naszej wieloosobowej rodziny, ten samochód jest idealny - mnóstwo miejsca i bezpieczny.",
    "Ten terenowy samochód to prawdziwy bohater, pokonuje wszystkie przeszkody na naszej drodze.",
    "Nie wyobrażam sobie lepszego samochodu niż ten luksusowy model - to po prostu klasa sama w sobie.",
    "Kompaktowy samochód świetnie sprawdza się w gąszczu miasta, jest bardzo poręczny i łatwy w prowadzeniu.",
    "Samochód dostawczy jest moim niezastąpionym partnerem w biznesie, nie zawodzi mnie.",
    "Przez długi czas unikałem elektrycznych samochodów, ale teraz jestem pod wrażeniem ich wydajności i ekologiczności."
]


additional_requests = ["",
                       "fotelik",
                       "GPS",
                       "dodatkowe ubezpieczenie",
                       "system nawigacji satelitarnej",
                       "dodatkowy bagażnik na dachu",
                       "system monitorowania martwego pola",
                       "podgrzewane fotele",
                       "dodatkowy kierowca",
                       "automatyczne światła dziene",
                       "system automatycznego parkowania",
                       "klimatyzacja dwustrefowa",
                       "system automatycznego hamowania",
                       "wyjątkowy system bezpieczeństwa",
                       "dodatkowy system oszczędzania paliwa",
                       "dodatkowe zabezpieczenia antykradzieżowe"]


random.seed(523)


def generate_password(length=12):
    characters = string.ascii_letters + string.digits + string.punctuation
    password = ''.join(secrets.choice(characters) for i in range(length))
    return password


def toDate(date: datetime) -> str:
    return date.strftime('%Y-%m-%dT%H:%M')


def offsetDates(date: datetime, months=0, days=0, hours=0, minutes=0):
    offset = timedelta(days=days, hours=hours, minutes=minutes)
    result_date = date + offset
    result_date += relativedelta(months=months)
    return result_date


def generatePeople(amount, maleToFemaleRatio, activeAmount):
    people = []
    for i in range(amount):
        if random.random() < maleToFemaleRatio:  # male
            name = maleNames[random.randint(0, 9)]
        else:
            name = maleNames[random.randint(0, 9)]
        lastname = lastNames[random.randint(0, 29)]
        phone = random.randint(111111111, 999999999)
        email = name + lastname + \
            str(random.randint(1, 999)) + emailDomains[random.randint(0, 4)]
        password = generate_password()
        driverslicence = "00" + str(random.randint(111, 999)) + "/" + str(
            random.randint(1970, 2006) % 100) + "/" + str(random.randint(1111, 2000))
        if random.random() < activeAmount:
            status = "A"
        else:
            status = "B"
        people.append({
            "clientID": i,
            "firstname": name,
            "lastname": lastname,
            "phone": phone,
            "email": email,
            "password": password,
            "driverslicence": driverslicence,
            "status": status,
            "isEmployee": False
        })
    people.append({
        "clientID": amount,
        "firstname": "Admin",
        "lastname": "Admin",
        "phone": "999999999",
        "email": "admin.admin@admin.com",
        "password": "admin",
        "driverslicence": "",
        "status": "A",
        "isEmployee": True
    })

    return people


def generateCars(amount, economyRatio, basicRatio, serviceRatio, maxRatings, maxDeposit):
    cars = []
    for i in range(amount):
        curr_car = random.randint(0, 4)
        brand = carBrands[curr_car]["brand"]
        model = carBrands[curr_car]["models"][random.randint(0, 4)]
        year = random.randint(2005, 2024)
        registration = ("KR " + ''.join(secrets.choice(string.digits)) +  # fist char have to be digit by law
                        ''.join(secrets.choice(string.ascii_uppercase + string.digits)
                                for _ in range(4)))
        classNumber = random.random()
        if classNumber < economyRatio:
            classType = "economy"
            deposit = random.randint(100, int(maxDeposit*economyRatio))
        elif economyRatio <= classNumber and classNumber < basicRatio:
            classType = "basic"
            deposit = random.randint(
                int(maxDeposit*economyRatio), int(maxDeposit*basicRatio))
        else:
            classType = "premium"
            deposit = random.randint(int(maxDeposit*basicRatio), maxDeposit)
        if random.random() < serviceRatio:
            availability = "A"
        else:
            availability = "S"
        description = car_descriptions[random.randint(0, 9)]
        ratings = {
            "bad": random.randint(0, maxRatings),
            "good": random.randint(0, maxRatings),
            "excelent": random.randint(0, maxRatings)
        }
        pricePerDay = random.randint(100,1000)
        cars.append({
            "carID": i,
            "brand": brand,
            "model": model,
            "year": year,
            "registration": registration,
            "pricePerDay": pricePerDay,
            "class": classType,
            "availability": availability,
            "description": description,
            "ratings": ratings,
            "deposit": deposit,
            "photo": "need LINKS!!!!!!"
        })
    return cars


def generateHires(cars, clients, carsRented, priceRanges, maxFuel):
    # determine carsRented Cars and carsRented clients:

    carsIDs = [i for i in range(len(cars))]
    random.shuffle(carsIDs)
    clientsIDs = [i for i in range(len(clients))]
    random.shuffle(clientsIDs)

    hires = []
    for i in range(int(len(cars)*carsRented)):
        clientID = clientsIDs[i]
        carID = carsIDs[i]
        startDate = datetime(2024, 4, random.randint(
            1, 31), random.randint(0, 23), random.randint(0, 59))
        endDate = offsetDates(startDate, random.randint(0, 2), random.randint(
            0, 10), random.randint(0, 5), random.randint(0, 59))
        if endDate > datetime.today():
            cars[carID]["availaibility"] = "R"
            clients[clientID]["status"] = "O"
            endDate = ""
            status = "A"
        else:
            endDate = toDate(endDate)
            status = "F"
        startDate = toDate(startDate)
        price = random.randint(priceRanges[0], priceRanges[1])
        fuel = {
            "start": round(random.random()*maxFuel, 2),
            "end": round(random.random()*maxFuel, 2)
        }

        hires.append({
            "hireID": i,
            "clientID": clientID,
            "rentDate": {
                "start": startDate,
                "end": endDate
            },
            "price": price,
            "carID": carID,
            "fuel": fuel,
            "status": status
        })
    return hires


def generateOpinions(amount, carsAmount, clientsAmount):
    opinions = []
    for i in range(amount):
        opinions.append({
            "opinionID": i,
            "clientID": random.randint(0, clientsAmount),
            "carID": random.randint(0, carsAmount),
            "description": car_reviews[random.randint(0, len(car_reviews)-1)]
        })
    return opinions


def generateReservations(amount, canceledAmount, carsAmount, clientsAmount):
    def generateReservationsForCar(amount, carID, currentIndex):
        nonlocal canceledAmount, clientsAmount
        reservations = []
        endDate = offsetDates(datetime.today(), random.randint(0, 2), random.randint(
            0, 20), random.randint(0, 23), random.randint(0, 59))
        for i in range(amount):
            clientID = random.randint(0, clientsAmount-1)
            additionalRequest = additional_requests[random.randint(
                0, len(additional_requests)-1)]
            startDate = offsetDates(endDate, random.randint(0, 2), random.randint(
                0, 20), random.randint(0, 23), random.randint(0, 59))
            endDate = offsetDates(endDate, random.randint(0, 2), random.randint(
                0, 20), random.randint(0, 23), random.randint(0, 59))
            if random.random() > canceledAmount:
                status = "C"
            else:
                status = "A"
            reservations.append({
                "reservationID": currentIndex + i,
                "clientID": clientID,
                "carID": carID,
                "additionalRequests": additionalRequest,
                "reservationDate": {
                    "start": toDate(startDate),
                    "end": toDate(endDate)
                },
                "status": status
            })
        return reservations

    reservations = []
    for i in range(carsAmount):
        reservations.extend(generateReservationsForCar(
            amount//carsAmount, i, amount//carsAmount*i))
    return reservations


people = generatePeople(clientsAmount, maleToFemaleRatio, activeAmount)
cars = generateCars(carsAmount, economyRatio, basicRatio,
                    serviceRatio, maxRatings, maxDeposit)

hires = generateHires(cars, people, carsRented, priceRanges, maxFuel)

opinions = generateOpinions(opinionsAmount, len(cars), len(people))

reservations = generateReservations(
    reservationsAmount, canceledReservationsAmount, len(cars), len(people))

save_dicts_to_file(people, "population/users.json")
save_dicts_to_file(cars, "population/cars.json")
save_dicts_to_file(hires, "population/hires.json")
save_dicts_to_file(opinions, "population/opinions.json")
save_dicts_to_file(reservations, "population/reservations.json")
