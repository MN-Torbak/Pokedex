package com.example.pokedex.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.PokemonFromResults
import com.example.pokedex.model.PokemonSprites
import com.example.pokedex.ui.theme.PokedexTheme
import com.example.pokedex.viewmodel.PokemonViewModel

class PokeActivity : ComponentActivity() {

    private val pokeViewModel: PokemonViewModel by viewModels()
    private var pokemonList = ArrayList<PokemonFromResults>()
    private val modifier: Modifier = Modifier
    private var pokemonSprites: PokemonSprites =
        PokemonSprites("1", "1", "1", "1", "1", "1", "1", "1")
    private var pokemonPicked: Pokemon = Pokemon("2", "1", "3", "4", pokemonSprites)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            pokeViewModel.getPokemonList()
            pokeViewModel.pokemonListLiveData.observe(this) {
                for (pokemonFromResults in it.results) {
                    pokemonList.add(pokemonFromResults)
                }
            }

            val navController = rememberNavController()
            NavHost(navController, startDestination = "pokeList") {

                composable("pokeList") { PokeList(navController, modifier, pokemonList) }
                //composable("pokeDetail") { PokeDetail() }
                composable(
                    route = "pokeDetail/{pokemonId}",
                    arguments = listOf(navArgument("pokemonId") { type = NavType.StringType })
                ) { backStackEntry ->
                    PokeDetail(backStackEntry.arguments?.getString("pokemonId"))
                }
            }
        }
    }

    fun getPokemon(pokemonName: String): Pokemon {
        pokeViewModel.getPokemon(pokemonName)
        pokeViewModel.pokemonLiveData.observe(this) {
            pokemonPicked = it
        }
        return pokemonPicked
    }

}

// ECRAN 1

@Composable
fun PokeList(
    navController: NavController,
    modifier: Modifier = Modifier,
    pokemonList: List<PokemonFromResults>
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(pokemonList) { pokemonFromResults ->
            PokemonRow(pokemonFromResults, navController)
        }
    }
}

@Composable
private fun PokemonRow(pokemonFromResults: PokemonFromResults, navController: NavController) {

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = pokemonFromResults.name)
            }
            ElevatedButton(
                onClick = {
                    navController.navigate(route = "pokeDetail/${(pokemonFromResults.name)}")
                }
            ) {
                Text("Details")
            }
        }
    }
}


// ECRAN 2


@Composable
fun PokeDetail( pokemonName: String?, viewModel: PokemonViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val textPoke: String
    if (pokemonName != null) {
        viewModel.getPokemon(pokemonName)
        val pokemonState = viewModel.pokemonLiveData.observeAsState()
        pokemonState.value?.height?.let { Text(it) }
    }

    //val pokemon = getPokemon(pokemonName)
    /*Column {
        Image(
            painter = rememberAsyncImagePainter("https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/International_Pok%C3%A9mon_logo.svg/1200px-International_Pok%C3%A9mon_logo.svg.png"),
            contentDescription = "gfg image",
            modifier = Modifier.size(150.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = pokemon.sprites.frontDefault,
                contentDescription = "Translated description of what the image contains",
                modifier = Modifier.size(100.dp)
            )
            Card(modifier = Modifier.padding(5.dp)) {
                Column(modifier = Modifier.padding(5.dp)) {
                    Text(pokemon.name)
                    Text(text = "height: " + pokemon.height)
                    Text(text = "weight: " + pokemon.weight)
                }
            }
        }
    }*/
}

@Preview(showBackground = true)
@Composable
fun PokeListPreview() {
    val pokemonSprites = PokemonSprites(
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/1.png",
        "1",
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/1.png",
        "1",
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
        "1",
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/1.png",
        "1"
    )
    val pokemonPicked = Pokemon("1", "7", "69", "Bulbasaur", pokemonSprites)
    PokedexTheme {
        PokeDetail("")
    }
}

