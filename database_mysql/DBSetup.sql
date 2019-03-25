SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema chillingM
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `chillingM` ;

-- -----------------------------------------------------
-- Schema chillingM
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `chillingM` DEFAULT CHARACTER SET utf8 ;
USE `chillingM` ;

-- -----------------------------------------------------
-- Table `chillingM`.`ingredients`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`ingredients` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`ingredients` (
  `foodID` BIGINT NOT NULL,
  `foodName` VARCHAR(128) NOT NULL,
  `fCalories` FLOAT(10,3) NULL DEFAULT NULL,
  `fProtein` FLOAT(10,3) NULL DEFAULT NULL,
  `fTotalFat` FLOAT(10,3) NULL DEFAULT NULL,
  `fCarbohydrate` FLOAT(10,3) NULL DEFAULT NULL,
  `fSodium` FLOAT(10,3) NULL DEFAULT NULL,
  `fSaturatedFat` FLOAT(10,3) NULL DEFAULT NULL,
  `fCholestero` FLOAT(10,3) NULL DEFAULT NULL,
  `fSugar` FLOAT(10,3) NULL DEFAULT NULL,
  `fCalcium` FLOAT(10,3) NULL DEFAULT NULL,
  `fIron` FLOAT(10,3) NULL DEFAULT NULL,
  `fPotassium` FLOAT(10,3) NULL DEFAULT NULL,
  `fVC` FLOAT(10,3) NULL DEFAULT NULL,
  `fVE` FLOAT(10,3) NULL DEFAULT NULL,
  `fVD` FLOAT(10,3) NULL DEFAULT NULL,
  `expTime` INT(3) NULL DEFAULT NULL,
  PRIMARY KEY (`foodID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `foodName_UNIQUE` ON `chillingM`.`ingredients` (`foodName` ASC) VISIBLE;

CREATE UNIQUE INDEX `foodID_UNIQUE` ON `chillingM`.`ingredients` (`foodID` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `chillingM`.`userProfile`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`userProfile` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`userProfile` (
  `userID` BIGINT NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR(45) NOT NULL,
  `userEmail` VARCHAR(60) NOT NULL,
  `gender` ENUM('male', 'female', 'other') NULL DEFAULT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`userID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE UNIQUE INDEX `userEmail_UNIQUE` ON `chillingM`.`userProfile` (`userEmail` ASC) VISIBLE;

CREATE UNIQUE INDEX `userID_UNIQUE` ON `chillingM`.`userProfile` (`userID` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `chillingM`.`userIntake`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`userIntake` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`userIntake` (
  `intakeID` BIGINT NOT NULL AUTO_INCREMENT,
  `userID` BIGINT NOT NULL,
  `intakeNote` VARCHAR(256) NULL,
  PRIMARY KEY (`intakeID`),
  CONSTRAINT `fk_userIntake_userProfile`
    FOREIGN KEY (`userID`)
    REFERENCES `chillingM`.`userProfile` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `intakeID_UNIQUE` ON `chillingM`.`userIntake` (`intakeID` ASC) VISIBLE;

CREATE INDEX `fk_userIntake_userProfile_idx` ON `chillingM`.`userIntake` (`userID` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `chillingM`.`foodIntake`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`foodIntake` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`foodIntake` (
  `intakeID` BIGINT NOT NULL,
  `intakeQtty` FLOAT(10,3) NOT NULL,
  `intakeDate` DATETIME NOT NULL,
  `foodID` BIGINT NOT NULL,
  CONSTRAINT `fk_foodIntake_userIntake`
    FOREIGN KEY (`intakeID`)
    REFERENCES `chillingM`.`userIntake` (`intakeID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_foodIntake_ingredients`
    FOREIGN KEY (`foodID`)
    REFERENCES `chillingM`.`ingredients` (`foodID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_foodIntake_userIntake_idx` ON `chillingM`.`foodIntake` (`intakeID` ASC) VISIBLE;

CREATE INDEX `fk_foodIntake_ingredients_idx` ON `chillingM`.`foodIntake` (`foodID` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `chillingM`.`recipes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`recipes` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`recipes` (
  `recipeID` BIGINT NOT NULL AUTO_INCREMENT,
  `recipeName` VARCHAR(45) NOT NULL,
  `dateCreated` DATETIME NOT NULL,
  `userID` BIGINT NULL,
  `recipeDescription` VARCHAR(256) NOT NULL,
  `accessibility` ENUM('public', 'private') NOT NULL,
  PRIMARY KEY (`recipeID`),
  CONSTRAINT `fk_recipes_userProfile`
    FOREIGN KEY (`userID`)
    REFERENCES `chillingM`.`userProfile` (`userID`)
    ON DELETE SET NULL
    ON UPDATE SET NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_recipes_userProfile_idx` ON `chillingM`.`recipes` (`userID` ASC) VISIBLE;

CREATE UNIQUE INDEX `recipeID_UNIQUE` ON `chillingM`.`recipes` (`recipeID` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `chillingM`.`stockItems`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`stockItems` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`stockItems` (
  `foodID` BIGINT NOT NULL,
  `foodQtty` FLOAT(10,3) NOT NULL,
  `foodExpDate` DATE NOT NULL,
  `userID` BIGINT NOT NULL,
  `stockItemID` BIGINT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`stockItemId`),
  CONSTRAINT `fk_stockItems_ingredients`
    FOREIGN KEY (`foodID`)
    REFERENCES `chillingM`.`ingredients` (`foodID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_stockItems_userProfile`
    FOREIGN KEY (`userID`)
    REFERENCES `chillingM`.`userProfile` (`userID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_stockItems_ingredients_idx` ON `chillingM`.`stockItems` (`foodID` ASC) VISIBLE;

CREATE INDEX `fk_stockItems_userProfile_idx` ON `chillingM`.`stockItems` (`userID` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `chillingM`.`recipeIngredients`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`recipeIngredients` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`recipeIngredients` (
  `foodID` BIGINT NOT NULL,
  `recipeID` BIGINT NOT NULL,
  `ingredientQtty` FLOAT(10,3) NULL,
  PRIMARY KEY (`foodID`, `recipeID`),
  CONSTRAINT `fk_recipeIngredients_ingredients`
    FOREIGN KEY (`foodID`)
    REFERENCES `chillingM`.`ingredients` (`foodID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_recipeIngredients_recipes`
    FOREIGN KEY (`recipeID`)
    REFERENCES `chillingM`.`recipes` (`recipeID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE INDEX `fk_recipeIngredients_ingredients_idx` ON `chillingM`.`recipeIngredients` (`foodID` ASC) VISIBLE;

CREATE INDEX `fk_recipeIngredients_recipes_idx` ON `chillingM`.`recipeIngredients` (`recipeID` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `chillingM`.`promotedRecipe`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`promotedRecipe` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`promotedRecipe` (
  `userID` BIGINT NOT NULL,
  `recipeID` BIGINT NOT NULL,
  PRIMARY KEY (`userID`, `recipeID`),
  CONSTRAINT `fk_promotedRecipe_userProfile`
    FOREIGN KEY (`userID`)
    REFERENCES `chillingM`.`userProfile` (`userID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_promotedRecipe_recipes`
    FOREIGN KEY (`recipeID`)
    REFERENCES `chillingM`.`recipes` (`recipeID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE INDEX `fk_promotedRecipe_userProfile_idx` ON `chillingM`.`promotedRecipe` (`userID` ASC) VISIBLE;

CREATE INDEX `fk_promotedRecipe_recipes_idx` ON `chillingM`.`promotedRecipe` (`recipeID` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `chillingM`.`recipeIntake`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`recipeIntake` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`recipeIntake` (
  `intakeID` BIGINT NOT NULL,
  `serving` INT NOT NULL,
  `intakeDate` DATETIME NOT NULL,
  `recipeID` BIGINT NOT NULL,
  CONSTRAINT `fk_recipeIntake_userIntake`
    FOREIGN KEY (`intakeID`)
    REFERENCES `chillingM`.`userIntake` (`intakeID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_recipeIntake_recipes`
    FOREIGN KEY (`recipeID`)
    REFERENCES `chillingM`.`recipes` (`recipeID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE INDEX `fk_recipeIntake_userIntake_idx` ON `chillingM`.`recipeIntake` (`intakeID` ASC) VISIBLE;

CREATE INDEX `fk_recipeIntake_recipes_idx` ON `chillingM`.`recipeIntake` (`recipeID` ASC) VISIBLE;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
