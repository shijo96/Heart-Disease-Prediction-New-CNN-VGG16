from database import *
from geopy.geocoders import Nominatim
from geopy.distance import geodesic
import time

# Create a geolocator object
geolocator = Nominatim(user_agent="geoapi")

# Define the main place name
main_place_name = "Puthenvelikkara, Kerala, India"

# Get the coordinates for the main place
main_place = geolocator.geocode(main_place_name)

if main_place:
    main_coordinates = (main_place.latitude, main_place.longitude)
    print(f"Coordinates of {main_place_name}: {main_coordinates}")

    # Query to get doctors and their places
    query = '''
    SELECT doctor_id, first_name, place
    FROM doctors
    '''

    doctors = select(query)
    print("doctors : ", doctors)

    # List to store doctors with distances
    doctor_distances = []

    for doctor in doctors:
        doctor_id = doctor['doctor_id']
        first_name = doctor['first_name']
        place = doctor['place']

  

        # Use a more specific place name (e.g., append the region/state)
        place_query = f"{place}, Kerala, India"
        doctor_place = geolocator.geocode(place_query)

        if doctor_place:
            doctor_coordinates = (doctor_place.latitude, doctor_place.longitude)
            distance = geodesic(main_coordinates, doctor_coordinates).kilometers
            doctor_distances.append((doctor_id, first_name, place, distance))
        else:
            print(f"Could not find coordinates for {place}")

        # Sleep to avoid overloading the geolocation service (Nominatim's usage policy)
        time.sleep(1)

    # Sort the list of doctors by distance
    doctor_distances.sort(key=lambda x: x[3])

    # Print the sorted list of nearby doctors within 30 kilometers
    print("\nNearby doctors within 30 kilometers:")
    for doctor_id, first_name, place, distance in doctor_distances:
        if distance <= 30:
            print(f"Doctor ID: {doctor_id}, Name: {first_name}, Place: {place}, Distance: {distance:.2f} km")
else:
    print("The main place could not be found.")
