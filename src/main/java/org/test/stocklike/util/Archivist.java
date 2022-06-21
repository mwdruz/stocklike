package org.test.stocklike.util;

import static io.vavr.API.Right;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.UnaryOperator;

import org.apache.commons.compress.compressors.lz4.FramedLZ4CompressorInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vavr.control.Either;

public class Archivist {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final UnaryOperator<String> fileNameMaker =
            s -> "archives/index" + s + ".html.lz4";
    
    private Archivist() { }
    
    private static Either<String, String> retrieveFile(String filename) throws IOException
    {
        LOGGER.info("Loading archive: {}", filename);
        InputStream fIn = Archivist.class.getClassLoader().getResourceAsStream(filename);
        if (fIn == null) {
            String msg = "Can't load resource: " + filename;
            LOGGER.error(msg);
            throw new FileNotFoundException(msg);
        }
        try (BufferedInputStream bIn = new BufferedInputStream(fIn);
             FramedLZ4CompressorInputStream zIn = new FramedLZ4CompressorInputStream(bIn)) {
            return Right(new String(zIn.readAllBytes(), StandardCharsets.UTF_8));
        } finally {
            fIn.close();
        }
    }
    
    public static Either<String, String> retrieveIndex() throws IOException
    {
        return retrieveIndex(0);
    }
    
    public static Either<String, String> retrieveIndex(int i) throws IOException
    {
        return retrieveFile(fileNameMaker.apply(String.valueOf(i)));
    }
}
