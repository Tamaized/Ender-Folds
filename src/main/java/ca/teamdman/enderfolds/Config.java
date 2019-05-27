package ca.teamdman.enderfolds;

@net.minecraftforge.common.config.Config(modid= EnderFolds.MOD_ID, name= EnderFolds.MOD_NAME, category="")
public class Config {
	@net.minecraftforge.common.config.Config.Comment("General Options")
	@net.minecraftforge.common.config.Config.RequiresMcRestart
	public static General general = new General();
	public static class General {
		@net.minecraftforge.common.config.Config.Comment("Forces destinations to always be indestructible.")
		public boolean indestructibleDestinations = false;
		@net.minecraftforge.common.config.Config.Comment("Only allows one core to be extracted from a destination.")
		public boolean unqiueCores = true;
	}
}
