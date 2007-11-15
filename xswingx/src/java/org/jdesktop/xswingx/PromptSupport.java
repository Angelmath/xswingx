package org.jdesktop.xswingx;

import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeListener;

import javax.swing.plaf.TextUI;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;

import org.jdesktop.xswingx.plaf.PromptTextAreaUI;
import org.jdesktop.xswingx.plaf.PromptTextFieldUI;
import org.jdesktop.xswingx.plaf.PromptTextUI;
import org.jdesktop.xswingx.plaf.TextUIWrapper;

/**
 * <p>
 * Sets prompt text, foreground, background and {@link FocusBehavior} properties
 * on a JTextComponent by calling
 * {@link JTextComponent#putClientProperty(Object, Object)}. These properties
 * are used by {@link PromptTextUI} instances to render the prompt of a text
 * component.
 * </p>
 * 
 * <p>
 * This class is used by {@link JXPromptField}, {@link JXFormattedPromptField}
 * and {@link JXPromptArea} to get and set prompt properties.
 * {@link PromptTextUI} retrieves these properties using PromptSupport.
 * </p>
 * 
 * @see JXPromptField
 * @see JXFormattedPromptField
 * @see JXPromptArea
 * @see PromptTextUI
 * 
 * @author Peter Weishapl <petw@gmx.net>
 * 
 */
public class PromptSupport {
	/**
	 * The prompt text property.
	 */
	public static final String PROMPT = "promptText";

	/**
	 * The color of the prompt text poroperty.
	 */
	public static final String FOREGROUND = "promptForeground";

	/**
	 * The prompt background property.
	 */
	public static final String BACKGROUND = "promptBackground";

	/**
	 * The focus behavior property.
	 */
	public static final String FOCUS_BEHAVIOR = "focusBehavior";

	/**
	 * The font style property, if different from the components font.
	 */
	public static final String FONT_STYLE = "promptFontStyle";

	/**
	 * <p>
	 * Determines how the {@link JTextComponent} is rendered when focused and no
	 * text is present.
	 * </p>
	 */
	public static enum FocusBehavior {
		/**
		 * Keep the prompt text visible.
		 */
		SHOW_PROMPT,
		/**
		 * Highlight the prompt text as it would be selected.
		 */
		HIGHLIGHT_PROMPT,
		/**
		 * Hide the prompt text.
		 */
		HIDE_PROMPT
	};

	/**
	 * <p>
	 * Wraps and replaces the current UI of the given <code>textComponent</code>,
	 * by calling {@link #wrapUI(JTextComponent)} if necessary.
	 * </p>
	 * <p>
	 * The support for prompts will be discontinued on any change of the text
	 * components UI. If you want to prevent this, use
	 * {@link #install(JTextComponent, boolean)}.
	 * </p>
	 * 
	 * @param textComponent
	 * @see #install(JTextComponent, boolean)
	 */
	public static void install(final JTextComponent textComponent) {
		install(textComponent, true);
	}

	/**
	 * <p>
	 * Wraps and replaces the current UI of the given <code>textComponent</code>,
	 * by calling {@link #wrapUI(JTextComponent)} if necessary.
	 * </p>
	 * 
	 * @param textComponent
	 * @param stayOnUIChange
	 *            if <code>true</code>, a {@link PropertyChangeListener} is
	 *            registered, which listens for UI changes and wraps any new UI
	 *            object.
	 */
	public static void install(final JTextComponent textComponent, boolean stayOnUIChange) {
		wrapper.install(textComponent, stayOnUIChange);
	}

	/**
	 * Calls {@link TextUIWrapper}{@link #uninstall(JTextComponent)}
	 * 
	 * @param textComponent
	 */
	public static void uninstall(final JTextComponent textComponent) {
		wrapper.uninstall(textComponent);
	}

	/**
	 * Wraps the UI of <code>textComponent</code>.
	 * 
	 * @see #wrapUI(TextUI)
	 * @param textComponent
	 * @return
	 */
	public static PromptTextUI wrapUI(JTextComponent textComponent) {
		return wrapUI(textComponent.getUI());
	}

	/**
	 * <p>
	 * Creates a new {@link PromptTextUI}, which wraps the given
	 * <code>textUI</code>.
	 * </p>
	 * <p>
	 * If <code>textUI</code> is of type {@link PromptTextUI},
	 * <code>textUI</code> will be returned. If <code>textUI</code> is of
	 * type {@link BasicTextFieldUI} a {@link PromptTextFieldUI} object will be
	 * returned. If <code>textUI</code> is of type {@link BasicTextAreaUI} a
	 * {@link PromptTextAreaUI} object will be returned.
	 * </p>
	 * 
	 * @param textUI
	 *            wrap this UI
	 * @return a {@link PromptTextUI} which wraps <code>textUI</code>
	 */
	public static PromptTextUI wrapUI(TextUI textUI) {
		return wrapper.wrapUI(textUI);
	}

	/**
	 * <p>
	 * Convenience method to set the <code>promptText</code> and
	 * <code>promptTextColor</code> on a {@link JTextComponent}.
	 * </p>
	 * <p>
	 * If <code>stayOnUIChange</code> is true,  The prompt support will stay installed, even when the text components UI
	 * changes. See {@link #install(JTextComponent, boolean)}.
	 * </p>
	 * 
	 * @param promptText
	 * @param promptForeground
	 * @param promptBackground
	 * @param textComponent
	 * @param stayOnUIChange
	 */
	public static void init(String promptText, Color promptForeground, Color promptBackground,
			final JTextComponent textComponent, boolean stayOnUIChange) {
		install(textComponent, stayOnUIChange);
		
		setPrompt(promptText, textComponent);
		setForeground(promptForeground, textComponent);
		setBackground(promptBackground, textComponent);
	}

	/**
	 * Get the {@link FocusBehavior} of <code>textComponent</code>.
	 * 
	 * @param textComponent
	 * @return the {@link FocusBehavior} or {@link FocusBehavior#HIDE_PROMPT} if
	 *         none is set
	 */
	public static FocusBehavior getFocusBehavior(JTextComponent textComponent) {
		FocusBehavior fb = (FocusBehavior) textComponent.getClientProperty(FOCUS_BEHAVIOR);
		if (fb == null) {
			fb = FocusBehavior.HIDE_PROMPT;
		}
		return fb;
	}

	/**
	 * Sets the {@link FocusBehavior} on <code>textComponent</code> and
	 * repaints the component to reflect the changes, if it is the focus owner.
	 * 
	 * @param focusBehavior
	 * @param textComponent
	 */
	public static void setFocusBehavior(FocusBehavior focusBehavior, JTextComponent textComponent) {
		textComponent.putClientProperty(FOCUS_BEHAVIOR, focusBehavior);
		if (textComponent.isFocusOwner()) {
			textComponent.repaint();
		}
	}

	/**
	 * Get the prompt text of <code>textComponent</code>.
	 * 
	 * @param textComponent
	 * @return the prompt text
	 */
	public static String getPrompt(JTextComponent textComponent) {
		return (String) textComponent.getClientProperty(PROMPT);
	}

	/**
	 * <p>
	 * Sets the prompt text on <code>textComponent</code>. Also sets the
	 * tooltip text to the prompt text if <code>textComponent</code> has no
	 * tooltip text or the current tooltip text is the same as the current
	 * prompt text.
	 * </p>
	 * <p>
	 * Calls {@link #install(JTextComponent)} to ensure that the
	 * <code>textComponent</code>s UI is wrapped by the appropriate
	 * {@link PromptTextUI}.
	 * </p>
	 * 
	 * @param promptText
	 * @param textComponent
	 */
	public static void setPrompt(String promptText, JTextComponent textComponent) {
		install(textComponent);

		// display prompt as tooltip by default
		if (textComponent.getToolTipText() == null || textComponent.getToolTipText().equals(getPrompt(textComponent))) {
			textComponent.setToolTipText(promptText);
		}

		textComponent.putClientProperty(PROMPT, promptText);
		textComponent.repaint();
	}

	/**
	 * Get the foreground color of the prompt text. If no color has been set,
	 * the <code>textComponent</code>s disabled text color will be returned.
	 * 
	 * @param textComponent
	 * @return the color of the prompt text or
	 *         {@link JTextComponent#getDisabledTextColor()} if none is set
	 */
	public static Color getForeground(JTextComponent textComponent) {
		if (textComponent.getClientProperty(FOREGROUND) == null) {
			return textComponent.getDisabledTextColor();
		}
		return (Color) textComponent.getClientProperty(FOREGROUND);
	}

	/**
	 * Sets the foreground color of the prompt on <code>textComponent</code>
	 * and repaints the component to reflect the changes. This color will be
	 * used when no text is present.
	 * 
	 * @param promptTextColor
	 * @param textComponent
	 */
	public static void setForeground(Color promptTextColor, JTextComponent textComponent) {
		textComponent.putClientProperty(FOREGROUND, promptTextColor);
		textComponent.repaint();
	}

	/**
	 * Get the background color of the <code>textComponent</code>, when no
	 * text is present. If no color has been set, the <code>textComponent</code>s
	 * background color color will be returned.
	 * 
	 * @param textComponent
	 * @return the the background color of the text component, when no text is
	 *         present
	 */
	public static Color getBackground(JTextComponent textComponent) {
		if (textComponent.getClientProperty(BACKGROUND) == null) {
			return textComponent.getBackground();
		}
		return (Color) textComponent.getClientProperty(BACKGROUND);
	}

	/**
	 * <p>
	 * Sets the prompts background color on <code>textComponent</code> and
	 * repaints the component to reflect the changes. This background color will
	 * only be used when no text is present.
	 * </p>
	 * <p>
	 * Calls {@link #install(JTextComponent)} to ensure that the
	 * <code>textComponent</code>s UI is wrapped by the appropriate
	 * {@link PromptTextUI}.
	 * </p>
	 * 
	 * @param background
	 * @param textComponent
	 */
	public static void setBackground(Color background, JTextComponent textComponent) {
		install(textComponent);

		textComponent.putClientProperty(BACKGROUND, background);
		textComponent.repaint();
	}

	/**
	 * <p>
	 * Set the style of the prompt font, if different from the
	 * <code>textComponent</code>s font.
	 * </p>
	 * <p>
	 * Allowed values are {@link Font#PLAIN}, {@link Font#ITALIC},
	 * {@link Font#BOLD}, a combination of {@link Font#BOLD} and
	 * {@link Font#ITALIC} or <code>null</code> if the prompt font should be
	 * the same as the <code>textComponent</code>s font.
	 * </p>
	 * 
	 * @param fontStyle
	 * @param textComponent
	 */
	public static void setFontStyle(Integer fontStyle, JTextComponent textComponent) {
		textComponent.putClientProperty(FONT_STYLE, fontStyle);
		textComponent.revalidate();
		textComponent.repaint();
	}

	/**
	 * Returns the font style of the prompt text, or <code>null</code> if the
	 * prompt's font style should not differ from the <code>textComponent</code>s
	 * font.
	 * 
	 * @param textComponent
	 * @return font style of the prompt text
	 */
	public static Integer getFontStyle(JTextComponent textComponent) {
		return (Integer) textComponent.getClientProperty(FONT_STYLE);
	}

	private static final PromptWrapper wrapper = new PromptWrapper();
	private static final class PromptWrapper extends TextUIWrapper<PromptTextUI> {
		private PromptWrapper() {
			super(PromptTextUI.class);
		}

		@Override
		public PromptTextUI wrapUI(TextUI textUI) {
			if (textUI instanceof PromptTextUI) {
				return (PromptTextUI) textUI;
			} else if (textUI instanceof BasicTextFieldUI) {
				return new PromptTextFieldUI(textUI);
			} else if (textUI instanceof BasicTextAreaUI) {
				return new PromptTextAreaUI(textUI);
			}
			throw new IllegalArgumentException();
		}
	}
}