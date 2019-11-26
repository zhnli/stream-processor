package com.stream.demo;
 
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * DeviceEventHandler
 *   - Receives device events and forwards them to event manager.
 *   - Periodically get aggregated events from event manager. 
 */
public class DeviceEventHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LogManager.getLogger(DeviceEventHandler.class);
    private static ObjectMapper objMapper = new ObjectMapper();
    private ScheduledFuture sf;
    private DeviceEventManager eventManager;

    public DeviceEventHandler(DeviceEventManager em) {
        this.eventManager = em;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("Connection established");
        sf = ctx.executor().scheduleAtFixedRate(() -> {
            logger.info("Trigger aggregated device event reporting");
            reportAggregatedEvents();
        }, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("Connection closed by the peer");
        sf.cancel(false);
    }

    private void reportAggregatedEvents() {
        List<AggregatedEvent> res = this.eventManager.eventReport();
        logger.info("Aggregated device event reported: Total={}", res.size());

        for (AggregatedEvent e : res) {
            try {
                System.out.println(objMapper.writeValueAsString(e));
            } catch (Exception ex) {
                logger.error("Failed to write aggregated device event");
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            DeviceEvent event = (DeviceEvent) msg;
            logger.debug("DeviceEvent received");
            this.eventManager.eventReceived(event);
        } catch (Exception ex) {
            logger.error("Invalid DeviceEvent: {}", ex);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
