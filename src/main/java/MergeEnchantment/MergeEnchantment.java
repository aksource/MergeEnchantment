package MergeEnchantment;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

@Mod(modid = MergeEnchantment.MOD_ID,
        name = MergeEnchantment.MOD_NAME,
        version = MergeEnchantment.MOD_VERSION,
        dependencies = MergeEnchantment.MOD_DEPENDENCIES,
        useMetadata = true,
        acceptedMinecraftVersions = MergeEnchantment.MOD_MC_VERSION)
public class MergeEnchantment {

    public static final String MOD_ID = "MergeEnchantment";
    public static final String MOD_NAME = "MergeEnchantment";
    public static final String MOD_VERSION = "@VERSION@";
    public static final String MOD_DEPENDENCIES = "required-after:Forge@[12.17.0,)";
    public static final String MOD_MC_VERSION = "[1.9,1.10.99]";

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        GameRegistry.addRecipe(new AddEnchantmentRecipes());
        GameRegistry.addRecipe(new MergeEnchantmentRecipes());
        RecipeSorter.register("MergeEnchantment", MergeEnchantmentRecipes.class, Category.SHAPELESS, "after:FML");
        RecipeSorter.register("AddEnchantment", AddEnchantmentRecipes.class, Category.SHAPELESS, "after:FML");
    }

    static String getUniqueStrings(Object obj) {
        String registryName = "none:dummy";
        if (obj instanceof ItemStack) {
            obj = ((ItemStack) obj).getItem();
        }
        if (obj instanceof Block) {
            registryName = ((Block) obj).getRegistryName().toString();
        }
        if (obj instanceof Item) {
            registryName = ((Item) obj).getRegistryName().toString();
        }
        return registryName;
    }
}