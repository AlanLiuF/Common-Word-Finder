author: Fengzhe Liu (fl2635)


I believe that the hashmap have the best performance than the other two data structures. AVL Tree perform the second fastest, and Binary Search is the slowest one among those three. 

After I run the command provided in the Cygwin, I find the "hash" result of my program's execution matches my expectations. However, the rank is reversed between the "avl" version and "bst" version.
I displayed the running time (real time) below respectively:
(1) for bst: 0m3.583s
(2) for avl: 0m3.660s
(3) for hash: 0m3.247s
From the actual result, we can see that hashmap performs the best as expected, and avl performs the worst unexpectedly.

Is this CommonWordFinder project, we only used two operations of certain data structure: insertion and probing. As for hashmap, it is actually a mapping from key to value, so the time complexity for insertion (map.put) and probing (map.get) is ideally O(1) and the worst case (O(n)) is rare. So it would be the best. As for  BST tree, when the tree is in its worse case (three does not branch and height = n), the running time for "put" and "get" is O(n). When the tree is in its best case, which means it is a complete binary tree, the running time for "put" and "get" is O(lgn). As for AVL tree, since it is a self-balanced binary search tree and for every node the height of the left and right subtree differs only by at most 1, ideally, it is safe to say that the running time for "put" and "get" is always O(lgn). 
The reasons above can account for my expectation. However, in reality, binary search tree performs better than avl tree. I think maybe it is because, firstly, when we parse text content and read the keys (the unique words) one by one, the words often appear randomly rather than alphabetically, which means that there is a high probability that this tree is relatively balanced. So the runtime for "bst" and "avl" is approximately the same and there are not apparent difference. Secondly and most importantly, when we use the AVL tree, it always takes time to balance (rotate) the current tree if it is unbalanced. This Balance operation is the reason why avl tree is slightly shower than bst. 