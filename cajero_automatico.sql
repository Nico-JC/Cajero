-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 20-11-2024 a las 20:32:06
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `cajero_automatico`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cajero`
--

CREATE TABLE `cajero` (
  `id_cajero` int(11) NOT NULL,
  `efectivo_disponible` decimal(10,2) NOT NULL DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `cajero`
--

INSERT INTO `cajero` (`id_cajero`, `efectivo_disponible`) VALUES
(1, 31000.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `movimiento`
--

CREATE TABLE `movimiento` (
  `id` int(11) NOT NULL,
  `userID` int(11) NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `tipo` varchar(20) NOT NULL,
  `id_cajero` int(11) NOT NULL,
  `receptor` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `movimiento`
--

INSERT INTO `movimiento` (`id`, `userID`, `monto`, `tipo`, `id_cajero`, `receptor`) VALUES
(3, 1, 100.00, 'Deposito', 1, NULL),
(4, 1, 100.00, 'Deposito', 1, NULL),
(5, 1, 100.00, 'Deposito', 1, NULL),
(6, 1, 100.00, 'Deposito', 1, NULL),
(7, 1, 100.00, 'Deposito', 1, NULL),
(8, 1, 100.00, 'Deposito', 1, NULL),
(9, 1, 400.00, 'Deposito', 1, NULL),
(10, 1, 300.00, 'Deposito', 1, NULL),
(11, 1, 100.00, 'Retiro', 1, NULL),
(12, 1, 100.00, 'Deposito', 1, NULL),
(13, 1, 100.00, 'Deposito', 1, NULL),
(14, 1, 100.00, 'Deposito', 1, NULL),
(15, 1, 310.00, 'Retiro', 1, NULL),
(16, 1, 600.00, 'Retiro', 1, NULL),
(17, 1, 100.00, 'Deposito', 1, NULL),
(18, 1, 300.00, 'Retiro', 1, NULL),
(19, 1, 90.00, 'Retiro', 1, NULL),
(20, 1, 200.00, 'Retiro', 1, NULL),
(21, 1, 1000.00, 'Deposito', 1, NULL),
(22, 1, 100.00, 'Retiro', 1, NULL),
(23, 1, 610.00, 'Retiro', 1, NULL),
(24, 1, 100.00, 'Retiro', 1, NULL),
(25, 1, 1000.00, 'Deposito', 1, NULL),
(26, 1, 290.00, 'Retiro', 1, NULL),
(27, 1, 100.00, 'Retiro', 1, NULL),
(28, 1, 300.00, 'Retiro', 1, NULL),
(29, 1, 110.00, 'Retiro', 1, NULL),
(30, 1, 190.00, 'Retiro', 1, NULL),
(31, 1, 250.00, 'Retiro', 1, NULL),
(32, 1, 100.00, 'Deposito', 1, NULL),
(33, 1, 100.00, 'Deposito', 1, NULL),
(34, 1, 200.00, 'Deposito', 1, NULL),
(35, 2, 3000.00, 'Deposito', 1, NULL),
(37, 2, 200.00, 'Retiro', 1, NULL),
(38, 2, 5.00, 'Retiro', 1, NULL),
(40, 2, 1000.00, 'Transferencia', 1, 1),
(41, 2, 5.00, 'Transferencia', 1, 1),
(42, 1, 455.00, 'Transferencia', 1, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `contraseña` varchar(45) NOT NULL,
  `saldo` decimal(10,2) NOT NULL DEFAULT 0.00,
  `isEmployed` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `nombre`, `contraseña`, `saldo`, `isEmployed`) VALUES
(1, 'messi', '1010', 1000.00, NULL),
(2, 'Rodrigo', '777', 2245.00, NULL),
(3, 'admin', '1231', 0.00, 1),
(5, 'quiero ser messi', 'el10', 0.00, NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `cajero`
--
ALTER TABLE `cajero`
  ADD PRIMARY KEY (`id_cajero`);

--
-- Indices de la tabla `movimiento`
--
ALTER TABLE `movimiento`
  ADD PRIMARY KEY (`id`),
  ADD KEY `userID` (`userID`),
  ADD KEY `cajeroID` (`id_cajero`),
  ADD KEY `receptor` (`receptor`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `cajero`
--
ALTER TABLE `cajero`
  MODIFY `id_cajero` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `movimiento`
--
ALTER TABLE `movimiento`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `movimiento`
--
ALTER TABLE `movimiento`
  ADD CONSTRAINT `movimiento_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `movimiento_ibfk_2` FOREIGN KEY (`id_cajero`) REFERENCES `cajero` (`id_cajero`),
  ADD CONSTRAINT `movimiento_ibfk_3` FOREIGN KEY (`receptor`) REFERENCES `usuario` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
