ConfigureMultiplayer({
	isClientOnly: true
});

WRAP_JAVA("ru.koshakmine.icstd.modloader.Mod").runMods();
WRAP_JAVA("ru.koshakmine.icstd.js.ToolAPI").init({
	addBlockMaterial(name, breakingMultiplier){
		ToolAPI.addBlockMaterial(String(name), Number(breakingMultiplier));
	},
	addToolMaterial(name, obj){
		for(let key in obj)
			obj[key] = Number(obj[key]);
		ToolAPI.addToolMaterial(String(name), obj);
	},
	getBlockDestroyLevel(id){
		return ToolAPI.getBlockDestroyLevel(Number(id));
	},
	getBlockMaterialName(id){
		return ToolAPI.getBlockMaterialName(Number(id));
	},
	getToolLevel(id){
		return ToolAPI.getToolLevel(Number(id));
	},
	getToolLevelViaBlock(itemId, blockId){
		return ToolAPI.getToolLevelViaBlock(Number(itemId), Number(blockId));
	},
	registerSword(id, material){
		if(material instanceof java.lang.String)
			material = String(material);
		else
			for(let key in material)
				material[key] = Number(material[key]);
		ToolAPI.registerSword(Number(id), material);
	},
	registerTool(id, material, blocks){
		if(material instanceof java.lang.String)
			material = String(material);
		else
			for(let key in material)
				material[key] = Number(material[key]);
		for(let i in blocks)
			blocks[i] = String(blocks[i]);
		ToolAPI.registerTool(Number(id), material, blocks);
	},
	registerBlockMaterial(id, material, level){
		ToolAPI.registerBlockMaterial(Number(id), String(material), Number(level));
	},
	registerDropFunction(id, func, level){
	    Block.registerDropFunction(id, func, level);
	}
});
WRAP_JAVA("ru.koshakmine.icstd.js.LiquidRegistry").init(LiquidRegistry);

Launch();