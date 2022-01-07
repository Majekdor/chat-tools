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
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;
import net.kyori.adventure.text.minimessage.transformation.TransformationType;
import net.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.NotNull;

/**
 * A wrapper for {@link MiniMessage} to add a few more methods for more customization.
 *
 * @author Majekdor
 */
public interface MiniMessageWrapper extends Buildable<MiniMessageWrapper, MiniMessageWrapper.Builder> {

  /**
   * <p>Gets a simple instance.</p>
   * <p>This will parse everything like {@link MiniMessage} will except for advanced transformations.</p>
   * <p>Builder options with this instance:</p>
   * <ul>
   *   <li>Gradients: True</li>
   *   <li>Hex Colors: True</li>
   *   <li>Standard Colors: True</li>
   *   <li>Legacy Colors: False</li>
   *   <li>Advanced Transformations: False</li>
   *   <li>Placeholder Resolver: Empty</li>
   *   <li>Block Text Decorations: None</li>
   *   <li>Blocked Colors: None</li>
   *   <li>Luminance Threshold: 0</li>
   * </ul>
   *
   * @return a simple instance
   */
  static @NotNull MiniMessageWrapper standard() {
    return MiniMessageWrapperImpl.STANDARD;
  }

  /**
   * <p>Gets a simple instance with legacy code support.</p>
   * <p>This will parse everything like {@link MiniMessage} will with the addition of
   * legacy code support and the subtraction of advanced transformation support.</p>
   * <p>Builder options with this instance:</p>
   * <ul>
   *   <li>Gradients: True</li>
   *   <li>Hex Colors: True</li>
   *   <li>Standard Colors: True</li>
   *   <li>Legacy Colors: True</li>
   *   <li>Advanced Transformations: False</li>
   *   <li>Placeholder Resolver: Empty</li>
   *   <li>Block Text Decorations: None</li>
   *   <li>Blocked Colors: None</li>
   *   <li>Luminance Threshold: 0</li>
   * </ul>
   *
   * @return a simple instance
   */
  static @NotNull MiniMessageWrapper legacy() {
    return MiniMessageWrapperImpl.LEGACY;
  }

  /**
   * Parse a string into a {@link Component} using {@link MiniMessage}.
   *
   * @param mmString the string to parse
   * @return component
   */
  @NotNull Component mmParse(@NotNull String mmString);

  /**
   * Get the modified string.
   *
   * @param mmString string to modify
   * @return modified string
   */
  @NotNull String mmString(@NotNull String mmString);

  /**
   * <p>Creates a new {@link Builder}.</p>
   * <p>Default builder options:</p>
   * <ul>
   *   <li>Gradients: True</li>
   *   <li>Hex Colors: True</li>
   *   <li>Standard Colors: True</li>
   *   <li>Legacy Colors: False</li>
   *   <li>Advanced Transformations: False</li>
   * </ul>
   *
   * @return a builder
   */
  static @NotNull Builder builder() {
    return new MiniMessageWrapperImpl.BuilderImpl();
  }

  /**
   * Create a {@link Builder} to modify options.
   *
   * @return a builder
   */
  @Override
  @NotNull Builder toBuilder();

  /**
   * A builder for {@link MiniMessageWrapper}.
   */
  interface Builder extends Buildable.Builder<MiniMessageWrapper> {

    /**
     * Whether gradients on the final string should be parsed.
     *
     * @param parse whether to parse
     * @return this builder
     */
    @NotNull Builder gradients(final boolean parse);

    /**
     * Whether hex colors on the final string should be parsed.
     *
     * @param parse whether to parse
     * @return this builder
     */
    @NotNull Builder hexColors(final boolean parse);

    /**
     * Whether all standard color codes on the final string should be parsed.
     *
     * @param parse whether to parse
     * @return this builder
     */
    @NotNull Builder standardColors(final boolean parse);

    /**
     * Whether legacy color codes on the final string should be parsed.
     *
     * @param parse whether to parse
     * @return this builder
     */
    @NotNull Builder legacyColors(final boolean parse);

    /**
     * Whether to parse advanced {@link TransformationType}s on the final string to be parsed.
     * This includes click events, hover events, fonts, etc.
     *
     * @param parse whether to parse
     * @return this builder
     */
    @NotNull Builder advancedTransformations(final boolean parse);

    /**
     * The {@link TextDecoration}s that should not be parsed.
     *
     * @param decorations the decorations
     * @return this builder
     */
    @NotNull Builder removeTextDecorations(final @NotNull TextDecoration... decorations);

    /**
     * Set the {@link PlaceholderResolver} for the {@link MiniMessage} instance.
     *
     * @param placeholderResolver the placeholder resolver
     * @return this builder
     */
    @NotNull Builder placeholderResolver(final @NotNull PlaceholderResolver placeholderResolver);


    /**
     * The {@link NamedTextColor}s that should not be parsed.
     *
     * @param blockCloseHex whether to block hex codes that are close to blocked colors
     * @param colors the colors
     * @return this builder
     */
    @NotNull Builder removeColors(final boolean blockCloseHex, final @NotNull NamedTextColor... colors);

    /**
     * <p>Prevent hex colors that have a luminance below a certain threshold.
     * Luminance is measure 0 - 255. There's a method in {@link MiniMessageWrapperImpl}
     * that is used to calculate it. This is typically used to prevent very dark colors.</p>
     * <p>Note: If you want to block the normal black color, remember to add
     * it to {@link #removeColors(boolean, NamedTextColor...)}</p>
     *
     * @param threshold all colors with luminance below this will not be parsed
     * @return this builder
     */
    @NotNull Builder preventLuminanceBelow(final int threshold);

    /**
     * Build the {@link MiniMessageWrapper} ready to parse.
     *
     * @return the wrapper
     */
    @Override
    @NotNull MiniMessageWrapper build();
  }
}
