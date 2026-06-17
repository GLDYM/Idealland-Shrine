package cn.mcmod.ideallandshrine.init;

import cn.mcmod.ideallandshrine.IdeallandShrine;
import cn.mcmod.ideallandshrine.entity.CatharVexEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber()
public final class EntityTypeRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister
            .create(BuiltInRegistries.ENTITY_TYPE, IdeallandShrine.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<CatharVexEntity>> CATHAR_VEX = ENTITY_TYPES
            .register("cathar_vex",
                    () -> EntityType.Builder.<CatharVexEntity>of(CatharVexEntity::new, MobCategory.MONSTER)
                            .sized(0.4F, 0.8F)
                            .clientTrackingRange(8)
                            .build("cathar_vex"));

    private EntityTypeRegistry() {
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(CATHAR_VEX.get(), CatharVexEntity.createAttributes().build());
    }
}
