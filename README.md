# Nutrimon - Don't Waste, It's time to eat!

## Table of Contents
- [Overview](#overview)
  * [Description](#description)
  * [Setting Up](#setting-up)
  * [How to Run](#how-to-run)
- [Libraries Required](#libraries-required)
- [Diagrams](#diagrams)

## Overview
Nutrimon is a meal planning and tracking application. A user on this application will have access to an extensive database of various ingredients and be able to manage his or her own stock of these ingredients, as well as create, customize, and share various recipes made through the application. 
    
### Description
NutriMon’s most unique feature goal is to keep track of a user’s stock of ingredients and when they are expected to go bad:

"An estimated 30-40 percent of the United States’ food supply is wasted according to the USDA’s Economic Research Service.  This corresponds to approximately $161 billion worth of food in 2010.  This food waste is the largest component going into municipal landfills and quickly generates methane, which contributes to climate change.  Furthermore, all the land, water, labor, and energy that goes into producing, processing, transporting, preparing, storing, and discarding food are essentially wasted resources." - From USDA

### Setting Up
#### MySQL Database
On MACOSX:
1.Go to https://dev.mysql.com/downloads/
2.Click MySQL Community Server3.Select Operating System = Mac OS X4.Download Mac OS X 10.13(x86, 64-bit), DMG Archive
5.Pick  “No thanks, just start my download”
6.Open the DMG and double click on the .pkg object (installer)
7.Click continue
8.Click continue again and agree to license.
9.Click install, enter password when asked
10.Use LegacyPassword Encryption(There are some compatibility issues using Strong Encryption)
11.Enter a root password and click Finish
12.Enter laptop admin password to allow installation
13.Click close. You can move the dmg to trash if it asks.
14.Open a terminal window.  cd to /usr/local –verify you see listings for mysql.e.g.  mysql -> mysql-8.0.11-macos10.13-x86_64 15.Go to mysql bin directory:  $  cd mysql/bin
16.Start mysql shell: $ ./mysql –u root –p(Enter the root password given in step 11.)This starts up the text client.  You should get a mysql> prompt. Done!

17.Go back to https://dev.mysql.com/downloads/
18.Click on MySQL Workbench
19.Select Operating System = Mac OSX
20.Click Download button, again say “No thanks, just start my download”
21.When download finishes, open the DMG
22.Drag workbench to applications
23.In applications folder, double click on MySQL Workbench to start
24.One time: click open to bypass warning about application downloaded from internet
25.Clickon Startup / Shutdown on left panel
26.Enter laptop user admin password
27.When popup for user root displays, enter your new root password 
28.Verify that it shows the server as “running”

#### Setups
1.run `git clone https://github.com/chilling-monsters/nutrimon_main.git`
2.open folder /nutrimon_main/database_mysql
3.double click `chillingM.mwb`
4.Press 'command+G' or click database->forward engineer
5.Press next until its done. Then a 'chillingM' schema will show up under 'SCHEMAS' in your local instances
6.click 'chillingM'-> 'Tables' then right click 'ingredients' table choose 'Table data import wizzard'
7.use the provided csv file under /nutrimon_main/database_mysql called 'chillingData999999.csv'
8.Import the data
9.Open PopulateData.sql and run it (click the lightning sign above the script)

### How to Run
1.

## Libraries Required

## Diagrams

DevEnv Setup
1. install intellij
2. import project using pom.xml in /Development
3. Leave defaults
4. install sonarlint plugin
