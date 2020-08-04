package com.uzm.hylex.core.bungee.commands;

import com.google.common.collect.Lists;
import com.uzm.hylex.core.bungee.Bungee;
import com.uzm.hylex.core.skins.bungee.listeners.LoginListener;
import com.uzm.hylex.core.skins.shared.exception.SkinRequestException;
import com.uzm.hylex.core.skins.shared.storage.CooldownStorage;
import com.uzm.hylex.core.skins.shared.storage.Locale;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
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
    player.sendMessage(TextComponent.fromLegacyText("  §e- §f/" + label + " <nome> §7Altere sua skin."));
    player.sendMessage(TextComponent.fromLegacyText("  §e- §f/" + label + " clear §7Limpe sua skin atual."));
    player.sendMessage(TextComponent.fromLegacyText(""));
  }

  public SkinCommand(Bungee plugin) {
    super("skin");
    this.label = this.getName();
    this.plugin = plugin;
  }

  private boolean setSkin(CommandSender sender, ProxiedPlayer p, String skin) {
    return this.setSkin(sender, p, skin, true);
  }

  // if save is false, we won't save the skin skin name
  // because default skin names shouldn't be saved as the users custom skin
  private boolean setSkin(CommandSender sender, ProxiedPlayer p, String skin, boolean save) {
    if (skin.equalsIgnoreCase("null") || !LoginListener.validUsername(skin)) {
      sender.sendMessage(new TextComponent("§cA skin §f" + skin + " §cnão é válida."));
      return false;
    }

    if (!sender.hasPermission("hylex.staff") && CooldownStorage.hasCooldown(sender.getName())) {
      sender.sendMessage(new TextComponent(Locale.SKIN_COOLDOWN_NEW.replace("%s", "" + CooldownStorage.getCooldown(sender.getName()))));
      return false;
    }

    CooldownStorage.resetCooldown(sender.getName());
    CooldownStorage.setCooldown(sender.getName(), 50, TimeUnit.SECONDS);

    String oldSkinName = plugin.getSkinStorage().getPlayerSkin(p.getName());
    if (LoginListener.validUsername(skin)) {
      try {
        plugin.getMojangAPI().getUUID(skin);
        if (save) {
          plugin.getSkinStorage().setPlayerSkin(p.getName(), skin);
          plugin.getSkinApplier().applySkin(p);
        } else {
          plugin.getSkinApplier().applySkin(p, skin, null);
        }
        p.sendMessage(new TextComponent("§e* Sua skin foi atualizada com sucesso, reconectando ela atualizará para você."));
        return true;
      } catch (SkinRequestException e) {
        sender.sendMessage(new TextComponent("§cOcorreu um erro enquanto tentavamos buscar por essa skin."));
        CooldownStorage.setCooldown(sender.getName(), 20, TimeUnit.SECONDS);
        this.rollback(p, oldSkinName, save); // set custom skin name back to old one if there is an exception
      } catch (Exception e) {
        //e.printStackTrace(); //todo: not throw error without context
        CooldownStorage.setCooldown(sender.getName(), 20, TimeUnit.SECONDS);
        this.rollback(p, oldSkinName, save); // set custom skin name back to old one if there is an exception
      }
      return false;
    }
    return false;
  }

  private void rollback(ProxiedPlayer p, String oldSkinName, boolean save) {
    if (save)
      plugin.getSkinStorage().setPlayerSkin(p.getName(), oldSkinName != null ? oldSkinName : p.getName());
  }


  public void onSkinClear(ProxiedPlayer p) {
    this.onSkinClearOther(p, p);
  }

  public void onSkinClearOther(CommandSender sender, ProxiedPlayer target) {
    ProxyServer.getInstance().getScheduler().runAsync(Bungee.getInstance(), () -> {
      ProxiedPlayer p = target;
      String skin = plugin.getSkinStorage().getDefaultSkinNameIfEnabled(p.getName(), true);

      plugin.getSkinStorage().removePlayerSkin(p.getName());
      if (this.setSkin(sender, p, skin, false)) {
        sender.sendMessage(new TextComponent("§e* Sua skin foi limpada com sucesso."));
      }
    });
  }

  public void onSkinUpdate(ProxiedPlayer p) {
    this.onSkinUpdateOther(p, p);
  }

  public void onSkinUpdateOther(CommandSender sender, ProxiedPlayer target) {
    ProxyServer.getInstance().getScheduler().runAsync(Bungee.getInstance(), () -> {
      ProxiedPlayer p = target;
      String skin = plugin.getSkinStorage().getPlayerSkin(p.getName());

      // User has no custom skin set, get the default skin name / his skin
      if (skin == null)
        skin = plugin.getSkinStorage().getDefaultSkinNameIfEnabled(p.getName(), true);

      if (skin.contains(" ")) {
        sender.sendMessage(new TextComponent("§cNome inválido da skin."));
        return;
      }

      if (!plugin.getSkinStorage().forceUpdateSkinData(skin)) {
        sender.sendMessage(new TextComponent(Locale.ERROR_UPDATING_SKIN));
        return;
      }

      if (this.setSkin(sender, p, skin, false)) {
        sender.sendMessage(new TextComponent("§e* Sua skin foi atualizada com sucesso, reconectando ela atualizará para você."));
      }
    });
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
        String name = args[0];
        if (!sender.hasPermission("hylex.staff") && CooldownStorage.hasCooldown(sender.getName())) {
          sender.sendMessage(new TextComponent(Locale.SKIN_COOLDOWN_NEW.replace("%s", "" + CooldownStorage.getCooldown(sender.getName()))));
          return;
        }
        if (!name.equalsIgnoreCase("clear")) {
          ProxyServer.getInstance().getScheduler().runAsync(Bungee.getInstance(), () -> {
            this.setSkin(sender, player, name);
          });
        } else {
          ProxyServer.getInstance().getScheduler().runAsync(Bungee.getInstance(), () -> {
            this.onSkinClear(player);
          });
        }

        break;
      default:
        help(player, this.label);
        break;
    }
  }
}
