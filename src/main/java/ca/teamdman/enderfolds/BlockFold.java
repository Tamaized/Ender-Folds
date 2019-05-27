package ca.teamdman.enderfolds;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static ca.teamdman.enderfolds.EnderFolds.MOD_ID;

public class BlockFold extends Block {
	enum Type {
		SOURCE,
		DESTINATION
	}

	public final Type type;

	public BlockFold(Type type) {
		super(Material.PISTON, MapColor.BLACK);
		this.type = type;
		if (type == Type.SOURCE) {
			setRegistryName(MOD_ID, "source");
			setTranslationKey("source");
			setHardness(3);
		} else {
			setRegistryName(MOD_ID, "destination");
			setTranslationKey("destination");
			if (Config.general.indestructibleDestinations) {
				setHardness(-1);
			} else {
				setHardness(3);
			}
		}
		setCreativeTab(CreativeTabs.DECORATIONS);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return type == Type.SOURCE ? new TileFoldSource() : new TileFoldDestination();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote || hand == EnumHand.OFF_HAND)
			return false;
		if (type == Type.DESTINATION) {
			if (worldIn.getTileEntity(pos) instanceof TileFoldDestination) {
				TileFoldDestination tile = ((TileFoldDestination) worldIn.getTileEntity(pos));
				if (tile.hasCore) {
					if (playerIn.getHeldItem(hand).isEmpty()) {
						playerIn.inventory.addItemStackToInventory(tile.removeCore());
						return true;
					}
				} else {
					return ((TileFoldDestination) worldIn.getTileEntity(pos)).insertCore(playerIn.getHeldItem(hand));
				}
			}
		} else if (type == Type.SOURCE) {
			if (playerIn.getHeldItem(hand).getItem() == EnderFolds.Items.SHARD) {
				TileEntity tile = worldIn.getTileEntity(pos);
				if (tile instanceof TileFoldSource) {
					((TileFoldSource) tile).readDestFromNBT(playerIn.getHeldItem(hand).getTagCompound());
					tile.markDirty();
					playerIn.getHeldItem(hand).splitStack(1);
					return true;
				}
			}
		}
		return false;
	}
}
