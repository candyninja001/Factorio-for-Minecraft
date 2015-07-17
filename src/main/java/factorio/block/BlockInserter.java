package factorio.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
		setLightOpacity(0);
	}
	
	public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
	public TileEntity createNewTileEntity( World p_149915_1_, int p_149915_2_ )
	{
		return new TileEntityInserter();
	}
}