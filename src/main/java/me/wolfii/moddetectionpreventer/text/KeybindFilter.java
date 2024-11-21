package me.wolfii.moddetectionpreventer.text;

import me.wolfii.moddetectionpreventer.ModDetectionPreventer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Language;

import java.util.Arrays;
import java.util.OptionalInt;


public final class KeybindFilter {
    private KeybindFilter() { throw new UnsupportedOperationException("Consider this class abstract-final"); }


    /// Returns `true` if the keybind is a vanilla keybind.
    public static boolean shouldFilterKeybinding(String keybindingToCheck) { // This is currently unused. It may be useful later on.
        return Arrays.stream(MinecraftClient.getInstance().options.allKeys).noneMatch(
                key -> keybindingToCheck.equals(key.getTranslationKey()) // This checks if vanilla contains the keybinding to check.
        );
    }

    /// Gets the translated name of a key's default setting.
    public static String defaultKeybindingValue(String keybindingToCheck) {
        for (KeyBinding key : MinecraftClient.getInstance().options.allKeys) {
            if (keybindingToCheck.equals(key.getTranslationKey())) {
                Language language    = Language.getInstance();
                InputUtil.Key defaultKey  = key.getDefaultKey();
                String        translation = defaultKey.getTranslationKey();
                // If the keybind key has a translation, use that.
                if (language.hasTranslation(translation)) {
                    KeybindFilter.warnAccess("Falsified", keybindingToCheck);
                    return language.get(translation);
                }
                // If the default keybind is not a control character, use that.
                OptionalInt keyCodeInt = defaultKey.toInt();
                if (keyCodeInt.isPresent()) {
                    char keyCode = (char) keyCodeInt.getAsInt();
                    if (! Character.isISOControl(keyCode)) {
                        KeybindFilter.warnAccess("Falsified", keybindingToCheck);
                        return Character.toString(keyCode);
                    }
                }
                // Otherwise just use the final part of the translation key.
                String[] parts = translation.split("\\.");
                KeybindFilter.warnAccess("Falsified", keybindingToCheck);
                return parts[parts.length - 1].toUpperCase();
            }
        }
        // None of the vanilla keybindings match. Return the translation.
        return TranslationFilter.localisedTranslationKey(keybindingToCheck);
    }

    /// Called when the server tries to access the client's keybindings.
    public static void warnAccess(String action, String key) {
        ModDetectionPreventer.warnAccess(action, "keybinding", key);
    }


}
