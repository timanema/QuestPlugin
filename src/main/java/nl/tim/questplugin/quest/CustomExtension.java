/*
 * Copyright (C) 2019  Tim Anema
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package nl.tim.questplugin.quest;

import nl.tim.questplugin.api.Configurable;
import nl.tim.questplugin.player.PlayerHandler;
import nl.tim.questplugin.player.QPlayer;
import nl.tim.questplugin.storage.Saveable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public abstract class CustomExtension extends Configurable implements Saveable
{
    private UUID uuid;
    private String identifier;
    private Owner owner;

    private String displayName;
    private String description;

    private TaskHandler taskHandler;
    private QuestHandler questHandler;
    private PlayerHandler playerHandler;

    public CustomExtension(String displayName, String description)
    {
        this.displayName = displayName;
        this.description = description;
    }

    void register(UUID uuid,
                  String identifier,
                  Owner owner,
                  TaskHandler taskHandler,
                  QuestHandler questHandler,
                  PlayerHandler playerHandler,
                  Map<String, Object> settings)
    {
        this.uuid = uuid;
        this.identifier = identifier;
        this.owner = owner;
        this.taskHandler = taskHandler;
        this.questHandler = questHandler;
        this.playerHandler = playerHandler;
        this.copySettings(settings);
    }

    protected void init()
    {
        // Empty, but tasks can override this
    }

    public QPlayer getPlayer(Player player)
    {
        return this.getPlayerHandler().getPlayer(player);
    }

    public UUID getUUID()
    {
        return this.uuid;
    }

    public String getIdentifier()
    {
        return this.identifier;
    }

    protected Owner getOwner()
    {
        return this.owner;
    }

    protected String getDisplayName()
    {
        return this.displayName;
    }

    protected String getDescription()
    {
        return this.description;
    }

    protected TaskHandler getTaskHandler()
    {
        return this.taskHandler;
    }

    protected QuestHandler getQuestHandler()
    {
        return this.questHandler;
    }

    protected PlayerHandler getPlayerHandler()
    {
        return this.playerHandler;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }

        if (object == null || getClass() != object.getClass())
        {
            return false;
        }

        CustomExtension that = (CustomExtension) object;

        return new EqualsBuilder()
                .append(uuid, that.uuid)
                .append(identifier, that.identifier)
                .append(owner, that.owner)
                .append(displayName, that.displayName)
                .append(description, that.description)
                .append(taskHandler, that.taskHandler)
                .append(questHandler, that.questHandler)
                .append(playerHandler, that.playerHandler)
                .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37)
                .append(uuid)
                .append(identifier)
                .append(owner)
                .append(displayName)
                .append(description)
                .append(taskHandler)
                .append(questHandler)
                .append(playerHandler)
                .toHashCode();
    }
}
