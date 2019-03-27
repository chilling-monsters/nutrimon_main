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
-- select * from ingredients;

-- DEV RESET
delete from userProfile;
-- delete from ingredients;


-- ------------------------- 
-- USER PROFILE
-- ------------------------- 

-- create userProfile
set @testName = 'Admin001';
set @testEmail = 'admin@husky.neu.edu';
set @testGender = 'male'; -- 'female' 'other'
set @testPass = '12345678';
-- randString(9)
set @testID = 'ADMINTEST'; 

insert into userProfile (userName, userEmail, gender, `password`)
	value (@testName, @testEmail, @testGender, @testPass);

-- edit userProfile
-- update userProfile set `password` = 'newpassword' where userID = @testID;

-- delete userProfile
-- delete from userProfile where userID = @testID;


-- ------------------------- 
-- RECIPE & FOOD
-- ------------------------- 



-- ------------------------- 
-- STOCK & FOOD
-- ------------------------- 


-- -----------------------------
-- GENERAL QUERIES
-- -----------------------------
