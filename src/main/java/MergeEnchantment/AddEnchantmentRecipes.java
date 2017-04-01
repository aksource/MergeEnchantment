package MergeEnchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Map;

public class AddEnchantmentRecipes implements IRecipe {

    private ItemStack output = ItemStack.EMPTY;

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World world) {
        int toolFlag = 0;
        ItemStack tool = null;
        int bookFlag = 0;
        ItemStack book = null;
        boolean flag = false;
        ItemStack craftItem;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            craftItem = inv.getStackInSlot(i);
            if (!craftItem.isEmpty()) {
                if (craftItem.getItem().getItemEnchantability() > 0) {
                    toolFlag++;
                    tool = craftItem.copy();
                } else if (craftItem.getItem() instanceof ItemEnchantedBook) {
                    bookFlag++;
                    book = craftItem.copy();
                } else {
                    return false;
                }
            }
        }
        if (toolFlag > 0 && toolFlag < 2 && bookFlag > 0 && bookFlag < 2) {
            Map<Enchantment, Integer> toolenchlist = EnchantmentHelper.getEnchantments(tool);
            Map<Enchantment, Integer> bookenchlist = EnchantmentHelper.getEnchantments(book);
            for (Enchantment ench1 : bookenchlist.keySet()) {
                int var3 = toolenchlist.containsKey(ench1) ? toolenchlist.get(ench1) : 0;
                int var4 = bookenchlist.get(ench1);
                int Max;
                if (var3 == var4) {
                    Max = var4;
                } else {
                    Max = Math.max(var4, var3);
                }

                var4 = Max;
                flag = ench1.canApplyAtEnchantingTable(tool);
                for (Enchantment ench2 : toolenchlist.keySet()) {
                    flag = ench1.func_191560_c/*canApplyTogether*/(ench2);
                }

                toolenchlist.put(ench1, var4);
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
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inventorycrafting) {
        return this.output.copy();
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inventoryCrafting) {
        NonNullList<ItemStack> nonNullList = NonNullList.withSize(inventoryCrafting.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonNullList.size(); ++i) {
            ItemStack itemstack = inventoryCrafting.getStackInSlot(i);
            if (itemstack.getItem().hasContainerItem(itemstack)) {
                nonNullList.set(i, itemstack.getItem().getContainerItem(itemstack));
            }
        }

        return nonNullList;
    }
}
