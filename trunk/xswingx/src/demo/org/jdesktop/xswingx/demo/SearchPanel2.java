/*
 * SearchPanel2.java
 *
 * Created on 22. April 2007, 22:02
 */

package org.jdesktop.xswingx.demo;

import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author  peterw
 */
public class SearchPanel2 extends javax.swing.JPanel {
	Popup popup = new Popup();
	class Popup extends JPopupMenu implements ChangeListener{
		ButtonGroup bg = new ButtonGroup();
		JRadioButtonMenuItem mi1 = new JRadioButtonMenuItem("Entire Message");
		JRadioButtonMenuItem mi2 = new JRadioButtonMenuItem("Subject");
		JRadioButtonMenuItem mi3 = new JRadioButtonMenuItem("Sender");
		
		Popup(){
			add(mi1);
			add(mi2);
			add(mi3);
			
			bg.add(mi1);
			bg.add(mi2);
			bg.add(mi3);
			
			mi1.addChangeListener(this);
			mi2.addChangeListener(this);
			mi3.addChangeListener(this);
		}

		public void stateChanged(ChangeEvent e) {
			searchField.setPrompt(((JRadioButtonMenuItem) e.getSource()).getText());
		}
	}
	
    /** Creates new form SearchPanel2 */
    public SearchPanel2() {
        initComponents();
        
        searchField.getSearchButton().setIcon(new ImageIcon(getClass().getResource("/org/jdesktop/xswingx/plaf/basic/resources/search_popup.png")));
        searchField.getSearchButton().addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent e) {
        		Rectangle r = SwingUtilities.getLocalBounds(searchField.getSearchButton());
        		popup.show(searchField.getSearchButton(), r.x, r.y+r.height);
        	}
        });
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        searchField = new org.jdesktop.xswingx.JXSearchField();
        jLabel1 = new javax.swing.JLabel();

        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(267, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_searchFieldActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private org.jdesktop.xswingx.JXSearchField searchField;
    // End of variables declaration//GEN-END:variables
    
}
