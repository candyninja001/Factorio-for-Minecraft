
package factorio.client;


import cpw.mods.fml.client.registry.ClientRegistry;

import factorio.client.renderer.tileentity.TileEntitySpecialRendererInserter;
import factorio.common.CommonProxy;
import factorio.tileentity.TileEntityInserter;


public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInserter.class, new TileEntitySpecialRendererInserter());
	}
}
