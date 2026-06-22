# RPi-5-Minecraft-Server
Project where I created and stress tested a Minecraft Paper/Java Crossplay Server to host for friends and students at my school. 

This project was made PURELY for fun before I realised it was kind of something I should document, even if it was a low level project. 
In fairness, I mainly used youtube tutorials for the JavaScript, Memory Management, Terminal Commands, and Performance Diagnostics, looked online on threads, and debugged with AI when stuck. 

Several optimisations were made to keep it running well and it performed very consistently, only being bottlenecked by
the occasional Playit.gg latency/connection.

# Features:

1. ***Self-Made Java Plug-In (Dcplugin)***
	- **Simple Fly Command** : Custom togglable flight via Bukkit Permissions
	- **Moblore Command** : Tab-completable interface to print custom mob descriptions
	- **Server Diagnostics Command** : Tracks real-time CPU Temperature and RAM Allocation/Usage IN-GAME. Has a command cooldown.
	- **Timed Server Shutdown Command** : A structured, scheduled server shutdown command in minutes, broadcasting to the server in intervals and even using Kernel Commands to shut down the Pi.
	- **Mob Variants** : Special listener for Mobs, turned Zombies and Skeletons into special enemies which required different strategies to beat, of which could be listed by the Moblore command.
	- **Projectile Listener** : Special listener for Arrows, which allowed for the special Skeletons and special Bows by Username and Item Name validation checks. For example, explosive arrows were added dependent on username and itemname of the Player entity firing them.

2. ***Python Discord Bot (derrickwhitebot.py)***
	- Connects via Discord Developer Portal API to be used to view current server players, and if the server was on.
	- Functional user-role management system

3. ***LCD Hardware Compatibility (LCDserver.py)***
   	- Uses a Freenove 2004 LCD display to output System Diagnostics, Player Count and Date/Time.

4. ***Network, Performance Optimisation***
    - **No Port Forwarding** : Using a playit.gg proxy tunnel to protect host network.
	- **Aikar's Flags** : Don't fully understand this, but necessary for Java Memory Optimisation and reducing server latency.
	- **Chunky Pre-Generation** : Minimises how often chunks are generated DURING player exploration which can heavily reduce server performance.

## Equipment Used:
- Raspberry Pi 5 16GB Model (Yes, this was bought before the RAM Crisis)
- Raspberry Pi Official Raspberry Pi 27W USB-C Power Supply
- Raspberry Pi 5 Case with Fan, Overclocked and Config Changed
- Raspberry Pi 5 Heatsink

## Optional Equipment Used:
- Freenove I2C IIC LCD 2004 Module (Diagnostics, in pair with LCDserver.py)

## Services Used:
- **Playit.gg**
- **Discord**
- **MCRcon** (Requires enabling RCon on server.properties)
- **IntelliJ IDEA CE** for Plug-in Development
- **Geany Programmer Editor** for Python Development on RPi5

This will **not detail how to set up a server**, so follow the official Minecraft website guide and also Paper MC website.

https://www.minecraft.net/en-us/download/server

https://papermc.io/

https://docs.papermc.io/paper/aikars-flags/

To set up Playit.gg:

https://www.youtube.com/watch?v=itVVhcid2_Q

To set up the Discord Bot, head to the Discord Developer Portal and create one, then grab its API token, and **DO NOT SHARE IT**.

## Plugins/Minecraft Dependencies Used:
- **PaperMC** - Server hoster, you can find this by going to the experimental dropdown.
- **ViaVersion** - Allows future MC versions to join
- **ViaBackwards** - Allows older MC versions to join
- **Floodgate Spigot** - Responsible for Crossplay 
- **GeyserMC Spigot** - Responsible for Crossplay. Need both Floodgate and Geyser (avoids a certain "issue")
- **Chunky Bukkit** - This is vital. Pre generates chunks so the Pi doesn't have to later which is very memory demanding.
- **"Dcplugin"**, a self-made plugin through a tutorial on YouTube, which I can't find anymore, and other documentation online. Source code for this is attached.

### Pros:
- Performance Wise, the server performed well with around 11-20 concurrent players including mixed Java, Bedrock and Xbox platforms, all separated by thousands of blocks, with different amounts of entities, through hundreds of nether portals, etc.
- The Commands in Dcplugin are weirdly very good to use and was used by some users to inform me when the server was really throttling. Once my fan was not plugged in which caused the server to rocket to 95C with 5 users.

### Cons:
- This was coded from piecing tutorials and documentation together, as I'd never coded in Java prior.
- **SECURITY RISK** - Whilst I don't know too much about the frameworks I used, allowing minecraft commands to trigger kernel commands such as sudo shutdown could be potentially very dangerous.
- **ANOTHER SECURITY RISK** - The Discord Bot could also talk directly to the Server and could be another security risk.
- **Stress Testing with 8GB** was tested, and even at peak usage, the server never really demanded more than 8GB to begin with. However this could be biased as I was virtually limiting allocation of RAM to the server.jar file of which the actual system had more, and this could potentially not work on older models of Raspberry Pis due to the difference in specs all-around, not just RAM.
- **RPi5 is fundamentally different to RPi4 and previous models** and prior so installation may be specific to RPi5 Model. I remember some GPIO libraries are different from RPi4 to RPi5 so keep this in mind. They also handle GPIO and I2C much differently.
- **No Real Permission Levels** which could be implemented using LuckyPerms. Relied on just Operator status which technically could be very unsecure.
- **QoL was Poor** The server shutdown command, would've needed a way to see that shutdown time in a command available to everyone. I didn't code this, but this is very useful for QoL in future development.
- Ensure you have any Java Dependencies, and I'm sorry for not listing them.
- This wasn't a AI Super Start-Up Ultra Buzzword Project to put on a personal statement, and was made after my deadline...

# --**WHAT TO DO**--

1. Overclock your RPi5. Go to `/boot/firmware/config.txt` and add these flags via sudo nano:
  `over_voltage=2 arm_freq = 2800`
   This makes it have more voltage than usual and run at a higher GHz/Clock Speed (2.8GHz). This is a safe threshold so increase at your own risk.
   
3. On the RPi5, download these dependencies (these might not be all of them, sorry). `pip install discord mcrcon smbus2`

4. You can manipulate fan thresholds via something like this:
   `dtparam=fan_temp0=40000
   dtparam=fan_temp0_hyst=30000
   dtparam=fan_temp0_speed=135
   dtparam=fan_temp1=45000
   dtparam=fan_temp1_hyst=3000
   dtparam=fan_temp1_speed=175`
   And so on and so forth. 40000 = 40C Celsius, and Hyst is like an uncertainty in degrees to force a change between these levels you set in config. Speed is just PWM of the fan so higher values = fan rotates more often.
	Furthermore, I did this 2 more times to have 4 separate modes, which followed the same trend of increasing temp and PWM proportionately.

5. Set-up all Minecraft Plug-ins and Dependencies. Search for every website one, and my Dcplugin is linked on this project.
   
6. Create your Playit.gg account, and make a tunnel, linking the ports correctly and running playit on your RPi5. You will use the addresses it gives off to connect to the server. Agree to EULA in server.properties and make an rcon.password so you can connect the bot and LCD diagnostics.
   
7. Set-up the discord bot and LCD diagnostics script by changing the code attached in my project.
   LCDserver.py and derrickwhitebot.py
	 Connect the API token from the Discord Developer Portal to the script and the RCon passwords. If you haven't already, create a Bot and give it perms to your server.

8. Run the server in the kernel after running LCDserver.py, derrickwhitebot.py AND "playit" in a fresh terminal and connecting anything necessary. The I2C component only used 4 GPIO pins and shouldnt be too hard to use.
	 I used this command to run it:
	 
	 `java -Xms12G -Xmx12G -XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200 -XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -XX:+AlwaysPreTouch -XX:G1NewSizePercent=30 -XX:G1MaxNewSizePercent=40 -XX:G1HeapRegionSize=8M -XX:G1ReservePercent=20 -XX:G1HeapWastePercent=5 -XX:G1MixedGCCountTarget=4 -XX:InitiatingHeapOccupancyPercent=15 -XX:G1MixedGCLiveThresholdPercent=90 -XX:G1RSetUpdatingPauseTimePercent=5 -XX:SurvivorRatio=32 -XX:+PerfDisableSharedMem -XX:MaxTenuringThreshold=1 -Dusing.aikars.flags=https://mcflags.emc.gs -Daikars.new.flags=true -jar <server-jar-file>.jar nogui`

	So replace <server-jar-file>.jar with whatever your server.jar file is called.

9. Hopefully it works. When running, use chunky to pre-load some chunks pre-hand and the server should operate quite well.

## Notes for Future Development
With IntelliJ IDEA CE, I also utilised Java with Maven to package .jar files. 

I doubt I will work on this project any further, but some more polymorphic mob variants were supposed to be added:
- *Fission Zombie*, this zombie would split into 2 Daughter Zombies on defeat.
- *Ticker/Boomer*, this zombie would summon a lingering cloud of poison on defeat.
- *Swarmer*, this zombie could summon hostile Bee's angry at players in the server. Hive on their head and cause Slow on hit.
- *Firecracker*, this Pillager just had increased range attributes and a firework reserve instead of arrows.

For anyone willing to continue this (doubt it), for more QoL, I would:
- As I said, make an all-player accessible command that outputs a private message to the user who called it, when the server is shutting down. If it wasn't then just output something along the lines of "No shutdown scheduled". Add a cooldown on this.
- Actually add and implement permissions. Use of LuckyPerms would be great, but permissions were in my plugin code anyways.
- I'd imagine data logging could be manipulated to somehow track more advanced server diagnostics and telemetry over time, such as reporting data to a database then analysing this via a spreadsheet or something to do with Matplotlib, to create graphs and show peak usage times, etc. 
