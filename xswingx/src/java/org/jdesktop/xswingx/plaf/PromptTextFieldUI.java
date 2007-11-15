package org.jdesktop.xswingx.plaf;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.plaf.TextUI;
import javax.swing.text.JTextComponent;

import org.jdesktop.xswingx.BuddySupport;
import org.jdesktop.xswingx.SearchFieldSupport;

/**
 * {@link PromptTextUI} implementation for rendering prompts on
 * {@link JTextField}s and uses a {@link JTextField} as a prompt component.
 * 
 * @author Peter Weishapl <petw@gmx.net>
 * 
 */
public class PromptTextFieldUI extends PromptTextUI {
	/**
	 * Shared prompt renderer.
	 */
	private final static LabelField txt = new LabelField();

	/**
	 * Creates a new {@link PromptTextFieldUI}.
	 * 
	 * @param delegate
	 */
	public PromptTextFieldUI(TextUI delegate) {
		super(delegate);
	}

	/**
	 * Overrides {@link #getPromptComponent(JTextComponent)} to additionally
	 * update {@link JTextField} specific properties.
	 */
	public JTextComponent getPromptComponent(JTextComponent txt) {
		LabelField lbl = (LabelField) super.getPromptComponent(txt);
		JTextField txtField = (JTextField) txt;

		lbl.setHorizontalAlignment(txtField.getHorizontalAlignment());
		lbl.setColumns(txtField.getColumns());

		// Make search field in Leopard paint focused border.
		lbl.hasFocus = txtField.hasFocus() && SearchFieldSupport.isNativeSearchField(txtField);

		// quaqua rounded corners
		promptComponent.putClientProperty("Quaqua.TextField.style", txt.getClientProperty("Quaqua.TextField.style"));

		// leopard client properties. see
		// http://developer.apple.com/technotes/tn2007/tn2196.html#JTEXTFIELD_VARIANT
		promptComponent.putClientProperty("JTextField.variant", txt.getClientProperty("JTextField.variant"));
		promptComponent.putClientProperty("JTextField.Search.FindPopup", txt
				.getClientProperty("JTextField.Search.FindPopup"));

		//buddy support
		BuddySupport.setOuterMargin(lbl, BuddySupport.getOuterMargin(txtField));
		BuddySupport.setLeft(lbl, BuddySupport.getLeft(txtField));
		BuddySupport.setRight(lbl, BuddySupport.getRight(txtField));

		return lbl;
	}

	/**
	 * Overriden to support native search fields in Mac OS 10.5.
	 * 
	 * Also paints the prompt if the text field is a search field as returned by
	 * {@link SearchFieldSupport#isSearchField(JTextField)} and native search
	 * fields are supported.
	 * 
	 * @see SearchFieldSupport#isNativeSearchFieldSupported()
	 * @see SearchFieldSupport#isSearchField(JTextField)
	 */
	@Override
	public void paint(Graphics g, JComponent c) {
		JTextField txt = (JTextField) c;

		if (SearchFieldSupport.isNativeSearchField(txt)) {
			// Paint Mac OS 10.5 native search field.
			getPromptComponent(txt).paint(g);
			if (!shouldPaintPrompt(txt)) {
				delegate.paint(g, c);
			}
		} else {
			super.paint(g, c);
		}
	}

	/**
	 * Returns a shared {@link JTextField}.
	 */
	protected JTextComponent createPromptComponent() {
		txt.updateUI();
		return txt;
	}

	private static final class LabelField extends JTextField {
		boolean hasFocus;

		@Override
		public boolean hasFocus() {
			return hasFocus;
		}
	}
}