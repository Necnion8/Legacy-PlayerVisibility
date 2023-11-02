package com.gmail.necnionch.mymod.legacyplayervisibility.mixin;

import com.gmail.necnionch.mymod.legacyplayervisibility.PlayerVisibilityMod;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    private void renderEntity(Entity entity, float f, CallbackInfoReturnable<Boolean> cir) {
        if (PlayerVisibilityMod.isRenderPlayer())
            return;

        ClientPlayerEntity me = MinecraftClient.getInstance().player;

        if (me.squaredDistanceTo(entity) > 2)
            return;

        if (entity instanceof PlayerEntity) {
            try {
                renderHitbox(entity, me.x, me.y, me.z);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            cir.cancel();
        }
    }


    private void renderHitbox(Entity entity, double x, double y, double z) {
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();

//        float i = entity.width / 2.0F;
        Box box = entity.getBoundingBox();

        double offsetX = x - entity.prevX + entity.prevX;
        double offsetY = y - entity.prevY + entity.prevY;
        double offsetZ = z - entity.prevZ + entity.prevZ;

        Box box2 = new Box(
                box.minX - offsetX, box.minY - offsetY, box.minZ - offsetZ,
                box.maxX - offsetX, box.maxY - offsetY, box.maxZ - offsetZ
        );
        WorldRenderer.drawBox(box2, 255, 255, 255, 255 / 2);
//        if (entity instanceof LivingEntity) {
//            float j = 0.01F;
//            WorldRenderer.drawBox(new Box(x - (double)i, y + (double)entity.getEyeHeight() - 0.009999999776482582, z - (double)i, x + (double)i, y + (double)entity.getEyeHeight() + 0.009999999776482582, z + (double)i), 255, 0, 0, 255);
//        }

//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder bufferBuilder = tessellator.getBuffer();
//        Vec3d vec3d = entity.getRotationVector(yaw);
//        bufferBuilder.begin(3, VertexFormats.POSITION_COLOR);
//        bufferBuilder.vertex(x, y + (double)entity.getEyeHeight(), z).color(0, 0, 255, 255).next();
//        bufferBuilder.vertex(x + vec3d.x * 2.0, y + (double)entity.getEyeHeight() + vec3d.y * 2.0, z + vec3d.z * 2.0).color(0, 0, 255, 255).next();
//        tessellator.draw();

        GlStateManager.enableTexture();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);

    }


}
