USE chillingM;

-- execute this if FK doesn't work
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=1;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=1;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- INITIALIZE DATA
SET SQL_SAFE_UPDATES = 0;
update ingredients set fVD = NULL where fVD = 999999;
update ingredients set fVE = NULL where fVE = 999999;
update ingredients set fVC = NULL where fVC = 999999;
update ingredients set fPotassium = NULL where fPotassium = 999999;
update ingredients set fIron = NULL where fIron = 999999;
update ingredients set fCalcium = NULL where fCalcium = 999999;
update ingredients set fSugar = NULL where fSugar = 999999;
update ingredients set fCholestero = NULL where fCholestero = 999999;
update ingredients set fSaturatedFat = NULL where fSaturatedFat = 999999;
update ingredients set fSodium = NULL where fSodium = 999999;
update ingredients set fCarbohydrate = NULL where fCarbohydrate = 999999;
update ingredients set fTotalFat = NULL where fTotalFat = 999999;
update ingredients set fProtein = NULL where fProtein = 999999;
update ingredients set fCalories = NULL where fCalories = 999999;
update ingredients set expTime = NULL where expTime = 999999;

delimiter //

DROP TRIGGER IF EXISTS set_exp;
CREATE TRIGGER set_exp BEFORE INSERT ON stockitems
FOR EACH ROW
BEGIN
	DECLARE lifetime int(3);
	IF NEW.foodExpDate IS NULL THEN
		SELECT expTime INTO lifetime FROM ingredients WHERE foodID = NEW.foodID;
        SET NEW.foodExpDate = DATE_ADD(CURDATE(), INTERVAL lifetime DAY);
	END IF;
END //

delimiter ;

DESCRIBE userprofile;
INSERT INTO userprofile (userName, userEmail, password) VALUES 
('testUser', 'test@email.com', 'password'),
('Alan', 'z@husky.neu.edu', 'donuts'),
('seanyles', 'seanyles@gmail.com', 'frig'),
('dannyboi', 'Dnguyen@yahoo.com', 'FXman'),
('tz', 'tz@gmail.com', 'spicyFries'),
('ssingh', 'ssingh@northeastern.edu', 'SoftwareEngineeringIsHard'),
('romar', 'tuffaha.o@husky.neu.edu', 'fourPointOh'),
('tomcat', 'tomcat@cartoonnetwork.com', 'bestCartoon'),
('invaderzim', 'zim@alien.com', 'invadearth'),
('Kuzco', 'me@emperor.com', 'groovy');

describe stockitems;
INSERT INTO stockitems (foodID, foodQtty, userID) VALUES 
(1001, 10, 98121941486927873), (1010, 300, 98121941486927873), 
(5146, 1200, 98121941486927873), (6152, 800, 98121941486927873), (4529, 9000, 98121941486927873);

SELECT * from ingredients;
SELECT * FROM userprofile;
INSERT INTO recipes (recipeName, dateCreated, userID, recipeDescription, accessibility) VALUES
('Teriyaki Chicken', CURTIME(), 98121941486927873, 'Mix the sauces.  Marinate chicken for an hour.  Throw chicken in a skillet for 5 minutes on each side. Eat.', 'public'),
('Deep-Fried Butter', CURTIME(), 98121941486927873, 'Deep fry the butter', 'public'),
('Honeyed Turkey', 2017-06-15, 98121941486927877,'Honeybaste that fat turkey.  B a k e.', 'public'),
('Seans secret soup', CURTIME(), 98121941486927875, 'Heat it up', 'private');


