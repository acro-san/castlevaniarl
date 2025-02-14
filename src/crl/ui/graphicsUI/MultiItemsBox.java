package crl.ui.graphicsUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import crl.item.Item;
import crl.player.Equipment;
import crl.ui.graphicsUI.components.GFXButton;

class MultiItemsBox extends AdornedBorderPanel {
	private JList lstInventory;
	private GFXButton btnExit;
	private GFXButton btnOk;
	private JLabel lblPrompt;
	
	private Thread activeThread;
	
	public void setVisible(boolean val) {
		super.setVisible(val);
		if (val){
			lstInventory.requestFocus();
			if (lstInventory.getModel().getSize() > 0)
				lstInventory.setSelectedIndex(0);
		}
	}
	
	public MultiItemsBox(Image UPRIGHT, 
			Image UPLEFT, Image DOWNRIGHT, Image DOWNLEFT,
			Color OUT_COLOR, Color IN_COLOR,
			int borderWidth, int borderHeight) {
		super(UPRIGHT, UPLEFT, DOWNRIGHT, DOWNLEFT, OUT_COLOR, IN_COLOR, borderWidth, borderHeight);
		
		lstInventory = new JList(new DefaultListModel());
		lstInventory.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		btnExit = new GFXButton(GFXUserInterface.IMG_EXIT_BTN);
		btnOk = new GFXButton(GFXUserInterface.IMG_OK_BTN);

		lstInventory.setOpaque(false);
		lstInventory.setCellRenderer(new ItemsCellRenderer());
		
		setOpaque(false);
		int sw = GFXUserInterface.STANDARD_WIDTH;
		setBorder(new EmptyBorder(sw,sw,sw,sw));
		
		setLayout(new BorderLayout());
		
		lblPrompt = new JLabel("Inventory");
		lblPrompt.setFont(GFXUserInterface.FNT_MESSAGEBOX);
		lblPrompt.setForeground(GFXDisplay.COLOR_BOLD);
		
		JPanel pnlButtons = new JPanel();
		pnlButtons.add(btnExit);
		pnlButtons.add(btnOk);
		pnlButtons.setOpaque(false);
		
		add(lblPrompt, BorderLayout.NORTH);
		add(lstInventory, BorderLayout.CENTER);
		add(pnlButtons, BorderLayout.SOUTH);
		
		setBackground(Color.BLACK);
		
		btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				doExit();
			}
		});
		btnOk.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				doOk();
			}
		});
		lstInventory.addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE){
					doOk();
				}else
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
					doExit();
				}else if (e.getKeyCode() == KeyEvent.VK_NUMPAD8){
					if (lstInventory.getSelectedIndex() > 0)
						lstInventory.setSelectedIndex(lstInventory.getSelectedIndex()-1);
				} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD2){
					if (lstInventory.getSelectedIndex() < lstInventory.getModel().getSize()-1)
						lstInventory.setSelectedIndex(lstInventory.getSelectedIndex()+1);
				}
			}

			public void keyReleased(KeyEvent e) {}

			public void keyTyped(KeyEvent e) {}
			
		});
		
	}
	
	private void doOk(){
		if (activeThread != null) {
			choice = new Vector();
			int[] indices = lstInventory.getSelectedIndices();
			for (int i = 0; i < indices.length; i++){
				choice.add(((Equipment)inventory.elementAt(indices[i])).getItem());
			}
			activeThread.interrupt();
		}
	}
	
	private void doExit(){
		if (activeThread != null) {
			choice = null;
			activeThread.interrupt();
		}
	}
	private Vector<Item> choice;
	private Vector inventory;
	
	public Vector getChoice(){
		return choice;
	}
	
	public void setPrompt(String prompt){
		lblPrompt.setText(prompt);
	}
	public void setItems(Vector items){
		inventory = (Vector) items.clone();
		((DefaultListModel)lstInventory.getModel()).removeAllElements();
		for (int i = 0; i < items.size(); i++){
			((DefaultListModel)lstInventory.getModel()).addElement(items.elementAt(i));
		}
	}
	
	public void informChoice(Thread who) {
		activeThread = who;
	}
	
	class ItemsCellRenderer extends DefaultListCellRenderer {
		private JLabel ren;
		
		public ItemsCellRenderer() {
			ren = new JLabel();
			ren.setFont(GFXUserInterface.FNT_MESSAGEBOX);
			ren.setOpaque(false);
			ren.setForeground(Color.WHITE);
			ren.setBackground(GFXDisplay.COLOR_BOLD);

		}
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			if (value instanceof Equipment){
				Equipment smi = (Equipment) value;
				ren.setText(smi.getMenuDescription());
				ren.setIcon(new ImageIcon(((GFXAppearance)smi.getItem().getAppearance()).getIconImage()));
			} else {
				Item smi = (Item) value;
				ren.setText(smi.getMenuDescription());
				ren.setIcon(new ImageIcon(((GFXAppearance)smi.getAppearance()).getIconImage()));
			}
				
			ren.setOpaque(isSelected);
			return ren;
		}
	}
}