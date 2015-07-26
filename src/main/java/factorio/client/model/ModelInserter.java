
package factorio.client.model;


import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;


public class ModelInserter extends ModelBase
{

	ModelRenderer base;
	ModelRenderer footForward;
	ModelRenderer footLeft;
	ModelRenderer footRight;

	ModelRenderer arm1;
	ModelRenderer joint;
	ModelRenderer arm2;

	ModelRenderer handBase;
	ModelRenderer handRight;
	ModelRenderer handLeft;

	public ModelInserter()
	{
		textureWidth = 64;
		textureHeight = 32;

		base = new ModelRenderer( this, 0, 0 );
		base.setRotationPoint( 8f, 3f, 8f );
		base.addBox( -4f, -2f, -4f, 8, 3, 8 );
		base.setTextureSize( textureWidth, textureHeight );
		base.rotateAngleY = (float) Math.toRadians( 45 );

		footForward = new ModelRenderer( this, 24, 0 );
		footForward.setRotationPoint( 8f, 3f, 8f );
		footForward.addBox( -1f, -3f, 4f, 2, 5, 3 );
		footForward.setTextureSize( textureWidth, textureHeight );

		footLeft = new ModelRenderer( this, 24, 0 );
		footLeft.setRotationPoint( 8f, 3f, 8f );
		footLeft.addBox( -1f, -3f, 4f, 2, 5, 3 );
		footLeft.setTextureSize( textureWidth, textureHeight );
		footLeft.rotateAngleY = (float) Math.toRadians( 135 );

		footRight = new ModelRenderer( this, 24, 0 );
		footRight.setRotationPoint( 8f, 3f, 8f );
		footRight.addBox( -1f, -3f, 4f, 2, 5, 3 );
		footRight.setTextureSize( textureWidth, textureHeight );
		footRight.rotateAngleY = (float) Math.toRadians( 225 );

		arm1 = new ModelRenderer( this, 34, 0 );
		arm1.setRotationPoint( 8f, 4f, 8f );
		arm1.addBox( -1.5f, -1.5f, -1f, 3, 12, 2 );
		arm1.setTextureSize( textureWidth, textureHeight );

		arm2 = new ModelRenderer( this, 44, 0 );
		arm2.addBox( -1.5f, 1f, -1f, 3, 8, 2 );
		arm2.setTextureSize( textureWidth, textureHeight );

		joint = new ModelRenderer( this, 0, 11 );
		joint.addBox( -2f, -1.5f, -1.5f, 4, 3, 3 );
		joint.setTextureSize( textureWidth, textureHeight );
		joint.rotateAngleX = (float) Math.toRadians( 45 );

		handBase = new ModelRenderer( this, 11, 11 );
		handBase.addBox( -2.5f, 9f, -1f, 5, 1, 2 );

		handLeft = new ModelRenderer( this, 25, 11 );
		handLeft.addBox( -3.5f, 9f, -1f, 1, 4, 2 );

		handRight = new ModelRenderer( this, 25, 11 );
		handRight.addBox( 2.5f, 9f, -1f, 1, 4, 2 );

	}

	public void render()
	{
		base.render( 1F / 16f );
		footForward.render( 1F / 16f );
		footLeft.render( 1F / 16f );
		footRight.render( 1F / 16f );
	}

	public void renderArm( float angle, double radius, double height )
	{
		arm1.rotateAngleX = (float) ( Math.acos( radius / 28 ) - Math.acos( height / radius ) );
		arm1.rotateAngleY = (float) Math.toRadians( 180 - angle );
		arm1.render( 1 / 16f );

		double f = 12;

		float edge = (float) ( f * ( Math.sin( Math.acos( radius / 28 ) - Math.acos( height / radius ) ) ) );
		float arm2X = (float) -( edge * ( Math.sin( Math.toRadians( 180 + angle ) ) ) );
		float arm2Y = (float) ( 11.5 * ( Math.cos( Math.acos( radius / 28 ) - Math.acos( height / radius ) ) ) );
		float arm2Z = (float) ( edge * ( Math.cos( Math.toRadians( 180 + angle ) ) ) );

		joint.setRotationPoint( arm2X + 8f, arm2Y + 4f, arm2Z + 8f );
		joint.rotateAngleY = (float) Math.toRadians( 180 - angle );
		joint.render( 1 / 16f );

		arm2.setRotationPoint( arm2X + 8f, arm2Y + 4f, arm2Z + 8f );
		arm2.rotateAngleX = (float) -( Math.acos( radius / 28 ) + Math.acos( height / radius ) );
		arm2.rotateAngleY = (float) Math.toRadians( 180 - angle );
		arm2.render( 1 / 16f );

		handBase.setRotationPoint( arm2X + 8f, arm2Y + 4f, arm2Z + 8f );
		handBase.rotateAngleX = (float) -( Math.acos( radius / 28 ) + Math.acos( height / radius ) );
		handBase.rotateAngleY = (float) Math.toRadians( 180 - angle );
		handBase.render( 1 / 16f );

		handLeft.setRotationPoint( arm2X + 8f, arm2Y + 4f, arm2Z + 8f );
		handLeft.rotateAngleX = (float) -( Math.acos( radius / 28 ) + Math.acos( height / radius ) );
		handLeft.rotateAngleY = (float) Math.toRadians( 180 - angle );
		handLeft.render( 1 / 16f );

		handRight.setRotationPoint( arm2X + 8f, arm2Y + 4f, arm2Z + 8f );
		handRight.rotateAngleX = (float) -( Math.acos( radius / 28 ) + Math.acos( height / radius ) );
		handRight.rotateAngleY = (float) Math.toRadians( 180 - angle );
		handRight.render( 1 / 16f );
	}

}