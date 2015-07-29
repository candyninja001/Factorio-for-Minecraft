
package factorio.client.renderer.tileentity;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import factorio.client.model.ModelInserter;
import factorio.tileentity.TileEntityInserter;


public class TileEntitySpecialRendererInserter extends TileEntitySpecialRenderer
{
	private ModelInserter model = new ModelInserter();

	private final RenderItem inserterItem;

	public static EntityItem item = new EntityItem( null );

	public TileEntitySpecialRendererInserter()
	{
		inserterItem = new RenderItem(){
			@Override
			public boolean shouldBob()
			{
				return false;
			}

			@Override
			public boolean shouldSpreadItems()
			{
				return false;
			}
		};
		inserterItem.setRenderManager( RenderManager.instance );
	}

	@Override
	public void renderTileEntityAt( TileEntity tileEntity, double x, double y, double z, float ticks )
	{
		// Start calculating values to position the inserter arm correcctly
		
		if( !( tileEntity instanceof TileEntityInserter ) )
			return;
		TileEntityInserter inserter = (TileEntityInserter) tileEntity;

		double renderX = ( 16 * inserter.destX ) - 8;
		double renderY = ( 16 * inserter.destY ) - 4;
		double renderZ = ( 16 * inserter.destZ ) - 8;
		double renderPrevX = ( 16 * inserter.prevDestX ) - 8;
		double renderPrevY = ( 16 * inserter.prevDestY ) - 4;
		double renderPrevZ = ( 16 * inserter.prevDestZ ) - 8;

		double renderAngle = Math.toDegrees( Math.tan( renderX / renderZ ) );
		double renderPrevAngle = Math.toDegrees( Math.tan( renderX / renderZ ) );

		if( renderZ < 0 )
			renderAngle += 180;
		if( renderPrevZ < 0 )
			renderPrevAngle += 180;
		if( renderAngle < 0 )
			renderAngle += 360;
		if( renderPrevAngle < 0 )
			renderPrevAngle += 360;

		double renderAngleDif = renderAngle - renderPrevAngle;
		
		if( Math.abs( renderAngleDif ) > 180 )
			renderAngleDif = 360 - renderAngleDif;

		double renderTrueRadius = ( inserter.progress * ( Math.sqrt( ( renderX * renderX ) + ( renderY * renderY ) + ( renderZ * renderZ ) ) - Math.sqrt( ( renderPrevX * renderPrevX ) + ( renderPrevY * renderPrevY ) + ( renderPrevZ * renderPrevZ ) ) ) / inserter.progressToComplete ) + Math.sqrt( ( renderPrevX * renderPrevX ) + ( renderPrevY * renderPrevY ) + ( renderPrevZ * renderPrevZ ) );
		double renderTrueHeight = ( inserter.progress * ( renderY - renderPrevY ) / inserter.progressToComplete ) + renderPrevY;
		double renderTrueAngle = ( inserter.progress * renderAngleDif / inserter.progressToComplete ) + renderPrevAngle;

		// Stop calculating values
		
		GL11.glPushMatrix();
		GL11.glTranslated( x+.5, y, z+.5 );
		GL11.glRotatef( -90 * inserter.blockMetadata, 0f, 1f, 0f );
		GL11.glTranslated( -.5, 0, -.5 );
		Minecraft.getMinecraft().renderEngine.bindTexture( new ResourceLocation( "factorio:textures/model/inserter.png" ) );

		model.render();
		model.renderArm( (float) renderTrueAngle, renderTrueRadius, renderTrueHeight );

		item.setEntityItemStack( new ItemStack( Items.diamond, 1 ) );
		item.hoverStart = 0;

		GL11.glTranslated( ( ( Math.sqrt( ( renderTrueRadius * renderTrueRadius ) + ( renderTrueHeight * renderTrueHeight ) ) * Math.sin( Math.toRadians( 180 + renderTrueAngle ) ) ) ) / 16 + .53125, ( renderTrueHeight + 2 ) / 16, -( Math.sqrt( ( renderTrueRadius * renderTrueRadius ) + ( renderTrueHeight * renderTrueHeight ) ) * Math.cos( Math.toRadians( 180 + renderTrueAngle ) ) ) / 16 + .4375 );

		inserterItem.doRender( item, 0, 0, 0, 0f, 0f );
		
		GL11.glPopMatrix();
	}

}
