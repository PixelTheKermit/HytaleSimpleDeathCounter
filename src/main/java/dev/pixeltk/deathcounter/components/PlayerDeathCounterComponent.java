package dev.pixeltk.deathcounter.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.pixeltk.deathcounter.DeathCounter;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class PlayerDeathCounterComponent implements Component<EntityStore>
{
    public static final BuilderCodec<PlayerDeathCounterComponent> CODEC;
    public int deaths;
    public boolean isDead;

    public PlayerDeathCounterComponent()
    {
        this.deaths = 0;
        this.isDead = false;
    }

    public static ComponentType<EntityStore, PlayerDeathCounterComponent> getComponentType(){
        return DeathCounter.get().getDeathCounterType();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        PlayerDeathCounterComponent newComp = new PlayerDeathCounterComponent();
        newComp.deaths = this.deaths;

        return newComp;
    }
    static {
        BuilderCodec.Builder<PlayerDeathCounterComponent> builderCodec = BuilderCodec.builder(PlayerDeathCounterComponent.class, PlayerDeathCounterComponent::new);
        builderCodec = builderCodec.append(
                new KeyedCodec<>("Deaths", Codec.INTEGER),
                (comp, input) -> comp.deaths = input,
                (comp) -> comp.deaths).add();

        builderCodec = builderCodec.append(
                new KeyedCodec<>("IsDead", Codec.BOOLEAN),
                (comp, input) -> comp.isDead = input,
                (comp) -> comp.isDead).add();

        CODEC = builderCodec.build();
    }
}
