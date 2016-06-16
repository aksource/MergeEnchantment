package MergeEnchantment;

import com.google.common.base.Optional;
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
    public static final String MOD_DEPENDENCIES = "required-after:Forge@[11.14.0.1237,)";
    public static final String MOD_MC_VERSION = "[1.8,1.8.9]";

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        GameRegistry.addRecipe(new AddEnchantmentRecipes());
        GameRegistry.addRecipe(new MergeEnchantmentRecipes());
        RecipeSorter.register("MergeEnchantment", MergeEnchantmentRecipes.class, Category.SHAPELESS, "after:FML");
        RecipeSorter.register("AddEnchantment", AddEnchantmentRecipes.class, Category.SHAPELESS, "after:FML");
    }

    public static String getUniqueStrings(Object obj) {
        GameRegistry.UniqueIdentifier uId = null;
        if (obj instanceof ItemStack) {
            obj = ((ItemStack) obj).getItem();
        }
        if (obj instanceof Block) {
            uId = GameRegistry.findUniqueIdentifierFor((Block) obj);
        }
        if (obj instanceof Item) {
            uId = GameRegistry.findUniqueIdentifierFor((Item) obj);
        }
        return Optional.fromNullable(uId).or(new GameRegistry.UniqueIdentifier("none:dummy")).toString();
    }
}