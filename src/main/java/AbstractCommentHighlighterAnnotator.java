import Objects.QuarantineItem;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractCommentHighlighterAnnotator implements Annotator {

    private static final CommentHighlighter commentHighlighter = ApplicationManager.getApplication().getService(CommentHighlighter.class);

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (isCommentHighlightingElement(element)) {
            final String comment = extractCommentTextFromElement(element);
            int startOffset = element.getTextRange().getStartOffset();

            List<Pair<TextRange, TextAttributesKey>> highlights = commentHighlighter.getHighlights(comment, startOffset);

            for (Pair<TextRange, TextAttributesKey> highlight : highlights) {
                holder.newAnnotation(HighlightSeverity.WARNING, "non-inclusive language detected: " + commentHighlighter.getReason())
                        .range(highlight.first)
                        .textAttributes(highlight.second)
                        .create();
            }
        }
    }

    protected String extractCommentTextFromElement(@NotNull PsiElement element) {
        return element.getText();
    }

    protected abstract boolean isCommentHighlightingElement(@NotNull PsiElement element);

}
