
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
		if( !( tileEntity instanceof TileEntityInserter ) )
			return;
		TileEntityInserter inserter = (TileEntityInserter) tileEntity;

		double renderX = ( 16 * inserter.destX ) - 8;
		double renderY = ( 16 * inserter.destY ) - 4;
		double renderZ = ( 16 * inserter.destZ ) - 8;
		double renderPrevX = ( 16 * inserter.prevDestX ) - 8;
		double renderPrevY = ( 16 * inserter.prevDestY ) - 4;
		double renderPrevZ = ( 16 * inserter.prevDestZ ) - 8;

		double renderRadius = Math.sqrt( ( renderX * renderX ) + ( renderY * renderY ) + ( renderZ * renderZ ) );
		double renderPrevRadius = Math.sqrt( ( renderPrevX * renderPrevX ) + ( renderPrevY * renderPrevY ) + ( renderPrevZ * renderPrevZ ) );
		
		double renderAngle = Math.toDegrees( Math.tan( renderX/renderZ ) );
		double renderPrevAngle = Math.toDegrees( Math.tan( renderX/renderZ ) );

		if (renderZ <0)
			renderAngle += 180;
		if (renderPrevZ <0)
			renderPrevAngle += 180;
		if (renderAngle <0)
			renderAngle += 360;
		if (renderPrevAngle <0)
			renderPrevAngle += 360;
		
		double renderRadiusDif = renderRadius - renderPrevRadius;
		double renderHeightDif = renderY - renderPrevY;
		double renderAngleDif = renderAngle - renderPrevAngle;
		if ( Math.abs( renderAngleDif ) > 180 )
			renderAngleDif = 360 - renderAngleDif;

		double renderTrueRadius = ( inserter.progress * renderRadiusDif / inserter.progressToComplete ) + renderPrevRadius;

		double renderTrueHeight = ( inserter.progress * renderHeightDif / inserter.progressToComplete ) + renderPrevY;
		
		double renderTrueAngle = ( inserter.progress * renderAngleDif / inserter.progressToComplete ) + renderPrevAngle;
		
		GL11.glPushMatrix();
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslated( x, y, z );
		Minecraft.getMinecraft().renderEngine.bindTexture( new ResourceLocation( "factorio:textures/model/inserter.png" ) );

		//GL11.glTranslatef(0.5F, 1.5F, 0.5F);
		//GL11.glScalef(1F, -1F, -1F);

		model.render();

		Tessellator tessellator = Tessellator.instance;

		float angle = (float)renderTrueAngle;
		double radius = renderTrueRadius;
		double difHeight = renderTrueHeight;

		model.renderArm( angle, radius, difHeight );

		item.setEntityItemStack( new ItemStack( Items.diamond, 1 ) );
		item.hoverStart = 0;

		GL11.glTranslated( ( ( Math.sqrt( ( radius * radius ) + ( difHeight * difHeight ) ) * Math.sin( Math.toRadians( 180 + angle ) ) ) ) / 16 + .53125, ( difHeight + 2 ) / 16, -( Math.sqrt( ( radius * radius ) + ( difHeight * difHeight ) ) * Math.cos( Math.toRadians( 180 + angle ) ) ) / 16 + .4375 );

		inserterItem.doRender( item, 0, 0, 0, 0f, 0f );

		//GL11.glScalef(1F, -1F, -1F);
		//GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

}
