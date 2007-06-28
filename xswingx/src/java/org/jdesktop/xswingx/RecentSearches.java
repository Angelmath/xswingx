package org.jdesktop.xswingx;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.xswingx.JXSearchField.SearchMode;

/**
 * Maintains a list of recent searches and persists this list automatically
 * using {@link Preferences}. A recent searches popup menu can be installed on
 * a {@link JXSearchField} using {@link #install(JXSearchField)}.
 * 
 * @author Peter Weishapl <petw@gmx.net>
 * 
 */
public class RecentSearches implements ActionListener {
	private Preferences prefs;

	private int maxRecents = 5;

	private List<String> recentSearches = new ArrayList<String>();

	private List<ChangeListener> listeners = new ArrayList<ChangeListener>();

	/**
	 * Creates a list of recent searches and uses <code>saveName</code> to
	 * persist this list under the {@link Preferences} user root node. Existing
	 * entries will be loaded automatically.
	 * 
	 * @param saveName
	 *            a unique name for saving this list of recent searches
	 */
	public RecentSearches(String saveName) {
		this(Preferences.userRoot(), saveName);
	}

	/**
	 * Creates a list of recent searches and uses <code>saveName</code> to
	 * persist this list under the <code>prefs</code> node. Existing entries
	 * will be loaded automatically.
	 * 
	 * @param prefs
	 *            the preferences node under which this list will be persisted
	 * @param saveName
	 *            a unique name for saving this list of recent searches
	 */
	public RecentSearches(Preferences prefs, String saveName) {
		this.prefs = prefs.node(saveName);
		load();
	}

	private void load() {
		try {
			String[] recent = new String[prefs.keys().length];
			for (String key : prefs.keys()) {
				recent[prefs.getInt(key, -1)] = key;
			}
			recentSearches.addAll(Arrays.asList(recent));
		} catch (Exception ex) {
			// ignore
		}
	}

	private void save() {
		try {
			prefs.clear();
		} catch (BackingStoreException e) {
			// ignore
		}

		int i = 0;
		for (String search : recentSearches) {
			prefs.putInt(search, i++);
		}

		fireChangeEvent();
	}

	/**
	 * Add a search string as the first element. If the search string is
	 * <code>null</code> or empty nothing will be added. If the search string
	 * already exists, the old element will be removed. The modified list will
	 * automatically be persisted.
	 * 
	 * If the number of elements exceeds the maximum number of entries, the last
	 * entry will be removed.
	 * 
	 * @see #getMaxRecents()
	 * @param searchString
	 *            the search string to add
	 */
	public void put(String searchString) {
		if (searchString == null || searchString.trim().length() == 0) {
			return;
		}

		int lastIndex = recentSearches.indexOf(searchString);
		if (lastIndex != -1) {
			recentSearches.remove(lastIndex);
		}
		recentSearches.add(0, searchString);
		if (getLength() > getMaxRecents()) {
			recentSearches.remove(recentSearches.size() - 1);
		}
		save();
	}

	/**
	 * Returns all recent searches in this list.
	 * 
	 * @return the recent searches
	 */
	public String[] getRecentSearches() {
		return recentSearches.toArray(new String[] {});
	}

	/**
	 * The number of recent searches.
	 * 
	 * @return number of recent searches
	 */
	public int getLength() {
		return recentSearches.size();
	}

	/**
	 * Remove all recent searches.
	 */
	public void removeAll() {
		recentSearches.clear();
		save();
	}

	/**
	 * Returns the maximum number of recent searches.
	 * 
	 * @see #put(String)
	 * @return the maximum number of recent searches
	 */
	public int getMaxRecents() {
		return maxRecents;
	}

	/**
	 * Set the maximum number of recent searches.
	 * 
	 * @see #put(String)
	 * @param maxRecents
	 *            maximum number of recent searches
	 */
	public void setMaxRecents(int maxRecents) {
		this.maxRecents = maxRecents;
	}

	/**
	 * Add a change listener. A {@link ChangeEvent} will be fired whenever a
	 * search is added or removed.
	 * 
	 * @param l
	 *            the {@link ChangeListener}
	 */
	public void addChangeListener(ChangeListener l) {
		listeners.add(l);
	}

	/**
	 * Remove a change listener.
	 * 
	 * @param l
	 *            a registered {@link ChangeListener}
	 */
	public void removeChangeListener(ChangeListener l) {
		listeners.remove(l);
	}

	/**
	 * Returns all registered {@link ChangeListener}s.
	 * 
	 * @return all registered {@link ChangeListener}s
	 */
	public ChangeListener[] getChangeListeners() {
		return listeners.toArray(new ChangeListener[] {});
	}

	private void fireChangeEvent() {
		ChangeEvent e = new ChangeEvent(this);

		for (ChangeListener l : listeners) {
			l.stateChanged(e);
		}
	}

	/**
	 * Creates the recent searches popup menu which will be set as a search
	 * popup menu on <code>searchField</code> by
	 * {@link #install(JXSearchField)}. Override to return a custom popup menu.
	 * 
	 * @param searchField
	 *            the search field the returned popup menu will be installed on
	 * @return the recent searches popup menu
	 */
	protected JPopupMenu createPopupMenu(JXSearchField searchField) {
		return new RecentSearchesPopup(this, searchField);
	}

	/**
	 * Install a recent the searches popup menu returned by
	 * {@link #createPopupMenu(JXSearchField)} on <code>searchField</code>.
	 * Also registers an {@link ActionListener} on <code>searchField</code>
	 * and adds the search string to the list of recent searches whenever a
	 * {@link ActionEvent} is received.
	 * 
	 * @param searchField
	 *            the search field to install a recent searches popup menu on
	 */
	public void install(JXSearchField searchField) {
		searchField.addActionListener(this);
		searchField.setSearchPopupMenu(createPopupMenu(searchField));
	}

	/**
	 * Remove the recent searches popup from <code>searchField</code> when
	 * installed and stop listening for {@link ActionEvent}s fired by the
	 * search field.
	 * 
	 * @param searchField
	 *            uninstall recent searches popup menu
	 */
	public void uninstall(JXSearchField searchField) {
		searchField.removeActionListener(this);
		if (searchField.getSearchPopupMenu() instanceof ChangeListener) {
			removeChangeListener((ChangeListener) searchField.getSearchPopupMenu());
		}
	}

	/**
	 * Calls {@link #put(String)} with the {@link ActionEvent}s action command
	 * as the search string.
	 */
	public void actionPerformed(ActionEvent e) {
		put(e.getActionCommand());
	}

	/**
	 * The popup menu returned by
	 * {@link RecentSearches#createPopupMenu(JXSearchField)}.
	 */
	public static class RecentSearchesPopup extends JPopupMenu implements ActionListener, ChangeListener {
		private RecentSearches recentSearches;

		private JXSearchField searchField;

		private JMenuItem clear;

		/**
		 * Creates a new popup menu based on the given {@link RecentSearches}
		 * and {@link JXSearchField}.
		 * 
		 * @param recentSearches
		 * @param searchField
		 */
		public RecentSearchesPopup(RecentSearches recentSearches, JXSearchField searchField) {
			this.searchField = searchField;
			this.recentSearches = recentSearches;

			recentSearches.addChangeListener(this);
			buildMenu();
		}

		/**
		 * Rebuilds the menu according to the recent searches.
		 */
		private void buildMenu() {
			setVisible(false);
			removeAll();

			if (recentSearches.getLength() == 0) {
				JMenuItem noRecent = new JMenuItem(UIManager.getString("SearchField.noRecentsText"));
				noRecent.setEnabled(false);
				add(noRecent);
			} else {
				JMenuItem recent = new JMenuItem(UIManager.getString("SearchField.recentsMenuTitle"));
				recent.setEnabled(false);
				add(recent);

				for (String searchString : recentSearches.getRecentSearches()) {
					JMenuItem mi = new JMenuItem(searchString);
					mi.addActionListener(this);
					add(mi);
				}

				addSeparator();
				clear = new JMenuItem(UIManager.getString("SearchField.clearRecentsText"));
				clear.addActionListener(this);
				add(clear);
			}
		}

		/**
		 * Sets {@link #searchField}s text to the {@link ActionEvent}s action
		 * command and call {@link JXSearchField#postActionEvent()} to fire an
		 * {@link ActionEvent}, if <code>e</code>s source is not the clear
		 * menu item. If the source is the clear menu item, all recent searches
		 * will be removed.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == clear) {
				recentSearches.removeAll();
			} else {
				searchField.setText(e.getActionCommand());
				searchField.postActionEvent();
			}
		}

		/**
		 * Every time the recent searches fires a {@link ChangeEvent} call
		 * {@link #buildMenu()} to rebuild the whole menu.
		 */
		public void stateChanged(ChangeEvent e) {
			buildMenu();
		}
	}
}
