package cc.modlabs.moddetectionpreventer.mixin.antidetection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import cc.modlabs.moddetectionpreventer.text.CombinedFilter;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


/// Catches incoming anvil screens, filtering out components.
@Mixin(AnvilScreen.class)
class AnvilEditScreenMixin {

    @WrapOperation(
            method = "onSlotUpdate",
            at     = @At(
                    value  = "INVOKE",
                    target = "Lnet/minecraft/text/Text;getString()Ljava/lang/String;"
            )
    )
    private String preventAnvilItemNameChangeModDetection(Text instance, Operation<String> original) {
        return original.call(CombinedFilter.filterComponents(instance));
    }

    @WrapOperation(
            method = "onRenamed",
            at     = @At(
                    value  = "INVOKE",
                    target = "Lnet/minecraft/text/Text;getString()Ljava/lang/String;"
            )
    )
    private String preventAnvilItemNameCheckModDetection(Text instance, Operation<String> original) {
        return original.call(CombinedFilter.filterComponents(instance));
    }

}
