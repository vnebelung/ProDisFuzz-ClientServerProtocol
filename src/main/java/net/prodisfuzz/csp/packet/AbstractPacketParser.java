/*
 * This file is part of ProDisFuzz, modified on 28.09.16 23:36.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package net.prodisfuzz.csp.packet;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * This class represents a not yet parsed packet that is received by a client or server.
 */
public abstract class AbstractPacketParser<V, C> {

    private InputStream inputStream;

    /**
     * Constructs a new packet parser with a given input stream.
     *
     * @param inputStream the input stream
     */
    public AbstractPacketParser(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Reads an incoming message from a server or client input stream and tries to parse the packet in respect to
     * the given allowed protocol states and returns it. A
     * valid packet structure is as follows: "CCC L… B…", CCC = three character command, L… = length of body, B… =
     * body with length L…
     *
     * @param allowedStates        the allowed protocol states
     * @param commandClass         the class of the commands
     * @param incomingMessageClass the class of the incoming message
     * @return the parsed incoming message
     * @throws IOException             if an I/O error occurs
     * @throws ProtocolFormatException if the received package is not of the format "CCC L… B…", CCC = three character
     *                                 command, L… = length of body, B… = body with length L…
     */
    protected V readIncomingMessage(Class<V> incomingMessageClass, Class<C> commandClass,
                                    Map<String, C> allowedStates) throws IOException, ProtocolFormatException {
        try {
            C command = readCommand(allowedStates);
            int length = readLength();
            byte[] body = readBody(length);
            return incomingMessageClass.getConstructor(commandClass, body.getClass()).newInstance(command, body);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException
                ignored) {
        } catch (ProtocolFormatException e) {
            clearInputStream();
            throw e;
        }
        // Should not happen
        return null;
    }

    /**
     * Reads the packet's three characters command and a preceding space character "CCC " from an input data
     * stream. Packet structure: "CCC L… B…", CCC = three character command, L… = length of body, B… = body with
     * length L…
     *
     * @param allowedCommands the allowed commands
     * @return the packet's command
     * @throws ProtocolFormatException if the next received four bytes are not a valid command "CCC "
     * @throws IOException             if an I/O error occurs
     */
    private C readCommand(Map<String, C> allowedCommands) throws IOException, ProtocolFormatException {
        byte[] commandBuffer = new byte[4];
        //noinspection ResultOfMethodCallIgnored
        if (inputStream.read(commandBuffer) != 4) {
            throw new ProtocolFormatException("Received command has not the structure 'CCC '");
        }
        String command = new String(commandBuffer, 0, 3, StandardCharsets.UTF_8);
        if (!allowedCommands.containsKey(command)) {
            throw new ProtocolFormatException("Received command '" + command + "' is invalid");
        }
        if (commandBuffer[3] != ' ') {
            throw new ProtocolFormatException("Received command has not the structure 'CCC '");
        }
        return allowedCommands.get(command);
    }

    /**
     * Reads the length of the packet's body and a preceding space character "L… " from an input stream. Packet
     * structure: "CCC L… B…", CCC = three character command, L… = length of body, B… = body with length L…
     *
     * @return the length of the packet's body
     * @throws ProtocolFormatException if the next received bytes are not a valid length "L… "
     * @throws IOException             if an I/O error occurs
     */
    private int readLength() throws IOException, ProtocolFormatException {
        StringBuilder lengthBuffer = new StringBuilder();
        while (true) {
            int digit = inputStream.read();
            if (digit == -1) {
                throw new ProtocolFormatException("Received length has not the structure 'L… '");
            }
            char c = (char) digit;
            if (c == ' ') {
                break;
            }
            lengthBuffer.append(c);
        }
        if (lengthBuffer.length() < 1) {
            throw new ProtocolFormatException("Received length has not the structure 'L… '");
        }
        for (int i = 0; i < lengthBuffer.length(); i++) {
            if (lengthBuffer.charAt(i) < '0' || lengthBuffer.charAt(i) > '9') {
                throw new ProtocolFormatException("Received length is invalid");
            }
        }
        return Integer.parseInt(lengthBuffer.toString());
    }

    /**
     * Reads the packet's body from an input stream with the given length. Packet structure: "CCC L… B…", CCC = three
     * character command,
     * L… = length of body, B… = body with length L…
     *
     * @param length the length of the body
     * @return the packet's body
     * @throws ProtocolFormatException if the next received bytes are not of the given length
     * @throws IOException             if an I/O error occurs
     */
    private byte[] readBody(int length) throws IOException, ProtocolFormatException {
        byte[] result = new byte[length];
        if (inputStream.read(result) != length) {
            throw new ProtocolFormatException("Received body has not the defined length");
        }
        return result;
    }

    /**
     * Clears the given input stream by reading all available bytes, leading to an empty stream
     *
     * @throws IOException if an I/O error occurs
     */
    private void clearInputStream() throws IOException {
        //noinspection StatementWithEmptyBody
        while (inputStream.read() != -1) {
        }
    }
}
