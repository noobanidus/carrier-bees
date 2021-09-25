package noobanidus.mods.carrierbees.client.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.client.model.BodyModel;
import noobanidus.mods.carrierbees.client.model.CarrierBeeModel;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class BeeHeldItemLayer<T extends AnimalEntity, M extends BodyModel<T>> extends LayerRenderer<T, M> {
  public BeeHeldItemLayer(IEntityRenderer<T, M> bee) {
    super(bee);
  }

  @Override
  public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, T entity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
    ItemStack itemstack = entity.getMainHandItem();
    if (itemstack.isEmpty()) {
      itemstack = entity.getOffhandItem();
    }
    if (!itemstack.isEmpty()) {
      boolean block = itemstack.getItem() instanceof BlockItem;
      matrixStack.pushPose();
      if (this.getParentModel().young) {
        matrixStack.translate(0.0D, 0.75D, 0.0D);
        matrixStack.scale(0.5F, 0.5F, 0.5F);
      }

      BodyModel<T> m = getParentModel();
      matrixStack.pushPose();
      matrixStack.translate(0, block ? 1.57 : 1.5, block ? 0.15 : 0.05);
      matrixStack.translate(0, -m.body.xRot * 0.5f, 0);
      matrixStack.mulPose(Vector3f.XN.rotationDegrees(block ? 180f : 90f));
      matrixStack.mulPose(new Quaternion(m.body.xRot * 1f, m.body.yRot * 1f, m.body.zRot * 1f, false));
      matrixStack.scale(0.5f, 0.5f, 0.5f);
      Minecraft.getInstance().getItemInHandRenderer().renderItem(entity, itemstack, ItemCameraTransforms.TransformType.FIXED, true, matrixStack, buffer, light);
      matrixStack.popPose();
      matrixStack.popPose();
    }
  }
}
