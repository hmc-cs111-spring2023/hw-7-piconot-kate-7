# Evaluation: running commentary

## Internal DSL

_Describe each change from your ideal syntax to the syntax you implemented, and
describe_ why _you made the change._

**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**

**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**

## External DSL

_Describe each change from your ideal syntax to the syntax you implemented, and
describe_ why _you made the change._

trying to extract surroundings from not explicit surroundings info - adding a helper function, and then seeing it if it works. 

decided i needed a way to say what state it currently is in. "staying" implies you have the same state, but when "turning" you need to say from which state to the other. If you don't move, but need to change state, i used the language "get off" and "get on" to indicate it's switching routes.

similarly, i needed a way to make sure i knew which direction was blocked. there are a few ways I could probably have approached this, but for now am just adding in a direction

the last thing i needed to hammer out was how to handle surroundings information when there was two-four different things someone is specifying. I debated whether or not to make that explicit, or if you could leave it out or just say "hey the user isn't allowed to do that in this language" but I decided I'd rather make the language a bit more awkward and instead keep that functionality. To handle this, I just expanded the "when the X road ends" statement, so that if it's blocked the X road has ended, you can implicitly say that a road is open by saying to turn (right now assuming that that error would get cauught later, and not in the design of the language) and then if a secondary/tertiary/etc road is blocked, it "is closed." I also at this point decided explicitly that you can only have one open direction, it's just whether or not you also specificy one or more blocked roads. Since picobot can only move in one direction, it seems overkill to account for whether more than one road is unblocked explicitly. Similarly, I'm not accounting for where there are all four roads "closed"

i changed "stay" to "continue" just because i liked it better

Any incongruities with direction, actual roads aren't much better (;


okayyy so I finished the parser and it works, but I have too much ambiguity in the places where you are supposed to stay because you still need to specificy that the new direction you are turning is open. So i need to add a way to write that back into my language. I added a turn statement for when the state changes but picobot doesn't move - that way there is a distinguishment in Surroundings. I also changed the functions I was calling to map the Surroundings, so that it has the explicit "open" for when turning, rather than "anything" as the default all the time

**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**

**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**
