import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Service
final public class CommentHighlighter implements TokenHighlighter {

    private static final HighlightTokenConfiguration tokenConfiguration = ApplicationManager.getApplication().getService(HighlightTokenConfiguration.class);

    private static final String DOC_COMMENT_START_LINE = "/**";
    private static final List<Character> START_LINE_CHARACTERS_LIST = Arrays.asList('/', '<', '-', ' ', '#', '*', '!', '\t', '{');

    @Override
    public List<Pair<TextRange, TextAttributesKey>> getHighlights(String text, int startOffset) {

        Collection<String> supportedTokens = tokenConfiguration.getAllTokensByType(getSupportedTokenTypes());

        // General comment data
        final int commentLength = text.length();
        final int lastCharPosition = text.length() - 1;
        final boolean isDocComment = text.startsWith(DOC_COMMENT_START_LINE);

        // Variables to process current line highlighting
        // ? Move into separate DTO object? Will it decrease performance?
        int currentLineStartIndex = startOffset;
        boolean isProcessedCurrentLine = false;
        boolean isHighlightedCurrentLine = false;
        boolean isSkippedFirstStarCharInDocComment = false;
        TextAttributesKey currentLineHighlightAttribute = null;

        // Result list of pairs from which annotation would be created
        List<Pair<TextRange, TextAttributesKey>> highlightAnnotationData = new ArrayList<>();

        // Code is a little bit crappy, but has better performance
        for (int i = 0; i < commentLength; i++) {
            char c = text.charAt(i);
            // Reset attributes and create highlight on line end
            if (c == '\n' || i == lastCharPosition) {
                if (isHighlightedCurrentLine) {
                    TextRange textRange = new TextRange(currentLineStartIndex, startOffset + i + 1);
                    highlightAnnotationData.add(Pair.create(textRange, currentLineHighlightAttribute));
                }
                currentLineStartIndex = startOffset + i + 1;
                isHighlightedCurrentLine = false;
                isProcessedCurrentLine = false;
                isSkippedFirstStarCharInDocComment = false;
                continue;
            }

            // Skip processing of current char in line if highlight was already defined
            if (isProcessedCurrentLine) {
                continue;
            }

            // Skip processing of first "*" in doc comments
            if (!isSkippedFirstStarCharInDocComment && shouldSkipFistStarInDocComment(c, isDocComment)) {
                isSkippedFirstStarCharInDocComment = true;
                continue;
            }

            // Create highlight if current char is valid highlight char
            if (isValidPosition(text, i) && isHighlightTriggerChar(c, supportedTokens) && containsHighlightToken(text.substring(i), supportedTokens)) {
                isHighlightedCurrentLine = true;
                isProcessedCurrentLine = true;
                currentLineHighlightAttribute = getHighlightTextAttribute(text.substring(i), supportedTokens);
                var test = "123";
            }

            // Check that line highlight was defined and no more processing needs
            if (!isValidStartLineChar(c)) {
                isProcessedCurrentLine = true;
            }
        }
        return highlightAnnotationData;
    }

    private boolean shouldSkipFistStarInDocComment(char c, boolean isDocComment) {
        return isDocComment && c == '*';
    }


    private boolean isValidPosition(String comment, int i) {
        char c = comment.charAt(i);
        // Length and i checks is used to not fall in StringIndexOutOfBoundsException
        if (i > 0) {
            int length = comment.length();
            if (c == '!' && length >= 3) {
                // Skip "<!-" and shebang "#!/"
                return !((comment.charAt(i - 1) == '<' && comment.charAt(i + 1) == '-') || (comment.charAt(i - 1) == '#' && comment.charAt(i + 1) == '/'));
            } else if (c == '*') {

                // Skip "/*", "*/", "/**"
                return !(comment.charAt(i - 1) == '/' || (i >= 2 && comment.charAt(i - 2) == '/' && comment.charAt(i - 1) == '*') || ((i + 1) < length && comment.charAt(i + 1) == '/'));
            }
        }

        return true;
    }

    private boolean isValidStartLineChar(char c) {
        return START_LINE_CHARACTERS_LIST.contains(c);
    }

    private boolean isHighlightTriggerChar(char c, Collection<String> supportedTokens) {
        for (String token : supportedTokens) {
            if (token.charAt(0) == c) {
                return true;
            }
        }

        return false;
    }

    private boolean containsHighlightToken(String commentSubstring, Collection<String> supportedTokens) {
        // ! We resolve first acceptable token. Tokens are received from saved configuration, so they are
        // ! always sorted by length desc.
        // ! In such case it allows to resolve longest token in case there is overlapping tokens.

        // ! For example, we have tokens ">", ">>", ">>>" and comment line "// >>>".
        // ! In such case we want to resolve token ">>>", not ">".

        for (String token : supportedTokens) {
            if (commentSubstring.startsWith(token)) {
                return true;
            }
        }
        return false;
    }


    private TextAttributesKey getHighlightTextAttribute(String commentSubstring, Collection<String> supportedTokens) {
        for (String token : supportedTokens) {
            if (commentSubstring.startsWith(token)) {
                return TextAttributesKey.createTextAttributesKey(getTextAttributeKeyByToken(token));
            }
        }
        return null;
    }

    @NotNull
    @Override
    public String getTextAttributeKeyByToken(String token) {
        return token + "_COMMENT";
    }

    @Override
    public List<HighlightTokenType> getSupportedTokenTypes() {
        return Collections.singletonList(HighlightTokenType.COMMENT);
    }
}
