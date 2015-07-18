
package factorio.tileentity;


import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


public class TileEntityInserter extends TileEntity
{

	int progress = 0;

	@Override
	public void writeToNBT( NBTTagCompound par1 )
	{
		super.writeToNBT( par1 );
		par1.setInteger( "progress", progress );
	}

	@Override
	public void readFromNBT( NBTTagCompound par1 )
	{
		super.readFromNBT( par1 );
		progress = par1.getInteger( "progress" );
	}

	@Override
	public void updateEntity()
	{
		if( progress < 20 )
			++progress;
		if( !this.worldObj.isRemote && progress >= 20 )
		{
			TileEntity tile = worldObj.getTileEntity( xCoord + 1, yCoord, zCoord );
			if( tile != null && tile instanceof IInventory )
			{
				IInventory tileInv = (IInventory) tile;
				int i = tileInv.getSizeInventory();
				int v = 0;
				boolean flag = true;
				ItemStack stack = null;
				while( flag )
				{
					stack = tileInv.decrStackSize( v, 1 );
					if( stack == null )
					{
						++v;
						if( v == i )
						{
							flag = false;
						}
					}
					else
					{
						flag = false;
					}
				}
				if( stack != null )
				{
					EntityItem entityitem = new EntityItem( worldObj, ( (double) xCoord ) - .75, ( (double) yCoord ) + .5, ( (double) zCoord ) + .5, stack );
					entityitem.motionX = 0;
					entityitem.motionY = 0;
					entityitem.motionZ = 0;
					worldObj.spawnEntityInWorld( entityitem );
					progress = 0;
					//
				}
			}

		}
		this.markDirty();
	}
}