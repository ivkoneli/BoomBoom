package com.ivkoneli.boomboom;

import java.util.EnumSet;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Makes the mob behave like a primed creeper, following vanilla swell rules:
 * a visible player within 3 blocks primes it; the fuse keeps burning while a
 * player is within 7 blocks and in line of sight, and ticks back down when
 * the player retreats or breaks line of sight. Explodes at a full 30-tick fuse.
 */
public class ExplodeLikeCreeperGoal extends Goal {
    private static final double PRIME_RANGE = 3.0;
    private static final double SWELL_RANGE = 7.0;
    private static final int FUSE_TICKS = 30;
    private static final float EXPLOSION_RADIUS = 3.0F;

    private final Mob mob;
    private int fuse;

    public ExplodeLikeCreeperGoal(Mob mob) {
        this.mob = mob;
        // Claim movement so the mob stands still while fusing instead of wandering off
        setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return fuse > 0 || findVisiblePlayer(PRIME_RANGE) != null;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        mob.getNavigation().stop();

        if (findVisiblePlayer(SWELL_RANGE) != null) {
            if (fuse == 0) {
                mob.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
                mob.setGlowingTag(true);
            }
            fuse++;
        } else if (fuse > 0) {
            fuse--;
            if (fuse == 0) {
                mob.setGlowingTag(false);
            }
        }

        if (fuse > 0 && mob.level() instanceof ServerLevel level) {
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

    @Override
    public void stop() {
        fuse = 0;
        mob.setGlowingTag(false);
    }

    // Creative and spectator players don't set mobs off, same as real creepers
    private Player findVisiblePlayer(double range) {
        Player player = mob.level().getNearestPlayer(mob, range);
        return (player != null && player.isAlive() && !player.isCreative() && !player.isSpectator()
                && mob.getSensing().hasLineOfSight(player)) ? player : null;
    }
}
