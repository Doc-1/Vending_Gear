package com.docvin.vending_gear.geckolib3.model;

import javax.annotation.Nullable;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public abstract class CustomAnimatedGeoModel<T extends IAnimatable> extends AnimatedGeoModel<T> {

	@Override
	public void setLivingAnimations(T entity, Integer uniqueID,
			@SuppressWarnings("rawtypes") @Nullable AnimationEvent customPredicate) {
		// TODO Auto-generated method stub
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		setCustomAnimations(entity, 0, customPredicate);
	}

	public void setCustomAnimations(T animatable, long instanceId, AnimationEvent animationEvent) {

	}

}
