package org.tuffplugin.dcplugin;

import org.bukkit.*;
import org.bukkit.block.spawner.SpawnerEntry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Attr;

import javax.annotation.Nullable;

public class myListener implements Listener{
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        final Player player = event.getPlayer(); // get the player that just joined
        player.sendMessage(ChatColor.YELLOW + "Welcome, " + player.getName());
        player.sendMessage(ChatColor.YELLOW + "Custom cmds: /moblore /temp ");
        // remove join message
        event.setJoinMessage(ChatColor.YELLOW + player.getName() + " has joined the server."); // null = remove join message, but can set to anything if you want
    }
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
            LivingEntity entity = event.getEntity();

            if ((Math.random() <0.07)&&(entity instanceof Zombie || entity instanceof Skeleton || entity instanceof Phantom))  { //7 % chance of spawning, only check if entity is valid to be enhanced
                enhanceMob(entity);
            }

        }
    }
    @EventHandler
    public void onSkeletonShoot(EntityShootBowEvent event) {
        // check if shooter is skeleton
        if (event.getEntity() instanceof Skeleton) {
            //check projectile, but first check skeleton name
            Skeleton skeleton = (Skeleton) event.getEntity();
            if (skeleton.getCustomName() != null && skeleton.getCustomName().equals("Trickster Skeleton")) {
                if (event.getProjectile() instanceof Arrow) {
                    Arrow arrow = (Arrow) event.getProjectile();
                    arrow.addCustomEffect(new PotionEffect(PotionEffectType.DARKNESS,130, 0), true); // darkness 1 for 6.5 seconds ( 20 ticks : 1 second )
                    arrow.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS,20, 0), true); // blindness 1 for 1 second ( 20 ticks : 1 second )
                }
            }
            else if (skeleton.getCustomName() != null && skeleton.getCustomName().equals("Sapper Skeleton")) {
                if (event.getProjectile() instanceof Arrow) {
                    Arrow arrow = (Arrow) event.getProjectile();
                    arrow.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, 100, 2), true); // weakness 3 for 5 seconds  ( 20 ticks : 1 second )
                    arrow.addCustomEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 100, 2), true); // mining fatigue 3 for 5 seconds  ( 20 ticks : 1 second )
                }
            }
            else if (skeleton.getCustomName() != null && skeleton.getCustomName().equals("Curser Skeleton")) {
                if (event.getProjectile() instanceof Arrow) {
                    Arrow arrow = (Arrow) event.getProjectile();
                    arrow.addCustomEffect(new PotionEffect(PotionEffectType.INFESTED, 280, 1), true); // infested 2 for 14 seconds  ( 20 ticks : 1 second )
                    arrow.addCustomEffect(new PotionEffect(PotionEffectType.HUNGER, 280, 1), true); // hunger 2 for 14 seconds  ( 20 ticks : 1 second )
                }
            }
        }
        else if (event.getEntity() instanceof Player) { // This is the custom arrows part where it depends on a player and a weapon name to have secret powers. Just change Player to a username
            Player player = (Player) event.getEntity();
            String playername = player.getName();
            EntityEquipment equipment = player.getEquipment();
            ItemStack Item = equipment.getItemInMainHand();
            ItemMeta meta = Item.getItemMeta();
            String itemName = meta.getDisplayName();
            if (playername.equals("Player") && itemName.equals("Slayer")) {
                Arrow arrow = (Arrow) event.getProjectile();
                arrow.setVelocity(arrow.getVelocity().multiply(1.67));
            }
            else if (playername.equals("Player") && itemName.equals("Wither Bow")) {
                Arrow arrow = (Arrow) event.getProjectile();
                arrow.setVelocity(arrow.getVelocity().multiply(1.1));
            }
            else if (playername.equals("Player") && itemName.equals("Exterminator")) {
                Arrow arrow = (Arrow) event.getProjectile();
                arrow.setVelocity(arrow.getVelocity().multiply(1.3));
                arrow.getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_SHOOT, 1.0f, 1.0f);
                arrow.setDamage(arrow.getDamage()+4);
            }
            else if (playername.equals("Player") && itemName.equals("Planetary Death Ray")) {
                Arrow arrow = (Arrow) event.getProjectile();
                arrow.setVelocity(arrow.getVelocity().multiply(2));
                //AreaEffectCloud cloud = (AreaEffectCloud) arrow.getWorld().spawnEntity(arrow.getLocation(), EntityType.AREA_EFFECT_CLOUD);
                //cloud.setRadius(4.0f);
                //cloud.setDuration(20);
                //cloud.setParticle(Particle.SOUL_FIRE_FLAME);
                //arrow.addPassenger(cloud);
            }
            else if (playername.equals("Player") && itemName.equals("Sniper")) {
                Arrow arrow = (Arrow) event.getProjectile();
                arrow.setVelocity(arrow.getVelocity().multiply(1.33));
                arrow.setDamage(arrow.getDamage()*2);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (arrow.isDead() || arrow.isOnGround()) {
                            this.cancel();
                            return;
                        }

                        // Spawn particle at arrow location
                        arrow.getWorld().spawnParticle(Particle.FLAME, arrow.getLocation(), 1, 0, 0, 0, 0);
                    }
                };
            }
            //Bukkit.getConsoleSender().sendMessage("Arrow shot by " + playername + " , bow name: " + itemName);
        }
    }
    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            if (event.getEntity().getShooter() instanceof Player) {
                Player shooter = (Player) event.getEntity().getShooter();
                String playername = shooter.getName();
                EntityEquipment equipment = shooter.getEquipment();
                ItemStack Item = equipment.getItemInMainHand();
                ItemMeta meta = Item.getItemMeta();
                String itemName = meta.getDisplayName();
                if (playername.equals("Player") && itemName.equals("Slayer")) {
                    Location location = event.getEntity().getLocation();
                    event.getEntity().getWorld().createExplosion(location, 2.0F, false, true);
                    Bukkit.getConsoleSender().sendMessage("Slayer Explosion attempted by " + playername);
                }
                else if (playername.equals("Player") && itemName.equals("Wither Bow")) {
                    Location location = event.getEntity().getLocation();
                    AreaEffectCloud witherCloud = (AreaEffectCloud) event.getEntity().getWorld().spawnEntity(location, EntityType.AREA_EFFECT_CLOUD);
                    witherCloud.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, 200, 0), true);
                    witherCloud.addCustomEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 0), true);
                    witherCloud.setParticle(Particle.SQUID_INK);
                    witherCloud.setRadius(0.5f);
                    witherCloud.setRadiusPerTick(0.05f);
                    witherCloud.setDuration(45);
                    witherCloud.setWaitTime(10);
                    witherCloud.getWorld().playSound(location, Sound.ENTITY_WITHER_AMBIENT, 1.0f, 1.0f);
                }
                else if (playername.equals("Player") && itemName.equals("Exterminator")) {
                    Location location = event.getEntity().getLocation();
                    event.getEntity().getWorld().createExplosion(location, 2.5F, false, false); // fire? break blocks?
                    AreaEffectCloud witherCloud = (AreaEffectCloud) event.getEntity().getWorld().spawnEntity(location, EntityType.AREA_EFFECT_CLOUD);
                    witherCloud.addCustomEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE, 250, 0), true);
                    witherCloud.setParticle(Particle.FLAME);
                    witherCloud.setRadius(1.0f);
                    witherCloud.setRadiusPerTick(0.24f);
                    witherCloud.setDuration(32);
                    witherCloud.setWaitTime(5);
                    witherCloud.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 1.0f);
                }
                else if (playername.equals("Player") && itemName.equals("Planetary Death Ray")) {
                    Location location = event.getEntity().getLocation();
                    event.getEntity().getWorld().createExplosion(location, 10.0F, true, true);
                    Bukkit.getConsoleSender().sendMessage("PDR Explosion attempted by " + playername);
                }
            }

        }
    }
    private void enhanceMob(LivingEntity entity) {
        //Check mob
        double chance = Math.random();
        if (entity instanceof Zombie) {

            Zombie zombie = (Zombie) entity;
            EntityEquipment equipment = zombie.getEquipment();
            if (!zombie.isAdult()) { // if a baby then stop
                return;
            }
            if (chance < 0.42) {
                zombie.setCustomName("Enhanced " + zombie.getName());
                if (equipment != null) {
                    equipment.setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
                    equipment.setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
                    equipment.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
                    equipment.setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));

                    //equipment.setHelmetDropChance(0.1f);
                    //equipment.setChestplateDropChance(0.1f);
                    //equipment.setLeggingsDropChance(0.1f);
                    //equipment.setBootsDropChance(0.1f);

                    equipment.setItemInMainHand(new ItemStack(Material.IRON_AXE));

                    //equipment.setItemInMainHandDropChance(0.1f);
                }
                zombie.getAttribute(Attribute.MAX_HEALTH).setBaseValue(40.0);
                zombie.setHealth(40.0);
                zombie.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(4.5);
                zombie.getAttribute(Attribute.SCALE).setBaseValue(1.025641026);
                zombie.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.265);
                zombie.getAttribute(Attribute.KNOCKBACK_RESISTANCE).setBaseValue(0.71);
                zombie.getAttribute(Attribute.SPAWN_REINFORCEMENTS).setBaseValue(0.1);
            } else if (chance > 0.42 && chance < 0.84) {
                zombie.setCustomName("Rusher " + zombie.getName());
                equipment.setHelmet(new ItemStack(Material.CARVED_PUMPKIN));
                equipment.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                equipment.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                equipment.setBoots(new ItemStack(Material.LEATHER_BOOTS));

                ItemStack enchantedsword = new ItemStack(Material.WOODEN_SWORD);
                enchantedsword.addEnchantment(Enchantment.KNOCKBACK, 1);
                equipment.setItemInMainHand(enchantedsword);

                //zombie.getAttribute(Attribute.).setBaseValue();
                zombie.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.37);
                zombie.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(2);
                zombie.getAttribute(Attribute.KNOCKBACK_RESISTANCE).setBaseValue(0.4);
                zombie.getAttribute(Attribute.FOLLOW_RANGE).setBaseValue(41);
                zombie.getAttribute(Attribute.STEP_HEIGHT).setBaseValue(1);
            } else {
                zombie.setCustomName("Coal Head " + zombie.getName());
                equipment.setHelmet(new ItemStack(Material.COAL_BLOCK));
                ItemStack enchantedpick = new ItemStack(Material.IRON_PICKAXE);
                ItemMeta meta = enchantedpick.getItemMeta();

                meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
                meta.addEnchant(Enchantment.BREACH, 4, true);
                List<String> lore = new ArrayList<>();
                lore.add("A brimstone infused pickaxe, strong enough to pierce armor.");
                meta.setLore(lore);
                meta.setDisplayName("Burning Iron Pickaxe");
                //enchantedpick.addEnchantment(Enchantment.FIRE_ASPECT, 1);
                enchantedpick.setItemMeta(meta);
                equipment.setItemInMainHand(enchantedpick);
                equipment.setItemInMainHandDropChance(0.05f);
                equipment.setHelmetDropChance(0.4f);
                ItemStack chestplate = new ItemStack(Material.GOLDEN_CHESTPLATE);
                ItemMeta meta2 = chestplate.getItemMeta();
                ArmorTrim trim = new ArmorTrim(TrimMaterial.RESIN, TrimPattern.SILENCE);

                if (meta2 instanceof ArmorMeta) {
                    ArmorMeta armorMeta = (ArmorMeta) meta2;
                    armorMeta.setTrim(trim);
                    chestplate.setItemMeta(armorMeta);
                }
                equipment.setChestplate(chestplate);
                zombie.getAttribute(Attribute.ARMOR).setBaseValue(8);
                zombie.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.255);
                zombie.getAttribute(Attribute.KNOCKBACK_RESISTANCE).setBaseValue(0.92);
                zombie.getAttribute(Attribute.MAX_HEALTH).setBaseValue(50);
                zombie.setHealth(50);
                zombie.getAttribute(Attribute.SPAWN_REINFORCEMENTS).setBaseValue(0.55);

            }
        } else if (entity instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) entity;

            EntityEquipment equipment = skeleton.getEquipment();
            if (chance <0.33) {
                skeleton.setCustomName("Trickster " + skeleton.getName());
                equipment.setHelmet(new ItemStack(Material.IRON_HELMET));
                equipment.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                equipment.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                equipment.setBoots(new ItemStack(Material.LEATHER_BOOTS));

                ItemStack enchantedbow = new ItemStack(Material.BOW);
                ItemMeta meta = enchantedbow.getItemMeta();
                meta.addEnchant(Enchantment.PUNCH, 2, true);
                enchantedbow.setItemMeta(meta);
                equipment.setItemInMainHand(enchantedbow);


                skeleton.getAttribute(Attribute.BURNING_TIME).setBaseValue(0);
                skeleton.getAttribute(Attribute.KNOCKBACK_RESISTANCE).setBaseValue(0.33);
                skeleton.getAttribute(Attribute.FOLLOW_RANGE).setBaseValue(24);
            } else if (chance > 0.33 && chance < 0.66) {
                skeleton.setCustomName("Sapper " + skeleton.getName());
                equipment.setHelmet(new ItemStack(Material.GOLDEN_HELMET));
                equipment.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                equipment.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                equipment.setBoots(new ItemStack(Material.LEATHER_BOOTS));

                ItemStack enchantedbow = new ItemStack(Material.BOW);
                ItemMeta meta = enchantedbow.getItemMeta();
                meta.addEnchant(Enchantment.MULTISHOT, 1, true);
                enchantedbow.setItemMeta(meta);
                equipment.setItemInMainHand(enchantedbow);

                skeleton.getAttribute(Attribute.BURNING_TIME).setBaseValue(0);
                skeleton.getAttribute(Attribute.KNOCKBACK_RESISTANCE).setBaseValue(0.22);
                skeleton.getAttribute(Attribute.FOLLOW_RANGE).setBaseValue(24);
            }
            else {
                skeleton.setCustomName("Curser " + skeleton.getName());
                equipment.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                equipment.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                equipment.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                equipment.setBoots(new ItemStack(Material.LEATHER_BOOTS));

                ItemStack enchantedbow = new ItemStack(Material.BOW);
                ItemMeta meta = enchantedbow.getItemMeta();
                meta.addEnchant(Enchantment.PUNCH, 1, true);
                meta.addEnchant(Enchantment.PIERCING, 2, true);
                enchantedbow.setItemMeta(meta);
                equipment.setItemInMainHand(enchantedbow);

                skeleton.getAttribute(Attribute.BURNING_TIME).setBaseValue(0);
                skeleton.getAttribute(Attribute.KNOCKBACK_RESISTANCE).setBaseValue(0.33);
                skeleton.getAttribute(Attribute.FOLLOW_RANGE).setBaseValue(24);
            }
        } else if (entity instanceof Phantom) {
            Phantom phantom = (Phantom) entity;
            phantom.setCustomName("Day Phantom");
            phantom.getAttribute(Attribute.SCALE).setBaseValue(1.6);
            phantom.getAttribute(Attribute.BURNING_TIME).setBaseValue(0);
            phantom.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(11);
            phantom.getAttribute(Attribute.MAX_HEALTH).setBaseValue(30);
            phantom.getAttribute(Attribute.FOLLOW_RANGE).setBaseValue(44);
            phantom.getAttribute(Attribute.KNOCKBACK_RESISTANCE).setBaseValue(0.6);
            phantom.getAttribute(Attribute.ATTACK_KNOCKBACK).setBaseValue(1.2);
            phantom.setHealth(30);
        }
        //@EventHandler
        //public void onBlockBreak(BlockBreakEvent event){
        //    final Player player = event.getPlayer();
        //    player.sendMessage(ChatColor.RED + "You cant break blocks here");// uses cancellable, can prevent block breaking if necessary
        //    event.setCancelled(true); // true = cancel event
        //}
    }
    }

