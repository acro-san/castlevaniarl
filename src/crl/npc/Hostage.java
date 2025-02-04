package crl.npc;

import crl.data.Text;
import crl.item.Item;
import sz.util.*;

public class Hostage extends NPC {
	
	public int reward;
	public Item itemReward;
	private String hostrinchLevel;
	private String rescuedMessage;
	private boolean rescued;
	
	public boolean isRescued() {
		return rescued;
	}

	public void setRescued(boolean rescued) {
		this.rescued = rescued;
		rescuedMessage = Util.pick(Text.HOSTAGE_RESCUE_TIPS);
	}
	
	public String getTalkMessage() {
		if (!rescued)
			return super.getTalkMessage();
		else
			return rescuedMessage;
	}

	public Hostage(NPCDef def) {
		super(def);
	}
	/*
	 * public int getReward() { return reward; }
	 * 
	 * public void setReward(int l) { reward = l; }
	 */

	public String getHostrinchLevel() {
		return hostrinchLevel;
	}

	public void setHostrinchLevel(String hostrinchLevel) {
		this.hostrinchLevel = hostrinchLevel;
	}

	/*
	public Item getItemReward() {
		return itemReward;
	}

	public void setItemReward(Item itemReward) {
		this.itemReward = itemReward;
	}
	*/
	
}
