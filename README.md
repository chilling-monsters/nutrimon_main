# Nutrimon - Don't Waste, It's time to eat!

## Table of Contents
- [Overview](#overview)
  * [Description](#description)
  * [Setting Up](#setting-up)
  * [How to Run](#how-to-run)
- [Libraries Required](#libraries-required)
- [Slides and Diagrams](#slides-and-diagrams)

## Overview
Nutrimon is a meal planning and tracking application. A user on this application will have access to an extensive database of various ingredients and be able to manage his or her own stock of these ingredients, as well as create, customize, and share various recipes made through the application. 
    
### Description
NutriMon’s most unique feature goal is to keep track of a user’s stock of ingredients and when they are expected to go bad:

"An estimated 30-40 percent of the United States’ food supply is wasted according to the USDA’s Economic Research Service.  This corresponds to approximately $161 billion worth of food in 2010.  This food waste is the largest component going into municipal landfills and quickly generates methane, which contributes to climate change.  Furthermore, all the land, water, labor, and energy that goes into producing, processing, transporting, preparing, storing, and discarding food are essentially wasted resources." - From USDA

### Setting Up
Prerequisite:
This instruction is on macos system. Need to have JAVA JDK1.8 installed. And IntelliJ JAVA IDE installed (or eclipse).

#### MySQL Database
On MACOSX:<br>
1.Go to https://dev.mysql.com/downloads/ <br>
2.Click MySQL Community Server3.Select Operating System = Mac OS X4.Download Mac OS X 10.13(x86, 64-bit), DMG Archive<br>
5.Pick  “No thanks, just start my download”<br>
6.Open the DMG and double click on the .pkg object (installer)<br>
7.Click continue<br>
8.Click continue again and agree to license.<br>
9.Click install, enter password when asked<br>
10.Use LegacyPassword Encryption(There are some compatibility issues using Strong Encryption)<br>
11.Enter a root password and click Finish<br>
12.Enter laptop admin password to allow installation<br>
13.Click close. You can move the dmg to trash if it asks.<br>
14.Open a terminal window.  cd to /usr/local –verify you see listings for mysql.e.g.  mysql -> mysql-8.0.11-macos10.13-x86_64<br>
15.Go to mysql bin directory:  $  cd mysql/bin<br>
16.Start mysql shell: $ ./mysql –u root –p(Enter the root password given in step 11.)This starts up the text client.  You should get a mysql> prompt. Done!<br>

17.Go back to https://dev.mysql.com/downloads/<br>
18.Click on MySQL Workbench<br>
19.Select Operating System = Mac OSX<br>
20.Click Download button, again say “No thanks, just start my download”<br>
21.When download finishes, open the DMG<br>
22.Drag workbench to applications<br>
23.In applications folder, double click on MySQL Workbench to start<br>
24.One time: click open to bypass warning about application downloaded from internet<br>
25.Clickon Startup / Shutdown on left panel<br>
26.Enter laptop user admin password<br>
27.When popup for user root displays, enter your new root password <br>
28.Verify that it shows the server as “running”<br>

#### Setups
1.run `git clone https://github.com/chilling-monsters/nutrimon_main.git`<br>
2.open folder /nutrimon_main/database_mysql<br>
3.double click `chillingM.mwb`<br>
4.Press 'command+G' or click database->forward engineer<br>
5.Press next until its done. Then a 'chillingM' schema will show up under 'SCHEMAS' in your local instances<br>
6.click 'chillingM'-> 'Tables' then right click 'ingredients' table choose 'Table data import wizzard'<br>
7.use the provided csv file under /nutrimon_main/database_mysql called 'chillingData999999.csv'<br>
8.Import the data<br>
9.Open PopulateData.sql and run it (click the lightning sign above the script)<br>

### How to Run
1.Open IntelliJ<br>
2.Select "import project"<br>
3.Choose /nutrimon-main/Development/NutriMon folder<br>
4.Select "import project from external model" and choose 'Maven'<br>
5.Press next until a new project is created<br>
6.On the left side, under /NutriMon/src/main/java/chillingMonsters<br>
7.right click 'NutriMonMain', select 'Run NutriMonMain'<br>

## Libraries Required
These plugins will be automatically included thanks to Maven:<br>

JavaFX - 11.0.2<br>
MySQL Java Connector - 5.1.47<br>

## Slides and Diagrams
Slides: https://docs.google.com/presentation/d/1fbyloTxywshkTuL6YCYUdayOKEgPNec9yEy0VkLm038/edit?usp=sharing <br>
Diagrams: https://www.lucidchart.com/invitations/accept/08e94a18-1fec-463d-b747-6afdb6b91085

