# EECS-3421-database-projects
Project 1
project 1 is the E/R graph based on the description below.
description: The Contact Tracing Domain
To be able to trace with whom a person has been in contact, we need to know where that person has been and when. We have to know this about everyone else too. Then we could figure out the potential contacts by seeing who was in places at the same time as the person. During an epidemic with a highly infectious disease, by tracing the recent contacts of a person who has become ill, these people can be warned to take appropriate action. This can greatly help to stem the epidemic.

Thus Person is fundamental in our database. We are tracing people, after all. Information we should keep for people is a name, address, and phone#. We can keep sin, a person's social insurance number to identify the person. (Assume that the government will issue anyone in Canada who does not have a sin a temporary sin for this very purpose. Also note that, in building such a database for real, in truth, using sin for this would likely not be a good choice. But, for the project, let us assume it is.)

We should also record Places. This will include public building and other indoor places where people can meet, and thus come into contact. (We assume for now that the disease does not transmit outdoors, so we are not keeping track of all possible locations.) We can use a place's name to identify it, and we want to keep about a place its gps coordinates, address, and a description.

Central to the whole database's operation is to collect “observations” about which places a person has been, and when they were there. Thus, we are designing a mass-surveillance database! But we are assuming that this is for a good cause, and that the database, once up and running, will not be abused for other purposes.

Let us call an observation that such a person was in such a place at such a time a Recon — a shortened form of the military term reconnaissance — to have an easier way to organize our thoughts. A Recon names a Person (who) as being at a Place at a given time (when). Let us employ the notion of Time Slot for handling times. There will be effectively an entry in Time Slot for every fifteen-minute period; e.g., 3:00pm 25 September 2020, 3:15pm 25 September 2020, 3:30pm 25 September 2020, and so forth. For any given time slot in which a person was observed somehow to be at a place, we would have a Recon entry. Thus each Recon is associated with a Time Slot, telling us when the person was there. (A better way to handle time in such a scenario is to work with time intervals. This is significantly more involved design-wise, however. Therefore, for our first cut of a schema for our contact-tracing database — that is, this project — let us employ Time Slot to handle the “when” aspect.) In fact, we can use when to identify any given Time Slot. (Say we assume the when value identifies the beginning of the time slot; e.g., 3:00pm stands for 3:00pm up to 3:15pm.)

A Recon then identifies — and therefore, is identified, at least in part, by — the person, the place, and the time slot (when), which is “saying” that Person (who) was at Place during this Time-slot time (when). For any Recon, we also must record the Method, the way, that we know that the person was at the place at that time. Examples of methods might be, for example, contact-tracing phone app, surveillance camera with facial recognition, and a registry entry (the person had to sign a registry book on entering and exiting the building). Note that there could be more than one recons telling us a person was at a place at a given time, each recon supported by different evidence (Method).

In our database, we also need to track when a person is tested, a Test, for the disease. Such a diagnosis Test is administered to a given Person at (“upon”) a given time (Time Slot). This information identifies any given Test. A Test is administered in a Test Centre — which is a Place — and is of a given Test Type. (There are different types of test for the disease, which may differ in efficacy and cost.) Thus, we want to record where the Test was administered, and what type of test it was.

A Test may result in an Action. Let Action be identified by an action name. (An action might be taken given the result of the test; for instance, if it came back positive for the disease, the person might be placed in quarantine.) Assume that a given Test results in at most one Action. Of course, a Test might not result in any action. (Say, the test came back negative and no action was necessary.) We want to track actions resulting from tests.

Not all Test Centres are equiped to administer all types of test (Test Types). Thus, we want to record which Test Types are offered at which Test Centres. A Test Centre may offer different types of test; and a type of test may be available at a number of test centres.

Lastly, the Government is to require that each Person identifies their bubble; that is family and friends — other people (Persons) — with whom they are regularly in contact. We need to record this “bubble” information in the database.
