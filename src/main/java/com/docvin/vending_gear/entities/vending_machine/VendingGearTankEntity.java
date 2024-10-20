package com.docvin.vending_gear.entities.vending_machine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.creativemd.creativecore.common.packet.PacketHandler;
import com.docvin.vending_gear.packets.server.JetBoostPacket;
import com.docvin.vending_gear.packets.server.SparkPacket;
import com.docvin.vending_gear.packets.server.StartAttackAnimationPacket;
import com.docvin.vending_gear.packets.server.StartJumpAnimationPacket;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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

	public boolean startJumpAnim = false;
	public boolean startAttackAnim = false;

	public boolean startAnimation = false;
	private int timeSinceFalling = 0;
	private int fallingTime = 120;
	private Vec3d originPos = null;
	private int distance = 20;

	protected static final AnimationBuilder JUMP_ANIM = new AnimationBuilder().addAnimation("animation.model.jump",
			ILoopType.EDefaultLoopTypes.PLAY_ONCE);
	protected static final AnimationBuilder ATTACK_ANIM = new AnimationBuilder().addAnimation("animation.model.attack",
			ILoopType.EDefaultLoopTypes.PLAY_ONCE);

	private final AnimationFactory FACTORY = new AnimationFactory(this);

	private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		event.getController().clearAnimationCache();
		if (this.startJumpAnim) {
			event.getController().setAnimation(JUMP_ANIM);
			this.startJumpAnim = false;
		}
		if (this.startAttackAnim) {
			event.getController().setAnimation(ATTACK_ANIM);
			this.startAttackAnim = false;
		}
		return PlayState.CONTINUE;
	}

	public VendingGearTankEntity(World worldIn) {
		super(worldIn);
		this.setSize(1.6F, 3.4F);
		this.enablePersistence();
		this.setPathPriority(PathNodeType.WATER, -1.0F);
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6000000238418579D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(2.0D);
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
		for (EntityPlayer entityplayer : this.world.playerEntities)
			if (entityplayer.getDistanceSq(this) < 4096.0D)
				PacketHandler.sendPacketToPlayer(new StartAttackAnimationPacket(this.getEntityId()),
						(EntityPlayerMP) entityplayer);
		double d0 = target.posY + (double) target.getEyeHeight();
		double d1 = target.posX + target.motionX - this.posX;
		double d2 = d0 - this.posY;
		double d3 = target.posZ + target.motionZ - this.posZ;
		float f = MathHelper.sqrt(d1 * d1 + d3 * d3);

		List<PotionType> list = new ArrayList<PotionType>();
		list.add(PotionTypes.AWKWARD);
		list.add(PotionTypes.FIRE_RESISTANCE);
		list.add(PotionTypes.HARMING);
		list.add(PotionTypes.HEALING);
		list.add(PotionTypes.INVISIBILITY);
		list.add(PotionTypes.LEAPING);
		list.add(PotionTypes.LONG_FIRE_RESISTANCE);
		list.add(PotionTypes.LONG_INVISIBILITY);
		list.add(PotionTypes.LONG_LEAPING);
		list.add(PotionTypes.LONG_NIGHT_VISION);
		list.add(PotionTypes.LONG_POISON);
		list.add(PotionTypes.LONG_REGENERATION);
		list.add(PotionTypes.LONG_SLOWNESS);
		list.add(PotionTypes.LONG_STRENGTH);
		list.add(PotionTypes.LONG_SWIFTNESS);
		list.add(PotionTypes.LONG_WATER_BREATHING);
		list.add(PotionTypes.LONG_WEAKNESS);
		list.add(PotionTypes.MUNDANE);
		list.add(PotionTypes.NIGHT_VISION);
		list.add(PotionTypes.POISON);
		list.add(PotionTypes.REGENERATION);
		list.add(PotionTypes.SLOWNESS);
		list.add(PotionTypes.STRENGTH);
		list.add(PotionTypes.STRONG_HARMING);
		list.add(PotionTypes.STRONG_HEALING);
		list.add(PotionTypes.STRONG_LEAPING);
		list.add(PotionTypes.STRONG_POISON);
		list.add(PotionTypes.STRONG_REGENERATION);
		list.add(PotionTypes.STRONG_STRENGTH);
		list.add(PotionTypes.STRONG_SWIFTNESS);
		list.add(PotionTypes.SWIFTNESS);
		list.add(PotionTypes.THICK);
		list.add(PotionTypes.WATER);
		list.add(PotionTypes.WATER_BREATHING);
		list.add(PotionTypes.WEAKNESS);

		PotionType potiontype = list.get(ThreadLocalRandom.current().nextInt(0, list.size()));

		EntityPotion entitypotion = new EntityPotion(this.world, this,
				PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potiontype));
		entitypotion.rotationPitch -= -20.0F;
		entitypotion.posY -= 1.5;
		entitypotion.shoot(d1, d2 + (double) (f * 0.2F), d3, 0.75F, 1.0F);
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
	protected float getJumpUpwardsMotion() {
		return 0.6F;
	}

	@Override
	protected void jump() {
		super.jump();
		for (EntityPlayer entityplayer : this.world.playerEntities)
			if (entityplayer.getDistanceSq(this) < 4096.0D)
				PacketHandler.sendPacketToPlayer(new StartJumpAnimationPacket(this.getEntityId()),
						(EntityPlayerMP) entityplayer);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.motionY = Math.max(this.motionY, -3);
		if (!this.world.isRemote) {
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

				if (this.shouldStartBreak()) {
					this.timeSinceFalling++;

					if (this.timeSinceFalling >= this.fallingTime) {
						this.timeSinceFalling = 0;
						this.originPos = null;
					} else {
						double value = easeOutBack((double) this.timeSinceFalling / (double) this.fallingTime)
								* (this.distance - 3);
						double height = Math.abs(value - this.distance);
						this.setPositionAndUpdate(this.posX, this.originPos.y - this.distance + height, this.posZ);
						this.motionY = 0;
						for (EntityPlayer entityplayer : this.world.playerEntities)
							if (entityplayer.getDistanceSq(this) < 4096.0D)
								PacketHandler.sendPacketToPlayer(new JetBoostPacket(this.posX, this.posY, this.posZ),
										(EntityPlayerMP) entityplayer);
					}
				} else {
					this.timeSinceFalling = 0;
					this.originPos = null;
				}
			}

		}
	}

	/**
	 * Cubic-Brezier Easing function to smooth out the landing. Thank you N247S for
	 * the help teaching me easing functions!
	 * 
	 * @param x Normalized value how far in the animation it is in.
	 * @return
	 */
	public double easeOutBack(double x) {
		double c1 = 1.70158;
		double c3 = c1 + 1;

		return (1 + c3 * Math.pow(x - 1, 3) + c1 * Math.pow(x - 1, 2));
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		// TODO Auto-generated method stub

	}

	/**
	 * Provides the distance the entity is from the ground
	 * 
	 * @param vec This is the entity's position
	 * @return the distance from the ground the entity is.
	 */
	private double distanceToGround(Vec3d vec) {
		int block;
		Vec3d pos = this.getPositionVector();
		for (block = 0; block <= distance; block++) {
			pos = pos.add(new Vec3d(0, -1, 0));
			if (!this.world.getBlockState(new BlockPos(pos)).equals(Blocks.AIR.getDefaultState()))
				break;
		}
		return vec.y - pos.y;
	}

	/**
	 * Checks to see if the entity has met conditions to start break animation.
	 * 
	 * @return true if it has no {@link #originPos},
	 *         {@link net.minecraft.entity.Entity#motionY} is not more than -0.3 and
	 *         is less than -1.0, and if {@link #distance} is greater than or equal
	 *         to {@link #distanceToGround(Vec3d)}
	 */
	public boolean shouldStartBreak() {
		if (this.originPos != null)
			return true;
		if (this.motionY > -0.3D)
			return false;
		double y = this.distanceToGround(this.getPositionVector());
		if (y <= this.distance && this.motionY < -1D) {
			this.originPos = this.getPositionVector();
			return true;
		}
		return false;
	}

	/**
	 * Disables fall damage.
	 */
	@Override
	public void fall(float distance, float damageMultiplier) {
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(2, new EntityAIAttackRanged(this, 0.3D, 60, 10.0F));
		this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 0.3D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(4, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true));
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
