# Use https://github.com/NikOverflow/ExploitPreventer

## The Vulnerability

Minecraft has a feature that allows text (in chat, on signs, or in the bossbar) to be specified by a keybind the user
has set, or a translation key. The Client will then replace the translation key, or the keybind with the stored value.
This can be abused by the server by serving the client a sign with such a placeholder (for example Sodium:
`sodium.option_impact.low`). By immediately closing the sign screen, the client sends the edited text to the server
without ever seeing a sign open screen. The server can then detect wether you have that specific mod installed, by
checking if your client replaced the placeholder with the corresponding text (`sodium.option_impact.low -> Low`). If
you don't have Sodium installed, the placeholder will stay there
(`sodium.option_impact.low -> sodium.option_impact.low`).

This also works on the Anvil screen. The server could prompt you to open the anvil screen, with an item in the
renaming slot that has a translation key as it's name. The client would then rename the item to the corresponding
value and send an update to the server. (Huge thanks to Frog, `@croaak` on discord, for figuring this out)

This detection method works for any mod that has custom translations.

## The Fix

This mod fixes this issue by simply not resolving any translation or keybind placeholders on signs, except vanilla
ones. This makes it impossible for the server to use this method to detect installed mods.

To verify this works you can test it in a [test world](https://github.com/ModLabsCC/ModDetectionPreventer/raw/main/testWorld.zip).

## Intentions

~~While this feature can be used to prevent harm by detecting cheaters early, it is implemented improperly on some
servers, including [Cytooxien](CytooxienDetectedMods.md). Immediately banning players upon joining, simply because they
have tweakeroo installed, is unacceptable.~~
After a discussion with the developer of Cytooxien, they told me that players won't get banned for using tweakeroo, only kicked repeatedly.

# Warning: Use At Your Own Risk

## Important Security Notice

This modification alters Minecraft's network communication patterns by preventing the resolution of non-vanilla translation keys and keybinds. While this functionality protects your privacy by preventing servers from detecting installed mods, it constitutes a form of packet manipulation that violates the rules on many Minecraft servers.

**Please be advised:**
- Using this mod may result in temporary or permanent account suspension on multiplayer servers
- Server administrators can implement alternative detection methods
- The developers of this modification cannot be held responsible for any penalties incurred through its use

This software is provided "as is" without warranty of any kind, and you assume full responsibility for any consequences resulting from its use.
If you choose to proceed, understand that you do so entirely at your own discretion.
