# RPi-5-Minecraft-Server
Project where I created and stress tested a Minecraft Paper/Java Crossplay Server to host for friends and students at my school. 

Features:
- Self-made Java Plug-in (Dcplugin)
  - Simple Fly Command with permissions.
  - Moblore Command to print mob descriptions.
  - Server Diagnostics Command to measure CPU Temp, RAM/Memory usage and allocation. Cooldown implemented also.
  - Timed Server Shutdown Command in minutes. Also shut down the Pi via some kernel commands.
  - Zombie and Skeleton Variant Spawning (Coolest part) with validation.
  - Explosive Projectiles with validation.
- Discord Bot in Python
  - Command to display current users online.
  - Command for Role adding/removing.
- LCD Compatibility for Server Diagnostics in Python
	- Quite similar to the Discord Bot.
- Proxy Server, so no Port Forwarding.
- Good Optimisation
- Aikar's Flags

This project was made PURELY for fun before I realised it was kind of something I should document, even if it was a low level project. 
In fairness, I mainly used youtube tutorials for the JavaScript, Memory Management, Terminal Commands, and Performance Diagnostics, looked online on threads, 
and debugged with AI when stuck. 

This was relatively cool and challenging to make, but in hindsight quite an easy project.

Several optimisations were made to keep it running well and it performed very consistently, only being bottlenecked by
the occasional Playit.gg latency/connection.

Necessary Equipment Used:
- RPi5 (Raspberry Pi 5), 16GB Model
- Raspberry Pi Official Raspberry Pi 27W USB-C Power Supply
- Raspberry Pi 5 Case with Fan, Overclocked and Config Changed
- Raspberry Pi 5 Heatsink

Optional Equipment Used:
- Freenove I2C IIC LCD 2004 Module (Diagnostics)

Services Used:
- Playit.gg 
- Discord
- MCRcon (requires you enabling RCon on server.properties)
- IntelliJ IDEA CE for Plug-in Development
- Geany Programmer Editor for Python Development on RPi5

This will not detail how to set up a server, so follow the official Minecraft website guide and also Paper MC website.
https://www.minecraft.net/en-us/download/server

https://papermc.io/

https://docs.papermc.io/paper/aikars-flags/

To set up Playit.gg:
https://www.youtube.com/watch?v=itVVhcid2_Q

To set up the Discord Bot, head to the Discord Developer Portal and create one, then grab its API token.

Plugins/Minecraft Dependencies Used:
- Newest version of PaperMC Server hoster, you can find this by going to the experimental dropdown.
- ViaVersion - Allows future MC versions to join
- ViaBackwards - Allows older MC versions to join
- Floodgate Spigot - Responsible for Crossplay 
- GeyserMC Spigot - Responsible for Crossplay. Need both Floodgate and Geyser (avoids a certain "issue")
- Chunky Bukkit - This is vital. Pre generates chunks so the Pi doesn't have to later which is very memory demanding.
- "Dcplugin", a self-made plugin through a tutorial on YouTube, which I can't find anymore

Pros:
- Memory Management is very efficient
- Worked with several players all thousands of Blocks apart from each other, even with the custom Mob Spawning, Listeners, Commands Etc.
- Peak Player Usage: 20 Players, iirc, 2-3 were Bedrock, and of those, 1 was Pocket Edition, other two being Xbox.
- Was a good month or two of fun to try out
- Commands were very useful

Cons:
- This was mostly coded from piecing tutorials together.
- May not be secure. Despite port forwarding, the fact a command could shut down the server via kernel commands sounds like a security risk.
- The Discord Bot could also directly talk to the server, run commands and could be another security risk.
- Probably inefficient.
- Not an AI super agent tutorial start-up ultra buzzword project to put on your personal statement for Top Unis.
- Was stress tested with 8GB but unsure of any other specs on a lower end RPi which could affect it.
- RPi5 is fundamentally different to RPi4 and prior so installation may be specific to RPi5 Model.
- Required a few many optimisation techniques and unsure how many I used.
- Did not actually use permission levels which could be implemented using LuckyPerms. Relied on just Operator status which technically is more secure in itself(?)
- I DO NOT KNOW ALL OF THE JAVA DEPENDENCIES SO YOU WILL HAVE TO DOWNLOAD THEM FOR PLUGIN DEVELOPMENT/USE
- The server shutdown command, would've needed a way to see that shutdown time in a command available to everyone. I didn't code this, but this is very useful for QoL in future development.

--**WHAT TO DO**--

1. Overclock your RPi5. Go to /boot/firmware/config.txt and add these flags via sudo nano: over_voltage=2 arm_freq = 2800
   This makes it have more voltage than usual and run at a higher GHz/Clock Speed (2.8GHz). This is a safe threshold so increase at your own risk.
2. You can manipulate fan thresholds via something like this:
   dtparam=fan_temp0=40000
	 dtparam=fan_temp0_hyst=30000
	 dtparam=fan_temp0_speed=135

	 dtparam=fan_temp1=45000
	 dtparam=fan_temp1_hyst=3000
	 dtparam=fan_temp1_speed=175
	 And so on and so forth. 40000 = 40C Celsius, and Hyst is like an uncertainty in degrees to force a change. Speed is just PWM.
	 Furthermore, I did this 2 more times to have 4 separate modes, which followed the same trend of increasing temp and PWM proportionately.
4. Set-up all Minecraft Plug-ins and Dependencies. Search for every website one, and my Dcplugin is linked on this project.
5. Create your Playit.gg account, and make a tunnel, linking the ports correctly and running playit on your RPi5.
6. Agree to EULA on server.properties
7. In server.properties, create an rcon.password
8. Set-up the discord bot and LCD diagnostics script by changing the code attached in my project.
   LCDserver.py and derrickwhitebot.py
	 Connect the API token from the Discord Developer Portal to the script and the RCon passwords.
9. Run the server in the kernel after running LCDserver.py, derrickwhitebot.py AND "playit" in a fresh terminal and connecting anything necessary. The I2C component only used 4 GPIO pins and shouldnt be too hard to use.
	 I used this command to run it:
	 java -Xms12G -Xmx12G -XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200 -XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -XX:+AlwaysPreTouch -XX:G1NewSizePercent=30 -XX:G1MaxNewSizePercent=40 -XX:G1HeapRegionSize=8M -XX:G1ReservePercent=20 -XX:G1HeapWastePercent=5 -XX:G1MixedGCCountTarget=4 -XX:InitiatingHeapOccupancyPercent=15 -XX:G1MixedGCLiveThresholdPercent=90 -XX:G1RSetUpdatingPauseTimePercent=5 -XX:SurvivorRatio=32 -XX:+PerfDisableSharedMem -XX:MaxTenuringThreshold=1 -Dusing.aikars.flags=https://mcflags.emc.gs -Daikars.new.flags=true -jar <server-jar-file>.jar nogui
10. Hopefully it works. When running, use chunky to pre-load some chunks pre-hand and the server should operate quite well.
	
