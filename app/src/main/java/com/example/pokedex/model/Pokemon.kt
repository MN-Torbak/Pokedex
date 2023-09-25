package com.example.pokedex.model

import com.squareup.moshi.Json

data class Pokemon (
    val id: String,
    val height: String,
    val weight: String,
    val name: String,
    val sprites: PokemonSprites
)

data class PokemonListFromResults (
    @Json(name = "results") val results: List<PokemonFromResults>
)

data class PokemonFromResults (
    val name: String,
    val url: String
)

data class PokemonSprites(
    @Json(name = "back_default") val backDefault: String?,
    @Json(name = "back_female")val backFemale: String?,
    @Json(name = "back_shiny")val backShiny: String?,
    @Json(name = "back_shiny_female")val backShinyFemale: String?,
    @Json(name = "front_default")val frontDefault: String?,
    @Json(name = "front_female")val frontFemale: String?,
    @Json(name = "front_shiny")val frontShiny: String?,
    @Json(name = "front_shiny_female")val frontShinyFemale: String?
)

data class PokemonSpecies(
    @Json(name="flavor_text_entries") val description: List<DescriptionPokemon>
)

data class DescriptionPokemon (
    @Json(name="flavor_text") val flavorText: String
)