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

DROP PROCEDURE IF EXISTS wipe_db;
CREATE PROCEDURE wipe_db ()
BEGIN
	SET SQL_SAFE_UPDATES = 0;
	DELETE FROM foodintake WHERE TRUE;
    DELETE FROM recipeintake WHERE TRUE;
    DELETE FROM userintake WHERE TRUE;
    DELETE FROM promotedrecipe WHERE TRUE;
    DELETE FROM recipeingredients WHERE TRUE;
    DELETE FROM recipes WHERE TRUE;
    DELETE FROM stockitems WHERE TRUE;
    DELETE FROM userprofile WHERE TRUE;
    SET SQL_SAFE_UPDATES = 1;
END //

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

DROP PROCEDURE IF EXISTS make_user;
CREATE PROCEDURE make_user(
	id BIGINT,
    name varchar(45),
    emailAddress varchar(60),
    pass varchar(45)
)
BEGIN
	INSERT INTO userprofile (userName, userEmail, password) VALUES
		(name, emailAddress, pass);
	UPDATE userprofile SET userID = id WHERE userName = name;
END //

DROP PROCEDURE IF EXISTS make_recipe;
CREATE PROCEDURE make_recipe(
	id BIGINT,
    name VARCHAR(45),
    dateMade DATETIME,
    creator BIGINT,
    description VARCHAR(256),
    access enum('public','private')
)
BEGIN
	INSERT INTO recipes (recipeName, dateCreated, userID, recipeDescription, accessibility) VALUES
		(NAME, dateMade, creator, description, access);
	UPDATE recipes SET recipeID = ID WHERE recipeName = NAME AND userID = creator;
END //

DROP PROCEDURE IF EXISTS make_stock;
CREATE PROCEDURE make_stock(
	id BIGINT,
	food BIGINT,
    quantity float(10,3),
    user BIGINT
)
BEGIN
	INSERT INTO stockitems (foodID, foodQtty, userID) VALUES 
		(food, quantity, user);
	UPDATE stockitems SET stockItemID = id WHERE foodID = food AND foodQtty = quantity AND userID = user;
END //

DROP PROCEDURE IF EXISTS make_intake;
CREATE PROCEDURE make_intake(
	id BIGINT,
    intaker BIGINT,
    date DATETIME
)
BEGIN
	INSERT INTO userintake(userID, intakeDate, intakeNote) VALUES (intaker, date, id);
    UPDATE userintake SET intakeID = id WHERE intakeNote = id;
END //

DROP PROCEDURE IF EXISTS init_db;
CREATE PROCEDURE init_db()
BEGIN
    CALL wipe_db();

	SET SQL_SAFE_UPDATES = 0;

	CALL make_user(1, 'testUser', 'test@email.com', 'password');
	CALL make_user(2, 'Alan', 'z@husky.neu.edu', 'donuts');
	CALL make_user(3, 'seanyles', 'seanyles@gmail.com', 'frig');
	CALL make_user(4, 'dannyboi', 'Dnguyen@yahoo.com', 'FXman');
	CALL make_user(5, 'tz', 'tz@gmail.com', 'spicyFries');
	CALL make_user(6, 'ssingh', 'ssingh@northeastern.edu', 'SoftwareEngineeringIsHard');
	CALL make_user(7, 'romar', 'tuffaha.o@husky.neu.edu', 'fourPointOh');
	CALL make_user(8, 'tomcat', 'tomcat@cartoonnetwork.com', 'bestCartoon');
	CALL make_user(9, 'invaderzim', 'zim@alien.com', 'invadearth');
	CALL make_user(10, 'Kuzco', 'me@emperor.com', 'groovy');
		
	CALL make_stock(1, 1001, 10, 1);
	CALL make_stock(2, 1010, 300, 1);
	CALL make_stock(3, 5146, 1200, 1);
	CALL make_stock(4, 6152, 800, 1);
	CALL make_stock(5, 4529, 9000, 1);

	CALL make_recipe(1, 'Teriyaki Chicken', CURDATE(), 1, 'Mix the sauces.  Marinate chicken for an hour.  Throw chicken in a skillet for 5 minutes on each side. Eat.', 'public');
	CALL make_recipe(2, 'Deep-Fried Butter', CURDATE(), 2, 'Deep fry the butter', 'public');
	CALL make_recipe(3, 'Honeyed Turkey', CURDATE(), 2,'Honeybaste that fat turkey.  B a k e.', 'public');
	CALL make_recipe(4, 'Seans secret soup', CURDATE(), 3, 'Heat it up', 'private');

	INSERT INTO recipeingredients (foodID, recipeID, ingredientQtty) VALUES 
		(6212, 4, 500), 
		(4575, 3, 1000), (19296, 3, 20), 
		(1003, 2, 200), (1001, 2, 750),
		(16424, 1, 324), (5059, 1, 1200);
		
	CALL make_intake(1, 1, '2019-01-01 00:00:01');
    CALL make_intake(2, 1, '2019-01-08 00:00:01');
    CALL make_intake(3, 1, '2019-01-09 00:00:01');
    
    CALL make_intake(4, 2, CURTIME());
    CALL make_intake(5, 8, CURTIME());
    
    INSERT INTO recipeintake VALUES
		(1, 2, 1), (3, 3, 2);
    
    INSERT INTO foodintake VALUES
		(2, 200, 1081);
    
    SET SQL_SAFE_UPDATES = 1;
END //

delimiter ;

CALL init_db();
SELECT * FROM userintake JOIN foodintake USING (intakeID);

SELECT *, CAST(intakeDATE AS DATE) as DAY
FROM
	(SELECT intakeID, userID, intakeDATE, intakeQtty, foodID, NULL as serving, NULL as recipeID, 'food' as type 
	FROM userintake JOIN foodintake USING (intakeID)
	UNION
	SELECT intakeID, userID, intakeDATE, NULL as intakeQtty, NULL as foodID, serving, recipeID, 'recipe' as type
	FROM userintake JOIN recipeintake USING (intakeID)) intakes
GROUP BY DAY
ORDER BY intakeDATE DESC;


