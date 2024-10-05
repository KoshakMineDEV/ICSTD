package ru.koshakmine.icstd.type.common;

import com.zhekasmirnov.innercore.api.commontypes.Coords;
import com.zhekasmirnov.innercore.api.commontypes.ScriptableParams;
import ru.koshakmine.icstd.entity.Entity;

public class ProjectileHitTarget {
   public float x, y, z;
   public Entity entity;
   public BlockPosition position;

   public ProjectileHitTarget(ScriptableParams params) {
       this.x = (float) params.get("x", params);
       this.y = (float) params.get("y", params);
       this.z = (float) params.get("z", params);
       this.entity = new Entity((long) params.get("entity", params));
       this.position = new BlockPosition((Coords) params.get("coords", params));
   }

    @Override
    public String toString() {
        return "ProjectileHitTarget{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", entity=" + entity +
                ", position=" + position +
                '}';
    }
}
