package org.test.stocklike.data.repository.parser;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

import org.apache.logging.log4j.LogManager;
import org.jsoup.nodes.Document;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;

abstract class ParserAbstract<T> {
    private static final String DATA_BOX_ID_ATTRIBUTE = "data-box-id";
    private static final String DATA_SERIALIZE_BOX_ATTRIBUTE = "data-serialize-box-id";
    private static final UnaryOperator<String> dataBoxSelectorIdMaker =
            name -> "div[data-box-name=" + name + "]";
    private static final UnaryOperator<String> dataBoxSelectorMaker =
            dataBoxId -> String.format("script[%s=%s]", DATA_SERIALIZE_BOX_ATTRIBUTE, dataBoxId);
    
    protected abstract T process(Document doc) throws ParsingException, IOException;
    
    Either<String, T> parse(Document doc) { return handleExceptions(doc); }
    
    Either<String, T> handleExceptions(Document doc)
    {
        try {
            return Right(process(doc));
        } catch (IOException
                 | ParsingException e) {
            String errorMessage = String.format("%s(\"%s\")",
                                                e.getClass().getSimpleName(),
                                                e.getMessage());
            LogManager.getLogger(this).error(errorMessage);
            dumpDocument(doc);
            return Left(errorMessage);
        }
    }
    
    protected void dumpDocument(Document doc)
    {
        String fileName = "dump" + this.getClass().getSimpleName() + ".html";
        LogManager.getLogger(this).info("Dumping offending document to file: {}", fileName);
        try {
            Files.writeString(Paths.get(fileName), doc.html(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LogManager.getLogger(this).error("Additionally, error while trying to write file...");
            LogManager.getLogger(this).error(e);
        }
    }
    
    private void dumpJson(JsonNode node)
    {
        IntFunction<String> fileNamer = counter ->
                "dump" + this.getClass().getSimpleName() + counter + ".json";
        int i = 0;
        while (new File(fileNamer.apply(i)).isFile()) i++;
        String fileName = fileNamer.apply(i);
        ObjectMapper mapper = new ObjectMapper();
        try (var stream = new FileOutputStream(fileName, false)) {
            LogManager.getLogger(this).info("Dumping json to file: {}", fileName);
            final var result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
            stream.write(result.getBytes());
        } catch (IOException e) {
            LogManager.getLogger(this).error("Additionally, error while trying to write file...");
            LogManager.getLogger(this).error(e);
        }
    }
    
    String extractDataBox(Document doc, String boxName) throws ParsingException
    {
        String dataBoxIdSelector = dataBoxSelectorIdMaker.apply(boxName);
        String dataBoxId = doc.select(dataBoxIdSelector).attr(DATA_BOX_ID_ATTRIBUTE);
        if (dataBoxId.isEmpty())
            throw new ParsingException("Can't extract " + DATA_BOX_ID_ATTRIBUTE);
        String dataBox = doc.select(dataBoxSelectorMaker.apply(dataBoxId)).html();
        if (dataBox.isEmpty())
            throw new ParsingException("Can't extract " + DATA_SERIALIZE_BOX_ATTRIBUTE);
        return dataBox;
    }
}
