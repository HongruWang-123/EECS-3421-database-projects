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
