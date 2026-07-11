# Arrays - 20 Scenario-Based Questions & Answers

## Q1: Given an array of stock prices, find the maximum profit with at most k transactions.
**Answer:** Use dynamic programming with O(n*k) time complexity. Maintain two arrays: one for max profit with at most i transactions up to day j, and another for max profit with at most i transactions with the last transaction on day j. For each day, update the maximum profit by considering either skipping the day or selling on that day.

## Q2: Find the smallest positive integer missing from an unsorted array in O(n) time and O(1) space.
**Answer:** Use the array itself as a hash table. For each number x in range [1, n], place it at index x-1. Then scan to find the first index where value != index+1. This works because we only care about numbers in range [1, n+1].

## Q3: Given an array of integers, find if there exists a subarray with sum equal to zero.
**Answer:** Use a HashSet to store cumulative sums. If the same cumulative sum appears twice, the subarray between those indices sums to zero. Also, if cumulative sum itself is zero, we found a subarray from start.

## Q4: Merge k sorted arrays into one sorted array efficiently.
**Answer:** Use a min-heap of size k. Insert the first element of each array into the heap. Extract minimum, add to result, and insert the next element from the same array. Time complexity: O(nk log k) where n is average array length.

## Q5: Find the majority element in an array where it appears more than n/2 times.
**Answer:** Use Boyer-Moore voting algorithm. Maintain a candidate and count. If current element equals candidate, increment count; else decrement. If count becomes zero, change candidate. The candidate at the end is the majority element.

## Q6: Given an array, find the length of the longest consecutive elements sequence.
**Answer:** Use a HashSet. For each element, if it's the start of a sequence (element-1 not in set), count consecutive elements by checking element+1, element+2, etc. Track maximum length. O(n) time.

## Q7: Rearrange array in alternating positive and negative numbers with O(1) extra space.
**Answer:** Use the "out-of-place" approach with index manipulation. For an array with equal positives and negatives, process in pairs and use the property that even positions should have positive and odd positions should have negative (or vice versa).

## Q8: Find the maximum sum of a subarray with size k (sliding window).
**Answer:** Use sliding window technique. Calculate sum of first k elements, then slide the window by subtracting the leftmost element and adding the new rightmost element. Track maximum sum. O(n) time.

## Q9: Given an array, find the equilibrium index where sum of left elements equals sum of right elements.
**Answer:** Calculate total sum first. Then iterate through array while maintaining left sum. For each index, right sum = total - left - current element. If left == right, return index. Update left sum with current element.

## Q10: Find the minimum number of platforms required for a railway station given arrival and departure times.
**Answer:** Sort both arrival and departure arrays separately. Use two pointers to simulate the process. Count platforms needed when arrival time <= departure time, decrement when departure time < arrival time. Track maximum count.

## Q11: Given an array of 0s and 1s, find the maximum length of a subarray with equal number of 0s and 1s.
**Answer:** Replace 0s with -1s. The problem becomes finding the longest subarray with sum 0. Use a HashMap to store the first occurrence of each cumulative sum. If the same sum appears again, the subarray between indices has sum 0.

## Q12: Find the smallest window in an array containing all elements of another array.
**Answer:** Use sliding window with frequency counting. Count frequencies of elements in the second array. Expand window until all required elements are included, then contract from left to find minimum window.

## Q13: Given an array, find the maximum of j - i such that arr[j] > arr[i].
**Answer:** Create two auxiliary arrays: leftMin[i] = minimum element from 0 to i, rightMax[j] = maximum element from j to n-1. Use two pointers to find maximum j-i where rightMax[j] > leftMin[i].

## Q14: Rotate an array by k positions to the right in O(n) time and O(1) space.
**Answer:** Reverse the entire array, then reverse first k elements, then reverse remaining n-k elements. This achieves the rotation in-place with O(n) time.

## Q15: Find the duplicate number in an array of n+1 integers where each integer is between 1 and n (inclusive).
**Answer:** Use Floyd's cycle detection (tortoise and hare). Treat array as a linked list where index points to value. Find the intersection point of slow and fast pointers, then find the entrance of the cycle which is the duplicate.

## Q16: Given an unsorted array, find the kth smallest element using QuickSelect.
**Answer:** Use QuickSelect algorithm (partition-based selection). Choose a pivot, partition array around pivot, and recursively search in the relevant partition. Average O(n) time, worst O(n²) but can be improved with median-of-medians.

## Q17: Find the maximum sum of i*arr[i] with only rotations allowed on the array.
**Answer:** Use the mathematical relationship between consecutive rotations. If sum of i*arr[i] for rotation 0 is S0, then S1 = S0 - sum + n*arr[n-1]. Precompute total sum and use this formula to calculate all rotation sums in O(n).

## Q18: Given an array, find the length of the longest bitonic subsequence.
**Answer:** Use dynamic programming. Compute LIS (Longest Increasing Subsequence) from left to right and LDS (Longest Decreasing Subsequence) from right to left. For each index, bitonic length = LIS[i] + LDS[i] - 1. Find maximum.

## Q19: Find the minimum number of swaps required to sort an array.
**Answer:** Create a graph where each element is a node and edges connect elements to their correct positions. Count cycles in this graph. Minimum swaps = sum of (cycle_size - 1) for all cycles.

## Q20: Given an array, partition it into two subsets such that the difference of subset sums is minimum.
**Answer:** This is a variation of the partition problem. Use DP to find if a subset with sum = total/2 is possible. The minimum difference = total - 2*max achievable sum <= total/2.
