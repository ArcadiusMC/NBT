package net.forthecrown.nbt.paper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
final class Reflect {
  private Reflect() {}

  static String craftBukkitPackage;

  /**
   * @param className Should be the class name path from the {@code craftbukkit} package,
   *                  so for example, the input should be {@code inventory.CraftItemStack} as it'll
   *                  become {@code org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack}
   */
  public static Class<?> getCraftBukkitClass(String className) {
    String fqcn = getCraftBukkitPackage() + "." + className;

    try {
      return Class.forName(fqcn);
    } catch (ReflectiveOperationException exc) {
      throw new RuntimeException(exc);
    }
  }

  static MethodHandle findHandle(
      Class<?> lookupClass,
      String name,
      Class returnType,
      Class... paramTypes
  ) {
    Lookup lookup = MethodHandles.publicLookup();
    MethodType type = MethodType.methodType(returnType, paramTypes);

    try {
      MethodHandle handle = lookup.findVirtual(lookupClass, name, type);
      return handle;
    } catch (ReflectiveOperationException exc) {
      throw new RuntimeException(exc);
    }
  }

  private static String getCraftBukkitPackage() {
    if (craftBukkitPackage != null) {
      return craftBukkitPackage;
    }

    Class<?> metaClass = Bukkit.getItemFactory()
        .getItemMeta(Material.TORCH)
        .getClass();

    String packageName = metaClass.getPackageName();

    return craftBukkitPackage = packageName.replace(".inventory", "");
  }

  static <T> T invokeHandle(MethodHandle handle, Object... args) {
    try {
      Object o = handle.invokeWithArguments(args);
      return (T) o;
    } catch (Throwable t) {
      Throwable cause;

      if (t instanceof InvocationTargetException target) {
        cause = target.getCause();
      } else {
        cause = t;
      }

      throw new RuntimeException(cause);
    }
  }
}