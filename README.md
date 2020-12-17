# EECS-3421-database-projects
# Project 1
project 1 is the E/R graph based on the description below.

description: working to design and field a contact-tracing database to be able to trace rapidly people who may have been in close proximity (contact) to someone diagnosed with a highly infectious disease. This can be used to help contain an epidemic.

# project 2
project 2 is building relational schema based on what have done in project 1

# project 3

project 3 is making a schema of Raccoon Rhapsody Database and use different query to obtain 10 different goal.

1. myself
List each player whose login is part of his or her name; i.e., his or her login is a substring of his or her name. This should be case insensitive; e.g., “thom” is a substring of “Thomas Kane”.

schema: login, name, gender, address, joined
order by login (asc)


2. golden
List each quest by realm, day, and theme which offered a prize (treasure) with “Gold” in the name which was rewarded to some player.

schema: realm, day, theme

order by day, realm, theme


3. evening
List the quests by theme, day, and realm that were not completed before 8pm (on the day of the quest) with their succeeded time (which is null if it did not succeed).

schema: theme, day, realm, succeeded

order by theme, day, realm


4. cheat
Report for each player by login and name who managed to participate in more than one quest on the same day, along with those quests by day, realm, and theme.

schema: login, name, day, realm, theme

order by login, name, day, realm, theme


5. bend
List each player by login, name, and gender who gender swapped at least once with their avatars, along with the count of how many avatars that he or she has (avatars).

schema: login, name, gender, avatars

order by login



6. successful
Select the themes (theme) for which the quests were always successful, and report the number of successful quests (quests) for each such.

schema: theme, quests

order by theme



7. frequency
Report the average number of days (as frequency) between visits to each given realm for each player. Also show the number of visits (visits) to that realm for the player. (Ignore a player in a realm if the player has never visited it or has only visited it once; the frequency is not defined in such cases.)

notes

Cast frequency with precision five and scale two.

schema: login, realm, visits, frequency

order by login, realm



8. race
Show each realm and race (of avatar) with the gender whose avatars of that race earned the most scrip (sql) collectively from loot rewarded in quests in that realm, along with the what that race and gender collectively earned in quests in the realm (total).

In case of ties for most in a region, list all that tied.

schema: realm, race, gender, total

order by realm, race, gender



9. companions
List each occurrence in which an avatar (by login as companion1 and avatar's name as fname) whose participation in quests within a given realm has always been together with a second avatar (by login as companion2 and name as lname) who has participated in exactly those same quests within the realm.

Since each pair of such companions would be shown twice — once with avatar X and avatar Y and once with avatar Y and avatar X — break the tie and show each such pair (per realm) just once; choose such that companion1 is before companion2 in dictionary order.

schema: companion1, fname, realm, companion2, lname

order by realm, companion1, fname, companion2, lname



10. potential
Show for each avatar by login, avatar's name, and race, the scrip (sql) that the avatar would have earned (earned) if the avatar had been rewarded the prize (loot) of highest value (and just that prize, one piece of loot) for each quest in which the avatar participated that was successfully completed, and how many successful quests the avatar has participated in (quests).

schema: login, name, race, earned, quests

order by login, name

# project 4

Raccoon Rhapsody, the multi-player online game by Questeme, has become wildly popular. The company wants to streamline maintaining the RR-DB database that backs the game.

One task that game administrators have to do is to create new quests on a regular basis for the players. This involves making a new entry into the Quest table for the new quest for a future day with a given theme and region. And then assigning loot — adding to the Loot table — for the new quest. (Of course, much more too has to be done. The game designers have to come up with the story line for the new quest, the artists sometimes must create new gaming assets for the quest and perhaps new scenes, etc. But we do not have to worry about those aspects!)

You have been asked to automate this task with an application program, let us call it CreateQuest. You are to make this in Java using JDBC; so, CreateQuest.java. The app should connect with Questeme's PostgreSQL database server at db (okay, really EECS's) with the RR-DB database to make the necessary updates on request.\n
CreateQuest <day> <realm> <theme> <amount> [<user>] [seed]
day: the day for the new quest
realm: which realm the new quest is in
theme: the theme for the new quest
amount: the floor for the sum of the assigned loot by value (sql)
user (optional): which user and database the app is connecting with and to, respectively. This should default to your user name (which is also your database's name).
seed (optional): a real (float) number between -1 and 1 that is seeded before the use of random(). (If no seed is provided, then no seeding is to be done beforehand.)
Your program should accept the command-line parameters as specified above.

Error Messages

The app should provide an error message back to the user for each of the following cases. (Your Java program should finish without failing in error itself in these cases!)

day is not in future: if the day provided is not in the future, the app should state this and not make any changes to the database.
realm does not exist: if the realm does not exist in the Realm table, the app should state this and not make any changes to the database.
amount exceeds what is possible: if loot cannot be assigned by the loot rules listed below that meets or exceeds the sql amount requested, the app should state this and not make any changes to the database.
seed value is improper: if the seed value is not a real between -1 and 1, the app should state this and not make any changes to the database.
If a user (/ database) value is provided who does not exist, or for whom your program cannot successfully procure permissions, your Java program can fail. The failure trace will indicate the issue. Your app must work with the default user, your Postgres account in the class.

Operation

Given no failure mode occurs, your app should proceed to do the following.

A tuple is added to the Quest table with the specified day, realm, and theme (in database user).
Tuples are added to the Loot table that “asign” loot to the new quest, following the loot assignment rules below (in database user).
Loot Assignment Rules

Loot is to be assigned to the new quest randomly, but with the following two constraints.

distinct. Two pieces of loot of the same type (treasure) are not assigned. (That is, we are sampling the Treasure table without replacement.)
exceeds. The sum of the assigned loots's value — the corresponding sql in the Treasure table — equals or exceeds that requested (parameter amount).
For exceeds, you may follow a strategy in which you add loot pieces one at a time until the amount is matched or exceeded by the last piece added. (You do not need to then go remove assigned loot to come back down closer to the amount.) Questeme is just wanting to assure the app does not go overboard on assigning loot to the new quest.

Note that if you have assigned a piece of loot per type of treasure — say, by walking over the Treasure table in a “random” order — for every type of treasure but still have not met or exceeded the amount, then the loot assignment is not possible. In this case, your app should report that error.
