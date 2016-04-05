import javax.swing.*;
import java.awt.*;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import java.io.IOException;
import java.math.BigDecimal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class stockUI {
	
	public stockUI() {
	Stock stock = null;
	try {
		stock = YahooFinance.get("NOK");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 
    BigDecimal price = stock.getQuote().getPrice();
    
    String symbol = stock.getQuote().getSymbol();
    String hinta = price.toString();
 
    //stock.print();
  
	JFrame guiFrame = new JFrame();
    
    //make sure the program exits when the frame closes
    guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    guiFrame.setTitle("Rikastu helposti");
    guiFrame.setSize(300,250);
    guiFrame.setBackground(Color.YELLOW);
    
    //This will center the JFrame in the middle of the screen
    guiFrame.setLocationRelativeTo(null);
    
    //Options for the JList
    String[] priceBox = {hinta};
    
    //The first JPanel contains a JLabel and JTextField
    final JPanel comboPanel = new JPanel();
    comboPanel.setVisible(false);
    JLabel comboLbl = new JLabel(symbol);
    JTextField area = new JTextField(hinta);
    
    comboPanel.add(comboLbl);
    comboPanel.add(area);
    
    JButton stockBut = new JButton( "Hae osake");
    
    //The ActionListener class is used to handle the
    //event that happens when the user clicks the button.
    //As there is not a lot that needs to happen we can 
    //define an anonymous inner class to make the code simpler.
    stockBut.addActionListener(new ActionListener()
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
           //When the  button is pressed
           //the setVisible value of the 
           //comboPanel is switched from true to 
           //value or vice versa.
           comboPanel.setVisible(!comboPanel.isVisible());

        }
    });
    
    //The JFrame uses the BorderLayout layout manager.
    //Put the JPanel and JButton in different areas.
    guiFrame.add(comboPanel, BorderLayout.NORTH);
    guiFrame.add(stockBut,BorderLayout.SOUTH);
 
    //make sure the JFrame is visible
    guiFrame.setVisible(true);
}

}
