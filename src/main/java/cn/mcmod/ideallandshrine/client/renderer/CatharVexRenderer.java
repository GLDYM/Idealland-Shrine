package cn.mcmod.ideallandshrine.client.renderer;

import cn.mcmod.ideallandshrine.IdeallandShrine;
import cn.mcmod.ideallandshrine.entity.CatharVexEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CatharVexRenderer extends MobRenderer<CatharVexEntity, HumanoidModel<CatharVexEntity>> {
    private static final ResourceLocation NORMAL = ResourceLocation.fromNamespaceAndPath(IdeallandShrine.MODID,
            "textures/entity/cathar_vex.png");
    private static final ResourceLocation CHARGING = ResourceLocation.fromNamespaceAndPath(IdeallandShrine.MODID,
            "textures/entity/cathar_vex_charging.png");

    public CatharVexRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.PLAYER)),
                0.3F);
    }

    @Override
    protected void scale(CatharVexEntity entity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(0.4F, 0.4F, 0.4F);
        super.scale(entity, poseStack, partialTickTime);
    }

    @Override
    public ResourceLocation getTextureLocation(CatharVexEntity entity) {
        return entity.isCharging() ? CHARGING : NORMAL;
    }
}
