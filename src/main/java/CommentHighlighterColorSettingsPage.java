//import com.intellij.openapi.application.ApplicationManager;
//import com.intellij.openapi.editor.colors.TextAttributesKey;
//import com.intellij.openapi.fileTypes.PlainSyntaxHighlighter;
//import com.intellij.openapi.fileTypes.SyntaxHighlighter;
//import com.intellij.openapi.options.colors.AttributesDescriptor;
//import com.intellij.openapi.options.colors.ColorDescriptor;
//import com.intellij.openapi.options.colors.ColorSettingsPage;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import javax.swing.*;
//import java.util.Map;
//import java.util.stream.Stream;
//
//public class CommentHighlighterColorSettingsPage implements ColorSettingsPage {
//
//    // ? Is it possible to get list of services by Interface?
//    private static final CommentHighlighter commentHighlighter = ApplicationManager.getApplication().getService(CommentHighlighter.class);
//    private static final KeywordHighlighter keywordHighlighter = ApplicationManager.getApplication().getService(KeywordHighlighter.class);
//
//    private static final HighlightTokenConfiguration tokenConfiguration = ApplicationManager.getApplication().getService(HighlightTokenConfiguration.class);
//
//    @Nullable
//    @Override
//    public Icon getIcon() {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public SyntaxHighlighter getHighlighter() {
//        return new PlainSyntaxHighlighter();
//    }
//
//    @NotNull
//    @Override
//    public String getDemoText() {
//        return " ";
//    }
//
//    @Nullable
//    @Override
//    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public AttributesDescriptor[] getAttributeDescriptors() {
//        Stream<AttributesDescriptor> commentColorStream = tokenConfiguration.getAllTokensByType(commentHighlighter.getSupportedTokenTypes()).stream()
//                .map(token -> createAttributeDescriptor(token.getTerm(), commentHighlighter));
//
//        Stream<AttributesDescriptor> keywordColorStream = tokenConfiguration.getAllTokensByType(keywordHighlighter.getSupportedTokenTypes()).stream()
//                .map(token -> createAttributeDescriptor(token.getTerm(), keywordHighlighter));
//
//        return Stream.concat(commentColorStream, keywordColorStream).toArray(AttributesDescriptor[]::new);
//    }
//
//    @NotNull
//    private AttributesDescriptor createAttributeDescriptor(String token, TokenHighlighter tokenHighlighter) {
//
//        if (tokenHighlighter instanceof CommentHighlighter) {
//            return new AttributesDescriptor("Comment//" + token, createTextAttributeKey(token, tokenHighlighter));
//        } else if (tokenHighlighter instanceof KeywordHighlighter) {
//            return new AttributesDescriptor("Keyword//" + token, createTextAttributeKey(token, tokenHighlighter));
//        }
//
//        return new AttributesDescriptor("Other//" + token, createTextAttributeKey(token, tokenHighlighter));
//    }
//
////    @NotNull
////    private TextAttributesKey createTextAttributeKey(String token, TokenHighlighter tokenHighlighter) {
////        return TextAttributesKey.createTextAttributesKey(tokenHighlighter.getTextAttributeKeyByToken(token));
////    }
//
//    @NotNull
//    @Override
//    public ColorDescriptor[] getColorDescriptors() {
//        return ColorDescriptor.EMPTY_ARRAY;
//    }
//
//    @NotNull
//    @Override
//    public String getDisplayName() {
//        return "Comments/Keywords";
//    }
//}
