package no.cantara.ratpack.websockets;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import ratpack.api.Nullable;
import ratpack.exec.ExecController;
import ratpack.exec.Execution;
import ratpack.exec.ExecutionRef;
import ratpack.exec.internal.DefaultExecController;
import ratpack.exec.internal.DefaultExecution;
import ratpack.exec.internal.ExecControllerInternal;
import ratpack.exec.internal.ExecThreadBinding;
import ratpack.func.Action;
import ratpack.registry.RegistrySpec;
import ratpack.websocket.WebSocketHandler;
import ratpack.websocket.WebSocketMessage;

public interface RatpackManagedWebSocketHandler<T> extends WebSocketHandler<T> {

    /**
     * Override this method where you normally would override onMessage in order to get a ratpack-managed execution.
     * This is a workaround to the fact calls to Blocking.get() from onMessage() throws an UnmanagedThreadException.
     *
     * @param frame
     * @throws Exception
     */
    void onManagedMessage(WebSocketMessage<T> frame) throws Exception;

    @Override
    default void onMessage(WebSocketMessage<T> frame) throws Exception {
        ExecThreadBinding.requireComputeThread("WebSocketHandler requires processing by a ratpack-compute thread.");
        ExecThreadBinding threadBinding = ExecThreadBinding.get().get();
        ExecController execController = threadBinding.getExecController();
        if (!(execController instanceof DefaultExecController)) {
            throw new IllegalStateException("Could not get a DefaultExecController from ThreadBinding.get().get()");
        }
        DefaultExecController defaultExecController = (DefaultExecController) execController;
        EventLoopGroup eventLoopGroup = defaultExecController.getEventLoopGroup();
        EventLoop next = eventLoopGroup.next();
        new DefaultExecution(
                defaultExecController,null,
                next,
                Action.noop(),
                execution -> {
                    onManagedMessage(frame);
                },
                t -> DefaultExecution.LOGGER.error("Uncaught execution exception", t),
                Action.noop(),
                Action.noop());
    }
}

