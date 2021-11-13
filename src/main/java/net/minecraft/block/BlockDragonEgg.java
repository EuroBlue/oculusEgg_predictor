package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOculusEggOnOff;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.LoggingPrintStream;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.lwjgl.Sys;

public class BlockDragonEgg extends Block
{
    public BlockDragonEgg()
    {
        super(Material.dragonEgg, MapColor.blackColor);
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    /**
     * Called when a neighboring block changes.
     */
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        this.checkFall(worldIn, pos);
    }

    private void checkFall(World worldIn, BlockPos pos)
    {
        if (BlockFalling.canFallInto(worldIn, pos.down()) && pos.getY() >= 0)
        {
            int i = 32;

            if (!BlockFalling.fallInstantly && worldIn.isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i)))
            {
                worldIn.spawnEntityInWorld(new EntityFallingBlock(worldIn, (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), this.getDefaultState()));
            }
            else
            {
                worldIn.setBlockToAir(pos);
                BlockPos blockpos;

                for (blockpos = pos; BlockFalling.canFallInto(worldIn, blockpos) && blockpos.getY() > 0; blockpos = blockpos.down())
                {
                    ;
                }

                if (blockpos.getY() > 0)
                {
                    worldIn.setBlockState(blockpos, this.getDefaultState(), 2);
                }
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        //this.dropBlockAsItem(worldIn,pos,state,1);
        Minecraft.logger.info("Hier wird geclickt");
        this.teleport(worldIn, pos);
        return true;
    }

    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {
        this.teleport(worldIn, pos);
    }
    public void dropBlockAsItem(World worldIn, BlockPos pos, IBlockState state, int forture)
    {
        this.dropBlockAsItemWithChance(worldIn, pos, state, 1.0F, forture);
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        Item item = this.getItemDropped(state, worldIn.rand, fortune);

        if (item != null)
        {
            spawnAsEntity(worldIn, pos, new ItemStack(item, 64, this.damageDropped(state)));
        }
    }

    /**
     * Spawns the given ItemStack as an EntityItem into the World at the given position
     */
    public static void spawnAsEntity(World worldIn, BlockPos pos, ItemStack stack) {
        EntityItem entityitem = new EntityItem(worldIn, (double) pos.getX()+2 , (double) pos.getY() +2, (double) pos.getZ() , stack);
        entityitem.setDefaultPickupDelay();
        worldIn.spawnEntityInWorld(entityitem);
    }
    private void teleport(World worldIn, BlockPos pos)
    {
        Minecraft.logger.info(worldIn.getSeed());
        IBlockState iblockstate = worldIn.getBlockState(pos);
        boolean activ= GuiOculusEggOnOff.actiiv;
        boolean on=GuiOculusEggOnOff.block_switch;
        BlockPos pos_davor=pos.add(0,-1,0);
        Block davor=worldIn.getBlockState(pos_davor).getBlock();
        BlockPos blockposi=null;
        if(activ)
        {
            MinecraftServer server=MinecraftServer.getServer();
//            Minecraft.logger.info(server.getEntityWorld().getSeed());
////        server.deleteWorldAndStopServer();
//            if(server!=null)
//            {
//                if(server.worldServers!=null)
//                {
//                    for (int i = 0; i < server.worldServers.length; i++) {
//                        Minecraft.logger.info(i + server.worldServers[i].getProviderName() + server.worldServers[i].getSeed());
//                    }
//                }
//                else
//                {
//                    Minecraft.logger.info("worldServer null");
//                }
//            }
//            else
//            {
//                Minecraft.logger.info("server null");
//            }
            if (iblockstate.getBlock() == this)
            {
                for (int i = 0; i < 1000; ++i)
                {
                    BlockPos blockpos = pos.add(worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16), 0, worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16));
                    BlockPos pos_neu=blockpos.add(0,-1,0);
                    if (worldIn.getBlockState(blockpos).getBlock().blockMaterial == Material.air&&worldIn.getBlockState(pos_neu).getBlock().blockMaterial != Material.air&& (worldIn.getBlockState(pos_neu).getBlock().getClass()!=davor.getClass())==on)
                    {
                        Minecraft.s="\nSeed: "+worldIn.getSeed()+"\nNeu: "+worldIn.getBlockState(pos_neu).getBlock().getClass().toString()+"\nAlt:"+davor.getClass();
                        Minecraft.logger.info(Minecraft.s);
                        if (worldIn.isRemote)
                        {
                            for (int j = 0; j < 128; ++j)
                            {
                                double d0 = worldIn.rand.nextDouble();
                                float f = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
                                float f1 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
                                float f2 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
                                double d1 = (double)blockpos.getX() + (double)(pos.getX() - blockpos.getX()) * d0 + (worldIn.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
                                double d2 = (double)blockpos.getY() + (double)(pos.getY() - blockpos.getY()) * d0 + worldIn.rand.nextDouble() * 1.0D - 0.5D;
                                double d3 = (double)blockpos.getZ() + (double)(pos.getZ() - blockpos.getZ()) * d0 + (worldIn.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
                                worldIn.spawnParticle(EnumParticleTypes.PORTAL, d1, d2, d3, (double)f, (double)f1, (double)f2, new int[0]);
                            }
                        }
                        worldIn.setBlockState(blockpos, iblockstate, 2);
                        worldIn.setBlockToAir(pos);
                        if(worldIn.isRemote)
                        {
                            worldIn.setBlockToAir(blockpos);
                        }
                        return;
                    }
                }
            }
        }
        else
        {
            if (iblockstate.getBlock() == this)
            {
                for (int i = 0; i < 1000; ++i)
                {
                    BlockPos blockpos = pos.add(worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16), worldIn.rand.nextInt(8) - worldIn.rand.nextInt(8), worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16));

                    if (worldIn.getBlockState(blockpos).getBlock().blockMaterial == Material.air)
                    {
                        if (worldIn.isRemote)
                        {
                            for (int j = 0; j < 128; ++j)
                            {
                                double d0 = worldIn.rand.nextDouble();
                                float f = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
                                float f1 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
                                float f2 = (worldIn.rand.nextFloat() - 0.5F) * 0.2F;
                                double d1 = (double)blockpos.getX() + (double)(pos.getX() - blockpos.getX()) * d0 + (worldIn.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
                                double d2 = (double)blockpos.getY() + (double)(pos.getY() - blockpos.getY()) * d0 + worldIn.rand.nextDouble() * 1.0D - 0.5D;
                                double d3 = (double)blockpos.getZ() + (double)(pos.getZ() - blockpos.getZ()) * d0 + (worldIn.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
                                worldIn.spawnParticle(EnumParticleTypes.PORTAL, d1, d2, d3, (double)f, (double)f1, (double)f2, new int[0]);
                            }
                        }
                        else
                        {
                            worldIn.setBlockState(blockpos, iblockstate, 2);
                            System.out.println("WAT BLYAT");
                            worldIn.setBlockToAir(pos);
                        }

                        return;
                    }
                }
            }
        }
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 5;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    public Item getItem(World worldIn, BlockPos pos)
    {
        return null;
    }
}
