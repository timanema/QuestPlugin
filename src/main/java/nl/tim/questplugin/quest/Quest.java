package nl.tim.questplugin.quest;

import nl.tim.questplugin.area.Area;
import nl.tim.questplugin.quest.stage.Stage;
import nl.tim.questplugin.quest.wrappers.RewardWrapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

public class Quest
{
    private UUID uuid;
    private Area questArea;
    private LinkedList<Stage> questStages;
    private List<RewardWrapper> rewards;

    private boolean areaLocked;
    private boolean replayable;

    // Some flags
    private boolean broken;
    private boolean hidden;
    private boolean branching;
    private boolean sequential;

    protected Quest(UUID uuid, 
                    Area questArea,
                    LinkedList<Stage> questStages,
                    List<RewardWrapper> rewards,
                    boolean areaLocked,
                    boolean replayable,
                    boolean hidden, 
                    boolean branching,
                    boolean sequential,
                    boolean broken)
    {
        this.uuid = uuid;
        this.questArea = questArea;
        this.questStages = questStages;
        this.rewards = rewards;
        this.areaLocked = areaLocked;
        this.replayable = replayable;
        this.hidden = hidden;
        this.branching = branching;
        this.sequential = sequential;
        this.broken = broken;
    }

    public Quest(UUID uuid,
                 Area questArea,
                 LinkedList<Stage> questStages,
                 List<RewardWrapper> rewards,
                 boolean areaLocked,
                 boolean replayable,
                 boolean hidden,
                 boolean branching,
                 boolean sequential)
    {
        this(uuid, questArea, questStages, rewards, areaLocked, replayable, hidden, branching, sequential, false);
    }

    public UUID getUUID()
    {
        return this.uuid;
    }

    /**
     * Returns this quest's {@link Area}.
     * @return This quest's {@link Area}.
     */
    public Area getQuestArea()
    {
        return this.questArea;
    }

    /**
     * Reruns check on this quest to check if it is broken. Returns result.
     * @return Boolean that indicates if this quest is broken after rechecking.
     */
    public boolean checkBroken()
    {
        // Check if the quest has stages and an area
        if (this.questStages != null && (!this.areaLocked && this.questArea != null))
        {
            for (Stage stage : this.questStages)
            {
                // Check if there is a broken stage
                if (stage.checkBroken())
                {
                    this.broken = true;
                    return true;
                }
            }

            // At this points all checks have succeeded, safe to assume this quest is no longer broken
            this.broken = false;
        }

        return this.broken;
    }

    public boolean checkBranches()
    {
        for (Stage stage : this.getStages())
        {
            if (stage.hasBranchingTasks() || stage.isBranching())
            {
                this.branching = true;
                return true;
            }
        }

        this.branching = false;
        return false;
    }


    /**
     * Returns a boolean indicating whether this quest can be started/progressed.
     * @return True if this quest can be started/progressed.
     */
    public boolean isAvailable()
    {
        return !this.hidden && !this.broken;
    }

    /**
     * Returns a boolean indicating whether this quest is broken. If this returns true,
     * the quest is either not yet configured properly on something has failed during initialization of this quest.
     * @return True if this quest is broken.
     */
    public boolean isBroken()
    {
        return this.broken;
    }

    /**
     * Returns a boolean indicating whether this quest is hidden (can not be viewed by players).
     * @return True if  this quest is hidden (can not be viewed by players).
     */
    public boolean isHidden()
    {
        return this.hidden;
    }

    /**
     * Returns a boolean indicating whether this quest requires an {@link Area} or can be progressed anywhere.
     * @return True if this quest requires an {@link Area} or can be progressed anywhere.
     */
    public boolean isAreaLocked()
    {
        return this.areaLocked;
    }

    /**
     * Returns a boolean indicating whether this quest is replayable.
     * @return True if this quest is replayable.
     */
    public boolean isReplayable()
    {
        return this.replayable;
    }

    /**
     * Returns a boolean indicating whether this quest has branches or not.
     * @return True if this quest has branches, false otherwise.
     */
    public boolean hasBranches()
    {
        return this.branching;
    }

    /**
     * Returns a boolean indicating whether this quest's stages have to be completed sequentially or can be done in parallel.
     * Do note that a quest is automatically parallel if it has branching stages.
     * @return True if the stages have to be completed sequentially, false otherwise.
     */
    public boolean isSequential()
    {
        return this.sequential || this.hasBranches();
    }

    public List<Stage> getFirstStages()
    {
        List<Stage> firstStages = new ArrayList<>();

        if (isSequential())
        {
            firstStages.add(this.questStages.getFirst());
        } else
        {
            firstStages.addAll(this.questStages);
        }

        return firstStages;
    }

    public LinkedList<Stage> getStages()
    {
        return this.questStages;
    }

    public List<RewardWrapper> getRewards()
    {
        return this.rewards;
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

        Quest quest = (Quest) object;

        return new EqualsBuilder()
                .append(areaLocked, quest.areaLocked)
                .append(replayable, quest.replayable)
                .append(broken, quest.broken)
                .append(hidden, quest.hidden)
                .append(branching, quest.branching)
                .append(uuid, quest.uuid)
                .append(questArea, quest.questArea)
                .append(questStages, quest.questStages)
                .append(rewards, quest.rewards)
                .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37)
                .append(uuid)
                .append(questArea)
                .append(questStages)
                .append(rewards)
                .append(areaLocked)
                .append(replayable)
                .append(broken)
                .append(hidden)
                .append(branching)
                .toHashCode();
    }
}
