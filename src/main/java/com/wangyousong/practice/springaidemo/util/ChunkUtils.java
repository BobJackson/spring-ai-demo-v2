package com.wangyousong.practice.springaidemo.util;

import lombok.experimental.UtilityClass;
import reactor.core.publisher.Flux;

@UtilityClass
public class ChunkUtils {
    public static Flux<String> toJsonChunk(Flux<String> tokenFlux) {
        return Flux.create(sink -> {
            StringBuilder buffer = new StringBuilder();
            tokenFlux.subscribe(
                    token -> {
                        buffer.append(token);
                        int idx;
                        if ((idx = buffer.indexOf("\n")) >= 0) {
                            String line = buffer.substring(0, idx);
                            sink.next(line);
                            buffer.delete(0, idx + 1);
                        }
                    },
                    sink::error,
                    () -> {
                        if (!buffer.isEmpty()) {
                            sink.next(buffer.toString());
                        }
                        sink.complete();
                    }
            );
        });
    }
}
