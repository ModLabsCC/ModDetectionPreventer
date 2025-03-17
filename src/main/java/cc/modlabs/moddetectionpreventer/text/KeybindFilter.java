package cc.modlabs.moddetectionpreventer.text;

import cc.modlabs.moddetectionpreventer.ModDetectionPreventer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Language;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.OptionalInt;


public final class KeybindFilter {
    private KeybindFilter() { throw new UnsupportedOperationException("Consider this class abstract-final"); }


    /// A list of keybindings defined by the vanilla game, along with their default keycodes.
    ///  See `GameOptionsMixin` for more information.
    public static HashMap<String, KeyBinding> unfilteredKeybindings = new HashMap<>();


    /// Returns `true` if the keybind is a vanilla keybind.
    public static boolean shouldFilterKeybinding(String keybindingToCheck) { // This is currently unused. It may be useful later on.
        return ! KeybindFilter.unfilteredKeybindings.containsKey(keybindingToCheck);
    }

    /// Gets the translated name of a key's default setting.
    public static String defaultKeybindingValue(String keybindingToCheck) {
        @Nullable KeyBinding defaultKeybinding = KeybindFilter.unfilteredKeybindings.get(keybindingToCheck);
        if (defaultKeybinding != null) {
            InputUtil.Key defaultKey  = defaultKeybinding.getDefaultKey();
            String        translation = defaultKey.getTranslationKey();
            Language      language    = Language.getInstance();
            // If the keybind key has a translation, use that.
            if (language.hasTranslation(translation)) {
                KeybindFilter.warnAccess("Falsified", keybindingToCheck);
                return language.get(keybindingToCheck);
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
        // None of the vanilla keybindings match. Return the translation.
        return TranslationFilter.localisedTranslationKey(keybindingToCheck);
    }

    /// Called when the server tries to access the client's keybindings.
    public static void warnAccess(String action, String key) {
        ModDetectionPreventer.warnAccess(action, "keybinding", key);
    }


}
