package ru.koshakmine.icstd.block;

import com.zhekasmirnov.apparatus.minecraft.enums.GameEnums;
import com.zhekasmirnov.innercore.api.NativeAPI;
import com.zhekasmirnov.innercore.api.NativeBlock;
import com.zhekasmirnov.innercore.api.NativeItem;
import com.zhekasmirnov.innercore.api.NativeItemModel;
import com.zhekasmirnov.innercore.api.unlimited.BlockRegistry;
import com.zhekasmirnov.innercore.api.unlimited.BlockVariant;
import com.zhekasmirnov.innercore.api.unlimited.IDDataPair;
import com.zhekasmirnov.innercore.api.unlimited.IDRegistry;
import ru.koshakmine.icstd.block.blockentity.BlockEntity;
import ru.koshakmine.icstd.block.blockentity.BlockEntityManager;
import ru.koshakmine.icstd.block.blockentity.BlockEntityRegistry;
import ru.koshakmine.icstd.block.blockentity.LocalBlockEntity;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.item.event.IClickable;
import ru.koshakmine.icstd.js.StorageInterfaceLib;
import ru.koshakmine.icstd.js.TileEntity;
import ru.koshakmine.icstd.js.ToolAPI;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.modloader.IBaseRegisterGameObject;
import ru.koshakmine.icstd.modloader.Mod;
import ru.koshakmine.icstd.modloader.ObjectFactory;
import ru.koshakmine.icstd.type.block.SoundType;
import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.type.tools.BlockMaterials;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public abstract class Block implements IBaseRegisterGameObject {
    protected static final ObjectFactory FACTORY = Mod.getFactory();

    private static final List<Integer> CONSTANT_VANILLA_UI_TILES = new LinkedList<>(), CONSTANT_REPLACEABLE_TILE = new LinkedList<>();
    private static HashMap<IDDataPair, BlockVariant> blockVariantMap;
    private static final HashMap<Integer, IPlaceBlock> placed = new HashMap<>();
    private static final HashMap<Integer, IClickable> clickable = new HashMap<>();

    public static void registerPlace(int id, IPlaceBlock block){
        placed.put(id, block);
    }

    public static void registerClick(int id, IClickable block){
        clickable.put(id, block);
    }

    static {
        try {
            Field field = BlockRegistry.class.getDeclaredField("blockVariantMap");
            field.setAccessible(true);
            blockVariantMap = (HashMap<IDDataPair, BlockVariant>) field.get(null);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        CONSTANT_VANILLA_UI_TILES.add(23);
        CONSTANT_VANILLA_UI_TILES.add(25);
        CONSTANT_VANILLA_UI_TILES.add(26);
        CONSTANT_VANILLA_UI_TILES.add(54);
        CONSTANT_VANILLA_UI_TILES.add(58);
        CONSTANT_VANILLA_UI_TILES.add(61);
        CONSTANT_VANILLA_UI_TILES.add(62);
        CONSTANT_VANILLA_UI_TILES.add(64);
        CONSTANT_VANILLA_UI_TILES.add(69);
        CONSTANT_VANILLA_UI_TILES.add(77);
        CONSTANT_VANILLA_UI_TILES.add(84);
        CONSTANT_VANILLA_UI_TILES.add(92);
        CONSTANT_VANILLA_UI_TILES.add(93);
        CONSTANT_VANILLA_UI_TILES.add(94);
        CONSTANT_VANILLA_UI_TILES.add(96);
        CONSTANT_VANILLA_UI_TILES.add(107);
        CONSTANT_VANILLA_UI_TILES.add(116);
        CONSTANT_VANILLA_UI_TILES.add(117);
        CONSTANT_VANILLA_UI_TILES.add(122);
        CONSTANT_VANILLA_UI_TILES.add(125);
        CONSTANT_VANILLA_UI_TILES.add(130);
        CONSTANT_VANILLA_UI_TILES.add(138);
        CONSTANT_VANILLA_UI_TILES.add(143);
        CONSTANT_VANILLA_UI_TILES.add(145);
        CONSTANT_VANILLA_UI_TILES.add(146);
        CONSTANT_VANILLA_UI_TILES.add(149);
        CONSTANT_VANILLA_UI_TILES.add(150);
        CONSTANT_VANILLA_UI_TILES.add(151);
        CONSTANT_VANILLA_UI_TILES.add(178);
        CONSTANT_VANILLA_UI_TILES.add(154);
        CONSTANT_VANILLA_UI_TILES.add(183);
        CONSTANT_VANILLA_UI_TILES.add(184);
        CONSTANT_VANILLA_UI_TILES.add(185);
        CONSTANT_VANILLA_UI_TILES.add(186);
        CONSTANT_VANILLA_UI_TILES.add(187);
        CONSTANT_VANILLA_UI_TILES.add(193);
        CONSTANT_VANILLA_UI_TILES.add(194);
        CONSTANT_VANILLA_UI_TILES.add(195);
        CONSTANT_VANILLA_UI_TILES.add(196);
        CONSTANT_VANILLA_UI_TILES.add(197);
        CONSTANT_VANILLA_UI_TILES.add(205);
        CONSTANT_VANILLA_UI_TILES.add(218);
        CONSTANT_VANILLA_UI_TILES.add(395);
        CONSTANT_VANILLA_UI_TILES.add(396);
        CONSTANT_VANILLA_UI_TILES.add(397);
        CONSTANT_VANILLA_UI_TILES.add(398);
        CONSTANT_VANILLA_UI_TILES.add(399);
        CONSTANT_VANILLA_UI_TILES.add(400);
        CONSTANT_VANILLA_UI_TILES.add(401);
        CONSTANT_VANILLA_UI_TILES.add(402);
        CONSTANT_VANILLA_UI_TILES.add(403);
        CONSTANT_VANILLA_UI_TILES.add(404);
        CONSTANT_VANILLA_UI_TILES.add(449);
        CONSTANT_VANILLA_UI_TILES.add(450);
        CONSTANT_VANILLA_UI_TILES.add(451);
        CONSTANT_VANILLA_UI_TILES.add(496);
        CONSTANT_VANILLA_UI_TILES.add(452);
        CONSTANT_VANILLA_UI_TILES.add(453);
        CONSTANT_VANILLA_UI_TILES.add(454);
        CONSTANT_VANILLA_UI_TILES.add(455);
        CONSTANT_VANILLA_UI_TILES.add(457);
        CONSTANT_VANILLA_UI_TILES.add(458);
        CONSTANT_VANILLA_UI_TILES.add(459);
        CONSTANT_VANILLA_UI_TILES.add(461);
        CONSTANT_VANILLA_UI_TILES.add(499);
        CONSTANT_VANILLA_UI_TILES.add(500);
        CONSTANT_VANILLA_UI_TILES.add(501);
        CONSTANT_VANILLA_UI_TILES.add(502);
        CONSTANT_VANILLA_UI_TILES.add(513);
        CONSTANT_VANILLA_UI_TILES.add(514);
        CONSTANT_VANILLA_UI_TILES.add(515);
        CONSTANT_VANILLA_UI_TILES.add(516);
        CONSTANT_VANILLA_UI_TILES.add(551);

        CONSTANT_REPLACEABLE_TILE.add(0);
        CONSTANT_REPLACEABLE_TILE.add(8);
        CONSTANT_REPLACEABLE_TILE.add(9);
        CONSTANT_REPLACEABLE_TILE.add(10);
        CONSTANT_REPLACEABLE_TILE.add(11);
        CONSTANT_REPLACEABLE_TILE.add(31);
        CONSTANT_REPLACEABLE_TILE.add(51);
        CONSTANT_REPLACEABLE_TILE.add(78);
        CONSTANT_REPLACEABLE_TILE.add(106);

        Event.onItemUse(((position, item, block, player) -> {
            final IPlaceBlock place = placed.get(item.id);
            final Level level = player.getRegion();

            final BlockEntityManager SERVER_MANAGER = BlockEntity.getManager();
            final BlockEntityRegistry<IBlockEntityHolder> SERVER_REGISTRY = BlockEntity.getRegistry();

            IBlockEntityHolder holder = SERVER_REGISTRY.get(block.id);
            if(holder != null) {
                SERVER_MANAGER.addBlockEntity(holder.createBlockEntity(position, level));
            }
            TileEntity.addTileEntity(position, level);

            final BlockEntity entity = (BlockEntity) SERVER_MANAGER.getBlockEntity(position, level);
            if(entity != null) {
                entity.onClick(position, item, player);
            }

            if(place != null && !NativeAPI.isDefaultPrevented()){
                final Position tile = place.onPlace(position, item, block, player, level);

                holder = SERVER_REGISTRY.get(item.id);
                if(holder != null){
                    SERVER_MANAGER.addBlockEntity(holder.createBlockEntity(tile, level));
                }

                TileEntity.addTileEntity(tile, level);

                player.setCarriedItem(item.decrease(1));
                level.playSound(position, "dig.stone", 1, 0.8f);
                NativeAPI.preventDefault();
            }

            final IClickable click = clickable.get(block.id);
            if(click != null){
                click.onClick(position, item, block, player);
            }
        }), 1);
    }

    public static boolean canTileBeReplaced(int id, int data){
        if(id == 175 && (data % 8 == 2 || data % 8 == 3)) return true;
        return CONSTANT_REPLACEABLE_TILE.contains(id);
    }

    public static boolean canTileBeReplaced(int id){
        return id == 175 || CONSTANT_REPLACEABLE_TILE.contains(id);
    }

    public static boolean doesVanillaTileHasUI(int id){
        return CONSTANT_VANILLA_UI_TILES.contains(id);
    }

    protected NativeBlock block;

    @Override
    public int getNumId() {
        if(block == null)
            return IDRegistry.genBlockID(getId());
        return block.getId();
    }

    public abstract String[] getTextures();

    @Override
    public void onPreInit() {}

    @Override
    public void onInit() {}

    @Override
    public int getPriority() {
        return 0;
    }

    private final UUID uuid = UUID.randomUUID();
    @Override
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Sets the block breaking tool
     * @return ru.koshakmine.icstd.type.tools.BlockMaterials
     */
    public String getBlockMaterial(){
        return BlockMaterials.STONE;
    }

    public int getToolLevel(){
        return 0;
    }

    public int getMaterial() {
        return 3;
    }

    public int getMaterialBase() {
        return 0;
    }

    public String getSoundType() {
        return SoundType.STONE;
    }

    public boolean isSolid() {
        return true;
    }

    public boolean canContainLiquid() {
        return false;
    }

    public boolean canBeExtraBlock() {
        return false;
    }

    public boolean renderAllFaces() {
        return false;
    }

    public int getRenderType() {
        return 0;
    }

    public int getRenderLayer() {
        return GameEnums.getInt(GameEnums.getSingleton().getEnum("block_render_layer", "alpha"));
    }

    public int getLightLevel() {
        return 0;
    }

    public int getLightOpacity() {
        return 0;
    }

    public float getExplosionResistance() {
        return 3.0f;
    }

    public float getFriction() {
        return 0.6f;
    }

    public float getDestroyTime() {
        return 1.0f;
    }

    public float getTranslucency() {
        return 1.0f;
    }

    public int getMapColor() {
        return 0;
    }

    public boolean addToCreativeInventory() {
        return true;
    }

    protected static String[] fixedTextures(String[] textures){
        final String[] fixedTextures = new String[6];
        String lastTexture = "missing_block";
        for (int i = 0; i < 6; i++)
            if(i < textures.length)
                fixedTextures[i] = lastTexture = textures[i];
            else {
                fixedTextures[i] = lastTexture;
            }
        return fixedTextures;
    }

    private int data;
    public void addVariant(String name, String[] textures){
        if(data >= 16)
            throw new RuntimeException("Limit variant block");

        textures = fixedTextures(textures);
        final BlockVariant variant = new BlockVariant(getNumId(), data++, name, textures, new int[textures.length], false);
        block.addVariant(name, variant.textures, variant.textureIds);
        blockVariantMap.put(new IDDataPair(variant.uid, variant.data), variant);


    final NativeItemModel model = NativeItemModel.getFor(variant.uid, variant.data);
        model.updateForBlockVariant(variant);
        if(model.getCacheKey() == null){
            model.setCacheKey("modded");
        }
        model.isLazyLoading = variant.isTechnical;
    }

    public NativeBlock createBlock(){
        block = NativeBlock.createBlock(IDRegistry.genBlockID(getId()), getId(), "blank", 0);
        addVariant(getName(), getTextures());
        return block;
    }

    public String getBlockEntityType(){
        return getId();
    }

    @Override
    public void factory() {
        createBlock();

        NativeBlock.setMaterial(block.getId(), getMaterial());
        NativeBlock.setMaterialBase(block.getId(), getMaterialBase());
        NativeBlock.setSoundType(block.getId(), getSoundType());
        NativeBlock.setSolid(block.getId(), isSolid());
        NativeBlock.setCanContainLiquid(block.getId(), canContainLiquid());
        NativeBlock.setCanBeExtraBlock(block.getId(), canBeExtraBlock());
        NativeBlock.setRenderAllFaces(block.getId(), renderAllFaces());
        NativeBlock.setRenderType(block.getId(), getRenderType());
        NativeBlock.setRenderLayer(block.getId(), getRenderLayer());
        NativeBlock.setLightLevel(block.getId(), getLightLevel());
        NativeBlock.setLightOpacity(block.getId(), getLightOpacity());
        NativeBlock.setExplosionResistance(block.getId(), getExplosionResistance());
        NativeBlock.setFriction(block.getId(), getFriction());
        NativeBlock.setDestroyTime(block.getId(), getDestroyTime());
        NativeBlock.setTranslucency(block.getId(), getTranslucency());
        NativeBlock.setMapColor(block.getId(), getMapColor());
        ToolAPI.registerBlockMaterial(block.getId(), getBlockMaterial(), getToolLevel());

        if (this instanceof IShapedBlock) {
            IShapedBlock shapedBlock = (IShapedBlock) this;
            shapedBlock.getShape().setToBlock(block.getId(), 0);

            final BlockVariant variant = BlockRegistry.getBlockVariant(block.getId(), 0);

            if (variant != null) {
                variant.shape = shapedBlock.getShape();
            }
        }

        if (this instanceof ILocalBlockEntityHolder) {
            LocalBlockEntity.getRegistry().registerBlockEntity(getBlockEntityType(), (ILocalBlockEntityHolder) this);
            LocalBlockEntity.getRegistry().registerBlockEntity(getBlockEntityType(), getNumId());
        }

        if (this instanceof IBlockEntityHolder) {
            BlockEntity.getRegistry().registerBlockEntity(getBlockEntityType(), (IBlockEntityHolder) this);
            BlockEntity.getRegistry().registerBlockEntity(getBlockEntityType(), getNumId());
        }

        if (this instanceof IDropBlock) {
            ToolAPI.registerDropFunction(block.getId(), (IDropBlock) this, getToolLevel());
        }

        if (this instanceof IPlaceBlock) {
            placed.put(getNumId(), (IPlaceBlock) this);
        }

        if(this instanceof IClickable){
            clickable.put(getNumId(), (IClickable) this);
        }

        if(this instanceof StorageInterfaceLib.StorageDescriptor){
            StorageInterfaceLib.createInterface(getNumId(), (StorageInterfaceLib.StorageDescriptor) this);
        }

        if (addToCreativeInventory()) {
            NativeItem.addToCreative(block.getId(), 1, 0, null);
        }
    }

    public NativeBlock getNativeBlock() {
        return block;
    }
}
