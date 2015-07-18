
package factorio.client.renderer;


import java.util.HashMap;

import javax.swing.Icon;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import factorio.Factorio;
import factorio.block.BlockBaseTransportBelt;


@SideOnly( Side.CLIENT )
public final class RenderTransportBelt implements ISimpleBlockRenderingHandler
{

	public static int renderID;

	public RenderTransportBelt()
	{
		renderID = RenderingRegistry.getNextAvailableRenderId();
	}

	@Override
	public void renderInventoryBlock( Block block, int metadata, int modelID, RenderBlocks renderer )
	{
		Tessellator tessellator = Tessellator.instance;

		// if you don't perform this translation, the item won't sit in the
		// player's hand properly in 3rd person view
		GL11.glTranslatef( -0.5F, -0.5F, -0.5F );

		// for "inventory" blocks (actually for items which are equipped,
		// dropped, or in inventory), should render in [0,0,0] to [1,1,1]
		tessellator.startDrawingQuads();
		renderTransportBelt( tessellator, block, 0.0, 0.0, 0.0, 0 );
		tessellator.draw();

		// don't forget to undo the translation you made at the start
		GL11.glTranslatef( 0.5F, 0.5F, 0.5F );

	}

	@Override
	public boolean renderWorldBlock( IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer )
	{
		Tessellator tessellator = Tessellator.instance;

		// world blocks should render in [x,y,z] to [x+1, y+1, z+1]
		// tessellator.startDrawingQuads() has already been called by the caller

		int lightValue = block.getMixedBrightnessForBlock( world, x, y, z );
		tessellator.setBrightness( lightValue );
		tessellator.setColorOpaque_F( 1.0F, 1.0F, 1.0F );

		renderTransportBelt( tessellator, block, (double) x, (double) y, (double) z, world.getBlockMetadata( x, y, z ) );
		// tessellator.draw() will be called by the caller after return
		return true;
	}

	// render the pyramid at the given [x,y,z]
	// call tessellator.startDrawingQuads() before and tessellator.draw()
	// afterwards

	private void renderTransportBelt( Tessellator tessellator, Block block, double x, double y, double z, int metadata )
	{

		IIcon icon1 = Factorio.TransportBelt.getIcon( 0, 0 );
		double minU1 = (double) icon1.getMinU();
		double minV1 = (double) icon1.getMinV();
		double maxU1 = (double) icon1.getMaxU();
		double maxV1 = (double) icon1.getMaxV();

		tessellator.setNormal( 0.0F, 1.0F, 0.0F );
		if( metadata == 0 )
		{
			tessellator.addVertexWithUV( x + 0.0, y + 0.0625, z + 0.0, maxU1, maxV1 );
			tessellator.addVertexWithUV( x + 0.0, y + 0.0625, z + 1.0, maxU1, minV1 );
			tessellator.addVertexWithUV( x + 1.0, y + 0.0625, z + 1.0, minU1, minV1 );
			tessellator.addVertexWithUV( x + 1.0, y + 0.0625, z + 0.0, minU1, maxV1 );
		}
		if( metadata == 1 )
		{
			tessellator.addVertexWithUV( x + 1.0, y + 0.0625, z + 0.0, maxU1, maxV1 );
			tessellator.addVertexWithUV( x + 0.0, y + 0.0625, z + 0.0, maxU1, minV1 );
			tessellator.addVertexWithUV( x + 0.0, y + 0.0625, z + 1.0, minU1, minV1 );
			tessellator.addVertexWithUV( x + 1.0, y + 0.0625, z + 1.0, minU1, maxV1 );
		}
		if( metadata == 2 )
		{
			tessellator.addVertexWithUV( x + 1.0, y + 0.0625, z + 1.0, maxU1, maxV1 );
			tessellator.addVertexWithUV( x + 1.0, y + 0.0625, z + 0.0, maxU1, minV1 );
			tessellator.addVertexWithUV( x + 0.0, y + 0.0625, z + 0.0, minU1, minV1 );
			tessellator.addVertexWithUV( x + 0.0, y + 0.0625, z + 1.0, minU1, maxV1 );
		}
		if( metadata == 3 )
		{
			tessellator.addVertexWithUV( x + 0.0, y + 0.0625, z + 1.0, maxU1, maxV1 );
			tessellator.addVertexWithUV( x + 1.0, y + 0.0625, z + 1.0, maxU1, minV1 );
			tessellator.addVertexWithUV( x + 1.0, y + 0.0625, z + 0.0, minU1, minV1 );
			tessellator.addVertexWithUV( x + 0.0, y + 0.0625, z + 0.0, minU1, maxV1 );
		}
		//RenderBlocks.getInstance().renderFaceYPos( block, x, y, z, icon1 );

	}

	@Override
	public boolean shouldRender3DInInventory( int modelId )
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return renderID;
	}

}