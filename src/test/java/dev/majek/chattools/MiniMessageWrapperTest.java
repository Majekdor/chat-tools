/*
 * This file is part of chat-tools, licensed under the MIT License.
 *
 * Copyright (c) 2020-2022 Majekdor
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.majek.chattools;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.placeholder.Placeholder;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;
import org.junit.Assert;
import org.junit.Test;

public class MiniMessageWrapperTest {

  @Test
  public void gradients() {
    String gradient = "<gradient:#1eae98:#d8b5ff>Majekdor</gradient>";
    Assert.assertEquals(
        "Majekdor",
        MiniMessageWrapper.builder().gradients(false).build().mmString(gradient)
    );
  }

  @Test
  public void hexColors() {
    String hex = "<#1eae98>Majek<color:#d8b5ff>dor";
    Assert.assertEquals(
        "<#1eae98>Majek<color:#d8b5ff>dor",
        MiniMessageWrapper.builder().gradients(false).build().mmString(hex)
    );
    Assert.assertEquals(
        "Majekdor",
        MiniMessageWrapper.builder().hexColors(false).build().mmString(hex)
    );
  }

  @Test
  public void standardColors() {
    String color = "<blue>Majek<light_purple>dor";
    Assert.assertEquals(
        "<blue>Majek<light_purple>dor",
        MiniMessageWrapper.builder().hexColors(false).build().mmString(color)
    );
    Assert.assertEquals(
        "Majekdor",
        MiniMessageWrapper.builder().standardColors(false).build().mmString(color)
    );
  }

  @Test
  public void legacyColors() {
    String legacy = "&9&lMajek&b&odor&x&f&a&c&a&d&e!";
    Assert.assertEquals(
        "<blue><bold>Majek<aqua><italic>dor<#facade>!",
        MiniMessageWrapper.legacy().mmString(legacy)
    );
    Assert.assertEquals(
        "Majekdor!",
        MiniMessageWrapper.standard().mmString(legacy)
    );
  }

  @Test
  public void escapedLegacy() {
    String string = "&bMajekdor with this color code \\&b";
    Assert.assertEquals(
        "<aqua>Majekdor with this color code &b",
        MiniMessageWrapper.legacy().mmString(string)
    );
  }

  @Test
  public void legacyHexColors() {
    String legacyHex = "&#336633Majek<blue>dor&a!";
    Assert.assertEquals(
        "<#336633>Majekdor!",
        MiniMessageWrapper.builder().standardColors(false).legacyColors(true).build().mmString(legacyHex)
    );
    Assert.assertEquals(
        "&#336633Majek<blue>dor!",
        MiniMessageWrapper.standard().toBuilder().build().mmString(legacyHex)
    );
    Assert.assertEquals(
        "Majek<blue>dor<green>!",
        MiniMessageWrapper.builder().legacyColors(true).hexColors(false).build().mmString(legacyHex)
    );
  }

  @Test
  public void threeCharHex() {
    String threeCharHex = "&#363Majek<blue>dor";
    Assert.assertEquals(
        "<#336633>Majekdor",
        MiniMessageWrapper.builder().legacyColors(true).standardColors(false).build().mmString(threeCharHex)
    );
  }

  @Test
  public void everything() {
    String everything = "<gradient:#1eae98:#d8b5ff>Majek</gradient><aqua>dor<#336633>!";
    Assert.assertEquals(
        "Majek<aqua>dor<#336633>!",
        MiniMessageWrapper.builder().gradients(false).build().mmString(everything)
    );
    Assert.assertEquals(
        "<gradient:#1eae98:#d8b5ff>Majek</gradient><aqua>dor!",
        MiniMessageWrapper.builder().hexColors(false).build().mmString(everything)
    );
    Assert.assertEquals(
        "Majekdor!",
        MiniMessageWrapper.builder().gradients(false).hexColors(false)
            .standardColors(false).advancedTransformations(false).build().mmString(everything)
    );
    Assert.assertEquals(
        Component.text("Majekdor!"),
        MiniMessageWrapper.builder().gradients(false).hexColors(false).standardColors(false)
            .advancedTransformations(false).build().mmParse(everything)
    );
  }

  @Test
  public void removedDecorations() {
    String string = "<bold><blue>Majekdor";
    Assert.assertEquals(
        MiniMessageWrapper.builder().removeTextDecorations(TextDecoration.BOLD).build().mmParse(string),
        Component.text("Majekdor").color(NamedTextColor.BLUE).decoration(TextDecoration.BOLD, false)
    );
  }

  @Test
  public void placeholderResolver() {
    String string = "<bold><placeholder>Majekdor";
    Assert.assertEquals(
        MiniMessageWrapper.builder().placeholderResolver(PlaceholderResolver.placeholders(
            Placeholder.miniMessage("placeholder", "I am ")
        )).build().mmParse(string),
        Component.text("I am Majekdor").decorate(TextDecoration.BOLD)
    );
    string = "<bold><placeholder>Majekdor";
    Assert.assertEquals(
        MiniMessageWrapper.builder().placeholderResolver(PlaceholderResolver.placeholders(
            Placeholder.miniMessage("placeholder", "<blue>")
        )).build().mmParse(string),
        Component.text("Majekdor").color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD)
    );
  }

  @Test
  public void removeColors() {
    String string = "<bold><blue>I am <red>Majekdor";
    Assert.assertEquals(
        MiniMessageWrapper.builder().removeColors(true, NamedTextColor.RED).build().mmParse(string),
        Component.text("I am Majekdor").color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD)
    );
    string = "<bold><blue>I am &cMajekdor";
    Assert.assertEquals(
        MiniMessageWrapper.builder().legacyColors(true).removeColors(true, NamedTextColor.RED).build().mmParse(string),
        Component.text("I am Majekdor").color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD)
    );
  }

  @Test
  public void preventLuma() {
    String string = "<#000000>Dark Majekdor haha";
    Assert.assertEquals(
        MiniMessageWrapper.builder().preventLuminanceBelow(16).build().mmString(string),
        "Dark Majekdor haha"
    );
    string = "Dark <#000000>Majekdor and light <#ffffff>Majekdor";
    Assert.assertEquals(
        MiniMessageWrapper.builder().preventLuminanceBelow(16).build().mmString(string),
        "Dark Majekdor and light <#ffffff>Majekdor"
    );
    String lots = "<#000001>Dark and some more <#100000>dark and a little more <#001000>dark but <#440044>this isn't!";
    Assert.assertEquals(
        MiniMessageWrapper.builder().preventLuminanceBelow(16).build().mmString(lots),
        "Dark and some more dark and a little more dark but <#440044>this isn't!"
    );
  }

  @Test
  public void advancedTransformations() {
    String string = "<hover:show_text:hover text>Hover here!</hover>";
    Assert.assertEquals(
        Component.text("<hover:show_text:hover text>Hover here!</hover>"),
        MiniMessageWrapper.builder().advancedTransformations(false).build().mmParse(string)
    );
  }

  @Test
  public void cssColorsNonMinecraft() {
    final String oneColor = "<aliceblue>Alice Blue";
    Assert.assertEquals(
        Component.text("Alice Blue").color(TextColor.color(0xf0f8ff)),
        MiniMessageWrapper.standard().mmParse(oneColor)
    );
  }

  @Test
  public void cssColorsMinecraft() {
    final String minecraft = "<aqua>Minecraft";
    Assert.assertEquals(
        Component.text("Minecraft").color(TextColor.color(0x55ffff)),
        MiniMessageWrapper.standard().mmParse(minecraft)
    );
    final String css = "<css:aqua>CSS";
    Assert.assertEquals(
        Component.text("CSS").color(TextColor.color(0x00ffff)),
        MiniMessageWrapper.standard().mmParse(css)
    );
  }
}
