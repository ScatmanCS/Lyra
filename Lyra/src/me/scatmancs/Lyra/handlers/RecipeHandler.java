package me.scatmancs.Lyra.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import me.scatmancs.Lyra.utils.Utilities;

public class RecipeHandler {
    private static RecipeHandler instance;

    public void disableRecipes() {
        Bukkit.getServer().clearRecipes();
    }

    public void enableRecipes() {
        ShapedRecipe cmelon = new ShapedRecipe(new ItemStack(Material.SPECKLED_MELON, 1));

        cmelon.shape(new String[] { "AAA", "CBA", "AAA" })
              .setIngredient('B', Material.MELON)
              .setIngredient('C', Material.GOLD_NUGGET);
        Bukkit.getServer().addRecipe(cmelon);
    }

    public static RecipeHandler getInstance() {
        return instance;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
