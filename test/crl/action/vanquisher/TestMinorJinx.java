package crl.action.vanquisher;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import crl.item.Item;
import crl.level.Level;
import crl.player.Player;

/**
 * Tests for minor jinx
 * 
 * @author Tuukka Turto
 *
 */
public class TestMinorJinx {

	private Player player;
	private MinorJinx jinx;
	private Level level;
	
	@Before
	public void setUp() throws Exception {
		level = mock(Level.class);
		when(level.isDay()).thenReturn(true);
		
		player = new Player();
		player.setHeartMax(250);
		player.setHearts(50);
		player.setHPMax(30);
		player.setHP(30);
		player.level = level;
		
		jinx = new MinorJinx();
		jinx.setPerformer(player);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void castingMinorJinxShouldIncreaseHearts() {
		int oldHearts = player.getHearts();
		
		jinx.execute();
		
		assertThat(player.getHearts(), greaterThan(oldHearts));
	}
	
	@Test
	public void castingMinorJinxShouldDecreaseHP() {
		int oldHP = player.getHP();
		
		jinx.execute();
		
		assertThat(player.getHP(), lessThan(oldHP));
	}
	
	@Test
	public void armourShouldNotDecreaseDamageWhenCastingMinorJinx() {
		int oldHP = player.getHP();
		Item armour = mock(Item.class);
		when(armour.getDefense()).thenReturn(10);
		
		player.armor = armour;
		
		jinx.execute();
		
		assertThat(player.getHP(), equalTo(oldHP - 5));
	}

}
