package ar.outfitmaker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OutfitMakerApplication

fun main(args: Array<String>) {
    runApplication<OutfitMakerApplication>(*args)
}