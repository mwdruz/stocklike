Stocklike, test Jira 2

This application is supposed to allow monitoring changing prices of a product, and represent this as if that product was a stock share i.e. as a simple, hopefully meaningful plot.

Comparing prices of a single offer in one shop is not something we'd need computer software for. The interesting problem is creating some kind of average or other composite metrics of "all" prices of a certain product. That would show us how the market sees the real value of a product.

It is not obvious what metrics will be useful in this role. Therefore, minimal viable product queries source of offers, and shows histogram of prices. Hopefully it will give valuable insight what's the smart number to pick to represent whole distribution of prices. Likely candidates are: minimum, average, median.

What should be considered "all" prices? Source of offers in minimal viable product is single auction site. It is unlikely to be representative enough to create good statistics, but will suffice for demonstrative purposes.

However, actual aim of the application is to experiment with design. There are two goals:

1. Get a feel on how to shape an application structure to achieve modular plug and play software, as promised by "ports and adapters" architecture. Particularly, decouple domain logic from data sources and user interface.

2. Experiment with cognitive load of reading code written by others, where others-ness is simulated by taking long breaks and trying to jump right back in with non-trivial functionality.

[User stories](doc/user%20stories.md)

[State machine](doc/state%20machine.png)

[Program flow](doc/program%20flow.png)

[Web crawl flow](doc/web%20crawl%20flow.png)

[Development plan](doc/development%20plan.md)
