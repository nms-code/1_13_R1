package net.minecraft.server;

import javax.annotation.Nullable;

public class ItemWrittenBook extends Item {

    public ItemWrittenBook(Item.Info item_info) {
        super(item_info);
    }

    public static boolean b(@Nullable NBTTagCompound nbttagcompound) {
        if (!ItemBookAndQuill.b(nbttagcompound)) {
            return false;
        } else if (!nbttagcompound.hasKeyOfType("title", 8)) {
            return false;
        } else {
            String s = nbttagcompound.getString("title");

            return s.length() > 32 ? false : nbttagcompound.hasKeyOfType("author", 8);
        }
    }

    public static int e(ItemStack itemstack) {
        return itemstack.getTag().getInt("generation");
    }

    public IChatBaseComponent i(ItemStack itemstack) {
        if (itemstack.hasTag()) {
            NBTTagCompound nbttagcompound = itemstack.getTag();
            String s = nbttagcompound.getString("title");

            if (!UtilColor.b(s)) {
                return new ChatComponentText(s);
            }
        }

        return super.i(itemstack);
    }

    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (!world.isClientSide) {
            this.a(itemstack, entityhuman);
        }

        entityhuman.a(itemstack, enumhand);
        entityhuman.b(StatisticList.ITEM_USED.b(this));
        return new InteractionResultWrapper(EnumInteractionResult.SUCCESS, itemstack);
    }

    private void a(ItemStack itemstack, EntityHuman entityhuman) {
        NBTTagCompound nbttagcompound = itemstack.getTag();

        if (nbttagcompound != null && !nbttagcompound.getBoolean("resolved")) {
            nbttagcompound.setBoolean("resolved", true);
            if (b(nbttagcompound)) {
                NBTTagList nbttaglist = nbttagcompound.getList("pages", 8);

                for (int i = 0; i < nbttaglist.size(); ++i) {
                    String s = nbttaglist.getString(i);

                    Object object;

                    // CraftBukkit start
                    // Some commands use the worldserver variable but we leave it full of null values,
                    // so we must temporarily populate it with the world of the commandsender
                    WorldServer[] prev = MinecraftServer.getServer().worldServer;
                    MinecraftServer server = MinecraftServer.getServer();
                    server.worldServer = new WorldServer[server.worlds.size()];
                    server.worldServer[0] = (WorldServer) entityhuman.getWorld();
                    int bpos = 0;
                    for (int pos = 1; pos < server.worldServer.length; pos++) {
                        WorldServer world = server.worlds.get(bpos++);
                        if (server.worldServer[0] == world) {
                            pos--;
                            continue;
                        }
                        server.worldServer[pos] = world;
                    }
                    // CraftBukkit end
                    try {
                        IChatBaseComponent ichatbasecomponent = IChatBaseComponent.ChatSerializer.b(s);

                        object = ChatComponentUtils.filterForDisplay(entityhuman.getCommandListener(), ichatbasecomponent, entityhuman);
                    } catch (Exception exception) {
                        object = new ChatComponentText(s);
                    }
                    finally { MinecraftServer.getServer().worldServer = prev; } // CraftBukkit

                    nbttaglist.set(i, (NBTBase) (new NBTTagString(IChatBaseComponent.ChatSerializer.a((IChatBaseComponent) object))));
                }

                nbttagcompound.set("pages", nbttaglist);
                if (entityhuman instanceof EntityPlayer && entityhuman.getItemInMainHand() == itemstack) {
                    Slot slot = entityhuman.activeContainer.getSlot(entityhuman.inventory, entityhuman.inventory.itemInHandIndex);

                    ((EntityPlayer) entityhuman).playerConnection.sendPacket(new PacketPlayOutSetSlot(0, slot.rawSlotIndex, itemstack));
                }

            }
        }
    }
}
