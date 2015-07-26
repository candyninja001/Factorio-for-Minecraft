
package factorio.tileentity;


import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


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
		if( progress >= progressToComplete )
		{
			prevDestX = destX;
			prevDestY = destY;
			prevDestZ = destZ;
			if( prevDestX == 0 )
			{
				destX = 1;
				destY = 1;
				destZ = 2;
			}
			else if( prevDestX == 1 )
			{
				destX = 0;
				destY = 0;
				destZ = 0;
			}
			progress = 0;
		}
		this.markDirty();
	}
}