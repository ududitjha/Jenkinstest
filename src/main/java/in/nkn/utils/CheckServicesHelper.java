    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.nkn.utils;

import com.rabbitmq.tools.json.JSONSerializable;
import helper.GenericEJBHelper;
import in.nkn.utils.ServicesXML;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author user
 */
public class CheckServicesHelper {

    Logger log = Logger.getLogger(CheckServicesHelper.class);

    /**
     *
     * Checks for given service in XML file.
     *
     * @param service name of service to be checked.
     * @return a map of service name and its value.
     */
    public ServicesXML checkServiceByXML(String service, HttpServletRequest request) {
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure();
        List<ServicesXML> listService = new ArrayList<ServicesXML>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            File file = new File(request.getServletContext().getRealPath("") + File.separator + "WebTexts" + File.separator + "services.xml");
            //File file = new File("E:\\ProjectsVersion1\\Accounts\\Accounts\\web\\WebTexts\\services.xml");
            if (file.exists()) {
                Document doc = db.parse(file);
                Element docEle = doc.getDocumentElement();

                NodeList serviceList = docEle.getElementsByTagName("service");

                if (serviceList != null && serviceList.getLength() > 0) {
                    for (int i = 0; i < serviceList.getLength(); i++) {
                        Node node = serviceList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element e = (Element) node;
                            NodeList nodeList = e.getElementsByTagName("name");
                            String name = nodeList.item(0).getChildNodes().item(0)
                                    .getNodeValue();

                            nodeList = e.getElementsByTagName("value");
                            String value = nodeList.item(0).getChildNodes().item(0)
                                    .getNodeValue();

                            nodeList = e.getElementsByTagName("domain");
                            String domain = nodeList.item(0).getChildNodes().item(0)
                                    .getNodeValue();
                            nodeList = e.getElementsByTagName("realm");
                            String realm = nodeList.item(0).getChildNodes().item(0)
                                    .getNodeValue();
                            nodeList = e.getElementsByTagName("apiKey");
                            String api_key = nodeList.item(0).getChildNodes().item(0)
                                    .getNodeValue();

                            ServicesXML serviceXML = new ServicesXML();

                            System.out.println("domain:" + domain);
                            System.out.println("value:" + value);
                            System.out.println("realm:" + realm);
                            System.out.println("apiKey:" + api_key);
                            serviceXML.setDomain(domain);
                            serviceXML.setName(name);
                            serviceXML.setUrl(value);
                            serviceXML.setRealm(realm);
                            serviceXML.setApi_key(api_key);

                            //listService.add(serviceXML);

                            // System.out.println(name + "---" + value + "---" + domain + "---" + realm);
                            if (name.equals(service)) {
                                System.out.println("name: " + name);
                                log.info("Service exist in the XML File");

                                return serviceXML;
                            }

                        }
                    }
                } else {

                    return null;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public ServicesXML checkServiceByEJB(String serviceName) {
        try {
            ServicesXML services = new ServicesXML();
            String details = new GenericEJBHelper().getServiceDetails(serviceName);
            JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(details);
            JSONObject innerJSON = jsonObject.getJSONObject("Services");
            services.setName(innerJSON.getString("serviceName"));
            services.setRealm(innerJSON.getString("realm"));
            services.setUrl(innerJSON.getString("homeURL"));
            services.setApi_key(innerJSON.getString("apiKey"));
            services.setLogout_url(innerJSON.getString("logoutURL"));
            services.setDomain(innerJSON.getString("domain"));
            services.setApplication_type(innerJSON.getString("applicationType"));
            return services;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
//        public List<ServicesXML> checkServiceByEJB() {
//        try {
//            ServicesXML services = new ServicesXML();
//            String details = new GenericEJBHelper().getServiceDetails(serviceName);
//            JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(details);
//            JSONObject innerJSON = jsonObject.getJSONObject("Services");
//            services.setName(innerJSON.getString("serviceName"));
//            services.setRealm(innerJSON.getString("realm"));
//            services.setUrl(innerJSON.getString("homeURL"));
//            services.setApi_key(innerJSON.getString("apiKey"));
//            services.setLogout_url(innerJSON.getString("logoutURL"));
//            services.setDomain(innerJSON.getString("domain"));
//            services.setApplication_type(innerJSON.getString("applicationType"));
//            return services;
//        } catch (Exception e) {
//            System.out.println(e);
//            return null;
//        }
//    public static void main(String[] args) {
//        ServicesXML listService = checkServiceByXML("ABC", null);
//        //for (Iterator<ServicesXML> it = listService.iterator(); it.hasNext();) {
//        //ServicesXML servicesXML = it.next();
//        System.out.println(listService.getDomain() + "---" + listService.getName() + "---" + listService.getRealm() + "---" + listService.getUrl());
//
//        //}
//
//    }
//    public static void main(String[] args) {
//        //HttpServletRequest request = null;
//        
//        //ServicesXML services = new CheckServicesHelper().checkServiceByEJB("Client");
//
//    }
}
