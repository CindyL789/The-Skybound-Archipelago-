package com.example.data

import com.example.R

object StoryChaptersPart2 {
  val chapters: List<Chapter> = listOf(
    Chapter(
      id = 9,
      actNumber = 4,
      actTitle = "Act IV: The Ward & The List",
      title = "Chapter XIII & XIV: The Quiet Room & The Forged Seal",
      subtitle = "Auditor Kelm & The False Ledger",
      paragraphs = listOf(
        StoryParagraph(
          id = "p9_1",
          text = "The summons arrived in ordinary wax from the Bazaar Ward offices. Three people sat at a table of black lacquer so polished it held their faces like a committee: Ward-Auditor Kelm, who loved ledgers more than outcomes, and a shrine liaison. 'Courier Venn. You delivered to a lock. The Ward requires the list of names who paid to have routes marked missing.' Sera sat: 'I carry packets. I do not keep a public index of other people's sins.'",
          amberLore = "Auditor Kelm rose through the Net-Tax registry; he believes every draft can be balanced."
        ),
        StoryParagraph(
          id = "p9_2",
          text = "'There is a rumor the Ward will license moon-koi,' the liaison warned. 'Compass animals. Public safety.' Sera's eyes narrowed: 'If you put my companion on a license, you will discover how many routes go missing without anyone paying. I will simply stop finding people.' She left into the blue light.",
          blueGlassSecret = "A courier license gives the Ward power to requisition any staff-lantern during emergencies."
        ),
        StoryParagraph(
          id = "p9_3",
          text = "By the second night, a forged sheet hung on the public board at the lacquer avenue bend, listing nine names—including Olla of the Hold, the pepper-broth auntie, and Sera Venn herself! The seal was ribbed the wrong way. Sera took the sheet down. At the Den, Olla folded it into a coaster: 'They want me angry at you. Anger is a corridor. I don't walk corridors other people built.'"
        )
      ),
      choice = StoryChoice(
        id = "choice_forged_list",
        prompt = "How does Sera handle the forged accusation sheet in the Bazaar?",
        options = listOf(
          ChoiceOption(
            id = "opt_pay_witness",
            text = "Pay the whistling street boy to watch who comes to copy the postings.",
            consequenceText = "The boy reported slate-runners checking the board every half hour, tracing back to the Office.",
            reputationTag = "Street Network"
          ),
          ChoiceOption(
            id = "opt_confront_post",
            text = "Slam the fake sheet on the courier clerk's counter and demand expensive lies.",
            consequenceText = "The clerk looked down. 'Your seals don't lean, Venn. We'll tell them the lie costs double.'",
            reputationTag = "Courier Pride"
          )
        )
      )
    ),
    Chapter(
      id = 10,
      actNumber = 4,
      actTitle = "Act IV: The Ward & The List",
      title = "Chapter XV & XVI: Sand-Grey & The Slack Chain",
      subtitle = "Sailcloth Row & The Kneeling Office",
      paragraphs = listOf(
        StoryParagraph(
          id = "p10_1",
          text = "At dawn-dock, Sera wore the sand-grey oilskin with her face wrapped. A contact missing the left thumb-joint met her under the pier: 'A barge called Plenty's cousin—renamed Measure—is moving jars again under Sailcloth Row. Dropping crates into cloud to a platform that doesn't appear on public boards. Paid gone.'",
          moonlightTruth = "Nami hated the sand-grey wrap, drifting high like an untamed thought."
        ),
        StoryParagraph(
          id = "p10_2",
          text = "Sera walked Sailcloth Row. She found the dark interval: a platform with lantern-hooks scraped bare and an iron chain slack in a way chains shouldn't be slack. Slack meant weight below had changed. Slack meant something was being allowed to think about falling. She marked the collar with two nicks and a third: salvage grammar for 'this will move.'",
          blueGlassSecret = "A slack chain generates harmonic rattle that can be silenced with tallow wedges."
        ),
        StoryParagraph(
          id = "p10_3",
          text = "The older warden met her with his hood up: 'The Office wants a smaller kneeling. One district. Proof of concept. The slack chain is theirs.' He handed her a spare brass gallery key: 'If you use it, you didn't get it from me. If you don't, throw it in cloud.'"
        )
      )
    ),
    Chapter(
      id = 11,
      actNumber = 5,
      actTitle = "Act V: Unmaking the Demonstration",
      title = "Chapter XVII & XVIII: The Platform That Forgot Its Chain",
      subtitle = "Catastrophe on Sailcloth Row",
      paragraphs = listOf(
        StoryParagraph(
          id = "p11_1",
          text = "Captain Irix offered crews to relight the cut: 'Together we make their demonstration fail. Separately, you become a hymn and I become an example.' Sera accepted only one term: 'When I mark a collar, your crews relight every lamp without sending a bill dressed as friendship. It expires at dawn.'",
          amberLore = "Irix's crews carry high-flash phosphor flares capable of cutting fog for twenty minutes."
        ),
        StoryParagraph(
          id = "p11_2",
          text = "Suddenly the platform groaned! A laundry line lifted as if the district exhaled. Someone screamed as a kitchen tilted. Nami was sucked down into boiling jar-violet weather beneath the beams! 'No!' Sera cried. She swung through the violet with grappling iron and hook, wrapping Nami against her ribs inside the sailcoat.",
          moonlightTruth = "The jar-weather was trying to read Nami, forcing the celestial compass to point at false north."
        ),
        StoryParagraph(
          id = "p11_3",
          text = "The platform shuddered and caught! Caught not on faith, but on the spare key Sera seated and an enormous salvage door-hook thrown from a neighboring barge by the person with the missing thumb-joint. Slack became strain. Strain became holding. The kitchen-scream became swearing—the honest sound of people expecting to live."
        )
      ),
      choice = StoryChoice(
        id = "choice_platform_save",
        prompt = "The platform is tilting into the cloud sea. Where do you drive the brace?",
        options = listOf(
          ChoiceOption(
            id = "opt_seat_spare_key",
            text = "Drive the warden's spare key deep into the collar pins.",
            consequenceText = "The collar bit into the descending iron, holding the district's timber floor by sheer tension.",
            reputationTag = "Hardware Master"
          ),
          ChoiceOption(
            id = "opt_catch_salvage_hook",
            text = "Snag the giant door-hook from the neighboring barge and tie it off.",
            consequenceText = "The crude salvage hook took the brunt of the drop, groaning but refusing to snap.",
            reputationTag = "Salvage Instinct"
          )
        )
      )
    ),
    Chapter(
      id = 12,
      actNumber = 5,
      actTitle = "Act V: Unmaking the Demonstration",
      title = "Chapter XIX to XXII: Unmaking Measure's Well",
      subtitle = "Weather Poured Into Weather",
      paragraphs = listOf(
        StoryParagraph(
          id = "p12_1",
          text = "A blue-glass packet arrived addressed directly to Sera Venn from the eldest pilgrim, containing an old clouded moon-koi scale. It revealed the Office kept a secret well under the barge Measure. Sera slipped in through the lower hull: inside was a massive ribbed tank forcing violet lightning and silver moonlight into a terrible marriage.",
          blueGlassSecret = "Measure's lower ribs were bolted with double-shear rivets salvaged from temple wrecks."
        ),
        StoryParagraph(
          id = "p12_2",
          text = "An elder woman in clean gray watched calmly: 'A city that hangs from weather deserves a weather it can instruct. You could help us write the instructions.' 'I taught a lock to remember the city,' Sera countered. 'That's not the same as teaching a current to kneel.'",
          amberLore = "The kneeling clerks believed weather was just an unwritten administrative code."
        ),
        StoryParagraph(
          id = "p12_3",
          text = "Sera pressed the pilgrim's old scale to the ribbed glass. Moonlight-that-had-been-someone met moonlight-that-wanted-to-be-used. The tank cracked in a clean seam! Violet bled, silver ran, and the blacked porthole burst open to the cloud sea. Sera tore the brass lines out with her grappling iron. Weather poured into weather, becoming nothing a ledger could own."
        )
      )
    ),
    Chapter(
      id = 13,
      actNumber = 6,
      actTitle = "Act VI: The Dragon & The Descent",
      title = "Chapter XXIII & XXIV: The Coil Around the Shrine",
      subtitle = "Elders in the Aurora",
      imageRes = R.drawable.img_coil_dragon,
      imageCaption = "The Coil looked at the city. Looking was worse than an attack.",
      paragraphs = listOf(
        StoryParagraph(
          id = "p13_1",
          text = "Three nights later, a green-and-violet aurora poured down the sky. Above the western barges swam two colossal shapes: Moon-Koi Elders, the size of prayer-hulls, flanks holding starlight like wet lacquer. One turned slowly, and the clouds around its whiskers blossomed into a navigable road no clerk had ever sold.",
          moonlightTruth = "Elder moon-koi swim the aurora like living continents; they remember the sky before chains existed."
        ),
        StoryParagraph(
          id = "p13_2",
          text = "At the third strain-bell, the central island changed its outline. The white spine of The Coil uncoiled! White scales the size of shutters caught the full moon. A head like a temple gate lifted over the highest pagoda, antlers branching into cloud, amber eyes older than money. The Coil looked at the city.",
          blueGlassSecret = "When the Coil stirs, the tension on all 72 perimeter anchor chains drops by half an inch."
        ),
        StoryParagraph(
          id = "p13_3",
          text = "An Office author on a pagoda tiles held up petition papers to the dragon. The Coil did not read. It exhaled raw, uncompressed weather—moonlight and wind smelling of rain deciding. The paper drifted into cloud. The dragon settled again by dawn, head on tail, leaving the city in awe."
        )
      )
    ),
    Chapter(
      id = 14,
      actNumber = 6,
      actTitle = "Act VI: The Dragon & The Descent",
      title = "Chapter XXV & XXVI: A Ship That Remembers Water",
      subtitle = "Descent to the True Tide",
      paragraphs = listOf(
        StoryParagraph(
          id = "p14_1",
          text = "In the lower bazaar, a hooded weather seller revealed: 'The galleon Letter of Descent on the east chain is paying out cables. It is dropping through the cloud sea to the true tide of Lower Salt with storm jars in its hold.' Sera boarded the descending ship as chains screamed through the fairleads.",
          amberLore = "Before the clouds rose, all humanity sailed true water; the memory is kept only in timber hulls."
        ),
        StoryParagraph(
          id = "p14_2",
          text = "In the hold, Sera unmade the violet jars with her blue-glass stub and pilgrim scale. But the galleon kept plunging! Then came a sound like a city slapping a table—Letter of Descent hit the true ocean tide in a white ruin of foam! For three breaths, Sera stood on a ship that did not hang.",
          blueGlassSecret = "The salinity of Lower Salt water corrodes sky-bronze within three tidal cycles."
        ),
        StoryParagraph(
          id = "p14_3",
          text = "The ocean reflected stars and lightning. Sera climbed back up a dropped chain that kissed both worlds, guided by Nami's faint thread. On the shrine steps, her hands shook over hot tea. Maps could grow a floor without asking."
        )
      ),
      choice = StoryChoice(
        id = "choice_ocean_descent",
        prompt = "The ship strikes true ocean water. Do you step onto the dark shoreline or climb back?",
        options = listOf(
          ChoiceOption(
            id = "opt_climb_sky",
            text = "Follow Nami's thread up the dropped chain back to the Archipelago.",
            consequenceText = "The thread rose through rain like a hair. The hanging city took her back without applause.",
            reputationTag = "True Sky Courier"
          ),
          ChoiceOption(
            id = "opt_leave_caps",
            text = "Leave the unseamed weather caps on the captain's chart table as a warning.",
            consequenceText = "'Your demonstration leaked,' Sera told them. 'If you return with my mark, the shrine will know you remember how.'",
            reputationTag = "Courier Law"
          )
        )
      )
    ),
    Chapter(
      id = 15,
      actNumber = 7,
      actTitle = "Act VII: The Parish & The Route Below",
      title = "Chapter XXVII to XXX: Gathering the Allies",
      subtitle = "Tavi, Len, Sister Marrow, and Hane",
      paragraphs = listOf(
        StoryParagraph(
          id = "p15_1",
          text = "In the registry loft, Tavi Quill copied honest route charts in secret: 'My sister was on the pilgrim-hull that closed two years ago. I owe you a true copy. There is a third chart: Lower Salt. The Registry pretends it is blank, but someone in the Office has been erasing Venns who live below.'",
          amberLore = "Tavi's desk is tucked under the eaves where rain sound drowns out the scratching of forbidden pens."
        ),
        StoryParagraph(
          id = "p15_2",
          text = "On the collar, chain-rigger Len Vale flexed three broken fingers: 'Ward paid for a lecture on posture; I told them their paper could learn to swim. I'm with you for the auntie who leaves flakes and the next platform that forgets its chain.'",
          blueGlassSecret = "Len carries four hardened wedge-pins sewn into her boot linings."
        ),
        StoryParagraph(
          id = "p15_3",
          text = "On the shrine gallery, Sister Marrow delivered a secret sheet: 'The sea remembers the families the cloud forgot.' And in the Den, Hane the sailor who jumped back from the galleon bought tea with a salt-crusted coin: 'Lower Salt isn't empty. There's a barge-grave they call Venn-water.'"
        )
      )
    ),
    Chapter(
      id = 16,
      actNumber = 7,
      actTitle = "Act VII: The Parish & The Route Below",
      title = "Chapter XXXI & Epilogue: Temporary Routes",
      subtitle = "The Comma Points Down",
      imageRes = R.drawable.img_hero_archipelago,
      imageCaption = "The Skybound Archipelago at Moonrise.",
      paragraphs = listOf(
        StoryParagraph(
          id = "p16_1",
          text = "They weren't an organization—organizations attracted kneeling. They met in pieces: Tavi with a chart, Len with a pin, Marrow with a blank, Hane with sea stories. Tavi unfurled the chart of Lower Salt: 'There is a barge-grave. There is a market on stilts when the tide is kind. There is a name that matches the packet the spice clerk is afraid to sell you.'",
          moonlightTruth = "Residual moonlight shone from the un-erased ink on the chart."
        ),
        StoryParagraph(
          id = "p16_2",
          text = "Under the spice clerk's counter lay the second packet, sealed in wax the color of drowned paper. The address was a name Sera hadn't heard spoken aloud since her father went over the rail: Joss Venn. Lower Salt. If living.",
          amberLore = "Joss Venn was the chief rigger who helped swing the Third Rib's original keel."
        ),
        StoryParagraph(
          id = "p16_3",
          text = "Sera didn't pick it up yet. Some promises required a night of thinking and a chart that hadn't yet been drawn. Behind the stall, Tavi Quill was already drawing it. The world ran on light, wind, and favors. Sera adjusted the vermilion sash and checked her scale compass. Nami drew a pale comma in the dark, and the comma meant this way, and also for now. This time the comma pointed down.",
          moonlightTruth = "The story continues into Book Two: The Map That Lied."
        )
      ),
      choice = StoryChoice(
        id = "choice_epilogue",
        prompt = "How does Sera Venn prepare for the journey down to Lower Salt?",
        options = listOf(
          ChoiceOption(
            id = "opt_pick_up_packet",
            text = "Rest your finger on the drowned wax and commit to Joss Venn's delivery.",
            consequenceText = "The residual weather in the wax hummed against her fingertip. The promise was made against the weather.",
            rewardItem = CourierItem("item_lower_salt_chart", "Tavi's Lower Salt Chart", "Marks the barge-graves of Venn-water and the route to Joss Venn.", "map"),
            reputationTag = "Promise Keeper"
          ),
          ChoiceOption(
            id = "opt_salvage_ready",
            text = "Check the scale compass with Len's extra pins and prepare the descent rig.",
            consequenceText = "Nami circled her shoulder, shining with steady, fearless light pointing down into the deep.",
            reputationTag = "Skybound Legend"
          )
        )
      )
    )
  )
}
