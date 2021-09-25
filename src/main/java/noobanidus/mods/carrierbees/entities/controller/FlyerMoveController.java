package noobanidus.mods.carrierbees.entities.controller;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.MathHelper;
import noobanidus.mods.carrierbees.entities.BeehemothEntity;

import net.minecraft.entity.ai.controller.MovementController.Action;

public class FlyerMoveController extends MovementController {
  private final BeehemothEntity entity;

  public FlyerMoveController(BeehemothEntity entity) {
    super(entity);
    this.entity = entity;
  }

  @Override
  public void tick() {
    if (entity.isControlledByLocalInstance()) {
      operation = Action.WAIT;
      return;
    }

    if (this.operation == MovementController.Action.MOVE_TO) {
      this.operation = MovementController.Action.WAIT;
      this.mob.setNoGravity(true);
      double d0 = this.wantedX - this.mob.getX();
      double d1 = this.wantedY - this.mob.getY();
      double d2 = this.wantedZ - this.mob.getZ();
      double d3 = d0 * d0 + d1 * d1 + d2 * d2;
      if (d3 < (double) 2.5000003E-7F) {
        this.mob.setYya(0.0F);
        this.mob.setZza(0.0F);
        return;
      }

      float f = (float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
      this.mob.yRot = this.rotlerp(this.mob.yRot, f, 90.0F);
      float f1;
      if (this.mob.isOnGround()) {
        f1 = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
      } else {
        f1 = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
      }

      this.mob.setSpeed(f1);
      this.mob.setYya(d1 > 0.0D ? f1 : -f1);
    } else {
      this.mob.setNoGravity(false);
      this.mob.setYya(0.0F);
      this.mob.setZza(0.0F);
    }
  }
}
