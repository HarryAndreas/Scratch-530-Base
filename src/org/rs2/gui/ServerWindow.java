package org.rs2.gui;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.rs2.gui.plugin.cmd.CommandFactory;
import org.rs2.model.World;
import org.rs2.model.player.Player;
import org.rs2.service.logic.LogicService;
import org.rs2.util.BandwidthMonitor;
import org.rs2.util.Configuration;
import org.rs2.util.FileDownload;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JProgressBar;
import javax.swing.JTable;

public class ServerWindow {
	
	/**
	 * Singleton instance
	 */
	private static ServerWindow instance;
	
	private Executor executor = Executors.newSingleThreadExecutor();
	
	/**
	 * Gets the singleton instance
	 * @return
	 */
	public static ServerWindow getSingleton() {
		if(instance == null) {
			instance = new ServerWindow();
		}
		return instance;
	}
	
	
	public void printCommandText(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				String current = commandLinePane.getText();
				String newText = current + ""+ (current.isEmpty() ? "" :System.getProperty("line.separator")) + text;
				commandLinePane.setText(newText);
				int length = commandLinePane.getDocument().getLength();
				commandLinePane.setCaretPosition(length);
			}
		});
	}
	
	/**
	 * Updates players online
	 * @param online
	 */
	public void updatePlayersOnline(final int online) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				playersOnlineLabel.setText(""+online);
				String[] names = new String[online + 1];
				int index = 0;
				for(Player p : World.getSingleton().getRegisteredPlayers()) {
					names[index] = p.getFormattedName();
					index++;
				}
				playersOnlineList.setListData(names);
			}
		});
	}
	
	/**
	 * Updates the server information
	 */
	public void updateServerInfo() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				serverName.setText(Configuration.SERVER_NAME);
				revisionLabel.setText("#"+Configuration.REVISION);
				tickRate.setText(Configuration.TICK_TIME+"ms");
				gameTicks.setText(Integer.toString(LogicService.getSingleton().getGameTicks()));
				badTicks.setText(Integer.toString(LogicService.getSingleton().getBadTicks()));
				lastTickTime.setText(Integer.toString(LogicService.getSingleton().getLastTickTime()) + "ms");
				dataR.setText(FileDownload.formatSize(BandwidthMonitor.bytesReceived.get()));
				dataS.setText(FileDownload.formatSize(BandwidthMonitor.bytesSent.get()));
				long memoryUsage = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory();
				threadCount.setText(Integer.toString(Thread.activeCount()));
				memUsage.setText(FileDownload.formatSize(memoryUsage));
			}
		});
	}
	
	/**
	 * Shows the window
	 */
	public void init() {
		instance.frame.setVisible(true);
	}

	private JFrame frame;
	public JTextField textField;
	public JTextPane commandLinePane;
	public JButton btnEnter;
	private JLabel playersOnlineLabel;
	private JList playersOnlineList;
	private JLabel serverName, revisionLabel, tickRate, gameTicks, badTicks, lastTickTime, dataR, dataS,
	memUsage, threadCount;
	private JTable table;
	private JTextField textField_1;

	/**
	 * Create the application.
	 */
	private ServerWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setName(Configuration.SERVER_NAME+ " Control Panel");
		frame.setBounds(100, 100, 450, 555);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
		);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Info", null, panel_1, null);
		
		JLabel lblServerName = new JLabel("Server Name:");
		
		serverName = new JLabel("Server name here");
		
		JLabel lblServerRevision = new JLabel("Server Revision:");
		
		revisionLabel = new JLabel("Server Revision here");
		
		JLabel lblTickRate = new JLabel("Tick Rate:");
		
		tickRate = new JLabel("Tick rate here");
		
		JLabel lblGameTicks = new JLabel("Game Ticks:");
		
		gameTicks = new JLabel("Amount of game ticks here");
		
		JLabel lblBadTicks = new JLabel("Bad Ticks:");
		
		badTicks = new JLabel("Amount of bad ticks here");
		
		JLabel lblLastTickTime = new JLabel("Last tick time:");
		
		lastTickTime = new JLabel("Last tick time");
		
		JLabel lblDataSent = new JLabel("Data sent:");
		
		dataS = new JLabel("Data sent here");
		
		JLabel lblDataRecieved = new JLabel("Data recieved:");
		
		dataR = new JLabel("Data recieved here");
		
		JSeparator separator = new JSeparator();
		
		JLabel lblMemoryUsage = new JLabel("Memory Usage:");
		
		memUsage = new JLabel("Memory usage here");
		
		JLabel lblThreadCount = new JLabel("Thread Count:");
		
		threadCount = new JLabel("Thread count here");
		
		JButton btnGarbageCollect = new JButton("Clean Memory");
		btnGarbageCollect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.gc();
				System.runFinalization();
			}
		});
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(18)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(separator, GroupLayout.PREFERRED_SIZE, 364, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(lblServerRevision)
								.addComponent(lblServerName)
								.addComponent(lblGameTicks)
								.addComponent(lblTickRate)
								.addComponent(lblBadTicks)
								.addComponent(lblLastTickTime)
								.addComponent(lblDataSent)
								.addComponent(lblDataRecieved))
							.addGap(18)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(dataR)
								.addComponent(dataS)
								.addComponent(lastTickTime)
								.addComponent(badTicks)
								.addComponent(tickRate)
								.addComponent(serverName)
								.addComponent(revisionLabel)
								.addComponent(gameTicks)))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(6)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(btnGarbageCollect, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
										.addComponent(lblMemoryUsage)
										.addComponent(lblThreadCount))
									.addGap(18)
									.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
										.addComponent(threadCount)
										.addComponent(memUsage))))))
					.addContainerGap(22, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(17)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblServerName)
						.addComponent(serverName))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblServerRevision)
						.addComponent(revisionLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTickRate)
						.addComponent(tickRate))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblGameTicks)
						.addComponent(gameTicks))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblBadTicks)
						.addComponent(badTicks))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLastTickTime)
						.addComponent(lastTickTime))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDataSent)
						.addComponent(dataS))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDataRecieved)
						.addComponent(dataR))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMemoryUsage)
						.addComponent(memUsage))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblThreadCount)
						.addComponent(threadCount))
					.addGap(207)
					.addComponent(btnGarbageCollect)
					.addContainerGap(18, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Administration", null, panel, null);
		
		JLabel lblNewLabel = new JLabel("Players Online:");
		
		playersOnlineLabel = new JLabel("0");
		
		playersOnlineList = new JList();
		
		JButton btnKickPlayer = new JButton("Kick Player");
		
		JButton btnBanPlayer = new JButton("Ban Player");
		
		JButton btnMutePlayer = new JButton("Mute Player");
		
		JButton btnJailPlayer = new JButton("Jail Player");
		
		JButton btnGiveItems = new JButton("Give Items");
		
		JButton btnResetPlayer = new JButton("Reset");
		
		JButton btnSavePlayer = new JButton("Save Player");
		
		JButton btnTeleportPlayer = new JButton("Tele Player");
		
		JButton btnMsgPlayer = new JButton("Msg. Player");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(19)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(playersOnlineList, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(btnMutePlayer, GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
								.addComponent(btnBanPlayer, GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
								.addComponent(btnKickPlayer, 0, 0, Short.MAX_VALUE)
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(btnGiveItems, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
										.addComponent(btnSavePlayer, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
										.addComponent(btnMsgPlayer, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 95, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(btnJailPlayer, 0, 0, Short.MAX_VALUE)
										.addComponent(btnTeleportPlayer, 0, 0, Short.MAX_VALUE)
										.addComponent(btnResetPlayer, 0, 0, Short.MAX_VALUE)))))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(playersOnlineLabel)))
					.addContainerGap(20, GroupLayout.PREFERRED_SIZE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(playersOnlineLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnMsgPlayer)
								.addComponent(btnTeleportPlayer))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnSavePlayer)
								.addComponent(btnResetPlayer))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnGiveItems)
								.addComponent(btnJailPlayer))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnKickPlayer)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnBanPlayer)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnMutePlayer))
						.addComponent(playersOnlineList, GroupLayout.PREFERRED_SIZE, 468, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(16, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Command line", null, panel_2, null);
		
		commandLinePane = new JTextPane();
		commandLinePane.selectAll();
		commandLinePane.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		commandLinePane.setEditable(false);
		commandLinePane.setAutoscrolls(true);
		commandLinePane.setBackground(Color.BLACK);
		commandLinePane.setForeground(Color.green);
		btnEnter = new JButton("Enter");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final String cmd = textField.getText();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						textField.setText("");
					}
				});
				executor.execute(new Runnable() {
					@Override
					public void run() {
						textField.setEnabled(false);
						textField.setEditable(false);
						Object[] args = cmd.split(" ");
						String cmd1 = (String)args[0];
						try {
							CommandFactory.getSingleton().invoke(cmd1, args);
						} catch (Exception e) {
							//Ignore
						}
						textField.setEditable(true);
						textField.setEnabled(true);
					}
				});
			}
		});
		
		textField = new JTextField();
		textField.setColumns(10);
		
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(commandLinePane, GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(textField, GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnEnter)))
					.addContainerGap())
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(commandLinePane, GroupLayout.PREFERRED_SIZE, 463, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnEnter)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel_2.setLayout(gl_panel_2);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Plugin Manager", null, panel_3, null);
		
		JLabel lblInstalledPlugins = new JLabel("Installed Plugins");
		
		JProgressBar progressBar = new JProgressBar();
		
		JLabel lblNewLabel_1 = new JLabel("Status label");
		
		table = new JTable();
		JButton btnRemove = new JButton("Remove");
		
		JButton btnUpdate = new JButton("Update");
		
		JSeparator separator_1 = new JSeparator();
		
		JLabel lblInstallPlugin = new JLabel("Install Plugin");
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		JLabel lblXmlUrl = new JLabel("Build URL:");
		
		JButton btnNewButton = new JButton("Install");
		
		JButton btnNewButton_1 = new JButton("Recompile plugins");
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING, false)
							.addGroup(Alignment.LEADING, gl_panel_3.createSequentialGroup()
								.addGap(143)
								.addComponent(lblInstalledPlugins))
							.addGroup(Alignment.LEADING, gl_panel_3.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
									.addComponent(btnRemove, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
									.addComponent(table, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)))
							.addGroup(Alignment.LEADING, gl_panel_3.createSequentialGroup()
								.addContainerGap()
								.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 204, GroupLayout.PREFERRED_SIZE)
								.addGap(26)
								.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(157)
							.addComponent(lblInstallPlugin))
						.addGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING)
							.addGroup(Alignment.LEADING, gl_panel_3.createSequentialGroup()
								.addContainerGap()
								.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 402, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panel_3.createSequentialGroup()
								.addGap(17)
								.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING, false)
									.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addGroup(Alignment.TRAILING, gl_panel_3.createSequentialGroup()
										.addComponent(lblXmlUrl)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 322, GroupLayout.PREFERRED_SIZE))
									.addComponent(btnNewButton_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
					.addContainerGap(22, Short.MAX_VALUE))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblInstalledPlugins)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(table, GroupLayout.PREFERRED_SIZE, 199, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnRemove)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnUpdate)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblInstallPlugin)
					.addGap(8)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblXmlUrl)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_1)
					.addPreferredGap(ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1))
					.addContainerGap())
		);
		panel_3.setLayout(gl_panel_3);
		frame.getContentPane().setLayout(groupLayout);
	}
}