from datetime import datetime, timedelta

def get_upcoming_days(day_name):
    # Map day names to numbers (0 = Monday, 1 = Tuesday, ..., 6 = Sunday)
    days_map = {
        "monday": 0, "tuesday": 1, "wednesday": 2,
        "thursday": 3, "friday": 4, "saturday": 5, "sunday": 6
    }

    # Convert day_name to lowercase and find corresponding day number
    day_number = days_map.get(day_name.lower())
    if day_number is None:
        return "Invalid day name. Please enter a valid day."

    # Get today's date
    today = datetime.today()
    # Calculate days until the next specified day
    days_until_next = (day_number - today.weekday() + 7) % 7
    next_day = today + timedelta(days=days_until_next)

    # Generate the dates for the next 4 occurrences of the specified day
    dates = [next_day + timedelta(weeks=i) for i in range(4)]
    return [date.strftime("%Y-%m-%d") for date in dates]

# Get user input for the day
user_day = input("Enter a day of the week (e.g., Monday): ")
print(get_upcoming_days(user_day))
