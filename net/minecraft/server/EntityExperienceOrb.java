package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
// CraftBukkit end

public class EntityExperienceOrb extends Entity {

    public int a;
    public int b;
    public int c;
    private int d = 5;
    public int value;
    private EntityHuman targetPlayer;
    private int targetTime;

    public EntityExperienceOrb(World world, double d0, double d1, double d2, int i) {
        super(EntityTypes.EXPERIENCE_ORB, world);
        this.setSize(0.5F, 0.5F);
        this.setPosition(d0, d1, d2);
        this.yaw = (float) (Math.random() * 360.0D);
        this.motX = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.motY = (double) ((float) (Math.random() * 0.2D) * 2.0F);
        this.motZ = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.value = i;
    }

    public EntityExperienceOrb(World world) {
        super(EntityTypes.EXPERIENCE_ORB, world);
        this.setSize(0.25F, 0.25F);
    }

    protected boolean playStepSound() {
        return false;
    }

    protected void x_() {}

    public void tick() {
        super.tick();
        EntityHuman prevTarget = this.targetPlayer;// CraftBukkit - store old target
        if (this.c > 0) {
            --this.c;
        }

        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        if (this.a(TagsFluid.a)) {
            this.k();
        } else if (!this.isNoGravity()) {
            this.motY -= 0.029999999329447746D;
        }

        if (this.world.b(new BlockPosition(this)).a(TagsFluid.b)) {
            this.motY = 0.20000000298023224D;
            this.motX = (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            this.motZ = (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            this.a(SoundEffects.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
        }

        this.i(this.locX, (this.getBoundingBox().b + this.getBoundingBox().e) / 2.0D, this.locZ);
        double d0 = 8.0D;

        if (this.targetTime < this.a - 20 + this.getId() % 100) {
            if (this.targetPlayer == null || this.targetPlayer.h(this) > 64.0D) {
                this.targetPlayer = this.world.findNearbyPlayer(this, 8.0D);
            }

            this.targetTime = this.a;
        }

        if (this.targetPlayer != null && this.targetPlayer.isSpectator()) {
            this.targetPlayer = null;
        }

        if (this.targetPlayer != null) {
            // CraftBukkit start
            boolean cancelled = false;
            if (this.targetPlayer != prevTarget) {
                EntityTargetLivingEntityEvent event = CraftEventFactory.callEntityTargetLivingEvent(this, targetPlayer, EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
                EntityLiving target = event.getTarget() == null ? null : ((org.bukkit.craftbukkit.entity.CraftLivingEntity) event.getTarget()).getHandle();
                targetPlayer = target instanceof EntityHuman ? (EntityHuman) target : null;
                cancelled = event.isCancelled();
            }

            if (!cancelled && targetPlayer != null) {
            double d1 = (this.targetPlayer.locX - this.locX) / 8.0D;
            double d2 = (this.targetPlayer.locY + (double) this.targetPlayer.getHeadHeight() / 2.0D - this.locY) / 8.0D;
            double d3 = (this.targetPlayer.locZ - this.locZ) / 8.0D;
            double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            double d5 = 1.0D - d4;

            if (d5 > 0.0D) {
                d5 *= d5;
                this.motX += d1 / d4 * d5 * 0.1D;
                this.motY += d2 / d4 * d5 * 0.1D;
                this.motZ += d3 / d4 * d5 * 0.1D;
            }
            }
            // CraftBukkit end
        }

        this.move(EnumMoveType.SELF, this.motX, this.motY, this.motZ);
        float f = 0.98F;

        if (this.onGround) {
            f = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(this.getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().n() * 0.98F;
        }

        this.motX *= (double) f;
        this.motY *= 0.9800000190734863D;
        this.motZ *= (double) f;
        if (this.onGround) {
            this.motY *= -0.8999999761581421D;
        }

        ++this.a;
        ++this.b;
        if (this.b >= 6000) {
            this.die();
        }

    }

    private void k() {
        this.motY += 5.000000237487257E-4D;
        this.motY = Math.min(this.motY, 0.05999999865889549D);
        this.motX *= 0.9900000095367432D;
        this.motZ *= 0.9900000095367432D;
    }

    protected void au() {}

    protected void burn(int i) {
        this.damageEntity(DamageSource.FIRE, (float) i);
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            this.aA();
            this.d = (int) ((float) this.d - f);
            if (this.d <= 0) {
                this.die();
            }

            return false;
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("Health", (short) this.d);
        nbttagcompound.setShort("Age", (short) this.b);
        nbttagcompound.setShort("Value", (short) this.value);
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.d = nbttagcompound.getShort("Health");
        this.b = nbttagcompound.getShort("Age");
        this.value = nbttagcompound.getShort("Value");
    }

    public void d(EntityHuman entityhuman) {
        if (!this.world.isClientSide) {
            if (this.c == 0 && entityhuman.bJ == 0) {
                entityhuman.bJ = 2;
                entityhuman.receive(this, 1);
                ItemStack itemstack = EnchantmentManager.b(Enchantments.G, (EntityLiving) entityhuman);

                if (!itemstack.isEmpty() && itemstack.f()) {
                    int i = Math.min(this.c(this.value), itemstack.getDamage());

                    // CraftBukkit start
                    org.bukkit.event.player.PlayerItemMendEvent event = CraftEventFactory.callPlayerItemMendEvent(entityhuman, this, itemstack, i);
                    i = event.getRepairAmount();
                    if (!event.isCancelled()) {
                        this.value -= this.b(i);
                        itemstack.setDamage(itemstack.getDamage() - i);
                    }
                    // CraftBukkit end
                }

                if (this.value > 0) {
                    entityhuman.giveExp(CraftEventFactory.callPlayerExpChangeEvent(entityhuman, this.value).getAmount()); // CraftBukkit - this.value -> event.getAmount()
                }

                this.die();
            }

        }
    }

    private int b(int i) {
        return i / 2;
    }

    private int c(int i) {
        return i * 2;
    }

    public int f() {
        return this.value;
    }

    public static int getOrbValue(int i) {
        // CraftBukkit start
        if (i > 162670129) return i - 100000;
        if (i > 81335063) return 81335063;
        if (i > 40667527) return 40667527;
        if (i > 20333759) return 20333759;
        if (i > 10166857) return 10166857;
        if (i > 5083423) return 5083423;
        if (i > 2541701) return 2541701;
        if (i > 1270849) return 1270849;
        if (i > 635413) return 635413;
        if (i > 317701) return 317701;
        if (i > 158849) return 158849;
        if (i > 79423) return 79423;
        if (i > 39709) return 39709;
        if (i > 19853) return 19853;
        if (i > 9923) return 9923;
        if (i > 4957) return 4957;
        // CraftBukkit end
        return i >= 2477 ? 2477 : (i >= 1237 ? 1237 : (i >= 617 ? 617 : (i >= 307 ? 307 : (i >= 149 ? 149 : (i >= 73 ? 73 : (i >= 37 ? 37 : (i >= 17 ? 17 : (i >= 7 ? 7 : (i >= 3 ? 3 : 1)))))))));
    }

    public boolean bk() {
        return false;
    }
}
