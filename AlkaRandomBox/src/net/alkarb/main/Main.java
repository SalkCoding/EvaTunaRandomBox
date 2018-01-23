package net.alkarb.main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.alkarb.boxmanager.Box;
import net.alkarb.boxmanager.BoxManager;
import net.alkarb.daily.DailyManager;
import net.alkarb.daily.TimeChecker;
import net.alkarb.event.BlockPlace;
import net.alkarb.event.InventoryClick;
import net.alkarb.event.PlayerInteract;
import net.alkarb.event.PlayerJoin;
import net.alkarb.untill.Constants;
import net.alkarb.untill.RandomBox;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {

	private static Main instance;

	public static Main getInstance() {
		if (instance == null) {
			throw new IllegalStateException(Constants.Console + "not initialized!");
		}
		return instance;
	}

	public Main() {
		if (instance == null) {
			instance = this;
		} else {
			throw new IllegalStateException(Constants.Console + "This class is singleton.");
		}
	}

	@Override
	public void onEnable() {
		BoxManager.loadList();
		DailyManager.loadList();
		Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new TimeChecker(), 20, 100);
		Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
		Bukkit.getPluginManager().registerEvents(new BlockPlace(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
		System.out.println(Constants.Console + "플러그인 작동중");
	}

	@Override
	public void onDisable() {
		System.out.println(Constants.Console + "플러그인 동작 종료");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("랜덤박스")) {
			if (!(sender.isOp())) {
				sender.sendMessage(Constants.Format + ChatColor.RED + "권한이 없습니다.");
			}
			if (args.length == 0) {
				sender.sendMessage(Constants.Format + ChatColor.RED + "잘못된 명령어 입니다.");
				sender.sendMessage(Constants.Format + ChatColor.WHITE + "/랜덤박스 지급 [박스이름] [닉네임] " + ChatColor.YELLOW
						+ "플레이어에게 해당 박스 지급");
				sender.sendMessage(
						Constants.Format + ChatColor.WHITE + "/랜덤박스 전체지급 [박스이름] " + ChatColor.YELLOW + "모두에게 해당 박스 지급");
				sender.sendMessage(
						Constants.Format + ChatColor.WHITE + "/랜덤박스 reload " + ChatColor.YELLOW + "Config reload");
				return true;
			}
			if (args[0].equalsIgnoreCase("지급")) {
				if (args.length == 4) {
					Box box = BoxManager.getBoxName(args[1]);
					if (box == null) {
						sender.sendMessage(Constants.Format + ChatColor.RED + args[1] + "이라는 아이템은 존재하지 않습니다.");
						return true;
					}
					Player target = Bukkit.getPlayer(args[2]);
					if (target == null) {
						sender.sendMessage(Constants.Format + ChatColor.RED + args[2] + "라는 플레이어가 존재하지 않습니다.");
						return true;
					}
					try {
						int amount = Integer.parseInt(args[3]);
						RandomBox.giveBox(target, box, amount);
					} catch (NumberFormatException e) {
						sender.sendMessage(Constants.Format + ChatColor.RED + "잘못된 명령어 입니다.");
						sender.sendMessage(Constants.Format + ChatColor.WHITE + "/랜덤박스 지급 [박스이름] [닉네임] [갯수]");
						return true;
					}
					sender.sendMessage(Constants.Format + ChatColor.GOLD + box.getDisplayName() + ChatColor.GREEN
							+ "을/를 " + ChatColor.LIGHT_PURPLE + target.getDisplayName() + ChatColor.GREEN
							+ "에게 지급 하였습니다.");
					target.sendMessage(Constants.Format + ChatColor.LIGHT_PURPLE + sender.getName() + ChatColor.GREEN
							+ "이/가 당신에게 " + ChatColor.GOLD + box.getDisplayName() + ChatColor.GREEN + "을/를 지급하였습니다.");
					return true;
				} else {
					sender.sendMessage(Constants.Format + ChatColor.RED + "잘못된 명령어 입니다.");
					sender.sendMessage(Constants.Format + ChatColor.WHITE + "/랜덤박스 지급 [박스이름] [닉네임] [갯수] "
							+ ChatColor.YELLOW + "플레이어에게 해당 박스 지급");
					sender.sendMessage(Constants.Format + ChatColor.WHITE + "/랜덤박스 전체지급 [박스이름] [갯수] " + ChatColor.YELLOW
							+ "모두에게 해당 박스 지급");
					sender.sendMessage(
							Constants.Format + ChatColor.WHITE + "/랜덤박스 reload " + ChatColor.YELLOW + "Config reload");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("전체지급")) {
				if (args.length == 3) {
					Box box = BoxManager.getBoxName(args[1]);
					if (box == null) {
						sender.sendMessage(Constants.Format + ChatColor.RED + args[1] + "이라는 아이템은 존재하지 않습니다.");
						return true;
					}
					int amount = 0;
					try {
						amount = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage(Constants.Format + ChatColor.RED + "잘못된 명령어 입니다.");
						sender.sendMessage(Constants.Format + ChatColor.WHITE + "/랜덤박스 전체지급 [박스이름] [갯수]");
						return true;
					}
					for (Player player : Bukkit.getOnlinePlayers()) {
						RandomBox.giveBox(player, box, amount);
						player.sendMessage(Constants.Format + ChatColor.LIGHT_PURPLE + sender.getName()
								+ ChatColor.GREEN + "이/가 모두에게 " + ChatColor.GOLD + box.getDisplayName()
								+ ChatColor.GREEN + "을/를 지급하였습니다.");
					}
					return true;
				} else {
					sender.sendMessage(Constants.Format + ChatColor.RED + "잘못된 명령어 입니다.");
					sender.sendMessage(Constants.Format + ChatColor.WHITE + "/랜덤박스 지급 [박스이름] [닉네임] [갯수] "
							+ ChatColor.YELLOW + "플레이어에게 해당 박스 지급");
					sender.sendMessage(Constants.Format + ChatColor.WHITE + "/랜덤박스 전체지급 [박스이름] [갯수] " + ChatColor.YELLOW
							+ "모두에게 해당 박스 지급");
					sender.sendMessage(
							Constants.Format + ChatColor.WHITE + "/랜덤박스 reload " + ChatColor.YELLOW + "Config reload");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("reload")) {
				if (args.length != 1) {
					sender.sendMessage(Constants.Format + ChatColor.RED + "잘못된 명령어 입니다.");
					sender.sendMessage(Constants.Format + ChatColor.WHITE + "/랜덤박스 지급 [박스이름] [닉네임] [갯수] "
							+ ChatColor.YELLOW + "플레이어에게 해당 박스 지급");
					sender.sendMessage(Constants.Format + ChatColor.WHITE + "/랜덤박스 전체지급 [박스이름] [갯수] " + ChatColor.YELLOW
							+ "모두에게 해당 박스 지급");
					sender.sendMessage(
							Constants.Format + ChatColor.WHITE + "/랜덤박스 reload " + ChatColor.YELLOW + "Config reload");
					return true;
				}
				BoxManager.loadList();
				sender.sendMessage(Constants.Format + ChatColor.GREEN + "reload 완료!");
				return true;
			}
		}
		return false;
	}
}
