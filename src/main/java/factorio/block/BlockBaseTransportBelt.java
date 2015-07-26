
package factorio.block;


import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import factorio.Factorio;
import factorio.client.renderer.RenderTransportBelt;
import factorio.entity.EntityBeltItem;
import factorio.tileentity.TileEntityInserter;
import factorio.tileentity.TileEntityTransportBelt;


public class BlockBaseTransportBelt extends Block
{
	public float speed = 0.0625f;

	public BlockBaseTransportBelt( float moveSpeed )
	{
		super( Material.circuits );
		speed = moveSpeed;
		setBlockName( "transportBelt" );
		setBlockTextureName( Factorio.MODID + ":" + getUnlocalizedName().substring( 5 ) );
		setCreativeTab( CreativeTabs.tabRedstone );
		setHardness( 3.0F );
		setResistance( 5.0F );
		setHarvestLevel( "pickaxe", 0 );
		setBlockBounds( 0.0f, 0.0f, 0.0f, 1.0f, 0.0625f, 1.0f );
		setLightOpacity( 0 );
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return RenderTransportBelt.renderID;
	}

	@Override
	public boolean canPlaceBlockAt( World world, int x, int y, int z )
	{
		return world.getBlock( x, y - 1, z ).isSideSolid( world, x, y, z, ForgeDirection.UP );
	}

	@Override
	public boolean canBlockStay( World world, int x, int y, int z )
	{
		return world.getBlock( x, y - 1, z ).isSideSolid( world, x, y, z, ForgeDirection.UP );
	}

	@Override
	public void onEntityCollidedWithBlock( World world, int x, int y, int z, Entity entity )
	{
		if( entity == null )
			return;
		// Start movement of non item entities on transport belt
		List list = world.getEntitiesWithinAABBExcludingEntity( (Entity) null, AxisAlignedBB.getBoundingBox( x, y, z, x + 1, y + .065, z + 1 ) );
		if( Math.round( entity.posX - 0.5 ) == x && Math.round( entity.posZ - 0.5 ) == z && list.contains( entity ) )
		{
			if( !( entity instanceof EntityItem ) )
			{
				int metadata = world.getBlockMetadata( x, y, z );
				switch( metadata )
				{
					case 0:
						entity.addVelocity( 0.0f, 0.0f, speed );
						break;
					case 1:
						entity.addVelocity( -speed, 0.0f, 0.0f );
						break;
					case 2:
						entity.addVelocity( 0.0f, 0.0f, -speed );
						break;
					case 3:
						entity.addVelocity( speed, 0.0f, 0.0f );
						break;
					default:
						break;
				}
			}

			// End movement of non items

			// Start conversion of EntityItem 's to EntityBeltItem
			if( !world.isRemote && entity instanceof EntityItem && !( entity instanceof EntityBeltItem ) )
			{
				EntityItem entityItem = (EntityItem) entity;
				EntityBeltItem item = new EntityBeltItem( entity.worldObj, entity.posX, entity.posY, entity.posZ, entityItem.getEntityItem() );
				item.motionX = entity.motionX;
				item.motionY = entity.motionY;
				item.motionZ = entity.motionZ;
				world.spawnEntityInWorld( item );
				entity.setDead();
			}

			// End conversion of EntityItem

			// Start movement of EntityBeltItem 's
			if( entity instanceof EntityBeltItem )
			{
				EntityBeltItem item = (EntityBeltItem) entity;
				item.resetBeltTimer();
				{
					int metadata = world.getBlockMetadata( x, y, z );
					switch( metadata )
					{
						case 0:
							// item.addVelocity( 0.0f, 0.0f, speed );
							if( item.posX <= x + .5 )
							// && item.posX != x + .25 )
							{
								item.pathTo( x + .25, item.posY, z + 1.25, speed );
							}
							else if( item.posX > x + .5 )
							// && item.posX != x + .75 )
							{
								item.pathTo( x + .75, item.posY, z + 1.25, speed );
							}
							break;

						case 1:
							// item.addVelocity( -speed, 0.0f, 0.0f );
							if( item.posZ <= z + .5 )
							// && item.posZ != z + .25 )
							{
								item.pathTo( x - .25, item.posY, z + .25, speed );
							}
							else if( item.posZ > z + .5 )
							// item.posZ != z + .75 )
							{
								item.pathTo( x - .25, item.posY, z + .75, speed );
							}
							break;

						case 2:
							// item.addVelocity( 0.0f, 0.0f, -speed );
							if( item.posX <= x + .5 )
							// && item.posX != x + .25 )
							{
								item.pathTo( x + .25, item.posY, z - .25, speed );
							}
							else if( item.posX > x + .5 )
							// && item.posX != x + .75 )
							{
								item.pathTo( x + .75, item.posY, z - .25, speed );
							}
							break;

						case 3:
							// item.addVelocity( speed, 0.0f, 0.0f );
							if( item.posZ <= z + .5 )
							// && item.posZ != z + .25 )
							{
								item.pathTo( x + 1.25, item.posY, z + .25, speed );
							}
							else if( item.posZ > z + .5 )
							// &&item.posZ != z + .75 )
							{
								item.pathTo( x + 1.25, item.posY, z + .75, speed );
							}
							break;

						default:
							break;
					}
				}
			}
		}
	}

	@Override
	public void onBlockPlacedBy( World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack )
	{
		float yaw = entity.rotationYaw + 45;
		while( yaw >= 360 )
			yaw -= 360;
		while( yaw < 0 )
			yaw += 360;
		if( yaw < 90 )
			world.setBlockMetadataWithNotify( x, y, z, 0, 2 );
		else if( yaw < 180 )
			world.setBlockMetadataWithNotify( x, y, z, 1, 2 );
		else if( yaw < 270 )
			world.setBlockMetadataWithNotify( x, y, z, 2, 2 );
		else
			world.setBlockMetadataWithNotify( x, y, z, 3, 2 );
	}
}