/*
 * This file is part of ProDisFuzz, modified on 9/5/16 12:42 AM.
 * Copyright (c) 2013-2016 Volker Nebelung <vnebelung@prodisfuzz.net>
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

public class StreamSimulator {

    private Path fileInput;
    private Path fileOutput;

    public void init() throws IOException {
        fileInput = Files.createTempFile("a_", null);
        fileOutput = Files.createTempFile("a_", null);
    }

    public void exit() throws IOException {
        Files.delete(fileInput);
        Files.delete(fileOutput);
    }

    public DataInputStream getDataInputStream() throws FileNotFoundException {
        return new DataInputStream(new FileInputStream(fileInput.toFile()));
    }

    public DataOutputStream getDataOutputStream() throws FileNotFoundException {
        return new DataOutputStream(new FileOutputStream(fileOutput.toFile()));
    }

    public void writeForInputStream(String s) throws IOException {
        Files.write(fileInput, s.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }

    public byte[] readLastFromOutputStream() throws IOException {
        return Files.readAllBytes(fileOutput);
    }
}
