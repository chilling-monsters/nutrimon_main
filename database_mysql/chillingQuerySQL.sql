-- QUERY SAMPLES
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

select * from ingredients order by foodName;
select count(*) from ingredients; -- should return 7054

-- DEV QUERY
select * from userProfile;
select * from userRecipe;
select * from recipes;
select * from userStock;
select * from stocks;
-- select * from ingredients;

-- DEV RESET
delete from userProfile;
delete from userRecipe;
delete from recipes;
delete from userStock;
delete from stocks;
-- delete from ingredients;


-- ------------------------- 
-- USER PROFILE
-- ------------------------- 

-- create userProfile
set @testName = 'testUser';
set @testEmail = 'testmail@chilling.com';
set @testGender = 'male'; -- 'female' 'other'
set @testPass = 'encryptedpassword';
-- randString(9)
set @testID = 'ADMINTEST'; 

insert into userProfile
	value (@testID, @testName, @testEmail, @testGender, @testPass);

-- edit userProfile
-- update userProfile set `password` = 'newpassword' where userID = @testID;

-- delete userProfile
-- delete from userProfile where userID = @testID;


-- ------------------------- 
-- RECIPE & FOOD
-- ------------------------- 

-- create userRecipe
set @testCmt = 'The is a comment created by testUser';
-- recipeID = userID + 'R' + randString(4)
set @testRID = concat(@testID, 'R', '0001');  -- ADMINTESTR0001
set @testRName = 'testRecipe';

insert into userRecipe value (@testRID, @testID, @testCmt, @testRName, now());

-- edit recipe
-- (see edit userProfile)

-- remove recipe
-- (see delete userProfile)

-- add food to known recipe
set @appleJuiceID = '9400';
set @breadStickID = '18080';
set @testG1 = 2000; -- in grams
set @testG2 = 120; -- in grams

insert into recipes value (@testRID, @appleJuiceID, @testG1);
insert into recipes value (@testRID, @breadStickID, @testG2);

-- ------------------------- 
-- STOCK & FOOD
-- ------------------------- 
-- see RECIPE & FOOD
-- stockID = userID + 'S' + randString(4)
set @testSID = concat(@testID, 'S', '0001'); -- ADMINTESTS0001


-- -----------------------------
-- GENERAL QUERIES
-- -----------------------------

-- login
set @emailIN = 'testmail@chilling.com';
set @passwordIN = 'encryptedpassword';
select userID, userName, gender from userProfile 
	where `password` = @passwordIN and
				userEmail = @emailIN;

-- query food by name
set @qName = 'bread';
select foodID, foodName from ingredients 
	where foodName = @qName or foodName like concat('%', @qName, '%')
												or foodName like concat(@qName, '%')
												or foodName like concat('%', @qName)
		order by foodName;

-- query user profile
select * from userProfile where userID = 'ADMINTEST';

-- query user recipes
select * from userRecipe where userID = 'ADMINTEST';

-- query all foods & quantity in known recipe
select foodName, rQuantity from recipes inner join
								ingredients on recipes.foodID = ingredients.foodID
	where recipes.recipeID = 'ADMINTESTR0001';

