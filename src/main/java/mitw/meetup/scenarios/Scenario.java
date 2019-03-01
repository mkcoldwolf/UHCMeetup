package mitw.meetup.scenarios;

import lombok.Getter;
import lombok.Setter;
import net.development.mitw.Mitw;
import net.development.mitw.utils.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public enum Scenario {

    TimeBomb(Material.CHEST, "&6&l死亡箱子", "&6&lTimeBomb"),
    NoClean(Material.IRON_SWORD, "&d&l防止撿頭","&d&lNoClean"),
    RodLess(Material.FISHING_ROD, "&9&l無釣竿","&9&lRodLess"),
    BowLess(Material.BOW,"&e&l無弓","&e&lBowLess"),
    FireLess(Material.FLINT_AND_STEEL, "&6&l無火焰傷害","&6&lFireLess"),
    AirDrops(Material.ENDER_CHEST,"&e&l空投","&e&lAirDrops"),
    IronRush(Material.IRON_CHESTPLATE, "&f&l鐵裝&c&l戰鬥","&f&lIron&c&lRush");

    @Setter
    private int votes = 0;
    @Setter
    private boolean active = false;
    private Material displayMaterial;
    private Map<String, String> displayName = new HashMap<>();

    private Scenario(Material material, String displayNameZH, String displayNameEN) {
        this.displayName.put("zh_tw", StringUtil.cc(displayNameZH));
        this.displayName.put("en_us", StringUtil.cc(displayNameEN));
        this.displayMaterial = material;
    }

    public String getDisplayName(Player player) {
        return this.getDisplayName(player.getUniqueId());
    }

    public String getDisplayName(UUID uuid) {
        return this.displayName.get(Mitw.getInstance().getLanguageData().getLang(uuid));
    }

}
