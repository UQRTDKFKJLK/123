package committee.nova.snowwaifu.common.entity.ai;

import committee.nova.snowwaifu.common.entity.api.TamableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class OwnerHurtByTargetGoal extends TargetGoal {
    private final TamableMob tamed;
    private LivingEntity ownerLastHurtBy;
    private int timestamp;

    public OwnerHurtByTargetGoal(TamableMob tamed) {
        super(tamed, false);
        this.tamed = tamed;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    public boolean canUse() {
        if (this.tamed.isTame()) {
            Entity e = this.tamed.getOwner();
            if (!(e instanceof LivingEntity livingEntity)) {
                return false;
            } else {
                this.ownerLastHurtBy = livingEntity.getLastHurtByMob();
                int i = livingEntity.getLastHurtByMobTimestamp();
                return i != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT) && this.tamed.wantsToAttack(this.ownerLastHurtBy, livingEntity);
            }
        } else {
            return false;
        }
    }

    public void start() {
        this.mob.setTarget(this.ownerLastHurtBy);
        Entity e = this.tamed.getOwner();
        if (e instanceof LivingEntity l) {
            this.timestamp = l.getLastHurtByMobTimestamp();
        }

        super.start();
    }
}