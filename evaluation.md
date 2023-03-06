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

decided i needed a way to say what state it currently is in. "staying" implies you have the same state, but when "turning" you need to say from which state to the other. If you don't move, but need to change state, i used the language "get off" and "get on" to 

similarly, i needed a way to make sure i knew which direction was blocked. there are a few ways I could probably have approached this, but for now am just adding in a direction

the last thing i needed to hammer out was how to handle surroundings information when there was two-four different things someone is specifying. I debated whether or not to make that explicit, or if you could leave it out or just say "hey the user isn't allowed to do that in this language" but I decided I'd rather make the language a bit more awkward and instead keep that functionality. To handle this, I just expanded the "when the X road ends" statement, so that if it's blocked the X road has ended, you can implicitly say that a road is open by saying to turn (right now assuming that that error would get cauught later, and not in the design of the language) and then

**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**

**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**
