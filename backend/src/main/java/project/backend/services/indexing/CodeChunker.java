package project.backend.services.indexing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import project.backend.services.ai.RagSettings;

@Service 
public class CodeChunker 
{
    private final TokenTextSplitter splitter;
    private final CodeFileFilter fileFilter;

    public CodeChunker (@Value("${app.indexing.chunk-size:800}") int chunkSize, CodeFileFilter fileFilter)
    {
        // Spring AI splits by tokens; ~4 characters per token is a reasonable default for code.
        int chunkTokens = Math.max(50, chunkSize / 4);

        this.splitter = TokenTextSplitter.builder()
                .withChunkSize(chunkTokens)
                .build();
        this.fileFilter = fileFilter;
    }

    public List<Document> chunkFile(String repoId, String filePath, String content) {
        if (content == null || content.isBlank()) {
            return List.of();
        }

        String language = fileFilter.detectLanguage(filePath);
        String header = "// File: " + filePath + "\n";

        Document source = new Document(header + content, baseMetadata(repoId, filePath, language));
        List<Document> split = splitter.apply(List.of(source));

        String fullText = source.getText();
        int[] cursor = { 0 };
        return IntStream.range(0, split.size())
                .mapToObj(i -> withChunkIndex(split.get(i), repoId, filePath, language, i,
                        locateLines(fullText, split.get(i).getText(), cursor)))
                .toList();
    }

    /**
     * Finds the chunk in the source text and returns its 1-based {start, end} line range
     * relative to the original file (the injected header line is not counted).
     * Returns null if the chunk text can't be located (e.g. the splitter altered it).
     */
    private static int[] locateLines(String fullText, String chunkText, int[] cursor) {
        if (chunkText == null || chunkText.isBlank()) {
            return null;
        }
        String needle = chunkText.strip();
        int start = fullText.indexOf(needle, cursor[0]);
        if (start < 0) {
            start = fullText.indexOf(needle);
        }
        if (start < 0) {
            return null;
        }
        int end = start + needle.length();
        cursor[0] = end;
        return new int[] { lineAt(fullText, start), lineAt(fullText, Math.max(start, end - 1)) };
    }

    /** 1-based line number of the character at {@code index}, excluding the one-line header. */
    private static int lineAt(String text, int index) {
        int line = 1;
        for (int i = 0; i < index; i++) {
            if (text.charAt(i) == '\n') {
                line++;
            }
        }
        return Math.max(1, line - 1);
    }

    private static Map<String, Object> baseMetadata(String repoId, String filePath, String language) 
    {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(RagSettings.METADATA_REPO_ID, repoId);
        metadata.put("filePath", filePath);
        metadata.put("language", language);
        return metadata;
    }

    private static Document withChunkIndex(
            Document chunk,
            String repoId,
            String filePath,
            String language,
            int chunkIndex,
            int[] lines) 
    {
        Map<String, Object> metadata = new HashMap<>(chunk.getMetadata());
        metadata.put(RagSettings.METADATA_REPO_ID, repoId);
        metadata.put("filePath", filePath);
        metadata.put("language", language);
        metadata.put("chunkIndex", chunkIndex);
        if (lines != null) {
            metadata.put("startLine", lines[0]);
            metadata.put("endLine", lines[1]);
        }
        
        return new Document(chunk.getText(), metadata);
    }
}
