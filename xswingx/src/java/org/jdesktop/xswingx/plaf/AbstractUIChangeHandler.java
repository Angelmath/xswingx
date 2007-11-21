package org.jdesktop.xswingx.plaf;

import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

public abstract class AbstractUIChangeHandler implements PropertyChangeListener {
	public void install(JComponent c){
		c.addPropertyChangeListener("UI", this);
	}
	
	public void uninstall(JComponent c){
		c.removePropertyChangeListener("UI", this);
	}
}