package noobanidus.mods.carrierbees.client.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noobanidus.mods.carrierbees.client.model.CarrierBeeModel;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;
import noobanidus.mods.carrierbees.entities.CarrierBeeEntity;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class BeeHeldItemLayer<T extends AppleBeeEntity, M extends CarrierBeeModel<T>> extends LayerRenderer<T, M> {
  public BeeHeldItemLayer(IEntityRenderer<T, M> bee) {
    super(bee);
  }

  @Override
  public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, T entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
    ItemStack itemstack = entity.getHeldItemMainhand();
    if (itemstack.isEmpty()) {
      itemstack = entity.getHeldItemOffhand();
    }
    if (!itemstack.isEmpty()) {
      boolean block = itemstack.getItem() instanceof BlockItem;
      matrixStack.push();
      if (this.getEntityModel().isChild) {
        matrixStack.translate(0.0D, 0.75D, 0.0D);
        matrixStack.scale(0.5F, 0.5F, 0.5F);
      }

      CarrierBeeModel m = getEntityModel();
      matrixStack.push();
      matrixStack.translate(0, block ? 1.57 : 1.5, block ? 0.15 : 0.05);
      matrixStack.translate(0, -m.body.rotateAngleX * 0.5f, 0);
      matrixStack.rotate(Vector3f.XN.rotationDegrees(block ? 180f : 90f));
      matrixStack.rotate(new Quaternion(m.body.rotateAngleX * 1f, m.body.rotateAngleY * 1f, m.body.rotateAngleZ * 1f, false));
      matrixStack.scale(0.5f, 0.5f, 0.5f);
      Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(entity, itemstack, ItemCameraTransforms.TransformType.FIXED, true, matrixStack, buffer, light);
      matrixStack.pop();
      matrixStack.pop();
    }
  }
}
