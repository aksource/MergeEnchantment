package MergeEnchantment;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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

    public static final String MOD_ID = "mergeenchantment";
    public static final String MOD_NAME = "MergeEnchantment";
    public static final String MOD_VERSION = "@VERSION@";
    public static final String MOD_DEPENDENCIES = "required-after:forge@[13.20.0,)";
    public static final String MOD_MC_VERSION = "[1.11,1.99.99]";

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        GameRegistry.addRecipe(new AddEnchantmentRecipes());
        GameRegistry.addRecipe(new MergeEnchantmentRecipes());
        RecipeSorter.register("MergeEnchantment", MergeEnchantmentRecipes.class, Category.SHAPELESS, "after:FML");
        RecipeSorter.register("AddEnchantment", AddEnchantmentRecipes.class, Category.SHAPELESS, "after:FML");
    }

    static String getUniqueStrings(Object obj) {
        if (obj instanceof ItemStack) {
            obj = ((ItemStack) obj).getItem();
        }
        ResourceLocation resourceLocation = new ResourceLocation("none", "dummy");
        if (obj instanceof Block && ((Block) obj).getRegistryName() != null) {
            resourceLocation = ((Block) obj).getRegistryName();
        }
        if (obj instanceof Item && ((Item) obj).getRegistryName() != null) {
            resourceLocation = ((Item) obj).getRegistryName();
        }
        return resourceLocation.toString();
    }
}