package crm.mq.monitor;

import java.io.IOException;
import java.util.Hashtable;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

public class MQClientTest {


//    String qMngrStr = "MOMTEST.QUE.MGR";
//    String user = "mqm";
//    String password = "pr0f1l3";
//    String queueName = "JACO_ESB_REQ";
//    String hostName = "imhotep.momentum.co.za";
//    int port = 1415;
//    String channel = "CRM.CDI.SVRCONN";
	
  String qMngrStr = "MOMPRD.QUE.MGR";
  String user = "crm";
  String password = "pr0dcRmU$eR";
  String queueName = "SALES_CRM_MSTI_LEADS";
  String hostName = "maat.momentum.co.za";
  int port = 1414;
  String channel = "CRM.CDI.SVRCONN";
	
    //message to put on MQ.
    String msg = "Hello World, WelCome to MQ.";
    //Create a default local queue.
    MQQueue defaultLocalQueue;
    MQQueueManager qManager;
    
    /**
     * Initialize the MQ
     *
     */
    public void init(){
        
        //Set MQ connection credentials to MQ Envorinment.
//         MQEnvironment.hostname = hostName;
//         MQEnvironment.channel = channel;
//         MQEnvironment.port = port;
//         MQEnvironment.userID = user;
//         MQEnvironment.password = password;
//         //set transport properties.
//         MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES_CLIENT);
    	
		Hashtable connectionProperties = new Hashtable();
		connectionProperties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES_CLIENT);
		connectionProperties.put(MQC.HOST_NAME_PROPERTY, hostName);
		connectionProperties.put(MQC.CHANNEL_PROPERTY, channel);
		connectionProperties.put(MQC.PORT_PROPERTY, port);
		connectionProperties.put(MQC.USER_ID_PROPERTY, user);
		connectionProperties.put(MQC.PASSWORD_PROPERTY, password);
         
         try {
             //initialize MQ manager.
            qManager = new MQQueueManager(qMngrStr,connectionProperties);
        } catch (MQException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Method to put message to MQ.
     *
     */
    public void putAndGetMessage(){
        
        int openOptions = MQC.MQOO_INPUT_AS_Q_DEF | MQC.MQOO_OUTPUT; 
        try {
            defaultLocalQueue = qManager.accessQueue(queueName, openOptions);
            
            MQMessage putMessage = new MQMessage();
            putMessage.writeUTF(msg);
            
            //specify the message options...
            MQPutMessageOptions pmo = new MQPutMessageOptions(); 
            // accept 
            // put the message on the queue
            defaultLocalQueue.put(putMessage, pmo);
            
            System.out.println("Message is put on MQ.");
            
            //get message from MQ.
            MQMessage getMessages = new MQMessage();
            //assign message id to get message.
            getMessages.messageId = putMessage.messageId;
            
            //get message options.
            MQGetMessageOptions gmo = new MQGetMessageOptions();
            defaultLocalQueue.get(getMessages, gmo);
            
            String retreivedMsg = getMessages.readUTF();
            System.out.println("Message got from MQ: "+retreivedMsg);
            
        } catch (MQException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        
        System.out.println("Processing Main...");
        
        MQClientTest clientTest = new MQClientTest();
        
        //initialize MQ.
        clientTest.init();
        
        //put and retreive message from MQ.
        clientTest.putAndGetMessage();
        
        System.out.println("Done!");
    }
    
}