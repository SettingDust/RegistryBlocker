Change config in `config/registry-blocker.json`.   
The key is registry identifier, the value is entry identifier.  
Config should reload when reloading data packs.  
There will be error in `latest.log` if the registry is referenced by another entry.

Recommend use with https://modrinth.com/mod/data-dumper to dump matched registry with tag or regex.  
Recommend use with https://modrinth.com/mod/mod-sets to disable the developing mods in the published pack.

## Use Case

- Remove structures.
- Remove items that needn't in the pack.
- Disable structure sets and grouping all the structures in a few structure sets so that avoid overlapping.

## Example

```json5
{
    // Since https://github.com/misode/mcmeta/blob/data-json/data/minecraft/worldgen/structure_set/pillager_outposts.json#L6 is requiring this entry. The world won't load
    "minecraft:worldgen/structure_set": [
        "minecraft:villages"
    ]
}
```
