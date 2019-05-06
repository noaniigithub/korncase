package vk.com.korne3v.KornCase.events;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import vk.com.korne3v.KornCase.Utils;
import vk.com.korne3v.KornCase.playersdata.getData;
import vk.com.korne3v.KornCase.system.Animation;
import vk.com.korne3v.KornCase.system.Case;
import vk.com.korne3v.KornCase.system.CaseLoader;
import vk.com.korne3v.KornCase.system.ItemCase;

import java.util.*;

public class EventManager implements Listener {

    private HashMap<HumanEntity, ItemCase> icMap = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!getData.check(event.getPlayer().getUniqueId())) {
            getData.create(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCaseGuiClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null) return;
        if(event.getClickedInventory() == null) return;
        if(event.getClick() == null) return;

        if(event.getWhoClicked().getOpenInventory().getTopInventory().getTitle().contains(Utils.getText("case_title_name"))){
            if(event.getWhoClicked().getOpenInventory().getTopInventory().getHolder() == null){
                event.setCancelled(true);

                Player player = (Player) event.getWhoClicked();
                if(event.getSlot() >= 9){
                    player.playSound(player.getLocation(),Sound.ENTITY_ITEM_BREAK,1f,30f);
                }else{
                    player.playSound(player.getLocation(),Sound.ENTITY_ARROW_HIT_PLAYER,1f,30f);
                }
            }
        }
    }

    @EventHandler
    public void onOpen(PlayerInteractEvent event) {
        if (!event.hasBlock()) return;
        if (event.getClickedBlock().getType() == Material.ENDER_CHEST) {
            if (CaseLoader.getMapCases.containsKey(event.getClickedBlock())) {
                event.setCancelled(true);
                Case CASE = CaseLoader.getMapCases.get(event.getClickedBlock());
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (getData.getCases(event.getPlayer().getUniqueId()) > 0) {
                        if (!CASE.isWork()) {
                            List<ItemCase> items = new ArrayList<>();
                            event.getPlayer().sendMessage(Utils.getText("open_case"));
                            event.getPlayer().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 10f);
                            for (int i = 0; i < 10; i++)
                                items.add(CASE.getItemCases().get(new Random().nextInt(CASE.getItemCases().size())));
                            Animation.play(event.getPlayer(), CaseLoader.getMapCases.get(event.getClickedBlock()), new ArrayList<>());
                            getData.removeCases(event.getPlayer().getUniqueId(), 1);
                        } else {
                            event.getPlayer().sendMessage(Utils.getText("wait_open"));
                            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,1,1),true);
                            event.getPlayer().playSound(event.getPlayer().getLocation(),Sound.ENTITY_VILLAGER_NO,1f,55f);
                        }
                    } else {
                        event.getPlayer().sendMessage(Utils.getText("case_not_found"));
                        event.getPlayer().playSound(event.getPlayer().getLocation(),Sound.ENTITY_VILLAGER_NO,1f,55f);
                    }
                }else{
                    if(event.getPlayer().hasPermission(Utils.getPermission("open_inventory_case"))){
                        Inventory inv = Bukkit.createInventory(null,54,Utils.getText("case_title_name"));

                        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE,1, (short) new Random().nextInt(15));
                        ItemMeta ism = itemStack.getItemMeta();
                        ism.setDisplayName(" ");
                        itemStack.setItemMeta(ism);

                        ItemStack ecase = new ItemStack(Material.ENDER_CHEST);
                        ItemMeta ecm = ecase.getItemMeta();
                        ecm.setDisplayName(Utils.getText("case_in_inventory_name"));
                        List<String> lore = new ArrayList<>(Arrays.asList(Utils.getText("case_in_inventory_lore").replace("%work%", String.valueOf(CASE.isWork())).replace("%size%", String.valueOf(CASE.getItemCases().size())).split(";")));
                        ecm.setLore(lore);
                        ecase.setItemMeta(ecm);

                        for(int i = 0;i<9;i++){
                            inv.setItem(i,itemStack);
                        }

                        inv.setItem(4,ecase);

                        for(ItemCase itemCase : CASE.getItemCases()){
                            if(inv.firstEmpty() >= 1) {
                                ItemStack item = itemCase.getItemStack().clone();
                                ItemMeta meta = item.getItemMeta();
                                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',itemCase.getName()));
                                List<String> ilore = new ArrayList<>(Arrays.asList(Utils.getText("item_in_inventory_lore").replace("%chance%", String.valueOf(itemCase.getChance())).replace("%name%", String.valueOf(itemCase.getName())).split(";")));
                                if(event.getPlayer().hasPermission(Utils.getPermission("view_rewardcmd_inventory_case"))) {
                                    ilore.add(" ");
                                    ilore.add("§8[§7/" + itemCase.getReward().replace("%player%",event.getPlayer().getName()) + "§8]");
                                    ilore.add(" ");
                                }
                                meta.setLore(ilore);
                                item.setItemMeta(meta);
                                inv.addItem(item);
                            }
                        }

                        event.getPlayer().openInventory(inv);
                    }
                }
            }
        }
    }
}
