# Kangaroo-Bot

The big brother of [IfBot](https://github.com/impie66/If-Bot)
and a fork from [McRave](https://github.com/Cmccrave/McRave) using [jBWAPI](https://github.com/JavaBWAPI/JBWAPI) and [ASS](https://github.com/JavaBWAPI/ass)
<br/>
![yes](https://i.imgur.com/RVOblFp.png)

<h1>Current Features</h1>

* Building 3 overlords before hatch
* Crashing on Null Values
* Runs on Computer Startup
* getBuildLocation spam
* Horrible and mostly incorrect retreat manager
* `if(canWin()){Retreat()}`
* ~~A pure diet of ConcurrentModificationExceptions~~
* Invalid buildLocations
* Advanced Gas floating 
* Perfect simCity
* Highly advanced ~~If Statements~~ conditional arithmetics
* Hyper passive zerg
* Not making workers when dieing
* Spamming attack orders
* good very manny ouf speeling und gramamah
* Google Home integration
* Drawing lines to 0,0
* Hydralisks Playing Tennis
* Random Support
* Alot of arraylists.
* Even more arraylists.
* A half functional placement for bunkers
* 4th degree warp mind fuckery
* ***Memes***

<h1>Updates</h1>

<b>20/07/2012</b>
<br />
<p>No comment</p>
<br/ >

* Tanks are now known as siege tanks (They siege now)
* Lurkers are no longer lazy
* I did something to repairing but its broken so whatever
* Goliaths no longer think they are top bitch
* Bot will now attempt to hide behind defences when defending.
* And a ton of other shit i don't even remember what i did

<br />

<b>8/07/2021</b>
<br />
<br>1.1</br>
<p> Oh lord he coming..</p>
<br />

* Spell caster class file got sued and works 35% of the time, everytime.
* The building placement got caught cheating, now sleeps on a couch. nah for reals it's kinda better?
* Something about my bot being dumb. I have no idea what i changed
* Bot now remembers unit in the fog of war. #Blessed.
* Unit simulation is now worse thanks to dietary restrictions
* Smarter and worser unit targetting
* Goons can still get fucked.
* Terran Mech no longer does the marine hell build
* Vultures got grounded for planting mines ontop of friendly units. It now does this slightly less.
* Mine dragging?
* Detectors started a violently abusing drugs
* Leader of the workers union is in jail, now they work for worses pay and conditions. 
* If you really what to know what im working with you should check out the changes.txt file
* Special thanks to myself and no one else.
* 
<br />

![mcRave23235](https://i.imgur.com/sDwNsP9.jpg)

</br >

<b>20/08/2020</b>
<br />
<b>1.04</b>
<p>Mein herz brennt</p>
<p>Alot of shit happened. But i'm back for a short while. I fixed a few things but the 1.04 recent update is to see what Bot v Bot stuff i need to fix (I only test against the inbuilt computer)</p>

* Bot now "remembers" units in the fog and will sim them along with visible units.
* Many attempts to fix the "Forget where the enemy is" bug. I think i am breaking it more.
* Many bugs were aborted. They are now in a more hellish and hot place. 
* Added some sort of ZvR build.
* Redid unit production. It may be better in some cases but no promises. 
* Spell casters now get their daily rations reduced if they waste a spell on one unit.
* Added race specific build items. E.G protoss will made a stargate when doing a dumb DT build. 
* Bot now uses Dakka jets properly. 
* DT and air units have now been sponsored by gFuel and will not retreat if unattackable. 
* Stepped deeper into insanity
* Improved retreating to the point where retreating is no longer an option. 
* Bot will no longer send your data to china. (Thanks trump for banning my bot in the states)
<i>inb4 my shitcode spaghetti bot gets invested by the NSA</i>
* Zerg bot now has a taste of communism and milkshakes. ```makeMoreDronesAndProbablyDie();```
* Pornhub no longer recommends SCAT porn if the bot is running. 

<b>I can't be fucked updating the code in the repo</b>

<b>Many bugs incoming! Stay tuned to the ShitPostersGuild™</b>

![verygoodphoto](https://i.imgur.com/3IABLCe.png)
<b>Please click if this doesnt work</b>



<br />


<br />

<b>22/03/2020</b>
<br />
It's not my fault! PoE and doom came out
* Bot will now attack bases rather than the main if applicable
* Reworked some terran openings
* Bot will now attempt to kill defences/workers instead of random buildings.
* Some general SIM improvements.
* Added some basic aggression detection
* Also added some basic DT/lurker detection and reaction
* Tanks/Spidermines now get a pay reduction for friendly fire
* Added 2 protoss builds. 12 nexus and some shit tier DT rush. 
* Alot of poor bugs were killed in the process of the update
* But to make up for that i introduced alot more
<br />
There will be bugs still but i stil need to test against more bots. 

<b>12/03/2020</b>
<br />
Was running a minecraft server with some mates for abit. But now i'm back
* Added teching up and a "Main unit goal". Main unit goal is what the bot wants to primarily build. 
* Added a small delay for siegetanks to siege up upon contact with the enemy (WIP)
* There is a known bug that the bot forgets where the enemy is and scouts around the map. This can work in my favour as it sometimes targets last stored attack position which is often enemy bases. So for now it's a Feature
* Reworked unit unit production
* Also broke unit production
* Bot now attempts to run away from its own spider mines.


<b>6/03/2020</b>
* This segument isn't about updates. Just some general points about my bot. 
* I uploaded version 1.0 onto SSCAIT/BASIL. I don't expect it to do well. It's all about fun here. 
* Currently has a couple builds for each race. Z and T have match up specific openers. These are completely [unconventional](http://satirist.org/ai/starcraft/blog/archives/914-an-advantage-for-playing-random-on-BASIL.html) random builds i made up. 
* P is well... uh... Special :) (>tfw it has the highest win rate of the 3 races. Voom voom goon zealot go fast)
* KangarooBot doesn't build of "Supply" like most bots do. (As in Build SCV at 9 supply). I just give it an arraylist of unitypes and it'll make them once it has the resources. Same with units. It doesn't react very well to the enemys units. It'll make whatever it manages to put together. 
* Bot currently has trouble finding enemy buildings stashed in corners of its base. I have coded up something temporarily while i find something else.  
* The bot hasn't versed a bot before prior to the upload. Everything is subject to get worse. 


<br>

<b>9/08/2019</b>
* Now supports RANDOM
* FFA support. Still has issues that If bot had. Can't see unit's killed by the other players. Bot will attack the weakest player.
* Terran: Same 'ol Bio mech with nukes (Currently disabled because Yegers can't count up to 35)
* Protoss is braindead dead because bot can't micro goons. (but still works)
* Zerg should be fine... But i haven't tested it since i added random support. I don't want to test it just incase it broke
* Spellcasters, Medics, Queens, ~~Defilers?~~, Ghosts, Siege Tanks (Yes they cast siege.), BCS and Vultures
* Bot is now equiped with furniture and milkcrates to defend against terrorism.

<h1>Q/A</h1>


<b>Q: When is the bot being uploaded to any of the ladders</b>
<br>
A: Yes.
<br>
<br>
<b>Q: Is your bot competitive?</b>
<br>
A: No, the bot's a meme. This is what i wanted my other bot to be. People tend to get very serious about their bots and frustate themselves. This bot is to show you can develop a casual bot and still enjoy it.  The aim is still to win but also to have fun while doing so. 
<br>
<br>
<b>Q: Is the bot any good?</b>
<br>
A: have you seen my [other](https://github.com/impie66/If-Bot) bot
<br>
<br>
<b>Q: How did you manage to convert McRave to java??</b>
<br>
A: I didn't. The whole "Forked from McRave" was a troll and a joke.
<br>
<br>
<b>Q: Which is your bots best race</b>
<br>
A: Honestly i have no idea. Zerg is the most defined (As in what units to make and what opener it chooses.) If i had to choose it would be P because of how strong gateway units are.
<br>
<br>
<b>Q: Can i fork Kangaroobot?</b>
<br>
A: <b>No!!!!!.</b> The license file provided is a joke and is <b>NOT VALID.</b>
<br>

<h1>Media</h1>

![asd](https://media.giphy.com/media/443tu8Bvotpx8ltQOD/giphy.gif)
<br />
Zergling Surrounds

![asdd](https://media.giphy.com/media/wab1NqEYJhYLXQv8oO/giphy.gif)
<br />

![asdddd](https://media.giphy.com/media/VI9DC21TVt7Dj3tWtN/giphy.gif)

<br />

![asddddd](https://media.giphy.com/media/gfwaO01bpgZk7m6oVm/giphy.gif)
![!asdddddd](https://media.giphy.com/media/f3vOya4lFfDTqdQcRs/giphy.gif)
![asddddddd](https://media.giphy.com/media/MFrdxIEupizZO9D4Dc/giphy.gif)
![asdddddddd](https://media.giphy.com/media/YlGcIzixb79JiN8Tia/giphy.gif)

https://www.youtube.com/watch?v=ufDTDUPZrag


