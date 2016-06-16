package MergeEnchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import java.util.Map;

public class AddEnchantmentRecipes implements IRecipe {

    private ItemStack output = null;

    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        int toolflag = 0;
        ItemStack tool = null;
        int bookflag = 0;
        ItemStack book = null;
        boolean flag = false;
//		int EnchInt1;
        Enchantment ench1;
//		int EnchInt2;
        Enchantment ench2;
        ItemStack craftitem;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            craftitem = inv.getStackInSlot(i);
            if (craftitem != null) {
                if (craftitem.getItem().getItemEnchantability() > 0/*craftitem.getItem().isRepairable()*/) {
                    toolflag++;
                    tool = craftitem.copy();
                } else if (craftitem.getItem() instanceof ItemEnchantedBook) {
                    bookflag++;
                    book = craftitem.copy();
                } else {
                    return false;
                }
            }
        }
        if (toolflag > 0 && toolflag < 2 && bookflag > 0 && bookflag < 2) {
            Map<Integer, Integer> toolenchlist = EnchantmentHelper.getEnchantments(tool);
            Map<Integer, Integer> bookenchlist = EnchantmentHelper.getEnchantments(book);
            for (int EnchInt1 : bookenchlist.keySet()) {
                ench1 = Enchantment.getEnchantmentById(EnchInt1);
                int var3 = toolenchlist.containsKey(EnchInt1) ? toolenchlist.get(EnchInt1) : 0;
                int var4 = bookenchlist.get(EnchInt1);
                int Max;
                if (var3 == var4) {
                    Max = var4;
                } else {
                    Max = Math.max(var4, var3);
                }

                var4 = Max;
                flag = ench1.canApplyAtEnchantingTable(tool);
                for (int EnchInt2 : toolenchlist.keySet()) {
                    ench2 = Enchantment.getEnchantmentById(EnchInt2);
                    flag = ench1.canApplyTogether(ench2);
                }

                toolenchlist.put(EnchInt1, var4);
            }
            this.output = tool;
            int var5 = (this.output.getItemDamage() - this.output.getMaxDamage() / 20 < 1) ? 1 : this.output
                    .getItemDamage() - this.output.getMaxDamage() / 20;
            this.output.setItemDamage(var5);
            EnchantmentHelper.setEnchantments(toolenchlist, this.output);
            return flag;
        } else
            return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
        return this.output.copy();
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting p_179532_1_) {
        ItemStack[] aitemstack = new ItemStack[p_179532_1_.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i) {
            ItemStack itemstack = p_179532_1_.getStackInSlot(i);
            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return aitemstack;
    }
}
