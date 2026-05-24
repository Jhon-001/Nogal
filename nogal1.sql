-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: nogal1
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `carrito`
--

DROP TABLE IF EXISTS `carrito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carrito` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha_actualizacion` datetime(6) DEFAULT NULL,
  `fecha_creacion` datetime(6) DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `usuario_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKjo3wdfqn510qwk6ffx5v9nua8` (`usuario_id`),
  CONSTRAINT `FK8ymop2vbmxmjq6ehl5vj3hpqm` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrito`
--

LOCK TABLES `carrito` WRITE;
/*!40000 ALTER TABLE `carrito` DISABLE KEYS */;
/*!40000 ALTER TABLE `carrito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carrito_detalle`
--

DROP TABLE IF EXISTS `carrito_detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carrito_detalle` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cantidad` int NOT NULL,
  `precio_unitario` decimal(10,2) DEFAULT NULL,
  `subtotal` decimal(10,2) DEFAULT NULL,
  `carrito_id` bigint NOT NULL,
  `producto_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK64qh3ohthoj94ehwxqfqfgrvu` (`carrito_id`),
  KEY `FKsel9wcj28kn9lrj1cyqncyg0p` (`producto_id`),
  CONSTRAINT `FK64qh3ohthoj94ehwxqfqfgrvu` FOREIGN KEY (`carrito_id`) REFERENCES `carrito` (`id`),
  CONSTRAINT `FKsel9wcj28kn9lrj1cyqncyg0p` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrito_detalle`
--

LOCK TABLES `carrito_detalle` WRITE;
/*!40000 ALTER TABLE `carrito_detalle` DISABLE KEYS */;
/*!40000 ALTER TABLE `carrito_detalle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` VALUES (1,'Mesas de diferentes materiales y tamaños','Mesas'),(2,'Sillas para interior y exterior','Sillas'),(3,'Camas y box de diferentes plazas','Camas'),(4,'Roperos y armarios','Roperos'),(5,'Reposteros y muebles de cocina','Reposteros'),(6,'Cómodas y organizadores','Cómodas'),(7,'Colchones ortopédicos y de espuma','Colchones');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_ingreso`
--

DROP TABLE IF EXISTS `detalle_ingreso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_ingreso` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cantidad` int DEFAULT NULL,
  `precio_compra` decimal(10,2) DEFAULT NULL,
  `subtotal` decimal(10,2) DEFAULT NULL,
  `ingreso_id` bigint NOT NULL,
  `producto_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgm7e4m4y4v6g7ltvgxvtyq3i4` (`ingreso_id`),
  KEY `FKjisae987vmeqjk1whtlyywmmd` (`producto_id`),
  CONSTRAINT `FKgm7e4m4y4v6g7ltvgxvtyq3i4` FOREIGN KEY (`ingreso_id`) REFERENCES `ingreso_inventario` (`id`),
  CONSTRAINT `FKjisae987vmeqjk1whtlyywmmd` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_ingreso`
--

LOCK TABLES `detalle_ingreso` WRITE;
/*!40000 ALTER TABLE `detalle_ingreso` DISABLE KEYS */;
INSERT INTO `detalle_ingreso` VALUES (1,2,150.00,300.00,6,1),(2,1,150.00,150.00,7,1),(3,1,150.00,150.00,8,1),(4,1,150.00,150.00,9,1),(5,1,150.00,150.00,10,1),(6,1,200.00,200.00,11,2),(7,1,200.00,200.00,12,2),(8,2,200.00,400.00,13,2),(9,2,25.00,50.00,14,14),(10,2,25.00,50.00,15,14),(11,2,25.00,50.00,16,14),(12,2,25.00,50.00,17,14),(13,2,280.00,560.00,18,7),(14,2,380.00,760.00,19,10),(15,1,350.00,350.00,20,12),(16,1,60.00,60.00,21,5),(17,10,150.00,1500.00,22,16),(18,2,150.00,300.00,23,16),(19,2,120.00,240.00,24,4),(20,2,120.00,240.00,25,4),(21,2,120.00,240.00,26,4),(22,2,120.00,240.00,27,4),(23,1,350.00,350.00,28,12),(24,1,350.00,350.00,29,12),(25,1,350.00,350.00,30,12),(26,1,350.00,350.00,30,12),(27,1,25.00,25.00,31,14),(28,1,280.00,280.00,32,7),(29,10,100.00,1000.00,33,20),(30,10,40.00,400.00,34,21);
/*!40000 ALTER TABLE `detalle_ingreso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_pedido`
--

DROP TABLE IF EXISTS `detalle_pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_pedido` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cantidad` int NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  `pedido_id` bigint NOT NULL,
  `producto_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgqvba9e7dildyw45u0usdj1k2` (`pedido_id`),
  KEY `FK2yc3nts8mdyqf6dw6ndosk67a` (`producto_id`),
  CONSTRAINT `FK2yc3nts8mdyqf6dw6ndosk67a` FOREIGN KEY (`producto_id`) REFERENCES `producto` (`id`),
  CONSTRAINT `FKgqvba9e7dildyw45u0usdj1k2` FOREIGN KEY (`pedido_id`) REFERENCES `pedido` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_pedido`
--

LOCK TABLES `detalle_pedido` WRITE;
/*!40000 ALTER TABLE `detalle_pedido` DISABLE KEYS */;
INSERT INTO `detalle_pedido` VALUES (1,1,320.00,320.00,1,2),(2,2,320.00,640.00,2,2),(3,1,450.00,450.00,2,7),(4,1,320.00,320.00,3,2),(5,1,90.00,90.00,3,5),(6,1,450.00,450.00,4,15),(7,1,195.00,195.00,4,16),(8,1,450.00,450.00,5,15),(9,1,280.00,280.00,5,6),(10,1,90.00,90.00,6,5),(11,1,200.00,200.00,7,20),(12,1,200.00,200.00,8,20),(13,1,200.00,200.00,9,20),(14,1,450.00,450.00,9,15),(15,1,680.00,680.00,10,11),(16,1,90.00,90.00,11,5),(17,1,180.00,180.00,11,4);
/*!40000 ALTER TABLE `detalle_pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `direccion`
--

DROP TABLE IF EXISTS `direccion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `direccion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activa` bit(1) NOT NULL,
  `apellidos_receptor` varchar(100) NOT NULL,
  `departamento` varchar(255) DEFAULT NULL,
  `direccion` varchar(255) NOT NULL,
  `distrito` varchar(255) DEFAULT NULL,
  `dpto_oficina_casa` varchar(50) DEFAULT NULL,
  `es_principal` bit(1) NOT NULL,
  `fecha_actualizacion` datetime(6) DEFAULT NULL,
  `fecha_creacion` datetime(6) DEFAULT NULL,
  `nombre_direccion` varchar(100) NOT NULL,
  `nombre_receptor` varchar(100) NOT NULL,
  `numero` varchar(20) NOT NULL,
  `provincia` varchar(255) DEFAULT NULL,
  `telefono` varchar(9) NOT NULL,
  `usuario_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK73vv55f8i3n3b5me7kprxjr6o` (`usuario_id`),
  CONSTRAINT `FK73vv55f8i3n3b5me7kprxjr6o` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `direccion`
--

LOCK TABLES `direccion` WRITE;
/*!40000 ALTER TABLE `direccion` DISABLE KEYS */;
INSERT INTO `direccion` VALUES (1,_binary '','lopez','Arequipa','Jr. MicaelaLima/Comas','Miraflores','casa',_binary '','2025-12-01 19:01:19.459546','2025-12-01 19:01:19.459546','Jr. Belaunde ','juan','410','Lima','951236987',5),(2,_binary '','cliente','Lima','Jr. Andrés Avelino Caceres','Carabayllo','casa',_binary '','2025-12-02 01:54:18.234722','2025-12-02 01:54:18.234722','Jr. Belaunde ','cliente','456','Lima','951236547',14),(3,_binary '','Espinoza','Lima','Jr. BelaundeLima/Comas','Los Olivos','casa',_binary '','2025-12-02 06:17:16.659545','2025-12-02 06:17:16.659545','Jr. Belaunde ','Jhon','456','Callao','985236452',19);
/*!40000 ALTER TABLE `direccion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingreso_inventario`
--

DROP TABLE IF EXISTS `ingreso_inventario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingreso_inventario` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dias_credito` int DEFAULT NULL,
  `fecha_emision` date DEFAULT NULL,
  `fecha_ingreso` date DEFAULT NULL,
  `fecha_pago` date DEFAULT NULL,
  `igv` decimal(10,2) DEFAULT NULL,
  `metodo_pago` varchar(255) DEFAULT NULL,
  `numero_factura` varchar(255) DEFAULT NULL,
  `subtotal` decimal(10,2) DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `proveedor_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1qwr04jnxkvcfd4mld72i0pcs` (`proveedor_id`),
  CONSTRAINT `FK1qwr04jnxkvcfd4mld72i0pcs` FOREIGN KEY (`proveedor_id`) REFERENCES `proveedor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingreso_inventario`
--

LOCK TABLES `ingreso_inventario` WRITE;
/*!40000 ALTER TABLE `ingreso_inventario` DISABLE KEYS */;
INSERT INTO `ingreso_inventario` VALUES (6,0,'2025-10-22','2025-10-22','2025-10-22',54.00,'contado','F001-1',300.00,354.00,1),(7,0,'2025-10-22','2025-10-22','2025-10-22',27.00,'contado','F001-1',150.00,177.00,1),(8,0,'2025-10-22','2025-10-22','2025-10-22',27.00,'contado','F001-1',150.00,177.00,1),(9,0,'2025-10-22','2025-10-22','2025-10-22',27.00,'contado','F001-1',150.00,177.00,1),(10,0,'2025-10-22','2025-10-22','2025-10-22',27.00,'contado','F001-1',150.00,177.00,1),(11,0,'2025-10-22','2025-10-22','2025-10-22',36.00,'contado','F001-1',200.00,236.00,1),(12,0,'2025-10-22','2025-10-22','2025-10-22',36.00,'contado','F001-1',200.00,236.00,1),(13,0,'2025-10-22','2025-10-22','2025-10-22',72.00,'contado','F001-1',400.00,472.00,1),(14,0,'2025-10-22','2025-10-22','2025-10-22',9.00,'contado','f',50.00,59.00,3),(15,0,'2025-10-22','2025-10-22','2025-10-22',9.00,'contado','f',50.00,59.00,3),(16,0,'2025-10-22','2025-10-22','2025-10-22',9.00,'contado','f',50.00,59.00,3),(17,0,'2025-10-22','2025-10-22','2025-10-22',9.00,'contado','f',50.00,59.00,3),(18,8,'2025-10-22','2025-10-22','2025-10-30',100.80,'credito','F001-1',560.00,660.80,4),(19,0,'2025-10-22','2025-10-22','2025-10-22',136.80,'contado','F002-1',760.00,896.80,8),(20,0,'2025-10-22','2025-10-22','2025-10-22',63.00,'contado','h',350.00,413.00,9),(21,0,'2025-10-22','2025-10-22','2025-10-22',10.80,'contado','l',60.00,70.80,2),(22,0,'2025-10-22','2025-10-22','2025-10-22',270.00,'contado','F001-2',1500.00,1770.00,16),(23,0,'2025-10-22','2025-10-22','2025-10-22',54.00,'contado','F001-3',300.00,354.00,16),(24,0,'2025-10-22','2025-10-22','2025-10-22',43.20,'contado','F001-34445',240.00,283.20,2),(25,0,'2025-10-22','2025-10-22','2025-10-22',43.20,'contado','F001-34445',240.00,283.20,2),(26,0,'2025-10-22','2025-10-22','2025-10-22',43.20,'contado','F001-34445',240.00,283.20,2),(27,0,'2025-10-22','2025-10-22','2025-10-22',43.20,'contado','F001-34445',240.00,283.20,2),(28,0,'2025-10-22','2025-10-22','2025-10-22',63.00,'contado','f',350.00,413.00,9),(29,0,'2025-10-22','2025-10-22','2025-10-22',63.00,'contado','f',350.00,413.00,9),(30,0,'2025-10-22','2025-10-22','2025-10-22',126.00,'contado','f001-56',700.00,826.00,9),(31,0,'2025-10-22','2025-10-22','2025-10-22',4.50,'contado','F00-96',25.00,29.50,3),(32,0,'2025-10-22','2025-10-22','2025-10-22',50.40,'contado','K',280.00,330.40,4),(33,0,'2025-12-02','2025-12-02','2025-12-02',180.00,'contado','N00-55',1000.00,1180.00,1),(34,0,'2025-12-06','2025-12-06','2025-12-06',72.00,'contado','F00-12',400.00,472.00,2);
/*!40000 ALTER TABLE `ingreso_inventario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedido`
--

DROP TABLE IF EXISTS `pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedido` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `codigo_verificacion` varchar(6) DEFAULT NULL,
  `envio` decimal(10,2) NOT NULL,
  `estado` varchar(50) NOT NULL,
  `fecha_pedido` datetime(6) DEFAULT NULL,
  `igv` decimal(10,2) NOT NULL,
  `metodo_pago` varchar(50) NOT NULL,
  `notas` text,
  `numero_pedido` varchar(50) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `direccion_id` bigint NOT NULL,
  `tarjeta_id` bigint NOT NULL,
  `usuario_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKj0l80ge0fmklge54qlnx8ovur` (`numero_pedido`),
  KEY `FKeuawl7ohmc0vexy5jwu7d7bjw` (`direccion_id`),
  KEY `FK32ymtufyrs7siep4rlo66r58g` (`tarjeta_id`),
  KEY `FK6uxomgomm93vg965o8brugt00` (`usuario_id`),
  CONSTRAINT `FK32ymtufyrs7siep4rlo66r58g` FOREIGN KEY (`tarjeta_id`) REFERENCES `tarjeta` (`id`),
  CONSTRAINT `FK6uxomgomm93vg965o8brugt00` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
  CONSTRAINT `FKeuawl7ohmc0vexy5jwu7d7bjw` FOREIGN KEY (`direccion_id`) REFERENCES `direccion` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedido`
--

LOCK TABLES `pedido` WRITE;
/*!40000 ALTER TABLE `pedido` DISABLE KEYS */;
INSERT INTO `pedido` VALUES (1,'9490',0.00,'ENTREGADO','2025-12-01 19:01:59.405185',57.60,'TARJETA',NULL,'PED-20251201140159-5670',320.00,320.00,1,1,5),(2,'72942',0.00,'ENVIADO','2025-12-02 00:08:50.974900',196.20,'TARJETA',NULL,'PED-20251201190850-6310',1090.00,1090.00,1,1,5),(3,'3323',0.00,'CANCELADO','2025-12-02 00:45:39.956517',73.80,'TARJETA',NULL,'PED-20251201194539-7326',410.00,410.00,1,1,5),(4,'135402',0.00,'PROCESANDO','2025-12-02 01:04:29.879456',116.10,'TARJETA',NULL,'PED-20251201200429-6662',645.00,645.00,1,1,5),(5,'0180',0.00,'PENDIENTE','2025-12-02 01:09:03.306928',131.40,'TARJETA',NULL,'PED-20251201200903-4764',730.00,730.00,1,1,5),(6,'56362',15.00,'PENDIENTE','2025-12-02 01:54:56.944320',16.20,'TARJETA',NULL,'PED-20251201205456-9645',90.00,105.00,2,2,14),(7,'505837',0.00,'PENDIENTE','2025-12-02 02:02:22.595175',36.00,'TARJETA',NULL,'PED-20251201210222-1796',200.00,200.00,2,2,14),(8,'86929',0.00,'PENDIENTE','2025-12-02 03:32:29.245434',36.00,'TARJETA',NULL,'PED-20251201223229-3223',200.00,200.00,2,2,14),(9,'210923',0.00,'ENTREGADO','2025-12-02 06:18:11.749374',117.00,'TARJETA',NULL,'PED-20251202011811-7567',650.00,650.00,3,3,19),(10,'41772',0.00,'ENTREGADO','2025-12-05 22:09:52.117891',122.40,'TARJETA',NULL,'PED-20251205170952-6728',680.00,680.00,1,1,5),(11,'9605',0.00,'ENTREGADO','2025-12-06 13:03:31.841171',48.60,'TARJETA',NULL,'PED-20251206080331-7674',270.00,270.00,1,1,5);
/*!40000 ALTER TABLE `pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto`
--

DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activo` bit(1) NOT NULL,
  `caracteristicas` varchar(255) DEFAULT NULL,
  `color` varchar(255) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `dimensiones` varchar(255) DEFAULT NULL,
  `material` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  `precio_compra` decimal(10,2) NOT NULL,
  `precio_venta` decimal(10,2) NOT NULL,
  `stock` int NOT NULL,
  `categoria_id` bigint NOT NULL,
  `proveedor_id` bigint NOT NULL,
  `imagen_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKodqr7965ok9rwquj1utiamt0m` (`categoria_id`),
  KEY `FKid8vjxky5juk3fnuc1sb9qarf` (`proveedor_id`),
  CONSTRAINT `FKid8vjxky5juk3fnuc1sb9qarf` FOREIGN KEY (`proveedor_id`) REFERENCES `proveedor` (`id`),
  CONSTRAINT `FKodqr7965ok9rwquj1utiamt0m` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto`
--

LOCK TABLES `producto` WRITE;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` VALUES (1,_binary '','Patas reforzadas, superficie lisa','Negro','Mesa de fierro resistente para 4 personas','1.20m x 0.80m x 0.75m','Fierro','Mesa de Fierro para 4 personas',150.00,250.00,34,1,1,'assets/img/mesa-fierro-4.jpg'),(2,_binary '','Estructura robusta, fácil limpieza','Negro','Mesa de fierro para 6 personas','1.50m x 0.90m x 0.75m','Fierro','Mesa de Fierro para 6 personas',200.00,320.00,9,1,1,'assets/img/mesa-fierro-6.jpg'),(3,_binary '','Respaldo ergonómico, asiento acolchado','Negro/Rojo','Juego de 4 sillas de fierro','0.45m x 0.45m x 0.85m','Fierro','Silla de Fierro para 4 personas',80.00,120.00,20,2,1,'assets/img/sillas-fierro.jpg'),(4,_binary '','Ideal para exterior, fácil transporte','Blanco','Mesa plástica resistente a la intemperie','1.00m x 1.00m x 0.70m','Plástico','Mesa de Plástico Rey para 4 personas',120.00,180.00,22,1,2,'assets/img/mesa-plastico.jpg'),(5,_binary '','Apilables, resistentes al sol','Azul','Juego de 4 sillas plásticas','0.40m x 0.40m x 0.80m','Plástico','Silla de Plástico Rey para 4 personas',60.00,90.00,29,2,2,'assets/img/sillas-plastico.jpg'),(6,_binary '','3 cajones, ruedas incluidas','Blanco','Cómoda plástica organizadora','1.50m x 0.45m x 0.90m','Plástico','Cómoda de Plástico Rey 1½ metros',180.00,280.00,11,6,2,'assets/img/comoda-plastico.jpg'),(7,_binary '','Acabado barnizado, patas torneadas','Natural','Mesa de pino macizo para comedor','1.60m x 0.90m x 0.75m','Madera Pino','Mesa de Pino para 6 personas',280.00,450.00,8,1,4,'assets/img/mesa-pino.jpg'),(8,_binary '','Respaldo alto, asiento cómodo','Natural','Juego de 6 sillas de pino','0.42m x 0.42m x 0.88m','Madera Pino','Silla de Pino para 6 personas',120.00,190.00,18,2,4,'assets/img/sillas-pino.jpg'),(9,_binary '','2 puertas, 3 repisas internas','Blanco/Café','Ropero de melamine de 2 puertas','2.00m x 0.60m x 0.55m','Melamina','Ropero Melamine 2 metros',320.00,520.00,8,4,8,'assets/img/ropero-melamine.jpg'),(10,_binary '','Vitrina superior, 2 puertas inferiores','Blanco','Repostero de cocina con vitrina','1.50m x 0.40m x 2.20m','Melamina','Repostero Melamine 1½ metro',380.00,620.00,7,5,8,'assets/img/repostero-melamine.jpg'),(11,_binary '','Base slat, estructura reforzada','Natural','Cama box de eucalipto reforzado','1.40m x 1.90m x 0.30m','Madera Eucalipto','Cama Box Eucalipto 2 Plazas',420.00,680.00,3,3,7,'assets/img/cama-box.jpg'),(12,_binary '','Respaldo lumbar, funda extraíble','Blanco','Colchón ortopédico de alta densidad','1.40m x 1.90m x 0.20m','Espuma Ortopédica','Colchón Ortopédico Paraíso 2 Plazas',350.00,580.00,12,7,9,'assets/img/colchon-paraiso.jpg'),(13,_binary '','Confort medio, fácil mantenimiento','Gris','Colchón de espuma alta resiliencia','1.00m x 1.90m x 0.18m','Espuma HR','Colchón de Espuma Cisne 1½ Plaza',220.00,360.00,10,7,10,'assets/img/colchon-cisne.jpg'),(14,_binary '','Resistente, fácil de limpiar','Rojo','Banquito plástico multiusos','0.30m x 0.30m x 0.30m','Plástico','Banquito de Plástico BM',25.00,45.00,59,2,3,'assets/img/banquito-plastico.jpg'),(15,_binary '','Espejo incluido, 3 cajones','Blanco','Tocador con espejo y cajones','1.00m x 0.45m x 1.80m','Melamina','Tocador Melamine 1 metro',280.00,450.00,3,6,8,'assets/img/tocador-melamine.jpg'),(16,_binary '','Perfecta para departamentos o cocinas comedor.','Negro',NULL,'0.90m x 0.90m x 0.75m','Roble','Mesa cuadrada 4 personas',150.00,195.00,21,1,16,NULL),(20,_binary '','Patas reforzadas','plomo',NULL,'1.20 x 0.80 x 0.80','Fierro','Mesa fierro para 8 personas',100.00,200.00,17,1,1,'https://i.postimg.cc/CKGWSmRX/Mesa-fierro-8-personas.jpg'),(21,_binary '','Silla comoda para salir','rojo',NULL,'0.40 x 0.40 x 0.50','Plastico','Silla de Plástico Rey',40.00,90.00,10,2,2,NULL);
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proveedor`
--

DROP TABLE IF EXISTS `proveedor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proveedor` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activo` bit(1) NOT NULL,
  `contacto` varchar(255) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `material_especialidad` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  `ruc` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKawwnjxxy3vxst1ynt3tlqqbhu` (`ruc`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedor`
--

LOCK TABLES `proveedor` WRITE;
/*!40000 ALTER TABLE `proveedor` DISABLE KEYS */;
INSERT INTO `proveedor` VALUES (1,_binary '','Juan Pérez','Av. Los Constructores 123, Carabayllo','tallerfierro@gmail.com','Fierro','Taller Fierro','20123456781','987654321'),(2,_binary '','María Rodríguez','Mz. A Lt. 5, AA.HH. El Progreso, Carabayllo','reyplastico@hotmail.com','Plástico','Rey (Plástico)','20123456782','987654322'),(3,_binary '','Carlos López','Calle Las Flores 456, Carabayllo','bmplasticos@gmail.com','Plástico','BM (Plástico)','20123456783','987654323'),(4,_binary '','Roberto Silva','Jr. Los Pinos 789, Carabayllo','tallerpino@yahoo.com','Madera Pino','Taller Pino','20123456784','987654324'),(5,_binary '','Pedro Gómez','Av. La Molina 234, Carabayllo','capironamaderas@gmail.com','Madera Capirona','Taller Capirona','20123456785','987654325'),(6,_binary '','Luis Martínez','Mz. B Lt. 12, Carabayllo','tallertornillo@outlook.com','Madera Tornillo','Taller Tornillo','20123456786','987654326'),(7,_binary '','Ana Torres','Calle Los Eucaliptos 567, Huaral','tarimaseucalipto@gmail.com','Madera Eucalipto/Roble','Taller Tarimas','20123456787','987654327'),(8,_binary '','Jorge Díaz','Av. Industrial 890, Carabayllo','melamine.nogal@gmail.com','Melamina','Taller Melamine','20123456788','987654328'),(9,_binary '','Sofía Castro','Av. Argentina 1234, Lima Cercado','ventas@colchonesparaiso.com','Colchones','Paraíso','20123456789','987654329'),(10,_binary '','Miguel Ángel','Jr. Huánuco 567, La Victoria','info@colchonescisne.com.pe','Colchones','Cisne','20123456790','987654330'),(11,_binary '','Carmen Ruiz','Av. Abancay 890, Lima Centro','romantic.colchones@gmail.com','Colchones','Romantic','20123456791','987654331'),(12,_binary '','Raúl Mendoza','Calle Moquegua 234, Lima','karisma@colchoneskarisma.com','Colchones','Karisma','20123456792','987654332'),(13,_binary '','Elena Vargas','Av. Bolivia 456, Breña','forli.colchones@hotmail.com','Colchones','Forli','20123456793','987654333'),(14,_binary '','Daniel Ríos','Jr. Ica 789, Lima','deencanto.colchones@gmail.com','Colchones','De Encanto','20123456794','987654334'),(15,_binary '\0','Jannet Herrera','Jr. Los Angeles, 652, Lima, Los olivos','talleralgodon@gmail.com','Algodón','Taller Algodón','20136548455','965231454'),(16,_binary '\0','Juan Soto','Jr. Belaunde, Lima/Comas','juansoto@gmail.com','Madera','Carpintero','25413485135','963852741'),(17,_binary '\0','Luis Lopez','Jr. Hacienda, Lima/Carabayllo','calidoso@gmail.com','Madera','Sellador','20145678956','963852741');
/*!40000 ALTER TABLE `proveedor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reporte_entrega`
--

DROP TABLE IF EXISTS `reporte_entrega`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reporte_entrega` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `archivos_adjuntos` text,
  `codigo_verificacion` varchar(255) NOT NULL,
  `estado` varchar(255) NOT NULL,
  `fecha_entrega` datetime(6) NOT NULL,
  `foto_url` text,
  `observaciones` varchar(255) DEFAULT NULL,
  `pedido_id` bigint NOT NULL,
  `repartidor_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKl3yl1bcipl2n0ncr4cj35cw07` (`pedido_id`),
  KEY `FKmnot2fpuuxbmq69d4wkgf13id` (`repartidor_id`),
  CONSTRAINT `FKl3yl1bcipl2n0ncr4cj35cw07` FOREIGN KEY (`pedido_id`) REFERENCES `pedido` (`id`),
  CONSTRAINT `FKmnot2fpuuxbmq69d4wkgf13id` FOREIGN KEY (`repartidor_id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reporte_entrega`
--

LOCK TABLES `reporte_entrega` WRITE;
/*!40000 ALTER TABLE `reporte_entrega` DISABLE KEYS */;
INSERT INTO `reporte_entrega` VALUES (1,NULL,'9490','ENTREGADO','2025-12-01 19:03:24.730227',NULL,'Entrega exitosa sin observaciones',1,16),(2,NULL,'72942','ENTREGADO','2025-12-02 00:46:01.575060',NULL,'Entrega exitosa sin observaciones',2,16),(3,NULL,'41772','ENTREGADO','2025-12-05 22:11:43.591349',NULL,'Entrega exitosa sin observaciones',10,16),(4,NULL,'210923','ENTREGADO','2025-12-06 04:42:57.067915',NULL,'Entrega exitosa sin observaciones',9,28),(5,NULL,'9605','ENTREGADO','2025-12-06 13:07:18.975967',NULL,'Entrega exitosa sin observaciones',11,16);
/*!40000 ALTER TABLE `reporte_entrega` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tarjeta`
--

DROP TABLE IF EXISTS `tarjeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tarjeta` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `anio_expiracion` int NOT NULL,
  `fecha_actualizacion` datetime(6) DEFAULT NULL,
  `fecha_creacion` datetime(6) DEFAULT NULL,
  `mes_expiracion` int NOT NULL,
  `nombre_titular` varchar(255) NOT NULL,
  `numero_enmascarado` varchar(255) NOT NULL,
  `predeterminada` bit(1) NOT NULL,
  `tipo` varchar(255) NOT NULL,
  `usuario_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgb2oyupb6o2oppce0ow66ykg5` (`usuario_id`),
  CONSTRAINT `FKgb2oyupb6o2oppce0ow66ykg5` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tarjeta`
--

LOCK TABLES `tarjeta` WRITE;
/*!40000 ALTER TABLE `tarjeta` DISABLE KEYS */;
INSERT INTO `tarjeta` VALUES (1,2031,'2025-12-01 19:01:49.952212','2025-12-01 19:01:49.952212',9,'JHON','****2138',_binary '','DEFAULT',5),(2,2033,'2025-12-02 01:54:49.285370','2025-12-02 01:54:49.285370',11,'LUIS HERNANDEZ','****8168',_binary '','DEFAULT',14),(3,2031,'2025-12-02 06:16:47.418959','2025-12-02 06:16:47.418959',9,'JUAN','****1561',_binary '','DEFAULT',19);
/*!40000 ALTER TABLE `tarjeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apellidos` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `nombres` varchar(255) NOT NULL,
  `numero_documento` varchar(12) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` varchar(255) NOT NULL,
  `telefono` varchar(9) NOT NULL,
  `tipo_documento` varchar(20) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK5171l57faosmj8myawaucatdw` (`email`),
  UNIQUE KEY `UKcyd6xjxln9p38cm60dkrox5no` (`numero_documento`),
  UNIQUE KEY `UK863n1y3x0jalatoir4325ehal` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'Espinoza','jhon@gamil.com','Jhon','78541238','1234','cliente','963258744','DNI','jhon_mos'),(2,'Perez','logistico@nogal.com','Juan','87654321','$2a$12$jFgmlq8kelyd5aKaineAGOXgEFQCb3A8kcIe/8FFGboaz4hTZ30Cm','logistico','987654321','DNI','logistico'),(3,'Sistema','admin@nogal.com','Administrador','12345678','$2a$12$7Xr7VMfiEU.GmiOZghxsleg3i.SZyyHvMdEpu4lfJSYha74MfzHpO','admin','912345678','DNI','admin'),(4,'lopez','juan@gmail.com','juan','85236987','123456','cliente','951236987','DNI','juan'),(5,'lopez','joel@gmail.com','juan','74123658','$2a$12$H7CYkZ9zbHkpcMn/1Nv4T.tyqXbY2S7RbXW7Jh0PSjSRL36YUi.Zu','cliente','951236987','DNI','joel'),(6,'Lopez','jhon65@gmail.com','Jhon','85236945','123456','cliente','951585214','DNI','jhon'),(11,'Principal','logtico@nogal.com','Logístico','88524321','$2a$12$tRH/qp0p8vN2HlfsyM0/ZeNv62JEnA5Vxg4J/6SEeX6.WOl1/1nDa','logistico','987654321','DNI','logis'),(13,'Sistema','adn@nogal.com','Administrador','12652678','$2a$12$8KuUFEXG4bKpnLw89muvTuTdUve4l80DqBjlYhoPglW1ktevWDlVW','admin','912741678','DNI','admin1'),(14,'cliente','cliente@gmail.com','cliente','85214753','123456','cliente','951236547','DNI','cliente'),(15,'Espinoza','jo@gmail.com','Jhon','45678914','123456','cliente','989456412','DNI','go'),(16,'Lopez','repartidormain@gamil.com','Lucas','75915875','$2a$12$QTH1LPJa1faQl0leeRwPSO7Xsv46eGuYgQU5xtSwVhHGfd/pj9zDW','repartidor','920596569','DNI','repartidor'),(17,'Espinoza','calidad@gmail.com','Jhon','73091793','123456','cliente','965645123','DNI','reparti'),(18,'Espinoza','contacto@ferremax.com','Jhon','23654789','123456','logistico','951236587','DNI','log'),(19,'Espinoza','ventas@indeco.com','Jhon','74125698','$2a$12$oP37KaQb482yysodPDQbiuAimoO2i.VRRJx.YaBBL.xuNyWJ1OXLO','cliente','985236452','DNI','re'),(20,'Espinoza','rep@gmail.com','Jhon','74125874','123456','cliente','963582148','DNI','r'),(21,'Ramirez Lopez','mirco@gmail.com','Mirco','25631478','$2a$12$bkW/5Bay/Pm/n5jONM8qhOhyapZBpuPG4n52XBaCEhLxjDG9Gp35y','cliente','965231458','DNI','mirco'),(22,'Lopez','admin12@gamil.com','Lucas','35915875','$2a$12$C2IQzhrIn4dwlELgp.x.S.Zep7djLSyhUAHNL7HW7nGuFjrGxwQS.','admin','920596569','DNI','admin2'),(23,'Soto','carla@gmail.com','Carla','36521475','$2a$12$.WTZim3uUgUkgT6PUh28ROqEQjluFbOMAYMX/sbiz6Gdpa/sMY.rG','cliente','931653181','DNI','carla'),(24,'Calidasos','calidad1@gmail.com','Calidad','85236985','$2a$12$TrxA867ZA0whOcEVSVcQ6.b48vUhZlBXGw2mP6T87Psiep4KJphle','logistico','951236854','DNI','calidad'),(25,'UPN','joseph@gmail.com','joseph','74125632','$2a$12$/k62EtO2R7L02z8f7rDtjeqNFGueQthEZCqDniJJDNL3z8eHt3Rsi','repartidor','985632145','DNI','joseph'),(26,'Espinoza','jol@gmail.com','Jhon','25485236','$2a$12$1eyZ2491pWnF.q777nRa6OiWrElLG/jeRvVMEkj.KM/dwxxKR8SzS','repartidor','985236521','DNI','rep'),(27,'Espinoza','contacto1@ferremax.com','Jhon','74125652','$2a$12$p8Gjh9D4J4vQDWF8ex82feiTsvPZkM9.ER2h616sAtd/AB52NMnAK','repartidor','951236945','DNI','carlos'),(28,'Garro','gerar@gmail.com','Gerar','25896321','$2a$12$TtCOPn8VKXkhobwVWNq/iO2/wRN5oLhLCzOU6kmW5rD926./uB9zC','repartidor','985632145','DNI','Gerar'),(29,'Hernandez','angel@gmail.com','angel','36985214','$2a$12$9ia7l6rtz3iV/TbiOEznf.D8Xzjc5LRZDFpwWbFUo26Hg140Asf8m','logistico','985241256','DNI','angel');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-06  9:43:01
