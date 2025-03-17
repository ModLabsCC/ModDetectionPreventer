package cc.modlabs.moddetectionpreventer.mixin.datagetter;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import cc.modlabs.moddetectionpreventer.text.KeybindFilter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.StickyKeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.function.BooleanSupplier;


@Mixin(GameOptions.class)
class GameOptionsMixin {

    /// When the vanilla keybindings start loading, reset the list of keybindings to pass through.
    @Inject(
            method = "<init>",
            at     = @At(
                    value  = "INVOKE",
                    target = "Ljava/lang/Object;<init>()V",
                    shift  = At.Shift.AFTER
            )
    )
    private void resetAllowedKeys(MinecraftClient client, File optionsFile, CallbackInfo ci) {
        KeybindFilter.unfilteredKeybindings.clear();
    }

    /// When the vanilla keybindings are initialised, keep a record of them.
    @WrapOperation(
            method = "<init>",
            at     = @At(
                    value  = "NEW",
                    target = "(Ljava/lang/String;Lnet/minecraft/client/util/InputUtil$Type;ILjava/lang/String;)Lnet/minecraft/client/option/KeyBinding;"
            )
    )
    private KeyBinding getAllowedKeys0(String translationKey, InputUtil.Type type, int code, String category, Operation<KeyBinding> original) {
        KeyBinding keybinding = original.call(translationKey, type, code, category);
        KeybindFilter.unfilteredKeybindings.put(translationKey, keybinding);
        return keybinding;
    }

    /// When the vanilla keybindings are initialised, keep a record of them.
    @WrapOperation(
            method = "<init>",
            at     = @At(
                    value  = "NEW",
                    target = "(Ljava/lang/String;ILjava/lang/String;)Lnet/minecraft/client/option/KeyBinding;"
            )
    )
    private KeyBinding getAllowedKeys1(String translationKey, int code, String category, Operation<KeyBinding> original) {
        KeyBinding keybinding = original.call(translationKey, code, category);
        KeybindFilter.unfilteredKeybindings.put(translationKey, keybinding);
        return keybinding;
    }

    /// When the vanilla keybindings are initialised, keep a record of them.
    @WrapOperation(
            method = "<init>",
            at     = @At(
                    value  = "NEW",
                    target = "(Ljava/lang/String;ILjava/lang/String;Ljava/util/function/BooleanSupplier;)Lnet/minecraft/client/option/StickyKeyBinding;"
            )
    )
    private StickyKeyBinding getAllowedKeys2(String translationKey, int code, String category, BooleanSupplier toggleGetter, Operation<StickyKeyBinding> original) {
        StickyKeyBinding keybinding = original.call(translationKey, code, category, toggleGetter);
        KeybindFilter.unfilteredKeybindings.put(translationKey, keybinding);
        return keybinding;
    }

}
