package crl.action;

import sz.util.Line;
import sz.util.Position;
import crl.feature.CountDown;
import crl.feature.SmartFeature;
import crl.feature.SmartFeatureFactory;
import crl.level.Level;

public class Throw extends Action {
	
	public AT getID() {
		return AT.Throw;
	}
	
	public boolean needsItem() {
		return true;
	}

	public String getPromptItem() {
		return "What do you want to throw?";
	}

	public boolean needsPosition() {
		return true;
	}

	public void execute() {
		Level aLevel = performer.level;
		performer.level.addMessage("You throw the "+targetItem.getDescription());

		//aLevel.addEffect(new PCMissileEffect(performer.pos, "+x", Appearance.CYAN, targetDirection, 10));
		int distance = Position.flatDistance(performer.pos, targetPosition);
		Position destinationPoint = null;
		if (distance > targetItem.getThrowRange()){
			distance = targetItem.getThrowRange();
		}
		//aLevel.addEffect(new LineIconMissileEffect(performer.pos, targetItem.getAppearance().getChar(), targetItem.getAppearance().getColor(), performer.pos, targetPosition, distance, 30));
		//Removed for not complexing the tier, would need to specifiy fx for each item aLevel.addEffect(EffectFactory.getSingleton().createTileMissileEffect(performer.pos, destinationPoint, "SFX_RED_HIT", distance));
		Line line = new Line(performer.pos, targetPosition);
		int i = 0;
		Position runner = line.next();
		for (i=0; i<distance; i++) {
			runner = line.next();
			if (!aLevel.isValidCoordinate(runner) || (aLevel.getMapCell(runner)!= null && aLevel.getMapCell(runner).isSolid())) {
				break;
			}
		}
		destinationPoint = runner;
		if (aLevel.getMapCell(destinationPoint) == null) {
			destinationPoint = aLevel.getDeepPosition(destinationPoint);
		}
		
		if (destinationPoint == null) {
			// The feature falls to the infinity
		} else {
			String placedSmartFeature = targetItem.getPlacedSmartFeature();
			if (!placedSmartFeature.equals("")) {
				SmartFeature feature = SmartFeatureFactory.buildFeature(placedSmartFeature);
				feature.pos = destinationPoint;
				((CountDown)feature.selector).setTurns(targetItem.getFeatureTurns());
				aLevel.addSmartFeature(feature);
				
				feature = SmartFeatureFactory.buildFeature(placedSmartFeature);
				feature.pos = Position.add(destinationPoint, Action.directionToVariation(Action.UP));
				((CountDown)feature.selector).setTurns(targetItem.getFeatureTurns());
				aLevel.addSmartFeature(feature);
				
				feature = SmartFeatureFactory.buildFeature(placedSmartFeature);
				feature.pos = Position.add(destinationPoint, Action.directionToVariation(Action.DOWN));
				((CountDown)feature.selector).setTurns(targetItem.getFeatureTurns());
				aLevel.addSmartFeature(feature);
				
				feature = SmartFeatureFactory.buildFeature(placedSmartFeature);
				feature.pos = Position.add(destinationPoint, Action.directionToVariation(Action.LEFT));
				((CountDown)feature.selector).setTurns(targetItem.getFeatureTurns());
				aLevel.addSmartFeature(feature);
				
				feature = SmartFeatureFactory.buildFeature(placedSmartFeature);
				feature.pos = Position.add(destinationPoint, Action.directionToVariation(Action.RIGHT));
				((CountDown)feature.selector).setTurns(targetItem.getFeatureTurns());
				aLevel.addSmartFeature(feature);
			} else {
				aLevel.addItem(destinationPoint, targetItem);
			}
		}
		performer.level.getPlayer().reduceQuantityOf(targetItem);
	}


	public String getPromptPosition() {
		return "Where do you want to throw the "+targetItem.getDefinition().description+"?";
	}


	public String getSFX() {
		return "wav/rich_yah.wav";
	}


}