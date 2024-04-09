Change config in `config/registry-blocker.json`.   
The key is registry identifier, the value is entry identifier.  
Config should reload when reloading data packs.
Recommend use with https://modrinth.com/mod/data-dumper to dump matched registry with tag or regex

### Example
```json5
{
    // Since https://github.com/misode/mcmeta/blob/data-json/data/minecraft/worldgen/structure_set/pillager_outposts.json#L6 is requiring this entry. The world won't load
    "minecraft:worldgen/structure_set": [
        "minecraft:villages"
    ]
}
```
