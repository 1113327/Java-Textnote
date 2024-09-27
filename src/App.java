import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.awt.datatransfer.*;

public class App extends Frame {

  TextArea t;
  FileDialog fd;

  public App(String title) {

    super(title);

    t = new TextArea();
    t.setFont(new Font("Dialog", Font.PLAIN, 12));
    t.setEditable(true);
    add(t, BorderLayout.CENTER);

    MenuBar mb = new MenuBar();
    setMenuBar(mb);

    Menu fmi = new Menu("檔案");
    mb.add(fmi);

    MenuItem nmi = new MenuItem("開新檔案");
    nmi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        t.setText("");
      }
    });
    fmi.add(nmi);

    MenuItem omi = new MenuItem("開啟舊檔");
    omi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        fd = new FileDialog(App.this, "開啟舊檔", FileDialog.LOAD);
        fd.setVisible(true);
        String filename = fd.getFile();
         if (filename != null) {
            try(InputStream inputStream = new FileInputStream(fd.getDirectory() + filename);
               Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
               BufferedReader br = new BufferedReader(reader))
            {
               //BufferedReader br = new BufferedReader(new FileReader(fd.getDirectory() + filename));
               String line;
               t.setText("");
               while ((line = br.readLine()) != null) {
               t.append(line + "\n");
               }
               //br.close();
            } catch (IOException ioe) {
               ioe.printStackTrace();
            }
        }
      }
    });
    fmi.add(omi);
    

    MenuItem smi = new MenuItem("儲存檔案");
    smi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        fd = new FileDialog(App.this, "儲存檔案", FileDialog.SAVE);
        fd.setVisible(true);
        String filename = fd.getFile();
        if (filename != null) {
            try(OutputStream outputStream = new FileOutputStream(fd.getDirectory() + filename);
               Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
               BufferedWriter bw = new BufferedWriter(writer))
            {
               //BufferedWriter bw = new BufferedWriter(new FileWriter(fd.getDirectory() + filename));
               bw.write(t.getText());
               //bw.close();
            } catch (IOException ioe) {
               ioe.printStackTrace();
            }
        }
      }
    });
    fmi.add(smi);

    fmi.addSeparator();

    MenuItem exmi = new MenuItem("離開");
    exmi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        System.exit(0);
      }
    });
    fmi.add(exmi);

    Menu emi = new Menu("編輯");
    mb.add(emi);

    MenuItem cmi = new MenuItem("複製");
    cmi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
         String copiedText = t.getText();
         /*StringSelection stringSelection = new StringSelection(t.getText());
         Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
         clipboard.setContents(stringSelection, null);
         t.copy();*/
      }
    });
    emi.add(cmi);

    MenuItem pmi = new MenuItem("貼上");
    pmi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
               Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      try {
         // Get data from clipboard and convert it to String
         String pasteData = (String) clipboard.getData(DataFlavor.stringFlavor);
         
         // Get current cursor position
         int position = t.getCaretPosition();
         
         // Insert the text at current cursor position
         t.insert(pasteData, position);
      } catch (UnsupportedFlavorException | IOException ex) {
         ex.printStackTrace();
      }
        //t.paste();
      }
    });
    emi.add(pmi);

    MenuItem cutmi = new MenuItem("剪下");
    cutmi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
         String selectedText = t.getSelectedText(); // Get selected text
         int start = t.getSelectionStart(); // Get start position of selected text
         int end = t.getSelectionEnd(); // Get end position of selected text
         
         // Copy selected text to clipboard
         StringSelection stringSelection = new StringSelection(selectedText);
         Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
         clipboard.setContents(stringSelection, null);
         
         // Remove selected text from TextArea
         t.replaceRange("", start, end);
         //t.cut();
      }
    });
    emi.add(cutmi);

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    setSize(400, 250);
    setVisible(true);
  }

  public static void main(String s[]) {
    App notepad = new App("Java版記事本");
  }
}
