package com.example.data

import com.example.R

object StoryChaptersPart1 {
  val chapters: List<Chapter> = listOf(
    Chapter(
      id = 0,
      actNumber = 1,
      actTitle = "Act I: The Night Market",
      title = "Prologue: The Closed Current",
      subtitle = "Third Rib & The Young Koi",
      imageRes = R.drawable.img_sera_nami,
      imageCaption = "The closed current. A young koi making a road by existing.",
      paragraphs = listOf(
        StoryParagraph(
          id = "p0_1",
          text = "The pilgrims had already started praying when the current closed. Sera Venn was sixteen and not anyone's courier yet. She lived on a salvage skiff called Third Rib, patched so many times the original wood was a rumor. Her father had gone over the rail two seasons back, chasing a brass fitting that flashed like a coin in the cloud. The sea kept both of them. Sera kept the skiff, the debt, and a habit of looking twice at anything that glittered.",
          blueGlassSecret = "Tactical note: Third Rib's port gunwale still bears the iron salvage nicks from the lower reefs.",
          amberLore = "Old salvage proverb: What the cloud swallows, the debt remembers.",
          moonlightTruth = "In the stillness before the fold, moonlight tastes like cold silver on the tongue."
        ),
        StoryParagraph(
          id = "p0_2",
          text = "The pilgrims weren't glittering. Six of them, temple-marked, blue-glass beads fogged up with damp. Their boat was a skinny prayer-hull, the kind meant for short hops between shrine platforms. A weather clerk had sold them a route that wasn't there. Took their coin, took their names, pointed at a seam in the cloud that looked, from far off, like a calm road. It wasn't a road. It was a throat.",
          blueGlassSecret = "Fraudulent route markers often mimic dusk seams by diluting lamp oil with turpentine."
        ),
        StoryParagraph(
          id = "p0_3",
          text = "That was when she first saw the koi. No longer than her forearm. Pale as a lantern chimney, one vermilion mark at the gill like a drop of sealing wax. It swam not in water but in the cloud itself, a bright comma under the skiff. When Sera leaned over, the thing lifted its head like it was scenting her. 'You,' Sera whispered. 'If you know a way, show it.' The koi turned and slipped under the closed current's dark lip.",
          moonlightTruth = "A moon-koi that accepts a cage stops telling the truth. It followed her because her hands were open."
        ),
        StoryParagraph(
          id = "p0_4",
          text = "She called it Nami, because the old salvage-cant for found in moonlight sounded like that, and because a creature that refused cages deserved a name that wasn't a claim. In the morning she sold the skiff's last decent winch to pay a docking fine, practiced the courier hitch in a scrap of stolen vermilion cloth, and went down into the blue light with a fish at her shoulder and a seal still warmer than it should have been."
        )
      ),
      choice = StoryChoice(
        id = "choice_prologue",
        prompt = "How does young Sera navigate the closing throat of cloud behind Nami?",
        options = listOf(
          ChoiceOption(
            id = "opt_douse_lamps",
            text = "Douse every amber lamp, light one blue-glass stub, and trust the fish.",
            consequenceText = "Where the koi swam, the cloud thinned. Sera guided the prayer-hull through the teeth of the seam.",
            reputationTag = "Honest Courier"
          ),
          ChoiceOption(
            id = "opt_salvage_grapple",
            text = "Keep a towline hitched tight and watch the hull strain.",
            consequenceText = "Her hands bled through her gloves on the tiller, but the pilgrims reached the shrine steps alive.",
            reputationTag = "Salvage Tough"
          )
        )
      )
    ),
    Chapter(
      id = 1,
      actNumber = 1,
      actTitle = "Act I: The Night Market",
      title = "Chapter I: After Moonrise",
      subtitle = "The Language of Blue and Amber",
      paragraphs = listOf(
        StoryParagraph(
          id = "p1_1",
          text = "The Skybound Archipelago didn't so much wake as admit that night had become useful. By day the city was carpentry and weather: barges lashed to temple fragments, platforms held by chains that disappeared into a cloud sea the color of dirty pearl. Routes that had been honest at dusk were suggestions by noon. Captains argued with clerks. Clerks argued with the wind. The wind, being older than both, didn't file a response.",
          amberLore = "The Bazaar Ward counts three hundred platforms between the East Pocket and the Anchor."
        ),
        StoryParagraph(
          id = "p1_2",
          text = "Blue lanterns hooked along the wet black-lacquer avenue that stitched the market district together. The lacquer shone like fresh ink. Amber lamps bloomed in doorways and under awnings, and the difference was a language every child here learned before letters. Blue meant you may move. Amber meant you may stop and not be a fool for stopping.",
          blueGlassSecret = "Blue glass paths are certified by the Registry; when one flickers, an inspection was bought off."
        ),
        StoryParagraph(
          id = "p1_3",
          text = "Sera walked the avenue in her standard courier rig: indigo cloak stitched with faint gold, vermilion sash in the formal hitch, brass message tubes riding her ribs, staff-lantern glowing ribbed blue. Nami traveled the air near her shoulder—pale, translucent, fins stirring without sound. 'Packet for the spice quarter,' said the first clerk, pressing a sealed tube into her palm. 'Don't take the high cut. The high cut is lying tonight.'",
          moonlightTruth = "Nami touches the staff head. The glass brightens. The ordinary evening has just spent its last ordinary minute."
        )
      )
    ),
    Chapter(
      id = 2,
      actNumber = 1,
      actTitle = "Act I: The Night Market",
      title = "Chapter II: The Packet That Should Not Exist",
      subtitle = "Sealed in Blue Glass",
      paragraphs = listOf(
        StoryParagraph(
          id = "p2_1",
          text = "The woman waiting at the courier post wore temple gray and a chain-warden's ring, which usually meant someone wanted a thing done without being the person who had done it. 'Sera Venn,' she said. 'Moon-Koi Courier. You still take shrine runs.' 'I take runs that pay and don't require me to pretend a closed current is a suggestion.'",
          amberLore = "Chain-wardens wear rings cut from the anchor-iron of severed barges."
        ),
        StoryParagraph(
          id = "p2_2",
          text = "The woman set a packet on the lacquer counter. Sealed in blue glass rather than wax, the glass ribbed and cloudy, warm as a held breath. No honest clerk sealed in glass unless the contents were meant to survive weather that ate paper. 'Destination: Storm Anchor Shrine. Outer chain gallery. Recipient listed as Warden Pell.' Sera didn't pick it up. 'There is no Warden Pell on the shrine roster. Hasn't been since the last chain-census.'",
          blueGlassSecret = "Glass-sealing requires weather-craft furnaces forbidden outside the High Office."
        ),
        StoryParagraph(
          id = "p2_3",
          text = "Beside the packet: a fold of paper money and a favor-token—black lacquer with a single amber inclusion that looked like a trapped spark. 'Who paid to make the route missing?' Sera asked. The woman glanced toward the dark interval: 'If I knew that in a way I could swear to, I wouldn't be hiring the courier who is feared for knowing it.'"
        )
      ),
      choice = StoryChoice(
        id = "choice_ch2_packet",
        prompt = "How does Sera accept the mysterious warm glass packet?",
        options = listOf(
          ChoiceOption(
            id = "opt_accept_silence",
            text = "Demand silence fee upfront and examine the heat against your ribs.",
            consequenceText = "The warmth sat against her ribs like a second heart with bad intentions. She pocketed the token.",
            rewardItem = CourierItem("item_glass_packet", "Warm Blue-Glass Packet", "Sealed in ribbed blue glass that pulses like a second heart.", "packet"),
            reputationTag = "Courier Code"
          ),
          ChoiceOption(
            id = "opt_study_token",
            text = "Turn the favor-token and study the trapped amber spark.",
            consequenceText = "Favors bought futures. You bought bread with coin, but you bought a way out with lacquer.",
            rewardItem = CourierItem("item_favor_token", "Black Lacquer Favor-Token", "Black lacquer containing a single trapped amber inclusion.", "token"),
            reputationTag = "Sharp Eye"
          )
        )
      )
    ),
    Chapter(
      id = 3,
      actNumber = 1,
      actTitle = "Act I: The Night Market",
      title = "Chapter III & IV: Blue Glass & Missing Routes",
      subtitle = "Captain Irix Halder's Offer",
      paragraphs = listOf(
        StoryParagraph(
          id = "p3_1",
          text = "The scale compass rode in a brass cup at her belt. It didn't point north. North was a flatlander superstition, a story about ground that stayed. The needle was a sliver of moon-koi scale set in glass, and it quivered toward residual moonlight. Tonight the needle hesitated, swung, hesitated again. 'Paid missing,' Sera said under her breath. 'Not weather. Work.'",
          moonlightTruth = "When an honest path is unwritten, the needle shivers like plucked wire."
        ),
        StoryParagraph(
          id = "p3_2",
          text = "Under an amber awning she bought ten minutes of trustworthy passage: a stool and a bowl of pepper broth. From a clandestine book dealer's crate she acquired route fragments: High Cut going dark; Pilgrim Drop unadvised; Chain Road still viable, with a note: Viable is not the same as permitted.",
          amberLore = "Pepper broth auntie has stood on the curve for thirty years; she knows every courier's gait."
        ),
        StoryParagraph(
          id = "p3_3",
          text = "Captain Irix Halder found her near the rope bridge. Good oilskin, smiling with only the public half of his mouth. 'I can make the High Cut honest for an hour,' he said, lightly, as if offering tea. 'For a friend.' Sera let her staff-lantern hang between them: 'If you can make the High Cut honest, you helped make it false. I don't buy roads from the person who hid them.'"
        )
      )
    ),
    Chapter(
      id = 4,
      actNumber = 2,
      actTitle = "Act II: The Undertow Den",
      title = "Chapter V & VI: Civilian Cover & Neutral Ground",
      subtitle = "The Hold of Plenty & Olla",
      paragraphs = listOf(
        StoryParagraph(
          id = "p4_1",
          text = "In her changing locker above the net-mender's stall, four rigs hung like four arguments about the same woman: standard courier rig, dawn-dock disguise, storm-run kit, and undertow civilian cover. She pulled on the plum waistcoat over a dark shirt, rolled her sleeves, tied the sash low as a scrap, and slipped into the Undertow Den.",
          blueGlassSecret = "The locker has a false floor holding 20 fathoms of braided silk line and a spare grappling iron."
        ),
        StoryParagraph(
          id = "p4_2",
          text = "The Undertow Den was a circular hold in an old grain barge named Plenty. Drapes of plum and teal, cages of live-gold moths turning slowly, and a wagon-wheel porthole framing the endless drop of indigo cloud sea. That was the Den's sermon: you may be warm, you may drink, you are not above the fall. You are in a bubble someone caulked.",
          amberLore = "Olla bought Plenty's hull for three barrels of pickled lamp-grease during the great collapse."
        ),
        StoryParagraph(
          id = "p4_3",
          text = "Olla wiped the bar. 'Plum night. The usual, or the usual plus trouble?' Sera set the black lacquer token on the wood. Olla's eyebrows rose: 'That's shrine-adjacent money.' 'It's shrine-adjacent fear,' Sera replied. 'Someone hired me to carry glass to a warden who isn't on the roster.'"
        )
      ),
      choice = StoryChoice(
        id = "choice_den_approach",
        prompt = "How does Sera extract information from Olla in the Den?",
        options = listOf(
          ChoiceOption(
            id = "opt_tea_and_crate",
            text = "Offer to move a locked crate off Olla's books before dawn.",
            consequenceText = "Olla poured tea. 'Not because of the crate. Because your fish is anxious, and moon-koi don't get anxious about gossip.'",
            reputationTag = "Market Debt"
          ),
          ChoiceOption(
            id = "opt_point_porthole",
            text = "Watch the porthole and notice the violet thread in the cloud sea.",
            consequenceText = "The cloud sea had taken on a faint vermilion-violet bruise. Olla frowned: 'Weather-craft wearing a dead man's name.'",
            reputationTag = "Weather Sense"
          )
        )
      )
    ),
    Chapter(
      id = 5,
      actNumber = 2,
      actTitle = "Act II: The Undertow Den",
      title = "Chapter VII & VIII: Storm Jars & The Alley",
      subtitle = "Nami Will Not Be Caged",
      paragraphs = listOf(
        StoryParagraph(
          id = "p5_1",
          text = "'Storm jars have been moving,' Olla whispered in the back alcove. 'Three crates through this hold in ten nights, marked as pickled citrus. The author wants the shrine's outer chains to take a new strain at a chosen hour. You loosen a city's ankle, the city kneels. While it kneels, you can take what it was standing on.'",
          blueGlassSecret = "The third strain-bell corresponds to the midnight slack tide between outer chains."
        ),
        StoryParagraph(
          id = "p5_2",
          text = "In the alley outside, a man stepped from behind the crates holding a ribbed jar with a brass cap. 'Moon-Koi Courier. A collector will pay your fee twice if the companion comes separately. A reading jar. She would be a celebrity.' Inside the jar, a smear of captured moonlight turned like cream in tea.",
          amberLore = "Stolen moonlight loses its direction within two sun-counts, turning sour and useless."
        ),
        StoryParagraph(
          id = "p5_3",
          text = "Nami didn't follow the bait. Nami went through him in a flash of white, extinguishing the stolen light like a wet finger on glass. Sera hooked the man's ankle, set him on the lacquer, and planted her boot on the jar. 'Tell your collector that caging a compass does not make you north.' She changed into her storm-run rig: short sailcoat, tether harness, lightning-proof gloves."
        )
      )
    ),
    Chapter(
      id = 6,
      actNumber = 3,
      actTitle = "Act III: The Ascent & The Shrine",
      title = "Chapter IX: The Chain Road",
      subtitle = "Hook, Glove, Boot-Edge, Hook",
      imageRes = R.drawable.img_chain_climb,
      imageCaption = "The Chain Road. Hook, glove, boot-edge, hook.",
      paragraphs = listOf(
        StoryParagraph(
          id = "p6_1",
          text = "The Chain Road began where the last honest amber awnings ended and the city's confidence became hardware. The first great chain rose from a hole in a timber plaza and ran into the cloud sea. Each link as tall as a door, black with moisture, looking like wet bone under the moon. Wind arrived all at once, cold and clean.",
          blueGlassSecret = "The outer face has no prayer slats, but the iron links offer deep hand-holds at the welds."
        ),
        StoryParagraph(
          id = "p6_2",
          text = "Sera found the inner prayer-rungs filmed with grease—sabotage wearing maintenance's face. She swung to the outer face. Worse and simpler. Hook, glove, boot-edge, hook. Sheet lightning walked distant chains. Halfway out, the city looked like a jewelry box spilled on a dark table: amber points, blue threads, the lacquer avenue a single wet stroke of ink.",
          moonlightTruth = "Nami swam beside the iron, cutting a phosphorescent seam through the dense air."
        ),
        StoryParagraph(
          id = "p6_3",
          text = "The packet at her ribs had gone from warm to hot. Glass that hot was about to crack or do the thing it was made to do. Nami dove into a seam of cloud clinging to the chain like foam to a wreck. Sera unclipped, clipped lower, and followed the pale road the koi made by existing."
        )
      ),
      choice = StoryChoice(
        id = "choice_chain_climb",
        prompt = "A shrine-hand signals strain coming from the outer dark. How do you press through?",
        options = listOf(
          ChoiceOption(
            id = "opt_seam_dive",
            text = "Follow Nami's dive into the blue-teal cloud seam.",
            consequenceText = "The world went silent as closed currents. Her lamp was a small honest star guiding her through the gale.",
            reputationTag = "Courier Nerve"
          ),
          ChoiceOption(
            id = "opt_brace_collar",
            text = "Lock your tether ring around the traveler loop and count breaths.",
            consequenceText = "The iron rang like a tuning fork, sending vibrations through her teeth before settling.",
            reputationTag = "Iron Grip"
          )
        )
      )
    ),
    Chapter(
      id = 7,
      actNumber = 3,
      actTitle = "Act III: The Ascent & The Shrine",
      title = "Chapter X: Sacred Infrastructure",
      subtitle = "Pell's Collar & The Unseaming",
      paragraphs = listOf(
        StoryParagraph(
          id = "p7_1",
          text = "The Storm Anchor Shrine resolved out of the seam suddenly, with architecture: a colonnade of black stone veined with teal. Dozens of chains descended through the floor—the city's ankles, sleeved in prayer-script and grease. Wardens moved with pikes. 'I have a packet for the outer chain gallery,' Sera rasped. 'Addressed to Warden Pell.'",
          amberLore = "Pell was a master rigger who died in the census year; his memorial collar holds the central load."
        ),
        StoryParagraph(
          id = "p7_2",
          text = "An older warden led her to the outer gallery where three great chains passed through engraved iron collars. A lock-plate bore the name PELL. Nami tapped the plate with her mouth: recognition. Sera pressed the blue-glass packet to Pell's name. The glass unseamed without breaking, like a lid waiting for courage.",
          blueGlassSecret = "The glass unseams only when the thermal resonant frequency of the lock matches the seal."
        ),
        StoryParagraph(
          id = "p7_3",
          text = "Inside: a scale, a strip of shrine-script, and a blackened brass key. The script read: 'The missing routes were bought by the Bazaar Ward's second clerk, Irix Halder's coin, and a shrine-cousin who wants the outer collar loose at the third strain-bell. Use the key if you want the collar to remember the city. Burn this scale after.' From below, the bell began to think about ringing."
        )
      )
    ),
    Chapter(
      id = 8,
      actNumber = 3,
      actTitle = "Act III: The Ascent & The Shrine",
      title = "Chapter XI & XII: The Third Strain-Bell",
      subtitle = "Teaching the Lock to Remember",
      paragraphs = listOf(
        StoryParagraph(
          id = "p8_1",
          text = "They ran to the lower hymn-chain. A man in half-temple gray had three storm jars lashed to the chain, roiling with compressed violet. 'You should have used Irix's hour,' he smiled sadly. Sera didn't waste words: her grappling iron hooked the nearest jar-lash, cutting the rope and kicking the jar into a safe stone niche.",
          blueGlassSecret = "Never shatter a storm jar in the open drop; the localized downdraft shears chain mounts."
        ),
        StoryParagraph(
          id = "p8_2",
          text = "The cousin pulled a blade, but Nami struck his hand in a streak of white. The third strain-bell rang! The massive chain lifted a terrifying fraction. The shrine groaned like a tired animal. Sera sprinted back to Pell's collar and shoved the brass key into the lock. The pins shifted—pins, prayers, both. The great chain seated deeper. The lift reversed. The note in the iron returned to holding.",
          moonlightTruth = "At the inner well, the pilgrim scale dissolved into a brief silver film under the moonlight, leaving no trace for ledgers."
        ),
        StoryParagraph(
          id = "p8_3",
          text = "At dawn on the shrine steps, the older warden paid her in coin. On the timber plaza below, a child stared at Nami and asked, 'Are you the one who kept the street from tilting?' 'The chains kept the street from tilting,' Sera replied. 'I only carried the instructions.'"
        )
      )
    )
  )
}
