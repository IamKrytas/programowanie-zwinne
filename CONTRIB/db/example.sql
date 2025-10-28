-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: mysql
-- Generation Time: Paź 28, 2025 at 08:01 PM
-- Wersja serwera: 8.0.44
-- Wersja PHP: 8.2.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ptracker`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `chat_message`
--

CREATE TABLE `chat_message` (
  `id` bigint NOT NULL,
  `content` varchar(255) NOT NULL,
  `sender` varchar(255) NOT NULL,
  `timestamp` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `project`
--

CREATE TABLE `project` (
  `id` bigint NOT NULL,
  `creation_date` datetime(6) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `done_date` datetime(6) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `teacher_id` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `project_file_ids`
--

CREATE TABLE `project_file_ids` (
  `project_id` bigint NOT NULL,
  `file_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `project_student_ids`
--

CREATE TABLE `project_student_ids` (
  `project_id` bigint NOT NULL,
  `student_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `project_task_ids`
--

CREATE TABLE `project_task_ids` (
  `project_id` bigint NOT NULL,
  `task_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `student`
--

CREATE TABLE `student` (
  `id` bigint NOT NULL,
  `email` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `password` varchar(500) NOT NULL,
  `stationary` bit(1) DEFAULT NULL,
  `surname` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`id`, `email`, `name`, `password`, `stationary`, `surname`) VALUES
(1, 'student@test.com', 'Test', '$2a$10$Y4NaOOp1SKChHQD7yzcAne7NtR5riX390HZ9jpYXzceR4UFgi3gHy', b'0', 'Student'),
(2, 'daniel.rogowski@onet.pl', 'Rogowski', '$2a$10$v4izNIIA/uJxLmReLxP5xO/G2r1gFpyjIMF9D/xZJPwHlcXkgS1Pi', b'1', 'Rogowski');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `task`
--

CREATE TABLE `task` (
  `id` bigint NOT NULL,
  `assigned_student_id` varchar(255) NOT NULL,
  `creation_date` datetime(6) NOT NULL,
  `description` varchar(200) NOT NULL,
  `done_date` datetime(6) NOT NULL,
  `name` varchar(50) NOT NULL,
  `priority` int DEFAULT NULL,
  `project_id` varchar(255) DEFAULT NULL,
  `teacher_id` varchar(255) DEFAULT NULL
) ;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `task_file_ids`
--

CREATE TABLE `task_file_ids` (
  `task_id` bigint NOT NULL,
  `file_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `teacher`
--

CREATE TABLE `teacher` (
  `id` bigint NOT NULL,
  `email` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `password` varchar(500) NOT NULL,
  `surname` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `teacher`
--

INSERT INTO `teacher` (`id`, `email`, `name`, `password`, `surname`) VALUES
(1, 'teacher@test.com', 'Test', '$2a$10$yt.Ar.dN4rD9vZ/r1tSvquavwpWWXG3hvpFvMGeHKUGkjyb9A9hWq', 'Teacher');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `chat_message`
--
ALTER TABLE `chat_message`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `project_file_ids`
--
ALTER TABLE `project_file_ids`
  ADD KEY `FKfvvw1bd51dqi5gggctuxbancw` (`project_id`);

--
-- Indeksy dla tabeli `project_student_ids`
--
ALTER TABLE `project_student_ids`
  ADD KEY `FKeljmaog853a8a5kiuq17n3hqg` (`project_id`);

--
-- Indeksy dla tabeli `project_task_ids`
--
ALTER TABLE `project_task_ids`
  ADD KEY `FKngcohap87sewxw9cpvhshwlxr` (`project_id`);

--
-- Indeksy dla tabeli `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKfe0i52si7ybu0wjedj6motiim` (`email`);

--
-- Indeksy dla tabeli `task`
--
ALTER TABLE `task`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `task_file_ids`
--
ALTER TABLE `task_file_ids`
  ADD KEY `FKhkkfghc5txnmc8wudvij7n6yg` (`task_id`);

--
-- Indeksy dla tabeli `teacher`
--
ALTER TABLE `teacher`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK3kv6k1e64a9gylvkn3gnghc2q` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `chat_message`
--
ALTER TABLE `chat_message`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `project`
--
ALTER TABLE `project`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `student`
--
ALTER TABLE `student`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `task`
--
ALTER TABLE `task`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `teacher`
--
ALTER TABLE `teacher`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `project_file_ids`
--
ALTER TABLE `project_file_ids`
  ADD CONSTRAINT `FKfvvw1bd51dqi5gggctuxbancw` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`);

--
-- Constraints for table `project_student_ids`
--
ALTER TABLE `project_student_ids`
  ADD CONSTRAINT `FKeljmaog853a8a5kiuq17n3hqg` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`);

--
-- Constraints for table `project_task_ids`
--
ALTER TABLE `project_task_ids`
  ADD CONSTRAINT `FKngcohap87sewxw9cpvhshwlxr` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`);

--
-- Constraints for table `task_file_ids`
--
ALTER TABLE `task_file_ids`
  ADD CONSTRAINT `FKhkkfghc5txnmc8wudvij7n6yg` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
