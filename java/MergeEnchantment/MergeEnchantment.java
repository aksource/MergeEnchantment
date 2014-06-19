package MergeEnchantment;

import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="MergeEnchantment", name="MergeEnchantment", version="@VERSION@",dependencies="required-after:FML", useMetadata = true)
public class MergeEnchantment
{
	@Mod.Instance("MergeEnchantment")
	public static MergeEnchantment instance;
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		GameRegistry.addRecipe(new AddEnchantmentRecipes());
		GameRegistry.addRecipe(new MergeEnchantmentRecipes());
		RecipeSorter.register("MergeEnchantment", MergeEnchantmentRecipes.class, Category.SHAPELESS, "after:FML");
		RecipeSorter.register("AddEnchantment", AddEnchantmentRecipes.class, Category.SHAPELESS, "after:FML");	
	}
}