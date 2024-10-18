package com.docvin.vending_gear.entities.vending_machine;

import com.creativemd.creativecore.common.packet.PacketHandler;
import com.docvin.vending_gear.packets.server.SparkPacket;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class VendingGearTankEntity extends EntityMob implements IRangedAttackMob, IAnimatable {

	private int explosionRadius = 3;
	private int fuseTime = 30;
	private int timeSinceIgnited = 0;

	protected static final AnimationBuilder HOVER_ANIM = new AnimationBuilder().addAnimation("animation.model.hover",
			ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);
	protected static final AnimationBuilder ATTACK_ANIM = new AnimationBuilder().addAnimation("animation.model.attack",
			ILoopType.EDefaultLoopTypes.PLAY_ONCE);

	private final AnimationFactory FACTORY = new AnimationFactory(this);

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		if (this.isFalling()) {
			event.getController().setAnimation(HOVER_ANIM);
		}
		return PlayState.CONTINUE;
	}

	public VendingGearTankEntity(World worldIn) {
		super(worldIn);
		this.setSize(1F, 1F);
	}

	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		compound.setShort("Fuse", (short) this.fuseTime);
		compound.setByte("ExplosionRadius", (byte) this.explosionRadius);
	}

	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		if (compound.hasKey("ExplosionRadius", 99)) {
			this.explosionRadius = compound.getByte("ExplosionRadius");
		}
		if (compound.hasKey("Fuse", 99)) {
			this.fuseTime = compound.getShort("Fuse");
		}
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		double d0 = target.posY + (double) target.getEyeHeight() - 1.100000023841858D;
		double d1 = target.posX + target.motionX - this.posX;
		double d2 = d0 - this.posY;
		double d3 = target.posZ + target.motionZ - this.posZ;
		float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
		PotionType potiontype = PotionTypes.HARMING;

		if (f >= 8.0F && !target.isPotionActive(MobEffects.SLOWNESS)) {
			potiontype = PotionTypes.SLOWNESS;
		} else if (target.getHealth() >= 8.0F && !target.isPotionActive(MobEffects.POISON)) {
			potiontype = PotionTypes.POISON;
		} else if (f <= 3.0F && !target.isPotionActive(MobEffects.WEAKNESS) && this.rand.nextFloat() < 0.25F) {
			potiontype = PotionTypes.WEAKNESS;
		}

		EntityPotion entitypotion = new EntityPotion(this.world, this,
				PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potiontype));
		entitypotion.rotationPitch -= -20.0F;
		entitypotion.shoot(d1, d2 + (double) (f * 0.2F), d3, 0.75F, 8.0F);
		this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.BLOCK_PISTON_EXTEND,
				this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
		this.world.spawnEntity(entitypotion);
	}

	private void explode() {
		if (!this.world.isRemote) {
			boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this);
			float f = 2.0F;
			this.dead = true;
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, (float) this.explosionRadius * f, flag);
			this.setDead();
		}
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (isFalling()) {
			this.motionY *= 0.5D;
		}
		if (!this.world.isRemote)
			if (this.isEntityAlive()) {
				this.timeSinceIgnited += this.isInWater() ? 1 : -1;

				if (this.timeSinceIgnited > 0 && this.timeSinceIgnited % 2 == 0)
					for (EntityPlayer entityplayer : this.world.playerEntities)
						if (entityplayer.getDistanceSq(this) < 4096.0D)
							PacketHandler.sendPacketToPlayer(new SparkPacket(this.posX, this.posY + 3.2, this.posZ),
									(EntityPlayerMP) entityplayer);

				if (this.timeSinceIgnited < 0)
					this.timeSinceIgnited = 0;
				else if (this.timeSinceIgnited >= this.fuseTime) {
					this.timeSinceIgnited = this.fuseTime;
					this.explode();
				}

			}
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		// TODO Auto-generated method stub

	}

	public boolean isFalling() {
		return !this.onGround && this.motionY < 0.0D;
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(2, new EntityAIAttackRanged(this, 0.3D, 60, 10.0F));
		this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 0.3D));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityMob>(this, EntityMob.class, true));
	}

	@Override
	public void registerControllers(final AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "contoller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return FACTORY;
	}
}
