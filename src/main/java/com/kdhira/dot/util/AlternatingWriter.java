package com.kdhira.dot.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.kdhira.dot.util.ColoredString.StringColor;

public class AlternatingWriter {

    Map<InputStream, Consumer<String>> streams;

    public AlternatingWriter() {
        streams = new HashMap<InputStream, Consumer<String>>();
    }

    public void addStream(InputStream stream, Consumer<ColoredString> relay, StringColor color) {
        streams.put(stream, new Consumer<String>() {

            @Override
            public void accept(String t) {
                relay.accept(new Function<String, ColoredString>() {

                    @Override
                    public ColoredString apply(String s) {
                        return new ColoredString(s, color);
                    }

                }.apply(t));
            }
        });
    }

    public void addStream(InputStream stream, Consumer<ColoredString> relay) {
        addStream(stream, relay, StringColor.NONE);
    }

    public void relayWhile(Supplier<Boolean> condition) throws IOException {
        Map<InputStream, StringBuilder> builders = new HashMap<InputStream, StringBuilder>();
        while (condition.get() || anyAvailable()) {
            for (Entry<InputStream, Consumer<String>> streamRelayPair : streams.entrySet()) {
                InputStream stream = streamRelayPair.getKey();
                Consumer<String> relay = streamRelayPair.getValue();

                if (!builders.containsKey(stream)) {
                    builders.put(stream, new StringBuilder());
                }

                StringBuilder builder = builders.get(stream);

                while(stream.available() > 0) {
                    int i = stream.read();
                    if (i < 0) {
                        break;
                    }
                    if (i == '\n') {
                        relay.accept(builder.toString());
                        builder.setLength(0);
                    }
                    else {
                        builder.append((char) i);
                    }
                }

            }
        }
    }

    private boolean anyAvailable() throws IOException {
        for (InputStream stream : streams.keySet()) {
            if (stream.available() > 0) {
                return true;
            }
        }
        return false;
    }

}
