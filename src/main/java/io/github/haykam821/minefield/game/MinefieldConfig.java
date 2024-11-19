package io.github.haykam821.minefield.game;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.github.haykam821.minefield.game.map.MinefieldMapConfig;
import xyz.nucleoid.plasmid.api.game.GameSpace;
import xyz.nucleoid.plasmid.api.game.common.config.WaitingLobbyConfig;
import xyz.nucleoid.plasmid.api.game.stats.GameStatisticBundle;

public class MinefieldConfig {
	public static final MapCodec<MinefieldConfig> CODEC = RecordCodecBuilder.mapCodec(instance -> {
		return instance.group(
			MinefieldMapConfig.CODEC.fieldOf("map").forGetter(MinefieldConfig::getMapConfig),
			WaitingLobbyConfig.CODEC.fieldOf("players").forGetter(MinefieldConfig::getPlayerConfig),
			Codec.INT.optionalFieldOf("guide_ticks", 20 * 30).forGetter(MinefieldConfig::getGuideTicks),
			Codec.INT.optionalFieldOf("end_ticks", 20 * 5).forGetter(MinefieldConfig::getEndTicks),
			Codec.BOOL.optionalFieldOf("remove_exploded_pressure_plates", true).forGetter(MinefieldConfig::shouldRemoveExplodedPressurePlates),
			GameStatisticBundle.NAMESPACE_CODEC.optionalFieldOf("statistic_bundle_namespace").forGetter(MinefieldConfig::getStatisticBundleNamespace)
		).apply(instance, MinefieldConfig::new);
	});

	private final MinefieldMapConfig mapConfig;
	private final WaitingLobbyConfig playerConfig;
	private final int guideTicks;
	private final int endTicks;
	private final boolean removeExplodedPressurePlates;
	private final Optional<String> statisticBundleNamespace;

	public MinefieldConfig(MinefieldMapConfig mapConfig, WaitingLobbyConfig playerConfig, int guideTicks, int endTicks, boolean removeExplodedPressurePlates, Optional<String> statisticBundleNamespace) {
		this.mapConfig = mapConfig;
		this.playerConfig = playerConfig;
		this.guideTicks = guideTicks;
		this.endTicks = endTicks;
		this.removeExplodedPressurePlates = removeExplodedPressurePlates;
		this.statisticBundleNamespace = statisticBundleNamespace;
	}

	public MinefieldMapConfig getMapConfig() {
		return this.mapConfig;
	}

	public WaitingLobbyConfig getPlayerConfig() {
		return this.playerConfig;
	}

	public int getGuideTicks() {
		return this.guideTicks;
	}

	public int getEndTicks() {
		return this.endTicks;
	}

	public boolean shouldRemoveExplodedPressurePlates() {
		return this.removeExplodedPressurePlates;
	}

	public Optional<String> getStatisticBundleNamespace() {
		return this.statisticBundleNamespace;
	}

	public GameStatisticBundle getStatisticBundle(GameSpace gameSpace) {
		return this.statisticBundleNamespace
			.map(namespace -> {
				return gameSpace.getStatistics().bundle(namespace);
			})
			.orElse(null);
	}
}