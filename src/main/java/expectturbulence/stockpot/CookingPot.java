package expectturbulence.stockpot;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.Random;

public class CookingPot extends Block {

    static final PropertyBool PROPERTY_LID = PropertyBool.create("lid");
    static final PropertyBool PROPERTY_WOOD = PropertyBool.create("wood");
    static final PropertyInteger PROPERTY_LEVEL = PropertyInteger.create("level", 0, 3);

    public CookingPot() {
        super(Material.IRON);
        setUnlocalizedName(StockPot.MODID + ".cookingpot");
        setRegistryName("cookingpot");
        setCreativeTab(CreativeTabs.FOOD);
        setTickRandomly(true);

        setDefaultState(this.blockState.getBaseState().withProperty(PROPERTY_LID, false).withProperty(PROPERTY_WOOD, false).withProperty(PROPERTY_LEVEL, 0));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{PROPERTY_LID, PROPERTY_WOOD, PROPERTY_LEVEL});
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        int lidBits = (meta & 0x1);
        int woodBits = (meta & 0x2) >> 1;
        int levelBits = (meta & 0xc) >> 2;
        return getDefaultState().withProperty(PROPERTY_LID, lidBits==1).withProperty(PROPERTY_WOOD, woodBits==1).withProperty(PROPERTY_LEVEL, levelBits);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int lidBits = state.getValue(PROPERTY_LID)?1:0;
        int woodBits = state.getValue(PROPERTY_WOOD)?1:0 << 1;
        int levelBits = state.getValue(PROPERTY_LEVEL) << 2;
        return lidBits | woodBits | levelBits;
    }

    /*
                Used when players add ingredients, or to initiate cooking
                 */
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote){
            // Shift Right Click
            if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                worldIn.setBlockState(pos, state.withProperty(PROPERTY_WOOD, true));
                // Start a cookin'
                return true;
            }
            // Normal Right Click
            else if(!(playerIn.getHeldItem(EnumHand.MAIN_HAND).equals(ItemStack.EMPTY))){
                if(state.getValue(PROPERTY_WOOD) == true){
                    worldIn.setBlockState(pos, state.withProperty(PROPERTY_LID, !state.getValue(PROPERTY_LID)));
                }
                // Check if the held item is a valid ingredient

                // If it's valid, and there are four or fewer in the pot, add it to the pot
                //if(valid ingredient){return true;}

                // If it's not valid, or there's five or more in the pot, do nothing
                //else{return false;}

                return true;
            }
            return false;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if(stateIn.getValue(PROPERTY_WOOD) == true) {
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + 0.2D;
            double d2 = (double) pos.getZ() + 0.5D;
            double d3 = 0.22D;
            double d4 = 0.27D;

            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    /*
        Drop Cooking Pot item when harvested
         */
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    protected boolean canDie(World worldIn, BlockPos pos)
    {
        return worldIn.isRainingAt(pos) || worldIn.isRainingAt(pos.west()) || worldIn.isRainingAt(pos.east()) || worldIn.isRainingAt(pos.north()) || worldIn.isRainingAt(pos.south());
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
}