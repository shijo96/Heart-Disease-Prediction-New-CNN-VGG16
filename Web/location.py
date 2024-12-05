
# 2 gio coordinates distance calulation
###############################################


# from geopy.distance import geodesic

# # Define the coordinates for the two locations
# coordinates1 = (10.5255, 76.2145)  # First location
# coordinates2 = (10.0088, 76.3608)  # Second location


# # Calculate the distance between the two locations
# distance = geodesic(coordinates1, coordinates2).kilometers

# # Check if the distance is within 30 kilometers
# if distance <= 30:
#     print(f"The two locations are within 30 kilometers of each other, distance: {distance:.2f} km")
# else:
#     print(f"The two locations are not within 30 kilometers of each other, distance: {distance:.2f} km")




# Place Name based coordinates finding
###############################################

# from geopy.geocoders import Nominatim

# # Replace 'Place Name' with the desired location
# place_name = "Thrissur City Center"

# # Create a geolocator object
# geolocator = Nominatim(user_agent="geoapi")

# # Get the location
# location = geolocator.geocode(place_name)

# if location:
#     print(f"Latitude: {location.latitude}, Longitude: {location.longitude}")
# else:
#     print("Location not found.")





# 2 Place Name based distance finding
###############################################



# from geopy.geocoders import Nominatim
# from geopy.distance import geodesic

# # Create a geolocator object
# geolocator = Nominatim(user_agent="geoapi")

# # Define the place names
# place1_name = "Puthenvelikkara"
# place2_name = "Cheraman Juma Kodungallur"

# # Get the coordinates for the places
# place1 = geolocator.geocode(place1_name)
# place2 = geolocator.geocode(place2_name)

# if place1 and place2:
#     # Get the coordinates (latitude and longitude)
#     coordinates1 = (place1.latitude, place1.longitude)
#     coordinates2 = (place2.latitude, place2.longitude)
    
#     # Calculate the distance between the places
#     distance = geodesic(coordinates1, coordinates2).kilometers
    
#     # Check if the distance is within 30 kilometers
#     if distance <= 30:
#         print(f"The two places are within 30 kilometers of each other. Distance: {distance:.2f} km")
#     else:
#         print(f"The two places are not within 30 kilometers of each other. Distance: {distance:.2f} km")
# else:
#     print("One or both places could not be found.")



# 2 Place Name based distance calculation - Database and user location name from android
###############################################


from database import *
from geopy.geocoders import Nominatim
from geopy.distance import geodesic
import time


def calculate_distance(user_place):
    # Create a geolocator object
    geolocator = Nominatim(user_agent="geoapi")

    # Define the main place name
    main_place_name = user_place+", Kerala, India"

    # Get the coordinates for the main place
    main_place = geolocator.geocode(main_place_name)

    if main_place:
        main_coordinates = (main_place.latitude, main_place.longitude)
        print(f"Coordinates of {main_place_name}: {main_coordinates}")

        # Query to get doctors and their places
        query = '''
        SELECT * FROM doctors where status='Approved'
        '''

        doctors = select(query)
        print("doctors : ", doctors)

        # List to store doctors with distances
        doctor_distances = []

        for doctor in doctors:
            login_id = doctor['login_id']
            doctor_id = doctor['doctor_id']
            first_name = doctor['first_name']
            last_name = doctor['last_name']
            gender = doctor['gender']
            house_name = doctor['house_name']
            place = doctor['place']
            landmark = doctor['landmark']
            qualification = doctor['qualification']
            phone = doctor['phone']
            email = doctor['email']
            status = doctor['status']

       

            # Use a more specific place name (e.g., append the region/state)
            place_query = f"{place}, Kerala, India"
            doctor_place = geolocator.geocode(place_query)

            if doctor_place:
                doctor_coordinates = (doctor_place.latitude, doctor_place.longitude)
                distance = geodesic(main_coordinates, doctor_coordinates).kilometers
                doctor_distances.append((login_id,doctor_id, first_name,last_name,gender,house_name, place,landmark,qualification,phone,email,status, distance))
            else:
                print(f"Could not find coordinates for {place}")

            # Sleep to avoid overloading the geolocation service (Nominatim's usage policy)
            time.sleep(1)

        # Sort the list of doctors by distance
        doctor_distances.sort(key=lambda x: x[3])


        # Print the sorted list of nearby doctors within 30 kilometers
        print("\nNearby doctors within 30 kilometers:")
        # Filter and print the doctors within 30 km
        # filtered_doctors = []

        # for login_id,doctor_id, first_name,last_name,gender,house_name, place,landmark,qualification,phone,email,status, distance in doctor_distances:
        #     if distance <= 30:
        #         print(f"Doctor ID: {doctor_id}, Name: {first_name}, Place: {place}, Distance: {distance:.2f} km")
                
        #         filtered_doctors.append(
        #     (login_id, doctor_id, first_name, last_name, gender, house_name, place, landmark, qualification, phone, email, status, distance))
        #         # Return the filtered list of doctors
        #         return filtered_doctors

        # Assuming doctor_distances contains tuples of all details including distance
        filtered_doctors = []

        # Iterate over each doctor and filter those within 30 km
        for login_id, doctor_id, first_name, last_name, gender, house_name, place, landmark, qualification, phone, email, status, distance in doctor_distances:
            if distance <= 30:
                rounded_distance = round(distance, 2)
                print(rounded_distance)
                # Append each matching doctor to the filtered_doctors list
                filtered_doctors.append((
                    login_id, doctor_id, first_name, last_name, gender, house_name, place, landmark, qualification, phone, email, status, rounded_distance
                ))

        # Print the filtered doctors for debugging purposes
        print("\nFiltered doctors within 30 kilometers:")
        for doctor in filtered_doctors:
            print(doctor)

        # Return the filtered list
        return filtered_doctors

    else:
        print("The main place could not be found.")
        return "The main place could not be found."
