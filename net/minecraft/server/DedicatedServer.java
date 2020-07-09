package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CraftBukkit start
import java.io.PrintStream;
import org.apache.logging.log4j.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.LoggerOutputStream;
import org.bukkit.craftbukkit.SpigotTimings; // Spigot
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.event.server.RemoteServerCommandEvent;
// CraftBukkit end

public class DedicatedServer extends MinecraftServer implements IMinecraftServer {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Pattern i = Pattern.compile("^[a-fA-F0-9]{40}$");
    private final List<ServerCommand> serverCommandQueue = Collections.synchronizedList(Lists.<ServerCommand>newArrayList()); // CraftBukkit - fix decompile error
    private RemoteStatusListener k;
    public final RemoteControlCommandListener remoteControlCommandListener = new RemoteControlCommandListener(this);
    private RemoteControlListener m;
    public PropertyManager propertyManager;
    private EULA o;
    private boolean generateStructures;
    private EnumGamemode q;
    private boolean r;

    // CraftBukkit start - Signature changed
    public DedicatedServer(joptsimple.OptionSet options, DataFixer datafixer, YggdrasilAuthenticationService yggdrasilauthenticationservice, MinecraftSessionService minecraftsessionservice, GameProfileRepository gameprofilerepository, UserCache usercache) {
        super(options, Proxy.NO_PROXY, datafixer, new CommandDispatcher().init(true), yggdrasilauthenticationservice, minecraftsessionservice, gameprofilerepository, usercache);
        // CraftBukkit end
        Thread thread = new Thread("Server Infinisleeper") {
            {
                this.setDaemon(true);
                this.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(DedicatedServer.LOGGER));
                this.start();
            }

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2147483647L);
                    } catch (InterruptedException interruptedexception) {
                        ;
                    }
                }
            }
        };
    }

    public boolean init() throws IOException { // CraftBukkit - decompile error
        Thread thread = new Thread("Server console handler") {
            public void run() {
                // CraftBukkit start
                if (!org.bukkit.craftbukkit.Main.useConsole) {
                    return;
                }
                jline.console.ConsoleReader bufferedreader = reader;
                // CraftBukkit end

                String s;

                try {
                    // CraftBukkit start - JLine disabling compatibility
                    while (!DedicatedServer.this.isStopped() && DedicatedServer.this.isRunning()) {
                        if (org.bukkit.craftbukkit.Main.useJline) {
                            s = bufferedreader.readLine(">", null);
                        } else {
                            s = bufferedreader.readLine();
                        }
                        if (s != null && s.trim().length() > 0) { // Trim to filter lines which are just spaces
                            DedicatedServer.this.issueCommand(s, DedicatedServer.this.getServerCommandListener());
                        }
                        // CraftBukkit end
                    }
                } catch (IOException ioexception) {
                    DedicatedServer.LOGGER.error("Exception handling console input", ioexception);
                }

            }
        };

        // CraftBukkit start - TODO: handle command-line logging arguments
        java.util.logging.Logger global = java.util.logging.Logger.getLogger("");
        global.setUseParentHandlers(false);
        for (java.util.logging.Handler handler : global.getHandlers()) {
            global.removeHandler(handler);
        }
        global.addHandler(new org.bukkit.craftbukkit.util.ForwardLogHandler());

        final org.apache.logging.log4j.core.Logger logger = ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger());
        for (org.apache.logging.log4j.core.Appender appender : logger.getAppenders().values()) {
            if (appender instanceof org.apache.logging.log4j.core.appender.ConsoleAppender) {
                logger.removeAppender(appender);
            }
        }

        new Thread(new org.bukkit.craftbukkit.util.TerminalConsoleWriterThread(System.out, this.reader)).start();

        System.setOut(new PrintStream(new LoggerOutputStream(logger, Level.INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(logger, Level.WARN), true));
        // CraftBukkit end

        thread.setDaemon(true);
        thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(DedicatedServer.LOGGER));
        thread.start();
        DedicatedServer.LOGGER.info("Starting minecraft server version 1.13");
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            DedicatedServer.LOGGER.warn("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        DedicatedServer.LOGGER.info("Loading properties");
        this.propertyManager = new PropertyManager(this.options); // CraftBukkit - CLI argument support
        this.o = new EULA(new File("eula.txt"));
        // Spigot Start
        boolean eulaAgreed = Boolean.getBoolean( "com.mojang.eula.agree" );
        if ( eulaAgreed )
        {
            System.err.println( "You have used the Spigot command line EULA agreement flag." );
            System.err.println( "By using this setting you are indicating your agreement to Mojang's EULA (https://account.mojang.com/documents/minecraft_eula)." );
            System.err.println( "If you do not agree to the above EULA please stop your server and remove this flag immediately." );
        }
        // Spigot End
        if (!this.o.a() && !eulaAgreed) { // Spigot
            DedicatedServer.LOGGER.info("You need to agree to the EULA in order to run the server. Go to eula.txt for more info.");
            this.o.b();
            return false;
        } else {
            if (this.J()) {
                this.b("127.0.0.1");
            } else {
                this.setOnlineMode(this.propertyManager.getBoolean("online-mode", true));
                this.f(this.propertyManager.getBoolean("prevent-proxy-connections", false));
                this.b(this.propertyManager.getString("server-ip", ""));
            }

            this.setSpawnAnimals(this.propertyManager.getBoolean("spawn-animals", true));
            this.setSpawnNPCs(this.propertyManager.getBoolean("spawn-npcs", true));
            this.setPVP(this.propertyManager.getBoolean("pvp", true));
            this.setAllowFlight(this.propertyManager.getBoolean("allow-flight", false));
            this.setResourcePack(this.propertyManager.getString("resource-pack", ""), this.aV());
            this.setMotd(this.propertyManager.getString("motd", "A Minecraft Server"));
            this.setForceGamemode(this.propertyManager.getBoolean("force-gamemode", false));
            this.setIdleTimeout(this.propertyManager.getInt("player-idle-timeout", 0));
            this.l(this.propertyManager.getBoolean("enforce-whitelist", false));
            if (this.propertyManager.getInt("difficulty", 1) < 0) {
                this.propertyManager.setProperty("difficulty", Integer.valueOf(0));
            } else if (this.propertyManager.getInt("difficulty", 1) > 3) {
                this.propertyManager.setProperty("difficulty", Integer.valueOf(3));
            }

            this.generateStructures = this.propertyManager.getBoolean("generate-structures", true);
            int i = this.propertyManager.getInt("gamemode", EnumGamemode.SURVIVAL.getId());

            this.q = WorldSettings.a(i);
            DedicatedServer.LOGGER.info("Default game type: {}", this.q);
            InetAddress inetaddress = null;

            if (!this.getServerIp().isEmpty()) {
                inetaddress = InetAddress.getByName(this.getServerIp());
            }

            if (this.H() < 0) {
                this.setPort(this.propertyManager.getInt("server-port", 25565));
            }
            // Spigot start
            this.a((PlayerList) (new DedicatedPlayerList(this)));
            org.spigotmc.SpigotConfig.init((File) options.valueOf("spigot-settings"));
            org.spigotmc.SpigotConfig.registerCommands();
            // Spigot end

            DedicatedServer.LOGGER.info("Generating keypair");
            this.a(MinecraftEncryption.b());
            DedicatedServer.LOGGER.info("Starting Minecraft server on {}:{}", this.getServerIp().isEmpty() ? "*" : this.getServerIp(), Integer.valueOf(this.H()));

        if (!org.spigotmc.SpigotConfig.lateBind) {
            try {
                this.getServerConnection().a(inetaddress, this.H());
            } catch (IOException ioexception) {
                DedicatedServer.LOGGER.warn("**** FAILED TO BIND TO PORT!");
                DedicatedServer.LOGGER.warn("The exception was: {}", ioexception.toString());
                DedicatedServer.LOGGER.warn("Perhaps a server is already running on that port?");
                return false;
            }
        }

            // CraftBukkit start
            // this.a((PlayerList) (new DedicatedPlayerList(this))); // Spigot - moved up
            server.loadPlugins();
            server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.STARTUP);
            // CraftBukkit end

            if (!this.getOnlineMode()) {
                DedicatedServer.LOGGER.warn("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
                DedicatedServer.LOGGER.warn("The server will make no attempt to authenticate usernames. Beware.");
                // Spigot start
                if (org.spigotmc.SpigotConfig.bungee) {
                    DedicatedServer.LOGGER.warn("Whilst this makes it possible to use BungeeCord, unless access to your server is properly restricted, it also opens up the ability for hackers to connect with any username they choose.");
                    DedicatedServer.LOGGER.warn("Please see http://www.spigotmc.org/wiki/firewall-guide/ for further information.");
                } else {
                    DedicatedServer.LOGGER.warn("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
                }
                // Spigot end
                DedicatedServer.LOGGER.warn("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
            }

            if (this.aZ()) {
                this.getUserCache().c();
            }

            if (!NameReferencingFileConverter.a(this.propertyManager)) {
                return false;
            } else {
                this.convertable = new WorldLoaderServer(server.getWorldContainer().toPath(), server.getWorldContainer().toPath().resolve("../backups"), this.dataConverterManager); // CraftBukkit - moved from MinecraftServer constructor
                long j = SystemUtils.c();

                if (this.getWorld() == null) {
                    this.setWorld(this.propertyManager.getString("level-name", "world"));
                }

                String s = this.propertyManager.getString("level-seed", "");
                String s1 = this.propertyManager.getString("level-type", "DEFAULT");
                String s2 = this.propertyManager.getString("generator-settings", "");
                long k = (new Random()).nextLong();

                if (!s.isEmpty()) {
                    try {
                        long l = Long.parseLong(s);

                        if (l != 0L) {
                            k = l;
                        }
                    } catch (NumberFormatException numberformatexception) {
                        k = (long) s.hashCode();
                    }
                }

                WorldType worldtype = WorldType.getType(s1);

                if (worldtype == null) {
                    worldtype = WorldType.NORMAL;
                }

                this.getEnableCommandBlock();
                this.k();
                this.getSnooperEnabled();
                this.ay();
                this.c(this.propertyManager.getInt("max-build-height", 256));
                this.c((this.getMaxBuildHeight() + 8) / 16 * 16);
                this.c(MathHelper.clamp(this.getMaxBuildHeight(), 64, 256));
                this.propertyManager.setProperty("max-build-height", Integer.valueOf(this.getMaxBuildHeight()));
                TileEntitySkull.a(this.getUserCache());
                TileEntitySkull.a(this.ar());
                UserCache.a(this.getOnlineMode());
                DedicatedServer.LOGGER.info("Preparing level \"{}\"", this.getWorld());
                JsonObject jsonobject = new JsonObject();

                if (worldtype == WorldType.FLAT) {
                    jsonobject.addProperty("flat_world_options", s2);
                } else if (!s2.isEmpty()) {
                    jsonobject = (new JsonParser()).parse(s2).getAsJsonObject();
                }

                this.a(this.getWorld(), this.getWorld(), k, worldtype, jsonobject);
                long i1 = SystemUtils.c() - j;
                String s3 = String.format(Locale.ROOT, "%.3fs", new Object[] { Double.valueOf((double) i1 / 1.0E9D)});

                DedicatedServer.LOGGER.info("Done ({})! For help, type \"help\"", s3);
                if (this.propertyManager.a("announce-player-achievements")) {
                    this.aQ().set("announceAdvancements", this.propertyManager.getBoolean("announce-player-achievements", true) ? "true" : "false", this);
                    this.propertyManager.b("announce-player-achievements");
                    this.propertyManager.savePropertiesFile();
                }

                if (this.propertyManager.getBoolean("enable-query", false)) {
                    DedicatedServer.LOGGER.info("Starting GS4 status listener");
                    this.k = new RemoteStatusListener(this);
                    this.k.a();
                }

                if (this.propertyManager.getBoolean("enable-rcon", false)) {
                    DedicatedServer.LOGGER.info("Starting remote control listener");
                    this.m = new RemoteControlListener(this);
                    this.m.a();
                    this.remoteConsole = new org.bukkit.craftbukkit.command.CraftRemoteConsoleCommandSender(this.remoteControlCommandListener); // CraftBukkit
                }

                // CraftBukkit start
                if (this.server.getBukkitSpawnRadius() > -1) {
                    DedicatedServer.LOGGER.info("'settings.spawn-radius' in bukkit.yml has been moved to 'spawn-protection' in server.properties. I will move your config for you.");
                    this.propertyManager.properties.remove("spawn-protection");
                    this.propertyManager.getInt("spawn-protection", this.server.getBukkitSpawnRadius());
                    this.server.removeBukkitSpawnRadius();
                    this.propertyManager.savePropertiesFile();
                }
                // CraftBukkit end

        if (org.spigotmc.SpigotConfig.lateBind) {
            try {
                this.getServerConnection().a(inetaddress, this.H());
            } catch (IOException ioexception) {
                DedicatedServer.LOGGER.warn("**** FAILED TO BIND TO PORT!");
                DedicatedServer.LOGGER.warn("The exception was: {}", ioexception.toString());
                DedicatedServer.LOGGER.warn("Perhaps a server is already running on that port?");
                return false;
            }
        }

                if (false && this.ba() > 0L) {  // Spigot - disable
                    Thread thread1 = new Thread(new ThreadWatchdog(this));

                    thread1.setUncaughtExceptionHandler(new ThreadNamedUncaughtExceptionHandler(DedicatedServer.LOGGER));
                    thread1.setName("Server Watchdog");
                    thread1.setDaemon(true);
                    thread1.start();
                }

                Items.AIR.a(CreativeModeTab.g, NonNullList.a());
                return true;
            }
        }
    }

    public String aV() {
        if (this.propertyManager.a("resource-pack-hash")) {
            if (this.propertyManager.a("resource-pack-sha1")) {
                DedicatedServer.LOGGER.warn("resource-pack-hash is deprecated and found along side resource-pack-sha1. resource-pack-hash will be ignored.");
            } else {
                DedicatedServer.LOGGER.warn("resource-pack-hash is deprecated. Please use resource-pack-sha1 instead.");
                this.propertyManager.getString("resource-pack-sha1", this.propertyManager.getString("resource-pack-hash", ""));
                this.propertyManager.b("resource-pack-hash");
            }
        }

        String s = this.propertyManager.getString("resource-pack-sha1", "");

        if (!s.isEmpty() && !DedicatedServer.i.matcher(s).matches()) {
            DedicatedServer.LOGGER.warn("Invalid sha1 for ressource-pack-sha1");
        }

        if (!this.propertyManager.getString("resource-pack", "").isEmpty() && s.isEmpty()) {
            DedicatedServer.LOGGER.warn("You specified a resource pack without providing a sha1 hash. Pack will be updated on the client only if you change the name of the pack.");
        }

        return s;
    }

    public void setGamemode(EnumGamemode enumgamemode) {
        super.setGamemode(enumgamemode);
        this.q = enumgamemode;
    }

    public boolean getGenerateStructures() {
        return this.generateStructures;
    }

    public EnumGamemode getGamemode() {
        return this.q;
    }

    public EnumDifficulty getDifficulty() {
        return EnumDifficulty.getById(this.propertyManager.getInt("difficulty", EnumDifficulty.NORMAL.a()));
    }

    public boolean isHardcore() {
        return this.propertyManager.getBoolean("hardcore", false);
    }

    public CrashReport b(CrashReport crashreport) {
        crashreport = super.b(crashreport);
        crashreport.g().a("Is Modded", () -> {
            String s = this.getServerModName();

            return !"vanilla".equals(s) ? "Definitely; Server brand changed to \'" + s + "\'" : "Unknown (can\'t tell)";
        });
        crashreport.g().a("Type", () -> {
            return "Dedicated Server (map_server.txt)";
        });
        return crashreport;
    }

    public void u() { // CraftBukkit - decompile error
        System.exit(0);
    }

    public void w() { // CraftBukkit - fix decompile error
        super.w();
        this.aW();
    }

    public boolean getAllowNether() {
        return this.propertyManager.getBoolean("allow-nether", true);
    }

    public boolean getSpawnMonsters() {
        return this.propertyManager.getBoolean("spawn-monsters", true);
    }

    public void a(MojangStatisticsGenerator mojangstatisticsgenerator) {
        mojangstatisticsgenerator.a("whitelist_enabled", Boolean.valueOf(this.aX().getHasWhitelist()));
        mojangstatisticsgenerator.a("whitelist_count", Integer.valueOf(this.aX().getWhitelisted().length));
        super.a(mojangstatisticsgenerator);
    }

    public boolean getSnooperEnabled() {
        if (this.propertyManager.getBoolean("snooper-enabled", true)) {
            ;
        }

        return false;
    }

    public void issueCommand(String s, CommandListenerWrapper commandlistenerwrapper) {
        this.serverCommandQueue.add(new ServerCommand(s, commandlistenerwrapper));
    }

    public void aW() {
        SpigotTimings.serverCommandTimer.startTiming(); // Spigot
        while (!this.serverCommandQueue.isEmpty()) {
            ServerCommand servercommand = (ServerCommand) this.serverCommandQueue.remove(0);

            // CraftBukkit start - ServerCommand for preprocessing
            ServerCommandEvent event = new ServerCommandEvent(console, servercommand.command);
            server.getPluginManager().callEvent(event);
            if (event.isCancelled()) continue;
            servercommand = new ServerCommand(event.getCommand(), servercommand.source);

            // this.getCommandDispatcher().a(servercommand.source, servercommand.command); // Called in dispatchServerCommand
            server.dispatchServerCommand(console, servercommand);
            // CraftBukkit end
        }

        SpigotTimings.serverCommandTimer.stopTiming(); // Spigot
    }

    public boolean S() {
        return true;
    }

    public boolean X() {
        return this.propertyManager.getBoolean("use-native-transport", true);
    }

    public DedicatedPlayerList aX() {
        return (DedicatedPlayerList) super.getPlayerList();
    }

    public boolean af() {
        return true;
    }

    public int a(String s, int i) {
        return this.propertyManager.getInt(s, i);
    }

    public String a(String s, String s1) {
        return this.propertyManager.getString(s, s1);
    }

    public boolean a(String s, boolean flag) {
        return this.propertyManager.getBoolean(s, flag);
    }

    public void a(String s, Object object) {
        this.propertyManager.setProperty(s, object);
    }

    public void c_() {
        this.propertyManager.savePropertiesFile();
    }

    public String d_() {
        File file = this.propertyManager.c();

        return file != null ? file.getAbsolutePath() : "No settings file";
    }

    public String e() {
        return this.getServerIp();
    }

    public int f() {
        return this.H();
    }

    public String e_() {
        return this.getMotd();
    }

    public void aY() {
        ServerGUI.a(this);
        this.r = true;
    }

    public boolean ai() {
        return this.r;
    }

    public boolean a(EnumGamemode enumgamemode, boolean flag, int i) {
        return false;
    }

    public boolean getEnableCommandBlock() {
        return this.propertyManager.getBoolean("enable-command-block", false);
    }

    public int getSpawnProtection() {
        return this.propertyManager.getInt("spawn-protection", super.getSpawnProtection());
    }

    public boolean a(World world, BlockPosition blockposition, EntityHuman entityhuman) {
        if (world.worldProvider.getDimensionManager().getDimensionID() != 0) {
            return false;
        } else if (this.aX().getOPs().isEmpty()) {
            return false;
        } else if (this.aX().isOp(entityhuman.getProfile())) {
            return false;
        } else if (this.getSpawnProtection() <= 0) {
            return false;
        } else {
            BlockPosition blockposition1 = world.getSpawn();
            int i = MathHelper.a(blockposition.getX() - blockposition1.getX());
            int j = MathHelper.a(blockposition.getZ() - blockposition1.getZ());
            int k = Math.max(i, j);

            return k <= this.getSpawnProtection();
        }
    }

    public int k() {
        return this.propertyManager.getInt("op-permission-level", 4);
    }

    public void setIdleTimeout(int i) {
        super.setIdleTimeout(i);
        this.propertyManager.setProperty("player-idle-timeout", Integer.valueOf(i));
        this.c_();
    }

    public boolean l() {
        return this.propertyManager.getBoolean("broadcast-rcon-to-ops", true);
    }

    public boolean B_() {
        return this.propertyManager.getBoolean("broadcast-console-to-ops", true);
    }

    public int aw() {
        int i = this.propertyManager.getInt("max-world-size", super.aw());

        if (i < 1) {
            i = 1;
        } else if (i > super.aw()) {
            i = super.aw();
        }

        return i;
    }

    public int ay() {
        return this.propertyManager.getInt("network-compression-threshold", super.ay());
    }

    protected boolean aZ() {
        server.getLogger().info( "**** Beginning UUID conversion, this may take A LONG time ****"); // Spigot, let the user know whats up!
        boolean flag = false;

        int i;

        for (i = 0; !flag && i <= 2; ++i) {
            if (i > 0) {
                DedicatedServer.LOGGER.warn("Encountered a problem while converting the user banlist, retrying in a few seconds");
                this.bc();
            }

            flag = NameReferencingFileConverter.a((MinecraftServer) this);
        }

        boolean flag1 = false;

        for (i = 0; !flag1 && i <= 2; ++i) {
            if (i > 0) {
                DedicatedServer.LOGGER.warn("Encountered a problem while converting the ip banlist, retrying in a few seconds");
                this.bc();
            }

            flag1 = NameReferencingFileConverter.b((MinecraftServer) this);
        }

        boolean flag2 = false;

        for (i = 0; !flag2 && i <= 2; ++i) {
            if (i > 0) {
                DedicatedServer.LOGGER.warn("Encountered a problem while converting the op list, retrying in a few seconds");
                this.bc();
            }

            flag2 = NameReferencingFileConverter.c((MinecraftServer) this);
        }

        boolean flag3 = false;

        for (i = 0; !flag3 && i <= 2; ++i) {
            if (i > 0) {
                DedicatedServer.LOGGER.warn("Encountered a problem while converting the whitelist, retrying in a few seconds");
                this.bc();
            }

            flag3 = NameReferencingFileConverter.d((MinecraftServer) this);
        }

        boolean flag4 = false;

        for (i = 0; !flag4 && i <= 2; ++i) {
            if (i > 0) {
                DedicatedServer.LOGGER.warn("Encountered a problem while converting the player save files, retrying in a few seconds");
                this.bc();
            }

            flag4 = NameReferencingFileConverter.a(this, this.propertyManager);
        }

        return flag || flag1 || flag2 || flag3 || flag4;
    }

    private void bc() {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException interruptedexception) {
            ;
        }
    }

    public long ba() {
        return this.propertyManager.getLong("max-tick-time", TimeUnit.MINUTES.toMillis(1L));
    }

    public String getPlugins() {
        // CraftBukkit start - Whole method
        StringBuilder result = new StringBuilder();
        org.bukkit.plugin.Plugin[] plugins = server.getPluginManager().getPlugins();

        result.append(server.getName());
        result.append(" on Bukkit ");
        result.append(server.getBukkitVersion());

        if (plugins.length > 0 && server.getQueryPlugins()) {
            result.append(": ");

            for (int i = 0; i < plugins.length; i++) {
                if (i > 0) {
                    result.append("; ");
                }

                result.append(plugins[i].getDescription().getName());
                result.append(" ");
                result.append(plugins[i].getDescription().getVersion().replaceAll(";", ","));
            }
        }

        return result.toString();
        // CraftBukkit end
    }

    // CraftBukkit start - fire RemoteServerCommandEvent
    public String executeRemoteCommand(final String s) {
        Waitable<String> waitable = new Waitable<String>() {
            @Override
            protected String evaluate() {
                remoteControlCommandListener.clearMessages();
                // Event changes start
                RemoteServerCommandEvent event = new RemoteServerCommandEvent(remoteConsole, s);
                server.getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return "";
                }
                // Event change end
                ServerCommand serverCommand = new ServerCommand(event.getCommand(), remoteControlCommandListener.f());
                server.dispatchServerCommand(remoteConsole, serverCommand);
                return remoteControlCommandListener.getMessages();
            }
        };
        processQueue.add(waitable);
        try {
            return waitable.get();
        } catch (java.util.concurrent.ExecutionException e) {
            throw new RuntimeException("Exception processing rcon command " + s, e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Maintain interrupted state
            throw new RuntimeException("Interrupted processing rcon command " + s, e);
        }
        // CraftBukkit end
    }

    public PlayerList getPlayerList() {
        return this.aX();
    }

    // CraftBukkit start
    @Override
    public PropertyManager getPropertyManager() {
        return this.propertyManager;
    }

    @Override
    public CommandSender getBukkitSender(CommandListenerWrapper wrapper) {
        return console;
    }
    // CraftBukkit end
}