
package factorio.tileentity;


import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;


public class TileEntityInserter extends TileEntity
{
	public double destX;
	public double destZ;
	public double destY;
	public double prevDestX;
	public double prevDestZ;
	public double prevDestY;

	public static int progressToComplete = 12;
	public int progress = 0;

	@Override
	public void writeToNBT( NBTTagCompound par1 )
	{
		super.writeToNBT( par1 );
		par1.setInteger( "progress", progress );
		par1.setDouble( "destX", destX );
		par1.setDouble( "destY", destY );
		par1.setDouble( "destZ", destZ );
		par1.setDouble( "prevDestX", prevDestX );
		par1.setDouble( "prevDestY", prevDestY );
		par1.setDouble( "prevDestZ", prevDestZ );
	}

	@Override
	public void readFromNBT( NBTTagCompound par1 )
	{
		super.readFromNBT( par1 );
		progress = par1.getInteger( "progress" );
		destX = par1.getDouble( "destX" );
		destY = par1.getDouble( "destY" );
		destZ = par1.getDouble( "destZ" );
		prevDestX = par1.getDouble( "prevDestX" );
		prevDestY = par1.getDouble( "prevDestY" );
		prevDestZ = par1.getDouble( "prevDestZ" );
	}

	@Override
	public void updateEntity()
	{
		if( progress < progressToComplete )
			++progress;
		//if( !this.worldObj.isRemote && progress >= progressToComplete )
		if (false)//if( progress >= progressToComplete )
		{
			boolean flag = false;
			if( destZ == 1.5 )
			{
				switch( blockMetadata )
				{
					case 0:
						flag = pullOrPushItemTo( xCoord, yCoord, zCoord + 1 );
						break;
					case 1:
						flag = pullOrPushItemTo( xCoord - 1, yCoord, zCoord );
						break;
					case 2:
						flag = pullOrPushItemTo( xCoord, yCoord, zCoord - 1 );
						break;
					case 3:
						flag = pullOrPushItemTo( xCoord + 1, yCoord, zCoord );
						break;
					default:
						break;
				}
			}
			else if( destZ == -0.5 )
			{
				switch( blockMetadata )
				{
					case 0:
						flag = pullOrPushItemTo( xCoord, yCoord, zCoord - 1 );
						break;
					case 1:
						flag = pullOrPushItemTo( xCoord + 1, yCoord, zCoord );
						break;
					case 2:
						flag = pullOrPushItemTo( xCoord, yCoord, zCoord + 1 );
						break;
					case 3:
						flag = pullOrPushItemTo( xCoord - 1, yCoord, zCoord );
						break;
					default:
						break;
				}
			}
			if( flag )
			{
				switchDest();
				this.worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
				progress = 0;
				this.markDirty();
			}
		}
	}

	private boolean pullOrPushItemTo( int xCoord, int yCoord, int zCoord , ForgeDirection direction)
	{
		TileEntity tileEntity = worldObj.getTileEntity( xCoord, yCoord, zCoord );
		if( tileEntity == null )
		{
			if( worldObj.isAirBlock( xCoord, yCoord, zCoord ) )
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if( tileEntity instanceof IInventory )
		{
			if (tileEntity instanceof ISidedInventory){
				IInventory iInventory = (IInventory) tileEntity;
			}else{
				ISidedInventory iSidedInventory = (ISidedInventory) tileEntity;
				int[] slots = iSidedInventory.getAccessibleSlotsFromSide( ForgeDirection. );
			}
		}
		return false;
	}

	public void switchDest()
	{
		prevDestX = destX;
		prevDestY = destY;
		prevDestZ = destZ;
		if( prevDestZ == -.5 )
		{
			destX = .5;
			destY = .5;
			destZ = 1.5;
		}
		else
		{
			destX = .5;
			destY = .5;
			destZ = -.5;
		}
	}
}