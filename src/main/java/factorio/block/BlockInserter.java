
package factorio.block;


import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

import factorio.Factorio;
import factorio.tileentity.TileEntityInserter;


public class BlockInserter extends Block implements ITileEntityProvider
{
	public BlockInserter()
	{
		super( Material.circuits );
		setBlockName( "inserter" );
		setBlockTextureName( Factorio.MODID + ":" + getUnlocalizedName().substring( 5 ) );
		setCreativeTab( CreativeTabs.tabRedstone );
		setHardness( 3.0F );
		setResistance( 5.0F );
		setHarvestLevel( "pickaxe", 0 );
		setLightOpacity( 0 );
		this.setBlockBounds( -1, 0, -1, 2, 1, 2 );
		this.setBlockBoundsForItemRender();
		this.setBlockBounds( 0, 0, 0, 1, 1, 1 );
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
		return -1;
	}

	@Override
	public TileEntity createNewTileEntity( World p_149915_1_, int p_149915_2_ )
	{
		return new TileEntityInserter();
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
}