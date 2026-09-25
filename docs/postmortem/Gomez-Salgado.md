Your links. Your merged pull requests and your issues. Yours, not the team's.

[Pull requests](https://github.com/bob234-ui/Cst438Project-01/pulls?q=is%3Apr+state%3Aclosed+author%3Abob234-ui)

[Issues](https://github.com/bob234-ui/Cst438Project-01/issues?q=is%3Aissue%20state%3Aclosed%20assignee%3A%40me)

Your role. What did you actually build?

My role was database, API, and feature development. I built the Room database for user accounts, 
connected the app to the FBI Wanted API, created the Suspect of the Day feature, and added the 
favorites system that saves subjects to each user’s Personal Page. 
I also added tests for API results, navigation, and favorites.

Your biggest challenge. What was it, why was it hard, and how did you handle it?
My biggest challenge was connecting the app to the FBI Wanted API. It was difficult because the API returns a large amount of data, 
some fields can be missing, and network requests can fail. I handled it by using Retrofit, making optional fields nullable, adding 
loading and error messages, and testing the code with fake API responses.

Most valuable thing I learned: I learned that features must be planned around how the UI, navigation, API, and database work together. A feature is not finished just because one screen works.

Commitment 1: I will create smaller branches and commits, and regularly merge the latest main into my branch. I will know it worked if my pull requests are easy to review and I avoid large merge conflicts.

Commitment 2: I will write tests while building each feature instead of waiting until the end. I will know it worked if my tests pass before every pull request and teammates can use my code without needing major fixes.
