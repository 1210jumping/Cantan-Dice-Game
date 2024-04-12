## Code Review

Reviewed by: Alexander Gailis, u7497023

Reviewing code written by: Shu (Kerry) Zhang u7519580

Component: [rollDice()](https://gitlab.cecs.anu.edu.au/u7497023/comp1110-ass2/-/blob/6eba67c172f69b19ab6dfdfdf44617a0fe65f95e/src/comp1110/ass2/CatanDice.java#L157-201) 
method in CatanDice class

### Comments 
Note that these comments refer to the code before I edited it. I have made changes to ensure correctness changes to 
ensure task 10 is complete accurately. Link is to a previous commit before I made these changes.

The rollDice method is not entirely correct nor is it concise. The code almost works correctly. It is able to update the
resource state variable given an integer as to the number of dice to roll. However, there are two issues relating to 
correctness. Firstly, the random number generated (int num) is never updated inside the while loop [160-172](https://gitlab.cecs.anu.edu.au/u7497023/comp1110-ass2/-/blob/6eba67c172f69b19ab6dfdfdf44617a0fe65f95e/src/comp1110/ass2/CatanDice.java#L180-200)
and so all resources added to the state are of the same type. Secondly, the bounds for the random integer start at 1 
when the type of resources are indexed from 0. This means that it is impossible for ore to be added to the resource 
state. This method was difficult to test as it does not return a value, rather it updates the parameter object. This may
have been the cause of correctness issues as the method still passes all tests. However, it is possible to use the 
debugger to see what the actual output is. This would have made these issues identifiable at a glance.

The best part of this code is that it is well documented. The java dox documentation is helpful in understanding what 
the method does but Kerry also added comments within the code to explain what is happening. The code also follows all 
java conventions in terms of naming, using camel case for variables. Some variables could be named more appropriately, 
such as [roll2](https://gitlab.cecs.anu.edu.au/u7497023/comp1110-ass2/-/blob/6eba67c172f69b19ab6dfdfdf44617a0fe65f95e/src/comp1110/ass2/CatanDice.java#L180)
which sounds like it refers to the second roll rather than any roll in the result.

There are a number of imperfections with this method that should have been optimised. For example the scanner object 
[scan](https://gitlab.cecs.anu.edu.au/u7497023/comp1110-ass2/-/blob/6eba67c172f69b19ab6dfdfdf44617a0fe65f95e/src/comp1110/ass2/CatanDice.java#L158)
is never used, checking equality between variables uses == rather than Object.equals() and the [hash map](https://gitlab.cecs.anu.edu.au/u7497023/comp1110-ass2/-/blob/6eba67c172f69b19ab6dfdfdf44617a0fe65f95e/src/comp1110/ass2/CatanDice.java#L162)
uses redundant typing when creating a new instance.

The overall structure of the code is not concise nor efficient. As the method refers directly to the input parameter 
rather than giving a return value it would be better to directly update this object rather than creating a new object 
and then copying the results over. This would remove any need for the second loop [180,200](https://gitlab.cecs.anu.edu.au/u7497023/comp1110-ass2/-/blob/6eba67c172f69b19ab6dfdfdf44617a0fe65f95e/src/comp1110/ass2/CatanDice.java#L180-200)
The need for string representations of the resources is also redundant. As each resource corresponds directly to an 
index in the input array, and the random used is an integer, this double conversion could be bypassed. This would remove 
the need for a map and ArrayList, significantly optimising the code.