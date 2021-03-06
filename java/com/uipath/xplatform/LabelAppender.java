package com.uipath.xplatform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.StringWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

 

public class LabelAppender {

    public static String UiPath_Label ="Ui-%s-%d";
    public static String [] TAGS = {"Edit", "Radio", "Combo", "Button", "Tabpage", "Calendar", "TextArea", "Grid", "CheckBox", "MaskEdit"};

    public void appendLabel(File source, boolean overwrite) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer former = factory.newTransformer();
        former.setOutputProperty(OutputKeys.VERSION, "1.0");
        former.setOutputProperty(OutputKeys.STANDALONE, "no");
        //former.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        former.setOutputProperty(OutputKeys.INDENT, "yes");
        former.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "yes");

        File [] files = null;
        if( source.isDirectory()) {
            files = source.listFiles( new FilenameFilter(){
                @Override
                public boolean accept(File arg0, String arg1) {
                    if( arg1.endsWith(".xfdl"))
                        return true;
                    else
                        return false;
                }
            });
        } else {
            files = new File[] {source};
            //System.out.println( files.length);
        }

        for( File file: files) {
            Random startLabelId = new Random();
            int labelId = startLabelId.nextInt(100);
            System.out.println( file.toString());
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();
            for (String tag: LabelAppender.TAGS) {
                NodeList nodes = root.getElementsByTagName(tag); 
                for( int idx = 0; idx < nodes.getLength(); idx++) {
                    Element node = (Element)nodes.item(idx);
                    String attr = node.getAttribute("style");
                    if( attr != null ) {
                        if( !attr.contains("accessibility:enable") ) {
                            if( attr.trim().length() > 2) {
                                if( attr.trim().endsWith(";"))
                                    node.setAttribute("style",  attr + " accessibility:enable all 'label: " +  String.format(UiPath_Label, tag, labelId++) + "';");
                                else
                                    node.setAttribute("style",  attr + "; accessibility:enable all 'label: " +  String.format(UiPath_Label, tag, labelId++) + "';");
                            } else
                                node.setAttribute("style",  "accessibility:enable all 'label: " + String.format(UiPath_Label, tag, labelId++) + "';");
                        } 
                    } else {
                        node.setAttribute("style",  "accessibility:enable all 'lable: " + String.format(UiPath_Label, tag, labelId++) + "';");
                    }
                    //System.out.println( node.getAttribute("style"));
                }
            }
           
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource dest = new DOMSource(doc);
            former.transform(dest, result);

            if( overwrite) {
                String newxfdl = file.getAbsolutePath();
                file.delete();
                Writer fw = new OutputStreamWriter( new FileOutputStream( new File(newxfdl)), "UTF-8");
                fw.write( sw.toString());
                fw.flush();
                //new FileWriter( new File(newxfdl), false).write(sw.toString());
            } else {
                System.out.println(sw.toString());
            }
        }
        
    }

    public static void showHelp()
    {
       System.out.println("please check parameter names");
       System.out.println("java -jar xplatformutil.jar --dir {folder name contains xfdl files} --overwrite ");
       System.out.println("java -jar xplatformutil.jar --file {specific xfdl file} --overwrite ");
    }

    public static void main(String args[]) {
        boolean filemode = true;
        boolean overwrite = false;
        boolean check_file = false;
        String target ="";
        if( args.length < 2) {
            showHelp();
            return;
        } else {
            for( String p : args) {
                //System.out.println( p);
                if( check_file) {
                    target = p.trim();
                    check_file = false;
                } else if( p.trim().equals("--dir")) {
                    filemode = false;
                    check_file = true;
                } else if( p.trim().equals("--file")) {
                    filemode = true;
                    check_file = true;
                } else if( p.trim().equals("--overwrite")) {
                    overwrite = true;
                }
            }
            try {
                //System.out.println("target " + target + "  filemode " + filemode + ",  overwrite "+ overwrite);
                LabelAppender lable = new LabelAppender();
                lable.appendLabel( new File( target), overwrite);
            } catch ( Exception ioe) {
                System.err.println( ioe.getMessage());
            }
        }
    }
}