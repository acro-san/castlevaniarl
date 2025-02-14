package sz.csi.textcomponents;

import sz.csi.CharKey;
import sz.csi.ConsoleSystemInterface;
import sz.util.*;
import java.util.*;
import crl.item.Item;
import crl.ui.consoleUI.AdditionalKeysSignal;

public class MenuBox extends TextComponent {
	
	private Vector items;
	private int promptSize;
	private String title = "";

	//State Attributes
	private int currentPage;
	private int pages;
	
	//Components
	private TextBox promptBox;
	
	
	public MenuBox(ConsoleSystemInterface si){
		super(si);
		promptBox = new TextBox(si);
	}
	
	public void setPosition(int x, int y){
		super.setPosition(x,y);
		promptBox.setPosition(inPosition.x,inPosition.y+1);
	}
	
	public void setPromptSize(int size){
		promptSize = size;
		promptBox.setHeight(size);
	}
	
	public void setWidth(int width){
		super.setWidth(width);
		promptBox.setWidth(inWidth);
		promptBox.setPosition(inPosition.x,inPosition.y);
	}
	
	public void setBorder(boolean val){
		super.setBorder(val);
		promptBox.setWidth(inWidth);
		promptBox.setPosition(inPosition.x,inPosition.y);
	}
	
	public void setPrompt(String prompt){
		promptBox.clear();
		promptBox.setText(prompt);
	}
	
	// FIXME Vector <> ??? something extends MenuItem or GfxMenuItem? Equipment? Item?
	public void setMenuItems(Vector items) {
		this.items = items;
	}

	public void draw(boolean ordinal){
		this.ordinal = ordinal;
		draw();
		this.ordinal = true;
	}
	private boolean ordinal = true;
	public void draw(){
		//pages = (int)(Math.floor((items.size()-1) / (inHeight-promptSize)) +1);
		pages = (int)(Math.floor((items.size()-1) / (double)(inHeight-promptSize)) +1);
		clearBox();
		drawBorder();
		if (hasBorder())
			si.print(position.x+2, position.y, title);
		//promptBox.clear();
		promptBox.draw();
		
		int pageElements = inHeight-promptSize;
		/*si.print(inPosition.x, inPosition.y-1+promptSize, "items.len"+items.size() + " PE "+pageElements+"CP"+currentPage+"pages"+pages);
		si.refresh();
		si.waitKey(CharKey.SPACE);*/
		Vector shownItems = Util.page(items, pageElements, currentPage);
		
		int i = 0;
		for (; i < shownItems.size(); i++){
			MenuItem item = (MenuItem) shownItems.elementAt(i);
			if (ordinal)
				si.print(inPosition.x, inPosition.y+i+promptSize, ((char) (97 + i))+"." );
			si.print(inPosition.x+2, inPosition.y+i+promptSize, item.getMenuChar(), item.getMenuColor());
			String description = item.getMenuDescription();
			if (description.length() > getWidth()-5){
				description = description.substring(0,getWidth()-6);
			}
			si.print(inPosition.x+4, inPosition.y+i+promptSize, description);
		}
		//si.print(inPosition.x, inPosition.y, inHeight+" "+pageElements+" "+pages);
		/*for (; i < inHeight-promptSize; i++){
			si.print(inPosition.x, inPosition.y+i+promptSize+1, spaces);
		}*/
		si.refresh();
	}

	public Object getSelection() {
		int pageElements = inHeight - promptSize;
		while (true){
			clearBox();
			draw();
			Vector<Item> shownItems = (Vector<Item>)Util.page(items, pageElements, currentPage);
			CharKey key = new CharKey(CharKey.NONE);
			while (key.code != CharKey.SPACE &&
				   key.code != CharKey.UARROW &&
				   key.code != CharKey.DARROW &&
				   (key.code < CharKey.A || key.code > CharKey.A + pageElements-1) &&
				   (key.code < CharKey.a || key.code > CharKey.a + pageElements-1)
				   )
			   key = si.inkey();
			if (key.code == CharKey.SPACE)
				return null;
			if (key.code == CharKey.UARROW)
				if (currentPage > 0)
					currentPage --;
			if (key.code == CharKey.DARROW)
				if (currentPage < pages-1)
					currentPage ++;
			
			if (key.code >= CharKey.A && key.code <= CharKey.A + shownItems.size()-1)
				return shownItems.elementAt(key.code - CharKey.A);
			else
			if (key.code >= CharKey.a && key.code <= CharKey.a + shownItems.size()-1)
				return shownItems.elementAt(key.code - CharKey.a);

		}
	}
	
	private boolean isOneOf(int value, int[] values){
		for (int i = 0; i < values.length; i++){
			if (value == values[i])
				return true;
		}
		return false;
	}
	
	public Object getNullifiedSelection (int[] keys) throws AdditionalKeysSignal{
		while (true){
			clearBox();
			draw(false);
			CharKey key = new CharKey(CharKey.NONE);
			while (key.code != CharKey.SPACE &&
				   key.code != CharKey.UARROW &&
				   key.code != CharKey.DARROW &&
				   !isOneOf(key.code, keys)
				   )
			   key = si.inkey();
			if (key.code == CharKey.SPACE)
				return null;
			if (key.code == CharKey.UARROW)
				if (currentPage > 0)
					currentPage --;
			if (key.code == CharKey.DARROW)
				if (currentPage < pages-1)
					currentPage ++;
			
			if (isOneOf(key.code, keys))
				throw new AdditionalKeysSignal(key.code);

		}
	}
	public Object getSelectionAKS (int[] keys) throws AdditionalKeysSignal{
		int pageElements = inHeight - promptSize;
		while (true){
			clearBox();
			draw();
			Vector shownItems = Util.page(items, pageElements, currentPage);
			CharKey key = new CharKey(CharKey.NONE);
			while (key.code != CharKey.SPACE &&
				   key.code != CharKey.UARROW &&
				   key.code != CharKey.DARROW &&
				   (key.code < CharKey.A || key.code > CharKey.A + pageElements-1) &&
				   (key.code < CharKey.a || key.code > CharKey.a + pageElements-1) &&
				   !isOneOf(key.code, keys)
				   )
			   key = si.inkey();
			if (key.code == CharKey.SPACE)
				return null;
			if (key.code == CharKey.UARROW)
				if (currentPage > 0)
					currentPage --;
			if (key.code == CharKey.DARROW)
				if (currentPage < pages-1)
					currentPage ++;
			
			if (key.code >= CharKey.A && key.code <= CharKey.A + shownItems.size()-1)
				return shownItems.elementAt(key.code - CharKey.A);
			else
			if (key.code >= CharKey.a && key.code <= CharKey.a + shownItems.size()-1)
				return shownItems.elementAt(key.code - CharKey.a);
			if (isOneOf(key.code, keys))
				throw new AdditionalKeysSignal(key.code);

		}
	}
	
	
	public void setTitle(String s){
		title = s;
	}
}