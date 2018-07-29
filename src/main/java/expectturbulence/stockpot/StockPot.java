package expectturbulence.stockpot;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(modid = StockPot.MODID, name = StockPot.NAME, version = StockPot.VERSION)
public class StockPot {
    public static final String MODID = "stockpot";
    public static final String NAME = "StockPot";
    public static final String VERSION = "0.0.1";
    public static Random random = new Random();

    private static Logger logger;
    CookingPot cookingPot;

    @SidedProxy(clientSide = "expectturbulence.stockpot.StockPot$ClientProxy", serverSide = "expectturbulence.stockpot.StockPot$CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        logger = event.getModLog();
        proxy.preInit(event);
    }

    public static class CommonProxy {
        public void preInit(FMLPreInitializationEvent event) {
            //logger.info("CommonProxy preInit");
            RecipeDictionary recipeDictionary = new RecipeDictionary();
            recipeDictionary.addRecipe(Items.BEETROOT_SOUP, new Item[]{Items.BEETROOT, Items.BEETROOT_SEEDS});
            recipeDictionary.addRecipe(Items.CAKE, new Item[]{Items.APPLE, Items.CARROT});
            logger.info(recipeDictionary.toString()); // TODO: For debugging purposes only, remove before release

        }
    }

    public static class ClientProxy extends CommonProxy {
        @Override
        public void preInit(FMLPreInitializationEvent event) {
            super.preInit(event);

        }
    }
    // Shamelessly stolen from elucent, not sure if I'll even need it. Probably not
    public static class ItemBase extends Item {
        public ItemBase(String name){
            setRegistryName(StockPot.MODID+":"+name);
            setUnlocalizedName(name);
            setCreativeTab(CreativeTabs.FOOD);
        }
        public void initModel() {
            ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(),"inventory"));
        }
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        //logger.info("Registering Blocks");
        cookingPot = new CookingPot();
        event.getRegistry().register(cookingPot);

    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        //logger.info("Registering Items");
        event.getRegistry().register(new ItemBlock(cookingPot).setRegistryName(cookingPot.getRegistryName()));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(cookingPot), 0, new ModelResourceLocation(cookingPot.getRegistryName(),"inventory"));
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        //logger.info("Registering Recipes");
    }
}
