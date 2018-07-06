/*
 * This file is part of ProDisFuzz, modified on 06.07.18 15:01.
 * Copyright (c) 2013-2018 Volker Nebelung <vnebelung@prodisfuzz.net>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package support;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class StreamSimulator {

    private Path fileInput;
    private Path fileOutput;
    private InputStream inputStream;
    private OutputStream outputStream;
    private int currentStartPosition = 0;

    public StreamSimulator() throws IOException {
        fileInput = Files.createTempFile(null, null);
        inputStream = new FileInputStream(fileInput.toFile());
        fileOutput = Files.createTempFile(null, null);
        outputStream = new FileOutputStream(fileOutput.toFile(), false);
    }

    public void exit() throws IOException {
        inputStream.close();
        outputStream.close();
        Files.delete(fileInput);
        Files.delete(fileOutput);
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void writeForInputStream(String s) throws IOException {
        Files.write(fileInput, s.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }

    public byte[] readLastFromOutputStream() throws IOException {
        byte[] content = Files.readAllBytes(fileOutput);
        byte[] result = Arrays.copyOfRange(content, currentStartPosition, content.length);
        currentStartPosition = content.length;
        return result;
    }
}
