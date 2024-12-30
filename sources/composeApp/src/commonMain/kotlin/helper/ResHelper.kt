package helper

import compose_example.composeapp.generated.resources.Res
import compose_example.composeapp.generated.resources.hair_done_1
import compose_example.composeapp.generated.resources.hair_done_2
import compose_example.composeapp.generated.resources.hair_done_3
import compose_example.composeapp.generated.resources.hair_done_4
import compose_example.composeapp.generated.resources.hair_done_5
import compose_example.composeapp.generated.resources.hair_done_6
import compose_example.composeapp.generated.resources.hair_initial_1
import compose_example.composeapp.generated.resources.hair_initial_2
import compose_example.composeapp.generated.resources.hair_initial_3
import compose_example.composeapp.generated.resources.hair_initial_4
import compose_example.composeapp.generated.resources.player_1
import compose_example.composeapp.generated.resources.player_10
import compose_example.composeapp.generated.resources.player_11
import compose_example.composeapp.generated.resources.player_12
import compose_example.composeapp.generated.resources.player_13
import compose_example.composeapp.generated.resources.player_14
import compose_example.composeapp.generated.resources.player_15
import compose_example.composeapp.generated.resources.player_16
import compose_example.composeapp.generated.resources.player_17
import compose_example.composeapp.generated.resources.player_18
import compose_example.composeapp.generated.resources.player_19
import compose_example.composeapp.generated.resources.player_2
import compose_example.composeapp.generated.resources.player_20
import compose_example.composeapp.generated.resources.player_21
import compose_example.composeapp.generated.resources.player_22
import compose_example.composeapp.generated.resources.player_23
import compose_example.composeapp.generated.resources.player_24
import compose_example.composeapp.generated.resources.player_25
import compose_example.composeapp.generated.resources.player_26
import compose_example.composeapp.generated.resources.player_27
import compose_example.composeapp.generated.resources.player_28
import compose_example.composeapp.generated.resources.player_29
import compose_example.composeapp.generated.resources.player_39
import compose_example.composeapp.generated.resources.player_38
import compose_example.composeapp.generated.resources.player_37
import compose_example.composeapp.generated.resources.player_36
import compose_example.composeapp.generated.resources.player_35
import compose_example.composeapp.generated.resources.player_34
import compose_example.composeapp.generated.resources.player_33
import compose_example.composeapp.generated.resources.player_32
import compose_example.composeapp.generated.resources.player_31
import compose_example.composeapp.generated.resources.player_30
import compose_example.composeapp.generated.resources.player_3
import compose_example.composeapp.generated.resources.player_4
import compose_example.composeapp.generated.resources.player_5
import compose_example.composeapp.generated.resources.player_6
import compose_example.composeapp.generated.resources.player_7
import compose_example.composeapp.generated.resources.player_8
import compose_example.composeapp.generated.resources.player_9

object ResHelper {
    fun getInitialHair(hairInt: Int) = when (hairInt) {
        1 -> Res.drawable.hair_initial_1
        2 -> Res.drawable.hair_initial_2
        3 -> Res.drawable.hair_initial_3
        else -> Res.drawable.hair_initial_4
    }

    fun getFinishedHair(hairInt: Int) = when (hairInt) {
        1 -> Res.drawable.hair_done_1
        2 -> Res.drawable.hair_done_2
        3 -> Res.drawable.hair_done_3
        4 -> Res.drawable.hair_done_4
        5 -> Res.drawable.hair_done_5
        else -> Res.drawable.hair_done_6
    }

    fun getPlayerHead(playerInt: Int) = when (playerInt) {
        1 -> Res.drawable.player_1
        2 -> Res.drawable.player_2
        3 -> Res.drawable.player_3
        4 -> Res.drawable.player_4
        5 -> Res.drawable.player_5
        6 -> Res.drawable.player_6
        7 -> Res.drawable.player_7
        8 -> Res.drawable.player_8
        9 -> Res.drawable.player_9
        10 -> Res.drawable.player_10
        11 -> Res.drawable.player_11
        12 -> Res.drawable.player_12
        13 -> Res.drawable.player_13
        14 -> Res.drawable.player_14
        15 -> Res.drawable.player_15
        16 -> Res.drawable.player_16
        17 -> Res.drawable.player_17
        18 -> Res.drawable.player_18
        19 -> Res.drawable.player_19
        20 -> Res.drawable.player_20
        21 -> Res.drawable.player_21
        22 -> Res.drawable.player_22
        23 -> Res.drawable.player_23
        24 -> Res.drawable.player_24
        25 -> Res.drawable.player_25
        26 -> Res.drawable.player_26
        27 -> Res.drawable.player_27
        28 -> Res.drawable.player_28
        29 -> Res.drawable.player_29
        30 -> Res.drawable.player_30
        31 -> Res.drawable.player_31
        32 -> Res.drawable.player_32
        33 -> Res.drawable.player_33
        34 -> Res.drawable.player_34
        35 -> Res.drawable.player_35
        36 -> Res.drawable.player_36
        37 -> Res.drawable.player_37
        38 -> Res.drawable.player_38
        else -> Res.drawable.player_39
    }


}