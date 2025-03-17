package cc.modlabs.moddetectionpreventer.mixin.datagetter;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import cc.modlabs.moddetectionpreventer.text.TranslationFilter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.resource.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;


/// This mixin is used to figure out what translation keys should be filtered.
///  See `TranslationFilter` for more information.
@Mixin(TranslationStorage.class)
class TranslationStorageMixin {

    @Unique
    private static HashMap<String, String> nextUnfilteredTranslationKeys = new HashMap<>();



    /// When the language starts loading, reset the list of translation keys to pass through.
    @Inject(
            method = "load(Lnet/minecraft/resource/ResourceManager;Ljava/util/List;Z)Lnet/minecraft/client/resource/language/TranslationStorage;",
            at     = @At(
                    value = "HEAD"
            )
    )
    private static void clearAllowedKeys(
            ResourceManager resourceManager,
            List<String> definitions,
            boolean rightToLeft,
            CallbackInfoReturnable<TranslationStorage> cir
    ) {
        TranslationStorageMixin.nextUnfilteredTranslationKeys = new HashMap<>();
    }


    /// For each translation json file, check the resource pack source.
    ///
    /// Translation keys defined by the vanilla pack, or by resource packs sent from the server/world, bypass the filter.
    ///  This (should) makes it impossible for a server to see if keys are stripped from signs/anvils, which is how they used to check if this mod is installed.
    @WrapOperation(
            method = "load(Ljava/lang/String;Ljava/util/List;Ljava/util/Map;)V",
            at     = @At(
                    value  = "INVOKE",
                    target = "Lnet/minecraft/util/Language;load(Ljava/io/InputStream;Ljava/util/function/BiConsumer;)V"
            )
    )
    private static void checkPack(InputStream inputStream, BiConsumer<String, String> entryConsumer, Operation<Void> original, @Local Resource resource) {
        ResourcePack pack           = resource.getPack();
        boolean      shouldUnfilter = false;

        // Vanilla and realms packs.
        if (pack instanceof DefaultResourcePack defaultPack) {
            shouldUnfilter = true;
        }

        // Server resource packs.
        if (pack instanceof ZipResourcePack zipPack) {
            Path path = MinecraftClient.getInstance().runDirectory.toPath().relativize(zipPack.zipFile.file.toPath());
            if (path.getName(0).toString().equals(TranslationFilter.SERVER_PACK_PATH)) {
                shouldUnfilter = true;
            }
        }

        if (shouldUnfilter) {
            original.call(inputStream, (BiConsumer<String, String>) (translationKey, localisedValue) -> {
                TranslationStorageMixin.nextUnfilteredTranslationKeys.put(translationKey, localisedValue);
                entryConsumer.accept(translationKey, localisedValue);
            });
        } else {
            original.call(inputStream, entryConsumer);
        }
    }


    /// When all translation json files finish loading, swap the main filter to the new allowed translation keys list.
    @Inject(
            method = "load(Lnet/minecraft/resource/ResourceManager;Ljava/util/List;Z)Lnet/minecraft/client/resource/language/TranslationStorage;",
            at     = @At(
                    value = "RETURN"
            )
    )
    private static void swapAllowedKeys(
            ResourceManager resourceManager,
            List<String> definitions,
            boolean rightToLeft,
            CallbackInfoReturnable<TranslationStorage> cir
    ) {
        TranslationFilter.unfilteredTranslationKeys = TranslationStorageMixin.nextUnfilteredTranslationKeys;
    }


}
