1. Create application skeleton that could be extended by adding functionality in separate "slices", as little coupling as possible.

   1. Codebase will be split in two sorts:
      1. First one, that just passes data from one hand to another. That won't need tests other than acceptance testing.
      2. Second one, which encapsulates domain logic and will be tested by behavior.

   2. The backbone of the application is its state. The state determines which view is loaded by ui and what kind of interaction can be fired by ui

   3. Should the state be stored by ui or domain layer? Ideally, only interactors would need to know what's next state change.

2. Statistics:
   1. [x] Creating histogram

3. Explore possible domain by parsing web page.
   1. [x] Load all offers from all result pages
   2. [x] Clean up results: deal with useless items
   3. [x] Clean up results: deal with order of offers

4. Q: Where is an entry point for data loaded from web? A: Loading data from web will be triggered by new search query.
