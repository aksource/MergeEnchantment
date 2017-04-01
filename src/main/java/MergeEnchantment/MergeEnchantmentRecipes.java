package MergeEnchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;

public class MergeEnchantmentRecipes implements IRecipe {
    private Enchantment[] SameEnch = new Enchantment[256];
    private int SameEnchindex = 0;
    private ItemStack output = ItemStack.EMPTY;
    private ItemStack items[] = new ItemStack[2];


    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        int toolflag = 0;
        boolean flag = false;
        int bookflag = 0;
        for (int i = 0; i < 2; i++) {
            items[i] = null;
        }
        ItemStack craftitem;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            craftitem = inv.getStackInSlot(i);
            if (!craftitem.isEmpty()) {
                if (craftitem.getItem().getItemEnchantability() > 0
                        && !MergeEnchantment.getUniqueStrings(craftitem).equals(MergeEnchantment.getUniqueStrings(Items.BOOK))) {
                    if (items[0] == null) {
                        toolflag++;
                        items[0] = craftitem.copy();
                    } else if (MergeEnchantment.getUniqueStrings(items[0]).equals(MergeEnchantment.getUniqueStrings(craftitem))
                            && toolflag == 1) {
                        items[1] = craftitem.copy();
                        toolflag++;
                    } else {
                        return false;
                    }
                } else if (MergeEnchantment.getUniqueStrings(craftitem).equals(MergeEnchantment.getUniqueStrings(Items.BOOK))
                        && bookflag == 0) {
                    bookflag++;
                } else {
                    return false;
                }
            }
        }
        if (items[0] != null && items[1] != null && toolflag > 0 && toolflag < 3 && bookflag > 0)
            flag = true;
        return flag;
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inventorycrafting) {
        ArrayList<EnchantmentData> enchantmentDatas = new ArrayList<>();
        Enchantment.REGISTRY.forEach(enchantment -> {
            int lv = getMaxEnchantmentLevel(enchantment, items);
            if (lv > 0) {
                enchantmentDatas.add(new EnchantmentData(enchantment, lv));
            }
        });

        for (Iterator<EnchantmentData> it = enchantmentDatas.iterator(); it.hasNext(); ) {
            EnchantmentData data = it.next();
            for (EnchantmentData data2 : enchantmentDatas) {
                if (!data.enchantmentobj.func_191560_c/*canApplyTogether*/(data2.enchantmentobj)
                        && data.enchantmentLevel < data2.enchantmentLevel) {
                    it.remove();
                    break;
                }
            }
        }

        output = new ItemStack(items[0].getItem(), 1, items[0].getItemDamage());
        SameEnchindex = 0;
        for (EnchantmentData data : enchantmentDatas) {
            if (SameEnch[SameEnchindex] != null && SameEnch[SameEnchindex].equals(data.enchantmentobj)) {
                output.addEnchantment(data.enchantmentobj, data.enchantmentLevel + 1);
                SameEnchindex++;
            } else {
                output.addEnchantment(data.enchantmentobj, data.enchantmentLevel);
            }
        }
        boolean flag;
        if (output.getItem().isDamageable() && items[0].getCount() == 1 && items[1].getCount() == 1) {
            int a1 = output.getMaxDamage() - items[0].getItemDamage();
            int a2 = output.getMaxDamage() - items[1].getItemDamage();
            int a3 = output.getMaxDamage() - (a1 + a2);
            if (a3 < 0) {
                a3 = 0;
            }
            output.setItemDamage(a3);
        }
        SameEnchindex = 0;
        return output;
    }

    private int getMaxEnchantmentLevel(Enchantment enchantment, ItemStack itemStacks[]) {
        int j = 0;
        int intArray[] = new int[]{0, 0};
        int k = itemStacks.length;
        for (int l = 0; l < k; l++) {
            ItemStack itemstack = itemStacks[l];
            int i1 = EnchantmentHelper.getEnchantmentLevel(enchantment, itemstack);
            intArray[l] = (i1 > 0) ? i1 : 0;
            if (i1 > j) {
                j = i1;
            }
        }
        if (intArray[0] == intArray[1] && intArray[0] != 0) {
            SameEnch[SameEnchindex] = enchantment;
            SameEnchindex++;
        }
        return j;
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
