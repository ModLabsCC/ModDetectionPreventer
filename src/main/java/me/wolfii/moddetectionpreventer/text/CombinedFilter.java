package me.wolfii.moddetectionpreventer.text;

import net.minecraft.text.*;


public final class CombinedFilter {
    private CombinedFilter() { throw new UnsupportedOperationException("Consider this class abstract-final"); }


    /// Recursively walks through the components of a `Text`, masking any which should be filtered.
    public static Text filterComponents(Text message) {
        MutableText filtered = MutableText.of(message.getContent());

        // Filter keybind components.
        if (message.getContent() instanceof KeybindTextContent keybindTextContent) {
            String keybind = keybindTextContent.getKey();
            // Send back Minecraft's factory setting value of the keybind.
            filtered = MutableText.of(new PlainTextContent.Literal(KeybindFilter.defaultKeybindingValue(keybind)));
        }

        // Filter translatable components.
        if (message.getContent() instanceof TranslatableTextContent translatableTextContent) {
            String translationKey = translatableTextContent.getKey();
            filtered = MutableText.of(new PlainTextContent.Literal(TranslationFilter.localisedTranslationKey(translationKey)));
        }

        filtered.setStyle(message.getStyle());
        for (Text sibling : message.getSiblings()) {
            filtered.append(CombinedFilter.filterComponents(sibling));
        }

        return filtered;
    }


}
