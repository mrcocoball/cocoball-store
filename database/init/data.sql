DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `category_id` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `modified_at` datetime(6) DEFAULT NULL,
  `category_name` varchar(255) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO category (category_id, created_at, modified_at, category_name) values
('MT1', now(), now(), '대형 마트'),
('CS2', now(), now(), '편의점'),
('PK6', now(), now(), '주차장'),
('OL7', now(), now(), '주유소, 충전소'),
('SW8', now(), now(), '지하철역'),
('CT1', now(), now(), '문화시설'),
('AT4', now(), now(), '관광명소'),
('AD5', now(), now(), '숙박'),
('FD6', now(), now(), '음식점'),
('CE7', now(), now(), '카페');