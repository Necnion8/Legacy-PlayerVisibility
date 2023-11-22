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
    @Inject(method = "method_6913", at = @At("HEAD"), cancellable = true)
    private void pv$method_6913(Entity entity, double d, double e, double f, float g, float h, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        if (PlayerVisibilityMod.isRenderPlayer() || !(entity instanceof PlayerEntity))
            return;

        ClientPlayerEntity me = MinecraftClient.getInstance().player;
        if (me.squaredDistanceTo(entity) > 2)
            return;

        cir.cancel();

        try {
            pv$renderHitbox(entity, d, e, f, g, h);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private void pv$renderHitbox(Entity entity, double d, double e, double f, float g, float h) {
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();

        Box box = entity.getBoundingBox();
        Box box2 = new Box(box.minX - entity.x + d, box.minY - entity.y + e, box.minZ - entity.z + f, box.maxX - entity.x + d, box.maxY - entity.y + e, box.maxZ - entity.z + f);
        WorldRenderer.drawBox(box2, 255, 255, 255, 255);

        GlStateManager.enableTexture();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }

}
