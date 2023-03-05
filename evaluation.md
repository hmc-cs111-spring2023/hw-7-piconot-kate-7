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

decided i needed a way to say what state it currently is in. "staying" implies you have the same state, but when "turning" you need to say from which state to the other.

similarly, i needed a way to make sure i knew which direction was blocked. there are a few ways I could probably have approached this, but for now am just adding in a direction

**On a scale of 1–10 (where 10 is "a lot"), how much did you have to change your syntax?**

**On a scale of 1–10 (where 10 is "very difficult"), how difficult was it to map your syntax to the provided API?**
