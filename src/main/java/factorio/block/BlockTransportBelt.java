
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
import factorio.tileentity.TileEntityInserter;
import factorio.tileentity.TileEntityTransportBelt;


public class BlockTransportBelt extends Block implements ITileEntityProvider
{
	public float speed = 0.05f;

	public BlockTransportBelt( float moveSpeed )
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

	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity( World p_149915_1_, int p_149915_2_ )
	{
		return new TileEntityTransportBelt();
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

	public void onEntityCollidedWithBlock( World world, int x, int y, int z, Entity entity )
	{
		if( entity instanceof EntityItem )
			return;
		List list = world.getEntitiesWithinAABBExcludingEntity( (Entity) null, AxisAlignedBB.getBoundingBox( x, y, z, x + 1, y + .065, z + 1 ) );
		if( Math.round( entity.posX - 0.5 ) == x && Math.round( entity.posZ - 0.5 ) == z && list.contains( entity ) )
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
	}

	public void onBlockPlacedBy( World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack )
	{
		float yaw = entity.rotationYaw + 45;
		while( yaw >= 360 )
			yaw -= 360;
		while( yaw < 0 )
			yaw += 360;

		if( yaw < 90 )
			world.setBlockMetadataWithNotify( x, y, z, 2, 2 );
		else if( yaw < 180 )
			world.setBlockMetadataWithNotify( x, y, z, 3, 2 );
		else if( yaw < 270 )
			world.setBlockMetadataWithNotify( x, y, z, 0, 2 );
		else
			world.setBlockMetadataWithNotify( x, y, z, 1, 2 );

	}
}