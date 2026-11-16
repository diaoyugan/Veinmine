package top.diaoyugan.veinmine.gametest;

import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

final class GuestProcess implements AutoCloseable {
    static final Path UPDATE = Path.of("update-preferences");
    static final Path FINISH = Path.of("finish-test");

    private final Process process;
    private final Path directory;

    private GuestProcess(Process process, Path directory) {
        this.process = process;
        this.directory = directory;
    }

    static GuestProcess start(int port) throws IOException {
        Path directory = Files.createTempDirectory(Path.of(".").toAbsolutePath(), "guest-");
        String executable = File.separatorChar == '\\' ? "java.exe" : "java";
        Path java = Path.of(System.getProperty("java.home"), "bin", executable);
        String classpath = Arrays.stream(System.getProperty("java.class.path").split(File.pathSeparator))
                .map(path -> Path.of(path).toAbsolutePath().toString())
                .collect(Collectors.joining(File.pathSeparator));
        String launchConfig = System.getProperty("veinmine.gametest.launchConfig");
        MiningRulesGameTest.check(launchConfig != null, "Loom did not provide the guest launch configuration");
        List<String> arguments = List.of(
                "-classpath", classpath, "-Xmx2G", "--enable-native-access=ALL-UNNAMED",
                "-Dfabric.client.gametest=true", "-Dveinmine.gametest.guestPort=" + port,
                "-Dfabric.dli.config=" + launchConfig, "-Dfabric.dli.env=client",
                "-Dfabric.dli.main=net.fabricmc.loader.impl.launch.knot.KnotClient",
                "net.fabricmc.devlaunchinjector.Main", "--username", "VeinMineGuest", "--width", "320", "--height", "240"
        );
        Path args = directory.resolve("launch.args");
        Files.write(args, arguments.stream().map(GuestProcess::quote).toList());
        Process process = new ProcessBuilder(java.toString(), "@" + args)
                .directory(directory.toFile())
                .redirectErrorStream(true)
                .redirectOutput(directory.resolve("guest.log").toFile())
                .start();
        return new GuestProcess(process, directory);
    }

    private static String quote(String argument) {
        return "\"" + argument.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    void requestUpdate() throws IOException {
        Files.createFile(directory.resolve(UPDATE));
    }

    void requireRunning() {
        if (!process.isAlive()) {
            throw new AssertionError("guest exited early with status " + process.exitValue()
                    + ", see " + directory.resolve("guest.log"));
        }
    }

    void finish(ClientGameTestContext context) throws IOException {
        Files.createFile(directory.resolve(FINISH));
        MiningRulesGameTest.await(context, "guest process to exit", () -> !process.isAlive());
        MiningRulesGameTest.check(process.exitValue() == 0, "guest failed, see " + directory.resolve("guest.log"));
    }

    @Override
    public void close() {
        if (process.isAlive()) process.destroyForcibly();
    }
}
