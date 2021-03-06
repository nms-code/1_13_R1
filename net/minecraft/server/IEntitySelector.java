package net.minecraft.server;

import com.google.common.base.Predicates;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public final class IEntitySelector {

    public static final Predicate<Entity> a = Entity::isAlive;
    public static final Predicate<Entity> b = (entity) -> {
        return entity.isAlive() && !entity.isVehicle() && !entity.isPassenger();
    };
    public static final Predicate<Entity> c = (entity) -> {
        return entity instanceof IInventory && entity.isAlive();
    };
    public static final Predicate<Entity> d = (entity) -> {
        return !(entity instanceof EntityHuman) || !((EntityHuman) entity).isSpectator() && !((EntityHuman) entity).u();
    };
    public static final Predicate<Entity> e = (entity) -> {
        return !(entity instanceof EntityHuman) || !((EntityHuman) entity).isSpectator();
    };

    public static Predicate<Entity> a(double d0, double d1, double d2, double d3) {
        double d4 = d3 * d3;

        return (entity) -> {
            return entity != null && entity.d(d0, d1, d2) <= d3;
        };
    }

    public static Predicate<Entity> a(Entity entity) {
        ScoreboardTeamBase scoreboardteambase = entity.be();
        ScoreboardTeamBase.EnumTeamPush scoreboardteambase_enumteampush = scoreboardteambase == null ? ScoreboardTeamBase.EnumTeamPush.ALWAYS : scoreboardteambase.getCollisionRule();

        return (Predicate) (scoreboardteambase_enumteampush == ScoreboardTeamBase.EnumTeamPush.NEVER ? Predicates.alwaysFalse() : IEntitySelector.e.and((entity) -> {
            if (!entity.isCollidable()) {
                return false;
            } else if (entity1.world.isClientSide && (!(entity instanceof EntityHuman) || !((EntityHuman) entity).dn())) {
                return false;
            } else {
                ScoreboardTeamBase scoreboardteambase = entity.be();
                ScoreboardTeamBase.EnumTeamPush scoreboardteambase_enumteampush = scoreboardteambase == null ? ScoreboardTeamBase.EnumTeamPush.ALWAYS : scoreboardteambase.getCollisionRule();

                if (scoreboardteambase_enumteampush == ScoreboardTeamBase.EnumTeamPush.NEVER) {
                    return false;
                } else {
                    boolean flag = scoreboardteambase1 != null && scoreboardteambase1.isAlly(scoreboardteambase);

                    return (scoreboardteambase_enumteampush1 == ScoreboardTeamBase.EnumTeamPush.PUSH_OWN_TEAM || scoreboardteambase_enumteampush == ScoreboardTeamBase.EnumTeamPush.PUSH_OWN_TEAM) && flag ? false : scoreboardteambase_enumteampush1 != ScoreboardTeamBase.EnumTeamPush.PUSH_OTHER_TEAMS && scoreboardteambase_enumteampush != ScoreboardTeamBase.EnumTeamPush.PUSH_OTHER_TEAMS || flag;
                }
            }
        }));
    }

    public static Predicate<Entity> b(Entity entity) {
        return (entity) -> {
            while (true) {
                if (entity.isPassenger()) {
                    entity = entity.getVehicle();
                    if (entity != entity1) {
                        continue;
                    }

                    return false;
                }

                return true;
            }
        };
    }

    public static class EntitySelectorEquipable implements Predicate<Entity> {

        private final ItemStack a;

        public EntitySelectorEquipable(ItemStack itemstack) {
            this.a = itemstack;
        }

        public boolean a(@Nullable Entity entity) {
            if (!entity.isAlive()) {
                return false;
            } else if (!(entity instanceof EntityLiving)) {
                return false;
            } else {
                EntityLiving entityliving = (EntityLiving) entity;

                return !entityliving.getEquipment(EntityInsentient.e(this.a)).isEmpty() ? false : (entityliving instanceof EntityInsentient ? ((EntityInsentient) entityliving).dk() : (entityliving instanceof EntityArmorStand ? true : entityliving instanceof EntityHuman));
            }
        }

        public boolean test(@Nullable Object object) {
            return this.a((Entity) object);
        }
    }
}
