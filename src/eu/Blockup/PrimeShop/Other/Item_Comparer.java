package eu.Blockup.PrimeShop.Other;

import java.util.List;
import java.util.Map;

import org.bukkit.FireworkEffect;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

public class Item_Comparer {

    @SuppressWarnings("deprecation")
    public static boolean do_Items_match(ItemStack itemA, ItemStack itemB,
            boolean checkData, boolean checkAmount, boolean checkEnchantmens,
            boolean checkMeta, boolean checkDurability) {

        if (itemA.getType() != itemB.getType()) {
            return false;
        }

        if (checkData) {
            if (!(Integer.valueOf(itemA.getData().getData()).equals(-1))
                    || (Integer.valueOf(itemB.getData().getData()).equals(-1))) {
                if (!(itemA.getData().getData() == itemB.getData().getData())) {
                    return false;
                }
            }
        }

        if (checkEnchantmens) {
            Map<Enchantment, Integer> map = itemA.getEnchantments();
            for (Enchantment key : map.keySet()) {
                if (!itemB.containsEnchantment(key)) {
                    return false;
                }
            }
            map = itemB.getEnchantments();
            for (Enchantment key : map.keySet()) {
                if (!itemA.containsEnchantment(key)) {
                    return false;
                }
            }
        }

        if (checkAmount) {
            if (itemA.getAmount() != itemB.getAmount())
                return false;
        }

        if (checkMeta) {
            boolean itemA_has_Meta = false;
            boolean itemB_has_Meta = false;

            if (itemA.hasItemMeta()) {
                itemA_has_Meta = true;
            }
            if (itemB.hasItemMeta()) {
                itemB_has_Meta = true;
            }
            if (!(itemA_has_Meta == itemB_has_Meta))
                return false;

            if (itemA_has_Meta) {
                ItemMeta itemMetaA = itemA.getItemMeta();
                ItemMeta itemMetaB = itemA.getItemMeta();
                // PotionMeta
                if ((itemMetaA instanceof PotionMeta) == (itemMetaB instanceof PotionMeta)) {
                    if (itemMetaA instanceof PotionMeta) {
                        PotionMeta potionMetaA = (PotionMeta) itemMetaA;
                        PotionMeta potionMetaB = (PotionMeta) itemMetaB;
                        if (!compareEnchantmeList(potionMetaA.getEnchants(),
                                potionMetaB.getEnchants()))
                            return false;
                        // TODO compare custom effects
                        // potionMetaA.getCustomEffects()
                    }
                }
                // LeatherArmorMeta
                if ((itemMetaA instanceof LeatherArmorMeta) == (itemMetaB instanceof LeatherArmorMeta)) {
                    if (itemMetaA instanceof LeatherArmorMeta) {
                        LeatherArmorMeta leatherMetaA = (LeatherArmorMeta) itemMetaA;
                        LeatherArmorMeta leatherMetaB = (LeatherArmorMeta) itemMetaB;
                        if (!leatherMetaA.getDisplayName().matches(
                                leatherMetaB.getDisplayName()))
                            return false;
                        if (leatherMetaA.getColor() != leatherMetaB.getColor())
                            return false;
                        if (!compareEnchantmeList(leatherMetaA.getEnchants(),
                                leatherMetaB.getEnchants()))
                            return false;
                        // TODO compare custom effects
                    }
                }
                // FireworkMeta
                if ((itemMetaA instanceof FireworkMeta) == (itemMetaB instanceof FireworkMeta)) {
                    if (itemMetaA instanceof FireworkMeta) {
                        FireworkMeta fireworkMetaA = (FireworkMeta) itemMetaA;
                        FireworkMeta fireworkMetaB = (FireworkMeta) itemMetaB;
                        if (!compareEnchantmeList(fireworkMetaA.getEnchants(),
                                fireworkMetaB.getEnchants()))
                            return false;
                        List<FireworkEffect> fireworkEffectListA = fireworkMetaA
                                .getEffects();
                        List<FireworkEffect> fireworkEffectListB = fireworkMetaB
                                .getEffects();
                        for (FireworkEffect effect : fireworkEffectListB) {
                            if (!(fireworkEffectListB.contains(effect)))
                                return false;
                        }
                        for (FireworkEffect effect : fireworkEffectListA) {
                            if (!(fireworkEffectListB.contains(effect)))
                                return false;
                        }
                        // TODO compare custom effects
                    }
                }
                // BookMeta
                if ((itemMetaA instanceof BookMeta) == (itemMetaB instanceof BookMeta)) {
                    if (itemMetaA instanceof BookMeta) {
                        BookMeta bookMetaA = (BookMeta) itemMetaA;
                        BookMeta bookMetaB = (BookMeta) itemMetaB;
                        if (!bookMetaA.getTitle().matches(bookMetaB.getTitle()))
                            return false;
                        if (!bookMetaA.getAuthor().matches(
                                bookMetaB.getAuthor()))
                            return false;
                        // TODO compare pages
                        if (!compareEnchantmeList(bookMetaA.getEnchants(),
                                bookMetaB.getEnchants()))
                            return false;
                    }
                }
            }
        }

        if (checkDurability) {
            boolean durabilitysMatch = false;
            if (itemA.getDurability() == -1
                    || itemA.getDurability() == itemB.getDurability()) {
                durabilitysMatch = true;
            }
            if (!durabilitysMatch)
                return false;

        }

        return true;
    }

    private static boolean compareEnchantmeList(
            Map<Enchantment, Integer> enchantmensA,
            Map<Enchantment, Integer> enchantmensB) {

        Map<Enchantment, Integer> map = enchantmensA;
        for (Enchantment key : map.keySet()) {
            if (!enchantmensB.containsKey(key)) {
                return false;
            }
        }
        map = enchantmensB;
        for (Enchantment key : map.keySet()) {
            if (!enchantmensA.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    // public static boolean do_Items_match(ItemStack itemA, ItemStack itemB) {
    // // if ((itemA.getData().getData()) == (itemB.getData().getData()) &&
    // // ((itemA.getType().equals(itemB.getType())))) {
    // // if ((itemA.getData().getData()) == (itemB.getData().getData()) &&
    // // ((itemA.getType().getId()) == (itemB.getType().getId()))) {
    //
    // int auswertungsverfahren;
    //
    // // // TODO immer auswertverfahren 2 wenn Damagevalue = -1 !!
    // // switch (itemA.getData().getItemType().getId()) {
    // // //case 5: // Wood Plank
    // // case 125: // Double Wood Slab
    // // case 126: // Wood Slab
    // // case 17: // Wood
    // // auswertungsverfahren = 1;
    // // break;
    // //
    // // case 57: // Dia Block
    // // case 41: // Gold Block
    // // case 22: // Lapis Lazuli Block
    // // case 42: // Iron Block
    // // case 88: // Snow Block
    // // case 133: // Emerald Block
    // // case 138: // Beacon Block
    // // case 152: // Redstone Block
    // // case 165: // Slime Block
    // // case 173: // Block of Coal
    // // case 103: // Melon Block
    // // case 170: // Hay Bale
    // // case 46: // TNT
    // // case 145: // Amboss
    // // auswertungsverfahren = 2;
    // // break;
    // //
    // // default:
    // // auswertungsverfahren = 0;
    // // break;
    // // }
    // //
    // // // Wood Plank Sonderfall data: -1
    // // if (itemB.getData().getItemType().getId() == 5 &&
    // //
    // (String.valueOf(Integer.valueOf(itemB.getData().getData())).equals("-1")))
    // // {
    // // auswertungsverfahren = 2;
    // // }
    // // if (itemA.getData().getItemType().getId() == 5 &&
    // //
    // (String.valueOf(Integer.valueOf(itemA.getData().getData())).equals("-1")))
    // // {
    // // auswertungsverfahren = 2;
    // // }
    //
    // // /////////////////////////////
    // if ((Integer.valueOf(itemA.getData().getData()).equals(-1)) ||
    // (Integer.valueOf(itemB.getData().getData()).equals(-1))) {
    // auswertungsverfahren = 2;
    //
    // } else {
    // auswertungsverfahren = 0;
    // }
    //
    // // Wood Plank Sonderfall data: -1
    // if (itemB.getData().getItemType().getId() == 5 &&
    // (String.valueOf(Integer.valueOf(itemB.getData().getData())).equals("-1")))
    // {
    // auswertungsverfahren = 1;
    // }
    // if (itemA.getData().getItemType().getId() == 5 &&
    // (String.valueOf(Integer.valueOf(itemA.getData().getData())).equals("-1")))
    // {
    // auswertungsverfahren = 2;
    // }
    // // ////////////////////////////
    //
    // if (auswertungsverfahren == 2) {
    // itemB = new ItemStack(itemB.getData().getItemType());
    // itemA = new ItemStack(itemA.getData().getItemType());
    // auswertungsverfahren = 1;
    // }
    // if (auswertungsverfahren == 1) {
    // if (itemA.getData().getItemType().getId() ==
    // (itemB.getData().getItemType().getId())) {
    // return true;
    // } else {
    // return false;
    // }
    // }
    //
    // if (auswertungsverfahren == 0) {
    // // infinity loop if (itemA.getData().getItemType().getId() ==
    // // itemB.getData().getItemType().getId()) {
    // if (itemA.getData().getData() == itemB.getData().getData() &&
    // itemA.getType().getId() == itemB.getType().getId()) {
    //
    // return true;
    // } else {
    // return false;
    // }
    // }
    //
    // return false;
    // }

    // TODO Remove unused code found by UCDetector
    // public static boolean do_Items_match_typ_data_enchantments_meta( // TODO
    // replace with new one
    // ItemStack itemA, ItemStack itemB) {
    // itemB.setAmount(itemA.getAmount());
    // if (itemA.isSimilar(itemB)) {
    // return false;
    // }
    // return true;
    // }
}
