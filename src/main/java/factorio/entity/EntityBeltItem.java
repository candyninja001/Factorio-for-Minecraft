package factorio.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import factorio.block.BlockBaseTransportBelt;
import net.minecraft.block.material.Material;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class EntityBeltItem extends EntityItem
{
	
	public EntityBeltItem( World world, double x, double y, double z, ItemStack stack )
	{
		super( world, x, y, z, stack );
		this.obayGravity = true;
		this.obayPhysics = true;
		this.beltTimer = 5;
	}
	
	public EntityBeltItem( World world )
	{
		super( world );
		this.beltTimer = 2;
	}
	
	protected double xGoal;
	protected double yGoal;
	protected double zGoal;
	protected float speed;
	protected boolean obayGravity;
	protected boolean obayPhysics;
	protected boolean pathing;
	private int health;
	public int beltTimer;
	
	public void toggleGravity( boolean state )
	{
		this.obayGravity = state;
	}
	
	public void togglePhysics( boolean state )
	{
		this.obayPhysics = state;
	}
	
	public void resetBeltTimer()
	{
		this.beltTimer = 5;
	}
	
	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		if ( this.age > 600 )
		{
			this.age = 100;
		}
		
		ItemStack stack = this.getDataWatcher().getWatchableObjectItemStack( 10 );
		if ( stack != null && stack.getItem() != null )
		{
			if ( stack.getItem().onEntityItemUpdate( this ) )
			{
				return;
			}
		}
		
		if ( this.getEntityItem() == null )
		{
			this.setDead();
		} else
		{
			super.onEntityUpdate();
			
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			if ( obayGravity )
				this.motionY -= 0.03999999910593033D;
			if ( obayPhysics )
				this.noClip = this.func_145771_j( this.posX, ( this.boundingBox.minY + this.boundingBox.maxY ) / 2.0D, this.posZ );
			else
				this.noClip = true;
			
			// do no modify unless you really know what you're doing
			{
				
				double changeInX = this.xGoal-this.posX;
				double changeInY = this.yGoal-this.posY;
				double changeInZ = this.zGoal-this.posZ;
				
				double r = Math.sqrt( (changeInX * changeInX) + (changeInY * changeInY) + (changeInZ * changeInZ) );
				double ratio;
				if ( r <= speed )
					ratio = 1;
				else
					ratio = this.speed / r;
				
				this.motionX = ratio * changeInX;
				this.motionY = ratio * changeInY;
				this.motionZ = ratio * changeInZ;
				
			}
			
			this.moveEntity( this.motionX, this.motionY, this.motionZ );
			
			if(!worldObj.isRemote)
				((WorldServer)worldObj).getEntityTracker().updateTrackedEntities();
			
			if (this.posX == this.xGoal && this.posY == this.yGoal && this.posZ == this.zGoal)
				this.pathing = false;
			
			//boolean flag = ( int ) this.prevPosX != ( int ) this.posX || ( int ) this.prevPosY != ( int ) this.posY || ( int ) this.prevPosZ != ( int ) this.posZ;
			
			
			
			//float f = 0.98F;
			
			//if ( this.onGround )
			//{
			//	f = this.worldObj.getBlock( MathHelper.floor_double( this.posX ), MathHelper.floor_double( this.boundingBox.minY ) - 1, MathHelper.floor_double( this.posZ ) ).slipperiness * 0.98F;
			//}
			//
			//this.motionX *= ( double ) f;
			//this.motionY *= 0.9800000190734863D;
			//this.motionZ *= ( double ) f;
			
			if ( this.onGround )
			{
				this.motionY *= -0.5D;
			}
			
			// ++this.age;
			--this.beltTimer;
			
			ItemStack item = getDataWatcher().getWatchableObjectItemStack( 10 );
			
			if ( !this.worldObj.isRemote && this.beltTimer < 1 )
			{
				EntityItem entityItem = new EntityItem( worldObj, posX, posY, posZ, item );
				entityItem.motionX = this.motionX;
				entityItem.motionY = this.motionY;
				entityItem.motionZ = this.motionZ;
				worldObj.spawnEntityInWorld( entityItem );
				this.setDead();
			}
			
			if ( item != null && item.stackSize <= 0 )
			{
				this.setDead();
			}
		}
	}
	
	@Override
	public boolean canBeCollidedWith()
    {
        return true;
    }
	
	@Override
	public AxisAlignedBB getCollisionBox(Entity p_70114_1_)
    {
        return this.boundingBox;
    }
	
	@Override
	public boolean combineItems( EntityItem p_70289_1_ )
	{
		return false;
	}
	
	@Override
	public boolean handleWaterMovement()
	{
		// Can't even handle me right now.
		return false;
	}
	
	@Override
	protected void dealFireDamage( int p_70081_1_ )
	{
		// Deal with it.
	}
	
	@Override
	public boolean attackEntityFrom( DamageSource p_70097_1_, float p_70097_2_ )
	{
		// Nope.
		return false;
	}
	
	@Override
	public void onCollideWithPlayer( EntityPlayer p_70100_1_ )
	{
		//
	}
	
	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT( NBTTagCompound tag )
	{
		tag.setDouble( "XGoal", this.xGoal );
		tag.setDouble( "YGoal", this.yGoal );
		tag.setDouble( "ZGoal", this.zGoal );
		tag.setFloat( "Speed", this.speed );
		tag.setBoolean( "ObayGravity", this.obayGravity );
		tag.setBoolean( "ObayPhysics", this.obayPhysics );
		tag.setBoolean( "IsPathing", this.pathing );
		tag.setInteger( "BeltTimer", beltTimer );
		
		tag.setShort( "Health", ( short ) ( ( byte ) this.health ) );
		tag.setShort( "Age", ( short ) this.age );
		tag.setInteger( "Lifespan", lifespan );
		
		if ( this.getEntityItem() != null )
		{
			tag.setTag( "Item", this.getEntityItem().writeToNBT( new NBTTagCompound() ) );
		}
	}
	
	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT( NBTTagCompound tag )
	{
		this.xGoal = tag.getDouble( "XGoal" );
		this.yGoal = tag.getDouble( "YGoal" );
		this.zGoal = tag.getDouble( "ZGoal" );
		this.speed = tag.getFloat( "Speed" );
		this.obayGravity = tag.getBoolean( "ObayGravity" );
		this.obayPhysics = tag.getBoolean( "ObayPhysics" );
		this.pathing = tag.getBoolean( "IsPathing" );
		this.beltTimer = tag.getInteger( "BeltTimer" );
		
		this.health = tag.getShort( "Health" ) & 255;
		this.age = tag.getShort( "Age" );
		
		NBTTagCompound nbttagcompound1 = tag.getCompoundTag( "Item" );
		this.setEntityItemStack( ItemStack.loadItemStackFromNBT( nbttagcompound1 ) );
		
		ItemStack item = getDataWatcher().getWatchableObjectItemStack( 10 );
		
		if ( item == null || item.stackSize <= 0 )
		{
			this.setDead();
		}
		
		if ( tag.hasKey( "Lifespan" ) )
		{
			lifespan = tag.getInteger( "Lifespan" );
		}
	}
	
	public void pathTo( double x, double y, double z, float speed )
	{
		if ( !pathing ){
			this.xGoal = x;
			this.yGoal = y;
			this.zGoal = z;
			this.speed = speed;
			this.pathing = true;
		}
			
	}
}
