package dev.pixeltk.deathcounter;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.pixeltk.deathcounter.commands.GetDeathsCommand;
import dev.pixeltk.deathcounter.components.PlayerDeathCounterComponent;

import javax.annotation.Nonnull;

public class DeathCounter extends JavaPlugin
{
    private ComponentType<EntityStore, PlayerDeathCounterComponent> deathCounterType;
    public static DeathCounter instance;

    public DeathCounter(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    public static DeathCounter get() {
        return instance;
    }

    @Override
    protected void setup() {
        // Component registry
        deathCounterType = this.getEntityStoreRegistry().registerComponent(PlayerDeathCounterComponent.class, "DeathCounter", PlayerDeathCounterComponent.CODEC);
        // System registry
        this.getEntityStoreRegistry().registerSystem(new DeathCounterSystems.IncrementCounterOnPlayerDeath());
        // Command registry
        this.getCommandRegistry().registerCommand(new GetDeathsCommand("getDeaths", "Gets the number of deaths of a player."));

        // Event registry
        this.getEventRegistry().registerGlobal(PlayerConnectEvent.class, this::onPlayerReady);
    }
    private void onPlayerReady(PlayerConnectEvent event)
    {
        // We need the playercomp for sending messages to the player.
        Player player = event.getHolder().getComponent(Player.getComponentType());
        assert player == null;

        Holder<EntityStore> store = event.getHolder();

        PlayerDeathCounterComponent deathCounter = store.getComponent(PlayerDeathCounterComponent.getComponentType());

        if (deathCounter == null)
        {
            // Give them the death counter component.
            deathCounter = new PlayerDeathCounterComponent();
            store.addComponent(PlayerDeathCounterComponent.getComponentType(), deathCounter);
            // First time message, which ensures everything is okay.
            player.sendMessage(Message.raw("Welcome, " + event.getPlayerRef().getUsername() + "! Try not to die 88w88."));
        }
        else
        {
            player.sendMessage(Message.raw("Welcome back, "+ event.getPlayerRef().getUsername() +"! Current death count: "+ Integer.toUnsignedString(deathCounter.deaths)));
            if (deathCounter.deaths == 0)
            {
                // A small treat to those who haven't died.
                player.sendMessage(Message.raw("Good job on not dying so far!"));
            }
        }
    }
    public ComponentType<EntityStore, PlayerDeathCounterComponent> getDeathCounterType() {return deathCounterType;}
}