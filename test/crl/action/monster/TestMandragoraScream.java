package crl.action.monster;

import static org.mockito.Mockito.*;
import static org.mockito.AdditionalMatchers.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sz.util.Position;
import crl.Main;
import crl.item.Item;
import crl.level.Level;
import crl.monster.Monster;
import crl.monster.VMonster;
import crl.player.Player;
import crl.ui.effects.EffectFactory;

public class TestMandragoraScream {

	EffectFactory effectFactory;
	MandragoraScream action;
	Monster performer;
	Position performerPosition;
	Player player;
	Position playerPosition;
	Level level;
	VMonster monsters;
	
	@Before
	public void setUp() throws Exception {
		effectFactory = mock(EffectFactory.class);
		Main.efx = effectFactory;
		
		monsters = mock(VMonster.class);
		
		level = mock(Level.class);
		when(level.getMonsters()).thenReturn(monsters);
		when(level.isDay()).thenReturn(true);
		
		playerPosition = new Position(8, 8);
		player = new Player();
		player.pos = playerPosition;
		player.level = level;
		player.setHPMax(30);
		player.setHP(30);
		when(level.getPlayer()).thenReturn(player);
		
		performerPosition = new Position(10, 10);
		performer = mock(Monster.class);
		when(performer.level).thenReturn(level);
		when(performer.pos).thenReturn(performerPosition);
		
		action = new MandragoraScream();
		action.setPerformer(performer);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void pullingMandragoraRootShouldSetFlag() {
		when(performer.getFlag("MANDRAGORA_PULLED")).thenReturn(false);
		
		action.execute();
		
		verify(performer).setFlag("MANDRAGORA_PULLED", true);
	}
	
	@Test
	public void pullingMandragoraRootShouldEmitAMessage() {
		when(performer.getFlag("MANDRAGORA_PULLED")).thenReturn(false);
		
		action.execute();
		
		verify(level).addMessage(find("pulls out a mandragora"));
	}

	@Test
	public void mandragoraScreamShouldIgnoreArmor() {
		when(performer.getFlag("MANDRAGORA_PULLED")).thenReturn(true);
		
		int oldHits = player.getHP();
		Item armour = mock(Item.class);
		when(armour.getDefense()).thenReturn(10);
		
		player.armor = armour;
	
		action.execute();
		
		assertThat(player.getHP(), equalTo(oldHits - MandragoraScream.SCREAM_DAMAGE));
	}
}
