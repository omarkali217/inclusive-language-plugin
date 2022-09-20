# inclusive-language-plugin

## About

The inclusive language plugin is meant to help software developers and colleagues adapt to inclusive language standards within the company. It includes a pre-populated list of terms, recommended replacements and reasoning behind the standard. Custom additions can be easily added to increase functionality.  

## Terms List

List is found in `src/main/resources/inclusive.json` and follow a simple array of JSON objects. These include a term to be replaced, the recommendations of replacements (which can be a single string or array of strings), and reason for the change.

```json
{
      "term": "Coworker",
      "recommendations": [
        "Colleague",
        "Contractor"
      ],
      "reason": "The preferred term for the fine folks who work at Amex is \"Colleague\" (and/or \"Contractor\", as appropriate).\n"
    }
```

### Adding New Terms

To add a new term, simply add another JSON object to the array in the above format. It would be recommended to include both singular and plural versions of the word (where applicable).

## Future Improvements

- [ ] upper/lower-case detection: currently the plugin is case-sensitive to the terms, meaning it will not detect a lower-case term if the term is defined in `inclusive.json` as uppercase and vice-versa.
- [ ] formatting for accessibility such various IDE color schemes, dark-mode, visual impairment accessibility, etc. 
- [ ] quick fix click from recommendations list. This would allow the user to hover the term and see the list of replacements from which they can simply click on the desired replacement.


## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "inclusive-language-plugin"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/omarkali217/inclusive-language-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


## Troubleshooting

- The cache of terms/tokens can persist even during rebuilds and may need to be deleted manually. To so, navigate to `build` > `idea-sandbox` > `config` > `options` > `HighlightTokenConfiguration.java` and delete the file.
---
Plugin based on the [IntelliJ Platform Plugin Template][template].

<!-- Plugin description -->
This Fancy IntelliJ Platform Plugin is going to be your implementation of the brilliant ideas that you have.
To add a new term, simply add another JSON object to the array in the above format. It would be recommended to include both singular and plural versions of the word (where applicable).

This specific section is a source for the [plugin.xml](/src/main/resources/META-INF/plugin.xml) file which will be extracted by the [Gradle](/build.gradle.kts) during the build process.
## Future Improvements

To keep everything working, do not remove `<!-- ... -->` sections.
<!-- Plugin description end -->

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
