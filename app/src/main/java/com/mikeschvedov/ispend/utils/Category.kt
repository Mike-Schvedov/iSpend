package com.mikeschvedov.ispend.utils

import com.mikeschvedov.ispend.R

enum class Category(val hebrew: String) {
    FOOD_NORMAL(R.string.food_normal_category.resourceToString()),
    FOOD_JUNK(R.string.food_junk_category.resourceToString()),
    FUEL(R.string.fuel_category.resourceToString()),
    TRANSPORTATION(R.string.transportation_category.resourceToString()),
    RENT(R.string.rent_category.resourceToString()),
    SHOPPING_MAY(R.string.shopping_may_category.resourceToString()),
    SHOPPING_CLEANING(R.string.shopping_clean_category.resourceToString()),
    SHOPPING_OTHER(R.string.shopping_other_category.resourceToString()),
    SHOPPING_CLOTHING(R.string.shopping_clothing_category.resourceToString()),
    CAR_MAINTENANCE(R.string.car_maintenance_category.resourceToString()),
    EVENTS(R.string.events_category.resourceToString()),
    PETS(R.string.animal_category.resourceToString()),
    PARKING(R.string.parking_category.resourceToString()),
    GAME(R.string.game_category.resourceToString()),
    ERROR("שגיאה"),
}