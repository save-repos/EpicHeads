package com.songoda.epicheads.listeners;

import com.songoda.epicheads.EpicHeads;
import com.songoda.epicheads.head.Head;
import com.songoda.epicheads.head.HeadManager;
import com.songoda.epicheads.head.Tag;
import com.songoda.epicheads.utils.Methods;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Optional;

public class LoginListeners implements Listener {

    private final EpicHeads plugin;

    public LoginListeners(EpicHeads plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void loginEvent(PlayerLoginEvent event) {
        HeadManager headManager = plugin.getHeadManager();

        Player player = event.getPlayer();

        String encodededStr = Methods.getEncodedTexture(player);
        String url = Methods.getDecodedTexture(encodededStr);


        String tagStr = "Player Heads";

        Optional<Tag> tagOptional = headManager.getTags()
                .stream().filter(t -> t.getName().equalsIgnoreCase(tagStr)).findFirst();

        Tag tag = tagOptional.orElseGet(() -> new Tag(tagStr));

        if (!tagOptional.isPresent())
            headManager.addTag(tag);

        Optional<Head> optional = headManager.getLocalHeads().stream()
                .filter(h -> h.getName().equalsIgnoreCase(event.getPlayer().getName())).findFirst();

        int id = headManager.getNextLocalId();

        if (optional.isPresent()) {
            Head head = optional.get();
            id = head.getId();
            headManager.removeLocalHead(head);
        }

        headManager.addLocalHeads(new Head(id, player.getName(), url, tag, (byte) 0));

    }

}
