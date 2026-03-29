-- Include your create table DDL statements in this file.
-- Make sure to terminate each statement with a semicolon (;)

-- LEAVE this statement on. It is required to connect to your database.
CONNECT TO COMP421;

-- Remember to put the create table ddls for the tables with foreign key references
--    ONLY AFTER the parent tables have already been created.

CREATE TABLE Hotels
(
  hotel_id INTEGER NOT NULL PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  address VARCHAR(255) NOT NULL,
  city VARCHAR(100) NOT NULL
);

CREATE TABLE Rooms
(
  hotel_id INTEGER NOT NULL,
  room_number INTEGER NOT NULL,
  price INTEGER NOT NULL CHECK(price > 0),
  bed_type VARCHAR(6) CHECK(bed_type IN ('King', 'Queen', 'Double')),
  number_of_beds INTEGER NOT NULL CHECK(number_of_beds >= 1),
  size INTEGER NOT NULL CHECK(size >= 100),
  PRIMARY KEY(hotel_id, room_number),
  FOREIGN KEY(hotel_id) REFERENCES Hotels(hotel_id)
);

CREATE TABLE Users
(
  email VARCHAR(150) NOT NULL PRIMARY KEY,
  name VARCHAR(150),
  date_of_birth DATE
);

CREATE TABLE Reservations
(
  reservation_id INTEGER NOT NULL PRIMARY KEY,
  email VARCHAR(150) NOT NULL,
  check_in DATE NOT NULL,
  check_out DATE NOT NULL,
  is_paid_completely SMALLINT DEFAULT 0 CHECK(is_paid_completely IN (0, 1)),
  CHECK (check_out > check_in),
  FOREIGN KEY(email) REFERENCES Users(email)
);

CREATE TABLE Payments
(
  payment_id INTEGER NOT NULL PRIMARY KEY,
  reservation_id INTEGER NOT NULL,
  amount INTEGER NOT NULL CHECK (amount > 0),
  method VARCHAR(6) NOT NULL CHECK(method IN ('Credit', 'Debit', 'PayPal')),
  email VARCHAR(150) NOT NULL,
  FOREIGN KEY(reservation_id) REFERENCES Reservations(reservation_id),
  FOREIGN KEY(email) REFERENCES Users(email)
);

CREATE TABLE Reviews
(
  review_id INTEGER NOT NULL PRIMARY KEY,
  rating INTEGER NOT NULL CHECK(rating >= 1 AND rating <= 5),
  date DATE NOT NULL DEFAULT CURRENT DATE,
  title VARCHAR(255),
  comment VARCHAR(255),
  email VARCHAR(150) NOT NULL,
  hotel_id INTEGER NOT NULL,
  FOREIGN KEY(email) REFERENCES Users(email),
  FOREIGN key(hotel_id) REFERENCES Hotels(hotel_id)
);

CREATE TABLE RegularRooms
(
  hotel_id INTEGER NOT NULL,
  room_number INTEGER NOT NULL,
  standard_category VARCHAR(8) NOT NULL CHECK(standard_category IN ('Standard', 'Superior', 'Deluxe')),
  PRIMARY KEY(hotel_id, room_number),
  FOREIGN KEY(hotel_id, room_number) REFERENCES Rooms(hotel_id, room_number)
);

CREATE TABLE Suites
(
  hotel_id INTEGER NOT NULL,
  room_number INTEGER NOT NULL,
  view_type VARCHAR(8) CHECK(view_type IN ('Ocean', 'City', 'Garden', 'Mountain')),
  PRIMARY KEY(hotel_id, room_number),
  FOREIGN KEY(hotel_id, room_number) REFERENCES Rooms(hotel_id, room_number)
);

CREATE TABLE ConnectingRooms
(
  hotel_id INTEGER NOT NULL,
  room_number INTEGER NOT NULL,
  connecting_room_number INTEGER NOT NULL,
  PRIMARY KEY(hotel_id, room_number),
  FOREIGN KEY (hotel_id, room_number) REFERENCES Rooms(hotel_id, room_number),
  FOREIGN KEY (hotel_id, connecting_room_number) REFERENCES Rooms(hotel_id, room_number)
);

CREATE TABLE Amenities
(
  amenity_id INTEGER NOT NULL PRIMARY KEY,
  type VARCHAR(50) NOT NULL,
  description VARCHAR(255) NOT NULL
);

CREATE TABLE RoomAmenities
(
  hotel_id INTEGER NOT NULL,
  room_number INTEGER NOT NULL,
  amenity_id INTEGER NOT NULL,
  PRIMARY KEY(hotel_id, room_number, amenity_id),
  FOREIGN KEY(hotel_id, room_number) REFERENCES Rooms(hotel_id, room_number),
  FOREIGN KEY(amenity_id) REFERENCES Amenities(amenity_id)
);

CREATE TABLE PremiumFeatures
(
  feature_name VARCHAR(150) NOT NULL PRIMARY KEY,
  description VARCHAR(255) NOT NULL,
  category VARCHAR(100) NOT NULL
);

CREATE TABLE SuitePremiumFeatures
(
  hotel_id INTEGER NOT NULL,
  room_number INTEGER NOT NULL,
  feature_name VARCHAR(150) NOT NULL,
  PRIMARY KEY(hotel_id, room_number, feature_name),
  FOREIGN KEY(hotel_id, room_number) REFERENCES Suites(hotel_id, room_number),
  FOREIGN KEY(feature_name) REFERENCES PremiumFeatures(feature_name)
);

CREATE TABLE Transportation
(
  transport_name VARCHAR(150) NOT NULL,
  type VARCHAR(50) NOT NULL,
  PRIMARY KEY(transport_name, type)
);

CREATE TABLE AccessibleTransportation
(
  transport_name VARCHAR(150) NOT NULL,
  type VARCHAR(50) NOT NULL,
  hotel_id INTEGER NOT NULL,
  distance INTEGER NOT NULL CHECK(distance >= 0),
  PRIMARY KEY(transport_name, type, hotel_id),
  FOREIGN KEY(transport_name, type) REFERENCES Transportation(transport_name, type),
  FOREIGN KEY(hotel_id) REFERENCES Hotels(hotel_id)
);

CREATE TABLE Attractions
(
  attraction_id INTEGER NOT NULL PRIMARY KEY,
  address VARCHAR(255) NOT NULL,
  attraction_name VARCHAR(150) NOT NULL,
  rating INTEGER CHECK(rating >= 1 AND rating <= 5)
);

CREATE TABLE SurroundingAttractions
(
  attraction_id INTEGER NOT NULL,
  hotel_id INTEGER NOT NULL,
  distance INTEGER NOT NULL CHECK(distance >= 0),
  PRIMARY KEY(attraction_id, hotel_id),
  FOREIGN KEY(attraction_id) REFERENCES Attractions(attraction_id),
  FOREIGN KEY(hotel_id) REFERENCES Hotels(hotel_id)
);

CREATE TABLE RoomBookings
(
  reservation_id INTEGER NOT NULL,
  hotel_id INTEGER NOT NULL,
  room_number INTEGER NOT NULL,
  PRIMARY KEY(reservation_id, hotel_id, room_number),
  FOREIGN KEY(reservation_id) REFERENCES Reservations(reservation_id),
  FOREIGN KEY(hotel_id, room_number) REFERENCES Rooms(hotel_id, room_number)
);