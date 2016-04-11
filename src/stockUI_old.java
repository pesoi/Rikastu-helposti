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
	
	static String[] names = new String[] {"Intel", "Ali Baba", "Tesla", "AirBus", "Yahoo","Nokia"};
   	
	static String[] symbols = new String[] {"INTC", "BABA", "TSLA", "AIR.PA", "YHOO", "NOK"};
	
	public stockUI() {
	
		JFrame uiFrame = createStockFrame();
		//make sure the JFrame is visible
	    uiFrame.setVisible(true);
		
		//The first JPanel contains a JLabel and JTextField
        final JPanel comboPanel = new JPanel();
        comboPanel.setVisible(true);
        comboPanel.setPreferredSize(new Dimension(150,150));
        //comboPanel.setBackground(Color.WHITE);
        uiFrame.add(comboPanel, BorderLayout.NORTH);
        
        //Create the second JPanel. Add a JLabel and JList and
        //make use the JPanel is not visible.
    	final JPanel tipPanel = new JPanel();
        tipPanel.setVisible(false);
        tipPanel.setBackground(Color.WHITE);
        uiFrame.add(tipPanel, BorderLayout.CENTER);
        
    	addStockInfo(uiFrame, comboPanel);
    	
    	luoTekstiruutu(uiFrame, comboPanel);
    	
    	createTips(uiFrame, tipPanel);
    	
        createTipButton(uiFrame, comboPanel, tipPanel);
	  
}

	   public static JFrame createStockFrame() 
	   {
		   
	       JFrame uiFrame = new JFrame();
	       
	       //make surethe program exits when the frame closes
	       uiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       uiFrame.setTitle("Rikastu helposti");
	       uiFrame.setSize(250,250);
	       uiFrame.setForeground(Color.DARK_GRAY);
	       
	       //This will center the JFrame in the middle of the screen
	       uiFrame.setLocationRelativeTo(null);
	       
	       return uiFrame;
	   }
	    
	    public static void addStockInfo(JFrame uiFrame, JPanel comboPanel )
		{
	    	
	        JComboBox stockList = new JComboBox(names);
	        stockList.setAlignmentX(Component.RIGHT_ALIGNMENT);
	        stockList.setAlignmentY(Component.TOP_ALIGNMENT);
	  
	        JTextPane test = new JTextPane();
	        test.setContentType("text/html");
	        test.setBounds(0, 0, 100, 100);
	        
	        Stock defaultStock = getOneStock(symbols[0]);
		    test.setText(initializeInfoWindow(defaultStock));
		    
	        comboPanel.add(stockList);
	        comboPanel.add(test);
	        
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
    					StringBuilder details = new StringBuilder();
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
	    
	    public static void createTips(JFrame uiFrame, JPanel tipPanel)
	    {
	    	
	        //JLabel tipLbl = new JLabel("OSTA");
	        JTextPane infos = new JTextPane();
	        infos.setContentType("text/html");
	        //infos.setBounds(20, 50, 100, 200);
	        infos.setBackground(Color.CYAN);
	        
	        //tipPanel.add(tipLbl);
	        tipPanel.add(infos);
	        
	        StringBuilder details = new StringBuilder();
	        details.append("<html><table border='1'><tr><th>Name</th><th>Price </th><th>Todays change %</th><th>Difference for 200 days</th><th>200 day average</th><th>EPS estimate next Q</th><th>One year target price</th><th>P/E ratio</tr>");
	        String [] stockList = calculateTips();
	        Map <String,Stock> stocks = getMultipleStocks(stockList);
	        for (int i=0; i<stockList.length-1;i++)
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
	    
	    public static void createTipButton(JFrame uiFrame, JPanel tipPanel, JPanel comboPanel)
	    {
	    	JButton stockBut = new JButton( "Vinkit");
	    	   
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
	            	tipPanel.setVisible(!tipPanel.isVisible());
	                comboPanel.setVisible(!comboPanel.isVisible());
	                if(!tipPanel.isVisible())
	                {
	                	uiFrame.setSize(1100,350);
	                }
	                else
	                {
	                	uiFrame.setSize(250,250);
	                }

	            }
	        });
	        
	        uiFrame.add(stockBut, BorderLayout.EAST);
	    }

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
	    
	    public static Map<String, Stock> getMultipleStocks (String [] symbols)
	    {
	    	Map<String, Stock> stocks = null;
			try {
				stocks = YahooFinance.get(symbols);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // single request
			
			return stocks;
	    }
	    
	    public static String initializeInfoWindow(Stock s)
	    {
	    	BigDecimal price2 = s.getQuote().getPrice();
			BigDecimal change = s.getQuote().getChangeInPercent();
			BigDecimal ask = s.getQuote().getAsk();
			BigDecimal bid = s.getQuote().getBid();
			long volume = s.getQuote().getVolume();
			StringBuilder details = new StringBuilder();
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
	    
	    public static String [] calculateTips()
	    {
	    	String [] multiple = new String[] {"INTC", "BABA", "CSCO", "FOXA", "MU", "NOK", "MAR", "WMT", "F", "FB"};
	    	Map<String, Stock> many = getMultipleStocks(multiple);
	    	int size = multiple.length;
	    	for (int i=0; i<size-1;i++)
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
	    
	    public void luoTekstiruutu( JFrame guiFrame, JPanel panel ) {
            
            Stock nokia = null;
            Stock yahoo = null;
            Stock google = null;
            try {
                      nokia = YahooFinance.get("NOK");
                      yahoo = YahooFinance.get("YHOO");
                      google = YahooFinance.get("GOOG");
            } catch (IOException e) {
                      e.printStackTrace();
            }
            
            BigDecimal nokiaPrice = nokia.getQuote().getPrice();
            BigDecimal nokiaChange = nokia.getQuote().getChangeInPercent();
            BigDecimal yahooPrice = yahoo.getQuote().getPrice();
            BigDecimal yahooChange = yahoo.getQuote().getChangeInPercent();
            BigDecimal googlePrice = google.getQuote().getPrice();
            BigDecimal googleChange = google.getQuote().getChangeInPercent();
            
            
            //final JPanel panel = new JPanel();
            //panel.setVisible(true);
      
            JTextPane tekstiruutu = new JTextPane();
            tekstiruutu.setText("Nokia  "+nokiaPrice+" USD  "+nokiaChange+"\nYahoo  "+yahooPrice
                      +" USD  "+yahooChange+"\nGoogle  "+googlePrice+" USD  "+googleChange);
            tekstiruutu.setEditable(false);
      
            panel.add(tekstiruutu);
      
            guiFrame.add(panel, BorderLayout.WEST);

	    }

	    
	}
