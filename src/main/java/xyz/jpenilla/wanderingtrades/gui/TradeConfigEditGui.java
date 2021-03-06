package xyz.jpenilla.wanderingtrades.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.jpenilla.jmplib.HeadBuilder;
import xyz.jpenilla.jmplib.InputConversation;
import xyz.jpenilla.jmplib.ItemBuilder;
import xyz.jpenilla.wanderingtrades.WanderingTrades;
import xyz.jpenilla.wanderingtrades.config.Lang;
import xyz.jpenilla.wanderingtrades.config.TradeConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class TradeConfigEditGui extends GuiHolder {
    private final ItemStack enabledEnabled = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName(lang.get(Lang.GUI_TC_EDIT_ENABLED)).setLore(gui_toggle_lore).build();
    private final ItemStack enabledDisabled = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(lang.get(Lang.GUI_TC_EDIT_DISABLED)).setLore(gui_toggle_lore).build();
    private final ItemStack randomizedEnabled = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName(lang.get(Lang.GUI_TC_EDIT_RANDOMIZED)).setLore(gui_toggle_lore).build();
    private final ItemStack randomizedDisabled = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(lang.get(Lang.GUI_TC_EDIT_NOT_RANDOMIZED)).setLore(gui_toggle_lore).build();
    private final ItemStack invEnabled = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName(lang.get(Lang.GUI_TC_EDIT_INVINCIBLE)).setLore(gui_toggle_lore).build();
    private final ItemStack invDisabled = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(lang.get(Lang.GUI_TC_EDIT_NOT_INVINCIBLE)).setLore(gui_toggle_lore).build();
    private final ItemStack randAmount = new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(lang.get(Lang.GUI_TC_EDIT_RANDOM_AMOUNT)).build();
    private final ItemStack chance = new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).setName(lang.get(Lang.GUI_TC_EDIT_CHANCE)).build();
    private final ItemStack customName = new ItemBuilder(Material.PINK_STAINED_GLASS_PANE).setName(lang.get(Lang.GUI_TC_EDIT_CUSTOM_NAME)).build();
    private final ItemStack deleteButton = new HeadBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY5NzY0NjE1ZGQ5Y2EwNTk5YmQ5ODg1ZjIyMmFhNWVhNWI0NzZiZDFiOTNlOTYyODUzNjZkMWQ0YzEifX19")
            .setName(lang.get(Lang.GUI_TRADE_DELETE)).setLore(lang.get(Lang.GUI_CONFIG_DELETE_LORE)).build();

    private final String tradeConfig;

    public TradeConfigEditGui(String tradeConfig) {
        super(WanderingTrades.getInstance().getLang().get(Lang.GUI_TC_EDIT_TITLE) + tradeConfig, 45);
        this.tradeConfig = tradeConfig;
    }

    public Inventory getInventory() {
        inventory.clear();

        inventory.setItem(inventory.getSize() - 1, backButton);

        TradeConfig t = WanderingTrades.getInstance().getCfg().getTradeConfigs().get(tradeConfig);

        ItemStack enabled;
        if (t.isEnabled()) {
            enabled = enabledEnabled;
        } else {
            enabled = enabledDisabled;
        }
        ItemStack randomized;
        if (t.isRandomized()) {
            randomized = randomizedEnabled;
        } else {
            randomized = randomizedDisabled;
        }
        ItemStack inv;
        if (t.isInvincible()) {
            inv = invEnabled;
        } else {
            inv = invDisabled;
        }
        inventory.setItem(10, enabled);
        inventory.setItem(12, randomized);
        inventory.setItem(14, inv);

        ArrayList<String> randAmountLore = new ArrayList<>();
        randAmountLore.add(lang.get(Lang.GUI_VALUE_LORE) + "<color:#0092FF>" + t.getRandomAmount());
        randAmountLore.add(lang.get(Lang.GUI_EDIT_LORE));
        inventory.setItem(16, new ItemBuilder(randAmount).setLore(randAmountLore).build());

        ArrayList<String> chanceLore = new ArrayList<>();
        chanceLore.add(lang.get(Lang.GUI_VALUE_LORE) + "<color:#0092FF>" + t.getChance());
        chanceLore.add(lang.get(Lang.GUI_EDIT_LORE));
        inventory.setItem(28, new ItemBuilder(chance).setLore(chanceLore).build());

        ArrayList<String> customNameLore = new ArrayList<>();
        customNameLore.add(lang.get(Lang.GUI_VALUE_LORE) + "<white>" + t.getCustomName());
        customNameLore.add(lang.get(Lang.GUI_EDIT_LORE));
        inventory.setItem(30, new ItemBuilder(customName).setLore(customNameLore).build());

        inventory.setItem(inventory.getSize() - 11, deleteButton);

        IntStream.range(0, inventory.getSize()).forEach(slot -> {
            if (inventory.getItem(slot) == null) {
                inventory.setItem(slot, filler);
            }
        });

        return inventory;
    }

    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        Player p = (Player) event.getWhoClicked();
        ClickType click = event.getClick();
        if (event.getSlot() != event.getRawSlot()) {
            if (click.isKeyboardClick() || click.isShiftClick()) {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);

        if (backButton.isSimilar(item)) {
            p.closeInventory();
            new TradeListGui(tradeConfig).open(p);
        }

        TradeConfig t = WanderingTrades.getInstance().getCfg().getTradeConfigs().get(tradeConfig);

        if (enabledEnabled.isSimilar(item)) {
            t.setEnabled(false);
        }
        if (enabledDisabled.isSimilar(item)) {
            t.setEnabled(true);
        }

        if (randomizedEnabled.isSimilar(item)) {
            t.setRandomized(false);
        }
        if (randomizedDisabled.isSimilar(item)) {
            t.setRandomized(true);
        }

        if (invEnabled.isSimilar(item)) {
            t.setInvincible(false);
        }
        if (invDisabled.isSimilar(item)) {
            t.setInvincible(true);
        }

        if (randAmount.isSimilar(item)) {
            p.closeInventory();
            new InputConversation()
                    .onPromptText(player -> {
                        WanderingTrades.getInstance().getChat().sendParsed(player,
                                lang.get(Lang.MESSAGE_SET_RAND_AMOUNT_PROMPT)
                                        + "<reset>\n" + lang.get(Lang.MESSAGE_CURRENT_VALUE) + t.getRandomAmount()
                                        + "<reset>\n" + lang.get(Lang.MESSAGE_ENTER_NUMBER_OR_RANGE));
                        return "";
                    })
                    .onValidateInput(TradeConfigEditGui::validateIntRange)
                    .onConfirmText(this::onConfirmYesNo)
                    .onAccepted((player, s) -> {
                        t.setRandomAmount(s);
                        t.save(tradeConfig);
                        WanderingTrades.getInstance().getChat().sendParsed(player, lang.get(Lang.MESSAGE_EDIT_SAVED));
                        open(player);
                    })
                    .onDenied(this::onEditCancelled)
                    .start(p);
        }

        if (chance.isSimilar(item)) {
            p.closeInventory();
            new InputConversation()
                    .onPromptText(player -> {
                        WanderingTrades.getInstance().getChat().sendParsed(player,
                                lang.get(Lang.MESSAGE_SET_CHANCE_PROMPT)
                                        + "<reset>\n" + lang.get(Lang.MESSAGE_CURRENT_VALUE) + t.getChance()
                                        + "<reset>\n" + lang.get(Lang.MESSAGE_ENTER_NUMBER));
                        return "";
                    })
                    .onValidateInput(this::onValidateDouble0T1)
                    .onConfirmText(this::onConfirmYesNo)
                    .onAccepted((player, s) -> {
                        t.setChance(Double.parseDouble(s));
                        t.save(tradeConfig);
                        WanderingTrades.getInstance().getChat().sendParsed(player, lang.get(Lang.MESSAGE_EDIT_SAVED));
                        open(player);
                    })
                    .onDenied(this::onEditCancelled)
                    .start(p);
        }

        if (customName.isSimilar(item)) {
            p.closeInventory();
            new InputConversation()
                    .onPromptText(player -> {
                        WanderingTrades.getInstance().getChat().sendParsed(player,
                                lang.get(Lang.MESSAGE_CREATE_TITLE_OR_NONE_PROMPT)
                                        + "<reset>\n" + lang.get(Lang.MESSAGE_CURRENT_VALUE) + "<reset>" + t.getCustomName());
                        return "";
                    })
                    .onValidateInput((pl, s) -> true)
                    .onConfirmText(this::onConfirmYesNo)
                    .onAccepted((player, string) -> {
                        t.setCustomName(string);
                        t.save(tradeConfig);
                        open(player);
                    })
                    .onDenied(this::onEditCancelled)
                    .start(p);
        }

        if (deleteButton.isSimilar(item)) {
            p.closeInventory();
            new InputConversation()
                    .onPromptText((player -> {
                        WanderingTrades.getInstance().getChat().sendParsed(player, lang.get(Lang.MESSAGE_DELETE_PROMPT).replace("{TRADE_NAME}", tradeConfig));
                        WanderingTrades.getInstance().getChat().sendParsed(player, lang.get(Lang.MESSAGE_CONFIRM).replace("{KEY}", lang.get(Lang.MESSAGE_CONFIRM_KEY)));
                        return "";
                    }))
                    .onValidateInput(((player, s) -> {
                        if (s.equals(lang.get(Lang.MESSAGE_CONFIRM_KEY))) {
                            final File tcFile = new File(WanderingTrades.getInstance().getDataFolder() + "/trades/" + tradeConfig + ".yml");
                            try {
                                if (!tcFile.delete()) {
                                    WanderingTrades.getInstance().getLog().warn("File delete failed");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            WanderingTrades.getInstance().getCfg().load();
                            WanderingTrades.getInstance().getChat().sendParsed(player, lang.get(Lang.MESSAGE_EDIT_SAVED));
                            new TradeConfigListGui().open(player);
                        } else {
                            onEditCancelled(player, s);
                        }
                        return true;
                    }))
                    .start(p);
        }

        t.save(tradeConfig);

        getInventory();
    }

    public void reOpen(Player player) {
        Bukkit.getServer().getScheduler().runTaskLater(WanderingTrades.getInstance(), () -> new TradeConfigEditGui(tradeConfig).open(player), 1L);
    }

    public static boolean validateIntRange(Player p, String s) {
        if (s.contains(":")) {
            try {
                String[] split = s.split(":");
                if (validateInt(null, split[0]) && validateInt(null, split[1])) {
                    return true;
                } else {
                    WanderingTrades.getInstance().getChat().sendParsed(p, WanderingTrades.getInstance().getLang().get(Lang.MESSAGE_ENTER_NUMBER_OR_RANGE));
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        } else {
            return validateInt(p, s);
        }
    }

    public static boolean validateInt(Player player, String input) {
        try {
            int i = Integer.parseInt(input);
            if (i < 0) {
                WanderingTrades.getInstance().getChat().sendParsed(player, WanderingTrades.getInstance().getLang().get(Lang.MESSAGE_NUMBER_GTE_0));
                return false;
            }
        } catch (NumberFormatException ex) {
            WanderingTrades.getInstance().getChat().sendParsed(player, WanderingTrades.getInstance().getLang().get(Lang.MESSAGE_ENTER_NUMBER_OR_RANGE));
            return false;
        }
        return true;
    }
}
