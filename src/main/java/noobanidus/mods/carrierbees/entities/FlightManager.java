package noobanidus.mods.carrierbees.entities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;

public class FlightManager {
  private BeehemothEntity dragon;
  private Vector3d target;
  private Vector3d startAttackVec;
  private Vector3d startPreyVec;
  private boolean hasStartedToScorch = false;
  private LivingEntity prevAttackTarget = null;

  public FlightManager(BeehemothEntity dragon) {
    this.dragon = dragon;
  }

  public static float approach(float number, float max, float min) {
    min = Math.abs(min);
    return number < max ? MathHelper.clamp(number + min, number, max) : MathHelper.clamp(number - min, max, number);
  }

  public static float approachDegrees(float number, float max, float min) {
    float add = MathHelper.wrapDegrees(max - number);
    return approach(number, number + add, min);
  }

  public static float degreesDifferenceAbs(float f1, float f2) {
    return Math.abs(MathHelper.wrapDegrees(f2 - f1));
  }

  public void update() {
    if (target == null || dragon.getDistanceSq(target.x, target.y, target.z) < 4 || !dragon.world.isAirBlock(new BlockPos(target)) && (dragon.isHovering() || dragon.isFlying())) {
      BlockPos viewBlock = null;
      if (viewBlock != null) {
        target = new Vector3d(viewBlock.getX() + 0.5, viewBlock.getY() + 0.5, viewBlock.getZ() + 0.5);
      }
    }
    if (target != null) {
      if (target.y > 254) {
        target = new Vector3d(target.x, 254, target.z);
      }
    }
  }

  public Vector3d getFlightTarget() {
    return target == null ? Vector3d.ZERO : target;
  }

  public void setFlightTarget(Vector3d target) {
    this.target = target;
  }

  private float getDistanceXZ(double x, double z) {
    float f = (float) (dragon.getPosX() - x);
    float f2 = (float) (dragon.getPosZ() - z);
    return f * f + f2 * f2;
  }

  public void onSetAttackTarget(@Nullable LivingEntity LivingEntityIn) {
    if (prevAttackTarget != LivingEntityIn) {
      if (LivingEntityIn != null) {
        startPreyVec = new Vector3d(LivingEntityIn.getPosX(), LivingEntityIn.getPosY(), LivingEntityIn.getPosZ());
      } else {
        startPreyVec = new Vector3d(dragon.getPosX(), dragon.getPosY(), dragon.getPosZ());
      }
      startAttackVec = new Vector3d(dragon.getPosX(), dragon.getPosY(), dragon.getPosZ());
    }
    prevAttackTarget = LivingEntityIn;
  }

  protected static class PlayerFlightMoveHelper<T extends MobEntity & IFlyingMount> extends MovementController {

    private T dragon;

    public PlayerFlightMoveHelper(T dragon) {
      super(dragon);
      this.dragon = dragon;
    }

    @Override
    public void tick() {
      double flySpeed = speed * speedMod();
      Vector3d dragonVec = dragon.getPositionVec();
      Vector3d moveVec = new Vector3d(posX, posY, posZ);
      Vector3d normalized = moveVec.subtract(dragonVec).normalize();
      double dist = dragonVec.distanceTo(moveVec);
      dragon.setMotion(normalized.x * flySpeed, normalized.y * flySpeed, normalized.z * flySpeed);
      if (dist > 2.5E-7) {
        float yaw = (float) Math.toDegrees(Math.PI * 2 - Math.atan2(normalized.x, normalized.y));
        dragon.rotationYaw = limitAngle(dragon.rotationYaw, yaw, 5);
        dragon.setAIMoveSpeed((float) (speed));
      }
      dragon.move(MoverType.SELF, dragon.getMotion());
    }

    public double speedMod() {
      return 0.75D; // : 0.5D) * IafConfig.dragonFlightSpeedMod;
    }
  }
}
