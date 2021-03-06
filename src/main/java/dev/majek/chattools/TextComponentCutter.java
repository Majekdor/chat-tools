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
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Cut components into a list based on a specified max length.
 */
public class TextComponentCutter {

  private final List<Component> result;
  private final Stack<Style> styleStack;
  private final int cutLength;
  private final int maxLength;

  private TextComponent.Builder builder;
  private int currentLength;

  /**
   * Create a new component cutter.
   *
   * @param cutLength the length to start looking for a space for a clean cut
   * @param maxLength the max length to stop looking for spaces and cut with a dash
   */
  public TextComponentCutter(final @Range(from = 0, to = Integer.MAX_VALUE) int cutLength,
                             final @Range(from = 0, to = Integer.MAX_VALUE) int maxLength) {
    this.result = new LinkedList<>();
    this.styleStack = new Stack<>();
    this.cutLength = cutLength;
    this.maxLength = maxLength;
    this.builder = Component.text();
    this.currentLength = 0;
  }

  /**
   * Cut a component into a list of components of the correct length.
   *
   * @param component the component to cut
   * @return list of components of appropriate length
   */
  public @NotNull List<Component> cutComponent(@NotNull Component component) {
    this.styleStack.push(component.style());
    this.process(component);
    this.result.add(this.builder.build());
    return this.result;
  }

  /**
   * Cut the component.
   *
   * @param component the component
   */
  private void process(@NotNull Component component) {
    this.styleStack.push(component.style().merge(this.styleStack.peek(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET));

    if (component instanceof TextComponent) {
      final String text = ((TextComponent) component).content();
      StringBuilder sb = new StringBuilder();
      for (char c : text.toCharArray()) {
        sb.append(c);
        this.currentLength++;

        if (this.currentLength > this.maxLength || (this.currentLength > this.cutLength && c == ' ')) {
          // Add to the builder and build the component
          if (c != ' ') {
            sb.append('-');
          }
          this.builder.append(Component.text(sb.toString(), this.styleStack.peek()));
          this.result.add(this.builder.build());

          // Reset all values
          sb = new StringBuilder(c != ' ' ? "-" : "");
          this.builder = Component.text();
          this.currentLength = 0;
        }
      }
      this.builder.append(Component.text(sb.toString(), this.styleStack.peek()));
    }

    for (Component child : component.children()) {
      this.process(child);
    }

    this.styleStack.pop();
  }
}
