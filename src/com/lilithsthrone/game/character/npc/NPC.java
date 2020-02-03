package com.lilithsthrone.game.character.npc;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.lilithsthrone.game.Game;
import com.lilithsthrone.game.character.CharacterImportSetting;
import com.lilithsthrone.game.character.CharacterUtils;
import com.lilithsthrone.game.character.EquipClothingSetting;
import com.lilithsthrone.game.character.GameCharacter;
import com.lilithsthrone.game.character.attributes.AffectionLevel;
import com.lilithsthrone.game.character.attributes.Attribute;
import com.lilithsthrone.game.character.attributes.CorruptionLevel;
import com.lilithsthrone.game.character.attributes.ObedienceLevel;
import com.lilithsthrone.game.character.attributes.ObedienceLevelBasic;
import com.lilithsthrone.game.character.body.Body;
import com.lilithsthrone.game.character.body.CoverableArea;
import com.lilithsthrone.game.character.body.types.AntennaType;
import com.lilithsthrone.game.character.body.types.HornType;
import com.lilithsthrone.game.character.body.types.PenisType;
import com.lilithsthrone.game.character.body.types.TailType;
import com.lilithsthrone.game.character.body.types.VaginaType;
import com.lilithsthrone.game.character.body.types.WingType;
import com.lilithsthrone.game.character.body.valueEnums.AssSize;
import com.lilithsthrone.game.character.body.valueEnums.BodyMaterial;
import com.lilithsthrone.game.character.body.valueEnums.BodySize;
import com.lilithsthrone.game.character.body.valueEnums.CumProduction;
import com.lilithsthrone.game.character.body.valueEnums.CupSize;
import com.lilithsthrone.game.character.body.valueEnums.Femininity;
import com.lilithsthrone.game.character.body.valueEnums.HipSize;
import com.lilithsthrone.game.character.body.valueEnums.LipSize;
import com.lilithsthrone.game.character.body.valueEnums.Muscle;
import com.lilithsthrone.game.character.body.valueEnums.OrificeModifier;
import com.lilithsthrone.game.character.body.valueEnums.PenisSize;
import com.lilithsthrone.game.character.body.valueEnums.TesticleSize;
import com.lilithsthrone.game.character.effects.Perk;
import com.lilithsthrone.game.character.effects.StatusEffect;
import com.lilithsthrone.game.character.fetishes.Fetish;
import com.lilithsthrone.game.character.fetishes.FetishDesire;
import com.lilithsthrone.game.character.gender.Gender;
import com.lilithsthrone.game.character.gender.PronounType;
import com.lilithsthrone.game.character.persona.NameTriplet;
import com.lilithsthrone.game.character.persona.Occupation;
import com.lilithsthrone.game.character.race.AbstractRacialBody;
import com.lilithsthrone.game.character.race.FurryPreference;
import com.lilithsthrone.game.character.race.Race;
import com.lilithsthrone.game.character.race.RaceStage;
import com.lilithsthrone.game.character.race.RacialBody;
import com.lilithsthrone.game.character.race.Subspecies;
import com.lilithsthrone.game.combat.Combat;
import com.lilithsthrone.game.combat.Spell;
import com.lilithsthrone.game.combat.SpellSchool;
import com.lilithsthrone.game.dialogue.DialogueNode;
import com.lilithsthrone.game.dialogue.responses.Response;
import com.lilithsthrone.game.dialogue.utils.UtilText;
import com.lilithsthrone.game.inventory.AbstractCoreItem;
import com.lilithsthrone.game.inventory.CharacterInventory;
import com.lilithsthrone.game.inventory.InventorySlot;
import com.lilithsthrone.game.inventory.clothing.AbstractClothing;
import com.lilithsthrone.game.inventory.enchanting.ItemEffect;
import com.lilithsthrone.game.inventory.enchanting.ItemEffectType;
import com.lilithsthrone.game.inventory.enchanting.TFEssence;
import com.lilithsthrone.game.inventory.enchanting.TFModifier;
import com.lilithsthrone.game.inventory.enchanting.TFPotency;
import com.lilithsthrone.game.inventory.item.AbstractItem;
import com.lilithsthrone.game.inventory.item.AbstractItemType;
import com.lilithsthrone.game.inventory.item.ItemType;
import com.lilithsthrone.game.occupantManagement.SlaveJob;
import com.lilithsthrone.game.settings.ForcedTFTendency;
import com.lilithsthrone.game.sex.SexAreaOrifice;
import com.lilithsthrone.game.sex.SexAreaPenetration;
import com.lilithsthrone.game.sex.SexControl;
import com.lilithsthrone.game.sex.SexPace;
import com.lilithsthrone.game.sex.SexType;
import com.lilithsthrone.game.sex.positions.AbstractSexPosition;
import com.lilithsthrone.game.sex.positions.slots.SexSlot;
import com.lilithsthrone.game.sex.sexActions.SexAction;
import com.lilithsthrone.game.sex.sexActions.SexActionInterface;
import com.lilithsthrone.main.Main;
import com.lilithsthrone.utils.Colour;
import com.lilithsthrone.utils.Util;
import com.lilithsthrone.utils.Util.Value;
import com.lilithsthrone.utils.XMLSaving;
import com.lilithsthrone.world.WorldType;
import com.lilithsthrone.world.places.AbstractPlaceType;

/**
 * @since 0.1.0
 * @version 0.3.5.5
 * @author Innoxia
 */
public abstract class NPC extends GameCharacter implements XMLSaving {
	
	protected long lastTimeEncountered = DEFAULT_TIME_START_VALUE;
	
	protected float buyModifier;
	protected float sellModifier;
	
	protected boolean addedToContacts;
	
	public Set<NPCFlagValue> NPCFlagValues;
	
	protected Set<SexSlot> sexPositionPreferences;
	
	protected Gender genderPreference = null;
	protected Subspecies subspeciesPreference = null;
	protected RaceStage raceStagePreference = null;
	
	protected NPC(boolean isImported,
			NameTriplet nameTriplet,
			String surname,
			String description,
			int age,
			Month birthMonth,
			int birthDay,
			int level,
			Gender startingGender,
			Subspecies startingSubspecies,
			RaceStage stage,
			CharacterInventory inventory,
			WorldType worldLocation,
			AbstractPlaceType startingPlace,
			boolean addedToContacts,
			NPCGenerationFlag... generationFlags) {
		super(nameTriplet, surname, description, level,
				LocalDateTime.of(Main.game.getStartingDate().getYear()-age, birthMonth, birthDay, 12, 0),
				startingGender, startingSubspecies, stage, inventory, worldLocation, startingPlace);
		
		List<NPCGenerationFlag> flags = Arrays.asList(generationFlags);
		
		this.addedToContacts = addedToContacts;
		
		sexPositionPreferences = new HashSet<>();
		
		buyModifier=0.75f;
		sellModifier=1.5f;
		
		NPCFlagValues = new HashSet<>();
		
		if(!isImported) {
			setStartingBody(true);
			if(!flags.contains(NPCGenerationFlag.NO_CLOTHING_EQUIP) && this.getBody()!=null) {
				equipClothing(EquipClothingSetting.getAllClothingSettings());
			}
		}
		
		if(this.getBody()!=null) {
			if(getLocation().equals(Main.game.getPlayer().getLocation()) && getWorldLocation()==Main.game.getPlayer().getWorldLocation()) {
				for(CoverableArea ca : CoverableArea.values()) {
					if(isCoverableAreaVisible(ca) && ca!=CoverableArea.MOUTH) {
						this.setAreaKnownByCharacter(ca, Main.game.getPlayer(), true);
					}
				}
			}
			
			if(!isImported || Main.isVersionOlderThan(Game.loadingVersion, "0.3.3.5")) {
				this.setStartingCombatMoves();
			}
			
			loadImages();
		}
	}
	
	
	public void setStartingCombatMoves() {
		resetDefaultMoves();
	}

	/**
	 * Helper method that should be overridden and included in constructor. Sets custom body parts.<br/>
	 * <b><u>What to include</u></b><br/>
	 * <b><u>Persona</u></b><br/>
	 * <b>-</b> Starting attributes.<br/>
	 * <b>-</b> Personality.<br/>
	 * <b>-</b> Sexual orientation.<br/>
	 * <b>-</b> Occupation.<br/>
	 * <b>-</b> Fetishes.<br/>
	 * <br/><br/>
	 * 
	 * <b><u>Body</u></b><br/>
	 * <b>Core parts:</b><br/>
	 * <b>-</b> Any part type changes.<br/>
	 * <b>-</b> Height.<br/>
	 * <b>-</b> Femininity.<br/>
	 * <b>-</b> Muscle & body size.<br/>
	 * <br/>
	 * <b>Coverings:</b><br/>
	 * <b>-</b> Body coverings for eyes, skin & fur.<br/>
	 * <b>-</b> Hair coverings, length & style.<br/>
	 * <b>-</b> Body hair coverings & length. (Underarm, ass, pubic, facial.)<br/>
	 * <b>-</b> Makeup. (Nail polish, blusher, lipstick, eye liner, eye shadow.)<br/>
	 * <br/>
	 * <b>Face:</b><br/>
	 * <b>-</b> Oral virginity.<br/>
	 * <b>-</b> Lip size.<br/>
	 * <b>-</b> Eye count.<br/>
	 * <b>-</b> Throat capacity & modifiers.<br/>
	 * <b>-</b> Tongue length.<br/>
	 * <b>-</b> Tongue modifiers.<br/>
	 * <br/>
	 * <b>Chest:</b><br/>
	 * <b>-</b> Virginity.<br/>
	 * <b>-</b> Breast size.<br/>
	 * <b>-</b> Breast shape.<br/>
	 * <b>-</b> Nipple shape.<br/>
	 * <b>-</b> Nipple size.<br/>
	 * <b>-</b> Areolae shape.<br/>
	 * <b>-</b> Areolae size.<br/>
	 * <b>-</b> Nipple settings (capacity, wetness, plasticity, elasticity, modifiers).<br/>
	 * <b>-</b> Milk production.<br/>
	 * <b>-</b> Milk modifiers & flavour.<br/>
	 * <br/>
	 * <b>Arms:</b><br/>
	 * <b>-</b> Arm count.<br/>
	 * <br/>
	 * <b>Ass:</b><br/>
	 * <b>-</b> Virginity.<br/>
	 * <b>-</b> Ass size.<br/>
	 * <b>-</b> Hip size.<br/>
	 * <b>-</b> Anus bleaching.<br/>
	 * <b>-</b> Anus settings (capacity, wetness, plasticity, elasticity, modifiers).<br/>
	 * <br/>
	 * <b>Penis:</b><br/>
	 * <b>-</b> Virginity.<br/>
	 * <b>-</b> Penis size.<br/>
	 * <b>-</b> Testicle size.<br/>
	 * <b>-</b> Testicle count.<br/>
	 * <b>-</b> Cum production.<br/>
	 * <b>-</b> Cum modifiers & flavour.<br/>
	 * <b>-</b> Penis modifiers.<br/>
	 * <b>-</b> Penis urethra settings (capacity, wetness, plasticity, elasticity, modifiers).<br/>
	 * <br/>
	 * <b>Vagina:</b><br/>
	 * <b>-</b> Virginity.<br/>
	 * <b>-</b> Clit size.<br/>
	 * <b>-</b> Labia size.<br/>
	 * <b>-</b> Squirter.<br/>
	 * <b>-</b> Girlcum modifiers & flavour.<br/>
	 * <b>-</b> Vagina settings (capacity, wetness, plasticity, elasticity, modifiers).<br/>
	 * <b>-</b> Vagina urethra settings (capacity, wetness, plasticity, elasticity, modifiers).<br/>
	 * <br/>
	 * <b>Feet:</b><br/>
	 * <b>-</b> Foot structure.<br/>
	 */
	public abstract void setStartingBody(boolean setPersona);
	
	public final void equipClothing() {
		equipClothing(new ArrayList<>());
	}
	
	/**
	 * Helper method that should be overridden and included in constructor. Should set starting clothing and piercings.<br/>
	 * <b><u>What to include</u></b><br/>
	 * <b>-</b> Weapons.<br/>
	 * <b>-</b> Tattoos.<br/>
	 * <b>-</b> Scars.<br/>
	 * <b>-</b> Piercings.<br/>
	 * <b>-</b> Clothing (remember underwear and accessories).<br/>
	 */
	public void equipClothing(List<EquipClothingSetting> settings) {
		CharacterUtils.equipClothingFromOutfit(this, null, settings);
	}
	
	protected void resetBodyAfterVersion_2_10_5() {
		// Need to save and restore breast size/lactation from pregnancy changes.
		CupSize size = this.getBreastSize();
		float milkStorage = this.getBreastRawMilkStorageValue();
		float milkStored = this.getBreastRawStoredMilkValue();
		
		setStartingBody(true);
		equipClothing(EquipClothingSetting.getAllClothingSettings());
		
		this.setBreastSize(size.getMeasurement());
		this.setBreastMilkStorage((int) milkStorage);
		this.setBreastStoredMilk(milkStored);
	}
	
	@Override
	public Element saveAsXML(Element parentElement, Document doc) {
		Element properties = super.saveAsXML(parentElement, doc);
		
		Element npcSpecific = doc.createElement("npcSpecific");
		properties.appendChild(npcSpecific);

		CharacterUtils.createXMLElementWithValue(doc, npcSpecific, "lastTimeEncountered", String.valueOf(lastTimeEncountered));
		CharacterUtils.createXMLElementWithValue(doc, npcSpecific, "buyModifier", String.valueOf(buyModifier));
		CharacterUtils.createXMLElementWithValue(doc, npcSpecific, "sellModifier", String.valueOf(sellModifier));
		CharacterUtils.createXMLElementWithValue(doc, npcSpecific, "addedToContacts", String.valueOf(addedToContacts));

		Element valuesElement = doc.createElement("NPCValues");
		npcSpecific.appendChild(valuesElement);
		for(NPCFlagValue value : NPCFlagValues) {
			CharacterUtils.createXMLElementWithValue(doc, valuesElement, "NPCValue", value.toString());
		}

		if(genderPreference!=null) {
			CharacterUtils.createXMLElementWithValue(doc, npcSpecific, "genderPreference", String.valueOf(genderPreference));
			CharacterUtils.createXMLElementWithValue(doc, npcSpecific, "subspeciesPreference", String.valueOf(subspeciesPreference));
			CharacterUtils.createXMLElementWithValue(doc, npcSpecific, "raceStagePreference", String.valueOf(raceStagePreference));
		}
		
		return properties;
	}
	
	public abstract void loadFromXML(Element parentElement, Document doc, CharacterImportSetting... settings);
	
	public static void loadNPCVariablesFromXML(NPC npc, StringBuilder log, Element parentElement, Document doc, CharacterImportSetting... settings) {
		GameCharacter.loadGameCharacterVariablesFromXML(npc, log, parentElement, doc, settings);
		
		Element npcSpecificElement = (Element) parentElement.getElementsByTagName("npcSpecific").item(0);
		
		if(npcSpecificElement!=null) {
			npc.setLastTimeEncountered(Long.valueOf(((Element)npcSpecificElement.getElementsByTagName("lastTimeEncountered").item(0)).getAttribute("value")));

			Element e = (Element)npcSpecificElement.getElementsByTagName("lastTimeHadSex").item(0);
			if(e!=null) {
				npc.setLastTimeHadSex(Long.valueOf(e.getAttribute("value")), false);
			}
			
			e = (Element)npcSpecificElement.getElementsByTagName("lastTimeOrgasmed").item(0);
			if(e!=null) {
				npc.setLastTimeOrgasmed(Long.valueOf(e.getAttribute("value")));
			} else {
				npc.setLastTimeOrgasmed(npc.getLastTimeHadSex());
			}
			
			npc.setBuyModifier(Float.valueOf(((Element)npcSpecificElement.getElementsByTagName("buyModifier").item(0)).getAttribute("value")));
			npc.setSellModifier(Float.valueOf(((Element)npcSpecificElement.getElementsByTagName("sellModifier").item(0)).getAttribute("value")));
			npc.addedToContacts = (Boolean.valueOf(((Element)npcSpecificElement.getElementsByTagName("addedToContacts").item(0)).getAttribute("value")));
		
	
			NodeList npcValues = ((Element) npcSpecificElement.getElementsByTagName("NPCValues").item(0)).getElementsByTagName("NPCValue");
			for(int i = 0; i < npcValues.getLength(); i++){
				e = (Element) npcValues.item(i);
				try {
					npc.NPCFlagValues.add(NPCFlagValue.valueOf(e.getAttribute("value")));
				} catch(Exception ex) {
				}
			}
			
			try {
				npc.genderPreference = Gender.valueOf(((Element)npcSpecificElement.getElementsByTagName("genderPreference").item(0)).getAttribute("value"));
				npc.subspeciesPreference = Subspecies.valueOf(((Element)npcSpecificElement.getElementsByTagName("subspeciesPreference").item(0)).getAttribute("value"));
				npc.raceStagePreference = RaceStage.valueOf(((Element)npcSpecificElement.getElementsByTagName("raceStagePreference").item(0)).getAttribute("value"));
			} catch(Exception ex) {
			}
		}
	}
	
	public void resetSlaveFlags() {
		for(NPCFlagValue flag : NPCFlagValue.getSlaveFlags()) {
			NPCFlagValues.remove(flag);
		}
	}
	
	public void resetOccupantFlags() {
		for(NPCFlagValue flag : NPCFlagValue.getOccupantFlags()) {
			NPCFlagValues.remove(flag);
		}
	}
	
	/**
	 * Applies a daily update to this NPC, called at midnight. Usually used for traders resetting their inventories.
	 */
	public void dailyUpdate() {
	}
	
	/**
	 * Applies an hourly update to this NPC.
	 */
	public void hourlyUpdate() {
	}
	
	/**
	 * Applies an update to this NPC every time the game makes a turn.
	 */
	public void turnUpdate() {
	}
	
	public abstract void changeFurryLevel();
	
	public abstract DialogueNode getEncounterDialogue();
	
	public boolean isClothingStealable() {
		return false;
	}

	/**
	 * Check for this NPC's willingness to have clothing equipped on them.
	 */
	public Value<Boolean, String> isInventoryEquipAllowed(AbstractClothing clothing, InventorySlot slotToEquipTo) {
		if((this.isSlave() && this.getOwner().isPlayer()) || clothing.getClothingType().isCondom(slotToEquipTo)) {
			// Can always equip condoms onto anyone (as they have a getCondomEquipEffects() method to handle it)
			// Can also always equip anything onto owned slaves
			return new Value<>(true, ""); 
		}
		if(this.isUnique()) {
			return new Value<>(false, "As [npc.name] is a unique character, who is not your slave, you cannot force [npc.herHim] to wear the "+clothing.getName()+"!");
		}
		return new Value<>(true, "");
	}

	public String getPresentInTileDescription(boolean inHiding) {
		StringBuilder tileSB = new StringBuilder();
		
		if(!this.isRaceConcealed()) {		
			tileSB.append(
					UtilText.parse(this,
							"<p style='text-align:center;'>"
								+ "<i>"
									+ (this.isPlayerKnowsName()
											?"[npc.Name], [npc.a_femininity(true)] [npc.raceStage(true)] [npc.race(true)],"
											:"[npc.A_femininity(true)] [npc.raceStage(true)] [npc.race(true)]")
									+ " is "+(inHiding?"[style.boldBad(hiding)] in":"prowling")+" this area!"
								+ "</i>"
							+ "</p>"));
		} else {
			tileSB.append(
					UtilText.parse(this,
							"<p style='text-align:center;'>"
									+"<i>Someone, or something, is "+(inHiding?"[style.boldBad(hiding)] in":"prowling")+" this area!</i>"
							+ "</p>"
				));
		}
		
		// Combat:
		tileSB.append("<p style='text-align:center;'>");
		if(this.getFoughtPlayerCount()>0) {
			tileSB.append(
					UtilText.parse(this,"You have <b style='color:"+Colour.GENERIC_COMBAT.toWebHexString()+";'>fought</b> [npc.herHim] <b>"));
					
					if(this.getFoughtPlayerCount()==1) {
						tileSB.append("once.");
					} else if(this.getFoughtPlayerCount()==2) {
						tileSB.append("twice.");
					} else {
						tileSB.append(Util.intToString(this.getFoughtPlayerCount())+" times.");
					}
					
			tileSB.append("</b>"
							+ "<br/>"
							+ "You have <b style='color:"+Colour.GENERIC_GOOD.toWebHexString()+";'>won</b> <b>");
					
					if(this.getLostCombatCount()==1) {
						tileSB.append("once.");
					} else if(this.getLostCombatCount()==2) {
						tileSB.append("twice.");
					} else {
						tileSB.append(Util.intToString(this.getLostCombatCount())+" times.");
					}
							
			tileSB.append("</b>"
					+ "<br/>"
					+ "You have <b style='color:"+Colour.GENERIC_BAD.toWebHexString()+";'>lost</b> <b>");
					if(this.getWonCombatCount()==1) {
						tileSB.append("once.");
					} else if(this.getWonCombatCount()==2) {
						tileSB.append("twice.");
					} else {
						tileSB.append(Util.intToString(this.getWonCombatCount())+" times.");
					}
					tileSB.append("</b></p>");
		}
		
		// Sex:
		if(this.getSexPartners().containsKey(Main.game.getPlayer().getId())) {
			tileSB.append("<p style='text-align:center;'>");
					
			tileSB.append(
					UtilText.parse(this,
							"You have had <b style='color:"+Colour.GENERIC_SEX.toWebHexString()+";'>submissive sex</b> with [npc.herHim]<b> "));
			
					if(this.getSexAsDomCount(Main.game.getPlayer())==1) {
						tileSB.append("once.");
					} else if(this.getSexAsDomCount(Main.game.getPlayer())==2) {
						tileSB.append("twice.");
					} else {
						tileSB.append(Util.intToString(this.getSexAsDomCount(Main.game.getPlayer()))+" times.");
					}
					
			tileSB.append(
					UtilText.parse(this,
							"</b>"
							+ "<br/>"
							+ "You have had <b style='color:"+Colour.GENERIC_SEX.toWebHexString()+";'>dominant sex</b> with  [npc.herHim]<b> "));
			
					if(this.getSexAsSubCount(Main.game.getPlayer())==1) {
						tileSB.append("once.");
					} else if(this.getSexAsSubCount(Main.game.getPlayer())==2) {
						tileSB.append("twice.");
					} else {
						tileSB.append(Util.intToString(this.getSexAsSubCount(Main.game.getPlayer()))+" times.");
					}
					tileSB.append("</b></p>");
		}
		
		return tileSB.toString();
	}
	
	public String getPresentInTileDescription() {
		return getPresentInTileDescription(false);
	}
	

	public String getPlayerRelationStatusDescription() {
		StringBuilder sb = new StringBuilder();
		
		if(this.isRelatedTo(Main.game.getPlayer())) {
			sb.append("<p style='text-align:center;'><i>");
			AffectionLevel al = this.getAffectionLevel(Main.game.getPlayer());
			switch(al) {
				case NEGATIVE_FIVE_LOATHE:
				case NEGATIVE_FOUR_HATE:
				case NEGATIVE_THREE_STRONG_DISLIKE:
				case NEGATIVE_TWO_DISLIKE:
				case NEGATIVE_ONE_ANNOYED:
				case ZERO_NEUTRAL:
					break;
				case POSITIVE_ONE_FRIENDLY:
					if(this.isAttractedTo(Main.game.getPlayer()) && Main.game.isIncestEnabled()) {
						sb.append("[npc.Name] is acting in a <i style='color:"+al.getColour().toWebHexString()+";'>friendly</i>, [style.italicsSex(flirtatious)] manner towards you.");
					} else {
						sb.append("[npc.Name] is acting in a <i style='color:"+al.getColour().toWebHexString()+";'>friendly</i> manner towards you.");
					}
					break;
				case POSITIVE_TWO_LIKE:
					if(this.isAttractedTo(Main.game.getPlayer()) && Main.game.isIncestEnabled()) {
						sb.append("[npc.Name] quite clearly <i style='color:"+al.getColour().toWebHexString()+";'>likes you</i>, and sees you as [style.italicsSex(more than just [npc.her] [pc.relation(npc)])].");
					} else {
						sb.append("[npc.Name] quite clearly <i style='color:"+al.getColour().toWebHexString()+";'>likes you</i>, and is happy to have you as [npc.her] [pc.relation(npc)].");
					}
					break;
				case POSITIVE_THREE_CARING:
					if(this.isAttractedTo(Main.game.getPlayer()) && Main.game.isIncestEnabled()) {
						sb.append("[npc.Name] quite clearly <i style='color:"+al.getColour().toWebHexString()+";'>cares about you a lot</i>, and is [style.italicsSex(deeply attracted)] towards you.");
					} else {
						sb.append("[npc.Name] quite clearly <i style='color:"+al.getColour().toWebHexString()+";'>cares about you a lot</i>, and considers you to be the best [pc.relation(npc)] [npc.she] could ask for.");
					}
					break;
				case POSITIVE_FOUR_LOVE:
					if(this.isAttractedTo(Main.game.getPlayer()) && Main.game.isIncestEnabled()) {
						sb.append("You can tell from the way that [npc.she] looks at you that [npc.name] <i style='color:"+al.getColour().toWebHexString()+";'>loves you</i> in a [style.italicsSex(romantic)] manner.");
					} else {
						sb.append("You can tell that [npc.name] <i style='color:"+al.getColour().toWebHexString()+";'>loves you</i> as only [npc.a_relation(pc)] can.");
					}
					break;
				case POSITIVE_FIVE_WORSHIP:
					if(this.isAttractedTo(Main.game.getPlayer()) && Main.game.isIncestEnabled()) {
						sb.append("[npc.Name] <i style='color:"+al.getColour().toWebHexString()+";'>worships you</i>, and is [style.italicsSex(head-over-heels in love)] with you.");
					} else {
						sb.append("[npc.Name] <i style='color:"+al.getColour().toWebHexString()+";'>worships you</i>, and would do almost anything you asked of [npc.herHim].");
					}
					break;
			}

			if(Main.game.isIncestEnabled()) {
				sb.append("<br/>");
				if(this.isAttractedTo(Main.game.getPlayer())) {
					sb.append("You notice [npc.namePos] gaze flick down as [npc.she] tries to take an unnoticed peek at your body."
								+ " From the hungry look in [npc.her] [npc.eyes], [style.italicsSex(you can tell that [npc.sheIs] attracted to you)]...");
				} else {
					sb.append("[npc.Name] [style.italicsMinorBad(doesn't show any sign of being sexually attracted to you)], and any affection that [npc.she] shows is no doubt simply due to your [pc.mother]-[npc.daughter] relationship.");
				}
			}
			sb.append("</i></p>");
			
		} else {
			sb.append("<p style='text-align:center;'><i>");
			AffectionLevel al = this.getAffectionLevel(Main.game.getPlayer());
			switch(al) {
				case NEGATIVE_FIVE_LOATHE:
					if(this.isAttractedTo(Main.game.getPlayer())) {
						sb.append("Even though [npc.name] seems to <i style='color:"+al.getColour().toWebHexString()+";'>loathe</i> you, you can tell that [npc.sheIs] still attracted to you.");
					} else {
						sb.append("[npc.Name] seems to <i style='color:"+al.getColour().toWebHexString()+";'>loathe</i> you.");
					}
					break;
				case NEGATIVE_FOUR_HATE:
					if(this.isAttractedTo(Main.game.getPlayer())) {
						sb.append("Even though [npc.name] seems to <i style='color:"+al.getColour().toWebHexString()+";'>hate</i> you, you can tell that [npc.sheIs] still attracted to you.");
					} else {
						sb.append("[npc.Name] seems to <i style='color:"+al.getColour().toWebHexString()+";'>hate</i> you.");
					}
					break;
				case NEGATIVE_THREE_STRONG_DISLIKE:
					if(this.isAttractedTo(Main.game.getPlayer())) {
						sb.append("Even though [npc.name] seems to <i style='color:"+al.getColour().toWebHexString()+";'>strongly dislike</i> you, you can tell that [npc.sheIs] still attracted to you.");
					} else {
						sb.append("[npc.Name] seems to <i style='color:"+al.getColour().toWebHexString()+";'>strongly dislike</i> you.");
					}
					break;
				case NEGATIVE_TWO_DISLIKE:
					if(this.isAttractedTo(Main.game.getPlayer())) {
						sb.append("Even though [npc.name] seems to <i style='color:"+al.getColour().toWebHexString()+";'>dislike</i> you, you can tell that [npc.sheIs] still attracted to you.");
					} else {
						sb.append("[npc.Name] seems to <i style='color:"+al.getColour().toWebHexString()+";'>dislike</i> you.");
					}
					break;
				case NEGATIVE_ONE_ANNOYED:
					if(this.isAttractedTo(Main.game.getPlayer())) {
						sb.append("Even though [npc.name] seems to be <i style='color:"+al.getColour().toWebHexString()+";'>annoyed</i> with you, you can tell that [npc.sheIs] still attracted to you.");
					} else {
						sb.append("[npc.Name] seems to be <i style='color:"+al.getColour().toWebHexString()+";'>annoyed</i> with you.");
					}
					break;
				case ZERO_NEUTRAL:
					if(this.isAttractedTo(Main.game.getPlayer())) {
						sb.append("[npc.Name] is acting in an <i style='color:"+al.getColour().toWebHexString()+";'>amicable, flirtatious</i> manner towards you.");
					} else {
						sb.append("[npc.Name] is acting in an <i style='color:"+al.getColour().toWebHexString()+";'>amicable</i> manner towards you.");
					}
					break;
				case POSITIVE_ONE_FRIENDLY:
					if(this.isAttractedTo(Main.game.getPlayer())) {
						sb.append("[npc.Name] is acting in a <i style='color:"+al.getColour().toWebHexString()+";'>friendly, flirtatious</i> manner towards you.");
					} else {
						sb.append("[npc.Name] is acting in a <i style='color:"+al.getColour().toWebHexString()+";'>friendly</i> manner towards you.");
					}
					break;
				case POSITIVE_TWO_LIKE:
					if(this.isAttractedTo(Main.game.getPlayer())) {
						sb.append("[npc.Name] quite clearly <i style='color:"+al.getColour().toWebHexString()+";'>likes you</i>, and sees you as more than just a friend.");
					} else {
						sb.append("[npc.Name] quite clearly <i style='color:"+al.getColour().toWebHexString()+";'>likes you</i>, and sees you as a close friend.");
					}
					break;
				case POSITIVE_THREE_CARING:
					if(this.isAttractedTo(Main.game.getPlayer())) {
						sb.append("[npc.Name] quite clearly <i style='color:"+al.getColour().toWebHexString()+";'>cares about you a lot</i>, and is deeply attracted towards you.");
					} else {
						sb.append("[npc.Name] quite clearly <i style='color:"+al.getColour().toWebHexString()+";'>cares about you a lot</i>, and considers you to be [npc.her] best friend.");
					}
					break;
				case POSITIVE_FOUR_LOVE:
					if(this.isAttractedTo(Main.game.getPlayer())) {
						sb.append("You can tell from the way that [npc.she] looks at you that [npc.name] <i style='color:"+al.getColour().toWebHexString()+";'>loves you</i>.");
					} else {
						sb.append("You can tell that [npc.name] <i style='color:"+al.getColour().toWebHexString()+";'>loves you</i> in a purely platonic manner.");
					}
					break;
				case POSITIVE_FIVE_WORSHIP:
					if(this.isAttractedTo(Main.game.getPlayer())) {
						sb.append("[npc.Name] <i style='color:"+al.getColour().toWebHexString()+";'>worships you</i>, and is head-over-heels in love with you.");
					} else {
						sb.append("[npc.Name] <i style='color:"+al.getColour().toWebHexString()+";'>worships you</i>, and would do almost anything you asked of [npc.herHim].");
					}
					break;
			}
			sb.append("</i></p>");
		}
		
		return UtilText.parse(this, sb.toString());
	}
	
	// Trader:

	public String getTraderDescription() {
		return UtilText.parse(this,
				"<p>"
					+ "You have a look at what [npc.name] has for sale."
				+ "</p>");
	}

	public boolean isTrader() {
		return false;
	}

	public boolean willBuy(AbstractCoreItem item) {
		return false;
	}
	
	/**
	 * Handles any extra effects that need to be taken into account when selling an item to the player.
	 * @param item
	 */
	public void handleSellingEffects(AbstractCoreItem item, int count, int itemPrice) {
	}

	public float getBuyModifier() {
		return buyModifier;
	}

	public void setBuyModifier(float buyModifier) {
		this.buyModifier = buyModifier;
	}

	/**
	 * @param item The item which this NPC is selling. If selling a slave, pass in null.
	 * @return The sell price modifier of the passed in item as a percentage.
	 */
	public float getSellModifier(AbstractCoreItem item) {
		float base = sellModifier;
		if(item instanceof AbstractItem) {
			if(((AbstractItem)item).getItemType()==ItemType.PROMISCUITY_PILL) {
				base*=10;
			}
		}
		return Math.max(getBuyModifier(), (base * (Main.game.getPlayer().hasTrait(Perk.JOB_STUDENT, true)?0.75f:1)));
	}

	public void setSellModifier(float sellModifier) {
		this.sellModifier = sellModifier;
	}

	// Combat:
	
	private List<Spell> getSpellsAbleToCast() {
		List<Spell> spellsAbleToCast = new ArrayList<>();
		
		for(Spell spell : this.getAllSpells()) {
			if(this.getMana()>spell.getModifiedCost(this)) {
				if(this.isElemental()) {
					if(spell!=Spell.ELEMENTAL_AIR
							&& spell!=Spell.ELEMENTAL_ARCANE
							&& spell!=Spell.ELEMENTAL_EARTH
							&& spell!=Spell.ELEMENTAL_FIRE
							&& spell!=Spell.ELEMENTAL_WATER) {
						spellsAbleToCast.add(spell);
					}
					
				} else {
					spellsAbleToCast.add(spell);
				}
			}
		}
		
		return spellsAbleToCast;
	}
	
	/**
	 * @param target The character that his character is targeting in combat.
	 * @return A weighted map of spell -> weight.
	 */
	public Map<Spell, Integer> getWeightedSpellsAvailable(GameCharacter target) {
		Map<Spell, Integer> weightedSpellMap = new HashMap<>();
		
//		System.out.println(this.getName()+" "+target.getName()+": "+Combat.isOpponent(this, target));
		
		for(Spell spell : getSpellsAbleToCast()) {
			switch(spell) {
				// Basic offensive spells:
				case ARCANE_AROUSAL:
				case FIREBALL:
				case ICE_SHARD:
				case POISON_VAPOURS:
				case SLAM:
				case VACUUM:
					if(Combat.isOpponent(this, target)) {
						weightedSpellMap.put(spell, 1);
					}
					break;
					
				// Spells that are based on applying status-effects:
				case ARCANE_CLOUD:
					if(Combat.isOpponent(this, target)
							&& !target.hasStatusEffect(StatusEffect.ARCANE_CLOUD)
							&& !target.hasStatusEffect(StatusEffect.ARCANE_CLOUD_ARCANE_LIGHTNING)
							&& !target.hasStatusEffect(StatusEffect.ARCANE_CLOUD_ARCANE_THUNDER)
							&& !target.hasStatusEffect(StatusEffect.ARCANE_CLOUD_LOCALISED_STORM)) {
						weightedSpellMap.put(spell, 1);
					}
					break;
				case CLOAK_OF_FLAMES:
					if(!Combat.isOpponent(this, target)
							&& !target.hasStatusEffect(StatusEffect.CLOAK_OF_FLAMES)
							&& !target.hasStatusEffect(StatusEffect.CLOAK_OF_FLAMES_1)
							&& !target.hasStatusEffect(StatusEffect.CLOAK_OF_FLAMES_2)
							&& !target.hasStatusEffect(StatusEffect.CLOAK_OF_FLAMES_3)) {
						weightedSpellMap.put(spell, 1);
					}
					break;
				case PROTECTIVE_GUSTS:
					if(!Combat.isOpponent(this, target)
							&& !target.hasStatusEffect(StatusEffect.PROTECTIVE_GUSTS)
							&& !target.hasStatusEffect(StatusEffect.PROTECTIVE_GUSTS_FOCUSED_BLAST)
							&& !target.hasStatusEffect(StatusEffect.PROTECTIVE_GUSTS_GUIDING_WIND)) {
						weightedSpellMap.put(spell, 1);
					}
					break;
				case RAIN_CLOUD:
					if(Combat.isOpponent(this, target)
							&& !target.hasStatusEffect(StatusEffect.RAIN_CLOUD)
							&& !target.hasStatusEffect(StatusEffect.RAIN_CLOUD_CLOUDBURST)
							&& !target.hasStatusEffect(StatusEffect.RAIN_CLOUD_DEEP_CHILL)
							&& !target.hasStatusEffect(StatusEffect.RAIN_CLOUD_DOWNPOUR)
							&& !target.hasStatusEffect(StatusEffect.RAIN_CLOUD_DOWNPOUR_FOR_CLOUDBURST)) {
						weightedSpellMap.put(spell, 1);
					}
					break;
				case SOOTHING_WATERS:
					if(!Combat.isOpponent(this, target) && target.getHealthPercentage()<0.8f) {
						weightedSpellMap.put(spell, (int) (1-(target.getHealthPercentage()*10))/2);
					}
					break;
				case STONE_SHELL:
					if(!Combat.isOpponent(this, target)
							&& !target.hasStatusEffect(StatusEffect.STONE_SHELL)
							&& !target.hasStatusEffect(StatusEffect.STONE_SHELL_EXPLOSIVE_FINISH)
							&& !target.hasStatusEffect(StatusEffect.STONE_SHELL_HARDENED_CARAPACE)
							&& !target.hasStatusEffect(StatusEffect.STONE_SHELL_SHIFTING_SANDS)) {
						weightedSpellMap.put(spell, 1);
					}
					break;
				case TELEKENETIC_SHOWER:
					if(Combat.isOpponent(this, target)
							&& !target.hasStatusEffect(StatusEffect.TELEKENETIC_SHOWER)
							&& !target.hasStatusEffect(StatusEffect.TELEKENETIC_SHOWER_PRECISION_STRIKES)
							&& !target.hasStatusEffect(StatusEffect.TELEKENETIC_SHOWER_UNSEEN_FORCE)) {
						weightedSpellMap.put(spell, 1);
					}
					break;
				case TELEPATHIC_COMMUNICATION:
					if(!Combat.isOpponent(this, target)
							&& !target.hasStatusEffect(StatusEffect.TELEPATHIC_COMMUNICATION)
							&& !target.hasStatusEffect(StatusEffect.TELEPATHIC_COMMUNICATION_POWER_OF_SUGGESTION)
							&& !target.hasStatusEffect(StatusEffect.TELEPATHIC_COMMUNICATION_POWER_OF_SUGGESTION_TARGETED)
							&& !target.hasStatusEffect(StatusEffect.TELEPATHIC_COMMUNICATION_PROJECTED_TOUCH)) {
						weightedSpellMap.put(spell, 1);
					}
					break;
				case TELEPORT:
					if(!Combat.isOpponent(this, target)
							&& !target.hasStatusEffect(StatusEffect.TELEPORT)
							&& !target.hasStatusEffect(StatusEffect.TELEPORT_ARCANE_ARRIVAL)) {
						weightedSpellMap.put(spell, 1);
					}
					break;
				case WITCH_CHARM:
					if(!Combat.isOpponent(this, target)
							&& !target.hasStatusEffect(StatusEffect.WITCH_CHARM)) {
						weightedSpellMap.put(spell, 1);
					}
					break;
					
				// Stuns:
				case FLASH:
				case WITCH_SEAL:
					if(Combat.isOpponent(this, target) && !target.isStunned()) {
						weightedSpellMap.put(spell, 1);
					}
					break;
					
				// Special condition spells:
				case CLEANSE:
					//TODO need to check for enemy & positive SEs, or ally & negative SEs
//					if(Combat.isOpponent(this, target)) {
//						
//					}
					break;
				case LILITHS_COMMAND:
					// TODO
					weightedSpellMap.put(spell, 1);
					break;
				case STEAL:
					// TODO
					weightedSpellMap.put(spell, 1);
					break;
					
				// Elementals:
				case ELEMENTAL_AIR:
				case ELEMENTAL_ARCANE:
				case ELEMENTAL_EARTH:
				case ELEMENTAL_FIRE:
				case ELEMENTAL_WATER:
					if(!(this.isElemental()) && !this.isElementalSummoned()) {
						weightedSpellMap.put(spell, 1);
					}
					break;
				// Spells that should not be used:
				// TODO
				case DARK_SIREN_SIRENS_CALL:
				case LIGHTNING_SPHERE_DISCHARGE:
				case LIGHTNING_SPHERE_OVERCHARGE:
					break;
			}
		}
		
		return weightedSpellMap;
	}
	
	/**
	 * Handles the behaviour when the player escapes from this enemy in combat.
	 */
	public void applyEscapeCombatEffects() {
	};
	
	public Response endCombat(boolean applyEffects, boolean playerVictory) {
		return null;
	};

	/**
	 * If this character has special scenes which interrupt combat at a certain point, then use this method to add them.
	 * (It is called at the end of every combat turn, and if it returns non-null values, it returns them as interrupting responses.)
	 */
	public Response interruptCombatSpecialCase() {
		return null;
	};
	
	public int getEscapeChance() {
		return 30;
	}

	public boolean isSurrendersAtZeroMana() {
		return true;
	}

	// Post-combat:

	public int getExperienceFromVictory() {
		return getLevel() * 2;
	}

	public int getLootMoney() {
		return (int) ((getLevel() * 25) * (1 + Math.random() - 0.5f));
	}
	
	public List<AbstractCoreItem> getLootItems() {
		double rnd = Math.random();
		
		if(rnd<=0.05) {
			return Util.newArrayListOfValues(AbstractItemType.generateItem(ItemType.FETISH_UNREFINED));
			
		} else if(rnd<=0.1) {
			return Util.newArrayListOfValues(AbstractItemType.generateItem(ItemType.ADDICTION_REMOVAL));
			
		} else {
			AbstractItemType raceIngredient = ItemType.INT_INGREDIENT_VANILLA_WATER;
			AbstractItemType raceTFIngredient = ItemType.RACE_INGREDIENT_HUMAN;
			AbstractItemType book = ItemType.getLoreBook(getSubspecies());
			
			switch(getSubspecies()) {
				case CAT_MORPH:
				case CAT_MORPH_CARACAL:
				case CAT_MORPH_CHEETAH:
				case CAT_MORPH_LEOPARD:
				case CAT_MORPH_LEOPARD_SNOW:
				case CAT_MORPH_LION:
				case CAT_MORPH_LYNX:
				case CAT_MORPH_TIGER:
					raceIngredient = ItemType.INT_INGREDIENT_FELINE_FANCY;
					raceTFIngredient = ItemType.RACE_INGREDIENT_CAT_MORPH;
					break;
					
				case COW_MORPH:
					raceIngredient = ItemType.STR_INGREDIENT_BUBBLE_MILK;
					raceTFIngredient = ItemType.RACE_INGREDIENT_COW_MORPH;
					break;
					
				case DOG_MORPH:
				case DOG_MORPH_BORDER_COLLIE:
				case DOG_MORPH_DOBERMANN:
				case DOG_MORPH_GERMAN_SHEPHERD:
					raceIngredient = ItemType.FIT_INGREDIENT_CANINE_CRUSH;
					raceTFIngredient = ItemType.RACE_INGREDIENT_DOG_MORPH;
					break;
					
				case FOX_MORPH:
				case FOX_ASCENDANT:
				case FOX_ASCENDANT_ARCTIC:
				case FOX_ASCENDANT_FENNEC:
				case FOX_MORPH_FENNEC:
				case FOX_MORPH_ARCTIC:
					raceIngredient = ItemType.INT_INGREDIENT_GRAPE_JUICE;
					raceTFIngredient = ItemType.RACE_INGREDIENT_FOX_MORPH;
					break;
					
				case HORSE_MORPH:
				case HORSE_MORPH_UNICORN:
				case HORSE_MORPH_PEGASUS:
				case HORSE_MORPH_ALICORN:
				case CENTAUR:
				case PEGATAUR:
				case ALITAUR:
				case UNITAUR:
				case HORSE_MORPH_ZEBRA:
					raceIngredient = ItemType.STR_INGREDIENT_EQUINE_CIDER;
					raceTFIngredient = ItemType.RACE_INGREDIENT_HORSE_MORPH;
					break;
					
				case REINDEER_MORPH:
					raceIngredient = ItemType.FIT_INGREDIENT_EGG_NOG;
					raceTFIngredient = ItemType.RACE_INGREDIENT_REINDEER_MORPH;
					break;
					
				case WOLF_MORPH:
					raceIngredient = ItemType.STR_INGREDIENT_WOLF_WHISKEY;
					raceTFIngredient = ItemType.RACE_INGREDIENT_WOLF_MORPH;
					break;
					
				case HUMAN:
					raceIngredient = ItemType.INT_INGREDIENT_VANILLA_WATER;
					raceTFIngredient = ItemType.RACE_INGREDIENT_HUMAN;
					break;
					
				case ANGEL:
					book = ItemType.DYE_BRUSH; //TODO
					raceIngredient = ItemType.DYE_BRUSH;
					raceTFIngredient = ItemType.RACE_INGREDIENT_HUMAN;
					break;
					
				case DEMON:
				case HALF_DEMON:
				case LILIN:
				case ELDER_LILIN:
					raceIngredient = ItemType.COR_INGREDIENT_LILITHS_GIFT;
					raceTFIngredient = null;
					break;
					
				case IMP:
				case IMP_ALPHA:
					raceIngredient = ItemType.COR_INGREDIENT_IMPISH_BREW;
					raceTFIngredient = ItemType.COR_INGREDIENT_IMPISH_BREW;
					break;
					
				case HARPY:
				case HARPY_BALD_EAGLE:
				case HARPY_RAVEN:
				case HARPY_PHOENIX:
					raceIngredient = ItemType.SEX_INGREDIENT_HARPY_PERFUME;
					raceTFIngredient = ItemType.RACE_INGREDIENT_HARPY;
					break;
					
				case ALLIGATOR_MORPH:
					raceIngredient = ItemType.STR_INGREDIENT_SWAMP_WATER;
					raceTFIngredient = ItemType.RACE_INGREDIENT_ALLIGATOR_MORPH;
					break;
					
				case SQUIRREL_MORPH:
					raceIngredient = ItemType.FIT_INGREDIENT_SQUIRREL_JAVA;
					raceTFIngredient = ItemType.RACE_INGREDIENT_SQUIRREL_MORPH;
					break;
					
				case BAT_MORPH:
					raceIngredient = ItemType.INT_INGREDIENT_FRUIT_BAT_SQUASH;
					raceTFIngredient = ItemType.RACE_INGREDIENT_BAT_MORPH;
					break;
					
				case RAT_MORPH:
					raceIngredient = ItemType.STR_INGREDIENT_BLACK_RATS_RUM;
					raceTFIngredient = ItemType.RACE_INGREDIENT_RAT_MORPH;
					break;
					
				case RABBIT_MORPH:
				case RABBIT_MORPH_LOP:
					raceIngredient = ItemType.SEX_INGREDIENT_BUNNY_JUICE;
					raceTFIngredient = ItemType.RACE_INGREDIENT_RABBIT_MORPH;
					break;
					
				case ELEMENTAL_AIR:
					book = ItemType.getIdToItemMap().get("SPELL_SCROLL_"+SpellSchool.AIR);
					raceIngredient = ItemType.getIdToItemMap().get("SPELL_SCROLL_"+SpellSchool.AIR);
					raceTFIngredient = ItemType.getIdToItemMap().get("SPELL_SCROLL_"+SpellSchool.AIR);
					break;
					
				case ELEMENTAL_ARCANE:
					book = ItemType.getIdToItemMap().get("SPELL_SCROLL_"+SpellSchool.ARCANE);
					raceIngredient = ItemType.getIdToItemMap().get("SPELL_SCROLL_"+SpellSchool.ARCANE);
					raceTFIngredient = ItemType.getIdToItemMap().get("SPELL_SCROLL_"+SpellSchool.ARCANE);
					break;
					
				case ELEMENTAL_EARTH:
					book = ItemType.getIdToItemMap().get("SPELL_SCROLL_"+SpellSchool.EARTH);
					raceIngredient = ItemType.getIdToItemMap().get("SPELL_SCROLL_"+SpellSchool.EARTH);
					raceTFIngredient = ItemType.getIdToItemMap().get("SPELL_SCROLL_"+SpellSchool.EARTH);
					break;
					
				case ELEMENTAL_FIRE:
					book = ItemType.getIdToItemMap().get("SPELL_SCROLL_"+SpellSchool.FIRE);
					raceIngredient = ItemType.getIdToItemMap().get("SPELL_SCROLL_"+SpellSchool.FIRE);
					raceTFIngredient = ItemType.getIdToItemMap().get("SPELL_SCROLL_"+SpellSchool.FIRE);
					break;
					
				case ELEMENTAL_WATER:
					book = ItemType.getIdToItemMap().get("SPELL_SCROLL_"+SpellSchool.WATER);
					raceIngredient = ItemType.getIdToItemMap().get("SPELL_SCROLL_"+SpellSchool.WATER);
					raceTFIngredient = ItemType.getIdToItemMap().get("SPELL_SCROLL_"+SpellSchool.WATER);
					break;
					
				case SLIME:
					raceIngredient = ItemType.SEX_INGREDIENT_SLIME_QUENCHER;
					if(this.hasFetish(Fetish.FETISH_TRANSFORMATION_GIVING)) {
						raceTFIngredient = ItemType.RACE_INGREDIENT_SLIME;
					} else {
						raceTFIngredient =  ItemType.SEX_INGREDIENT_SLIME_QUENCHER;
					}
					break;
					
			}
			
			if(rnd<0.6 && raceTFIngredient!=null) {
				return Util.newArrayListOfValues(AbstractItemType.generateItem(raceTFIngredient));
			
			} else if(rnd <= 0.8 && !Main.game.getPlayer().getRacesDiscoveredFromBook().contains(getSubspecies())) {
				return Util.newArrayListOfValues(AbstractItemType.generateItem(book));
				
			} else {
				return Util.newArrayListOfValues(AbstractItemType.generateItem(raceIngredient));
				
			}
		}
	}
	
	public Map<TFEssence, Integer> getLootEssenceDrops() {
		return Util.newHashMapOfValues(new Value<>(TFEssence.ARCANE, Util.random.nextInt(this.getLevel())+1));
	}
	
	
	// Relationships:
	
	public float getHourlyAffectionChange(int hour) {
		SlaveJob job = this.getSlaveJob(hour);
		
		// Rounding is to get rid of floating point ridiculousness (e.g. 2.3999999999999999999999):
		if(this.getSlaveJob(hour)==SlaveJob.IDLE) {
			return Math.round(this.getHomeLocationPlace().getHourlyAffectionChange()*100)/100f;
		} else {
			return Math.round(job.getAffectionGain(hour, this)*100)/100f;
		}
	}
	
	public float getDailyAffectionChange() {
		float totalAffectionChange = 0;
		
		for (int hour = 0; hour < 24; hour++) {
			SlaveJob job = this.getSlaveJob(hour);
			if(this.getSlaveJob(hour)==SlaveJob.IDLE) {
				totalAffectionChange += this.getHomeLocationPlace().getHourlyAffectionChange();
			} else {
				totalAffectionChange += job.getAffectionGain(hour, this);
			}
		}

		// Rounding is to get rid of floating point ridiculousness (e.g. 2.3999999999999999999999):
		return Math.round(totalAffectionChange*100)/100f;
	}
	
	
	// Misc:

	/**
	 * By default, NPCs can't be impregnated.
	 * 
	 * @return
	 */
	@Override
	public boolean isAbleToBeImpregnated() {
		return false;
	}

	public boolean hasFlag(NPCFlagValue flag) {
		return NPCFlagValues.contains(flag);
	}
	
	public boolean addFlag(NPCFlagValue flag) {
		return NPCFlagValues.add(flag);
	}
	
	public boolean removeFlag(NPCFlagValue flag) {
		return NPCFlagValues.remove(flag);
	}
	
	public boolean setFlag(NPCFlagValue flag, boolean value) {
		if(value) {
			return addFlag(flag);
		} else {
			return removeFlag(flag);
		}
	}
	
	public boolean isKnowsPlayerGender() {
		return NPCFlagValues.contains(NPCFlagValue.knowsPlayerGender);
	}

	public void setKnowsPlayerGender(boolean knowsPlayerGender) {
		if(knowsPlayerGender) {
			NPCFlagValues.add(NPCFlagValue.knowsPlayerGender);
		} else {
			NPCFlagValues.remove(NPCFlagValue.knowsPlayerGender);
		}
	}
	
	public boolean isIntroducedToPlayer() {
		return NPCFlagValues.contains(NPCFlagValue.introducedToPlayer);
	}

	public void setIntroducedToPlayer(boolean introducedToPlayer) {
		if(introducedToPlayer) {
			NPCFlagValues.add(NPCFlagValue.introducedToPlayer);
		} else {
			NPCFlagValues.remove(NPCFlagValue.introducedToPlayer);
		}
	}
	
	public boolean isPendingClothingDressing() {
		return NPCFlagValues.contains(NPCFlagValue.pendingClothingDressing);
	}
	public void setPendingClothingDressing(boolean pendingClothingDressing) {
		if(pendingClothingDressing) {
			NPCFlagValues.add(NPCFlagValue.pendingClothingDressing);
		} else {
			NPCFlagValues.remove(NPCFlagValue.pendingClothingDressing);
		}
	}
	
	public boolean isPendingTransformationToGenderIdentity() {
		return this.getGender()!=this.getGenderIdentity()
				&& !(this.isElemental())
				&& !this.isPregnant()
				&& !this.isUnique()
				&& !this.isSlave()
				&& !Main.game.getPlayer().getFriendlyOccupants().contains(this.getId())
				&& this.isAbleToSelfTransform();
	}
	
	/**
	 * Resets this character's body to align with their gender identity.
	 * @param completeReset True if you want them to completely regenerate a new body. False if you just want femininity, breasts, and genitals altered.
	 */
	public void setBodyToGenderIdentity(boolean completeReset) {
		if(completeReset) {
			boolean assVirgin = this.isAssVirgin();
			boolean faceVirgin = this.isFaceVirgin();
			boolean nippleVirgin = this.isNippleVirgin();
			boolean penisVirgin = this.isPenisVirgin();
			boolean urethraVirgin = this.isUrethraVirgin();
			boolean vaginaVirgin = this.isVaginaVirgin();
			boolean vaginaUrethraVirgin = this.isVaginaUrethraVirgin();
			
			BodyMaterial material = this.getBodyMaterial();
			this.setBody(this.getGenderIdentity(), Subspecies.getFleshSubspecies(this), this.getBody().getRaceStageFromPartWeighting(), false);
			this.setBodyMaterial(material);
			CharacterUtils.randomiseBody(this, false);
			
			this.setAssVirgin(assVirgin);
			this.setFaceVirgin(faceVirgin);
			this.setNippleVirgin(nippleVirgin);
			this.setPenisVirgin(penisVirgin);
			this.setUrethraVirgin(urethraVirgin);
			this.setVaginaVirgin(vaginaVirgin);
			this.setVaginaUrethraVirgin(vaginaUrethraVirgin);
			
		} else {
			AbstractRacialBody racialBody = RacialBody.valueOfRace(Subspecies.getFleshSubspecies(this).getRace());
			if(this.getGenderIdentity().getType()==PronounType.FEMININE) {
				this.setFemininity(racialBody.getFemaleFemininity());
				
			} else if(this.getGenderIdentity().getType()==PronounType.NEUTRAL) {
				this.setFemininity(50);
				
			} else {
				this.setFemininity(racialBody.getMaleFemininity());
			}
			
			if(this.getGenderIdentity().getGenderName().isHasBreasts()) {
				this.setBreastSize(racialBody.getBreastSize());
			} else {
				this.setBreastSize(racialBody.getNoBreastSize());
			}
			
			boolean largeGenitals = this.isTaur();
			if(this.getGenderIdentity().getGenderName().isHasPenis()) {
				this.setPenisType(racialBody.getPenisType());
				this.setPenisSize((int) (racialBody.getPenisSize()*(largeGenitals?2.5f:1)));
				this.setPenisGirth(racialBody.getPenisGirth()+(largeGenitals?1:0));
				this.setPenisCumStorage(racialBody.getCumProduction()*(largeGenitals?10:1));
				this.setTesticleSize(racialBody.getTesticleSize()+(largeGenitals?1:0));
				this.setTesticleCount(racialBody.getTesticleQuantity());
			} else {
				this.setPenisType(PenisType.NONE);
			}
			
			if(this.getGenderIdentity().getGenderName().isHasVagina()) {
				this.setVaginaType(racialBody.getVaginaType());
				this.setVaginaWetness(racialBody.getVaginaWetness());
			} else {
				this.setVaginaType(VaginaType.NONE);
			}
		}
	}
	
	public long getLastTimeEncountered() {
		return lastTimeEncountered;
	}

	public void setLastTimeEncountered(long minutesPassed) {
		this.lastTimeEncountered = minutesPassed;
	}

	public boolean isAddedToContacts() {
		return addedToContacts;
	}
	
	public boolean isUsingForcedTransform(GameCharacter target) {
		return (hasFetish(Fetish.FETISH_TRANSFORMATION_GIVING) || hasFetish(Fetish.FETISH_KINK_GIVING))
				&& target.getRace()!=Race.ELEMENTAL // Do not try to transform elementals
				&& target.getSubspeciesOverride()==null; // Do not try to transform demons
	}
	
	public boolean isUsingForcedFetish(GameCharacter target) {
		return hasFetish(Fetish.FETISH_KINK_GIVING);
	}
	
	public String getPreferredBodyDescription(String tag) {
		// If preference is demon, just do gender
		boolean cannotTransformPreference = getSubspeciesPreference().getRace()==Race.DEMON;
		
		return "<"+tag+" style='color:"+getGenderPreference().getColour().toWebHexString()+";'>"+getGenderPreference().getName()+"</"+tag+">"
				+ (cannotTransformPreference
						?""
						:" <"+tag+" style='color:"+getSubspeciesPreference().getColour(null).toWebHexString()+";'>"+getSubspeciesPreference().getName(null)+"</"+tag+">");
	}
	
	public Gender getGenderPreference() {
		if(genderPreference == null) {
			generatePartnerPreferences();
		}
		return genderPreference;
	}
	
	public Subspecies getSubspeciesPreference() {
		if(subspeciesPreference == null) {
			generatePartnerPreferences();
		}
		return subspeciesPreference;
	}
	
	public RaceStage getRaceStagePreference() {
		if(raceStagePreference == null) {
			generatePartnerPreferences();
		}
		return raceStagePreference;
	}
	
	public boolean isAffectionHighEnoughToInviteHome() {
		if(this.isRelatedTo(Main.game.getPlayer())) {
			return this.getAffection(Main.game.getPlayer())>=AffectionLevel.NEGATIVE_TWO_DISLIKE.getMinimumValue();
		} else {
			return this.getAffection(Main.game.getPlayer())>=AffectionLevel.POSITIVE_THREE_CARING.getMinimumValue();
		}
	}

	public boolean isAllowingPlayerToManageInventory() {
		return Main.game.getPlayer().getFriendlyOccupants().contains(this.getId()) || (this.isSlave() && this.getOwner().isPlayer());
	}
	
	public Value<AbstractItemType, Map<ItemEffect, String>> generateTransformativePotion(GameCharacter target) {
		Map<ItemEffect, String> possibleEffects = new HashMap<>();
		AbstractItemType itemType = ItemType.RACE_INGREDIENT_HUMAN;
		int numberOfTransformations = (2+Util.random.nextInt(4)) * (target.hasFetish(Fetish.FETISH_TRANSFORMATION_RECEIVING)?2:1);
		boolean cannotTransformPreference = getSubspeciesPreference().getRace()==Race.DEMON;
		
		if(this.getSubspeciesPreference()==Subspecies.SLIME && target.getBodyMaterial()!=BodyMaterial.SLIME) {
			possibleEffects.put(new ItemEffect(ItemEffectType.RACE_BIOJUICE, TFModifier.NONE, TFModifier.NONE, TFPotency.MINOR_BOOST, 1),
							"You're going to love being a slime!");
			 return new Value<>(itemType, possibleEffects);
		}
		
		if(Main.getProperties().getForcedTFPreference() != FurryPreference.HUMAN) {
			switch(cannotTransformPreference
					?target.getSubspecies()
					:getSubspeciesPreference()) {
				case CAT_MORPH:
				case CAT_MORPH_CARACAL:
				case CAT_MORPH_CHEETAH:
				case CAT_MORPH_LEOPARD:
				case CAT_MORPH_LEOPARD_SNOW:
				case CAT_MORPH_LION:
				case CAT_MORPH_LYNX:
				case CAT_MORPH_TIGER:
					itemType = ItemType.RACE_INGREDIENT_CAT_MORPH;
					break;
				case DOG_MORPH:
				case DOG_MORPH_BORDER_COLLIE:
				case DOG_MORPH_DOBERMANN:
				case DOG_MORPH_GERMAN_SHEPHERD:
					itemType = ItemType.RACE_INGREDIENT_DOG_MORPH;
					break;
				case FOX_MORPH:
				case FOX_ASCENDANT:
				case FOX_ASCENDANT_ARCTIC:
				case FOX_ASCENDANT_FENNEC:
				case FOX_MORPH_FENNEC:
				case FOX_MORPH_ARCTIC:
					itemType = ItemType.RACE_INGREDIENT_FOX_MORPH;
					break;
				case HARPY:
				case HARPY_BALD_EAGLE:
				case HARPY_RAVEN:
				case HARPY_PHOENIX:
					itemType = ItemType.RACE_INGREDIENT_HARPY;
					break;
				case HORSE_MORPH:
				case HORSE_MORPH_UNICORN:
				case HORSE_MORPH_PEGASUS:
				case HORSE_MORPH_ALICORN:
				case CENTAUR:
				case PEGATAUR:
				case ALITAUR:
				case UNITAUR:
				case HORSE_MORPH_ZEBRA:
					itemType = ItemType.RACE_INGREDIENT_HORSE_MORPH;
					break;
				case REINDEER_MORPH:
					itemType = ItemType.RACE_INGREDIENT_REINDEER_MORPH;
					break;
				case SQUIRREL_MORPH:
					itemType = ItemType.RACE_INGREDIENT_SQUIRREL_MORPH;
					break;
				case WOLF_MORPH:
					itemType = ItemType.RACE_INGREDIENT_WOLF_MORPH;
					break;
				case ALLIGATOR_MORPH:
					itemType = ItemType.RACE_INGREDIENT_ALLIGATOR_MORPH;
					break;
				case COW_MORPH:
					itemType = ItemType.RACE_INGREDIENT_COW_MORPH;
					break;
				case RAT_MORPH:
					itemType = ItemType.RACE_INGREDIENT_RAT_MORPH;
					break;
				case BAT_MORPH:
					itemType = ItemType.RACE_INGREDIENT_BAT_MORPH;
					break;
				case RABBIT_MORPH:
				case RABBIT_MORPH_LOP:
					itemType = ItemType.RACE_INGREDIENT_RABBIT_MORPH;
					break;
				case ANGEL:
				case HALF_DEMON:
				case DEMON:
				case LILIN:
				case ELDER_LILIN:
				case IMP:
				case IMP_ALPHA:
				case HUMAN:
				case SLIME:
				case ELEMENTAL_AIR:
				case ELEMENTAL_ARCANE:
				case ELEMENTAL_EARTH:
				case ELEMENTAL_FIRE:
				case ELEMENTAL_WATER:
					itemType = ItemType.RACE_INGREDIENT_HUMAN;
					break;
			}
		}
		
		AbstractItemType genitalsItemType = itemType;
		boolean skipGenitalsTF = false;
		
		Body body = CharacterUtils.generateBody(null, this.getGenderPreference(), this.getSubspeciesPreference(), this.getRaceStagePreference());

		boolean vaginaSet = target.getVaginaType()==body.getVagina().getType();
		boolean penisSet = target.getPenisType()==body.getPenis().getType();
		boolean humanGenitals = false;
		
		if(Main.getProperties().getForcedTFPreference()==FurryPreference.HUMAN || Main.getProperties().getForcedTFPreference()==FurryPreference.MINIMUM) {
			humanGenitals = true;
			genitalsItemType = ItemType.RACE_INGREDIENT_HUMAN;
			
			vaginaSet = body.getVagina().getType()!=VaginaType.NONE == target.hasVagina();
			penisSet = body.getPenis().getType()!=PenisType.NONE == target.hasPenisIgnoreDildo();
			
			skipGenitalsTF = vaginaSet && penisSet;
		}
		
		// Order of transformation preferences are: Sexual organs -> minor parts -> Legs & arms -> Face & skin 
		
		if(!skipGenitalsTF) {
			// Sexual transformations:
			if(!vaginaSet) {
				if(body.getVagina().getType()==VaginaType.NONE) {
					if(!target.isHasAnyPregnancyEffects()) { // Vagina cannot be transformed if pregnant, so skip this
						possibleEffects.put(new ItemEffect(genitalsItemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.REMOVAL, TFPotency.MINOR_BOOST, 1),
								"Say goodbye to your cunt; you're not going to be needing it anymore!");
					}
					
				} else {
					possibleEffects.put(new ItemEffect(genitalsItemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.NONE, TFPotency.MINOR_BOOST, 1),
							"Let's give you a nice "+(humanGenitals?"human":body.getVagina().getType().getTransformName())+" "+body.getVagina().getName(target, false)+"!");
					if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
				}
			}
			
			if(!penisSet) {
				if(body.getPenis().getType()==PenisType.NONE) {
					possibleEffects.put(new ItemEffect(genitalsItemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.REMOVAL, TFPotency.MINOR_BOOST, 1),
							"It's time to get rid of that cock of yours!");
					if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
					
				} else {
					possibleEffects.put(new ItemEffect(genitalsItemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.NONE, TFPotency.MINOR_BOOST, 1),
							"Let's give you a nice "+(humanGenitals?"human":body.getPenis().getType().getTransformName())+" "+body.getPenis().getName(target, false)+"!");
					if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
				}
			}
		}
		
		
		// All minor part transformations:
		if(Main.getProperties().getForcedTFPreference()!=FurryPreference.HUMAN && !cannotTransformPreference) {
			if(possibleEffects.isEmpty() || Math.random()>0.33f) {
				if(target.getAntennaType() != body.getAntenna().getType()) {
					possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_ANTENNA, TFModifier.NONE, TFPotency.MINOR_BOOST, 1),
							body.getAntenna().getType()==AntennaType.NONE
								?UtilText.parse(target, "I don't want you having those [npc.antennae] anymore!")
								:"Time to give you some antennae!");//TODO
					if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
				}
				if(Main.getProperties().getForcedTFPreference() != FurryPreference.MINIMUM) {
					if(target.getAssType() != body.getAss().getType()) {
						possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_ASS, TFModifier.NONE, TFPotency.MINOR_BOOST, 1),
								"Let's transform your ass!");
						if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
					}
					if(target.getBreastType() != body.getBreast().getType()) {
						possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_BREASTS, TFModifier.NONE, TFPotency.MINOR_BOOST, 1),
								"Your breasts need to be transformed as well!");
						if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
					}
				}
				if(target.getEarType() != body.getEar().getType()) {
					possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_EARS, TFModifier.NONE, TFPotency.MINOR_BOOST, 1),
							"Your ears could use some improvement!");
					if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
				}
				if(target.getEyeType() != body.getEye().getType()) {
					possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_EYES, TFModifier.NONE, TFPotency.MINOR_BOOST, 1),
							"Now for your eyes to be transformed!");
					if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
				}
				if(target.getHairType() != body.getHair().getType()) {
					possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_HAIR, TFModifier.NONE, TFPotency.MINOR_BOOST, 1),
							"This might tingle a little!");
					if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
				}
				if(target.getHornType() != body.getHorn().getType()) {
					possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_HORNS, TFModifier.NONE, TFPotency.MINOR_BOOST, 1),
							body.getHorn().getType()==HornType.NONE
								?"Let's get rid of those horns of yours..."
								:"Ready to grow some new horns?");
					if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
				}
				if(target.getTailType() != body.getTail().getType()
						&& target.getTailType()!=TailType.FOX_MORPH_MAGIC
						&& body.getTail().getType()!=TailType.FOX_MORPH_MAGIC) {
					possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_TAIL, TFModifier.NONE, TFPotency.MINOR_BOOST, 1), 
							body.getTail().getType()==TailType.NONE
								?"That tail of yours is only getting in the way!"
								:"Time to get a new tail!");
					if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
				}
				if(target.getWingType() != body.getWing().getType()) {
					possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_WINGS, TFModifier.NONE, TFPotency.MINOR_BOOST, 1),
							body.getWing().getType()==WingType.NONE
								?"Let's get rid of those wings of yours..."
								:"Ready to grow some new wings?");
					if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
				}
			}
			
			// Leg & Arm transformations:
			if(Main.getProperties().getForcedTFPreference() != FurryPreference.MINIMUM) {
				if(possibleEffects.isEmpty()) {
					if(target.getArmType() != body.getArm().getType()) {
						possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_ARMS, TFModifier.NONE, TFPotency.MINOR_BOOST, 1),
								"Your arms could do with a change!");
					}
					if(target.getLegType() != body.getLeg().getType()) {
						possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_LEGS, TFModifier.NONE, TFPotency.MINOR_BOOST, 1),
								"I'm sure you'll love getting some new legs!");
					}
					if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); } // Apply arms & legs at the same time
				}
			}
			// Face & Skin transformations:
			if(Main.getProperties().getForcedTFPreference() == FurryPreference.NORMAL || Main.getProperties().getForcedTFPreference() == FurryPreference.MAXIMUM) {
				if(possibleEffects.isEmpty()) {
					if(target.getSkinType() != body.getSkin().getType()) {
						possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_SKIN, TFModifier.NONE, TFPotency.MINOR_BOOST, 1),
								"This is going to be good!");
					}
					if(target.getFaceType() != body.getFace().getType()) {
						possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_FACE, TFModifier.NONE, TFPotency.MINOR_BOOST, 1),
								"I can't wait to see how you'll look after this!");
					}
					if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); } // Apply face & skin at the same time
				}
			}
		}
		
		
		// Other transformations:
		
		//----- Minor body part variation based on fetishes ------
		
		//Ass:
		if(hasFetish(Fetish.FETISH_ANAL_GIVING)) {
			if(this.getAttributeValue(Attribute.MAJOR_CORRUPTION) >= CorruptionLevel.THREE_DIRTY.getMinimumValue()) {
				body.getAss().getAnus().getOrificeAnus().addOrificeModifier(null, OrificeModifier.RIBBED);
				body.getAss().getAnus().getOrificeAnus().addOrificeModifier(null, OrificeModifier.MUSCLE_CONTROL);
				body.getAss().getAnus().getOrificeAnus().addOrificeModifier(null, OrificeModifier.PUFFY);
			}
			if(this.getAttributeValue(Attribute.MAJOR_CORRUPTION) >= CorruptionLevel.FOUR_LUSTFUL.getMinimumValue()) {
				body.getAss().getAnus().getOrificeAnus().addOrificeModifier(null, OrificeModifier.TENTACLED);
			}
			
			body.getAss().setAssSize(null, AssSize.FIVE_HUGE.getValue());
			body.getAss().setAssSize(null, HipSize.FIVE_VERY_WIDE.getValue());
		}
		
		//Breasts:
		if(hasFetish(Fetish.FETISH_BREASTS_OTHERS) && this.getGenderPreference().getGenderName().isHasBreasts()) {
			body.getBreast().setSize(null, (int) (body.getBreast().getRawSizeValue()*1.5f));
		}
		
		// Face:
		if(hasFetish(Fetish.FETISH_ORAL_RECEIVING)) {
			body.getFace().getMouth().getOrificeMouth().addOrificeModifier(null, OrificeModifier.PUFFY);
			body.getFace().getMouth().setLipSize(null, LipSize.FOUR_HUGE.getValue());
			
			if(this.getAttributeValue(Attribute.MAJOR_CORRUPTION) >= CorruptionLevel.THREE_DIRTY.getMinimumValue()) {
				body.getFace().getMouth().getOrificeMouth().addOrificeModifier(null, OrificeModifier.RIBBED);
				body.getFace().getMouth().getOrificeMouth().addOrificeModifier(null, OrificeModifier.MUSCLE_CONTROL);
			}
			if(this.getAttributeValue(Attribute.MAJOR_CORRUPTION) >= CorruptionLevel.FOUR_LUSTFUL.getMinimumValue()) {
				body.getFace().getMouth().getOrificeMouth().addOrificeModifier(null, OrificeModifier.TENTACLED);
			}
		}
		
		// Hair:
//		if(this.getGenderPreference().isFeminine()) {
//			body.getHair().setLength(null, body.getHair().getRawLengthValue());
//			
//		} else {
//			body.getHair().setLength(null, body.getHair().getRawLengthValue());
//		}
		
		// Penis:
		if(body.getPenis().getType()!=PenisType.NONE) {
			if(this.getGenderPreference()==Gender.F_P_TRAP) {
				body.getPenis().setPenisSize(null, PenisSize.ONE_TINY.getMedianValue());
				body.getPenis().getTesticle().setTesticleSize(null, TesticleSize.ONE_TINY.getValue());
				body.getPenis().getTesticle().setCumStorage(null, CumProduction.ONE_TRICKLE.getMedianValue());
			}
		}
		
		// Vagina:
		if(body.getVagina().getType()!=VaginaType.NONE) {
			if(this.getAttributeValue(Attribute.MAJOR_CORRUPTION) >= CorruptionLevel.THREE_DIRTY.getMinimumValue()) {
				body.getVagina().getOrificeVagina().addOrificeModifier(null, OrificeModifier.RIBBED);
				body.getVagina().getOrificeVagina().addOrificeModifier(null, OrificeModifier.MUSCLE_CONTROL);
			}
			if(this.getAttributeValue(Attribute.MAJOR_CORRUPTION) >= CorruptionLevel.FOUR_LUSTFUL.getMinimumValue()) {
				body.getVagina().getOrificeVagina().addOrificeModifier(null, OrificeModifier.TENTACLED);
			}
		}
		
		//-----------
		
		
		//--- CORE ---//
		
		// Height:
		if(target.getHeightValue() + 10 < body.getHeightValue()) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CORE, TFModifier.TF_MOD_SIZE, TFPotency.MAJOR_BOOST, 1), "Let's make you taller!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
			
		} else if(target.getHeightValue() - 10 > body.getHeightValue()) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CORE, TFModifier.TF_MOD_SIZE, TFPotency.MAJOR_DRAIN, 1), "Let's make you shorter!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
		}
		
		// Muscle:
		if(target.getMuscleValue() > body.getMuscle()
				&& target.getMuscle() != Muscle.valueOf(body.getMuscle())) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CORE, TFModifier.TF_MOD_SIZE_SECONDARY, TFPotency.MAJOR_DRAIN, 1), "You're too muscly for me!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
			
		} else if(target.getMuscleValue() < body.getMuscle()
				&& target.getMuscle() != Muscle.valueOf(body.getMuscle())) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CORE, TFModifier.TF_MOD_SIZE_SECONDARY, TFPotency.MAJOR_BOOST, 1), "You need to have more muscle!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
		}

		// Body size:
		if(target.getBodySizeValue() > body.getBodySize()
				&& target.getBodySize() != BodySize.valueOf(body.getBodySize())) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CORE, TFModifier.TF_MOD_SIZE_TERTIARY, TFPotency.MAJOR_DRAIN, 1), "Let's slim you down a bit!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
			
		} else if(target.getBodySizeValue() < body.getBodySize()
				&& target.getBodySize() != BodySize.valueOf(body.getBodySize())) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CORE, TFModifier.TF_MOD_SIZE_TERTIARY, TFPotency.MAJOR_BOOST, 1), "You're far too slim for my liking!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
		}
		
		// Femininity:
		if(target.getFemininityValue() < body.getFemininity()
				&& Femininity.valueOf(target.getFemininityValue()) != Femininity.valueOf(body.getFemininity())) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CORE, TFModifier.TF_MOD_FEMININITY, TFPotency.MAJOR_BOOST, 1), "I'm gonna need you to be more feminine!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
			
		} else if(target.getFemininityValue() > body.getFemininity()
				&& Femininity.valueOf(target.getFemininityValue()) != Femininity.valueOf(body.getFemininity())
				&& !Femininity.valueOf(body.getFemininity()).isFeminine()) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CORE, TFModifier.TF_MOD_FEMININITY, TFPotency.MAJOR_DRAIN, 1), "I'm gonna need you to be more of a man!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
		}
		

		//--- BREASTS ---//
		
		// Breast size:
		if(target.getBreastSize().getMeasurement() < body.getBreast().getSize().getMeasurement()) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_BREASTS, TFModifier.TF_MOD_SIZE, TFPotency.BOOST, 1), "Your breasts need to be bigger!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
			
		} else if(target.getBreastSize().getMeasurement() > body.getBreast().getSize().getMeasurement()) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_BREASTS, TFModifier.TF_MOD_SIZE, TFPotency.DRAIN, 1), "Your breasts are too big!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
		}

		//--- ASS ---//
		
		// Ass size:
		if(target.getAssSize().getValue() < body.getAss().getAssSize().getValue()) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_ASS, TFModifier.TF_MOD_SIZE, TFPotency.BOOST, 1), "Your ass needs to be bigger");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
			
		} else if(target.getAssSize().getValue() > body.getAss().getAssSize().getValue()) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_ASS, TFModifier.TF_MOD_SIZE, TFPotency.DRAIN, 1), "Your ass is too big!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
		}
		
		// Capacity:
		if(target.getAssRawCapacityValue()+10 < body.getAss().getAnus().getOrificeAnus().getRawCapacityValue()) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_ASS, TFModifier.TF_MOD_CAPACITY, TFPotency.BOOST, 1), "Your ass is too tight for my liking!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
			
		} else if(target.getAssRawCapacityValue()-20 > body.getAss().getAnus().getOrificeAnus().getRawCapacityValue()) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_ASS, TFModifier.TF_MOD_CAPACITY, TFPotency.MAJOR_DRAIN, 1), "Your ass is far too loose!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
		}
		
		// Wetness:
		if(target.getAssWetness().getValue() < body.getAss().getAnus().getOrificeAnus().getWetness(null).getValue()) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_ASS, TFModifier.TF_MOD_WETNESS, TFPotency.MINOR_BOOST, 1), "Your ass is too dry!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
		}
		
		// Hip size:
		if(target.getHipSize().getValue() < body.getAss().getHipSize().getValue()) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_ASS, TFModifier.TF_MOD_SIZE_SECONDARY, TFPotency.BOOST, 1), "Your hips need to be wider!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
			
		} else if(target.getHipSize().getValue() > body.getAss().getHipSize().getValue()) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_ASS, TFModifier.TF_MOD_SIZE_SECONDARY, TFPotency.DRAIN, 1), "Your hips are too wide!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
		}


		//--- HAIR ---//
		
		// Hair length:
		if(target.getHairRawLengthValue() < body.getHair().getRawLengthValue()) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_HAIR, TFModifier.TF_MOD_SIZE, TFPotency.MAJOR_BOOST, 1), "Your [pc.hair] "+(target.getHairType().isDefaultPlural()?"are":"is")+" too short!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
			
		} else if(target.getHairRawLengthValue() > body.getHair().getRawLengthValue()) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_HAIR, TFModifier.TF_MOD_SIZE, TFPotency.MAJOR_DRAIN, 1), "Your [pc.hair] "+(target.getHairType().isDefaultPlural()?"are":"is")+" too long!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
		}

		//--- FACE ---//
		
		// Lip size:
		if(target.getLipSize().getValue() < body.getFace().getMouth().getLipSize().getValue()) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_FACE, TFModifier.TF_MOD_SIZE, TFPotency.BOOST, 1), "Your [pc.lips] are too small!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
			
		} else if(target.getLipSize().getValue() > body.getFace().getMouth().getLipSize().getValue()) {
			possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_FACE, TFModifier.TF_MOD_SIZE, TFPotency.DRAIN, 1), "Your [pc.lips] are too big!");
			if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
		}
		
		
		//--- PENIS ---//
		
		if(target.getPenisType() != PenisType.NONE && body.getPenis().getType() != PenisType.NONE) {
			// Cum production:
			if(target.getPenisRawCumStorageValue() < body.getPenis().getTesticle().getRawCumStorageValue()) {
				possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_CUM, TFModifier.TF_MOD_WETNESS, TFPotency.MAJOR_BOOST, 1), "Mmm! You're gonna make lots of cum for me!");
				if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
			}
			// Size:
			if(target.getPenisRawSizeValue() < body.getPenis().getRawSizeValue()) {
				if(body.getPenis().getRawSizeValue() - target.getPenisRawSizeValue() > 5) {
					possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE, TFPotency.BOOST, 1), "Your cock needs to be a lot bigger!");
					if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
				} else {
					possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE, TFPotency.MINOR_BOOST, 1), "Your cock needs to be a little bigger!");
					if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
				}
				
			} else if(target.getPenisRawSizeValue() > body.getPenis().getRawSizeValue()) {
				if(target.getPenisRawSizeValue() - body.getPenis().getRawSizeValue() > 5) {
					if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
					possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE, TFPotency.DRAIN, 1), "Your cock needs to be a lot smaller!");
				} else {
					possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE, TFPotency.MINOR_DRAIN, 1), "Your cock needs to be a little smaller!");
					if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
				}
			}
			// Penis girth:
			if(target.getPenisRawGirthValue() < body.getPenis().getRawGirthValue()) {
				possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE_SECONDARY, TFPotency.MINOR_BOOST, 1), "I want your cock to be nice and thick!");
				if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
				
			} else if(target.getPenisRawGirthValue() > body.getPenis().getRawGirthValue()) {
				possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE_SECONDARY, TFPotency.MINOR_DRAIN, 1), "Your cock's far too thick!");
				if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
			}
			// Ball size:
			if(target.getTesticleSize().getValue() < body.getPenis().getTesticle().getTesticleSize().getValue()) {
				possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE_TERTIARY, TFPotency.MINOR_BOOST, 1), "Your balls need to be bigger than that!");
				if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
				
			} else if(target.getTesticleSize().getValue() > body.getPenis().getTesticle().getTesticleSize().getValue()) {
				possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_PENIS, TFModifier.TF_MOD_SIZE_TERTIARY, TFPotency.MINOR_DRAIN, 1), "Your balls shouldn't be so big!");
				if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
			}
		}

		
		//--- VAGINA ---//
		
		if(target.getVaginaType() != VaginaType.NONE && body.getVagina().getType() != VaginaType.NONE) {
			// Capacity:
			if(target.getVaginaRawCapacityValue()+10 < body.getVagina().getOrificeVagina().getRawCapacityValue()) {
				possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.TF_MOD_CAPACITY, TFPotency.BOOST, 1), "Your pussy's too tight for my liking!");
				if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
				
			} else if(target.getVaginaRawCapacityValue()-20 > body.getVagina().getOrificeVagina().getRawCapacityValue()) {
				possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.TF_MOD_CAPACITY, TFPotency.MAJOR_DRAIN, 1), "Your pussy's far too loose!");
				if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
			}
			// Wetness:
			if(target.getVaginaWetness().getValue() < body.getVagina().getOrificeVagina().getWetness(null).getValue()) {
				possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), TFModifier.TF_VAGINA, TFModifier.TF_MOD_WETNESS, TFPotency.MINOR_BOOST, 1), "Your pussy isn't wet enough!");
				if(possibleEffects.size()>=numberOfTransformations) { return new Value<>(itemType, possibleEffects); }
			}
		}
		
		if(possibleEffects.isEmpty()) {
			return null;
		}
		
		return new Value<>(itemType, possibleEffects);
	}
	
	private void generatePartnerPreferences() {
		
		// Preferred gender:
		
		Gender preferredGender = Gender.N_P_V_B_HERMAPHRODITE;
		Map<Gender, Integer> desiredGenders = new HashMap<>();
		
		switch(this.getSexualOrientation()) {
			case AMBIPHILIC:
				if(this.isFeminine() && 
						// ambiphilic characters respect .getForcedTFTendency() setting by not entering this case if the
						// player has requested a feminine tendency; admittedly, this specific logic does slightly skew 
						// towards pushing the player feminine in neutral scenarios, but only to a small degree, so more
						// complex but fair logic doesn't feel too required
						Main.getProperties().getForcedTFTendency() != ForcedTFTendency.FEMININE &&
						Main.getProperties().getForcedTFTendency() != ForcedTFTendency.FEMININE_HEAVY) {
					desiredGenders.put(Gender.M_P_MALE, 14);
					// maybe it would be appropriate to raise these chances for impregnators?
					desiredGenders.put(Gender.M_P_V_HERMAPHRODITE, 2);
					desiredGenders.put(Gender.M_V_CUNTBOY, 2);
					desiredGenders.put(Gender.F_P_TRAP, 2);
				} else {
					// basic chances of cis-female preference
					desiredGenders.put(Gender.F_V_B_FEMALE, 14);
					
					// increase chances of growing a penis if fetishes increase desirability 
					if(this.hasVagina() && (this.hasFetish(Fetish.FETISH_PREGNANCY))) {
						desiredGenders.put(Gender.F_P_V_B_FUTANARI, 4);
						desiredGenders.put(Gender.F_P_B_SHEMALE, 4);
						desiredGenders.put(Gender.F_P_TRAP, 4);
						
					} else {
						desiredGenders.put(Gender.F_P_V_B_FUTANARI, 2);
						desiredGenders.put(Gender.F_P_B_SHEMALE, 2);
						desiredGenders.put(Gender.F_P_TRAP, 2);
					};
					
					// heavy masculine .getForcedTFTendency() option adds a bit of a chance for masculine preferences here
					if (Main.getProperties().getForcedTFTendency() == ForcedTFTendency.MASCULINE_HEAVY) {
						desiredGenders.put(Gender.M_P_V_HERMAPHRODITE, 4);
						desiredGenders.put(Gender.M_V_CUNTBOY, 4);
						desiredGenders.put(Gender.F_P_TRAP, 4);
						desiredGenders.put(Gender.M_V_B_BUTCH, 4);
					}
				}
				break;
			case ANDROPHILIC:
				// Heavy feminine .getForcedTFTendency() causes androphiles to lose the majority of masculine options
				if (Main.getProperties().getForcedTFTendency() != ForcedTFTendency.FEMININE_HEAVY) {
					desiredGenders.put(Gender.M_P_MALE, 14);
				}
				
				// base chance options regardless of .getForcedTFTendency() option
				desiredGenders.put(Gender.M_P_V_HERMAPHRODITE, 2);
				desiredGenders.put(Gender.M_V_CUNTBOY, 2);
				
				// both feminine .getForcedTFTendency() options add decent chances to get some feminine options despite tastes
				if(Main.getProperties().getForcedTFTendency() == ForcedTFTendency.FEMININE || 
				   Main.getProperties().getForcedTFTendency() == ForcedTFTendency.FEMININE_HEAVY) {
					desiredGenders.put(Gender.F_P_V_B_FUTANARI, 2);
					desiredGenders.put(Gender.F_P_B_SHEMALE, 2);
					desiredGenders.put(Gender.F_P_TRAP, 2);
					desiredGenders.put(Gender.M_V_B_BUTCH, 2);
				}
				break;
			case GYNEPHILIC:
				// increase chances of growing a penis if fetishes increase desirability; also, this is a reasonable
				// base level of feminine options even if .getForcedTFTendency() is heavy male
				if(this.hasVagina() && (this.hasFetish(Fetish.FETISH_PREGNANCY))) {
					desiredGenders.put(Gender.F_P_V_B_FUTANARI, 2);
					desiredGenders.put(Gender.F_P_B_SHEMALE, 2);
					desiredGenders.put(Gender.F_P_TRAP, 2);
				// much lower base chance of pure female preference for heavy masculine .getForcedTFTendency()
				} else if (Main.getProperties().getForcedTFTendency() == ForcedTFTendency.MASCULINE_HEAVY) {
					desiredGenders.put(Gender.F_V_B_FEMALE, 4);
				}
				else {
					desiredGenders.put(Gender.F_V_B_FEMALE, 14);
				}
				
				// both masculine .getForcedTFTendency() options add decent chances to get some masculine options despite tastes
				if(Main.getProperties().getForcedTFTendency() == ForcedTFTendency.MASCULINE || 
				   Main.getProperties().getForcedTFTendency() == ForcedTFTendency.MASCULINE_HEAVY) {
					desiredGenders.put(Gender.M_P_V_HERMAPHRODITE, 2);
					desiredGenders.put(Gender.M_V_CUNTBOY, 2);
					desiredGenders.put(Gender.M_V_B_BUTCH, 2);
					desiredGenders.put(Gender.F_P_TRAP, 2);
				}
				break;
		}
		
		int total = 0;
		for(Entry<Gender, Integer> entry : desiredGenders.entrySet()) {
			total+=entry.getValue();
		}
		int count = Util.random.nextInt(total)+1;
		total = 0;
		for(Entry<Gender, Integer> entry : desiredGenders.entrySet()) {
			if(total < count && total+entry.getValue()>= count) {
				preferredGender = entry.getKey();
				break;
			}
			total+=entry.getValue();
		}
		
		this.genderPreference = preferredGender;
		
		// Leaving this present but commented out so it can be easily re-enabled by anyone wanting to tweak or check
		// the results of gender selection and the .getForcedTFTendency() setting
//		System.out.println("PREFERRED GENDER");
//		System.out.println(preferredGender);
//		System.out.println(desiredGenders);
		
		// Preferred race:
		
		Subspecies species = getSubspecies();
		RaceStage stage = getRaceStage();
		
		if(Main.getProperties().getForcedTFPreference()==FurryPreference.HUMAN) {
			species = Subspecies.HUMAN;
			stage = RaceStage.HUMAN;
			
		} else {
			// Chance for predator races to prefer prey races:
			if(getRace()==Race.CAT_MORPH && Math.random()>0.8f) {
				species = Subspecies.HARPY;
			}
			if((getRace()==Race.WOLF_MORPH || getRace()==Race.DOG_MORPH) && Math.random()>0.8f) {
				List<Subspecies> availableRaces = new ArrayList<>();
				availableRaces.add(Subspecies.CAT_MORPH);
				availableRaces.add(Subspecies.HARPY);
				availableRaces.add(Subspecies.COW_MORPH);
				availableRaces.add(Subspecies.SQUIRREL_MORPH);
				species = availableRaces.get(Util.random.nextInt(availableRaces.size()));
			}
			
			// Chance for race to be random:
			if(Math.random() <= Main.getProperties().getRandomRacePercentage()) {
				List<Subspecies> availableRaces = new ArrayList<>();
				availableRaces.add(Subspecies.CAT_MORPH);
				availableRaces.add(Subspecies.DOG_MORPH);
				availableRaces.add(Subspecies.HARPY);
				availableRaces.add(Subspecies.HORSE_MORPH);
				availableRaces.add(Subspecies.HUMAN);
				availableRaces.add(Subspecies.SQUIRREL_MORPH);
				availableRaces.add(Subspecies.COW_MORPH);
				availableRaces.add(Subspecies.WOLF_MORPH);
				species = availableRaces.get(Util.random.nextInt(availableRaces.size()));
			}
			
			// Preferred race stage:
			if(preferredGender.isFeminine()) {
				switch(Main.getProperties().getSubspeciesFeminineFurryPreferencesMap().get(species)) {
					case HUMAN:
						stage = RaceStage.HUMAN;
						break;
					case MAXIMUM:
						stage = RaceStage.GREATER;
						break;
					case MINIMUM:
						stage = RaceStage.PARTIAL_FULL;
						break;
					case NORMAL:
						stage = RaceStage.GREATER;
						break;
					case REDUCED:
						stage = RaceStage.LESSER;
						break;
				}
			} else {
				switch(Main.getProperties().getSubspeciesMasculineFurryPreferencesMap().get(species)) {
					case HUMAN:
						stage = RaceStage.HUMAN;
						break;
					case MAXIMUM:
						stage = RaceStage.GREATER;
						break;
					case MINIMUM:
						stage = RaceStage.PARTIAL_FULL;
						break;
					case NORMAL:
						stage = RaceStage.GREATER;
						break;
					case REDUCED:
						stage = RaceStage.LESSER;
						break;
				}
			}
		}
		
		this.subspeciesPreference = species;
		this.raceStagePreference = stage;
	}
	
	public Value<AbstractItemType, Map<ItemEffect, String>> generateFetishPotion(GameCharacter target, Boolean pairedFetishesOnly) {
		ItemEffect selectedEffect = null; // this will be the ultimately selected effect, or null if none available
		String selectedEffectString ; // this will be a flavor text string paired with the effect
		
		Map<ItemEffect, Integer> possibleEffects = new HashMap<>();
		
		AbstractItemType itemType = ItemType.FETISH_UNREFINED;
		
		Fetish currentTopFetish = null, currentBottomFetish = null;
		TFModifier currentTopModifier = null, currentBottomModifier = null;
		TFPotency currentTopPotency = null, currentBottomPotency = null, currentTopRemovePotency = null, currentBottomRemovePotency = null;;
		
		int baseTopChance = 5, baseBottomChance = 5,  baseTopRemoveChance = 0, baseBottomRemoveChance = 0; 
		int currentTopChance = 0, currentBottomChance = 0, currentTopRemoveChance = 0, currentBottomRemoveChance = 0;
		
		int pairedFetishMultiplier = 5;  
		int matchedFetishDecrement = 8;  // heavy tendency can still allow small chance giving a matched fetish, otherwise no chance at all
		int matchedFetishRemoveIncrement = 1;  // only a modest increase in chances to matched fetish
		
		int desiredFetishIncrement = 2;  // for now, keeping it simple, only modifying add chances based on desires, one increment (or decrement) per level
		int expFetishIncrement = 1;  // for now, keeping it simple, only modifying add chances based on exp, one increment per level
		
		switch(Main.getProperties().getForcedFetishTendency()) {
			case NEUTRAL:
				baseTopChance = 5;
				baseBottomChance = 5;
				baseTopRemoveChance = 2;
				baseBottomRemoveChance = 2;
				break;
			case BOTTOM:
				baseTopChance = 1;
				baseBottomChance = 8;
				baseTopRemoveChance = 3;
				baseBottomRemoveChance = 0;
				break;
			case BOTTOM_HEAVY:
				baseTopChance = -2;
				baseBottomChance = 10;
				baseTopRemoveChance = 4;
				baseBottomRemoveChance = -1;
				break;
			case TOP:
				baseTopChance = 8;
				baseBottomChance = 1;
				baseTopRemoveChance = 0;
				baseBottomRemoveChance = 3;
				break;
			case TOP_HEAVY:
				baseTopChance = 10;
				baseBottomChance = -2;
				baseTopRemoveChance = -1;
				baseBottomRemoveChance = 4;
				break;
		}
		
		// map of top -> bottom paired fetishes; NPCs with a paired fetish will greatly favor 
		// giving the player it's pair, and remove that fetish if there is a match
		Map<Fetish, Fetish> pairedFetishMap = new HashMap<>();

		pairedFetishMap.put(Fetish.FETISH_ANAL_GIVING, Fetish.FETISH_ANAL_RECEIVING);
		pairedFetishMap.put(Fetish.FETISH_VAGINAL_GIVING, Fetish.FETISH_VAGINAL_RECEIVING);
		pairedFetishMap.put(Fetish.FETISH_BREASTS_OTHERS, Fetish.FETISH_BREASTS_SELF);
		pairedFetishMap.put(Fetish.FETISH_ORAL_RECEIVING, Fetish.FETISH_ORAL_GIVING);
		pairedFetishMap.put(Fetish.FETISH_LEG_LOVER, Fetish.FETISH_STRUTTER);
		
		pairedFetishMap.put(Fetish.FETISH_DOMINANT, Fetish.FETISH_SUBMISSIVE);
		pairedFetishMap.put(Fetish.FETISH_CUM_STUD, Fetish.FETISH_CUM_ADDICT);
//		pairedFetishMap.put(Fetish.FETISH_DEFLOWERING, Fetish.FETISH_PURE_VIRGIN); // Do not give deflowering if pure virgin fetish...
		pairedFetishMap.put(Fetish.FETISH_IMPREGNATION, Fetish.FETISH_PREGNANCY);
		pairedFetishMap.put(Fetish.FETISH_SADIST, Fetish.FETISH_MASOCHIST);
		pairedFetishMap.put(Fetish.FETISH_NON_CON_DOM, Fetish.FETISH_NON_CON_SUB);
		pairedFetishMap.put(Fetish.FETISH_DENIAL, Fetish.FETISH_DENIAL_SELF);
		pairedFetishMap.put(Fetish.FETISH_VOYEURIST, Fetish.FETISH_EXHIBITIONIST);
		
		// Do not include these, as NPCs will otherwise always end up forcing them on the player:
//		if(!pairedFetishesOnly) {
//			pairedFetishMap.put(Fetish.FETISH_TRANSFORMATION_GIVING, Fetish.FETISH_TRANSFORMATION_RECEIVING);
//			pairedFetishMap.put(Fetish.FETISH_KINK_GIVING, Fetish.FETISH_KINK_RECEIVING);
//		}
		
		for(Entry<Fetish, Fetish> entry : pairedFetishMap.entrySet()) {
			currentTopFetish = entry.getKey();
			currentBottomFetish = entry.getValue();
			
			currentTopModifier = TFModifier.valueOf( "TF_MOD_" + currentTopFetish);
			currentBottomModifier = TFModifier.valueOf( "TF_MOD_" + currentBottomFetish);
			
			currentTopPotency = TFPotency.MINOR_BOOST;
			currentBottomPotency = TFPotency.MINOR_BOOST;
			currentTopRemovePotency = TFPotency.MINOR_DRAIN;
			currentBottomRemovePotency = TFPotency.MINOR_DRAIN;
			
			currentTopChance = baseTopChance;
			currentBottomChance = baseBottomChance;
			currentTopRemoveChance = baseTopRemoveChance;
			currentBottomRemoveChance = baseBottomRemoveChance;
			
			// Increase base add chances based on NPC's desire levels for these fetishes
			switch(this.getFetishDesire(currentBottomFetish)) {
				case THREE_LIKE:
					currentTopChance += desiredFetishIncrement;
					break;
				case FOUR_LOVE:
					currentTopChance += desiredFetishIncrement * 2;
					break;
				case ONE_DISLIKE:
					currentTopChance -= desiredFetishIncrement;
					break;
				case ZERO_HATE:
					currentTopChance = 0;
					break;
				default:
			}
			
			switch(this.getFetishDesire(currentTopFetish)) {
				case THREE_LIKE:
					currentBottomChance += desiredFetishIncrement;
					break;
				case FOUR_LOVE:
					currentBottomChance += desiredFetishIncrement * 2;
					break;
				case ONE_DISLIKE:
					currentBottomChance -= desiredFetishIncrement;
					break;
				case ZERO_HATE:
					currentBottomChance = 0;
					break;
				default:
			}
			
			// Increase base add chances based on NPC's experience levels for these fetishes
			switch(this.getFetishLevel(currentBottomFetish)) {
				case ONE_AMATEUR:
					currentTopChance += expFetishIncrement;
					break;
				case TWO_EXPERIENCED:
					currentTopChance += expFetishIncrement * 2;
					break;
					
				case THREE_EXPERT:
					currentTopChance += expFetishIncrement * 3;
					break;
					
				case FOUR_MASTERFUL:
					currentTopChance += expFetishIncrement * 4;
					break;
					
				default:
			}
			
			switch(this.getFetishLevel(currentTopFetish)) {
				case ONE_AMATEUR:
					currentBottomChance += expFetishIncrement;
					break;
				case TWO_EXPERIENCED:
					currentBottomChance += expFetishIncrement * 2;
					break;
				
				case THREE_EXPERT:
					currentBottomChance += expFetishIncrement * 3;
					break;
				
				case FOUR_MASTERFUL:
					currentBottomChance += expFetishIncrement * 4;
					break;
					
				default:
			}
			
			// set chances if NPC has top fetish
			if(this.hasFetish(currentTopFetish)) {
				currentBottomChance *= pairedFetishMultiplier;
				currentTopChance -= matchedFetishDecrement;
				currentBottomRemoveChance = 0;
				if(!pairedFetishesOnly) {
					currentTopRemoveChance += matchedFetishRemoveIncrement;
				}
			} else if(pairedFetishesOnly) {
				currentBottomChance = 0;
				// in paired only mode, we're only adding fetishes
				currentTopRemoveChance = 0;
				currentBottomRemoveChance = 0;
			}
			
			// set chances if NPC has bottom fetish
			if(this.hasFetish(currentBottomFetish)) {
				currentTopChance *= pairedFetishMultiplier;
				currentBottomChance -= matchedFetishDecrement;
				currentTopRemoveChance = 0;
				if(!pairedFetishesOnly) {
					currentBottomRemoveChance += matchedFetishRemoveIncrement;
				}
			} else if(pairedFetishesOnly) {
				currentTopChance = 0;
				// in paired only mode, we're only adding fetishes
				currentTopRemoveChance = 0;
				currentBottomRemoveChance = 0;
			}
			
			// if player has positive bottom fetish desire, adjust potency level to fully add fetish, not just desire
			if(target.getFetishDesire(currentBottomFetish) == FetishDesire.THREE_LIKE ||
					target.getFetishDesire(currentBottomFetish) == FetishDesire.FOUR_LOVE) {
				currentBottomPotency = TFPotency.BOOST;
			} 
			else if(target.getFetishDesire(currentBottomFetish) == FetishDesire.TWO_NEUTRAL) {
				int rand = Util.random.nextInt(100);
				
				// if the player is neutral, but the NPC has fetish,small chance to fully add rather than just boost desire
				if(this.hasFetish(currentTopFetish) && rand < 30) {
					currentBottomPotency = TFPotency.BOOST;
				}
			} else {
				// if they are already less than neutral, don't remove any more
				currentBottomRemoveChance = 0;
			}
			
			// prevent extraneous effects if player has bottom fetish, plus alter remove potency to drop fetish, not just desire
			if(target.hasFetish(currentBottomFetish)) {
				currentBottomChance = 0;
				currentBottomRemovePotency = TFPotency.DRAIN;
			}
			
			// if player has positive top fetish desire, adjust potency level to fully add fetish, not just desire
			if(target.getFetishDesire(currentTopFetish) == FetishDesire.THREE_LIKE ||
				target.getFetishDesire(currentTopFetish) == FetishDesire.FOUR_LOVE) {
				currentTopPotency = TFPotency.BOOST;
			} else if(target.getFetishDesire(currentTopFetish) == FetishDesire.TWO_NEUTRAL) {
				int rand = Util.random.nextInt(100);
				
				// if the player is neutral, but the NPC has paired fetish,small chance to fully add rather than just boost desire
				if(this.hasFetish(currentBottomFetish) && rand < 30) {
					currentTopPotency = TFPotency.BOOST;
				}
			} else {
				// if they are already less than neutral, don't remove any more
				currentTopRemoveChance = 0;
			}
			
			// prevent extraneous effects if player has top fetish, plus alter remove potency to drop fetish, not just desire
			if(target.hasFetish(currentTopFetish)) {
				currentTopChance = 0;
				currentTopRemovePotency = TFPotency.DRAIN;
			}
			
			// some settings and status combinations can create negative values, so let's zero those out
			if(currentTopChance < 0) { currentTopChance = 0 ;}
			if(currentBottomChance < 0) { currentBottomChance = 0 ;}
			if(currentTopRemoveChance < 0) { currentTopRemoveChance = 0 ;}
			if(currentBottomRemoveChance < 0) { currentBottomRemoveChance = 0 ;}
			
			if(currentTopChance > 0) {
				possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), 
						TFModifier.NONE, 
						currentTopModifier, 
						currentTopPotency, 
						1), 
						currentTopChance);
			}
			
			if(currentTopRemoveChance > 0) {
				possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), 
						TFModifier.NONE, 
						currentTopModifier, 
						currentTopRemovePotency, 
						1), 
						currentTopRemoveChance);
			}
			
			if(currentBottomChance > 0) {
				possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), 
						TFModifier.NONE, 
						currentBottomModifier, 
						currentBottomPotency, 
						1), 
						currentBottomChance);
			}
			
			if(currentBottomRemoveChance > 0) {
				possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), 
						TFModifier.NONE, 
						currentBottomModifier, 
						currentBottomRemovePotency, 
						1), 
						currentBottomRemoveChance);
			}
		}
		
		// map of unpaired fetish -> boolean stating whether it wants to be shared, or hoarded
		// currently, all unpaired fetishes seem like they are something the owner would want to share,
		// but setting the second argument to false will cause the NPC to instead have an aversion to 
		// giving the player the same fetish
		Map<Fetish, Boolean> unpairedFetishMap = new HashMap<>();

		unpairedFetishMap.put(Fetish.FETISH_BIMBO, true);
		unpairedFetishMap.put(Fetish.FETISH_CROSS_DRESSER, true);
		unpairedFetishMap.put(Fetish.FETISH_INCEST, true);
		unpairedFetishMap.put(Fetish.FETISH_MASTURBATION, true);
		
		for(Entry<Fetish, Boolean> entry : unpairedFetishMap.entrySet()) {
			currentTopFetish = entry.getKey();
			Boolean wantsToShare = entry.getValue();
			
			currentTopModifier = TFModifier.valueOf( "TF_MOD_" + currentTopFetish);
			
			currentTopPotency = TFPotency.MINOR_BOOST;
			currentTopRemovePotency = TFPotency.MINOR_DRAIN;
			
			currentTopChance = baseTopChance;
			currentTopRemoveChance = baseTopRemoveChance;
			
			if(wantsToShare) {
				// Increase base add chances based on NPC's experience levels for this fetishes
				switch(this.getFetishDesire(currentTopFetish)) {
					case THREE_LIKE:
						currentTopChance += desiredFetishIncrement;
						break;
					case FOUR_LOVE:
						currentTopChance += desiredFetishIncrement * 2;
						break;
					case ONE_DISLIKE:
						currentTopChance -= desiredFetishIncrement;
						break;
					case ZERO_HATE:
						currentTopChance = 0;
						break;
					default:
				}
				
				// Increase base add chances based on NPC's experience levels for this fetishes
				switch(this.getFetishLevel(currentTopFetish)) {
					case ONE_AMATEUR:
						currentTopChance += expFetishIncrement;
						break;
					case TWO_EXPERIENCED:
						currentTopChance += expFetishIncrement * 2;
						break;
					case THREE_EXPERT:
						currentTopChance += expFetishIncrement * 3;
						break;
					case FOUR_MASTERFUL:
						currentTopChance += expFetishIncrement * 4;
						break;
					default:
				}
			}
			
			// set chances if NPC has top fetish
			if(this.hasFetish(currentTopFetish)) {
				if(wantsToShare) {
					currentTopChance *= pairedFetishMultiplier;
					currentTopRemoveChance = 0;
				}
				else if(pairedFetishesOnly) {
					currentTopChance = 0;
				}
				else {
					currentTopChance -= matchedFetishDecrement;
					currentTopRemoveChance += matchedFetishRemoveIncrement;
				}
			}
			else if(pairedFetishesOnly && wantsToShare) {
				currentTopChance = 0;
				currentTopRemoveChance = 0;
			}
			
			// if player has positive top fetish desire, adjust potency level to fully add fetish, not just desire
			if(target.getFetishDesire(currentTopFetish) == FetishDesire.THREE_LIKE ||
				target.getFetishDesire(currentTopFetish) == FetishDesire.FOUR_LOVE) {
				currentTopPotency = TFPotency.BOOST;
			}
			else if(target.getFetishDesire(currentTopFetish) == FetishDesire.TWO_NEUTRAL) {
				int rand = Util.random.nextInt(100);
				
				// if the player is neutral, but the NPC has paired fetish,small chance to fully add rather than just boost desire
				if(wantsToShare && this.hasFetish(currentBottomFetish) && rand < 30) {
					currentTopPotency = TFPotency.BOOST;
				}
				
			} else {
				// if they are already less than neutral, don't remove any more
				currentTopRemoveChance = 0;
			}
			
			// prevent extraneous effects if player has top fetish, plus alter remove potency to drop fetish, not just desire
			if(target.hasFetish(currentTopFetish)) {
				currentTopChance = 0;
				currentTopRemovePotency = TFPotency.DRAIN;
			} 
			
			// some setting and status combos can result in negative values, so let's zero those out
			if(currentTopChance < 0) { currentTopChance = 0 ;}
			if(currentTopRemoveChance < 0) { currentTopRemoveChance = 0 ;}
			
			if(currentTopChance > 0) {
				possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), 
						TFModifier.NONE, 
						currentTopModifier, 
						currentTopPotency, 
						1), 
						currentTopChance);
			}
			if(currentTopRemoveChance > 0) {
				possibleEffects.put(new ItemEffect(itemType.getEnchantmentEffect(), 
						TFModifier.NONE, 
						currentTopModifier, 
						currentTopRemovePotency, 
						1), 
						currentTopRemoveChance);
			}
		}
		
		// randomly select from possible effects 
		int total = 0;
		for(Entry<ItemEffect, Integer> entry : possibleEffects.entrySet()) {
			total+=entry.getValue();
		}
		
		// no valid options found
		if (total == 0) {
			return null;
		}
		
		int count = Util.random.nextInt(total)+1;
		total = 0;
		for(Entry<ItemEffect, Integer> entry : possibleEffects.entrySet()) {
			if(total < count && total+entry.getValue()>= count) {
				selectedEffect = entry.getKey();
				break;
			}
			total+=entry.getValue();
		}
		
		// Leaving this present but commented out so it can be easily re-enabled by anyone wanting to tweak or check
		// the results of fetish selection for potion generation
//		System.out.println("POSSIBLE"); 
//		for(Entry<ItemEffect, Integer> entry : possibleEffects.entrySet()) {
//			System.out.println(entry.getValue()+ " " + entry.getKey().getSecondaryModifier()+ " " + entry.getKey().getPotency()); 
//		}
//		System.out.println("SELECTED"); 
//		System.out.println(selectedEffect.getSecondaryModifier() + " " + selectedEffect.getPotency()); 
//		System.out.println(count); 
		
		
		// no fetish to add, so we have nothing to return
		if(selectedEffect == null) {
			return null;
		}
		
		
		Map<TFModifier, String> fetishAddFlavorText = new HashMap<>(), fetishRemoveFlavorText = new HashMap<>();
		
		String defaultFetishAddFlavorText = "Why not expand your horizons a bit, eh?";
		String defaultFetishRemoveFlavorText = "Maybe you should cool down a bit about the more extreme stuff, eh?.";
		
		// Body part
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_ANAL_GIVING, "You're going to love doing it in the ass after this.");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_ANAL_GIVING, "Maybe you should cool down a bit about fucking people in the ass.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_ANAL_RECEIVING, "You're going to love taking it in the ass after this.");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_ANAL_RECEIVING, "Maybe you should cool down a bit about getting fucked in the ass.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_BREASTS_OTHERS, "Don't you just love a nice pair of tits?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_BREASTS_OTHERS, "You're way too into breasts.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_BREASTS_SELF, "Wouldn't you love to put your breasts to good use?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_BREASTS_SELF, "You're way too into your breasts.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_ORAL_GIVING, "That beautiful mouth of yours is about to get a lot more use");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_ORAL_GIVING, "You don't really need to suck every cock you come across, do you?");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_ORAL_RECEIVING, "Don't you just love getting head?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_ORAL_RECEIVING, "Not everyone enjoys getting fucked in the face, you know.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_VAGINAL_GIVING, "Nothing quite compares to fucking a wet pussy, right?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_VAGINAL_GIVING, "There's more to sex than just pussy. Expand your horizons a bit.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_VAGINAL_RECEIVING, "When it comes down to it, you just want to get fucked in the pussy, right?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_VAGINAL_RECEIVING, "There's more to sex than just pussy. Expand your horizons a bit.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_LEG_LOVER, "A nice pair of stems makes all the difference, right?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_LEG_LOVER, "Maybe focus a bit more on what's above the waist -- or at least around the hips?");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_STRUTTER, "You've got legs that don't quit -- you really ought to use them");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_STRUTTER, "Maybe focus a bit more on what's above your waist -- or at least around the hips?");
		
		// Behavioral
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_DOMINANT, "Don't you think you deserve to be the one in charge?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_DOMINANT, "You're really not as intimidating as you think.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_SUBMISSIVE, "Give in to it, and admit that you want nothing more than to be my plaything.");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_SUBMISSIVE, "Sometimes it's nice to get what you want too, right?");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_CUM_STUD, "Nothing really compares to filling a juicy hole hole with your seed, right?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_CUM_STUD, "Sex should be about the journey, not the destination.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_CUM_ADDICT, "I know a dirty little cum dumpster when I see one.");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_CUM_ADDICT, "You can be more than a receptacle for other people's jizz if you want, you know.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_DEFLOWERING, "There's something special about being the first to the party, right?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_DEFLOWERING, "Trust me, it's a lot more fun when they have a bit of experience.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_PURE_VIRGIN, "You should treasure whatever innocence you have left while it lasts.");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_PURE_VIRGIN, "Fuck virginity. You'll have a lot more fun doing it than having it.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_IMPREGNATION, "A stud like you really ought to be breeding as many bitches as you can");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_IMPREGNATION, "Get over yourself. No one wants to have your baby.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_PREGNANCY, "Being fucked is nice, but being bred is better, isn't it?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_PREGNANCY, "Being knocked up is a bit of a drag, don't you think?");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_SADIST, "Isn't it nice when you hurt them and they beg for more?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_SADIST, "Not everyone likes being your punching bag.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_MASOCHIST, "It's time for you to embrace the pain. You'll thank me later.");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_MASOCHIST, "You should really take better care of yourself.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_NON_CON_DOM, "When they beg for you to stop it just drives you crazy, doesn't it?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_NON_CON_DOM, "Most of the time, no really means no.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_NON_CON_SUB, "Every time you say 'no' I can see 'fuck me harder' in your eyes.");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_NON_CON_SUB, "You really can get off without being forced to, believe it or not.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_DENIAL, "The only thing better than coming is telling your partner they can't, right?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_DENIAL, "If they're willing to fuck you, at least let them come once in a while.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_DENIAL_SELF, "Where's the fun in coming right away? Wouldn't you rather savor the experience?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_DENIAL_SELF, "What's the point if you aren't getting off?");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_VOYEURIST, "Sometimes it's just fun to watch, isn't it?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_VOYEURIST, "Privacy is a thing worth respecting.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_EXHIBITIONIST, "You've got it -- you should flaunt it");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_EXHIBITIONIST, "Not everyone wants to see what you've got to offer.");
		
		// Behavioral unpaired
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_BIMBO, "I think it's time you embraced your inner braindead slut.");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_BIMBO, "Maybe have just a little self respect?");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_CROSS_DRESSER, "You should wear what you want, and enjoy it.");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_CROSS_DRESSER, "It wouldn't kill you to be a bit more reserved.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_MASTURBATION, "Nobody knows your body quite like you do, right?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_MASTURBATION, "Maybe you should think about getting your hands on someone else's junk once in a while?");

		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_INCEST, "You know it wouldn't be a taboo if it wasn't at least a little bit fun.");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_INCEST, "You what? Gross.");
		
		// Behavioral transformative
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_TRANSFORMATION_GIVING, "You strike me as someone who should be an agent of change.");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_TRANSFORMATION_GIVING, "You should really just let people be who they are.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_TRANSFORMATION_RECEIVING, "Don't you just love becoming something new?");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_TRANSFORMATION_RECEIVING, "I think you're good just as you are.");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_KINK_GIVING, "You're into so many interesting things -- you really should share them with others.");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_KINK_GIVING, "Just let people enjoy what they enjoy, okay?");
		
		fetishAddFlavorText.put(TFModifier.TF_MOD_FETISH_KINK_RECEIVING, "You strike me as someone who would really enjoy trying new things.");
		fetishRemoveFlavorText.put(TFModifier.TF_MOD_FETISH_KINK_RECEIVING, "I think you're already excitable enough as it is.");
		
		if(selectedEffect.getPotency() == TFPotency.MINOR_BOOST || selectedEffect.getPotency() == TFPotency.BOOST) {
			// default for adding a fetish, just in case a fetish is somehow selected without a string defined in the lookup
			selectedEffectString = defaultFetishAddFlavorText;
			
			if(fetishAddFlavorText.get(selectedEffect.getSecondaryModifier()) != null ) {
				selectedEffectString = fetishAddFlavorText.get(selectedEffect.getSecondaryModifier());
			}
			
		} else {
			// default for removing a fetish, just in case a fetish is somehow selected without a string defined in the lookup
			selectedEffectString = defaultFetishRemoveFlavorText;
			
			if(fetishRemoveFlavorText.get(selectedEffect.getSecondaryModifier()) != null ) {
				selectedEffectString = fetishRemoveFlavorText.get(selectedEffect.getSecondaryModifier());
			}
		}
		
		// finally, build and return our fetish potion
		return new Value<>(itemType,
				Util.newHashMapOfValues(new Value<>(selectedEffect, selectedEffectString)));
	}
	
	
	// Sex:
	
	/**
	 * Override this method to set a special virginity loss scene for the player.
	 */
	public String getSpecialPlayerVirginityLoss(GameCharacter penetratingCharacter, SexAreaPenetration penetrating, GameCharacter receivingCharacter, SexAreaOrifice penetrated) {
		return "";
	}
	
	public void endSex() {
	}
	
	public Value<AbstractItem, String> getSexItemToUse(GameCharacter partner) {
		if(Main.game.isInSex()) {
			List<GameCharacter> charactersPenetratingThisNpc = new ArrayList<>(Main.sex.getOngoingCharactersUsingAreas(this, SexAreaOrifice.VAGINA, SexAreaPenetration.PENIS));
			List<GameCharacter> charactersThisNpcIsPenetrating = new ArrayList<>(Main.sex.getOngoingCharactersUsingAreas(this, SexAreaPenetration.PENIS, SexAreaOrifice.VAGINA));
			
			if(this.equals(partner)) { // Self-using items:
				if(!charactersPenetratingThisNpc.isEmpty() && charactersPenetratingThisNpc.stream().anyMatch((c) -> c.hasPenisIgnoreDildo())) { // Pills for when this NPC is being penetrated:
					if(this.isAbleToAccessCoverableArea(CoverableArea.MOUTH, false)) {
						if((this.getFetishDesire(Fetish.FETISH_PREGNANCY).isNegative() || this.getHistory()==Occupation.NPC_PROSTITUTE)
								&& !this.isPregnant()
								&& !this.hasStatusEffect(StatusEffect.PROMISCUITY_PILL)
								&& this.hasItemType(ItemType.PROMISCUITY_PILL)) {
							return new Value<>(AbstractItemType.generateItem(ItemType.PROMISCUITY_PILL),
										UtilText.parse(this, charactersPenetratingThisNpc.get(0),
												"Taking a small blue '[#ITEM_PROMISCUITY_PILL.getName(false)]' out of [npc.her] inventory, [npc.name] pops it out of its protective wrapper before quickly slipping it into [npc.her] mouth and swallowing it down."
												+ (this.isMute()
														?" Knowing that [npc.sheIs] now far less fertile and extremely unlikely to get knocked up, [npc.name] lets out a relieved [npc.moan]..."
														:" Knowing that [npc.sheIs] now far less fertile and extremely unlikely to get knocked up, [npc.name] [npc.moansVerb], [npc.speech(I really don't want to get pregnant...)]")));
						}
						if((this.getFetishDesire(Fetish.FETISH_PREGNANCY).isPositive() && this.getHistory()!=Occupation.NPC_PROSTITUTE)
								&& !this.isPregnant()
								&& (Main.sex.getSexPace(this)!=SexPace.SUB_RESISTING || this.hasFetish(Fetish.FETISH_NON_CON_SUB)) // Do not want to get pregnant from rape unless they have the fetish
								&& !this.hasStatusEffect(StatusEffect.VIXENS_VIRILITY)
								&& this.hasItemType(ItemType.VIXENS_VIRILITY)) {
							return new Value<>(AbstractItemType.generateItem(ItemType.VIXENS_VIRILITY),
									UtilText.parse(this, charactersPenetratingThisNpc.get(0),
											"Taking a small pink '[#ITEM_VIXENS_VIRILITY.getName(false)]' out of [npc.her] inventory, [npc.name] pops it out of its protective wrapper before quickly slipping it into [npc.her] mouth and swallowing it down."
											+ (this.isMute()
													?" Knowing that [npc.sheIs] now a lot more fertile, [npc.name] lets out [npc.a_moan+] as [npc.she] imagines [npc2.name] finishing inside of [npc.herHim] and getting [npc.herHim] knocked up..."
													:" Knowing that [npc.sheIs] now a lot more fertile, [npc.name] lets out [npc.a_moan+] and pleads, [npc.speech(Finish inside of me, [npc2.name]! I want you to knock me up!)]")));
						}
					}
				}
				if(charactersThisNpcIsPenetrating.contains(partner) && this.hasPenisIgnoreDildo()) { // Pills for when this NPC is penetrating someone else:
					if(this.isAbleToAccessCoverableArea(CoverableArea.MOUTH, false)) {
						if(this.getFetishDesire(Fetish.FETISH_IMPREGNATION).isNegative()
								&& !partner.isPregnant()
								&& !this.hasStatusEffect(StatusEffect.PROMISCUITY_PILL)
								&& this.hasItemType(ItemType.PROMISCUITY_PILL)) {
							return new Value<>(AbstractItemType.generateItem(ItemType.PROMISCUITY_PILL),
										UtilText.parse(this, charactersThisNpcIsPenetrating.get(0),
												"Taking a small blue '[#ITEM_PROMISCUITY_PILL.getName(false)]' out of [npc.her] inventory, [npc.name] pops it out of its protective wrapper before quickly slipping it into [npc.her] mouth and swallowing it down."
												+ (this.isMute()
														?" Knowing that [npc.sheIs] now far less virile and extremely unlikely to knock [npc2.name] up, [npc.name] lets out a relieved [npc.moan]..."
														:" Knowing that [npc.sheIs] now far less virile and extremely unlikely to knock [npc2.name] up, [npc.name] [npc.moansVerb], [npc.speech(That's better! I won't be getting you pregnant now!)]")));
						}
						if(this.getFetishDesire(Fetish.FETISH_IMPREGNATION).isPositive()
								&& !partner.isPregnant()
								&& (Main.sex.getSexPace(this)!=SexPace.SUB_RESISTING || this.hasFetish(Fetish.FETISH_NON_CON_SUB)) // Do not want to impregnate during rape unless they have the fetish
								&& !this.hasStatusEffect(StatusEffect.VIXENS_VIRILITY)
								&& this.hasItemType(ItemType.VIXENS_VIRILITY)) {
							return new Value<>(AbstractItemType.generateItem(ItemType.VIXENS_VIRILITY),
									UtilText.parse(this, charactersThisNpcIsPenetrating.get(0),
											"Taking a small pink '[#ITEM_VIXENS_VIRILITY.getName(false)]' out of [npc.her] inventory, [npc.name] pops it out of its protective wrapper before quickly slipping it into [npc.her] mouth and swallowing it down."
											+ (this.isMute()
													?" Knowing that [npc.sheIs] now a lot more virile, [npc.name] lets out [npc.a_moan+] as [npc.she] imagines finishing inside of [npc2.herHim] and getting [npc2.herHim] knocked up..."
													:" Knowing that [npc.sheIs] now a lot more virile, [npc.name] lets out [npc.a_moan+] and teases, [npc.speech(I'm going to give you a big creampie and get you knocked up!)]")));
						}
					}
				}
				
			} else { // Non-self use:
				if(charactersPenetratingThisNpc.contains(partner) && charactersPenetratingThisNpc.stream().anyMatch((c) -> c.hasPenisIgnoreDildo())) { // Pills for when this NPC is being penetrated:
					if(partner.isAbleToAccessCoverableArea(CoverableArea.MOUTH, false)) {
						if(!Main.sex.getItemUseDenials(this, partner).contains(ItemType.PROMISCUITY_PILL)) {
							if((this.getFetishDesire(Fetish.FETISH_PREGNANCY).isNegative() || this.getHistory()==Occupation.NPC_PROSTITUTE)
									&& !partner.isPregnant()
									&& (Main.sex.getSexPace(this)!=SexPace.SUB_RESISTING || this.hasFetish(Fetish.FETISH_NON_CON_SUB))
									&& !partner.hasStatusEffect(StatusEffect.PROMISCUITY_PILL)
									&& this.hasItemType(ItemType.PROMISCUITY_PILL)) {
								if(partner.isPlayer()) {
									if(Main.sex.isForcingItemUse(this, partner)) {
										return new Value<>(AbstractItemType.generateItem(ItemType.PROMISCUITY_PILL),
												"Taking a small blue '[#ITEM_PROMISCUITY_PILL.getName(false)]' out of [npc.her] inventory, [npc.name] pops it out of its protective wrapper before reaching over and pushing it into your mouth."
												+ " Clasping [npc.her] [npc.hand] over your [pc.lips] to prevent you from spitting it out,"
													+ (this.isMute()
															?" [npc.name] lets out a commanding growl and refuses to let go until you've swallowed it down..."
															:" [npc.name] forces you to swallow it down and growls, [npc.speech(I don't want you knocking me up!)]"));
										
									} else {
										return new Value<>(AbstractItemType.generateItem(ItemType.PROMISCUITY_PILL),
												"Taking a small blue '[#ITEM_PROMISCUITY_PILL.getName(false)]' out of [npc.her] inventory, [npc.name] holds it out to you"
													+ (this.isMute()
															?" and makes a pleading whine as [npc.she] motions for you to swallow it..."
															:" and asks, [npc.speech(Please swallow this; I don't want you knocking me up!)]"));
									}
									
								} else {
									return new Value<>(AbstractItemType.generateItem(ItemType.PROMISCUITY_PILL), ""); // Description is appended in the SexAction
								}
							}
						}
						if((this.getFetishDesire(Fetish.FETISH_PREGNANCY).isPositive() && this.getHistory()!=Occupation.NPC_PROSTITUTE)
								&& !partner.isPregnant()
								&& (Main.sex.getSexPace(this)!=SexPace.SUB_RESISTING || this.hasFetish(Fetish.FETISH_NON_CON_SUB))
								&& !partner.hasStatusEffect(StatusEffect.VIXENS_VIRILITY)
								&& this.hasItemType(ItemType.VIXENS_VIRILITY)) {
							if(partner.isPlayer()) {
								if(Main.sex.isForcingItemUse(this, partner)) {
									return new Value<>(AbstractItemType.generateItem(ItemType.VIXENS_VIRILITY),
											"Taking a small pink '[#ITEM_VIXENS_VIRILITY.getName(false)]' out of [npc.her] inventory, [npc.name] pops it out of its protective wrapper before reaching over and pushing it into your mouth."
											+ " Clasping [npc.her] [npc.hand] over your [pc.lips] to prevent you from spitting it out,"
												+ (this.isMute()
														?" [npc.name] lets out a commanding growl and refuses to let go until you've swallowed it down..."
														:" [npc.name] forces you to swallow it down and growls, [npc.speech(I want you to get me knocked up!)]"));
									
								} else {
									return new Value<>(AbstractItemType.generateItem(ItemType.VIXENS_VIRILITY),
											"Taking a small pink '[#ITEM_VIXENS_VIRILITY.getName(false)]' out of [npc.her] inventory, [npc.name] holds it out to you"
												+ (this.isMute()
														?" and makes a pleading whine as [npc.she] motions for you to swallow it..."
														:" and asks, [npc.speech(Please swallow this; I want you to knock me up!)]"));
								}
								
							} else {
								return new Value<>(AbstractItemType.generateItem(ItemType.VIXENS_VIRILITY), ""); // Description is appended in the SexAction
							}
						}
					}
				}
				if(charactersThisNpcIsPenetrating.contains(partner) && this.hasPenisIgnoreDildo()) { // Pills for when this NPC is penetrating the partner:
					if(partner.isAbleToAccessCoverableArea(CoverableArea.MOUTH, false)) {
						if(!Main.sex.getItemUseDenials(this, partner).contains(ItemType.PROMISCUITY_PILL)) {
							if(this.getFetishDesire(Fetish.FETISH_IMPREGNATION).isNegative()
									&& !partner.isPregnant()
									&& (Main.sex.getSexPace(this)!=SexPace.SUB_RESISTING || this.hasFetish(Fetish.FETISH_NON_CON_SUB))
									&& !partner.hasStatusEffect(StatusEffect.PROMISCUITY_PILL)
									&& this.hasItemType(ItemType.PROMISCUITY_PILL)) {
								if(partner.isPlayer()) {
									if(Main.sex.isForcingItemUse(this, partner)) {
										return new Value<>(AbstractItemType.generateItem(ItemType.PROMISCUITY_PILL),
												"Taking a small blue '[#ITEM_PROMISCUITY_PILL.getName(false)]' out of [npc.her] inventory, [npc.name] pops it out of its protective wrapper before reaching over and pushing it into your mouth."
												+ " Clasping [npc.her] [npc.hand] over your [pc.lips] to prevent you from spitting it out,"
													+ (this.isMute()
															?" [npc.name] lets out a commanding growl and refuses to let go until you've swallowed it down..."
															:" [npc.name] forces you to swallow it down and growls, [npc.speech(I don't want to knock you up!)]"));
										
									} else {
										return new Value<>(AbstractItemType.generateItem(ItemType.PROMISCUITY_PILL),
												"Taking a small blue '[#ITEM_PROMISCUITY_PILL.getName(false)]' out of [npc.her] inventory, [npc.name] holds it out to you"
													+ (this.isMute()
															?" and makes a pleading whine as [npc.she] motions for you to swallow it..."
															:" and asks, [npc.speech(Please swallow this; I don't want to knock you up!)]"));
									}
									
								} else {
									return new Value<>(AbstractItemType.generateItem(ItemType.PROMISCUITY_PILL), ""); // Description is appended in the SexAction
								}
							}
						}
						if(this.getFetishDesire(Fetish.FETISH_IMPREGNATION).isPositive()
								&& !partner.isPregnant()
								&& (Main.sex.getSexPace(this)!=SexPace.SUB_RESISTING || this.hasFetish(Fetish.FETISH_NON_CON_SUB))
								&& !partner.hasStatusEffect(StatusEffect.VIXENS_VIRILITY)
								&& this.hasItemType(ItemType.VIXENS_VIRILITY)) {
							if(partner.isPlayer()) {
								if(Main.sex.isForcingItemUse(this, partner)) {
									return new Value<>(AbstractItemType.generateItem(ItemType.VIXENS_VIRILITY),
											"Taking a small pink '[#ITEM_VIXENS_VIRILITY.getName(false)]' out of [npc.her] inventory, [npc.name] pops it out of its protective wrapper before reaching over and pushing it into your mouth."
											+ " Clasping [npc.her] [npc.hand] over your [pc.lips] to prevent you from spitting it out,"
												+ (this.isMute()
														?" [npc.name] lets out a commanding growl and refuses to let go until you've swallowed it down..."
														:" [npc.name] forces you to swallow it down and growls, [npc.speech(I'm going to get you knocked up!)]"));
									
								} else {
									return new Value<>(AbstractItemType.generateItem(ItemType.VIXENS_VIRILITY),
											"Taking a small pink '[#ITEM_VIXENS_VIRILITY.getName(false)]' out of [npc.her] inventory, [npc.name] holds it out to you"
												+ (this.isMute()
														?" and makes a pleading whine as [npc.she] motions for you to swallow it..."
														:" and asks, [npc.speech(Please swallow this; I want to knock you up!)]"));
								}
								
							} else {
								return new Value<>(AbstractItemType.generateItem(ItemType.VIXENS_VIRILITY), ""); // Description is appended in the SexAction
							}
						}
					}
				}
			}
		}
		
		return null;
	}
	
	public boolean getSexBehaviourDeniesRequests(SexType sexTypeRequest) {
		boolean isConvincing = Main.game.getPlayer().hasPerkAnywhereInTree(Perk.CONVINCING_REQUESTS);
		
		if(Main.game.isInSex() && !isConvincing) {
			if(Main.sex.getSexControl(Main.game.getPlayer()).getValue()<=SexControl.ONGOING_PLUS_LIMITED_PENETRATIONS.getValue() && Main.sex.getSexPace(this)==SexPace.DOM_ROUGH) {
				return true;
			}
		}
		
		int weight = calculateSexTypeWeighting(sexTypeRequest, Main.game.getPlayer(), null);
		
		return weight<0 || (!isConvincing && this.hasFetish(Fetish.FETISH_SADIST));
	}
	

	/**
	 * @param position The position to check.
	 * @param slot The slot to check.
	 * @param slot The target's slot to check.
	 * @param target The person who is being interacted with in this slot.
	 * @return Whether this NPC is happy to be in this SexSlot
	 */
	public boolean isHappyToBeInSlot(AbstractSexPosition position, SexSlot slot, SexSlot targetSlot, GameCharacter target) {
		SexType targetSexPreference = this.getForeplayPreference(target);
		if(!Main.sex.isInForeplay(this)) {
			targetSexPreference = this.getMainSexPreference(target);
		}
		if(targetSexPreference==null) {
			return true;
		}
		return slot.isMeetsPreferenceCriteria(this, position, targetSlot, targetSexPreference);
	}
	
	public boolean isHappyToBeInSlot(AbstractSexPosition position, SexSlot slot, GameCharacter target) {
		return isHappyToBeInSlot(position, slot, null, target);
	}

	/**
	 * Override to force this character to use a certain SexPace in sex. Return null to use default Pace calculations.
	 */
	public SexPace getSexPaceSubPreference(GameCharacter character){
		return null;
	}

	// The methods of theoretical sex paces should be applicable to all those branches of thought in which the essential features are expressible with fetishes, arousal, and lust.
	public SexPace getTheoreticalSexPaceSubPreference(GameCharacter character) {
		if(!isAttractedTo(character) || this.hasFetish(Fetish.FETISH_NON_CON_SUB)) {
			if(Main.game.isNonConEnabled()) {
				if(isSlave()) {
					if(this.getObedienceValue()>=ObedienceLevel.POSITIVE_FIVE_SUBSERVIENT.getMinimumValue()) {
						return SexPace.SUB_EAGER;
						
					} else if(this.getObedienceValue()>=ObedienceLevel.POSITIVE_TWO_OBEDIENT.getMinimumValue()) {
						return SexPace.SUB_NORMAL;
					}
				}
				
				if (getHistory() == Occupation.NPC_PROSTITUTE) {
					if(Main.sex.isConsensual()) {
						return SexPace.SUB_NORMAL;
					}
				}
				
				return SexPace.SUB_RESISTING;
				
			} else {
				return SexPace.SUB_NORMAL;
				
			}
		}
		
		if(hasStatusEffect(StatusEffect.WEATHER_STORM_VULNERABLE)) { // If they're vulnerable to arcane storms, they will always be eager during a storm:
			return SexPace.SUB_EAGER;
		}
		
		if (hasFetish(Fetish.FETISH_SUBMISSIVE) // Subs like being sub I guess ^^
				|| (hasFetish(Fetish.FETISH_PREGNANCY) && character.hasPenisIgnoreDildo() && hasVagina()) // Want to get pregnant
				|| (hasFetish(Fetish.FETISH_IMPREGNATION) && character.hasVagina() && hasPenisIgnoreDildo()) // Want to impregnate player
				) {
			return SexPace.SUB_EAGER;
		}
		
		return SexPace.SUB_NORMAL;
	}
	
	/**
	 * Override to force this character to use a certain SexPace in sex. Return null to use default Pace calculations.
	 */
	public SexPace getSexPaceDomPreference(){
		return null;
	}
	
	// Most people don't have time to master the very lewd details of theoretical sex paces.
	public SexPace getTheoreticalSexPaceDomPreference() {
		if(hasStatusEffect(StatusEffect.FETISH_PURE_VIRGIN) || (hasFetish(Fetish.FETISH_SUBMISSIVE) && !hasFetish(Fetish.FETISH_DOMINANT))) {
			return SexPace.DOM_GENTLE;
		}
		
		if(hasFetish(Fetish.FETISH_SADIST) || hasFetish(Fetish.FETISH_DOMINANT)) {
			return SexPace.DOM_ROUGH;
		}
		
		return SexPace.DOM_NORMAL;
	}
	
	public List<Class<?>> getUniqueSexClasses() {
		return new ArrayList<>();
	}
	
	/**
	 * Override this method and return a non-null list of SexActionInterfaces in order to limit what actions are available to this character during sex. For an example, see the Amber class.
	 * Use the <b>getSexActionInterfacesFromClass()</b> helper method to add all SexActionInterfaces from a containing class.
	 */
	public List<SexActionInterface> getLimitedSexClasses() {
		return null;
	}
	
	/**
	 * Helper method for the getLimitedSexClasses() method. Extracts all SexActionInterfaces from a class, and returns them in a list.
	 */
	protected List<SexActionInterface> getSexActionInterfacesFromClass(Class<?> classToAddSexActionsFrom) {
		List<SexActionInterface> actions = new ArrayList<>();
		Field[] fields = classToAddSexActionsFrom.getFields();
		
		for(Field f : fields){
			if (SexAction.class.isAssignableFrom(f.getType())) {
				try {
					SexAction action = ((SexAction) f.get(null));
					actions.add(action);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return actions;
	}
	
	/**
	 * Override this to set a preferred target for this character in sex. If there is an orgasm happening, and the returned character is not an orgasming character, this preference will be ignored.
	 */
	public GameCharacter getPreferredSexTarget() {
		return null;
	}

	/**
	 * This method determines how this NPC reacts to items being used on them.
	 * 
	 * @param item The item being used.
	 * @param itemOwner The owner of the item (so the game knows whose inventory to take it out of).
	 * @param user The character using the item on the target.
	 * @param target The character who is going to be experiencing the effects of the item's use.
	 * @return A Value whose Key is a Boolean of whether this use was successful or not, and whose Value is a description of what happened.
	 */
	public Value<Boolean, String> getItemUseEffects(AbstractItem item, GameCharacter itemOwner, GameCharacter user, GameCharacter target) {
		if(!user.equals(target)) { // Item is not being self-used:
			boolean isItemOrdinary = !item.getItemType().isTransformative() && !item.getItemType().isFetishGiving();
			
			if(target.isElemental()) {
				if(item.getItemType().isTransformative()) {
					return new Value<>(false,
							UtilText.parse(user, target,
							"<p>"
								+ "As [npc.name] [npc.verb(move)] to get [npc2.name] to "+item.getItemType().getUseName()+" the "+item.getName()+", [npc2.she] calmly states,"
									+ " [npc2.speech(Being an elemental, I am unable to "+item.getItemType().getUseName()+" that.)]"
							+ "</p>"
							+ "<p>"
								+ "[npc.Name] [npc.verb(put)] the "+item.getName()+" back in [npc.her] inventory."
							+ "</p>"));
				} else {
					return new Value<>(true, itemOwner.useItem(item, target, false));
				}
				
			} else if(isItemOrdinary
					|| (!target.isUnique() && !Main.game.isInCombat() && Combat.getAllCombatants(true).contains(user) && Combat.isCharacterVictory(user) && Combat.getEnemies(user).contains(target))
					|| (target.isSlave() && target.getOwner()!=null && target.getOwner().equals(user))) {
				return new Value<>(true, this.getItemUseEffectsAllowingUse(item, itemOwner, user, target));
				
			} else if(!target.isUnique()
						&& ((target.hasStatusEffect(StatusEffect.DRUNK_5)
								|| target.hasStatusEffect(StatusEffect.DRUNK_4)
								|| target.hasStatusEffect(StatusEffect.PSYCHOACTIVE))
							|| target.getAffectionLevel(user)==AffectionLevel.POSITIVE_FIVE_WORSHIP
							|| (target.getFetishDesire(Fetish.FETISH_TRANSFORMATION_RECEIVING).isPositive() && item.getItemType().isTransformative())
							|| (target.getFetishDesire(Fetish.FETISH_KINK_RECEIVING).isPositive() && item.getItemType().isFetishGiving())
							|| (Main.game.isInSex() && !Main.sex.isConsensual() && Main.sex.isDom(user) && !Main.sex.isDom(target)))) {
				return new Value<>(true, this.getItemUseEffectsAllowingUse(item, itemOwner, user, target));
				
			} else {
				if(item.getItemType().isTransformative()) {
					return new Value<>(false,
							UtilText.parse(user, target,
							"<p>"
								+ "[npc.Name] [npc.verb(try)] to give [npc2.name] [npc.her] "+item.getName()+", but [npc2.she] [npc2.verb(take)] one look at it and [npc2.verb(laugh)],"
								+ " [npc2.speech(Hah! Nice try, but do you really expect me to drink some random potion?!)]<br/>"
								+ "[npc.Name] reluctantly [npc.verb(put)] the "+item.getName()+" back in [npc.her] inventory, disappointed that [npc2.nameIsFull] not interested."
							+ "</p>"));
				} else {
					return new Value<>(false,
							UtilText.parse(user, target,
							"<p>"
								+ "[npc.Name] [npc.verb(try)] to give [npc2.name] [npc.her] "+item.getName()+", but [npc2.she] refuses to take it."
								+ " [npc.Name] reluctantly [npc.verb(put)] the "+item.getName()+" back in [npc.her] inventory."
							+ "</p>"));
				}
			}
		
		} else { // Self-use always succeeds:
			return new Value<>(true, itemOwner.useItem(item, target, false));
		}
	}
	
	protected String getItemUseEffectsAllowingUse(AbstractItem item, GameCharacter itemOwner, GameCharacter user, GameCharacter target) {
		StringBuilder sb = new StringBuilder();
		
		boolean isObedientSlave = target.isSlave() && target.getObedienceBasic()==ObedienceLevelBasic.OBEDIENT;

		if(!user.equals(target)) { // Item is not being self-used:
			if(item.getItemType().equals(ItemType.PROMISCUITY_PILL)) {
				sb.append(UtilText.parse(user, target,
						"<p>"
							+ "Holding out a '[#ITEM_PROMISCUITY_PILL.getName(false)]' to [npc2.name], [npc.name] [npc.verb(tell)] [npc2.herHim] to swallow it so that [npc.she] [npc.does]'t have to worry about any unexpected pregnancies."));
				
				if(isObedientSlave) {
					sb.append(UtilText.parse(user, target, 
							" Obediently doing what's asked of [npc2.herHim], [npc2.she] happily [npc2.verb(take)] the pill out of [npc.namePos] [npc.hand], and quickly [npc2.verb(pop)] it out of its wrapping before swallowing it down."));
					
				} else if((target.hasFetish(Fetish.FETISH_IMPREGNATION) && target.hasPenis())
						|| (target.hasFetish(Fetish.FETISH_PREGNANCY) && target.hasVagina())) {
					sb.append(UtilText.parse(user, target,
							" Letting out an annoyed sigh, [npc2.she] nevertheless [npc2.verb(take)] the pill out of [npc.namePos] [npc.hand], and after popping it out of its wrapper, [npc2.she] [npc2.verb(swallow)] it down and [npc2.verb(whine)],"
							+ " [npc2.speech(What's even the point if nobody's going to get pregnant?)]"));
					
				} else {
					sb.append(UtilText.parse(user, target, 
							" Letting out a relieved sigh, [npc2.she] happily [npc2.verb(take)] the pill out of [npc.namePos] [npc.hand], before quickly popping it out of its wrapper and swallowing it down."));
				}
				
				sb.append("</p>");
				
				sb.append(itemOwner.useItem(item, target, false, true));
				
				return sb.toString();
				
			} else if(item.getItemType().equals(ItemType.VIXENS_VIRILITY)) {
				sb.append(UtilText.parse(user, target,
						"<p>"
							+ "Holding out a '[#ITEM_VIXENS_VIRILITY.getName(false)]' to [npc2.name], [npc.name] [npc.verb(tell)] [npc2.herHim] to swallow it in order to boost the chance of a successful impregnation."));
				
				if(isObedientSlave) {
					sb.append(UtilText.parse(user, target, 
							" Obediently doing what's asked of [npc2.herHim], [npc2.she] happily [npc2.verb(take)] the pill out of [npc.namePos] [npc.hand], and quickly [npc2.verb(pop)] it out of its wrapping before swallowing it down."));
					
				} else if((target.hasFetish(Fetish.FETISH_IMPREGNATION) && target.hasPenis())
						|| (target.hasFetish(Fetish.FETISH_PREGNANCY) && target.hasVagina())) {
					sb.append(UtilText.parse(user, target, 
							" Letting out a delighted cry, [npc2.she] enthusiastically [npc2.verb(snatch)] the pill out of [npc.namePos] [npc.hand], and after popping it out of its wrapper,"
							+ " [npc2.she] quickly [npc2.verb(swallow)] it down and [npc2.verb(exclaim)],"
							+ " [npc2.speech(Let's make some kids together!)]"));
					
				} else {
					sb.append(UtilText.parse(user, target, 
							" Letting out a hesitant sigh, [npc2.she] nevertheless [npc2.verb(take)] the pill out of [npc.namePos] [npc.hand], before quickly popping it out of its wrapper and swallowing it down."));
				}
				
				sb.append("</p>");
				
				sb.append(itemOwner.useItem(item, target, false, true));
				
				return sb.toString();
					
			} else if(item.getItemType().equals(ItemType.ELIXIR)) {
				sb.append(UtilText.parse(user, target,
						"<p>"
							+ "Taking [npc.her] "+item.getName()+" from out of [npc.her] inventory, [npc.name] [npc.verb(hold)] it out to [npc2.name]."));
				
				if(isObedientSlave) {
					sb.append(UtilText.parse(user, target, 
								" Obediently doing what's expected of [npc2.herHim], [npc2.she] [npc2.verb(take)] the bottle of transformative fluid from [npc.name] and [npc2.verb(say)], "
								+ " [npc2.speech(Of course I'll do my duty and be transformed into whatever form you desire...)]"
							+ "</p>"
							+ "<p>"
								+ "Eager to please, [npc2.she] then [npc2.verb(remove)] the bottle's stopper, before lifting it up to [npc2.her] waiting [npc2.lips] and gulping down all of the liquid contained within."
								+ " [npc2.She] [npc2.verb(cough)] and [npc2.verb(splutter)] for a moment, before taking a deep gasp as [npc2.she] [npc2.verb(start)] to feel the liquid's effects taking root deep in [npc2.her] body..."
							+ "</p>"));
					
				} else if(target.getRace()==Race.DEMON) {
					sb.append(UtilText.parse(user, target,
						"<p>"
							+ "Taking [npc.her] "+item.getName()+" from out of [npc.her] inventory, [npc.name] [npc.verb(hold)] it out to [npc2.name]."
							+ " Seeing what it is that [npc.nameIs] offering [npc2.herHim], [npc2.she] [npc2.verb(let)] out a mocking laugh, "
							+ " [npc2.speech(Hah! Don't you know demons can't be transfo- ~Mrph!~)]"
						+ "</p>"
						+ "<p>"
							+ "Not liking the start of [npc2.namePos] response, [npc.name] quickly [npc.verb(remove)] the bottle's stopper, before rather unceremoniously shoving the neck down [npc2.her] throat."
							+ " Pinching [npc2.her] nose and holding [npc2.herHim] still, [npc.name] [npc.verb(make)] sure to force [npc2.name] to down all of the liquid before finally letting [npc2.herHim] go."
							+ " [npc2.She] [npc2.verb(cough)] and [npc2.verb(splutter)] for a moment, before letting out an annoyed grumble as [npc2.she] [npc2.verb(wipe)] the liquid from [npc2.her] mouth,"
							+ " [npc2.speech(It doesn't taste too bad...)]"
						+ "</p>"));
					
							
				} else if(target.hasFetish(Fetish.FETISH_TRANSFORMATION_RECEIVING)) {
					sb.append(UtilText.parse(user, target, 
							" Seeing what it is that [npc.nameIs] offering [npc2.herHim], [npc2.she] [npc2.verb(let)] out a delighted cry and [npc2.verb(ask)], "
							+ " [npc2.speech(Is that a transformation elixir?! Please, let me drink it! Change me into whatever you want!)]"
						+ "</p>"
						+ "<p>"
							+ "Smiling as [npc.she] [npc.verb(hear)] [npc2.namePos] enthusiastic response, [npc.name] quickly [npc.verb(remove)] the bottle's stopper, before bringing the potion up to the eager [npc2.race]'s mouth."
							+ " Happily wrapping [npc2.her] [npc2.lips] around the bottle's opening, [npc2.name] [npc2.verb(gulp)] down all of the liquid in one huge swig."
							+ " [npc2.She] [npc2.verb(cough)] and [npc2.verb(splutter)] for a moment, before letting out an ecstatic cry as [npc2.she] [npc2.verb(start)] to feel the liquid's effects taking root deep in [npc2.her] body..."
						+ "</p>"));
					
				} else {
					if(target.getAffection(user) < AffectionLevel.POSITIVE_FIVE_WORSHIP.getMinimumValue()) {
						sb.append(UtilText.parse(user, target, 
								" Seeing what it is that [npc.nameIs] offering [npc2.herHim], [npc2.she] [npc2.verb(shift)] about uncomfortably and [npc2.verb(ask)], "
								+ " [npc2.speech(You don't seriously expect me to drink some rando- ~Mrph!~)]"
							+ "</p>"
							+ "<p>"
								+ "Not liking the start of [npc2.her] response, [npc.name] quickly [npc.verb(remove)] the bottle's stopper, before rather unceremoniously shoving the neck down [npc2.her] throat."
								+ " Pinching [npc2.her] nose and holding [npc2.herHim] still, [npc.name] [npc.verb(make)] sure to force [npc2.name] to down all of the liquid before finally letting [npc2.herHim] go."
								+ " [npc2.She] [npc2.verb(cough)] and [npc2.verb(splutter)] for a moment, before letting out a surprised cry as [npc.she] [npc2.verb(start)] to feel the liquid's effects taking root deep in [npc2.her] body..."
							+ "</p>"));
						
					} else {
						sb.append(UtilText.parse(user, target,
								" As [npc2.she] [npc2.verb(like)] [npc.name] so much, [npc2.she] [npc2.verb(put)] aside [npc2.her] worries of being transformed and [npc2.verb(say)],"
								+ " [npc2.speech(Of course I'll drink it! I'll do anything for you...)]"
							+ "</p>"
							+ "<p>"
								+ "Hearing [npc2.namePos] willing response, [npc.name] [npc.verb(remove)] the bottle's stopper, before handing it over to [npc2.herHim]."
								+ " Eager to please, [npc2.she] happily [npc2.verb(wrap)] [npc2.her] [npc2.lips] around the bottle's opening, before gulping down all of the liquid in one huge swig."
								+ " [npc2.She] [npc2.verb(cough)] and [npc2.verb(splutter)] for a moment, before letting out a startled cry as [npc.she] [npc2.verb(start)] to feel the liquid's effects taking root deep in [npc2.her] body..."
							+ "</p>"));
					}
				}
				
				sb.append(itemOwner.useItem(item, target, false, true));
				
				return sb.toString();
					
			} else if(item.getItemType().equals(ItemType.FETISH_UNREFINED) || item.getItemType().equals(ItemType.FETISH_REFINED)) {
				sb.append(UtilText.parse(user, target,
						"<p>"
							+ "Taking [npc.her] "+item.getName()+" from out of [npc.her] inventory, [npc.name] [npc.verb(hold)] it out to [npc2.name]."));
				
				if(isObedientSlave) {
					sb.append(UtilText.parse(user, target, 
								" Obediently doing what's expected of [npc2.herHim], [npc2.she] [npc2.verb(take)] the bottle of fetish-modifying fluid from [npc.name] and [npc2.verb(say)], "
								+ " [npc2.speech(Of course I'll do my duty in becoming the sort of slave you desire...)]"
							+ "</p>"
							+ "<p>"
								+ "Eager to please, [npc2.she] then [npc2.verb(remove)] the bottle's stopper, before lifting it up to [npc2.her] waiting [npc2.lips] and gulping down all of the liquid contained within."
								+ " [npc2.She] [npc2.verb(cough)] and [npc2.verb(splutter)] for a moment, before taking a deep gasp as [npc2.she] [npc2.verb(start)] to feel the liquid's effects taking root deep in [npc2.her] mind..."
							+ "</p>"));
					
				} else if(target.hasFetish(Fetish.FETISH_KINK_RECEIVING)) {
					sb.append(UtilText.parse(user, target, 
							" Seeing what it is that [npc.nameIs] offering [npc2.herHim], [npc2.she] [npc2.verb(let)] out a delighted cry and [npc2.verb(ask)], "
							+ " [npc2.speech(Is that "+UtilText.generateSingularDeterminer(item.getName())+" "+item.getName()+"?! Please, let me drink it!)]"
						+ "</p>"
						+ "<p>"
							+ "Smiling as [npc.she] [npc.verb(hear)] [npc2.namePos] enthusiastic response, [npc.name] quickly [npc.verb(remove)] the bottle's stopper, before bringing the potion up to the eager [npc2.race]'s mouth."
							+ " Happily wrapping [npc2.her] [npc2.lips] around the bottle's opening, [npc2.name] [npc2.verb(gulp)] down all of the liquid in one huge swig."
							+ " [npc2.She] [npc2.verb(cough)] and [npc2.verb(splutter)] for a moment, before letting out an ecstatic cry as [npc2.she] [npc2.verb(start)] to feel the liquid's effects taking root deep in [npc2.her] mind..."
						+ "</p>"));
					
				} else {
					if(target.getRace()==Race.DEMON) {
						sb.append(UtilText.parse(user, target, 
								" Seeing what it is that [npc.nameIs] offering [npc2.herHim], [npc2.she] [npc2.verb(let)] out a mocking laugh, "
								+ " [npc2.speech(Hah! Don't you know demons can't be transfo- ~Mrph!~)]"
							+ "</p>"
							+ "<p>"
								+ "Not liking the start of [npc2.namePos] response, [npc.name] quickly [npc.verb(remove)] the bottle's stopper, before rather unceremoniously shoving the neck down [npc2.her] throat."
								+ " Pinching [npc2.her] nose and holding [npc2.herHim] still, [npc.name] [npc.verb(make)] sure to force [npc2.name] to down all of the liquid before finally letting [npc2.herHim] go."
								+ " [npc2.She] [npc2.verb(cough)] and [npc2.verb(splutter)] for a moment, before letting out a lewd [npc2.moan] as [npc2.she] [npc2.verb(wipe)] the liquid from [npc2.her] mouth,"
								+ " [npc.speech(~Aah!~ Hey, that was a fetish transformative, wasn't it?! ~Ooh!~ I feel hot...)]"
							+ "</p>"));
						
					} else {
						if(target.getAffection(user) < AffectionLevel.POSITIVE_FIVE_WORSHIP.getMinimumValue()) {
							sb.append(UtilText.parse(user, target, 
									" Seeing what it is that [npc.nameIs] offering [npc2.herHim], [npc2.she] [npc2.verb(shift)] about uncomfortably and [npc2.verb(ask)], "
									+ " [npc2.speech(You don't seriously expect me to drink some rando- ~Mrph!~)]"
								+ "</p>"
								+ "<p>"
									+ "Not liking the start of [npc2.her] response, [npc.name] quickly [npc.verb(remove)] the bottle's stopper, before rather unceremoniously shoving the neck down [npc2.her] throat."
									+ " Pinching [npc2.her] nose and holding [npc2.herHim] still, [npc.name] [npc.verb(make)] sure to force [npc2.name] to down all of the liquid before finally letting [npc2.herHim] go."
									+ " [npc2.She] [npc2.verb(cough)] and [npc2.verb(splutter)] for a moment, before letting out a surprised cry as [npc.she] [npc2.verb(start)] to feel the liquid's effects taking root deep in [npc2.her] mind..."
								+ "</p>"));
							
						} else {
							sb.append(UtilText.parse(user, target,
									" As [npc2.she] [npc2.verb(like)] [npc.name] so much, [npc2.she] [npc2.verb(put)] aside [npc2.her] worries of being transformed and [npc2.verb(say)],"
										+ " [npc2.speech(Of course I'll drink it! I'll do anything for you...)]"
								+ "</p>"
								+ "<p>"
									+ "Hearing [npc2.namePos] willing response, [npc.name] [npc.verb(remove)] the bottle's stopper, before handing it over to [npc2.herHim]."
									+ " Eager to please, [npc2.she] happily [npc2.verb(wrap)] [npc2.her] [npc2.lips] around the bottle's opening, before gulping down all of the liquid in one huge swig."
									+ " [npc2.She] [npc2.verb(cough)] and [npc2.verb(splutter)] for a moment, before letting out a startled cry as [npc.she] [npc2.verb(start)] to feel the liquid's effects taking root deep in [npc2.her] mind..."
								+ "</p>"));
						}
					}
				}
				sb.append(itemOwner.useItem(item, target, false, true));
				
				return sb.toString();
				
			} else if(item.getItemType().equals(ItemType.POTION) || item.getItemType().equals(ItemType.EGGPLANT_POTION) || item.getItemType().equals(ItemType.MOTHERS_MILK)) {
				if(isObedientSlave) {
					sb.append(UtilText.parse(user, target,
							"<p>"
								+ "Taking [npc.her] "+item.getName()+" from out of [npc.her] inventory, [npc.name] [npc.verb(hold)] it out to [npc2.name]."
								+ " Obediently doing what's expected of [npc2.herHim], [npc2.she] [npc2.verb(take)] the bottle of fluid from [npc.name] and [npc2.verb(say)], "
								+ " [npc2.speech(Of course I'll drink whatever it is you give to me...)]"
							+ "</p>"
							+ "<p>"
								+ "Eager to please, [npc2.she] then [npc2.verb(remove)] the bottle's stopper, before lifting it up to [npc2.her] waiting [npc2.lips] and gulping down all of the liquid contained within."
								+ " [npc2.She] [npc2.verb(cough)] and [npc2.verb(splutter)] for a moment, before taking a deep gasp as [npc2.she] [npc2.verb(start)] to feel the liquid's effects taking effect..."
							+ "</p>"));
					
				} else {
					sb.append(UtilText.parse(user, target,
							"<p>"
								+ "Taking [npc.her] "+item.getName()+" from out of [npc.her] inventory, [npc.name] [npc.verb(hold)] it out to [npc2.name]."
								+ " Seeing what it is that [npc.nameIs] offering [npc2.herHim], [npc2.she] [npc2.verb(shift)] about uncomfortably and [npc2.verb(ask)], "
								+ " [npc2.speech(You don't seriously expect me to drink some rando- ~Mrph!~)]"
							+ "</p>"
							+ "<p>"
								+ "Not liking the start of [npc2.her] response, [npc.name] quickly [npc.verb(remove)] the bottle's stopper, before rather unceremoniously shoving the neck down [npc2.her] throat."
								+ " Pinching [npc2.her] nose and holding [npc2.herHim] still, [npc.name] [npc.verb(make)] sure to force [npc2.name] to down all of the liquid before finally letting [npc2.herHim] go."
								+ " [npc2.She] [npc2.verb(cough)] and [npc2.verb(splutter)] for a moment, before letting out a surprised cry as [npc.she] [npc2.verb(start)] to feel the liquid's effects taking root deep in [npc2.her] mind..."
							+ "</p>"));
				}
				sb.append(itemOwner.useItem(item, target, false, true));

				return sb.toString();
				
			} else if(item.getItemType().equals(ItemType.EGGPLANT)) {
				if(isObedientSlave) {
					sb.append(UtilText.parse(user, target,
							"<p>"
								+ "Taking the eggplant from out of [npc.her] inventory, [npc.name] [npc.verb(hold)] it out to [npc2.name]."
								+ " Seeing what it is that [npc.nameIs] offering [npc2.herHim], [npc2.she] obediently [npc2.verb(take)] it and [npc2.verb(say)], "
								+ " [npc2.speech(Of course I'll eat whatever it is you give to me...)]"
							+ "</p>"
							+ "<p>"
								+ "Eager to please, [npc2.she] [npc2.verb(lift)] the purple fruit up to [npc2.her] waiting [npc2.lips] and [npc2.verb(munch)] down every last bit of it..."
							+ "</p>"));
					
				} else {
					sb.append(UtilText.parse(target,
							"<p>"
								+ "Taking the eggplant from out of [npc.her] inventory, [npc.name] [npc.verb(hold)] it out to [npc2.name]."
								+ " Seeing what it is that [npc.nameIs] offering [npc2.herHim], [npc2.she] [npc2.verb(shift)] about uncomfortably and [npc2.verb(ask)], "
								+ " [npc2.speech(What are you going to do with th- -~Mrph!~)]"
							+ "</p>"
							+ "<p>"
								+ "Not liking the start of [npc2.her] response, [npc.name] quickly [npc.verb(shove)] the eggplant into [npc2.namePos] mouth, grinning as [npc.she] [npc.verb(force)] [npc2.herHim] to eat every last bit of the purple fruit..."
							+ "</p>"));
				}
				sb.append(itemOwner.useItem(item, target, false, true));

				return sb.toString();
				
			} else {
				return itemOwner.useItem(item, target, false);
			}
			
		} else { // Self-using:
			return itemOwner.useItem(item, target, false);
		}
	}

}
