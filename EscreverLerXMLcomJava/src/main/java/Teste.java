import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class Teste {

    public static void main (String[] agrs){
        try{
            String valor="Hello";
            String arquivo = "C:\\temp\\filexml.xml";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder dc = dbf.newDocumentBuilder();
            Document d = dc.newDocument();
            File file = new File(arquivo);
            Element raiz;
            int contador=1;

            if (file.exists()){
                d = dc.parse(file);
                raiz = d.getDocumentElement();
                NodeList nodes = d.getElementsByTagName("post");
                contador = nodes.getLength()+1;

            }else{
                d = dc.newDocument();
                raiz = d.createElement("Posts");
                d.appendChild(raiz);
            }

            //elemento post
            Element post = d.createElement("post");
            raiz.appendChild(post);

            //definindo o atributo do post
            Attr attr =d.createAttribute("id");
            long currentTimeStamp=System.currentTimeMillis();
            attr.setValue("_"+currentTimeStamp);
            post.setAttributeNode(attr);

            //definindo valor da postagem na tag text
            Element textoRecebido=d.createElement("Text");
            textoRecebido.appendChild(d.createTextNode(valor+" "+contador));
            post.appendChild(textoRecebido);


            //construção do XML
            TransformerFactory tf = TransformerFactory.newInstance();
            //para manter identação correta
            Transformer t = tf.newTransformer(new StreamSource(new File("c:\\temp\\xslt.xslt")));
            t.setOutputProperty(OutputKeys.INDENT,"yes");
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "2" );


            DOMSource domSource = new DOMSource(d);
            StreamResult streamResult = new StreamResult(new File(arquivo));

            //juntar o conteudo ao arquivo criado
            t.transform(domSource,streamResult);
            System.out.println("Criado");

            //correção de identação no XML
            //https://stackoverflow.com/questions/13925608/creating-xml-only-printing-to-one-line
            //correção de linhas em branco
            //https://stackoverflow.com/questions/58478632/how-to-avoid-extra-blank-lines-in-xml-generation-with-java

        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Diretório "+System.getProperty("user.dir"));
        }

        //leitura
        //https://www.javatpoint.com/how-to-read-xml-file-in-java
        try {
            File arquuivoXML = new File("C:\\temp\\filexml.xml");
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(arquuivoXML);
            System.out.println("Root element: "+ document.getDocumentElement().getNodeName());
            if (document.hasChildNodes()) {
                printNodeList(document.getChildNodes());
            }

        } catch (Exception e) {

        }

    }

    //
    private static void printNodeList(NodeList nodeList) {
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node elemNode = nodeList.item(count);
            if (elemNode.getNodeType() == Node.ELEMENT_NODE) {
                // get node name and value
                System.out.println("\nNode Name =" + elemNode.getNodeName() + " [OPEN]");
                System.out.println("Node Content =" + elemNode.getTextContent());
                if (elemNode.hasAttributes()) {
                    NamedNodeMap nodeMap = elemNode.getAttributes();
                    for (int i = 0; i < nodeMap.getLength(); i++) {
                        Node node = nodeMap.item(i);
                        System.out.println("attr name : " + node.getNodeName());
                        System.out.println("attr value : " + node.getNodeValue());
                    }
                }
                if (elemNode.hasChildNodes()) {
                    //recursive call if the node has child nodes
                    printNodeList(elemNode.getChildNodes());
                }
                System.out.println("Node Name =" + elemNode.getNodeName() + " [CLOSE]");
            }
        }
    }
}
