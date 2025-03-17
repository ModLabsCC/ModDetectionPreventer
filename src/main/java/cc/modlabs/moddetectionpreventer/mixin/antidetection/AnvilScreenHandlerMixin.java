package cc.modlabs.moddetectionpreventer.mixin.antidetection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import cc.modlabs.moddetectionpreventer.text.CombinedFilter;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


/// Catches incoming anvil screens, filtering out components.
@Mixin(AnvilScreenHandler.class)
class AnvilScreenHandlerMixin {

    @WrapOperation(
            method = "updateResult",
            at     = @At(
                    value  = "INVOKE",
                    target = "Lnet/minecraft/text/Text;getString()Ljava/lang/String;"
            )
    )
    private String preventAnvilModDetection(Text instance, Operation<String> original) {
        return original.call(CombinedFilter.filterComponents(instance));
    }

}
