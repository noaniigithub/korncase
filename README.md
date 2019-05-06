# KornCase v1.1

Plugin to cases that are suitable for any project and are good for mini-modes
in the plugin, you can customize the animation and localization

# Default Config Contents

BaseDataEnabled: false
PlaceholdersEnabled: true
CreateWorld: false
Settings:
  effect_speed: true
  level_change: true
  break_effect: true
  circle_effect: true
  Effects:
    circledParticle: 'MOBSPAWNER_FLAMES'
    circledItemParticle: 'HAPPY_VILLAGER'
    finalParticle: 'MOBSPAWNER_FLAMES'
    preCloseParticle: 'MOBSPAWNER_FLAMES'
  Sounds:
    circledSound: 'UI_BUTTON_CLICK'
    finalSound: 'ENTITY_WITHER_DEATH'
    preCloseSound: 'ENTITY_ARROW_HIT_PLAYER'
Permissions:
  open_inventory_case: 'korncase.open.inventory'
  view_rewardcmd_inventory_case: 'korncase.view.rewardcmd'
  knockback_bypass: 'korncase.knockback.bypass'
plugin_message:
  help:
   - '&bKornCase v%plver% &8| &fplugin by &7vk.com/korne3v'
   - ''
   - '&3/case create <casename> &8- &fCreates a new case'
   - '&3/case additem <casename> &8- &fAdds a new item.'
   - '&3/case setblock <casename> &8- &fSets the case on the block'
   - '&3/case givecase <player> <count> &8- &fAdds player cases'
   - '&3/case removecase <player> <count> &8- &fRemoves player cases'
   - '&3/case setcase <player> <count> &8- &fSets player cases'
   - ''
   - '&3/case edit <casename> &8- &fItem Editor'
   - '&3/case edit <casename> changename <name> <newname> &8- &fChanges subject name'
   - '&3/case edit <casename> changereward <name> <command> &8- &fChanges the reward command'
   - '&3/case edit <casename> changeitem <name> &8- &fHold the item in your hand as you type.'
   - ''
language:
  selected: "ru"
  ru:
    prefix: '&5Таинственный сундук &8| &f'
    broadcast: '%prefix%Игрок &7%player_name%&f выбил из кейса&7 %reward%'
    open_case: '&aОткрываем сундук..'
    wait_open: '&cПожалуйста подождите..'
    case_not_found: '&cУ вас нет кейсов!'
    case_title_name: '&8Содержимое сундука: '
    case_in_inventory_name: '&bТекущий сундук'
    case_in_inventory_lore: '&r;&fВ работе:&7 %work%;&fДоступно предметов: %size%;&r'
    item_in_inventory_lore: '&r;&fШанс: &e%chance%;&r'
    case_hologram:
     - '&5Таинственный сундук'
     - '&7нажмите, чтобы открыть'
  ua:
    prefix: '&5Таємничий скриню &8| &f'
    broadcast: '%prefix%Гравець &7%player_name%&f отримав з кейса&7 %reward%'
    open_case: '&aВідкриваємо скриню ..'
    wait_open: '&cБудь ласка зачекайте..'
    case_not_found: '&cУ вас немає кейсів!'
    case_title_name: '&8Вміст скрині:'
    case_in_inventory_name: '&bПоточний скриню'
    case_in_inventory_lore: '&r;&fВ роботі:&7 %work%;&fДоступно предметів: %size%;&r'
    item_in_inventory_lore: '&r;&fШанс: &e%chance%;&r'
    case_hologram:
     - '&5Таємничий скриню'
     - '&7натисніть, щоб відкрити'
  en:
    prefix: '&5Mysterious Chest &8| &f'
    broadcast: '%prefix%Player &7%player_name%&f received from the case of&7 %reward%'
    open_case: '&aOpen the chest...'
    wait_open: '&cPlease wait..'
    case_not_found: '&cYou have no cases!'
    case_title_name: '&8Chest Contents: '
    case_in_inventory_name: '&bCurrent Chest'
    case_in_inventory_lore: '&r;&fIn work:&7 %work%;&fAvailable items: %size%;&r'
    item_in_inventory_lore: '&r;&fChance: &e%chance%;&r'
    case_hologram:
     - '&5Mysterious Chest'
     - '&7click to open'
  de:
    prefix: '&5Geheimnisvolle Truhe &8| &f'
    broadcast: '%prefix%Der Spieler &7%player_name%&f erhielt vom fall&7 %reward%'
    open_case: '&aÖffne die Truhe ..'
    wait_open: '&cBitte warten Sie..'
    case_not_found: '&cSie haben keine Fälle!'
    case_title_name: '&8Inhalt der Truhe:'
    case_in_inventory_name: '&bAktuelle Truhe'
    case_in_inventory_lore: '&r;&fIn Arbeit:&7 %work%;&fVerfügbare Artikel: %size%;&r'
    item_in_inventory_lore: '&r;&fChance: &e%chance%;&r'
    case_hologram:
     - '&5Geheimnisvolle Truhe'
     - '&7Klicken Sie zum Öffnen'

# Bugs & Errors please report here:
VK: https://vk.com/korne3v
Spigot: https://www.spigotmc.org/members/korne3v.685012/

