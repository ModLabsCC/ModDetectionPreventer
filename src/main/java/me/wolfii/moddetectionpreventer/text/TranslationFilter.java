package me.wolfii.moddetectionpreventer.text;

import me.wolfii.moddetectionpreventer.ModDetectionPreventer;

import java.util.HashMap;


public final class TranslationFilter {
    private TranslationFilter() { throw new UnsupportedOperationException("Consider this class abstract-final"); }


    /// Translation keys defined in the `vanilla` and `realms` resource packs will be passed through.
    public static String VANILLA_PACK_NAME = "vanilla";

    /// A regular expression which detects if a resource pack id is a server resource pack.
    public static String SERVER_PACK_PATH = "server-resource-packs";

    /// A list of translation keys defined by the namespaces listed in `UNFILTERED_TRANSLATION_KEY_NAMESPACES`.
    ///  See `TranslationStorageMixin` for more information.
    public static HashMap<String, String> unfilteredTranslationKeys = new HashMap<>();



    /// Returns `true` if the translation key is vanilla, realms, or provided by the server/world.
    public static boolean shouldFilterTranslationKey(String translationKeyToCheck) {
        return ! TranslationFilter.unfilteredTranslationKeys.containsKey(translationKeyToCheck);
    }

    /// Get the localised value of a translation key.
    public static String localisedTranslationKey(String translationKey) {
        if (TranslationFilter.shouldFilterTranslationKey(translationKey)) {
            TranslationFilter.warnAccess("Blocked", translationKey);
            return translationKey;
        } else {
            TranslationFilter.warnAccess("Allowed", translationKey);
            return TranslationFilter.unfilteredTranslationKeys.get(translationKey); // Guaranteed to exist.
        }
    }

    /// Called when the server tries to access the client's keybindings.
    public static void warnAccess(String action, String key) {
        ModDetectionPreventer.warnAccess(action, "translation key", key);
    }


}
