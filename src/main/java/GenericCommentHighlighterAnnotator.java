import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPlainText;
import org.jetbrains.annotations.NotNull;

public class GenericCommentHighlighterAnnotator extends AbstractCommentHighlighterAnnotator {

    private static final HighlightTokenConfiguration tokenConfiguration = ApplicationManager.getApplication().getService(HighlightTokenConfiguration.class);

    @Override
    protected boolean isCommentHighlightingElement(@NotNull PsiElement element) {
        return isCommentType(element) || isPlainTextHighlight(element);
    }

    private boolean isPlainTextHighlight(@NotNull PsiElement element) {
        return element instanceof PsiPlainText && tokenConfiguration.isPlainTextFileHighlightEnabled();
    }

    private boolean isCommentType(@NotNull PsiElement element) {
        return element instanceof PsiComment;
    }

}
