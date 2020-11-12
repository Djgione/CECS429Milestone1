package main;

import cecs429.documents.Document;
import cecs429.documents.DocumentCorpus;
import cecs429.index.CheckForSpellings;
import cecs429.index.DiskInvertedIndex;
import cecs429.index.DiskKgramIndex;
import cecs429.index.Posting;
import cecs429.index.SpellingCorrector;

import java.awt.*;

import cecs429.indexer.DiskIndexWriter;
import cecs429.indexer.Indexer;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.tartarus.snowball.SnowballStemmer;



public class MyGUIProgram extends Frame{
        String queryOption = ""; 
        String corpType = "json";
        String milestone;
        String boolOrRankedOption= "";
        Boolean queryDiskIndex = false;
         Indexer indexer = null;
        DiskIndexWriter dW = null;
        DiskInvertedIndex di = null;
        DiskKgramIndex dki = null;
        SpellingCorrector sp = null;
        Frame f;
        Frame q;
        Frame e;
        Frame d;
        Frame c;
        Label label;
        Label label2 = new Label();
        Label buildQuery_label = new Label();
        Label boolOrRank = new Label();
        Button buildIndexButton;
        Button buildOrQuery_button = new Button("GO");
        TextField textFieldPath;
        TextField querySearchField;
        TextField buildOrQuery_textField;
        Button queryButton = new Button("Query");
        //Path corpusPath = Paths.get("C:\\Users\\Kermite\\eclipse-workspace\\CECS429Milestone2\\src\\corpus\\index").toAbsolutePath();    
        List<Integer> docId;
	    CheckForSpellings cfs; 
	    String corrections = null;
	    String ogQuery;
        JTable jt;    

        public void BuildorQueryIndex()
        {
        	 e = new Frame("Milestone 2");
        	
        	 CheckboxGroup initCbg = new CheckboxGroup();  
             Checkbox checkbox1 = new Checkbox("Build Index", initCbg,false);  
             checkbox1.setBounds(100,100, 50,50);  
             Checkbox checkbox2 = new Checkbox("Query Index", initCbg,false);  
             checkbox2.setBounds(100,150, 50,50);  
             textFieldPath = new TextField("Enter Corpus Path");
             textFieldPath.setBounds(50,20, 200,30);  
             textFieldPath.setLocation(150, 150);
             checkbox1.addItemListener((ItemEvent e) -> {               
            	 buildQuery_label.setText("Build new index");
            	 queryDiskIndex = false;

                 textFieldPath.setVisible(false);
             });  
             checkbox2.addItemListener((ItemEvent e) -> {
            	 buildQuery_label.setText("Query existing index on disk");
            	 queryDiskIndex = true;
            	 textFieldPath.setVisible(true);
             });
             
             e.add(checkbox1,BorderLayout.NORTH); 
             e.add(checkbox2,BorderLayout.NORTH); 
             e.add(textFieldPath,BorderLayout.SOUTH);
             e.add(buildOrQuery_button,BorderLayout.AFTER_LAST_LINE);
             e.setSize(400,400);  
             e.setLayout(new BoxLayout(e, BoxLayout.Y_AXIS));
             e.setVisible(true); 
             
             buildOrQuery_button.addMouseListener(new MouseAdapter() 
             {
            	 int count = 0;
            	 @Override
            	 public void mousePressed(MouseEvent me) {
            		 e.setVisible(false);
            		 e.setEnabled(false);
            		 if(indexer == null && queryDiskIndex && Files.exists(Paths.get(textFieldPath.getText() + "\\postings.bin").toAbsolutePath()))
            		 {
            			 count++;
            			 
            			 try {
            				 indexer = new Indexer(Paths.get(textFieldPath.getText()).toAbsolutePath(), corpType);
							
						 	 di = new DiskInvertedIndex(textFieldPath.getText());
		                        
	                        dki=new DiskKgramIndex(textFieldPath.getText());
	                        indexer.setDiskKgram(dki);
	                        indexer.setDiskIndex(di);
							 sp= new SpellingCorrector(dki,di);
							 cfs = new CheckForSpellings(di,dki);
							 System.out.println(count);
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
            			 BooleanOrRankedQuery();
            		 }
            		 else
            		 {
            			 if(indexer == null)
            				 BuildIndex();           		     
            		 }
            		 
            	 }
             });
             
        }
        
        public void BooleanOrRankedQuery()
        {
        	d = new Frame();
        	label = new Label("Choose a query type");          
            label.setAlignment(Label.CENTER);  
            label.setSize(400,100);  
            CheckboxGroup initCbg = new CheckboxGroup();  
            Checkbox checkbox1 = new Checkbox("Boolean", initCbg,false);  
            checkbox1.setBounds(100,100, 50,50);  
            Checkbox checkbox2 = new Checkbox("Ranked", initCbg,false);  
            checkbox2.setBounds(100,150, 50,50);  
            
            checkbox1.addItemListener((ItemEvent e) -> {               
                label.setText("Perform boolean querys");
                boolOrRankedOption = "boolean";
            });  
            checkbox2.addItemListener((ItemEvent e) -> {
                label.setText("Perform ranked querys");
                boolOrRankedOption = "ranked";
            });
            d.add(checkbox1,BorderLayout.NORTH); 
            d.add(checkbox2,BorderLayout.NORTH); 
            d.add(label);
            d.add(buildOrQuery_button,BorderLayout.AFTER_LAST_LINE);
            d.setSize(400,400);  
            d.setLayout(new BoxLayout(d, BoxLayout.Y_AXIS));
            d.setVisible(true); 
            
            buildOrQuery_button.addMouseListener(new MouseAdapter() 
            {
            	
	           	 @Override
	           	 public void mousePressed(MouseEvent me) {	           		 
	    
	           		 d.setVisible(false);
	           		 if(boolOrRankedOption.equals("boolean"))
	           		 {
	           			 QueryFrame();
	           		 }
	           		 else if(boolOrRankedOption.equals("ranked"))	           			
	           		 {
	           			RankedQueryFrame();
	           		 }
	           		 
	           	 }
            });
        }
        
        public void BuildIndex()
        {
        	
            f= new Frame("Build Index");
            
            label = new Label();          
            label.setAlignment(Label.CENTER);  
            label.setSize(400,100);  
            CheckboxGroup initCbg = new CheckboxGroup();  
            Checkbox checkbox1 = new Checkbox("json", initCbg,false);  
            checkbox1.setBounds(100,100, 50,50);  
            Checkbox checkbox2 = new Checkbox("txt", initCbg,false);  
            checkbox2.setBounds(100,150, 50,50);  
            
            checkbox1.addItemListener((ItemEvent e) -> {               
                label.setText("Create json directory corpus");
                corpType = "json";
            });  
            checkbox2.addItemListener((ItemEvent e) -> {
                label.setText("Create txt directory corpus");
                corpType = "txt";
            });
            textFieldPath = new TextField("Enter Directory Path");
            textFieldPath.setBounds(50,20, 200,30);  
            textFieldPath.setLocation(150, 150);
            buildIndexButton = new Button("Build Index");
            buildIndexButton.setBounds(100,120,80,30);  
            f.add(checkbox1,BorderLayout.NORTH); 
            f.add(checkbox2,BorderLayout.NORTH); 
            f.add(label);  
            f.add(textFieldPath,BorderLayout.SOUTH);
            f.add(buildIndexButton,BorderLayout.AFTER_LAST_LINE);
            f.setSize(400,400);  
            f.setLayout(new BoxLayout(f, BoxLayout.Y_AXIS));
            f.setVisible(true); 
            
            
            if(indexer == null)
            {
            	  if(Files.exists(Paths.get(textFieldPath.getText() + "\\theDB").toAbsolutePath()) 
       		      && Files.exists(Paths.get(textFieldPath.getText() + "\\postings.bin").toAbsolutePath()))
     			  {
     				  di.closeandDeleteDB(textFieldPath.getText());
     		          try {
   			 		  dW.DeleteBinFiles(textFieldPath.getText());
   			 		  dki.closeandDeleteDB(textFieldPath.getText());
   			 		  dW.DeleteKgramBinFiles(textFieldPath.getText());
	   			 	  } catch (FileNotFoundException e1) {
	   			 		  // TODO Auto-generated catch block
	   			 		  e1.printStackTrace();
	   			 	  } catch (IOException e1) {
	   			 		  // TODO Auto-generated catch block
	   			 		  e1.printStackTrace();
	   			  	  }
     			  }
                buildIndexButton.addMouseListener(new MouseAdapter() 
                { 
                   @Override
                   public void mousePressed(MouseEvent me) { 
                       f.setVisible(false);
                     
                       long st = System.currentTimeMillis();
                
                      
                       indexer = new Indexer(Paths.get(textFieldPath.getText()).toAbsolutePath(),corpType);
				
                       //corpusPath = Paths.get(textFieldPath.getText()).toAbsolutePath();
                       
                       long et = System.currentTimeMillis();
                       
                       long indexTime = (et - st);
                       label2 = new Label("Corpus Indexed in : " + indexTime + "ms");
                       dW = new DiskIndexWriter(textFieldPath.getText());
                       dW.writeIndex(indexer.getIndex(), Paths.get(textFieldPath.getText()).toAbsolutePath());
                       try {
                        dW.writeKgramIndex(indexer.getKgramIndex(), Paths.get(textFieldPath.getText()).toAbsolutePath());
                        indexer.delKgram();
                        indexer.delIndex();
                        di = new DiskInvertedIndex(textFieldPath.getText());
                        
                        dki=new DiskKgramIndex(textFieldPath.getText());
                        indexer.setDiskKgram(dki);
                        indexer.setDiskIndex(di);

                        sp= new SpellingCorrector(dki,di);
						cfs = new CheckForSpellings(di,dki);

                       }
                       catch(Exception e)
                       {
                    	   e.printStackTrace();
                       }
                       BooleanOrRankedQuery();
                   }  
                });
            }
        }
        
        public void RankedQueryFrame()
        {
        	c = new Frame();
            String column[] = new String[1];
            Label spc_label = new Label("Type Ranked Query:");
            Checkbox correctSpelling = new Checkbox("Spelling Corrections"); 
            Checkbox checkbox = new Checkbox("Replace Query");
            querySearchField = new TextField("");
            querySearchField.setBounds(getBounds());
            c.add(spc_label);
            c.add(querySearchField);
            c.add(queryButton);
            c.setSize(500,80);  
            
            GroupLayout layout = new GroupLayout(c);
            c.setLayout(layout);
            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);

            layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(spc_label)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(querySearchField))
                            
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(queryButton))

            );

            layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(spc_label)
                    .addComponent(querySearchField)
                    .addComponent(queryButton))
            );
            c.setVisible(true);
            Button requery = new Button();
            c.add(requery);
           
            queryButton.addMouseListener(new MouseAdapter() {
            	@Override
                public void mousePressed(MouseEvent me) {
            		c.setVisible(false);
            		try {
						corrections = cfs.suggest(querySearchField.getText());
						requery.setLabel("Requery with corrections: " + "\""+ corrections +"\"");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		List<String> rankedResults = indexer.rankedQuery(querySearchField.getText());
            		String data[][] = new String[rankedResults.size()][1];
            		int i = 0;
                	for(String p : rankedResults)
                    {
                        data[i][0] = p;
                
                        i++;
                    }
                	column[0] = "TITLE | ACCUMULATOR";
                	
                	 Frame jf2 =new Frame();
                	 JTable jt=new JTable(data,column);    
                     jt.setBounds(30,40,200,300);  
                     JScrollPane sp=new JScrollPane(jt);    
                     Button back = new Button("Back");
                     
                     jt.setRowSelectionAllowed(true);
                     jt.setEnabled(false);
                     jf2.add(sp);  
                     jf2.add(requery, BorderLayout.BEFORE_FIRST_LINE);
                     jf2.add(back,BorderLayout.AFTER_LAST_LINE);
                     jf2.setSize(300,400);    
                     jf2.setVisible(true);
                     
                     back.addMouseListener(new MouseAdapter() {
                    	 @Override
                         public void mousePressed(MouseEvent me)
                         {
                    		 jf2.setVisible(false);
                    		 BooleanOrRankedQuery();
                         }
                     });
                     
                     requery.addMouseListener(new MouseAdapter() {
                    	 @Override
                         public void mousePressed(MouseEvent me)
                         {
                    		jf2.setVisible(false);
                     		List<String> rankedResults = indexer.rankedQuery(corrections);
                     		String data[][] = new String[rankedResults.size()][1];
                    		int i = 0;
                        	for(String p : rankedResults)
                            {
                                data[i][0] = p;
                        
                                i++;
                            }
                            String column[] = new String[1];
                        	column[0] = "TITLE | ACCUMULATOR";
                       	 	Frame jf3 =new Frame();

                        	 JTable jt=new JTable(data,column); 
                        	 jt.setBounds(30,40,200,300);  
                             JScrollPane sp=new JScrollPane(jt);    
                             Button back = new Button("Back");
                             jt.setRowSelectionAllowed(true);
                             jt.setEnabled(false);
                             jf3.add(sp);  
                             jf3.add(back, BorderLayout.AFTER_LAST_LINE);
                             jf3.setSize(300,400);    
                             jf3.setVisible(true);
                             
                             back.addMouseListener(new MouseAdapter() {
                            	 @Override
                                 public void mousePressed(MouseEvent me)
                                 {
                            		 jf3.setVisible(false);
                            		 BooleanOrRankedQuery();
                                 }
                             });
                    		
                         }
                     });
            	}
            });

        	
        }
        
        public void QueryFrame()
        {
            q = new Frame();        
            label.setAlignment(Label.CENTER); 
            CheckboxGroup queryCbg = new CheckboxGroup();  
            Checkbox checkbox1 = new Checkbox(":q",queryCbg, false);  
            checkbox1.setBounds(50,100, 25,25);  
            Checkbox checkbox2 = new Checkbox(":stem",queryCbg, false);  
            checkbox2.setBounds(50,105, 25,25);  
            Checkbox checkbox3 = new Checkbox(":index",queryCbg, false);  
            checkbox3.setBounds(50,110, 25,25);  
            Checkbox checkbox4 = new Checkbox(":vocab",queryCbg, false);  
            checkbox4.setBounds(50,115, 25,25);  
            Checkbox checkbox5 = new Checkbox("srch",queryCbg, false);  
            checkbox5.setBounds(50,120, 25,50); 

             checkbox1.addItemListener(new ItemListener() {  
             public void itemStateChanged(ItemEvent e) {               
                label2.setText("Perform Query Option : \':q\'");
                queryOption = ":q";
             }  
            });  
            checkbox2.addItemListener(new ItemListener() {  
                public void itemStateChanged(ItemEvent e) {               
                   label2.setText("Perform Query Option : \':stem\'");
                queryOption = ":stem";
                }  
             });
            checkbox3.addItemListener(new ItemListener() {  
                public void itemStateChanged(ItemEvent e) {               
                   label2.setText("Perform Query Option : \':index\'");
                queryOption = ":index";
                }  
             });
            checkbox4.addItemListener(new ItemListener() {  
                public void itemStateChanged(ItemEvent e) {               
                   label2.setText("Perform Query Option : \':vocab\'");
                queryOption = ":vocab";
                }  
             });
            checkbox5.addItemListener(new ItemListener() {  
                public void itemStateChanged(ItemEvent e) {               
                   label2.setText("Perform Query Option : \'srch\'");
                   queryOption = "srch";
                }  
             });
            querySearchField = new TextField("Enter Query Search");
            querySearchField.setBounds(50,20, 200,30);  
            querySearchField.setLocation(150, 150);
            
            queryButton.setBounds(100,120,80,30);  
            q.add(checkbox1,BorderLayout.NORTH); 
            q.add(checkbox2,BorderLayout.NORTH); 
            q.add(checkbox3,BorderLayout.NORTH); 
            q.add(checkbox4,BorderLayout.NORTH); 
            q.add(checkbox5,BorderLayout.NORTH); 
            q.add(querySearchField);
            q.add(label2);
            q.add(queryButton);
            q.setSize(600,600);  

            q.setLayout(new BoxLayout(q, BoxLayout.Y_AXIS));

            q.setVisible(true);
            queryButton.addMouseListener(new MouseAdapter(){
                @Override
                public void mousePressed(MouseEvent me) 
                {
                    if(queryOption.equals(":q")){
                        q.setVisible(false);
                        BooleanOrRankedQuery();
                    }
                    else if(queryOption.equals(":stem"))
                    {
                        try {
                            Class<?>  stemClass = Class.forName("org.tartarus.snowball.ext." + "english" + "Stemmer");
                            SnowballStemmer stemmer = (SnowballStemmer) stemClass.newInstance();

                           stemmer.setCurrent(querySearchField.getText());
                           stemmer.stem();
                           String stemmed = stemmer.getCurrent();
                           JOptionPane.showInputDialog(querySearchField.getText() + " -> " + stemmed, null);
                        } catch (ClassNotFoundException ex) {
                           Logger.getLogger(MyGUIProgram.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InstantiationException ex) {
                           Logger.getLogger(MyGUIProgram.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                           Logger.getLogger(MyGUIProgram.class.getName()).log(Level.SEVERE, null, ex);
                        }
                   }
                   else if(queryOption.equals(":index")){
                       textFieldPath.setText(querySearchField.getText());
                       q.setVisible(false);
                       f.setVisible(true);
                       indexer = null;
                       di.closeandDeleteDB(textFieldPath.getText());
	       		        try {
					 		dW.DeleteBinFiles(textFieldPath.getText());
					    } catch (FileNotFoundException e1) {
					 		// TODO Auto-generated catch block
					 		e1.printStackTrace();
					 	} catch (IOException e1) {
					 		// TODO Auto-generated catch block
					 		e1.printStackTrace();
					  	}
                       
                   }
                   else if(queryOption.equals(":vocab"))
                   {
                       q.setVisible(false);
                       Frame jf =new Frame();
                       List<String> vocab = indexer.getVocab1000();
                       String data[][]= new String[vocab.size()][1];

                       for(int i = 0; i < vocab.size(); i++)
                       {
                           data[i][0] = vocab.get(i);
                       }
                       String column[]={"TITLE"};         
                       JTable jt=new JTable(data,column);    
                       jt.setBounds(30,40,200,300);          
                       JScrollPane sp=new JScrollPane(jt);    
                       Button back = new Button("Back");
                       jf.add(sp);  
                       jf.add(back, BorderLayout.AFTER_LAST_LINE);
                       jf.setSize(300,400);    

                       jf.setVisible(true); 
                       back.addMouseListener(new MouseAdapter() 
                       {
                           public void mousePressed(MouseEvent e){
                               jf.setVisible(false);
                               q.setVisible(true);
                               //QueryFrame();
                           }
                        });
                }
                else if(queryOption.equals("srch"))
                {
                        Button requery = new Button();
						try {
							corrections = cfs.suggest(querySearchField.getText());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						requery.setLabel("Requery with correct spelling: " + "\"" +corrections + "\"");
                        q.setVisible(false);
                        Frame jf2 =new Frame();
                        DocumentCorpus dc = indexer.getCorpus();
                        List<Posting> queryResults = new ArrayList<>();
                        
                        String data[][] = null;
                        String column[] = new String[1];
                        int i = 0;
                       
                    	queryResults = indexer.query(querySearchField.getText());
                    	
                    	
                    	data = new String[queryResults.size()][1];
                    	
                    	for(Posting p : queryResults)
                        {
                            data[i][0] = p.getDocumentId() +" "+dc.getDocument(p.getDocumentId()).getTitle();
                    
                            i++;
                        }
                    	column[0] = "Number of Results: " + queryResults.size(); 

   
                        jt=new JTable(data,column);    
                        jt.setBounds(30,40,200,300);  
                        JScrollPane sp=new JScrollPane(jt);    
                        Button back = new Button("Back");
                        jt.setRowSelectionAllowed(true);
                        jt.setEnabled(false);
                        jf2.add(sp);  
                        jf2.add(back, BorderLayout.AFTER_LAST_LINE);
                        jf2.add(requery, BorderLayout.BEFORE_FIRST_LINE);
                        jf2.setSize(300,400);    
                        jf2.setVisible(true);
                        
                        jt.addMouseListener(new MouseAdapter() {
                            public void mouseClicked(MouseEvent me)
                            {
                                    System.out.println("clicked item");
                                    JTable target = (JTable)me.getSource();
                                    Point p = me.getPoint();
                                    int row = target.rowAtPoint(p); // select a row
                                    int column = target.columnAtPoint(p); // select a column
                                
                                    jt.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
                                    if(column == 0 && me.getClickCount() == 2)
                                    {                           
                                    	System.out.println("row: " + row +" col: " + column);
                            
                                    	String str = target.getValueAt(row, column).toString();
                                    	System.out.println(str);
                                    	str=str.split(" ")[0];
                              
                                    	System.out.println("clicked twice");
                                    	JOptionPane op = new JOptionPane();
                                    	JTextArea jta = new JTextArea(dc.getDocument(Integer.parseInt(str)).getBody());
                                    	JScrollPane jsp = new JScrollPane(jta){
		                                    @Override
		                                    public Dimension getPreferredSize() {
		                                        return new Dimension(480, 320);
		                                    }
                                    	};
                                    	JOptionPane.showMessageDialog( null, jsp, "Content", JOptionPane.PLAIN_MESSAGE);                                  
                                }
                            }
                        });
                        back.addMouseListener(new MouseAdapter() 
                        {
                            @Override
                            public void mousePressed(MouseEvent e){
                                jf2.setVisible(false);
                                q.setVisible(true);
                            }
                        });
                        requery.addMouseListener(new MouseAdapter() {
                        	public void mousePressed(MouseEvent e) {
                        		System.out.println(corrections);
                        		List<Posting>queryResults = indexer.query(corrections);
                            	 
                        		  String data[][] = null;
                                  String column[] = new String[1];
                                  int i = 0;
                            	data = new String[queryResults.size()][1];
                            	
                            	for(Posting p : queryResults)
                                {
                                    data[i][0] = p.getDocumentId() +" "+dc.getDocument(p.getDocumentId()).getTitle();
                                    System.out.println("p.getDocumentId() +\" \"+dc.getDocument(p.getDocumentId()).getTitle() " +p.getDocumentId() +" "+dc.getDocument(p.getDocumentId()).getTitle());
                                    i++;
                                }
                            	column[0] = "Number of Results: " + queryResults.size(); 
                            	
                            	jt=new JTable(data,column);    
                                jt.setBounds(30,40,200,300);  
                                JScrollPane sp=new JScrollPane(jt);    
                                Button back = new Button("Back");
                                jt.setRowSelectionAllowed(true);
                                jt.setEnabled(false);
                                Frame jf3 = new Frame();
                                jf3.add(sp);  
                                jf3.add(back, BorderLayout.AFTER_LAST_LINE);
                                jf3.setSize(300,400);    
                                jf3.setVisible(true);
                                
                                jt.addMouseListener(new MouseAdapter() {
                                    public void mouseClicked(MouseEvent me)
                                    {
                                            System.out.println("clicked item");
                                            JTable target = (JTable)me.getSource();
                                            Point p = me.getPoint();
                                            int row = target.rowAtPoint(p); // select a row
                                            int column = target.columnAtPoint(p); // select a column
                                        
                                            jt.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
                                            if(column == 0 && me.getClickCount() == 2)
                                            {                           
                                            	System.out.println("row: " + row +" col: " + column);
                                    
                                            	String str = target.getValueAt(row, column).toString();
                                            	System.out.println(str);
                                            	str=str.split(" ")[0];
                                      
                                            	System.out.println("clicked twice");
                                            	JOptionPane op = new JOptionPane();
                                            	JTextArea jta = new JTextArea(dc.getDocument(Integer.parseInt(str)).getBody());
                                            	JScrollPane jsp = new JScrollPane(jta){
        		                                    @Override
        		                                    public Dimension getPreferredSize() {
        		                                        return new Dimension(480, 320);
        		                                    }
                                            	};
                                            	JOptionPane.showMessageDialog( null, jsp, "Content", JOptionPane.PLAIN_MESSAGE);                                  
                                        }
                                    }
                                });
                                
                                back.addMouseListener(new MouseAdapter() 
                                {
                                    @Override
                                    public void mousePressed(MouseEvent e){
                                        jf3.setVisible(false);
                                        q.setVisible(true);
                                    }
                                });
                        	}
                        });
                    }               
                }
            });             
        }
     
     public static void main(String[] args)
     {
    	 MyGUIProgram prog = new MyGUIProgram();
    	 prog.BuildorQueryIndex();
     }
}
