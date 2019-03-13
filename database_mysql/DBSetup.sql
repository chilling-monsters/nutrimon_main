-- MySQL Workbench Forward Engineering

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
  `foodID` INT NOT NULL,
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
  `fExp` INT(3) NULL DEFAULT NULL,
  PRIMARY KEY (`foodID`),
  UNIQUE INDEX `foodName_UNIQUE` (`foodName` ASC) VISIBLE,
  UNIQUE INDEX `foodID_UNIQUE` (`foodID` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `chillingM`.`userProfile`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`userProfile` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`userProfile` (
  `userID` INT NOT NULL,
  `userName` VARCHAR(45) NOT NULL,
  `userEmail` VARCHAR(60) NOT NULL,
  `gender` ENUM('male', 'female', 'other') NULL DEFAULT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE INDEX `userEmail_UNIQUE` (`userEmail` ASC) VISIBLE,
  UNIQUE INDEX `userID_UNIQUE` (`userID` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `chillingM`.`intakes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`intakes` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`intakes` (
  `intakeID` INT NOT NULL,
  `recipeID` INT NULL DEFAULT NULL,
  `foodID` INT NULL DEFAULT NULL,
  `iQuantity` FLOAT(10,3) NOT NULL,
  `intakeDate` DATETIME NOT NULL,
  `userID` INT NOT NULL,
  INDEX `foodID_intakes_idx` (`foodID` ASC) VISIBLE,
  INDEX `fk_intakes_userProfile1_idx` (`userID` ASC) VISIBLE,
  PRIMARY KEY (`intakeID`),
  CONSTRAINT `foodID_intakes`
    FOREIGN KEY (`foodID`)
    REFERENCES `chillingM`.`ingredients` (`foodID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_intakes_userProfile1`
    FOREIGN KEY (`userID`)
    REFERENCES `chillingM`.`userProfile` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `chillingM`.`recipes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`recipes` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`recipes` (
  `recipeID` INT NOT NULL,
  `rQuantity` FLOAT(10,3) NOT NULL,
  `recipeName` VARCHAR(45) NOT NULL,
  `dateCreated` DATETIME NOT NULL,
  `userID` INT NOT NULL,
  `recipeDescription` VARCHAR(255) NOT NULL,
  INDEX `fk_recipes_userProfile1_idx` (`userID` ASC) VISIBLE,
  PRIMARY KEY (`recipeID`),
  CONSTRAINT `fk_recipes_userProfile1`
    FOREIGN KEY (`userID`)
    REFERENCES `chillingM`.`userProfile` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `chillingM`.`stocks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`stocks` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`stocks` (
  `stockID` INT NOT NULL,
  `foodID` INT NOT NULL,
  `sQuantity` FLOAT(10,3) NOT NULL,
  `fExpirationDate` DATETIME NOT NULL,
  `userID` INT NOT NULL,
  INDEX `foodID_idx` (`foodID` ASC) VISIBLE,
  INDEX `fk_stocks_userProfile1_idx` (`userID` ASC) VISIBLE,
  PRIMARY KEY (`stockID`),
  CONSTRAINT `foodID_stock`
    FOREIGN KEY (`foodID`)
    REFERENCES `chillingM`.`ingredients` (`foodID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_stocks_userProfile1`
    FOREIGN KEY (`userID`)
    REFERENCES `chillingM`.`userProfile` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `chillingM`.`recipeIngredients`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`recipeIngredients` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`recipeIngredients` (
  `foodID` INT NOT NULL,
  `recipeID` INT NOT NULL,
  PRIMARY KEY (`foodID`, `recipeID`),
  INDEX `fk_recipeIngredients_ingredients1_idx` (`foodID` ASC) VISIBLE,
  INDEX `fk_recipeIngredients_recipes1_idx` (`recipeID` ASC) VISIBLE,
  CONSTRAINT `fk_recipeIngredients_ingredients1`
    FOREIGN KEY (`foodID`)
    REFERENCES `chillingM`.`ingredients` (`foodID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_recipeIngredients_recipes1`
    FOREIGN KEY (`recipeID`)
    REFERENCES `chillingM`.`recipes` (`recipeID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chillingM`.`favorites`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`favorites` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`favorites` (
  `userID` INT NOT NULL,
  `recipeID` INT NOT NULL,
  INDEX `fk_favorites_userProfile1_idx` (`userID` ASC) VISIBLE,
  INDEX `fk_favorites_recipes1_idx` (`recipeID` ASC) VISIBLE,
  PRIMARY KEY (`userID`, `recipeID`),
  CONSTRAINT `fk_favorites_userProfile1`
    FOREIGN KEY (`userID`)
    REFERENCES `chillingM`.`userProfile` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_favorites_recipes1`
    FOREIGN KEY (`recipeID`)
    REFERENCES `chillingM`.`recipes` (`recipeID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
