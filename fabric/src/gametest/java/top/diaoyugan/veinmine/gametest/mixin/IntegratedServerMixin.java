package top.diaoyugan.veinmine.gametest.mixin;

import net.minecraft.client.server.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.net.InetAddress;

@Mixin(IntegratedServer.class)
abstract class IntegratedServerMixin {
    @ModifyArg(
            method = "publishServer(Lnet/minecraft/server/MinecraftServer$MultiplayerScope;I)Z",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerConnectionListener;startTcpServerListener(Ljava/net/InetAddress;I)V"),
            index = 0
    )
    private InetAddress bindTestLanToLoopback(InetAddress address) {
        return InetAddress.ofLiteral("127.0.0.1");
    }
}
