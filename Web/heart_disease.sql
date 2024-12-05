-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Nov 29, 2024 at 02:03 PM
-- Server version: 10.4.18-MariaDB
-- PHP Version: 8.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `heart_disease`
--

-- --------------------------------------------------------

--
-- Table structure for table `bookings`
--

CREATE TABLE `bookings` (
  `booking_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `consulting_id` int(11) DEFAULT NULL,
  `date_time` varchar(255) DEFAULT NULL,
  `book_date` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `bookings`
--

INSERT INTO `bookings` (`booking_id`, `user_id`, `consulting_id`, `date_time`, `book_date`, `status`) VALUES
(1, 1, 1, '2024-11-29', '2024-11-28 10:55:27', 'Confirmed'),
(2, 1, 2, '2024-11-29', '2024-11-29 12:20:52', 'Booked'),
(3, 2, 1, '2024-11-29', '2024-11-29 12:23:21', 'Booked'),
(4, 2, 2, '2024-11-30', '2024-11-29 12:23:47', 'Booked');

-- --------------------------------------------------------

--
-- Table structure for table `chat`
--

CREATE TABLE `chat` (
  `chat_id` int(11) NOT NULL,
  `sender_id` int(11) DEFAULT NULL,
  `sender_type` varchar(255) DEFAULT NULL,
  `receiver_id` int(11) DEFAULT NULL,
  `receiver_type` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `date_time` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `chat`
--

INSERT INTO `chat` (`chat_id`, `sender_id`, `sender_type`, `receiver_id`, `receiver_type`, `message`, `date_time`) VALUES
(1, 2, 'Doctor', 3, 'User', 'hi', '2024-11-28 10:56:25'),
(2, 3, 'User', 2, 'Doctor', 'hello', '2024-11-28 15:40:50'),
(3, 3, 'User', 2, 'Doctor', 'how are you', '2024-11-28 15:41:28'),
(4, 2, 'Doctor', 3, 'User', 'hello', '2024-11-28 15:41:49'),
(5, 2, 'Doctor', 3, 'User', 'hi', '2024-11-28 15:41:55'),
(6, 2, 'Doctor', 3, 'User', 'ok', '2024-11-28 15:51:19'),
(7, 2, 'Doctor', 3, 'User', 'are you free now', '2024-11-28 15:51:30'),
(8, 3, 'User', 2, 'Doctor', 'yes', '2024-11-28 15:51:44'),
(9, 3, 'User', 2, 'Doctor', 'hi', '2024-11-28 15:54:09'),
(10, 3, 'User', 2, 'Doctor', 'uhf', '2024-11-28 15:57:42'),
(11, 3, 'User', 2, 'Doctor', 'hhj', '2024-11-28 15:57:45'),
(12, 3, 'User', 2, 'Doctor', 'hhh', '2024-11-28 15:57:48'),
(13, 3, 'User', 2, 'Doctor', 'hi', '2024-11-28 16:07:04'),
(14, 3, 'User', 2, 'Doctor', 'jjj', '2024-11-28 16:17:50');

-- --------------------------------------------------------

--
-- Table structure for table `complaints`
--

CREATE TABLE `complaints` (
  `complaint_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `complaint` varchar(255) DEFAULT NULL,
  `reply` varchar(255) DEFAULT NULL,
  `date_time` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `complaints`
--

INSERT INTO `complaints` (`complaint_id`, `user_id`, `complaint`, `reply`, `date_time`) VALUES
(1, 1, 'hh', 'Pending', '2024-11-29 11:18:39'),
(2, 1, 'hh', 'Pending', '2024-11-29 11:18:42'),
(3, 1, 'hh', 'Pending', '2024-11-29 11:18:42'),
(4, 1, 'hh', 'Pending', '2024-11-29 11:20:12'),
(5, 1, 'hh', 'Pending', '2024-11-29 11:20:31'),
(6, 1, 'hi', 'Pending', '2024-11-29 11:29:53'),
(7, 1, 'yyyy', 'Pending', '2024-11-29 11:30:00'),
(8, 1, 'nnnn', 'Pending', '2024-11-29 11:30:05'),
(9, 1, 'mmm', 'Pending', '2024-11-29 11:30:08'),
(10, 1, 'hxhdjjdzhsjszhshhdzhsh', 'Pending', '2024-11-29 11:56:42'),
(11, 1, 'gshsh dhdjjd suhehe hjdhdhdhshdhs uhdhjdhshsh', 'Pending', '2024-11-29 11:56:55');

-- --------------------------------------------------------

--
-- Table structure for table `consulting_times`
--

CREATE TABLE `consulting_times` (
  `consulting_id` int(11) NOT NULL,
  `doctor_id` int(11) DEFAULT NULL,
  `day` varchar(255) DEFAULT NULL,
  `start_time` varchar(255) DEFAULT NULL,
  `end_time` varchar(255) DEFAULT NULL,
  `date_time` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `consulting_times`
--

INSERT INTO `consulting_times` (`consulting_id`, `doctor_id`, `day`, `start_time`, `end_time`, `date_time`) VALUES
(1, 1, 'Monday', '10 am', '12 pm', '2024-11-28'),
(2, 2, 'Morning', '9 am', '1:30 pm', '2024-11-29');

-- --------------------------------------------------------

--
-- Table structure for table `doctors`
--

CREATE TABLE `doctors` (
  `doctor_id` int(11) NOT NULL,
  `login_id` int(11) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `gender` varchar(100) NOT NULL,
  `house_name` varchar(255) DEFAULT NULL,
  `place` varchar(255) DEFAULT NULL,
  `landmark` varchar(255) DEFAULT NULL,
  `qualification` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `doctors`
--

INSERT INTO `doctors` (`doctor_id`, `login_id`, `first_name`, `last_name`, `gender`, `house_name`, `place`, `landmark`, `qualification`, `phone`, `email`, `status`) VALUES
(1, 2, 'sam', 'john', 'Male', 'abc', 'kokkala', 'college', 'MD', '9087654321', 'sam@gmail.com', 'Approved'),
(2, 4, 'rr', 'fvgh', 'Female', 'vygvyh', 'Ernakulam', 'vujh', 'gugu', '1234567890', 'vhgh@fgh.bu', 'Approved');

-- --------------------------------------------------------

--
-- Table structure for table `fee`
--

CREATE TABLE `fee` (
  `fee_id` int(11) NOT NULL,
  `doctor_id` int(11) DEFAULT NULL,
  `amount` varchar(255) DEFAULT NULL,
  `date_time` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `fee`
--

INSERT INTO `fee` (`fee_id`, `doctor_id`, `amount`, `date_time`) VALUES
(1, 1, '300', '2024-11-28'),
(2, 2, '200', '2024-11-29');

-- --------------------------------------------------------

--
-- Table structure for table `history`
--

CREATE TABLE `history` (
  `history_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `file_path` text NOT NULL,
  `result` varchar(255) DEFAULT NULL,
  `date_time` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `history`
--

INSERT INTO `history` (`history_id`, `user_id`, `file_path`, `result`, `date_time`) VALUES
(1, 1, 'static/ecg_images/1_date_2024-11-29 16:55:49_uploaded_image.png', 'Based on the analysis, the disease predicted is: Myocardial Infarction.', '2024-11-29 16:55:49'),
(2, 1, 'static/ecg_images/1_date_2024-11-29 16:58:42_uploaded_image.png', 'Based on the analysis, the disease predicted is: Normal.', '2024-11-29 16:58:43'),
(3, 1, 'static/ecg_images/1_date_2024-11-29 16:58:57_uploaded_image.png', 'Based on the analysis, the disease predicted is: Abnormal Heartbeat.', '2024-11-29 16:58:58'),
(4, 1, 'static/ecg_images/1_date_2024-11-29 16:59:08_uploaded_image.png', 'Based on the analysis, the disease predicted is: Normal.', '2024-11-29 16:59:08'),
(5, 1, 'static/ecg_images/1_date_2024-11-29 16:59:15_uploaded_image.png', 'Based on the analysis, the disease predicted is: Abnormal Heartbeat.', '2024-11-29 16:59:16'),
(6, 1, 'static/ecg_images/1_date_2024-11-29 16:59:23_uploaded_image.png', 'Based on the analysis, the disease predicted is: Normal.', '2024-11-29 16:59:24'),
(7, 1, 'static/ecg_images/1_date_2024-11-29 17:00:54_uploaded_image.png', 'Based on the analysis, the disease predicted is: Abnormal Heartbeat.', '2024-11-29 17:00:55'),
(8, 1, 'static/ecg_images/1_date_2024-11-29 17:01:08_uploaded_image.png', 'Based on the analysis, the disease predicted is: Abnormal Heartbeat.', '2024-11-29 17:01:09'),
(9, 1, 'static/ecg_images/1_date_2024-11-29 17:01:16_uploaded_image.png', 'Based on the analysis, the disease predicted is: Abnormal Heartbeat.', '2024-11-29 17:01:17'),
(10, 1, 'static/ecg_images/1_date_2024-11-29 17:01:22_uploaded_image.png', 'Based on the analysis, the disease predicted is: Abnormal Heartbeat.', '2024-11-29 17:01:23'),
(11, 1, 'static/ecg_images/1_date_2024-11-29 17:01:32_uploaded_image.png', 'Based on the analysis, the disease predicted is: Abnormal Heartbeat.', '2024-11-29 17:01:33');

-- --------------------------------------------------------

--
-- Table structure for table `login`
--

CREATE TABLE `login` (
  `login_id` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `user_type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `login`
--

INSERT INTO `login` (`login_id`, `username`, `password`, `user_type`) VALUES
(1, 'admin', 'admin', 'admin'),
(2, 'sam@gmail.com', 'sam', 'Doctor'),
(3, 'uu', 'uu', 'user'),
(4, 'dd', 'dd', 'Doctor'),
(5, 'ss', 'ss', 'user');

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `payment_id` int(11) NOT NULL,
  `booking_id` int(11) DEFAULT NULL,
  `date_time` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`payment_id`, `booking_id`, `date_time`, `status`) VALUES
(1, 1, '2024-11-28 10:55:27', 'Paid'),
(2, 2, '2024-11-29 12:20:52', 'Paid'),
(3, 3, '2024-11-29 12:23:21', 'Paid'),
(4, 4, '2024-11-29 12:23:47', 'Paid');

-- --------------------------------------------------------

--
-- Table structure for table `ratings`
--

CREATE TABLE `ratings` (
  `rating_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `doctor_id` int(11) DEFAULT NULL,
  `rate` varchar(255) DEFAULT NULL,
  `review` varchar(255) DEFAULT NULL,
  `date_time` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `ratings`
--

INSERT INTO `ratings` (`rating_id`, `user_id`, `doctor_id`, `rate`, `review`, `date_time`) VALUES
(1, 1, 1, '5.0', 'kkk', '2024-11-28 14:34:51'),
(2, 1, 2, '3.0', 'hdhjjd', '2024-11-29 12:21:04'),
(3, 2, 1, '2.0', 'bad', '2024-11-29 12:23:32'),
(4, 2, 2, '5.0', 'wow', '2024-11-29 12:23:57');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `login_id` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `house_name` varchar(255) DEFAULT NULL,
  `place` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `login_id`, `first_name`, `last_name`, `house_name`, `place`, `phone`, `email`) VALUES
(1, '3', 'chv', 'vgv', 'ghv', 'vjv', '985247130', 'gccg@ih.com'),
(2, '5', 'bzbz', 'bsbs', 'shhs', 'hshs', '979797949', 'gsgsg@iei.com');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`booking_id`);

--
-- Indexes for table `chat`
--
ALTER TABLE `chat`
  ADD PRIMARY KEY (`chat_id`);

--
-- Indexes for table `complaints`
--
ALTER TABLE `complaints`
  ADD PRIMARY KEY (`complaint_id`);

--
-- Indexes for table `consulting_times`
--
ALTER TABLE `consulting_times`
  ADD PRIMARY KEY (`consulting_id`);

--
-- Indexes for table `doctors`
--
ALTER TABLE `doctors`
  ADD PRIMARY KEY (`doctor_id`);

--
-- Indexes for table `fee`
--
ALTER TABLE `fee`
  ADD PRIMARY KEY (`fee_id`);

--
-- Indexes for table `history`
--
ALTER TABLE `history`
  ADD PRIMARY KEY (`history_id`);

--
-- Indexes for table `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`login_id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`payment_id`);

--
-- Indexes for table `ratings`
--
ALTER TABLE `ratings`
  ADD PRIMARY KEY (`rating_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bookings`
--
ALTER TABLE `bookings`
  MODIFY `booking_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `chat`
--
ALTER TABLE `chat`
  MODIFY `chat_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `complaints`
--
ALTER TABLE `complaints`
  MODIFY `complaint_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `consulting_times`
--
ALTER TABLE `consulting_times`
  MODIFY `consulting_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `doctors`
--
ALTER TABLE `doctors`
  MODIFY `doctor_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `fee`
--
ALTER TABLE `fee`
  MODIFY `fee_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `history`
--
ALTER TABLE `history`
  MODIFY `history_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `login`
--
ALTER TABLE `login`
  MODIFY `login_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `payment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `ratings`
--
ALTER TABLE `ratings`
  MODIFY `rating_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
