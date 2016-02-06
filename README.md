# KevsPermissions
A lightweight permissions plugin every serveradmin must have!  
  
The download: https://www.dropbox.com/s/ryt824bpzifs9uo/KevsPermissions.jar?dl=1  
  
Commands:  
/kp <help> or /kevspermissions <help>  
  
Permissions:  
Simply the kp.subcommand; example: kp.help or kp.setgroup  
  
**SpigotMC Ressource:** https://www.spigotmc.org/resources/kevspermissions.16549/  
  
  
  
[CENTER][FONT=Arial][SIZE=6]***KevsPermissions***[/SIZE][/FONT]
[I][FONT=Arial]Functions[/FONT][/I][/CENTER]
[FONT=Arial]![](http://i.imgur.com/aJVrYio.png)  [SIZE=7]Functions[/SIZE][/FONT]

* [FONT=Arial]multiple global and world groups per player[/FONT]
* [FONT=Arial]prefix and suffix[/FONT]
* [FONT=Arial]plugin is lightweight[/FONT]
* [FONT=Arial]tab completor[/FONT]
* [FONT=Arial]yaml config[/FONT]
* [FONT=Arial]copy permissions from one to another[/FONT]
* [FONT=Arial]integrated chat manager and tablist prefix (toggleable)[/FONT]
* [FONT=Arial]Vault is **OPTIONAL **from version 1.2g[/FONT]
* [FONT=Arial]'*' permission like from GroupManager works[/FONT]
* [FONT=Arial][I]not-a-function: [/I]available on GitHub (OpenSource): [URL]https://github.com/Kev575/KevsPermissions/[/URL][/FONT]

[FONT=Arial]![](http://i.imgur.com/pe6YsOE.png) [SIZE=7]Information[/SIZE][/FONT]

* Installation? Just put the jar file in the plugins folder.
* Config.yml? Set the default group and toggle antibuild
* Other .yml's? Only modify if you know what you're doing!! You can do everything using commands ;)

[FONT=Arial]
![](http://i.imgur.com/FuHSEuC.png)  [SIZE=7]Commands[/SIZE][/FONT]

* [FONT=Arial]**command **» **description** » **permission**[/FONT]
* [FONT=Arial]/kp setgroup <Player> <Group> » Adds/removes a group to/from a player » kp.setgroup[/FONT]
* [FONT=Arial]/kp setwgroup <Player> <Group> <World> » Set the group of a player for a world » kp.setwgroup[/FONT]
* [FONT=Arial]/kp creategroup/removegroup » creates/removes a group » kp.creategroup / kp.removegroup[/FONT]
* [FONT=Arial]/kp setprefix <Group> <Prefix...> » Sets the prefix of a group » kp.setprefix[/FONT]
* [FONT=Arial]/kp setsuffix <Group> <Suffix...> » Sets the suffix of a groups » kp.setsuffix[/FONT]
* [FONT=Arial]/kp help » prints the help » kp.help[/FONT]
* [FONT=Arial]/kp setperm <Group> <Permission> » Adds/Removes a perimssion to/from a group » kp.setperm[/FONT]
* [FONT=Arial]/kp addsub <GroupFrom> » Copies the permissions from GroupFrom to GroupTo » kp.addsub[/FONT]
* [FONT=Arial]/kp listgroup <Player> » Lists every group of a player » kp.listgroup[/FONT]
* [FONT=Arial]/kp getgroup <Group> » Lists the permissions, the suffix and the prefix » kp.getgroup[/FONT]
* [FONT=Arial]/kp reload » Reloads the config.yml » kp.reload[/FONT]

[FONT=Arial]![](http://i.imgur.com/er6VjbG.png) [SIZE=7]To-Do[/SIZE][/FONT]

* [FONT=Arial]~~ Functions for Tablist Prefix and Chat Prefix and suffix~~[/FONT]
* [FONT=Arial]~~ Per-World-Permissions (requested by [USER=54022]@Colu[/USER])~~[/FONT]
* [FONT=Arial]Add MySQL support (requested by [USER=54022]@CoLu[/USER])[/FONT]
* [FONT=Arial]~~ Permissions for a player (requested by [USER=64216]@KillianPlaysMC[/USER])~~[/FONT]
* [FONT=Arial]Chat-Tooltips (requested by [USER=33076]@kemmeo[/USER])[/FONT]
* [FONT=Arial]~~ Inheritance and~~ /promote (requested by [USER=33076]@kemmeo[/USER])[/FONT]

[FONT=Arial]![](http://i.imgur.com/ZHkBUVH.png) [SIZE=6]Featured Servers[/SIZE][/FONT]

* [FONT=Arial]play.obios.net - [USER=33076]@kemmeo[/USER][/FONT]
* **[FONT=Arial]Please message me, if you want your server to appear here ;)[/FONT]**

[CENTER][I][FONT=Arial]For Developers[/FONT][/I][/CENTER]
[FONT=Arial][SPOILER][/SPOILER][/FONT][SPOILER]
[CENTER]**[FONT=Arial]View the source at ![](http://i.imgur.com/C4TeGIP.png) GitHub:[/FONT]**
[[FONT=Arial]https://github.com/Kev575/KevsPermissions/[/FONT]]('https://github.com/Kev575/KevsPermissions/')[/CENTER]
[FONT=Arial][CODE="java"]KevsPermissions kp;

@Override
public void onEnable() {
  kp = (KevsPermissions) Bukkit.getPluginManager().getPlugin("KevsPermissions");
  if (kp == null) {
    // handle error
  }
}

// How to use:
kp.config.getPlayerGroup(UUID idOfPlayer); // get ArrayList<PlayerGroup>
kp.config.getCfg(); // get config.yml
kp.config.getGroups(); // get groups.yml
kp.config.getPlayers(); // get players.yml
kp.config.saveConfig(); // save config.yml
kp.config.saveGroups(); // save groups.yml
kp.config.savePlayers(); // save players.yml
kp.config.getGroup(String nameOfGroup); // get a group (if not exists it returns group default)
kp.config.getGroup(String nameOfGroup).getPrefix(); // get prefix
kp.config.getGroup(String nameOfGroup).getSuffix(); // get suffix
kp.config.getGroup(String nameOfGroup).getName(); // get name
kp.config.getGroup(String nameOfGroup).permissions; // get List<String> permissions
[/CODE][/FONT][/SPOILER]

![](http://i.imgur.com/9ttkWNR.png) [SIZE=7]Issues/Bugs and suggestions[/SIZE]

* You can use GitHub **OR** the discussion section **OR** private messages for giving me suggestions and reporting bugs. :D
* For feedback **please **use the review section ;)
* Please** DON'T** rate bad if you find any bugs, just report them - I'm going to fix them for you :)

[FONT=Arial][SPOILER="licenses and sources"]
Font generated by <a href="[http://www.flaticon.com]('http://www.flaticon.com/')">flaticon.com</a>
under <a href="[URL]http://creativecommons.org/licenses/by/3.0/[/URL]">CC BY</a>. The authors are: <a href="[http://www.plainicon.com]('http://www.plainicon.com/')">Plainicon</a>, <a href="[http://www.freepik.com]('http://www.freepik.com/')">Freepik</a>, <a href="[http://www.adamwhitcroft.com]('http://www.adamwhitcroft.com/')">Adam Whitcroft</a>.
[/SPOILER]

By downloading and using you agree to this license: 
[URL]https://github.com/Kev575/KevsPermissions/blob/master/LICENSE[/URL]
A short overview is available over here, but it does not replace the whole license: [URL]https://github.com/Kev575/KevsPermissions/blob/master/SHORTLICENSE.md[/URL][/FONT]
