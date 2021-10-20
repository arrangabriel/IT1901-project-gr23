# Persistence metaphor
We decided on a model for our local persistence implementing save-data implicitly.
Our reasoning for this is based on experience using similar programs, e.g. for note-taking or shopping-lists.
In general a users expectation when using such software is to create an entry, and for it to be there after closing and reopening the program.

Being prompted to save and load data would simply hinder flow for the user. 
This being said one could view entry-creation as an explicit call to save data, but the exact implementation is hidden from the user.
Therefore saving is done implicitly from the user's perspective.
