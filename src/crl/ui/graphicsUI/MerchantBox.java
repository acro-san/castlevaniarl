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
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import crl.item.Item;
//import crl.ui.graphicsUI.GFXUserInterface.MerchantBox.MerchandiseCellRenderer;
import crl.ui.graphicsUI.components.GFXButton;

class MerchantBox extends AdornedBorderPanel {
	
	private JList lstMerchandise;
	private GFXButton btnBuy;
	private GFXButton btnExit;
	private GFXButton btnYes;
	private GFXButton btnNo;
	private JTextArea prompt;
	private JLabel lblGold;

	//private ShopMenuItem choice;
	private Item choice;
	private Thread activeThread;
	
	public void setVisible(boolean val){
		super.setVisible(val);
		if (val){
			lstMerchandise.requestFocus();
			if (lstMerchandise.getModel().getSize() > 0)
				lstMerchandise.setSelectedIndex(0);
		}
	}
	
	public MerchantBox(GFXUserInterface gui, Image UPRIGHT, 
			Image UPLEFT, Image DOWNRIGHT, Image DOWNLEFT,
			Color OUT_COLOR, Color IN_COLOR,
			int borderWidth, int borderHeight) {
		super(UPRIGHT, UPLEFT, DOWNRIGHT, DOWNLEFT, OUT_COLOR, IN_COLOR, borderWidth, borderHeight);
		
		lstMerchandise = new JList(new DefaultListModel());
		btnBuy = new GFXButton(gui.IMG_BUY_BTN);
		btnExit = new GFXButton(gui.IMG_EXIT_BTN);
		btnYes = new GFXButton(gui.IMG_YES_BTN);
		btnNo = new GFXButton(gui.IMG_NO_BTN);

		lblGold = new JLabel();
		lblGold.setOpaque(false);
		lblGold.setForeground(Color.YELLOW);
		btnYes.setVisible(false);
		btnNo.setVisible(false);
		
		lstMerchandise.setCellRenderer(new MerchandiseCellRenderer());
		lstMerchandise.setOpaque(false);
		
		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER ||
					e.getKeyCode() == KeyEvent.VK_SPACE ||
					e.getKeyCode() == KeyEvent.VK_Y)
				{
					if (btnYes.isVisible())
						doYes();
					else
						doBuy();
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE || 
					e.getKeyCode() == KeyEvent.VK_N) {
					if (btnYes.isVisible())
						doNo();
					else
						doExit();
				}
			}

			public void keyReleased(KeyEvent e) {}

			public void keyTyped(KeyEvent e) {}
		});
		
		lstMerchandise.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER || 
					e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (btnYes.isVisible())
						doYes();
					else
						doBuy();
				}else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (btnYes.isVisible())
						doNo();
					else
						doExit();
				} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
					if (lstMerchandise.getSelectedIndex() > 0)
						lstMerchandise.setSelectedIndex(lstMerchandise.getSelectedIndex()-1);
				} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD2){
					if (lstMerchandise.getSelectedIndex() < lstMerchandise.getModel().getSize()-1)
						lstMerchandise.setSelectedIndex(lstMerchandise.getSelectedIndex()+1);
				}
			}

			public void keyReleased(KeyEvent e) {}

			public void keyTyped(KeyEvent e) {}
			
		});
		
		setOpaque(false);
		int sw = gui.STANDARD_WIDTH;	// normal tile width. aka: tile dim
		setBorder(new EmptyBorder(sw,sw,sw,sw));
		
		setLayout(new BorderLayout());
		((BorderLayout)getLayout()).setHgap(16);
		((BorderLayout)getLayout()).setVgap(16);
		/*JLabel title = new JLabel("Skills");
		title.setFont(UI_FONT);
		title.setForeground(GFXDisplay.GOLD);*/
		
		prompt = new JTextArea();
		prompt.setFont(gui.FNT_MESSAGEBOX);
		prompt.setOpaque(false);
		prompt.setForeground(Color.WHITE);
		prompt.setLineWrap(true);
		prompt.setWrapStyleWord(true);
		prompt.setEditable(false);
		prompt.setFocusable(false);
		//prompt.setVisible(false);
		
		JPanel pnlButtons = new JPanel();
		pnlButtons.add(lblGold);
		pnlButtons.add(btnBuy);
		pnlButtons.add(btnExit);
		pnlButtons.setOpaque(false);
		
		JPanel pnlSuperior = new JPanel(new BorderLayout());
		pnlSuperior.add(prompt, BorderLayout.CENTER);
		JPanel miniPanelBotones = new JPanel();
		miniPanelBotones.setOpaque(false);
		miniPanelBotones.add(btnYes);
		miniPanelBotones.add(btnNo);
		pnlSuperior.add(miniPanelBotones, BorderLayout.SOUTH);
		pnlSuperior.setOpaque(false);	
		
		add(pnlSuperior, BorderLayout.NORTH);
		add(lstMerchandise, BorderLayout.CENTER);
		add(pnlButtons, BorderLayout.SOUTH);
		
		setBackground(Color.BLACK);
		
		btnYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				doYes();
			}
		});
		
		btnNo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				doNo();
			}
		});
		
		btnBuy.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				doBuy();
			}
		});
		
		btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				doExit();
			}
		});
		
	}
	
	private void doBuy() {
		if (activeThread == null) {
			return;
		}
		//choice = (ShopMenuItem) lstMerchandise.getSelectedValue();
		choice = (Item) lstMerchandise.getSelectedValue();
		setPrompt("The "+choice.getDescription()+", "+choice.getShopDescription()+"; it costs "+choice.getGoldPrice()+", Do you want to buy it? (Y/n)");
		btnBuy.setEnabled(false);
		btnExit.setEnabled(false);
		lstMerchandise.setEnabled(false);
		btnYes.setVisible(true);
		btnNo.setVisible(true);
		requestFocus();
	}
	
	private void doExit() {
		if (activeThread != null) {
			choice = null;
			activeThread.interrupt();
		}
	}
	
	private void doYes() {
		activeThread.interrupt();
		btnBuy.setEnabled(true);
		btnExit.setEnabled(true);
		lstMerchandise.setEnabled(true);
		btnYes.setVisible(false);
		btnNo.setVisible(false);
		lstMerchandise.requestFocus();
	}
	
	private void doNo() {
		setPrompt("Too bad... May I interest you in something else?");
		btnBuy.setEnabled(true);
		btnExit.setEnabled(true);
		lstMerchandise.setEnabled(true);
		btnYes.setVisible(false);
		btnNo.setVisible(false);
		lstMerchandise.requestFocus();
	}
	
	public void setPrompt(String prompt) {
		this.prompt.setText(prompt);
	}
	
	public void setMerchandise(Vector skills) {
		((DefaultListModel)lstMerchandise.getModel()).removeAllElements();
		for (int i = 0; i < skills.size(); i++) {
			((DefaultListModel)lstMerchandise.getModel()).addElement(skills.elementAt(i));
		}
	}
	
	public void informChoice(Thread who) {
		choice = null;
		activeThread = who;
	}
	
	public Item getSelection() {
		return choice;
	}
	
	public void setGold(int gold) {
		lblGold.setText("Player gold: "+gold);	// inconsistent labelling! 
	}
	
	class MerchandiseCellRenderer extends DefaultListCellRenderer {
		private JLabel ren;
		
		public MerchandiseCellRenderer() {
			ren = new JLabel();
			ren.setFont(GFXUserInterface.FNT_MESSAGEBOX);
			ren.setOpaque(false);
			ren.setForeground(Color.WHITE);
			ren.setBackground(GFXDisplay.COLOR_BOLD);
		}
		
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			Item smi = (Item) value;
			ren.setIcon(new ImageIcon(((GFXAppearance)smi.getAppearance()).getIconImage()));
			//ren.setText(smi.getMenuDescription());
			ren.setText(smi.getAttributesDescription() + " ["+smi.getDefinition().menuDescription+"] ($"+smi.getGoldPrice()+")");
			ren.setOpaque(isSelected);
			return ren;
		}
		
		
	}
}

