package com.ivkoneli.boomboom;

import java.util.EnumSet;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Makes the mob behave like a primed creeper: when a player gets close it
 * hisses, fuses for ~1.5s and explodes. Backing away past the abort range
 * defuses it, like a real creeper.
 */
public class ExplodeLikeCreeperGoal extends Goal {
    private static final double TRIGGER_RANGE = 3.0;
    private static final double ABORT_RANGE = 7.0;
    private static final int FUSE_TICKS = 30;
    private static final float EXPLOSION_RADIUS = 3.0F;

    private final PathfinderMob mob;
    private int fuse;

    public ExplodeLikeCreeperGoal(PathfinderMob mob) {
        this.mob = mob;
        // Claim movement so the mob stands still while fusing instead of wandering off
        setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return findThreateningPlayer(TRIGGER_RANGE) != null;
    }

    @Override
    public boolean canContinueToUse() {
        return findThreateningPlayer(ABORT_RANGE) != null;
    }

    @Override
    public void start() {
        fuse = 0;
        mob.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        mob.getNavigation().stop();
        fuse++;

        if (mob.level() instanceof ServerLevel level) {
            if (fuse % 5 == 0) {
                level.sendParticles(ParticleTypes.SMOKE,
                        mob.getX(), mob.getY() + mob.getBbHeight(), mob.getZ(),
                        2, 0.1, 0.1, 0.1, 0.0);
            }
            if (fuse >= FUSE_TICKS) {
                level.explode(mob, mob.getX(), mob.getY(), mob.getZ(),
                        EXPLOSION_RADIUS, Level.ExplosionInteraction.MOB);
                mob.discard();
            }
        }
    }

    // Spectators can't trigger the fuse; creative players can, so testing is easy
    private Player findThreateningPlayer(double range) {
        Player player = mob.level().getNearestPlayer(mob, range);
        return (player != null && player.isAlive() && !player.isSpectator()) ? player : null;
    }
}
