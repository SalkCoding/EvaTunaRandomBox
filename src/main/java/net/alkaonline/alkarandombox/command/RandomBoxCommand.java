package net.alkaonline.alkarandombox.command;

import net.alkaonline.alkarandombox.boxmanager.Box;
import net.alkaonline.alkarandombox.boxmanager.BoxManager;
import net.alkaonline.alkarandombox.untill.Constants;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class RandomBoxCommand implements CommandExecutor {

    private final BoxManager boxManager;

    public RandomBoxCommand(BoxManager boxManager) {
        this.boxManager = boxManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender.isOp())) {
            sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.RED + "권한이 없습니다.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.RED + "잘못된 명령어입니다.");
            sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.WHITE + "/랜덤박스 지급 [박스이름] [닉네임] " + ChatColor.YELLOW
                    + "플레이어에게 해당 박스 지급");
            sender.sendMessage(
                    Constants.MESSAGE_PREFIX + ChatColor.WHITE + "/랜덤박스 전체지급 [박스이름] " + ChatColor.YELLOW + "모두에게 해당 박스 지급");
            sender.sendMessage(
                    Constants.MESSAGE_PREFIX + ChatColor.WHITE + "/랜덤박스 reload " + ChatColor.YELLOW + "Config reload");
            return true;
        }
        if (args[0].equalsIgnoreCase("지급")) {
            if (args.length == 4) {
                Box box = boxManager.getBoxByName(args[1]);
                if (box == null) {
                    sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.RED + args[1] + "이라는 아이템은 존재하지 않습니다.");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.RED + args[2] + "라는 플레이어가 존재하지 않습니다.");
                    return true;
                }
                try {
                    int amount = Integer.parseInt(args[3]);
                    box.give(target, amount);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.RED + "잘못된 명령어 입니다.");
                    sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.WHITE + "/랜덤박스 지급 [박스이름] [닉네임] [갯수]");
                    return true;
                }
                sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.GOLD + box.getDisplayName() + ChatColor.GREEN
                        + "을/를 " + ChatColor.LIGHT_PURPLE + target.getDisplayName() + ChatColor.GREEN
                        + "에게 지급 하였습니다.");
                target.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.LIGHT_PURPLE + sender.getName() + ChatColor.GREEN
                        + "이/가 당신에게 " + ChatColor.GOLD + box.getDisplayName() + ChatColor.GREEN + "을/를 지급하였습니다.");
                return true;
            } else {
                sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.RED + "잘못된 명령어 입니다.");
                sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.WHITE + "/랜덤박스 지급 [박스이름] [닉네임] [갯수] "
                        + ChatColor.YELLOW + "플레이어에게 해당 박스 지급");
                sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.WHITE + "/랜덤박스 전체지급 [박스이름] [갯수] " + ChatColor.YELLOW
                        + "모두에게 해당 박스 지급");
                sender.sendMessage(
                        Constants.MESSAGE_PREFIX + ChatColor.WHITE + "/랜덤박스 reload " + ChatColor.YELLOW + "Config reload");
                return true;
            }
        } else if (args[0].equalsIgnoreCase("전체지급")) {
            if (args.length == 3) {
                Box box = boxManager.getBoxByName(args[1]);
                if (box == null) {
                    sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.RED + args[1] + "이라는 아이템은 존재하지 않습니다.");
                    return true;
                }
                int amount;
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.RED + "잘못된 명령어입니다.");
                    sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.WHITE + "/랜덤박스 전체지급 [박스이름] [갯수]");
                    return true;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    box.give(player, amount);
                    player.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.LIGHT_PURPLE + sender.getName()
                            + ChatColor.GREEN + "이/가 모두에게 " + ChatColor.GOLD + box.getDisplayName()
                            + ChatColor.GREEN + "을/를 지급하였습니다.");
                }
                return true;
            } else {
                sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.RED + "잘못된 명령어 입니다.");
                sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.WHITE + "/랜덤박스 지급 [박스이름] [닉네임] [갯수] "
                        + ChatColor.YELLOW + "플레이어에게 해당 박스 지급");
                sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.WHITE + "/랜덤박스 전체지급 [박스이름] [갯수] " + ChatColor.YELLOW
                        + "모두에게 해당 박스 지급");
                sender.sendMessage(
                        Constants.MESSAGE_PREFIX + ChatColor.WHITE + "/랜덤박스 reload " + ChatColor.YELLOW + "Config reload");
                return true;
            }
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (args.length != 1) {
                sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.RED + "잘못된 명령어 입니다.");
                sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.WHITE + "/랜덤박스 지급 [박스이름] [닉네임] [갯수] "
                        + ChatColor.YELLOW + "플레이어에게 해당 박스 지급");
                sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.WHITE + "/랜덤박스 전체지급 [박스이름] [갯수] " + ChatColor.YELLOW
                        + "모두에게 해당 박스 지급");
                sender.sendMessage(
                        Constants.MESSAGE_PREFIX + ChatColor.WHITE + "/랜덤박스 reload " + ChatColor.YELLOW + "Config reload");
                return true;
            }
            try {
                boxManager.loadBoxList();
            } catch (IOException e) {
                sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.RED + "오류 발생!");
                return true;
            }
            sender.sendMessage(Constants.MESSAGE_PREFIX + ChatColor.GREEN + "reload 완료!");
            return true;
        }
        return false;
    }

}
