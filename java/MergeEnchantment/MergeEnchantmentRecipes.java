package MergeEnchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;

public class MergeEnchantmentRecipes implements IRecipe {
    private Enchantment[] SameEnch = new Enchantment[256];
    private int SameEnchindex = 0;
    private ItemStack output = null;
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
            if (craftitem != null) {
                if (craftitem.getItem().getItemEnchantability() > 0/*craftitem.getItem().isRepairable()*/ && !MergeEnchantment.getUniqueStrings(craftitem).equals(MergeEnchantment.getUniqueStrings(Items.book))) {
                    if (items[0] == null) {
                        toolflag++;
                        items[0] = craftitem.copy();
                    } else if (MergeEnchantment.getUniqueStrings(items[0]).equals(MergeEnchantment.getUniqueStrings(craftitem)) && toolflag == 1) {
                        items[1] = craftitem.copy();
                        toolflag++;
                    } else {
                        return false;
                    }
                } else if (MergeEnchantment.getUniqueStrings(craftitem).equals(MergeEnchantment.getUniqueStrings(Items.book)) && bookflag == 0) {
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
    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
        ArrayList<EnchantmentData> alist = new ArrayList<EnchantmentData>();
        for (int i = 0; i < 256; i++) {
            int lv = getMaxEnchantmentLevel(i, items);
            if (lv > 0) {
                alist.add(new EnchantmentData(Enchantment.getEnchantmentById(i), lv));
            }
        }

        for (Iterator<EnchantmentData> it = alist.iterator(); it.hasNext(); ) {
            EnchantmentData data = it.next();
            for (EnchantmentData data2 : alist) {
                if (!data.enchantmentobj.canApplyTogether(data2.enchantmentobj)
                        && data.enchantmentLevel < data2.enchantmentLevel) {
                    it.remove();
                    break;
                }
            }
        }

        output = new ItemStack(items[0].getItem(), 1, items[0].getItemDamage());
        SameEnchindex = 0;
        for (EnchantmentData data : alist) {
            if (SameEnch[SameEnchindex] != null && SameEnch[SameEnchindex].equals(data.enchantmentobj)) {
                output.addEnchantment(data.enchantmentobj, data.enchantmentLevel + 1);
                SameEnchindex++;
            } else {
                output.addEnchantment(data.enchantmentobj, data.enchantmentLevel);
            }
        }
        boolean flag;
        if (output.getItem().isDamageable() && items[0].stackSize == 1 && items[1].stackSize == 1) {
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

    private int getMaxEnchantmentLevel(int i, ItemStack aitemstack[]) {
        int j = 0;
        int intarray[] = new int[]{0, 0};
        int k = aitemstack.length;
        for (int l = 0; l < k; l++) {
            ItemStack itemstack = aitemstack[l];
            int i1 = EnchantmentHelper.getEnchantmentLevel(i, itemstack);
            intarray[l] = (i1 > 0) ? i1 : 0;
            if (i1 > j) {
                j = i1;
            }
        }
        if (intarray[0] == intarray[1] && intarray[0] != 0) {
            SameEnch[SameEnchindex] = Enchantment.getEnchantmentById(i);
            SameEnchindex++;
//			System.out.println("Same Lv");
        }
        return j;
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
