import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MapEditor {
public JFrame curentFrame;
public Map map;
public Writer writer;
public Reader reader;
public MapReaderWriter mapReaderWriter;

    public MapEditor(Map mapIn){
        Display display = new Display();
        this.curentFrame=display;
        this.map=mapIn;
        this.writer=null;
        this.reader=null;
        this.mapReaderWriter = new MapReaderWriter();

    }

    public class Display extends JFrame {
        public MapPanel myPanel;
        public Display(){

            this.setLayout(new BorderLayout());
            super.setTitle("Frame Title");

            this.myPanel= new MapPanel();
            //this.setPreferredSize(myPanel.getPreferredSize());
            this.setJMenuBar(MyMenuBar());
            this.getContentPane().add(myPanel, BorderLayout.CENTER);
            this.setSize(600,600);
            this.setVisible(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }

        public void paint(Graphics g){
            super.paint(g);

        }
    }
    public class MapPanel extends JPanel implements MapListener {

        public MapPanel() {
            this.setLayout(null);
            Border border = this.getBorder();
            Border margin = new EmptyBorder(100,10,100,100);
            this.setBorder(new CompoundBorder(border, margin));
            this.setLocation(20,20);
            //this.setSize(300,300);
            this.setOpaque(true);
        }
        public Dimension getPreferredSize() {
            return new Dimension(250,200);
        }

        public void paint(Graphics g) {
            super.paint(g);
            Color framebg= Color.lightGray;
            this.setBackground(Color.white);
            // Draw Text
            g.drawString("This is my custom Panel!",10,20);
        }

        @Override
        public void placesChanged() {
            System.out.println("places changed");
        }

        @Override
        public void roadsChanged() {
            System.out.println("roads changed");
        }

        @Override
        public void otherChanged() {
            System.out.println("other changed");
        }
    }

    JMenuBar MyMenuBar() {
        JMenuBar menuBar;
        JMenu File, Edit;
        JMenuItem Open, SaveAs, Append, Quit ;
        JMenuItem NewPlace, NewRoad, SetStart, UnsetStart, SetEnd, UnsetEnd, Delete;

        menuBar=new JMenuBar();
        File= new JMenu("File");
        Edit= new JMenu("Edit");
        Open = new JMenuItem("Open");
        SaveAs = new JMenuItem("SaveAs");
        Append = new JMenuItem("Append");
        Quit = new JMenuItem("Quit");

        Open.setAccelerator(KeyStroke.getKeyStroke("control O"));
        SaveAs.setAccelerator(KeyStroke.getKeyStroke("control S"));
        Append.setAccelerator(KeyStroke.getKeyStroke("control A"));
        Quit.setAccelerator(KeyStroke.getKeyStroke("control Q"));

        Open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.out.println("Open File...");
                JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));

                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    //stuff goes here
                    Map map2 = new MapImpl();
                    try {
                        reader = new BufferedReader((new FileReader(chooser.getSelectedFile())));
                        mapReaderWriter.read(reader, map2);
                        } catch (MapFormatException e) {
                        JOptionPane.showMessageDialog(curentFrame, "Map format Error");
                            System.out.println("erroer detected!");
                            e.printStackTrace();
                        } catch (IOException e1) {
                        JOptionPane.showMessageDialog(curentFrame, "Error saving map");
                            e1.printStackTrace();
                        }

                    System.out.println("You chose to open this file: " +
                        chooser.getSelectedFile().getName());
                    map=map2;
                    Graphics g = curentFrame.getContentPane().getGraphics();
                    int y =40;
                    for (String line : map.toString().split("\n")){
                        g.drawString(line, 40, y += g.getFontMetrics().getHeight());
                    }

                    try {
                        reader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

       SaveAs.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               System.out.println("Save File...");
               JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));

               int returnVal = chooser.showSaveDialog(null);
               if(returnVal == JFileChooser.APPROVE_OPTION) {
                   try {
                       FileWriter fileWriter = new FileWriter(chooser.getSelectedFile());
                       mapReaderWriter.write(fileWriter, map);
                       fileWriter.close();
                   } catch (IOException e1) {
                       JOptionPane.showMessageDialog(curentFrame, "Error saving map");
                       e1.printStackTrace();
                   }
                   System.out.println("You chose to save this file: " +
                           chooser.getSelectedFile().getName());
               }
           }
       });

        Append.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Append File...");
                JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));

                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    //stuff goes here

                    System.out.println("You chose to Append this file: " +
                            chooser.getSelectedFile().getName());
                }
            }
        });

        Quit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Quit..");
              int input = JOptionPane.showOptionDialog(curentFrame, "Exit without saving?", "Warning,",JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE,null,null, null);
              if(input==JOptionPane.OK_OPTION){
                  System.exit(0);
              }
            }
        });


        NewPlace = new JMenuItem("NewPlace");
        NewRoad = new JMenuItem("NewRoad");
        SetStart = new JMenuItem("SetStart");
        UnsetStart = new JMenuItem("UnsetStart");
        SetEnd = new JMenuItem("SetEnd");
        UnsetEnd = new JMenuItem("UnsetEnd");
        Delete = new JMenuItem("Delete");



        Edit.add(NewPlace);
        Edit.add(NewRoad);
        Edit.add(SetStart);
        Edit.add(UnsetStart);
        Edit.add(SetEnd);
        Edit.add(UnsetEnd);
        Edit.add(Delete);

        File.add(Open);
        File.add(SaveAs);
        File.add(Append);
        File.add(Quit);
        menuBar.add(File);
        menuBar.add(Edit);

        return menuBar;
    }
}
