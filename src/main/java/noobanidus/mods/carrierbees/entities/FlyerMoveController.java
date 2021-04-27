package noobanidus.mods.carrierbees.entities;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.MathHelper;

public class FlyerMoveController extends MovementController {
  private final BeehemothEntity entity;

  public FlyerMoveController(BeehemothEntity entity) {
    super(entity);
    this.entity = entity;
  }

  @Override
  public void tick() {
    if (entity.canPassengerSteer()) {
      action = Action.WAIT;
      return;
    }

    if (this.action == MovementController.Action.MOVE_TO) {
      this.action = MovementController.Action.WAIT;
      this.mob.setNoGravity(true);
      double d0 = this.posX - this.mob.getPosX();
      double d1 = this.posY - this.mob.getPosY();
      double d2 = this.posZ - this.mob.getPosZ();
      double d3 = d0 * d0 + d1 * d1 + d2 * d2;
      if (d3 < (double) 2.5000003E-7F) {
        this.mob.setMoveVertical(0.0F);
        this.mob.setMoveForward(0.0F);
        return;
      }

      float f = (float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
      this.mob.rotationYaw = this.limitAngle(this.mob.rotationYaw, f, 90.0F);
      float f1;
      if (this.mob.isOnGround()) {
        f1 = (float) (this.speed * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
      } else {
        f1 = (float) (this.speed * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
      }

      this.mob.setAIMoveSpeed(f1);
      this.mob.setMoveVertical(d1 > 0.0D ? f1 : -f1);
    } else {
      this.mob.setNoGravity(false);
      this.mob.setMoveVertical(0.0F);
      this.mob.setMoveForward(0.0F);
    }
  }
}
