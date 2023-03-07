# Design
My language, in a nutshell, is a reimagining of picobot as a car on a roadtrip, so the language instructions are meant to be reminiscent of instructions from a GPS.

## Who is the target for this design, e.g., are you assuming any knowledge on the part of the language users?
I'm assuming that users have some familiarity with driving instructions, even if they have never driven. That being said, besides words like "road," "drive," and "route," most of the vocabulary used would not be confused with legitmate directions from a GPS like Apple Maps or Google Maps. 

## Why did you choose this design, i.e., why did you think it would be a good idea for users to express the maze-searching computation using this syntax?
I thought this was an interesting design idea because it can maintain a lot of the surroundings and state information that the original picobot language has, while still being a different way to think about picobot's movement and hopefully being intuitive about that movement.

## What behaviors are easier to express in your design than in Picobot’s original design?  If there are no such behaviors, why not?
I think it's a little easier to express "keep going." It's very similar to when a GPS just tells you to stay where you are - the direction doesn't change, nor does the state. I think it's also a little more intuitive to think about different options in states in this design. It's not something a GPS might tell you, but I've designed as if you come acrosss a crossroads, these are the choices that the picobot-car can make and in which order and why. Perhaps less like a GPS and more like your grandma's handwritten directions for a country road.

## What behaviors are more difficult to express in your design than in Picobot’s original design? If there are no such behaviors, why not?
It's not more difficult, but definitely more wordy to express different options in states. Part of this comes from what I do consider more difficult in my design - expressing specifics about surroundings. If we imagine the room like a set of roads, the empty room for example becomes just one big grid system which can be difficult to conceptualize, especially if most of the time you just need to know if the road ahead of you is clear. For my design right now, it obfuscates that information unless it becomes pertinent (which is similar to the original picobot with the wildcard) but makes an instruction more verbose if it needs to get added in.

## On a scale of 1–10 (where 10 is “very different”), how different is your syntax from PicoBot’s original design?
I'm going to say 7 because it is a different language - it's trying a new way to think about maze-navigation and so demands a new way to write it. It's a lot more verbose and uses words rather than more short-hand notation. It's not completely different because it maps a lot of the information in the same order (given surroundings, what's the movement and state). I did try making it even more different, but keeping it this way made it feel more intuitive as driving directions to me, and so I kept it.

## Is there anything you would improve about your design?
I'd like to streamline some of the "crossroads" (for lack of a better term) cases, but I'm unsure how well that will work within the road trip direction design.


## Final Design + Instructions
- everything is lowercase except for directions.
- cardinal directions are the same, but use "stop" instead of "StayHere"

There are six main cases to consider, with their base statements below
- whether you're moving in the same direction and do not change state
    - while there is road ahead, continue direction on route num
- whether you're moving in the same direction and do change state
    - while there is road ahead, get off route num and turn direction onto route num
- whether you're changing direction and do not change state
    - when the direction road ends, turn direction to continue on route num
- whether you're changing direction and do change state
    - when the direction road ends, get off route num and turn direction onto route num
- whether you're stopping and do not change state
    - when the direction road ends, stop to continue on route num
- whether you're stopping and do change state
    - when the direction road ends, stop to get off route num and turn onto route num

If you want to add more roads that are closed, you can add after the initial road statement either "and the direction road is closed" or "and the direction and direction roads are closed"
- i.e. "when the West road ends, and the North road is closed, stop to get off route 2 and turn East onto route 1"