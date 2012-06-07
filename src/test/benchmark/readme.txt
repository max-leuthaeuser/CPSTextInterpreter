Simply run the Python script (bench.py)
You can define how many objects (xl) should be considered
for pattern matching on how many positions (yl). This is specified at the beginning of the Python script.

Example:
# number of objects for pattern matching
xl = range(2, 6)
# on this positions
yl = range(2, 11)

This will benchmark the pattern matching for 
2 up to 5 objects on 2 up to 10 positions.

The CPSTextInterpreter executable needs to be in the same place as the script itself.