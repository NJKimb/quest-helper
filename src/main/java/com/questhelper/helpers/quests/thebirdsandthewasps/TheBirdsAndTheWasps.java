/*
 * Copyright (c) 2026, NJKimb <https://github.com/NJKimb>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.questhelper.helpers.quests.thebirdsandthewasps;

import com.questhelper.panel.PanelDetails;
import com.questhelper.questhelpers.BasicQuestHelper;
import com.questhelper.requirements.ChatMessageRequirement;
import com.questhelper.requirements.Requirement;
import com.questhelper.requirements.item.ItemRequirement;
import static com.questhelper.requirements.util.LogicHelper.and;
import com.questhelper.requirements.var.VarbitRequirement;
import com.questhelper.rewards.ExperienceReward;
import com.questhelper.rewards.ItemReward;
import com.questhelper.rewards.QuestPointReward;
import com.questhelper.rewards.UnlockReward;
import com.questhelper.steps.ConditionalStep;
import com.questhelper.steps.DetailedQuestStep;
import com.questhelper.steps.NpcStep;
import com.questhelper.steps.ObjectStep;
import com.questhelper.steps.QuestStep;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.runelite.api.coords.WorldPoint;

public class TheBirdsAndTheWasps extends BasicQuestHelper
{
	// Required items
	ItemRequirement beeBox, beeHunterHat, insectRepellent, insectNet;
	ItemRequirement beeHunterHatEquipped, insectNetEquipped;
	// Recommended items

	// Mid-quest item requirements

	// Zones

	// Miscellaneous requirements
	VarbitRequirement spokenToBeaAfterCutscene;

	// Fields
	ChatMessageRequirement beesAngry;

	// Steps
	NpcStep speakToFarmerBea, speakToBeeDealer, returnBees, speakToBeaAfterCutscene;
	ObjectStep takeEquipmentFromBox;
	DetailedQuestStep calmBees, equipGear, catchWasps;

	@Override
	protected void setupZones()
	{
	}

	@Override
	protected void setupRequirements()
	{
		beeBox = new ItemRequirement("Bee box", 34775);

		beeHunterHat = new ItemRequirement("Bee hunter's hat", 34777);
		insectNet = new ItemRequirement("Insect net", 10010);
		beeHunterHatEquipped = beeHunterHat.equipped();
		insectNetEquipped = insectNet.equipped();

		spokenToBeaAfterCutscene = new VarbitRequirement(16849, 1);

		beesAngry = new ChatMessageRequirement("You can sense the insects in the box getting agitated. Best give them a shake.");
		beesAngry.setInvalidateRequirement(
			new ChatMessageRequirement(
				"The agitated insects lash out and sting you through the holes in the box.",
				"You give the box a firm shake and the insects seem to calm down."
			)
		);
	}

	void setupSteps()
	{
		speakToFarmerBea = new NpcStep(this, 3217,new WorldPoint(3313, 3376, 0), "Speak to Farmer Bea to start the quest.");
		speakToFarmerBea.addDialogStep("Yes.");

		speakToBeeDealer = new NpcStep(this, 3220, new WorldPoint(3188, 3494, 0), "Speak to the Bee Dealer in the Grand Exchange");

		returnBees = new NpcStep(this, 3217, new WorldPoint(3313, 3376, 0), "Return the bees to Farmer Bea, clicking on the bee box when the bees get agitated.");

		calmBees = new DetailedQuestStep(this, "Click the bee box to calm the bees.", beeBox.highlighted());

		speakToBeaAfterCutscene = new NpcStep(this, 3217, new WorldPoint(3313, 3376, 0), "Speak to Farmer Bea after watching the cutscene.");

		takeEquipmentFromBox = new ObjectStep(this, 26249, new WorldPoint(3315, 3376, 0), "Take the equipment from the equipment box.");
		takeEquipmentFromBox.addDialogStep("Take everything.");

		equipGear = new DetailedQuestStep(this, "Equip the Bee hunter's hat and Insect net.", beeHunterHat.highlighted(), insectNet.highlighted());

		catchWasps = new NpcStep(this, 3226, new WorldPoint(3316, 3363, 0), "Catch wasps with your insect net.", true, beeHunterHatEquipped, insectNetEquipped);
	}

	@Override
	public Map<Integer, QuestStep> loadSteps()
	{
		initializeRequirements();
		setupSteps();

		var steps = new HashMap<Integer, QuestStep>();

		steps.put(0, speakToFarmerBea);
		steps.put(5, speakToFarmerBea);
		steps.put(10,speakToBeeDealer);

		var cCalmBees = new ConditionalStep(this, returnBees);
		cCalmBees.addStep(beesAngry, calmBees);
		steps.put(15, cCalmBees);
		steps.put(20, returnBees);

		var cAfterCutscene = new ConditionalStep(this, speakToBeaAfterCutscene);
		cAfterCutscene.addStep(and(spokenToBeaAfterCutscene, beeHunterHatEquipped, insectNetEquipped), catchWasps);
		cAfterCutscene.addStep(and(spokenToBeaAfterCutscene, beeHunterHat, insectNet), equipGear);
		cAfterCutscene.addStep(spokenToBeaAfterCutscene, takeEquipmentFromBox);
		steps.put(25, cAfterCutscene);

		return steps;
	}

	@Override
	public List<Requirement> getGeneralRequirements()
	{
		return List.of(
		);
	}

	@Override
	public List<Requirement> getGeneralRecommended()
	{
		return List.of(
			//
		);
	}

	@Override
	public List<ItemRequirement> getItemRequirements()
	{
		return List.of(
		);
	}

	@Override
	public List<ItemRequirement> getItemRecommended()
	{
		return List.of(
		);
	}

	@Override
	public List<String> getCombatRequirements()
	{
		return List.of(
		);
	}

	@Override
	public List<String> getNotes()
	{
		return List.of(
		);
	}

	@Override
	public QuestPointReward getQuestPointReward()
	{
		return new QuestPointReward(1);
	}

	@Override
	public List<ExperienceReward> getExperienceRewards()
	{
		return List.of(
		);
	}

	@Override
	public List<ItemReward> getItemRewards()
	{
		return List.of();
	}

	@Override
	public List<UnlockReward> getUnlockRewards()
	{
		return List.of(
		);
	}

	@Override
	public List<PanelDetails> getPanels()
	{
		var sections = new ArrayList<PanelDetails>();

		sections.add(new PanelDetails("Starting off", List.of(
			speakToFarmerBea
		), List.of(
		)));

		sections.add(new PanelDetails("Getting the bees", List.of(
			speakToBeeDealer,
			returnBees,
			calmBees,
			speakToBeaAfterCutscene
		), List.of(
		)));

		sections.add(new PanelDetails("Catching wasps", List.of(
			takeEquipmentFromBox,
			equipGear,
			catchWasps
		), List.of(
		)));

		return sections;
	}
}
