package factorio;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import factorio.block.BlockBaseTransportBelt;
import factorio.block.BlockInserter;
import factorio.client.renderer.RenderTransportBelt;
import factorio.common.CommonProxy;
import factorio.entity.EntityBeltItem;

@Mod( modid = Factorio.MODID, version = Factorio.VERSION )
// @NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class Factorio
{
	public static final String MODID = "factorio";
	public static final String VERSION = "0.0.1";
	
	public static Block Inserter;
	public static Block TransportBelt;
	
	@SidedProxy( clientSide = "candy.factorio.client.ClientProxy", serverSide = "candy.factorio.common.CommonProxy" )
	public static CommonProxy proxy;
	
	@EventHandler
	public void init( FMLInitializationEvent event )
	{
		Inserter = new BlockInserter();
		GameRegistry.registerBlock( Inserter, MODID + "_" + Inserter.getUnlocalizedName().substring( 5 ) );
		GameRegistry.registerTileEntity( factorio.tileentity.TileEntityInserter.class, "inserter" );
		
		TransportBelt = new BlockBaseTransportBelt( 0.04f );
		GameRegistry.registerBlock( TransportBelt, MODID + "_" + TransportBelt.getUnlocalizedName().substring( 5 ) );
		//GameRegistry.registerTileEntity( candy.factorio.tileentity.TileEntityTransportBelt.class, "transportbelt" );
		RenderingRegistry.registerBlockHandler( new RenderTransportBelt() );
		
		EntityRegistry.registerGlobalEntityID( EntityBeltItem.class, "entityBeltItem", EntityRegistry.findGlobalUniqueEntityId() );
		//EntityRegistry.registerModEntity( EntityBeltItem.class, "entitybeltitem", EntityRegistry.findGlobalUniqueEntityId(), this, 80, 3, true );
		
		proxy.registerRenderers();
	}
}