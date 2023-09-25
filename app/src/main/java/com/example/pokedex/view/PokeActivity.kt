package com.example.pokedex.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.pokedex.R
import com.example.pokedex.model.PokemonFromResults
import com.example.pokedex.ui.theme.PokedexTheme
import com.example.pokedex.viewmodel.PokemonViewModel
import java.util.Locale

class PokeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "pokeList") {

                composable("pokeList") { PokeList(navController) }
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
}

// ECRAN 1

@Composable
fun PokeList(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    viewModel.getPokemonList()
    val pokemonList = viewModel.pokemonListLiveData.observeAsState()
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        pokemonList.value?.let {
            items(it.results) { pokemonFromResults ->
                PokemonRow(pokemonFromResults, navController)
            }
        }
    }
}

@Composable
private fun PokemonRow(pokemonFromResults: PokemonFromResults, navController: NavController) {
    Surface(
        color = colorResource(R.color.yellow_pokemon),
        modifier = Modifier.padding(5.dp),
        border = BorderStroke(3.dp, colorResource(R.color.blue_pokemon)),
        //modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = pokemonFromResults.name.capitalize(Locale.ROOT), fontSize = 25.sp)
            }
            ElevatedButton(border = BorderStroke(3.dp, colorResource(R.color.blue_pokemon)),
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
fun PokeDetail(
    pokemonName: String?,
    viewModel: PokemonViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    if (pokemonName != null) {
        viewModel.getPokemon(pokemonName)
    }
    val pokemonState = viewModel.pokemonLiveData.observeAsState()
    if(pokemonState.value != null) {
        viewModel.getPokemonDescription(pokemonState.value?.id)
    }
    val pokemonDescription = viewModel.pokemonDescriptionLiveData.observeAsState()


    Image(
        painter = rememberAsyncImagePainter("https://i.pinimg.com/1200x/e8/03/c8/e803c8f229eaa547e81c6bc1fa287817.jpg"),
        contentDescription = "gfg image",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxWidth(1.0f).fillMaxHeight(1.0f),
        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(1f) })
    )

    Column {
        Card(
            modifier = Modifier.padding(5.dp),
            border = BorderStroke(3.dp, colorResource(R.color.blue_pokemon)),
            colors = CardDefaults.cardColors(colorResource(R.color.yellow_pokemon))
        ) {
            pokemonState.value?.name?.let {
                Text(
                    it.capitalize(Locale.ROOT),
                    fontSize = 45.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier
                        .width(400.dp)
                        .padding(15.dp)
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = pokemonState.value?.sprites?.frontDefault,
                contentDescription = "Translated description of what the image contains",
                modifier = Modifier.size(200.dp)
            )
            Card(
                modifier = Modifier.padding(5.dp),
                border = BorderStroke(3.dp, colorResource(R.color.blue_pokemon)),
                colors = CardDefaults.cardColors(colorResource(R.color.yellow_pokemon))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = "Height: " + pokemonState.value?.height, fontSize = 30.sp)
                    Text(text = "Weight: " + pokemonState.value?.weight, fontSize = 30.sp)
                }
            }
        }
        Card(
            modifier = Modifier.fillMaxWidth(1.0f).padding(5.dp),
            border = BorderStroke(3.dp, colorResource(R.color.blue_pokemon)),
            colors = CardDefaults.cardColors(colorResource(R.color.yellow_pokemon))
        ) {
            val description = pokemonDescription.value?.description?.get(0)
            Text(text = "Description: " + description?.flavorText?.replace("\n"," "), fontSize = 30.sp, modifier = Modifier.padding(10.dp))
        }
        Spacer(Modifier.size(165.dp))
        Image(
            painter = rememberAsyncImagePainter("https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/International_Pok%C3%A9mon_logo.svg/1200px-International_Pok%C3%A9mon_logo.svg.png"),
            contentDescription = "gfg image",
            modifier = Modifier.fillMaxWidth(1.0f).height(40.dp).padding(0.dp,0.dp,5.dp,5.dp), Alignment.BottomEnd
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PokeListPreview() {
    PokedexTheme {
        PokeDetail("Mew")
    }
}

