package crm.mq.monitor;

import java.util.Hashtable;

import javax.ejb.Schedule;
import javax.ejb.Stateless;

import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;


/*
 * 
 * Stateless Bean which prints out time in milliseconds every 10 seconds
 * 
 */

@Stateless
public class MQMonitorService {
	
	MQQueueManager queueManager;

	@Schedule(second = "*/10", minute = "*", hour = "*", persistent = true)
	public void monitorMQ() {
		System.out.println("Starting MQ monitor ...");

		// Set up the MQ properties
		Hashtable connectionProperties = new Hashtable();
		connectionProperties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES);
		connectionProperties.put(MQC.HOST_NAME_PROPERTY, "maat.momentum.co.za");
		//connectionProperties.put(MQC.HOST_NAME_PROPERTY, "imhotep.momentum.co.za" + "(1415)");
		connectionProperties.put(MQC.CHANNEL_PROPERTY, "CRM.CDI.SVRCONN");
		connectionProperties.put(MQC.USER_ID_PROPERTY, "crm");
		//connectionProperties.put(MQC.PASSWORD_PROPERTY, "pr0f1l3");
		connectionProperties.put(MQC.PASSWORD_PROPERTY, "pr0dcRmU$eR");
		
		//int openOptions = CMQC.MQOO_INQUIRE | CMQC.MQOO_INPUT_AS_Q_DEF;
        //MQEnvironment.hostname = "maat.momentum.co.za";
        //MQEnvironment.port = 1414;
        //MQEnvironment.channel = "CRM.CDI.SVRCONN";
        //MQEnvironment.userID = "crm";
        //MQEnvironment.password = "pr0dcRmU$eR";
        //MQEnvironment.properties.put(CMQC.TRANSPORT_PROPERTY,CMQC.TRANSPORT_MQSERIES);

		try {
			this.queueManager = new MQQueueManager("MOMPRD.QUE.MGR");
			System.out.println("Connected");
			
			try {
			    Thread.sleep(10000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			
			this.queueManager.disconnect();
			this.queueManager.close();
			System.out.println("Closed");
			
			try {
			    Thread.sleep(10000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			
		} catch (MQException mexc) {
			mexc.printStackTrace();
			mexc.getCause();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
}