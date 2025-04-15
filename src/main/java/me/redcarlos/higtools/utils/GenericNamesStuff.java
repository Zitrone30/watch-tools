package me.redcarlos.higtools.utils;

import jdk.jfr.Event;
import net.minecraft.util.NameGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GenericNamesStuff extends Event {
    public static GenericNames genericNames;
    public void onInitialize() {
            genericNames.clear();
        };


    static {
        genericNames = new GenericNames();
    }

    public static class GenericNames {
        private final Map<UUID, String> names = new HashMap<>();

        public String getName(UUID uuid) {
            this.names.computeIfAbsent(uuid, (k) -> NameGenerator.name(uuid));
            return this.names.get(uuid);
        }

        public void clear() {
            this.names.clear();
        }
    }    }


