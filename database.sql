-- MySQL dump 10.16  Distrib 10.3.10-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: j2ee2
-- ------------------------------------------------------
-- Server version	10.3.10-MariaDB-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Detalle`
--

DROP TABLE IF EXISTS `Detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Detalle` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `codProd` varchar(20) NOT NULL,
  `id_venta` int(11) DEFAULT NULL,
  `cantidad` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `codProd` (`codProd`),
  KEY `id_venta` (`id_venta`),
  CONSTRAINT `Detalle_ibfk_1` FOREIGN KEY (`codProd`) REFERENCES `Productos` (`codProd`),
  CONSTRAINT `Detalle_ibfk_2` FOREIGN KEY (`id_venta`) REFERENCES `Ventas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Detalle`
--

LOCK TABLES `Detalle` WRITE;
/*!40000 ALTER TABLE `Detalle` DISABLE KEYS */;
/*!40000 ALTER TABLE `Detalle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Productos`
--

DROP TABLE IF EXISTS `Productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Productos` (
  `codProd` varchar(20) NOT NULL,
  `seccion` varchar(20) NOT NULL,
  `nombreProd` varchar(20) NOT NULL,
  `precio` double DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `importado` tinyint(1) DEFAULT 0,
  `paisOrigen` varchar(40) DEFAULT NULL,
  `stock` int(11) DEFAULT NULL,
  PRIMARY KEY (`codProd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Productos`
--

LOCK TABLES `Productos` WRITE;
/*!40000 ALTER TABLE `Productos` DISABLE KEYS */;
INSERT INTO `Productos` VALUES ('euoua','oeuha','naruto',3242,'2323-03-29',1,'oeas',3),('oeuaeua','uoea','uoeau',3234,'2018-11-13',0,'oeaa',3),('oeuoeuaeuoa','aoeuue','aoeuu',2344,'2018-11-20',0,'',3),('p02380','aououu','oueauu',3279,'2018-11-20',0,'francia',3),('pemdas','pitagoras','gaus',3.141592653589793,'2018-03-14',0,'',0);
/*!40000 ALTER TABLE `Productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Usuarios`
--

DROP TABLE IF EXISTS `Usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Usuarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  `apellidos` varchar(20) NOT NULL,
  `usuario` varchar(20) NOT NULL,
  `contrasena` varchar(20) NOT NULL,
  `pais` varchar(20) DEFAULT NULL,
  `tecnologia` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `usuario` (`usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Usuarios`
--

LOCK TABLES `Usuarios` WRITE;
/*!40000 ALTER TABLE `Usuarios` DISABLE KEYS */;
INSERT INTO `Usuarios` VALUES (1,'sergio','iglesias','sergiotarxz','oeu',NULL,NULL),(3,'Sergio','Iglesias','sergiotarxz1','cancel','espana','x');
/*!40000 ALTER TABLE `Usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Ventas`
--

DROP TABLE IF EXISTS `Ventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Ventas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `conf` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_ventas_usuarios` (`id_usuario`),
  CONSTRAINT `fk_ventas_usuarios` FOREIGN KEY (`id_usuario`) REFERENCES `Usuarios` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Ventas`
--

LOCK TABLES `Ventas` WRITE;
/*!40000 ALTER TABLE `Ventas` DISABLE KEYS */;
/*!40000 ALTER TABLE `Ventas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-09 22:44:55
