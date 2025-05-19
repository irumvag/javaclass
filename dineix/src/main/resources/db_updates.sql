-- SQL script to add tables for purchase history and redemption tracking if they don't exist

-- Check if user_meal_purchases table exists and create it if it doesn't
CREATE TABLE IF NOT EXISTS `user_meal_purchases` (
  `purchase_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `package_id` int(11) NOT NULL,
  `meals_remaining` int(11) NOT NULL,
  `purchase_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `redemption_code` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`purchase_id`),
  KEY `user_id` (`user_id`),
  KEY `package_id` (`package_id`),
  CONSTRAINT `user_meal_purchases_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `user_meal_purchases_ibfk_2` FOREIGN KEY (`package_id`) REFERENCES `meal_packages` (`package_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Check if redemption_logs table exists and create it if it doesn't
CREATE TABLE IF NOT EXISTS `redemption_logs` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `purchase_id` int(11) DEFAULT NULL,
  `restaurant_id` int(11) DEFAULT NULL,
  `redemption_code` varchar(6) DEFAULT NULL,
  `success` tinyint(1) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`log_id`),
  KEY `purchase_id` (`purchase_id`),
  KEY `restaurant_id` (`restaurant_id`),
  CONSTRAINT `redemption_logs_ibfk_1` FOREIGN KEY (`purchase_id`) REFERENCES `user_meal_purchases` (`purchase_id`),
  CONSTRAINT `redemption_logs_ibfk_2` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`restaurant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Add indexes for better performance
CREATE INDEX IF NOT EXISTS idx_redemption_code ON user_meal_purchases (redemption_code);
CREATE INDEX IF NOT EXISTS idx_purchase_date ON user_meal_purchases (purchase_date);
CREATE INDEX IF NOT EXISTS idx_redemption_timestamp ON redemption_logs (timestamp);