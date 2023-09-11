package com.example.pokedex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.PokemonFromResults
import com.example.pokedex.model.PokemonListFromResults
import com.example.pokedex.network.PokeApi
import kotlinx.coroutines.launch

enum class PokeApiStatus { EMPTY, LOADING, ERROR, DONE }

class PokemonViewModel : ViewModel() {

    private val _pokeStatus = MutableLiveData(PokeApiStatus.EMPTY)
    val pokeStatus: LiveData<PokeApiStatus> = _pokeStatus
    private val _pokemon = MutableLiveData<Pokemon>()
    val pokemonLiveData: MutableLiveData<Pokemon> = _pokemon
    private val _pokemonList = MutableLiveData<PokemonListFromResults>()
    val pokemonListLiveData: MutableLiveData<PokemonListFromResults> = _pokemonList

    fun getPokemon(pokemonName: String) {
        viewModelScope.launch {
            try {
                _pokeStatus.value = PokeApiStatus.LOADING
                _pokemon.value = PokeApi.retrofitService.getPokemon(pokemonName)
                _pokeStatus.value = PokeApiStatus.DONE
            } catch (e: Exception) {
                _pokeStatus.value = PokeApiStatus.ERROR
                _pokemon.value = PokeApi.retrofitService.getPokemon(pokemonName)
            }
        }
    }

    fun getPokemonList() {
        viewModelScope.launch {
            try {
                _pokeStatus.value = PokeApiStatus.LOADING
                _pokemonList.value = PokeApi.retrofitService.getPokemonList()
                _pokeStatus.value = PokeApiStatus.DONE
            } catch (e: Exception) {
                _pokeStatus.value = PokeApiStatus.ERROR
                _pokemonList.value = PokeApi.retrofitService.getPokemonList()
            }
        }
    }

}