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
  `foodID` VARCHAR(10) NOT NULL,
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
  `userID` VARCHAR(10) NOT NULL,
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
-- Table `chillingM`.`userIntake`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`userIntake` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`userIntake` (
  `intakeID` VARCHAR(10) NOT NULL,
  `userID` VARCHAR(10) NOT NULL,
  `intakeDate` DATETIME NOT NULL,
  `intakeNote` VARCHAR(2048) NULL DEFAULT NULL,
  PRIMARY KEY (`intakeID`),
  UNIQUE INDEX `intakeID_UNIQUE` (`intakeID` ASC) VISIBLE,
  INDEX `userID_intake_idx` (`userID` ASC) VISIBLE,
  CONSTRAINT `userID_intake`
    FOREIGN KEY (`userID`)
    REFERENCES `chillingM`.`userProfile` (`userID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `chillingM`.`userRecipe`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`userRecipe` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`userRecipe` (
  `recipeID` VARCHAR(15) NOT NULL,
  `userID` VARCHAR(10) NOT NULL,
  `recipeComment` VARCHAR(2048) NULL DEFAULT NULL,
  `recipeName` VARCHAR(45) NOT NULL,
  `recipeDate` DATETIME NOT NULL,
  PRIMARY KEY (`recipeID`),
  UNIQUE INDEX `recipeID_UNIQUE` (`recipeID` ASC) VISIBLE,
  INDEX `userID_recipe_idx` (`userID` ASC) VISIBLE,
  CONSTRAINT `userID_recipe`
    FOREIGN KEY (`userID`)
    REFERENCES `chillingM`.`userProfile` (`userID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `chillingM`.`intakes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`intakes` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`intakes` (
  `intakeID` VARCHAR(10) NOT NULL,
  `recipeID` VARCHAR(10) NULL DEFAULT NULL,
  `foodID` VARCHAR(10) NULL DEFAULT NULL,
  `iQuantity` FLOAT(10,3) NOT NULL,
  INDEX `foodID_intakes_idx` (`foodID` ASC) VISIBLE,
  INDEX `recipeID_intakes_idx` (`recipeID` ASC) VISIBLE,
  INDEX `intakeID_intakes_idx` (`intakeID` ASC) VISIBLE,
  CONSTRAINT `foodID_intakes`
    FOREIGN KEY (`foodID`)
    REFERENCES `chillingM`.`ingredients` (`foodID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `intakeID_intakes`
    FOREIGN KEY (`intakeID`)
    REFERENCES `chillingM`.`userIntake` (`intakeID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `recipeID_intakes`
    FOREIGN KEY (`recipeID`)
    REFERENCES `chillingM`.`userRecipe` (`recipeID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `chillingM`.`recipes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`recipes` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`recipes` (
  `recipeID` VARCHAR(15) NOT NULL,
  `foodID` VARCHAR(10) NOT NULL,
  `rQuantity` FLOAT(10,3) NOT NULL,
  INDEX `foodID_idx` (`foodID` ASC) VISIBLE,
  INDEX `recipeID_recipe_idx` (`recipeID` ASC) VISIBLE,
  CONSTRAINT `foodID_recipe`
    FOREIGN KEY (`foodID`)
    REFERENCES `chillingM`.`ingredients` (`foodID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `recipeID_recipe`
    FOREIGN KEY (`recipeID`)
    REFERENCES `chillingM`.`userRecipe` (`recipeID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `chillingM`.`userStock`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`userStock` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`userStock` (
  `userID` VARCHAR(10) NOT NULL,
  `stockID` VARCHAR(15) NOT NULL,
  `stockComment` VARCHAR(2048) NULL DEFAULT NULL,
  `stockName` VARCHAR(45) NOT NULL,
  `stockDate` DATETIME NOT NULL,
  PRIMARY KEY (`stockID`),
  UNIQUE INDEX `stockID_UNIQUE` (`stockID` ASC) VISIBLE,
  INDEX `userID_stock_idx` (`userID` ASC) VISIBLE,
  CONSTRAINT `userID_stock`
    FOREIGN KEY (`userID`)
    REFERENCES `chillingM`.`userProfile` (`userID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `chillingM`.`stocks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `chillingM`.`stocks` ;

CREATE TABLE IF NOT EXISTS `chillingM`.`stocks` (
  `stockID` VARCHAR(15) NOT NULL,
  `foodID` VARCHAR(10) NOT NULL,
  `sQuantity` FLOAT(10,3) NOT NULL,
  `fExpirationDate` DATETIME NOT NULL,
  INDEX `foodID_idx` (`foodID` ASC) VISIBLE,
  INDEX `stockID_stock_idx` (`stockID` ASC) VISIBLE,
  CONSTRAINT `foodID_stock`
    FOREIGN KEY (`foodID`)
    REFERENCES `chillingM`.`ingredients` (`foodID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `stockID_stock`
    FOREIGN KEY (`stockID`)
    REFERENCES `chillingM`.`userStock` (`stockID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- SET SQL_MODE=@OLD_SQL_MODE;
-- SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
-- SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
