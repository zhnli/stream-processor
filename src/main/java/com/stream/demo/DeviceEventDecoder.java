package com.stream.demo;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * DeviceEventDecoder decodes JSON string to DeviceEvent object.
 */
public class DeviceEventDecoder extends MessageToMessageDecoder<HttpObject> {
    private static final Logger logger = LogManager.getLogger(DeviceEventDecoder.class);
    private static ObjectMapper objMapper = new ObjectMapper();

    @Override
    protected void decode(ChannelHandlerContext ctx, HttpObject in, List<Object> out) throws Exception {
        if (in instanceof HttpResponse) {
            logger.debug("HttpResponse received");
        }
        if (in instanceof HttpContent) {
            logger.debug("HttpContent received");
            HttpContent content = (HttpContent)in;
            String str = content.content().toString(CharsetUtil.UTF_8);

            int posStart = str.indexOf('{');
            if (posStart < 0) return;
            if (str.indexOf("busted dat") >= 0) return;
            int posEnd = str.indexOf('}');
            if (posEnd < 0) return;
            String jsonStr = str.substring(posStart, posEnd + 1);
            logger.debug("JSON string received: {}", jsonStr);
            DeviceEvent eventObj = null;
            
            try {
                eventObj = objMapper.readValue(jsonStr, DeviceEvent.class);
                logger.debug("DeviceEvent decoded: {}", eventObj);
                out.add(eventObj);
            } catch (Exception ex) {
                logger.error("Failed to decode DeviceEvent: {}", ex);
            }
        }
    }
}