import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

public class subscriptionListener {
	private Connection conn;
	private PacketListener listener;
	private Date start_time;
	private Date end_time;
	private BufferedWriter out;
	
	subscriptionListener(Connection conn, BufferedWriter out ){
		this.conn = conn;
		this.out = out;
		
		this.listener = new PacketListener(){
			@Override
			public void processPacket(Packet packet) {
				presenceChangedProcess( packet );
			}
		};
	}
	public Connection getConnection(){
		return this.conn;
	}
	public void addRosterListener(){
		this.conn.getRoster().setSubscriptionMode(Roster.SubscriptionMode.manual);
		this.conn.addPacketListener(
				this.listener ,
				new PacketTypeFilter(Presence.class){
				}
		);
	}
	void presenceChangedProcess( Packet packet ){
		if(packet.toString().equals(Presence.Type.subscribe.toString())){
			Presence add_friend;
			add_friend = new Presence(Presence.Type.subscribed);
			add_friend.setTo(packet.getFrom());
			this.conn.sendPacket(add_friend);
			add_friend = new Presence(Presence.Type.subscribe);
			add_friend.setTo(packet.getFrom());
			this.conn.sendPacket(add_friend);
			
			//
			this.end_time = new Date();

			try {
				out.write( this.conn.getUser() + " added " + packet.getFrom() + " " + getTime() + "\n" );
			} catch (IOException e) {
				System.err.println("Warning : logging file error!");
			}
		}
	}
	public void removeRosterListener(){
		this.conn.removePacketListener(this.listener);
	}
	public void setStartTime(){
		this.start_time = new Date();
	}
	public Date getStartTime(){
		return this.start_time;
	}
	public Date getEndTime(){
		return this.end_time;
	}
	public long getTime(){
		return this.end_time.getTime() - this.start_time.getTime();
	}
}
