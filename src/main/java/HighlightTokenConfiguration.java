import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import Objects.QuarantineItem;
import Objects.QuarantineList;


import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@State(name = "HighlightTokenConfiguration", storages = @Storage("highlightTokenConfiguration-v2.xml"))
public class HighlightTokenConfiguration implements PersistentStateComponent<HighlightTokenConfiguration.State> {

    static final ObjectMapper mapper = new ObjectMapper();

    private static QuarantineList quarantinedTokens;

    private static Multimap<HighlightTokenType, QuarantineItem> INITIAL_TOKENS;

    static {
        try {
            byte[] data;
            try (InputStream in = HighlightTokenConfiguration.class.getClassLoader().getResourceAsStream("inclusive.json")) {
                assert in != null;
                data = in.readAllBytes();
                quarantinedTokens = mapper.readValue(data, QuarantineList.class);

                assert quarantinedTokens != null;
                INITIAL_TOKENS = new ImmutableMultimap.Builder<HighlightTokenType, QuarantineItem>()
                        .putAll(HighlightTokenType.COMMENT, Arrays.asList(quarantinedTokens.getQuarantineItems()))
                        .putAll(HighlightTokenType.METHOD_KEYWORD, Arrays.asList(quarantinedTokens.getQuarantineItems()))
                        .build();
            }
            var test = "asdf";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    State currentState;

    static class State {
        public Map<HighlightTokenType, Collection<QuarantineItem>> highlightTokenMap;
        public boolean plainTextFileHighlightEnabled;
    }

    public boolean isPlainTextFileHighlightEnabled() {
        return currentState.plainTextFileHighlightEnabled;
    }

    public void setPlainTextFileHighlightEnabled(boolean plainTextFileHighlightEnabled) {
        currentState.plainTextFileHighlightEnabled = plainTextFileHighlightEnabled;
    }

    public Collection<QuarantineItem> getAllTokensByType(Collection<HighlightTokenType> tokenTypes) {
        ImmutableSet.Builder<QuarantineItem> setBuilder = ImmutableSet.builder();
        for (HighlightTokenType tokenType : tokenTypes) {
            addTokenByType(tokenType, setBuilder);
        }
        return setBuilder.build();
    }

    public Collection<QuarantineItem> getAllTokensByType(HighlightTokenType type) {
        ImmutableSet.Builder<QuarantineItem> setBuilder = ImmutableSet.builder();
        addTokenByType(type, setBuilder);
        return setBuilder.build();
    }

    public Map<HighlightTokenType, Collection<QuarantineItem>> getAllTokens() {
        return Collections.unmodifiableMap(currentState.highlightTokenMap);
    }

    public void setCustomTokens(Map<HighlightTokenType, List<QuarantineItem>> updatedTokens) {
        for (Map.Entry<HighlightTokenType, List<QuarantineItem>> entry : updatedTokens.entrySet()) {

            // ! During token resolving in CommentHighlighter we resolve first acceptable token.
            // ! In case tokens are overlapping it would lead to unexpected behaviour.
            // ! We need to sort tokens in CommentHighlighter#containsHighlightToken to get the longest one to get rid of unexpected behaviour.
            // ! But such approach will lead to performance degradations.
            // ! Instead, we will sort tokens on save, so first acceptable token resolving will lead to desired behaviour.
            List<QuarantineItem> values = entry.getValue();
            values.stream().map(n -> n.getTerm()).collect(Collectors.toList()).sort(Comparator.comparingInt(String::length).reversed());

            currentState.highlightTokenMap.put(entry.getKey(), values);
        }
    }

    private void addTokenByType(HighlightTokenType type, ImmutableSet.Builder<QuarantineItem> setBuilder) {
        Collection<QuarantineItem> tokens = currentState.highlightTokenMap.get(type);
        if (tokens != null) {
            setBuilder.addAll(tokens);
        }
    }

    @Override
    public State getState() {
        return currentState;
    }

    @Override
    public void loadState(@NotNull State state) {
        currentState = state;
    }

    @Override
    @SuppressWarnings("java:S1640")
    public void noStateLoaded() {
        // ! We can't use EnumMap here, because Intellij IDEA can't serialize it.
        Map<HighlightTokenType, Collection<QuarantineItem>> initialHighlightMap = new HashMap<>();
        for (HighlightTokenType tokenType : HighlightTokenType.values()) {
            Collection<QuarantineItem> tokens = INITIAL_TOKENS.get(tokenType);
            if (tokens != null) {
                initialHighlightMap.put(tokenType, new ArrayList<>(tokens));
            } else {
                initialHighlightMap.put(tokenType, new ArrayList<>());
            }
        }

        currentState = new State();
        currentState.highlightTokenMap = initialHighlightMap;

        currentState.plainTextFileHighlightEnabled = false;
    }

    @Override
    public void initializeComponent() {
        if (currentState == null || currentState.highlightTokenMap == null) {
            noStateLoaded();
        }
    }
}

