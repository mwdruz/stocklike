0. Create application skeleton that could be extended by
   adding functionality in separate "slices", as little
   coupling as possible.
   
   1. Codebase will be split in two sorts:

       1. First one, that just passes data from one hand to another.
          That won't need tests other than acceptance testing.
       2. Second one, which encapsulates domain logic and will be
          tested by behavior.

   2. The backbone of the application is its state. The state
             determines which view is loaded by ui and what kind of
             interaction can be fired by ui

   3. SHOULD THE STATE BE STORED BY UI OR DOMAIN LAYER??? 
      Ideally, interactors should know what's next state change.

1. Explore possible domain by parsing web page.
   1. Load all offers from all result pages
   2. Clean up results: deal with useless items
   3. Clean up results: deal order of promoted offers

2. Where is an entry point for data loaded from web?