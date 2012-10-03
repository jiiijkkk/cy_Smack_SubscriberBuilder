import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class SmackCore {	
	private String domain;
	private ConnectionConfiguration config;
	private int account_number;
	private int admin_number;
	private String name_prefix;
	private List<Connection> conns = new ArrayList<Connection>();
	private List<subscriptionListener> listeners = new ArrayList<subscriptionListener>();
	
	public SmackCore(
			String domain ,
			int account_number ,
			int admin_number ,
			String name_prefix
			){		
		this.domain = domain;
		this.account_number = account_number;
		this.admin_number = admin_number;
		this.name_prefix = name_prefix;

		//XMPPConnection.DEBUG_ENABLED = true;
		this.config = new ConnectionConfiguration(this.domain, 5222);
		this.config.setReconnectionAllowed(false);
		
	}
	public void createAccounts(){
		int i = 0;
		for ( ; i < account_number ; i++ ){
			Connection conn_tmp = new XMPPConnection(this.config);
			this.conns.add(conn_tmp);
			conn_tmp.getRoster().setSubscriptionMode(Roster.SubscriptionMode.manual);
			try {
				conn_tmp.connect();
				try {
					conn_tmp.getAccountManager().createAccount( name_prefix + i , name_prefix );
				} catch (XMPPException e_create) {
					System.out.println( "Warning : test " + i + " is existed!" );
				}
				try {
					conn_tmp.login( this.name_prefix + i , this.name_prefix );
				} catch (XMPPException e_login) {
					System.err.println("Warning : Can not login!");
				}
			} catch (XMPPException e_connect) {
				System.err.println("Warning : Can not connected!");
			}
		}
	}
	public void createListeners(){
		System.out.println(this.conns.size());
		for ( Connection conn_tmp : this.conns ){
			subscriptionListener thisSubscriptionListener = new subscriptionListener(conn_tmp);
			thisSubscriptionListener.addRosterListener();
			this.listeners.add(thisSubscriptionListener);
		}
	}
	public void createFriendships(){
		for ( Connection conn_tmp : this.conns ){
			int i = 0;
			for ( ; i < this.admin_number ; i++ ){
				try {
					conn_tmp.getRoster().createEntry( "admin"+i+"@vopenfire" , "admin"+i , null);
				} catch (XMPPException e) {
					System.out.println( "Warning : " + conn_tmp.getUser() + " adding admin " + i + " fail!" );
				}
			}
		}
	}
	public void removeAccounts(){
		for ( subscriptionListener thisSubscriptionListener : this.listeners ){
			thisSubscriptionListener.removeRosterListener();
		}
		for ( Connection conn_tmp : this.conns ){
			try {
				conn_tmp.getAccountManager().deleteAccount();
			} catch (XMPPException e_delete) {
				System.err.println("Warning : Can not be deleted!");
			}
		}
	}
	public void disconnectListeners(){
		for ( Connection conn_tmp : this.conns ){
			conn_tmp.disconnect();
		}
	}
}
