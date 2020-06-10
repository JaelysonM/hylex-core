package com.uzm.hylex.core.bungee.commands;

import com.google.common.collect.Lists;
import com.uzm.hylex.core.bungee.Bungee;
import com.uzm.hylex.core.skins.bukkit.PlayerJoin;
import com.uzm.hylex.core.skins.bungee.listeners.LoginListener;
import com.uzm.hylex.core.skins.shared.exception.SkinRequestException;
import com.uzm.hylex.core.skins.shared.storage.CooldownStorage;
import com.uzm.hylex.core.skins.shared.storage.Locale;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class SkinCommand extends Command {

  private String label;

  private Bungee plugin;

  public static ArrayList<String> getInvoke() {
    return Lists.newArrayList("skin");
  }

  public void help(ProxiedPlayer player, String label) {
    player.sendMessage(TextComponent.fromLegacyText(""));
    player.sendMessage(TextComponent.fromLegacyText("   §eAjuda do comando §f'" + label + "'"));
    player.sendMessage(TextComponent.fromLegacyText(""));
    player.sendMessage(TextComponent  .fromLegacyText("  §e- §f/" + label + " <nome> §7Altere sua skin."));
    player.sendMessage(TextComponent.fromLegacyText("  §e- §f/" + label + " clear §7Limpe sua skin atual."));
    player.sendMessage(TextComponent.fromLegacyText(""));
  }

  public SkinCommand(Bungee plugin) {
    super("skin");
    this.label = this.getName();
    this.plugin = plugin;
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§fHey brother, stop do it! You cannot execute commands."));
      return;
    }

    if (!sender.hasPermission("hylex.skin")) {
      sender.sendMessage(TextComponent.fromLegacyText("§c[Stone] §cSem §c§npermissão §cpara executar esse comando."));
      return;
    }
    ProxiedPlayer player = (ProxiedPlayer) sender;




    switch (args.length) {
      case 1:
        if (!sender.hasPermission("skinsrestorer.bypasscooldown") && CooldownStorage.hasCooldown(sender.getName())) {
          sender.sendMessage(new TextComponent(Locale.SKIN_COOLDOWN_NEW.replace("%s", "" + CooldownStorage.getCooldown(sender.getName()))));
          return;
        }

        String name = args[0];
        if (!name.equalsIgnoreCase("clear")) {
          String oldSkinName = plugin.getSkinStorage().getPlayerSkin(player.getName());
          if (LoginListener.validUsername(name)) {
            try {
              plugin.getMojangAPI().getUUID(name);
              plugin.getSkinStorage().setPlayerSkin(player.getName(), name);
              plugin.getSkinApplier().applySkin(player);

              player.sendMessage(new TextComponent("§e* Sua skin foi alterada com sucesso"));
              break;
            } catch (SkinRequestException e) {
              sender.sendMessage(new TextComponent(e.getReason()));
              CooldownStorage.setCooldown(sender.getName(), 20, TimeUnit.SECONDS);
              player.sendMessage(new TextComponent("§cOcorreu um erro enquanto tentavamos alterar sua skin."));
              this.rollback(player, oldSkinName, true); // set custom skin name back to old one if there is an exception
            } catch (Exception e) {
              //e.printStackTrace(); //todo: not throw error without context
              CooldownStorage.setCooldown(sender.getName(), 20, TimeUnit.SECONDS);
              this.rollback(player, oldSkinName, true); // set custom skin name back to old one if there is an exception
            }
            CooldownStorage.resetCooldown(sender.getName());
            CooldownStorage.setCooldown(sender.getName(), 20, TimeUnit.SECONDS);
            break;
          }else {
            player.sendMessage(new TextComponent("§cNome de usuário inválido."));
          }
        }else {
          Bungee.getInstance().getProxy().getScheduler().runAsync(Bungee.getInstance(), () -> {

            String skin = plugin.getSkinStorage().getDefaultSkinNameIfEnabled(player.getName(), true);

            // remove users custom skin and set default skin / his skin
            plugin.getSkinStorage().removePlayerSkin(player.getName());

            String oldSkinName = plugin.getSkinStorage().getPlayerSkin(player.getName());
            if (LoginListener.validUsername(name)) {
              try {
                plugin.getMojangAPI().getUUID(name);
                plugin.getSkinApplier().applySkin(player, skin, null);

                player.sendMessage(new TextComponent("§e* Sua skin foi limpada com sucesso."));

              } catch (SkinRequestException e) {
                sender.sendMessage(new TextComponent(e.getReason()));
                CooldownStorage.setCooldown(sender.getName(), 20, TimeUnit.SECONDS);
                player.sendMessage(new TextComponent("§cOcorreu um erro enquanto tentavamos alterar sua skin."));
                this.rollback(player, oldSkinName, true); // set custom skin name back to old one if there is an exception
              } catch (Exception e) {
                //e.printStackTrace(); //todo: not throw error without context
                CooldownStorage.setCooldown(sender.getName(), 20, TimeUnit.SECONDS);
                this.rollback(player, oldSkinName, true); // set custom skin name back to old one if there is an exception
              }
            }


          });
        }
        break;
      default:
        help(player, label);
        break;
    }
  }

  private void rollback(ProxiedPlayer p, String oldSkinName, boolean save) {
    if (save)
      plugin.getSkinStorage().setPlayerSkin(p.getName(), oldSkinName != null ? oldSkinName : p.getName());
  }

}
