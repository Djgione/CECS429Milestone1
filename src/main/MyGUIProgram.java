package main;

import cecs429.documents.Document;
import cecs429.documents.DocumentCorpus;
import cecs429.index.Posting;
import java.awt.*;
import cecs429.indexer.Indexer;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.Collections;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import org.tartarus.snowball.SnowballStemmer;


public class MyGUIProgram extends Frame{
        String queryOption = ""; 
        String corpType;
        static Indexer index = null;
        Frame f;
        Frame q;
        Label label;
        Label label2;
        Button buildIndexButton;
        TextField textFieldPath;
        TextField querySearchField;
        Button queryButton;
        List<Integer> docId; 

        public void Milestone2Frame()
        {
        	
        }
        public void InitFrame()
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
            
            if(index == null){
              

                buildIndexButton.addMouseListener(new MouseAdapter() 
                { 
                   @Override
                   public void mousePressed(MouseEvent me) { 
                       f.setVisible(false);
                       long st = System.currentTimeMillis();
                
                       System.out.println("textFieldPath.getText(): " + textFieldPath.getText());
                       index = new Indexer(Paths.get(textFieldPath.getText()).toAbsolutePath(),corpType);
                       long et = System.currentTimeMillis();
                       
                       long indexTime = (et - st);
                       label2 = new Label("Corpus Indexed in : " + indexTime + "ms");
                       QueryFrame();
                   }  
                });
            }
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
            queryButton = new Button("Query");
            buildIndexButton.setBounds(100,120,80,30);  
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
                        f.setVisible(true);
                        index = null;
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
                       index = null;

                   }
                   else if(queryOption.equals(":vocab"))
                   {
                       q.setVisible(false);
                       Frame jf =new Frame();
                       List<String> vocab = index.getVocab1000();
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
                        q.setVisible(false);
                        Frame jf2 =new Frame();
                        DocumentCorpus dc = index.getCorpus();
                        List<Posting> queryResults = index.query(querySearchField.getText());
                        
                        String data[][]= new String[queryResults.size()][1];

                        int i = 0;

                        for(Posting p : queryResults)
                        {
                            data[i][0] = p.getDocumentId() +" "+dc.getDocument(p.getDocumentId()).getTitle();
                    
                            i++;
                        }
                        String column[]={"Number of Results: " + queryResults.size()};         
                        JTable jt=new JTable(data,column);    
                        jt.setBounds(30,40,200,300);  
                        JScrollPane sp=new JScrollPane(jt);    
                        Button back = new Button("Back");
                        jt.setRowSelectionAllowed(true);
                        jt.setEnabled(false);
                        jf2.add(sp);  
                        jf2.add(back, BorderLayout.AFTER_LAST_LINE);
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
                                if(column == 0 && me.getClickCount() == 2){
                             
                                   System.out.println("row: " + row +" col: " + column);
                            
                                   String str = target.getValueAt(row, column).toString();
                                                                 System.out.println(str);
                                                                    
                                   str=str.split(" ")[0];
                                   System.out.println("clicked twice");
                                   //File file=new File(dc.getDocument(Integer.parseInt(str)).getBody())
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
                    }               
                }
            });             
        }
     
     public static void main(String[] args)
     {
    	 MyGUIProgram prog = new MyGUIProgram();
    	 prog.InitFrame();
     }
}
