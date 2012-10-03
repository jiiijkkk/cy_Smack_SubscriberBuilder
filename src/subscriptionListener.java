import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

public class subscriptionListener {
	private Connection conn;
	private PacketListener listener;
	
	subscriptionListener(Connection conn){
		this.conn = conn;
		
		this.listener = new PacketListener(){
			@Override
			public void processPacket(Packet packet) {
				presenceChangedProcess( packet );
			}
		};
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
		}
	}
	public void removeRosterListener(){
		this.conn.removePacketListener(this.listener);
	}
}
