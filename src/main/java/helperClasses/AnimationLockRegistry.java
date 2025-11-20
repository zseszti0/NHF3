package helperClasses;

import javafx.beans.value.WritableValue;

import java.util.*;

public class AnimationLockRegistry {

    // Node → Set of currently animated properties
    private static final WeakHashMap<javafx.scene.Node, Set<WritableValue<?>>> locks = new WeakHashMap<>();

    public static boolean isPropertyLocked(javafx.scene.Node node, WritableValue<?> prop) {
        return locks.getOrDefault(node, Collections.emptySet()).contains(prop);
    }

    public static boolean anyPropertyLocked(javafx.scene.Node node, Collection<WritableValue<?>> props) {
        Set<WritableValue<?>> set = locks.get(node);
        if (set == null) return false;
        for (WritableValue<?> p : props) {
            if (set.contains(p)) return true;
        }
        return false;
    }

    public static void lockProperties(javafx.scene.Node node, Collection<WritableValue<?>> props) {
        locks.computeIfAbsent(node, k -> new HashSet<>()).addAll(props);
    }

    public static void unlockProperties(javafx.scene.Node node, Collection<WritableValue<?>> props) {
        Set<WritableValue<?>> set = locks.get(node);
        if (set == null) return;
        set.removeAll(props);
        if (set.isEmpty()) locks.remove(node);
    }
}
