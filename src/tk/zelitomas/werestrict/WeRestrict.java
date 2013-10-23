package tk.zelitomas.werestrict;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Made for SpA
 * @author zelitomas
 */
public class WeRestrict extends JavaPlugin{
        public void onEnable() {
            new PreCommandListener(this);
        }
}
class PreCommandListener implements Listener {
    private final JavaPlugin plugin;
    public PreCommandListener(JavaPlugin plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
        @EventHandler(priority = EventPriority.LOWEST)
        public void onCommandPreprocess(PlayerCommandPreprocessEvent event){
        FileConfiguration config = this.plugin.getConfig();
        List<String> zakazano = config.getStringList("commands");
        for(String prikaz : zakazano){
            if(event.getMessage().toLowerCase().startsWith(prikaz)){
                int blocks = -1;
                Player player = event.getPlayer();
                WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
                Selection selection = worldEdit.getSelection(player);
                for(int i = 1; i < 9; i++){
                    if(player.hasPermission("werestrict.forbiddien")){
                        blocks = -1;
                    }
                    if(player.hasPermission("werestrict.restriction.L" + Integer.toString(i))){
                        blocks = config.getInt("L" + Integer.toString(i));
                    }
                    if(player.hasPermission("werestrict.override")){
                        blocks = 0;
                    }
                }
                if(selection != null && blocks > 0) {
                    if(selection.getArea() > 200){
                        event.setCancelled(true);
                        player.sendRawMessage("§f[§6WE§arestrict§f]§9 This may affect more than " + Integer.toString(blocks) + " blocks!! If you really need make changes in bigger regions, ask our [O]+. §7§OSelect another region.");
                    }
                }if(blocks == -1){
                    event.setCancelled(true);
                    player.sendMessage("§f[§6WE§arestrict§f]§9 You don't have access to this command (no permission or permission werestrict.forbiddien is set for you) if you think, its a mistake, contact any [O]+");
                }
            }
        }
    }
}
