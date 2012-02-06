package org.rs2.service.login;

import java.util.logging.Logger;

import org.jboss.netty.channel.*;
import org.rs2.model.World;
import org.rs2.model.mask.UpdateFlag;
import org.rs2.model.player.*;
import org.rs2.mysql.DatabaseConnection;
import org.rs2.mysql.DatabaseManager;
import org.rs2.net.ActionSender;
import org.rs2.net.msg.MessageBuilder;
import org.rs2.net.pipeline.DefaultProtocolDecoder;
import org.rs2.service.logic.LogicService;
import org.rs2.service.saving.SavingService;
import org.rs2.util.DatabaseUtils;
import org.rs2.util.Misc;

/**
 * 508 Base
 * @author Harry Andreas
 */
public class LoginServiceTask {
	
	/**
	 * Attributes for the task
	 */
	private final String username, password;
	
	/**
	 * The channel for the task
	 */
	private final Channel channel;
	
	/**
	 * Content
	 */
	private final ChannelHandlerContext context;
	
	/**
	 * Constructor
	 * @param username
	 * @param password
	 * @param connector
	 */
	public LoginServiceTask(String username, String password, Channel channel, ChannelHandlerContext ctx) {
		this.username = username;
		this.password = password;
		this.channel = channel;
		this.context = ctx;
	}
	
	/**
	 * Execute the event
	 */
	public void execute(Logger logger) {
		GameSession session = new GameSession(channel);
		DatabaseConnection con = DatabaseManager.getSingleton().getConnection("player-loader");
		DatabaseResult result = null;
		String hashedPass = "failed";
		int rights = 0;
		try {
			hashedPass = Misc.getMD5(password);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int returnCode = LoginConfigs.LOGIN_OK;
		boolean playerExists = DatabaseUtils.playerExists(username, con);
		if(playerExists) {
			try {
				result = DatabaseUtils.fetchProfile(username, con);
				if(!result.getPassword().equals(hashedPass)) {
					returnCode = LoginConfigs.INVALID_PASSWORD;
				}
				rights = result.getRights();
			} catch (Exception e) {
				returnCode = LoginConfigs.TRY_AGAIN;
				e.printStackTrace();
			}
		}
		Player player = new Player(session, rights);
		if(playerExists) {
			SavingService.loadData(player, result);
		}
		player.setHashedPassword(hashedPass);
		player.setUnformattedName(username);
		player.setFormattedName(Misc.formatName(username));
		if(World.getSingleton().checkExistance(username)) {
			returnCode = LoginConfigs.ALREADY_ONLINE;
		}
		if(returnCode == 2) {
			if(!World.getSingleton().register(player)) {
				returnCode = LoginConfigs.WORLD_FULL;
			}
		}
		MessageBuilder bldr = new MessageBuilder();
		bldr.writeByte(returnCode);
		bldr.writeByte(rights);
		bldr.writeByte(0);
		bldr.writeByte(0);
		bldr.writeByte(0);
		bldr.writeByte(1);
		bldr.writeShort(player.getIndex());
		bldr.writeByte(0);
		if(returnCode != 2) {
			channel.write(bldr.toMessage()).addListener(ChannelFutureListener.CLOSE);
			return;
		} else {
			channel.write(bldr.toMessage());
		}
		getContext().getPipeline().replace("decoder", "decoder", new DefaultProtocolDecoder(session));
		ActionSender.sendLogin(player);
		synchronized(LogicService.getSingleton()) {
			player.getUpdateFlags().flag(UpdateFlag.APPERANCE);
		}
		//World.getSingleton().getGroundItemManager().loadItemsForPlayer(player, player.getUnformattedName());
		logger.info("Registered player ["+username+" @ index "+player.getIndex()+"] successfully!");
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * @return the context
	 */
	public ChannelHandlerContext getContext() {
		return context;
	}

}