package vk.com.korne3v.KornCase.api;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import vk.com.korne3v.KornCase.system.CaseLoader;
import vk.com.korne3v.KornCase.playersdata.getData;

public class CasesPlaceHolders extends PlaceholderExpansion {

    @Override
    public boolean register(){
        return me.clip.placeholderapi.PlaceholderAPI.registerPlaceholderHook(getIdentifier(), this);
    }

    @Override
    public String getAuthor(){
        return "korne3v";
    }

    @Override
    public String getIdentifier(){
        return "korncase";
    }

    @Override
    public String getRequiredPlugin(){
        return "KornCase";
    }

    @Override
    public String getVersion(){
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String arg) {
        if(player == null) {
            return "";
        }
        if(getData.getCases(player.getUniqueId()) == null) {
            return "0";
        }
        if(CaseLoader.getMapCases == null) {
            return "0";
        }
        if(arg.equals("player_count")) {
            return getData.getCases(player.getUniqueId()).toString();
        }
        if(arg.equals("cases")) {
            return String.valueOf(CaseLoader.getMapCases.size());
        }
        return null;
    }
}
