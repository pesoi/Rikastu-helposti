import javax.swing.*;
import java.awt.*;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class stockUI {
	
	//Stock real names
	static String[] names = new String[] {"Intel", "Ali Baba", "Tesla", "AirBus", "Yahoo","Nokia"};
   	
	//Stock symbols for retrieving
	static String[] symbols = new String[] {"INTC", "BABA", "TSLA", "AIR.PA", "YHOO", "NOK"};
	
	/**
     * Constructor for stockUI. Creates frame, adds two panels. First panel is then 
     * populated with: Stock info UI with combobox for stock selection, Textpane with three default stocks,
     * button for changing the panels. Second panel is populated with real time stock buy proposals.
     */
	
	public stockUI(String stock1, String stock2, String stock3) {
	
		JFrame uiFrame = createStockFrame();
		
		//The first JPanel
        final JPanel comboPanel = new JPanel();
        comboPanel.setVisible(true); //Show panel1
        comboPanel.setPreferredSize(new Dimension(200,150));
        uiFrame.add(comboPanel, BorderLayout.NORTH); //Add panel to frame
        
        //Create the second JPanel. JPanel is not visible.
    	final JPanel tipPanel = new JPanel();
        tipPanel.setVisible(false);
        tipPanel.setBackground(Color.WHITE);
        uiFrame.add(tipPanel, BorderLayout.CENTER); //Add panel to frame
        
    	addStockInfo(uiFrame, comboPanel); //Create info UI
    	
    	createTextPane(uiFrame, comboPanel, stock1, stock2, stock3); //Create default textpane
    	
    	createTips(tipPanel); //Create tips with stock proposals
    	
        createTipButton(uiFrame, comboPanel, tipPanel); //Create the tip button
        
        //make the frame visible
        uiFrame.setVisible(true);
	  
}

	/**
     * Creates the stock frame
     */
	
	   public static JFrame createStockFrame() 
	   {
		   
	       JFrame uiFrame = new JFrame();
	       
	       //make sure the program exits when the frame closes
	       uiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       uiFrame.setTitle("Get rich");
	       uiFrame.setSize(280,250);
	       uiFrame.setForeground(Color.DARK_GRAY);
	       
	       //Put the frame in the middle of the screen
	       uiFrame.setLocationRelativeTo(null);
	       
	       return uiFrame;
	   }
	   
	   /**
	     * Creates the stock info UI
	     */ 
	   
	    public static void addStockInfo(JFrame uiFrame, JPanel comboPanel )
		{
	    	
	        JComboBox stockList = new JComboBox(names); 
	        stockList.setAlignmentX(Component.RIGHT_ALIGNMENT);
	        stockList.setAlignmentY(Component.TOP_ALIGNMENT);
	  
	        JTextPane test = new JTextPane(); //New Textpane
	        test.setContentType("text/html"); //HTML content
	        test.setBounds(0, 0, 100, 100);
	        
	        Stock defaultStock = getOneStock(symbols[0]); //Gets the first stock to show in start
		    test.setText(initializeInfoWindow(defaultStock)); //Puts the first stock data
		    
	        comboPanel.add(stockList); //Adds components to panel
	        comboPanel.add(test);
	        
	        //Actionlistener created for selecting the item in combobox
	        stockList.addActionListener(new ActionListener()
    		{
	        	
	        	public void actionPerformed(ActionEvent event)
	    			{
    					int stockIndex = stockList.getSelectedIndex();

    					Stock selected = getOneStock(symbols[stockIndex]);
    					BigDecimal price2 = selected.getQuote().getPrice();
    					BigDecimal change = selected.getQuote().getChangeInPercent();
    					BigDecimal ask = selected.getQuote().getAsk();
    					BigDecimal bid = selected.getQuote().getBid();
    					long volume = selected.getQuote().getVolume();
    					StringBuilder details = new StringBuilder(); //Builds string for showing the text in textpane
    					details.append("<html>");
    					details.append("Last bid " + "<font color='green'>" + bid.toString() +"</font>" +" USD");
    				    details.append("<br>");
    				    details.append("Last ask " + "<font color='red'>" + ask.toString() +"</font>" +" USD");
    				    details.append("<br>");
    				    details.append("Last sales " + "<font color='cyan'>" + price2.toString() +"</font>" +" USD");
    				    details.append("<br>");
    				    details.append("Daily change " + "<font color='brown'>" + change.toString() +"</font>" +" %");
    				    details.append("<br>");
    				    details.append("Daily volume " + "<font color='blue'>" + Long.toString(volume) +"</font>");
    				    details.append("</html>");
    				    test.setText(details.toString());
	    			}
					
    		});
	        
		}
	    
	    /**
	     * Creates the tips listing
	     */
	    
	    public static void createTips(JPanel tipPanel)
	    {
	    	
	        JTextPane infos = new JTextPane();
	        infos.setContentType("text/html");
	        infos.setBackground(Color.CYAN);
	        
	        tipPanel.add(infos); //Adds textpane in panel2
	        
	        StringBuilder details = new StringBuilder();
	        details.append("<html><table border='1'><tr><th>Name</th><th>Price </th><th>Todays change %</th><th>Difference for 200 days</th><th>200 day average</th><th>EPS estimate next Q</th><th>One year target price</th><th>P/E ratio</tr>");
	        String [] stockList = calculateTips(); //gets the calculated proposals
	        Map <String,Stock> stocks = getMultipleStocks(stockList); //Gets all the stocks at once
	        for (int i=0; i<stockList.length-1;i++) //populates the table with right details
	        {
		        Stock stock = stocks.get(stockList[i]);
		        String name = stock.getName();
		        BigDecimal price = stock.getQuote().getPrice();
		        BigDecimal change = stock.getQuote().getChange();
		        BigDecimal avg200 = stock.getQuote().getChangeFromAvg200();
		        BigDecimal price200 = stock.getQuote().getPriceAvg200();
		        BigDecimal epsEst = stock.getStats().getEpsEstimateNextQuarter();
		        BigDecimal targetPrice = stock.getStats().getOneYearTargetPrice();
		        BigDecimal pe = stock.getStats().getPe();
				details.append("<tr><td>" +name +"</td><td>" +price +"</td><td>" +change +"</td><td>" +avg200 +"</td><td>" +price200 +"</td><td>" +epsEst +"</td><td>" +targetPrice +"</td><td>" +pe +"</td></tr>");
	        }
			details.append("</table></html>");
			infos.setText(details.toString());
	       
	    }
	    
	    /**
	     * Creates the tips button and adds actionlistener for clicking on it
	     */
	    
	    public static void createTipButton(JFrame uiFrame, JPanel tipPanel, JPanel comboPanel)
	    {
	    	JButton stockBut = new JButton( "TIPS");
	  
	        stockBut.addActionListener(new ActionListener()
	        {
	            @Override
	            public void actionPerformed(ActionEvent event)
	            {
	               //When the  button is pressed
	               //the setVisible value of the 
	               //comboPanel is switched from true to 
	               //value or vice versa.
	            	tipPanel.setVisible(!tipPanel.isVisible());
	                comboPanel.setVisible(!comboPanel.isVisible());
	                if(!tipPanel.isVisible()) //Change the size when swapping panels
	                {
	                	uiFrame.setSize(1100,350);
	                	//Put the frame in the middle of the screen
	         	       	uiFrame.setLocationRelativeTo(null);
	                }
	                else
	                {
	                	uiFrame.setSize(280,250);
	                	//Put the frame in the middle of the screen
	         	       	uiFrame.setLocationRelativeTo(null);
	                }

	            }
	        });
	        
	        uiFrame.add(stockBut, BorderLayout.EAST);
	    }
	    
	    /**
	     * Method for retrieving one stock
	     */

		public static Stock getOneStock(String stock)
	    {
	    	Stock one = null;
			try {
				one = YahooFinance.get(stock);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return one;
	    }
		
		/**
	     * Method for getting multiple stocks at once
	     */
		
	    public static Map<String, Stock> getMultipleStocks (String [] symbols)
	    {
	    	Map<String, Stock> stocks = null; //Map the stock & string
			try {
				stocks = YahooFinance.get(symbols);
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			return stocks;
	    }
	    
	    /**
	     * Creates the info UI
	     */
	    
	    public static String initializeInfoWindow(Stock s)
	    {
	    	BigDecimal price2 = s.getQuote().getPrice();
			BigDecimal change = s.getQuote().getChangeInPercent();
			BigDecimal ask = s.getQuote().getAsk();
			BigDecimal bid = s.getQuote().getBid();
			long volume = s.getQuote().getVolume();
			StringBuilder details = new StringBuilder(); //Build the string for showing the details
			details.append("<html>");
			details.append("Last bid " + "<font color='green'>" + bid.toString() +"</font>" +" USD");
		    details.append("<br>");
		    details.append("Last ask " + "<font color='red'>" + ask.toString() +"</font>" +" USD");
		    details.append("<br>");
		    details.append("Last sales " + "<font color='cyan'>" + price2.toString() +"</font>" +" USD");
		    details.append("<br>");
		    details.append("Daily change " + "<font color='brown'>" + change.toString() +"</font>" +" %");
		    details.append("<br>");
		    details.append("Daily volume " + "<font color='blue'>" + Long.toString(volume) +"</font>");
		    details.append("</html>");
		    return details.toString();
	    }
	    
	    /**
	     * Calculates the stock proposals
	     */
	    
	    public static String [] calculateTips()
	    {
	    	String [] multiple = new String[] {"INTC", "BABA", "CSCO", "FOXA", "MU", "NOK", "MAR", "WMT", "F", "FB"};
	    	Map<String, Stock> many = getMultipleStocks(multiple);
	    	int size = multiple.length;
	    	for (int i=0; i<size-1;i++) //Gets the stock details and then arranges with bubble sort
	    	{
	    		for (int j=size-1;j>i;j--)
	    		{
	    		Stock last = many.get(multiple[j]);
	    		Stock previous = many.get(multiple[j-1]);
	    		BigDecimal lastPe = last.getStats().getPe();
	    		BigDecimal previousPe = previous.getStats().getPe();
	    		if (previousPe.compareTo(lastPe) == 1)
	    		{
	    			String tmp = multiple[j-1];
	    			multiple[j-1] = multiple[j];
	    			multiple[j] = tmp;
	    		}
	    		}
	    		
	    	}
	    	
	    	return multiple;
	    }
	    
	    
	    /**
	      * Creates a JTextPane with information on three stocks
	      * @param symbol1 Stock symbol
	      * @param symbol2 Stock symbol
	      * @param symbol3 Stock symbol
	      */
	     public void createTextPane( JFrame uiFrame, JPanel panel, String symbol1, String symbol2, String symbol3 ) {
	            
	            Stock stock1 = null;
	            Stock stock2 = null;
	            Stock stock3 = null;
	            try {
	             stock1 = YahooFinance.get(symbol1);
	                stock2 = YahooFinance.get(symbol2);
	                stock3 = YahooFinance.get(symbol3);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            
	            //the price and change of the stock are saved into variables price<number> and change<number>
	            BigDecimal price1 = stock1.getQuote().getPrice();
	            BigDecimal change1 = stock1.getQuote().getChangeInPercent();
	            BigDecimal price2 = stock2.getQuote().getPrice();
	            BigDecimal change2 = stock2.getQuote().getChangeInPercent();
	            BigDecimal price3 = stock3.getQuote().getPrice();
	            BigDecimal change3 = stock3.getQuote().getChangeInPercent();
	            
	            //the text that will be added to the text pane
	            StringBuffer text = new StringBuffer("<html>");
	            
	            /*if the change is positive, stock information is coloured green
	            if negative, colour is red*/
	            //STOCK1
	            if ( change1.compareTo(BigDecimal.ZERO) == 1 ) {
	             text.append("<font color='green'>Nokia "+price1+" USD,  +"+change1+" %"+"</font>");
	            }
	            else {
	             text.append("<font color='red'>Nokia "+price1+" USD,  "+change1+" %</font>");
	            }
	            text.append("<br>");
	            
	            //STOCK2
	            if ( change2.compareTo(BigDecimal.ZERO) == 1 ) {
	             text.append("<font color='green'>Yahoo "+price2+" USD,  +"+change2+" %</font>");
	            }
	            else {
	             text.append("<font color='red'>Yahoo "+price2+" USD,  "+change2+" %</font>");
	            }
	            text.append("<br>");
	            
	            //STOCK3
	            if ( change3.compareTo(BigDecimal.ZERO) == 1 ) {
	             text.append("<font color='green'>Google "+price3+" USD,  +"+change3+" %</font>");
	            }
	            else {
	             text.append("<font color='red'>Google "+price3+" USD,  "+change3+" %</font>");
	            }
	            text.append("</html>");
	      
	            
	            JTextPane textPane = new JTextPane(); //text pane is created
	            textPane.setContentType("text/html"); //text type is set html for the colours
	            textPane.setText(text.toString()); //text is added
	            textPane.setEditable(false);
	      
	            panel.add(textPane);
	      
	            uiFrame.add(panel, BorderLayout.WEST);

	     }


	    
	}
