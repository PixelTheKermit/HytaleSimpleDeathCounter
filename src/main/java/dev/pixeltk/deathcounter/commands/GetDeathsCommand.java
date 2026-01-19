package dev.pixeltk.deathcounter.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.pixeltk.deathcounter.components.PlayerDeathCounterComponent;

import javax.annotation.Nonnull;

public class GetDeathsCommand extends AbstractPlayerCommand {

    public GetDeathsCommand(String name, String description) {
        super(name, description);
        this.setPermissionGroup(GameMode.Adventure);
    }
    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world)
    {
        Player playerComponent = store.getComponent(ref, Player.getComponentType());

        assert playerComponent != null;

        PlayerDeathCounterComponent deathCounter = store.getComponent(ref, PlayerDeathCounterComponent.getComponentType());

        assert deathCounter != null;

        playerComponent.sendMessage(Message.raw("Your current death count is: " + Integer.toUnsignedString(deathCounter.deaths)));
    }

}
