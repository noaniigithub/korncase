package vk.com.korne3v.KornCase;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import vk.com.korne3v.KornCase.playersdata.getData;
import vk.com.korne3v.KornCase.system.Case;
import vk.com.korne3v.KornCase.system.CaseLoader;
import vk.com.korne3v.KornCase.system.ItemCase;

public class CommandManager implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("korncase.admin")) return true;
        try {
            if (args.length == 0) {
                if(sender instanceof ConsoleCommandSender) {
                    sender.sendMessage("§3/case list §8- §fList of available cases");
                    sender.sendMessage("§3/case reload §8- §fReloads plugin and configuration");
                    sender.sendMessage("§3/case get <player> §8- §fReturn player count cases");
                    return true;
                }else{
                    for (String string : Main.getInstance().getConfig().getStringList("plugin_message.help"))
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', string).replaceAll("%plver%", Main.getInstance().getDescription().getVersion()));
                    return true;
                }
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    for(Case CASE : CaseLoader.getMapCases.values()) {
                        sender.sendMessage("§bCase: " + CASE.toString());
                        sender.sendMessage("§8- §fLocation: §3" + CASE.getLocation().getWorld().getName()+"/"+ CASE.getLocation().getBlockX()+"/"+ CASE.getLocation().getBlockY()+"/"+ CASE.getLocation().getBlockZ());
                        sender.sendMessage("§8- §fChest locked: §3"+CASE.isWork());
                        sender.sendMessage("§8- §fItems available: §3"+CASE.getItemCases().size());
                        sender.sendMessage("§8-----------------------------------------------------");
                        sender.sendMessage("");
                        for(ItemCase ic : CASE.getItemCases()){
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"§8-- §fName: "+ic.getName()));
                            sender.sendMessage("§8-- §fReward: "+ic.getReward());
                            sender.sendMessage("§8-- §fItem: "+ic.getItemStack().getType()+"/"+ic.getItemStack().getType().getMaxDurability());
                            sender.sendMessage("");
                        }
                        sender.sendMessage("§8-----------------------------------------------------");
                    }
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    if(sender.isOp()){
                        sender.sendMessage("§aStarting the reload of the plugin ..");
                        Main.ReLoad();
                        sender.sendMessage("§aPlugin reloaded!");
                        sender.sendMessage("§7§ocheck console for errors");
                    }
                }
                if (args[0].equalsIgnoreCase("get")) {
                    sender.sendMessage("§3Cases: §f"+getData.getCases(((Player) sender).getUniqueId()).toString());
                }
            }
            if(args.length == 2){
                if (args[0].equalsIgnoreCase("get")) {
                    if(Bukkit.getPlayer(args[1]).isOnline())
                        sender.sendMessage("§3"+args[1]+" cases: §f"+getData.getCases(Bukkit.getPlayer(args[1]).getUniqueId()));
                }
            }
            if(sender instanceof ConsoleCommandSender) return true;
            Player player = (Player) sender;
            if (args[0].equalsIgnoreCase("edit")) {
                if (args.length == 2) {
                    String key = args[1];
                    player.sendMessage("§3/case edit "+key+" name <name> <newname>");
                    player.sendMessage("§3/case edit "+key+" reward <name> <command>");
                    player.sendMessage("§3/case edit "+key+" item <name>");
                    return true;
                }
                String key = args[1];
                if(args.length == 4){
                    if (args[2].equalsIgnoreCase("item")) {
                        CaseLoader.setItemStackInItemCase(key, args[3], player.getItemInHand());
                        CaseLoader.save();
                        CaseLoader.clear();
                        CaseLoader.load();
                        player.sendMessage("§3Changed the subject of " + args[3] + " in the case " + key);
                        return true;
                    }
                }
                if (args.length == 5) {
                    if (args[2].equalsIgnoreCase("name")) {
                        CaseLoader.setNameItem(key, args[3], args[4].replace("-"," "));
                        CaseLoader.save();
                        CaseLoader.clear();
                        CaseLoader.load();
                        player.sendMessage("§3Changed the subject of " + args[3] + " in the case " + key);
                        return true;
                    }
                    if (args[2].equalsIgnoreCase("reward")) {
                        CaseLoader.setRewardItem(key, args[3], args[4].replace("-"," "));
                        CaseLoader.save();
                        CaseLoader.clear();
                        CaseLoader.load();
                        player.sendMessage("§3Changed the subject of " + args[3] + " in the case " + key);
                        return true;
                    }
                    if (args[2].equalsIgnoreCase("chance")) {
                        CaseLoader.setChanceItem(key, args[3], Double.parseDouble(args[4]));
                        CaseLoader.save();
                        CaseLoader.clear();
                        CaseLoader.load();
                        player.sendMessage("§3Changed the subject of " + args[3] + " in the case " + key);
                        return true;
                    }
                }
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    String key = args[1];
                    CaseLoader.create(key);
                    player.sendMessage("§3Created a new case " + key);
                }
                if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
                    String key = args[1];
                    CaseLoader.removeCase(key);
                    player.sendMessage("§3Deleted a new case " + key);
                }
                if (args[0].equalsIgnoreCase("additem")) {
                    String key = args[1];
                    CaseLoader.addItem(key, player.getItemInHand(),0.5);
                    player.sendMessage("§3Item added to " + key);
                }
                if (args[0].equalsIgnoreCase("setblock")) {
                    String key = args[1];
                    CaseLoader.setLocation(key, player.getTargetBlock(null, 10).getLocation());
                    Main.ReLoad();
                    player.sendMessage("§3Case " + key + " was set!");
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("give")) {
                    int coint = Integer.parseInt(args[2]);
                    String playername = args[1];
                    getData.addCases(Bukkit.getPlayer(playername).getUniqueId(), coint);
                    player.sendMessage("§6You added a player " + playername + " / " + coint + " cases");
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    int coint = Integer.parseInt(args[2]);
                    String playername = args[1];
                    getData.removeCases(Bukkit.getPlayer(playername).getUniqueId(), coint);
                    player.sendMessage("§3You removed a player " + playername + " / " + coint + " cases");
                }
                if (args[0].equalsIgnoreCase("set")) {
                    int coint = Integer.parseInt(args[2]);
                    String playername = args[1];
                    getData.setCases(Bukkit.getPlayer(playername).getUniqueId(), coint);
                    player.sendMessage("§3You set a player " + playername + " / " + coint + " cases");
                }
            }
        }catch (Exception e){
            sender.sendMessage("§cCheck the correctness of the entered command..");
            e.printStackTrace();
        }
        return false;
    }
}
