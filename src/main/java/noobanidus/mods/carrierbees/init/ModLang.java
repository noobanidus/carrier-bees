package noobanidus.mods.carrierbees.init;

import net.minecraft.potion.Effects;

import static noobanidus.mods.carrierbees.CarrierBees.REGISTRATE;

public class ModLang {
  static {
    REGISTRATE.addRawLang("itemGroup.carrierbees", "Carrier Bees");
    REGISTRATE.addRawLang("subtitles.carrierbees.sploosh", "Sploosh!");
    REGISTRATE.addRawLang("subtitles.carrierbees.crumbled", "Item starts crumbling");
    REGISTRATE.addRawLang("subtitles.carrierbees.behemoth.hurt", "Beehemoth is hurt!");
    REGISTRATE.addRawLang("subtitles.carrierbees.behemoth.loop_aggressive", "Beehemoth buzzes angrily");
    REGISTRATE.addRawLang("subtitles.carrierbees.behemoth.loop", "Beehemoth buzzes");
    REGISTRATE.addRawLang("subtitles.carrierbees.behemoth.dies", "Beehemoth dies");

    REGISTRATE.addRawLang("carrierbees.advancements.collection.title", "Wax Included");
    REGISTRATE.addRawLang("carrierbees.advancements.collection.description", "Collect one of every honeycomb type.");
    REGISTRATE.addRawLang("carrierbees.advancements.queen.title", "Dancing Queen");
    REGISTRATE.addRawLang("carrierbees.advancements.queen.description", "Promote a Beehemoth into a Queen Beehemoth!");
    REGISTRATE.addRawLang("carrierbees.advancements.root.title", "Carried Away");
    REGISTRATE.addRawLang("carrierbees.advancements.root.title.desc", "No bath-houses included, sorry.");
/*    REGISTRATE.addRawLang("carrierbees.advancements.royal_jelly.title", "Aeroplane Royal Jelly");
    REGISTRATE.addRawLang("carrierbees.advancements.royal_jelly.description", "I like aeroplane jelly!");*/
    REGISTRATE.addRawLang("carrierbees.advancements.steed.title", "Loyal Steed");
    REGISTRATE.addRawLang("carrierbees.advancements.steed.description", "Befriend a Beehemoth and dub it your flying steed.");
    REGISTRATE.addRawLang("entity.carrierbees.beehemoth_queen", "Queen Beehemoth");
  }

  public static void load() {
  }
}
