package me.wolfii.moddetectionpreventer.mixin.antidetection;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.wolfii.moddetectionpreventer.text.CombinedFilter;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;
import java.util.stream.Stream;


/// Catches incoming sign screens, filtering out components.
@Mixin(AbstractSignEditScreen.class)
class AbstractSignEditScreenMixin {

    @WrapOperation(
            method = "<init>(Lnet/minecraft/block/entity/SignBlockEntity;ZZLnet/minecraft/text/Text;)V",
            at     = @At(
                    value  = "INVOKE",
                    target = "Ljava/util/stream/Stream;map(Ljava/util/function/Function;)Ljava/util/stream/Stream;"
            )
    )
    private Stream<String> preventSignModDetection(Stream<Text> instance, Function<Text, String> function, Operation<Stream<String>> original) {
        return original.call(
                instance.map(CombinedFilter::filterComponents),
                function
        );
    }
}
