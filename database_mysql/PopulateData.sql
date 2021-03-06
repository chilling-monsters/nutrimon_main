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

DROP TRIGGER IF EXISTS userIntake_BEFORE_INSERT;
ALTER TABLE recipeintake DROP FOREIGN KEY fk_recipeIntake_userIntake;
ALTER TABLE foodintake DROP FOREIGN KEY fk_foodIntake_userIntake;
ALTER TABLE userintake MODIFY COLUMN intakeID BIGINT(20) NOT NULL AUTO_INCREMENT;
ALTER TABLE recipeintake ADD CONSTRAINT fk_recipeIntake_userIntake FOREIGN KEY (intakeID) REFERENCES userintake (intakeID);
ALTER TABLE foodintake ADD CONSTRAINT fk_foodIntake_userIntake FOREIGN KEY (intakeID) REFERENCES userintake (intakeID);
delimiter //

DROP PROCEDURE IF EXISTS wipe_db //
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

DROP TRIGGER IF EXISTS set_exp //
CREATE TRIGGER set_exp BEFORE INSERT ON stockitems
FOR EACH ROW
BEGIN
	DECLARE lifetime int(3);
	IF NEW.foodExpDate IS NULL THEN
		SELECT expTime INTO lifetime FROM ingredients WHERE foodID = NEW.foodID;
        SET NEW.foodExpDate = DATE_ADD(CURDATE(), INTERVAL lifetime DAY);
	END IF;
END //

DROP TRIGGER IF EXISTS set_intake_date //
CREATE TRIGGER set_intake_date BEFORE INSERT ON userintake
FOR EACH ROW
BEGIN
	SET NEW.intakeDate = IFNULL(NEW.intakeDate, CURTIME());
END //

DROP PROCEDURE IF EXISTS make_user //
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

DROP PROCEDURE IF EXISTS make_recipe //
CREATE PROCEDURE make_recipe(
	id BIGINT,
    name VARCHAR(45),
    dateMade DATETIME,
    creator BIGINT,
    cookTime FLOAT,
    category VARCHAR(15),
    description VARCHAR(256),
    access enum('public','private')
)
BEGIN
	INSERT INTO recipes (recipeName, dateCreated, userID, recipeCookTime, recipeCategory, recipeDescription, accessibility) VALUES
		(NAME, dateMade, creator, cookTime, category, description, access);
	UPDATE recipes SET recipeID = ID WHERE recipeName = NAME AND userID = creator;
END //

DROP PROCEDURE IF EXISTS make_stock //
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

DROP PROCEDURE IF EXISTS make_intake //
CREATE PROCEDURE make_intake(
	id BIGINT,
    intaker BIGINT,
    date DATETIME
)
BEGIN
	INSERT INTO userintake(userID, intakeDate, intakeNote) VALUES (intaker, date, id);
    UPDATE userintake SET intakeID = id WHERE intakeNote = id;
END //

DROP PROCEDURE IF EXISTS init_db //
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
    CALL make_user(11, 'Olivia', 'helloliver@outlook.com', '0u0I1&6&Q');
    CALL make_user(12, 'PengQ', 'quan.pe@husky.neu.edu', '%88Y3D42c');
    CALL make_user(13, 'benny', 'benten@icould.com', 'BERYMPORKH');
    CALL make_user(14, 'Noah', 'noalceded@gmail.com', 'Ky!q85O10$');
    CALL make_user(15, 'liam', 'vsliam0@yahoo.com', 'hvG!Q77&og');
    CALL make_user(16, 'Saeed', 'alocrroi@yahoo.com', 'RANDpAtRuc');
    CALL make_user(17, 'selinee', 'ylzhao@optonline.net', 'atortYlvIL');
    CALL make_user(18, 'Aaron', 'alphamon@icloud.com', 'qnLlTMPdAo');
    CALL make_user(19, 'timeam', 'timk@live.com', 'f&YuaJeCvl');
    CALL make_user(20, 'Robby', 'obesity@verizon.net', 'sXPN$S');
    CALL make_user(21, 'Mia', 'mjwell@yahoo.com', 'o6o3xhslyx');
    CALL make_user(22, 'Ella', '', 'bn3g8h');
    CALL make_user(23, 'sebastian', 'cielishere@comcast.net', 'T#5HHFZ2WI4');
    CALL make_user(24, 'carter', 'mrcarter@gmail.com', 'r!%u%2gf2n');
    CALL make_user(25, 'JohnCena', 'empathy@yahoo.com', 'BERYMPORKH');
    CALL make_user(26, 'Mike', 'joehall@me.com', 'S7B0E7NJ%T!');
    CALL make_user(27, 'Jayden', 'miojay@live.com', '@4ND43MFI#');
    CALL make_user(28, 'Chilipepper', 'rednhotnjuicy@gmail.com', 'C4V8xe6LSu');
    CALL make_user(29, 'Sofia', 'proudcolombian@icloud.com', 'bonicaPaga');
    CALL make_user(30, 'Danlin', 'jia.da@husky.neu.edu', 'B6Z064G#S9');

		
	CALL make_stock(1, 1001, 10, 1);
	CALL make_stock(2, 1010, 300, 1);
	CALL make_stock(3, 5146, 1200, 1);
	CALL make_stock(4, 6152, 800, 1);
	CALL make_stock(5, 4529, 9000, 1);
    CALL make_stock(6, 8566, 459, 1);
	CALL make_stock(7, 4529, 796, 1);
    CALL make_stock(8, 1003, 500, 1);
    CALL make_stock(0, 1001, 2000, 1);
    

	CALL make_recipe(1, 'Teriyaki Chicken', CURDATE(), 1, 25, 'Dinner', 'Mix the sauces.  Marinate chicken for an hour.  Throw chicken in a skillet for 5 minutes on each side. Eat.', 'public');
	CALL make_recipe(2, 'Deep-Fried Butter', CURDATE(), 30, 20, 'Lunch', 'Deep fry the butter', 'public');
	CALL make_recipe(3, 'Honeyed Turkey', CURDATE(), 2, 60, 'Dinner', 'Honeybaste that fat turkey.  B a k e.', 'public');
	CALL make_recipe(4, 'Seans secret soup', CURDATE(), 3, 5, 'Snack', 'Heat it up', 'private');
    CALL make_recipe(5, 'Chocolate Chip Cookies', CURDATE(), 4, 20, 'Snack', 'Cream together the butter, brown sugar, and white sugar in a bowl. Stir in the chocolate chips and nuts. Bake for 10 to 12 minutes in 350 degrees F oven', 'public');
    CALL make_recipe(6, 'Glazed Ham', CURDATE(), 5, 60, 'Dinner', 'Skim fat from the top of the drippings, and discard. Bake for 30 to 40 minutes in the preheated 250 degress F oven. ', 'public');
    CALL make_recipe(7, 'Beef with Broccoli', CURDATE(), 7, 20, 'Lunch', 'Slicing the beef into small pieces, In a bowl, mix together the soy sauce, cornstarch, sherry, brown sugar, ginger and garlic. Add the beef and broccoli and the sauce into the skillet', 'public');
    CALL make_recipe(8, 'Black Bean–Stuffed Sweet Potatoes', CURDATE(), 8, 35, 'Lunch', 'The sweet potato is packed with protein and fiber. Add toppings — like avocado or sour cream', 'public');
    CALL make_recipe(9, 'Sheet Pan Sausage & Veggies', CURDATE(), 9, 20, 'Dinner', 'Line the sheet pan with parchment or foil for the quickest cleanup — then serve over rice, stuffed into a pita, or on its own.', 'public');
    CALL make_recipe(10, 'Upgraded Instant Ramen', CURDATE(), 10, 10, 'Snack', 'Fry some bacon and an egg, and add all of them and some green onions into the ramen.', 'public');
    CALL make_recipe(11, 'Zesty Italian Lunch Wrap', CURDATE(), 14, 10, 'Lunch', 'Layer meats, cheese, lettuce leaves, and banana peppers on top of a flour tortilla. Roll the wrap up tightly.', 'public');
    CALL make_recipe(12, 'Herbed Skillet Chicken', CURDATE(), 15, 15, 'Dinner', 'Toss white mushrooms and halved red onion. Add olive oil and fresh thyme leaves in 12-in. cast-iron or heavy skillet and top with 4 chicken-leg quarters; Sprinkle with salt and pepper and roast at 450°F 35 to 40 min.', 'public');
    CALL make_recipe(13, 'Grilling Thick Steaks', CURDATE(), 16, 50, 'Dinner', 'Season steak generously with salt and black pepper on both sides. Cook steak until crust forms ', 'public');
    CALL make_recipe(14, 'Baked Salmon Fillets Dijon', CURDATE(), 17, 30, 'Dinner', 'Place salmon skin-side down on foil. Spread a thin layer of mustard on the top of each fillet, and season with salt and pepper.', 'public');
    CALL make_recipe(15, 'Hamburger Stroganoff', CURDATE(), 17, 60, 'Lunch', 'Mix brown gravy, cream cheese, and mushrooms with hamburger, stirring until cream cheese melts. Add milk, sour cream, and mushroom soup to cooked pasta. Blend hamburger mixture with pasta', 'public');
    CALL make_recipe(16, 'Baked Kale Chips', CURDATE(), 19, 20, 'Snack', 'With a knife or kitchen shears carefully remove the leaves from the thick stems and tear into bite size pieces. Drizzle kale with olive oil and sprinkle with seasoning salt.', 'public');
    CALL make_recipe(17, 'Easy Tuna Casserole', CURDATE(), 20, 45, 'Lunch', 'Combine the macaroni, tuna, and soup. Mix well, and then top with cheese.', 'public');
    CALL make_recipe(18, 'Roasted Butternut Squash', CURDATE(), 11, 45, 'Lunch', 'Toss butternut squash with olive oil and garlic in a large bowl. Season with salt and black pepper. Arrange coated squash on a baking sheet', 'public');
    CALL make_recipe(19, 'Pesto Cheesy Chicken Rolls', CURDATE(), 12, 15, 'Breakfast', 'Spread 2 to 3 tablespoons of the pesto sauce onto each flattened chicken breast. Place one slice of cheese over the pesto. Roll up tightly, and secure with toothpicks. ', 'public');
    CALL make_recipe(20, 'Yummy Pork Chops', CURDATE(), 13, 45, 'Dinner', 'Place the pork chops in a skillet over medium heat, and cover with the dressing mixture. Cover skillet, and cook pork chops 25 minutes, turning occasionally. ', 'public');
    CALL make_recipe(21, 'Vanilla Ice Cream', CURDATE(), 14, 40, 'Snack', 'Combine half-and-half, cream, sugar, vanilla and salt in freezer container of ice cream maker. Freeze according to manufacturers instructions.', 'public');
    CALL make_recipe(22, 'Corn on the Cob', CURDATE(), 15, 10, 'Snack', 'Gently place ears of corn into boiling water, cover the pot, turn off the heat, and let the corn cook in the hot water until tender, about 10 minutes.', 'public');
    CALL make_recipe(23, 'Ground Beef Curly Noodle', CURDATE(), 16, 15, 'Breakfast', 'Stir in the flavor packet from the noodles, tomatoes, and corn (with their juices). Break up the noodles slightly, and add them to the skillet. Bring to a boil, then reduce heat to low, cover, and simmer for 10 minutes, or until noodles are tender. ', 'public');
    CALL make_recipe(24, 'Baked Zucchini Fries', CURDATE(), 17, 25, 'Snack', 'Working in batches, dip zucchini strips into egg mixture, shake to remove any excess, and roll strips in bread crumb mixture to coat. Transfer coated zucchini strips to the prepared baking sheet.', 'public');
    CALL make_recipe(25, 'Glazed Carrots', CURDATE(), 17, 35, 'Lunch', 'Melt butter in the same saucepan; stir brown sugar, salt, and white pepper into butter until brown sugar and salt have dissolved. Transfer carrots into brown sugar sauce; cook and stir until carrots are glazed with sauce, about 5 more minutes.', 'public');
    

	INSERT INTO recipeingredients (foodID, recipeID, ingredientQtty) VALUES 
		(6212, 4, 500), 
		(4575, 3, 1000), (19296, 3, 20), 
		(1003, 2, 200), (1001, 2, 750),
		(16424, 1, 324), (5059, 1, 1200);
    
    SET SQL_SAFE_UPDATES = 1;
END //

DROP FUNCTION IF EXISTS canBeMade //
CREATE FUNCTION canBeMade
(
	user BIGINT(20),
    recipe BIGINT(20)
)
RETURNS INT DETERMINISTIC
BEGIN
	DECLARE servings DOUBLE;

	SELECT MIN(IFNULL(stockQtty / recipeQtty, 0)) INTO servings
	FROM
		(SELECT foodID, ingredientQtty as 'recipeQtty'
		FROM recipeingredients
		WHERE recipeID = recipe) recipeIngredients
		LEFT JOIN
		(SELECT foodID, sum(foodQtty) as 'stockQtty'
		FROM stockitems
		WHERE userID = user
		GROUP BY foodID) stockIngredients
		USING(foodID);
        
	RETURN servings;
END // 

DROP FUNCTION IF EXISTS haveStock//
CREATE FUNCTION haveStock
(
	user BIGINT(20),
    food BIGINT(20),
    quantity float(10,3)
)
RETURNS TINYINT DETERMINISTIC
BEGIN
	DECLARE ans TINYINT;
    SELECT sum(foodQtty) >= quantity INTO ans
    FROM stockitems
    WHERE foodID = food AND userID = user;
    RETURN ans;
END //

DROP FUNCTION IF EXISTS calcRecipeCalories //
CREATE FUNCTION calcRecipeCalories(recipe BIGINT(20))
RETURNS INT DETERMINISTIC
BEGIN
	DECLARE calories INT;
    SELECT sum(fCalories * ingredientQtty / 100) INTO Calories
    FROM recipes JOIN recipeingredients USING (recipeID) JOIN ingredients USING (foodID)
    WHERE recipeID = recipe;
    RETURN calories;
END //

DROP FUNCTION IF EXISTS calcRecipeIntakeCalories //
CREATE FUNCTION calcRecipeIntakeCalories
(
	intake BIGINT(20)
)
RETURNS INT DETERMINISTIC
BEGIN
    DECLARE Calories INT;
    DECLARE id BIGINT(20);

    SELECT recipeID, calcRecipeCalories(recipeID) * serving INTO id, Calories 
    FROM recipeintake
    WHERE intakeID = intake
    GROUP BY recipeID;
    
    RETURN Calories;
END // 

DROP FUNCTION IF EXISTS calcFoodIntakeCalories //
CREATE FUNCTION calcFoodIntakeCalories
(
	intake BIGINT(20)
)
RETURNS INT DETERMINISTIC
BEGIN
	DECLARE Calories INT;
	
	SELECT fCalories * (intakeQtty / 100) INTO Calories
    FROM foodintake JOIN ingredients USING(foodID)
    WHERE intakeID = intake
    LIMIT 1;
    
    RETURN Calories;
END //

DROP PROCEDURE IF EXISTS get_intakes //
CREATE PROCEDURE get_intakes
(
	user BIGINT(20)
)
BEGIN
	SELECT * FROM
		(SELECT intakeID, serving, recipeID, NULL as intakeQtty, NULL as foodID, 'recipe' as type,
			CAST(intakeDate as DATE) as 'date', CAST(intakeDate as TIME) as 'time',
            calcRecipeIntakeCalories(intakeID) as 'Calories'
		FROM recipeintake JOIN userintake USING(intakeID)
        WHERE userID = user
		UNION
		SELECT intakeID, NULL as serving, NULL as recipeID, intakeQtty, foodID, 'stock' as type,
			CAST(intakeDate as DATE) as 'date', CAST(intakeDate as TIME) as 'time',
            calcFoodIntakeCalories(intakeID) as 'Calories'
		FROM foodintake JOIN userintake USING(intakeID)
        WHERE userID = user) intakes
	ORDER BY date DESC, time;
END //

DROP PROCEDURE IF EXISTS get_intake //
CREATE PROCEDURE get_intake
(
	user BIGINT(20),
    intake BIGINT(20)
)
BEGIN
	SELECT * FROM
		(SELECT intakeID, serving, recipeID, NULL as intakeQtty, NULL as foodID, 'recipe' as type,
			CAST(intakeDate as DATE) as 'date', CAST(intakeDate as TIME) as 'time',
            calcRecipeIntakeCalories(intakeID) as 'Calories'
		FROM recipeintake JOIN userintake USING(intakeID)
        WHERE userID = user AND intakeID = intake
		UNION
		SELECT intakeID, NULL as serving, NULL as recipeID, intakeQtty, foodID, 'stock' as type,
			CAST(intakeDate as DATE) as 'date', CAST(intakeDate as TIME) as 'time',
            calcFoodIntakeCalories(intakeID) as 'Calories'
		FROM foodintake JOIN userintake USING(intakeID)
        WHERE userID = user AND intakeID = intake) intakes
	LIMIT 1;
END //

DROP PROCEDURE IF EXISTS intake_recipe//
CREATE PROCEDURE intake_recipe
(
	user BIGINT(20),
    recipe BIGINT(20),
    serving FLOAT,
    eatTime DATETIME
)
BEGIN
	IF canBeMade(user, recipe) >= serving THEN
		INSERT INTO userintake(userID, intakeDate) VALUES (user, eatTime);
		INSERT INTO recipeintake VALUES(LAST_INSERT_ID(), serving, recipe);
	ELSE 
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'you do not have the necessary stock';
    END IF;
END //

DROP PROCEDURE IF EXISTS intake_food//
CREATE PROCEDURE intake_food
(
	user BIGINT(20),
    food BIGINT(20),
    quantity float(10,3),
    eatTime DATETIME
)
BEGIN
	IF haveStock(user, food, quantity) THEN
		INSERT INTO userintake(userID, intakeDate) VALUES (user, eatTime);
		INSERT INTO foodintake VALUES(LAST_INSERT_ID(), quantity, food);
	ELSE
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'you do not have the necessary stock';
    END IF;
END //

DROP PROCEDURE IF EXISTS update_stock_after_intake //
CREATE PROCEDURE update_stock_after_intake
(
	user BIGINT(20),
	food BIGINT(20),
    consumed FLOAT(10,3)
)
BEGIN
	DECLARE stockQtty FLOAT(10,3);
    DECLARE id BIGINT(20);
    DECLARE toRemove FLOAT(10,3) DEFAULT consumed;
	DECLARE stock_cursor CURSOR FOR SELECT stockItemID, foodQtty FROM stockitems WHERE foodID = food AND userID = user ORDER BY foodExpDate ASC;
	OPEN stock_cursor;
    update_stock: LOOP
		IF toRemove <= 0 THEN
			LEAVE update_stock;
        END IF;
		FETCH stock_cursor INTO id, stockQtty;
        IF (stockQtty >= toRemove) THEN
            UPDATE stockitems SET foodQtty = stockQtty - toRemove WHERE stockItemID = id;
            SET toRemove = 0;
        ELSE
			DELETE FROM stockitems WHERE stockItemID = id;
            SET toRemove = toRemove - stockQtty;
        END IF;
    END LOOP;
    CLOSE stock_cursor;
END //

DROP TRIGGER IF EXISTS after_foodintake_insert //
CREATE TRIGGER after_foodintake_insert AFTER INSERT ON foodintake
FOR EACH ROW
BEGIN
	DECLARE user BIGINT(20);
    SELECT userID into USER from userintake WHERE intakeID = NEW.intakeID;
	CALL update_stock_after_intake(user, NEW.foodID, NEW.intakeQtty);
END //

DROP TRIGGER IF EXISTS after_recipeintake_insert //
CREATE TRIGGER after_recipeintake_insert AFTER INSERT ON recipeintake
FOR EACH ROW
BEGIN
	DECLARE user BIGINT(20);
    DECLARE done TINYINT DEFAULT FALSE;
	DECLARE food BIGINT(20);
    DECLARE consumed FLOAT(10,3);
    DECLARE ingredient_cursor CURSOR FOR SELECT foodID, ingredientQtty * NEW.serving FROM recipeingredients WHERE recipeID = NEW.recipeID;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    SELECT userID into user FROM userintake WHERE intakeID = NEW.intakeID;
    
    OPEN ingredient_cursor;
    intake_ingredients_loop: LOOP
		FETCH ingredient_cursor INTO food, consumed;
        IF done THEN
			LEAVE intake_ingredients_loop;
		END IF;
        CALL update_stock_after_intake(user, food, consumed);
    END LOOP;
    CLOSE ingredient_cursor;
END //

DROP TRIGGER IF EXISTS before_insert_recipe //
CREATE TRIGGER before_insert_recipe BEFORE INSERT ON recipes
FOR EACH ROW
BEGIN
	SET NEW.dateCreated = IFNULL(NEW.dateCreated, CURDATE());
END //

delimiter ;

CALL init_db();
