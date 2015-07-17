package factorio.tileentity;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class TileEntityTransportBelt extends TileEntity
{
	
	private int[] progress = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	
	private ItemStack[] storage = new ItemStack[10];
	
	@Override
	public void writeToNBT( NBTTagCompound par1 )
	{
		super.writeToNBT( par1 );
		for ( int i = 0; i < 10; ++i )
		{
			par1.setInteger( "Progress" + i, progress[i] );
		}
		
		NBTTagList nbttaglist = new NBTTagList();
		
		for ( int i = 0; i < 10; ++i )
		{
			if ( storage[i] != null )
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte( "Slot", ( byte ) i );
				storage[i].writeToNBT( nbttagcompound1 );
				nbttaglist.appendTag( nbttagcompound1 );
			}
		}
		
		par1.setTag( "Items", nbttaglist );
	}
	
	@Override
	public void readFromNBT( NBTTagCompound par1 )
	{
		super.readFromNBT( par1 );
		for ( int i = 0; i < 10; ++i )
		{
			progress[i] = par1.getInteger( "Progress" + i );
			
		}
		
		NBTTagList nbttaglist = par1.getTagList( "Items", 10 );
		for ( int i = 0; i < nbttaglist.tagCount(); ++i )
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt( i );
			byte b = nbttagcompound1.getByte( "Slot" );
			
			if ( b >= 0 && b < 10 )
			{
				storage[b] = ItemStack.loadItemStackFromNBT( nbttagcompound1 );
			}
		}
	}
	
	@Override
	public void updateEntity()
	{
		
		updateItems();
	}
	
	private void updateItems()
	{
		for ( int i = 0; i < 8; ++i )
		{
			if ( progress[i] > 0 && progress[i] < 3 )
				progress[i]++;
			if ( progress[i] >= 3 )
			{
				if ( i != 0 && i != 4 && i < 8 )
				{
					progress[i] = 0;
					if ( storage[i] != null )
					{
						if ( storage[i - 1] == null )
						{
							storage[i - 1] = storage[i];
						} else
						{
							EntityItem entityItem = new EntityItem( worldObj, xCoord, yCoord, zCoord, storage[i] );
							entityItem.motionX = 0.0;
							entityItem.motionY = 0.0;
							entityItem.motionZ = 0.0;
							worldObj.spawnEntityInWorld( entityItem );
						}
						storage[i] = null;
					}
				} else
				// if ( i == 0 )
				{
					switch ( blockMetadata )
					{
						case 0:
							// if ()
							break;
						case 1:
							
							break;
						case 2:
							
							break;
						case 3:
							
							break;
						default:
							EntityItem entityItem = new EntityItem( worldObj, xCoord, yCoord, zCoord, storage[i] );
							entityItem.motionX = 0.0;
							entityItem.motionY = 0.0;
							entityItem.motionZ = 0.0;
							worldObj.spawnEntityInWorld( entityItem );
							storage[i] = null;
							break;
					}
					// } else
					// {
					
				}
			}
		}
	}
}