package dev.pixeltk.deathcounter;

import com.hypixel.hytale.builtin.mounts.NPCMountSystems;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.pixeltk.deathcounter.components.PlayerDeathCounterComponent;

import javax.annotation.Nonnull;

public class DeathCounterSystems {
    public static class IncrementCounterOnPlayerDeath extends DeathSystems.OnDeathSystem
    {
        public Query<EntityStore> getQuery() {
            return Player.getComponentType();
        }

        public void onComponentAdded(@Nonnull Ref<EntityStore> ref, @Nonnull DeathComponent component, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
            Player playerComponent = store.getComponent(ref, Player.getComponentType());

            assert playerComponent != null;

            PlayerDeathCounterComponent deathCounter = store.getComponent(ref, PlayerDeathCounterComponent.getComponentType());

            assert deathCounter != null;

            // Small check. The player can leave while dead, and it does increment the death counter if they rejoin.
            if (!deathCounter.isDead) {
                deathCounter.deaths++;
                deathCounter.isDead = true;

                playerComponent.sendMessage(Message.raw("You died! Current death count: " + Integer.toUnsignedString(deathCounter.deaths)));
            }
        }

        public void onComponentRemoved(@Nonnull Ref<EntityStore> ref, @Nonnull DeathComponent component, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
            PlayerDeathCounterComponent deathCounter = store.getComponent(ref, PlayerDeathCounterComponent.getComponentType());

            assert deathCounter != null;

            deathCounter.isDead = false;
        }
    }
}
