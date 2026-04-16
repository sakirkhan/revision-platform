package com.revision_platform;

import com.revision_platform.common.entity.Topic;
import com.revision_platform.common.enums.Difficulty;
import com.revision_platform.questions.entity.Question;
import com.revision_platform.questions.repository.QuestionRepository;
import com.revision_platform.common.repository.TopicRepository;
import com.revision_platform.users.entity.User;
import com.revision_platform.users.repository.UserRepository;
import com.revision_platform.common.constants.DefaultConfigConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

        private final QuestionRepository questionRepository;
        private final TopicRepository topicRepository;
        private final UserRepository userRepository;

        @Override
        public void run(String... args) throws Exception {
                if (userRepository.count() == 0) {
                        seedUsers();
                }
                if (topicRepository.count() == 0) {
                        seedTopics();
                }
                if (questionRepository.count() == 0) {
                        seedQuestions();
                }
        }

        private Map<String, Topic> topicMap = new HashMap<>();

        private void seedUsers() {
                User user = User.builder()
                                .name("sakir khan")
                                .email("sakir.khan9194@gmail.com")
                                .build();
                userRepository.save(user);
        }

        private void seedTopics() {
                List<String> topicNames = Arrays.asList(
                                "Arrays & Hashing", "Two Pointers", "Sliding Window", "Stack", "Binary Search",
                                "Linked List", "Trees", "Heap / Priority Queue", "Backtracking", "Tries",
                                "Graphs", "Advanced Graphs", "1-D Dynamic Programming", "2-D Dynamic Programming",
                                "Greedy", "Intervals", "Math & Geometry", "Bit Manipulation");

                List<Topic> topics = topicNames.stream()
                                .map(name -> Topic.builder().name(name).build())
                                .collect(Collectors.toList());

                topicRepository.saveAll(topics);

                // Cache for question seeding
                topics.forEach(t -> topicMap.put(t.getName(), t));
        }

        private void seedQuestions() {
                List<Question> questions = new ArrayList<>();

                // Arrays & Hashing
                Topic arrays = topicMap.get("Arrays & Hashing");
                questions.add(createQuestion("Contains Duplicate", "https://leetcode.com/problems/contains-duplicate/",
                                "https://neetcode.io/problems/duplicate-integer/question?list=neetcode150", null,
                                Difficulty.EASY, arrays));
                questions.add(createQuestion("Valid Anagram", "https://leetcode.com/problems/valid-anagram/",
                                "https://neetcode.io/problems/is-anagram/question?list=neetcode150", null,
                                Difficulty.EASY, arrays));
                questions.add(createQuestion("Two Sum", "https://leetcode.com/problems/two-sum/",
                                "https://neetcode.io/problems/two-integer-sum/question?list=neetcode150", null,
                                Difficulty.EASY, arrays));
                questions.add(createQuestion("Group Anagrams", "https://leetcode.com/problems/group-anagrams/",
                                "https://neetcode.io/problems/anagram-groups/question?list=neetcode150", null,
                                Difficulty.MEDIUM, arrays));
                questions.add(createQuestion("Top K Frequent Elements",
                                "https://leetcode.com/problems/top-k-frequent-elements/",
                                "https://neetcode.io/problems/top-k-elements-in-list/question?list=neetcode150", null,
                                Difficulty.MEDIUM, arrays));
                questions.add(createQuestion("Product of Array Except Self",
                                "https://leetcode.com/problems/product-of-array-except-self/",
                                "https://neetcode.io/problems/products-of-array-discluding-self/question?list=neetcode150",
                                null, Difficulty.MEDIUM, arrays));
                questions.add(createQuestion("Valid Sudoku", "https://leetcode.com/problems/valid-sudoku/",
                                "https://neetcode.io/problems/valid-sudoku/question?list=neetcode150", null,
                                Difficulty.MEDIUM, arrays));
                questions.add(createQuestion("Encode and Decode Strings",
                                "https://leetcode.com/problems/encode-and-decode-strings/",
                                "https://neetcode.io/problems/string-encode-and-decode/question",
                                "Design an algorithm to encode a list of strings to a single string. The encoded string is then sent over the network and is decoded back to the original list of strings.",
                                Difficulty.MEDIUM, arrays));
                questions.add(createQuestion("Longest Consecutive Sequence",
                                "https://leetcode.com/problems/longest-consecutive-sequence/",
                                "https://neetcode.io/problems/longest-consecutive-sequence/question?list=neetcode150",
                                null, Difficulty.MEDIUM, arrays));

                // Two Pointers
                Topic twoPointers = topicMap.get("Two Pointers");
                questions.add(createQuestion("Valid Palindrome", "https://leetcode.com/problems/valid-palindrome/",
                                "https://neetcode.io/problems/is-palindrome/question?list=neetcode150", null,
                                Difficulty.EASY, twoPointers));
                questions.add(createQuestion("Two Sum II Input Array Is Sorted",
                                "https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/",
                                "https://neetcode.io/problems/two-integer-sum-ii/question?list=neetcode150", null,
                                Difficulty.MEDIUM, twoPointers));
                questions.add(createQuestion("3Sum", "https://leetcode.com/problems/3sum/",
                                "https://neetcode.io/problems/three-integer-sum/question?list=neetcode150", null,
                                Difficulty.MEDIUM, twoPointers));
                questions.add(createQuestion("Container With Most Water",
                                "https://leetcode.com/problems/container-with-most-water/",
                                "https://neetcode.io/problems/max-water-container/question?list=neetcode150", null,
                                Difficulty.MEDIUM, twoPointers));
                questions.add(createQuestion("Trapping Rain Water",
                                "https://leetcode.com/problems/trapping-rain-water/",
                                "https://neetcode.io/problems/trapping-rain-water/question?list=neetcode150", null,
                                Difficulty.HARD, twoPointers));

                // Sliding Window
                Topic slidingWindow = topicMap.get("Sliding Window");
                questions.add(createQuestion("Best Time to Buy and Sell Stock",
                                "https://leetcode.com/problems/best-time-to-buy-and-sell-stock/",
                                "https://neetcode.io/problems/buy-and-sell-crypto/question?list=neetcode150", null,
                                Difficulty.EASY, slidingWindow));
                questions.add(createQuestion("Longest Substring Without Repeating Characters",
                                "https://leetcode.com/problems/longest-substring-without-repeating-characters/",
                                "https://neetcode.io/problems/longest-repeating-substring-with-replacement/question?list=neetcode150",
                                null, Difficulty.MEDIUM, slidingWindow));
                questions.add(createQuestion("Longest Repeating Character Replacement",
                                "https://leetcode.com/problems/longest-repeating-character-replacement/",
                                "https://neetcode.io/problems/longest-substring-without-duplicates/question?list=neetcode150",
                                null, Difficulty.MEDIUM, slidingWindow));
                questions.add(createQuestion("Permutation in String",
                                "https://leetcode.com/problems/permutation-in-string/",
                                "https://neetcode.io/problems/permutation-string/question?list=neetcode150", null,
                                Difficulty.MEDIUM, slidingWindow));
                questions.add(createQuestion("Minimum Window Substring",
                                "https://leetcode.com/problems/minimum-window-substring/",
                                "https://neetcode.io/problems/minimum-window-with-characters/question?list=neetcode150",
                                null, Difficulty.HARD, slidingWindow));
                questions.add(createQuestion("Sliding Window Maximum",
                                "https://leetcode.com/problems/sliding-window-maximum/",
                                "https://neetcode.io/problems/sliding-window-maximum/question?list=neetcode150", null,
                                Difficulty.HARD, slidingWindow));

                // Stack
                Topic stack = topicMap.get("Stack");
                questions.add(createQuestion("Valid Parentheses", "https://leetcode.com/problems/valid-parentheses/",
                                "https://neetcode.io/problems/validate-parentheses/question?list=neetcode150", null,
                                Difficulty.EASY, stack));
                questions.add(createQuestion("Min Stack", "https://leetcode.com/problems/min-stack/",
                                "https://neetcode.io/problems/minimum-stack/question?list=neetcode150", null,
                                Difficulty.MEDIUM, stack));
                questions.add(createQuestion("Evaluate Reverse Polish Notation",
                                "https://leetcode.com/problems/evaluate-reverse-polish-notation/",
                                "https://neetcode.io/problems/evaluate-reverse-polish-notation/question?list=neetcode150",
                                null, Difficulty.MEDIUM, stack));
                questions.add(createQuestion("Generate Parentheses",
                                "https://leetcode.com/problems/generate-parentheses/",
                                "https://neetcode.io/problems/generate-parentheses/question?list=neetcode150", null,
                                Difficulty.MEDIUM, stack));
                questions.add(createQuestion("Daily Temperatures", "https://leetcode.com/problems/daily-temperatures/",
                                "https://neetcode.io/problems/daily-temperatures/question?list=neetcode150", null,
                                Difficulty.MEDIUM, stack));
                questions.add(createQuestion("Car Fleet", "https://leetcode.com/problems/car-fleet/",
                                "https://neetcode.io/problems/car-fleet/question?list=neetcode150", null,
                                Difficulty.MEDIUM, stack));
                questions.add(createQuestion("Largest Rectangle in Histogram",
                                "https://leetcode.com/problems/largest-rectangle-in-histogram/",
                                "https://neetcode.io/problems/largest-rectangle-in-histogram/question?list=neetcode150",
                                null, Difficulty.HARD, stack));

                // Binary Search
                Topic binarySearch = topicMap.get("Binary Search");
                questions.add(createQuestion("Binary Search", "https://leetcode.com/problems/binary-search/",
                                "https://neetcode.io/problems/binary-search/question?list=neetcode150", null,
                                Difficulty.EASY, binarySearch));
                questions.add(createQuestion("Search a 2D Matrix", "https://leetcode.com/problems/search-a-2d-matrix/",
                                "https://neetcode.io/problems/search-2d-matrix/question?list=neetcode150", null,
                                Difficulty.MEDIUM, binarySearch));
                questions.add(createQuestion("Koko Eating Bananas",
                                "https://leetcode.com/problems/koko-eating-bananas/",
                                "https://neetcode.io/problems/eating-bananas/question?list=neetcode150", null,
                                Difficulty.MEDIUM, binarySearch));
                questions.add(createQuestion("Find Minimum in Rotated Sorted Array",
                                "https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/",
                                "https://neetcode.io/problems/find-minimum-in-rotated-sorted-array/question?list=neetcode150",
                                null, Difficulty.MEDIUM, binarySearch));
                questions.add(createQuestion("Search in Rotated Sorted Array",
                                "https://leetcode.com/problems/search-in-rotated-sorted-array/",
                                "https://neetcode.io/problems/find-target-in-rotated-sorted-array/question?list=neetcode150",
                                null, Difficulty.MEDIUM, binarySearch));
                questions.add(createQuestion("Time Based Key-Value Store",
                                "https://leetcode.com/problems/time-based-key-value-store/",
                                "https://neetcode.io/problems/time-based-key-value-store/question?list=neetcode150",
                                null, Difficulty.MEDIUM, binarySearch));
                questions.add(createQuestion("Median of Two Sorted Arrays",
                                "https://leetcode.com/problems/median-of-two-sorted-arrays/",
                                "https://neetcode.io/problems/median-of-two-sorted-arrays/question?list=neetcode150",
                                null, Difficulty.HARD, binarySearch));

                // Linked List
                Topic linkedList = topicMap.get("Linked List");
                questions.add(createQuestion("Reverse Linked List",
                                "https://leetcode.com/problems/reverse-linked-list/",
                                "https://neetcode.io/problems/reverse-a-linked-list/question?list=neetcode150", null,
                                Difficulty.EASY, linkedList));
                questions.add(createQuestion("Merge Two Sorted Lists",
                                "https://leetcode.com/problems/merge-two-sorted-lists/",
                                "https://neetcode.io/problems/merge-two-sorted-linked-lists/question?list=neetcode150",
                                null, Difficulty.EASY, linkedList));
                questions.add(createQuestion("Reorder List", "https://leetcode.com/problems/reorder-list/",
                                "https://neetcode.io/problems/reorder-linked-list/question?list=neetcode150", null,
                                Difficulty.MEDIUM, linkedList));
                questions.add(createQuestion("Remove Nth Node From End of List",
                                "https://leetcode.com/problems/remove-nth-node-from-end-of-list/",
                                "https://neetcode.io/problems/remove-node-from-end-of-linked-list/question?list=neetcode150",
                                null, Difficulty.MEDIUM, linkedList));
                questions.add(createQuestion("Copy List with Random Pointer",
                                "https://leetcode.com/problems/copy-list-with-random-pointer/",
                                "https://neetcode.io/problems/copy-linked-list-with-random-pointer/question?list=neetcode150",
                                null, Difficulty.MEDIUM, linkedList));
                questions.add(createQuestion("Add Two Numbers", "https://leetcode.com/problems/add-two-numbers/",
                                "https://neetcode.io/problems/add-two-numbers/question?list=neetcode150", null,
                                Difficulty.MEDIUM, linkedList));
                questions.add(createQuestion("Linked List Cycle", "https://leetcode.com/problems/linked-list-cycle/",
                                "https://neetcode.io/problems/linked-list-cycle-detection/question?list=neetcode150",
                                null, Difficulty.EASY, linkedList));
                questions.add(createQuestion("Find the Duplicate Number",
                                "https://leetcode.com/problems/find-the-duplicate-number/",
                                "https://neetcode.io/problems/find-duplicate-integer/question?list=neetcode150", null,
                                Difficulty.MEDIUM, linkedList));
                questions.add(createQuestion("LRU Cache", "https://leetcode.com/problems/lru-cache/",
                                "https://neetcode.io/problems/lru-cache/question?list=neetcode150", null,
                                Difficulty.MEDIUM, linkedList));
                questions.add(createQuestion("Merge k Sorted Lists",
                                "https://leetcode.com/problems/merge-k-sorted-lists/",
                                "https://neetcode.io/problems/merge-k-sorted-linked-lists/question?list=neetcode150",
                                null, Difficulty.HARD, linkedList));
                questions.add(createQuestion("Reverse Nodes in k-Group",
                                "https://leetcode.com/problems/reverse-nodes-in-k-group/",
                                "https://neetcode.io/problems/reverse-nodes-in-k-group/question?list=neetcode150", null,
                                Difficulty.HARD, linkedList));

                // Trees
                Topic trees = topicMap.get("Trees");
                questions.add(createQuestion("Invert Binary Tree", "https://leetcode.com/problems/invert-binary-tree/",
                                "https://neetcode.io/problems/invert-a-binary-tree/question?list=neetcode150", null,
                                Difficulty.EASY, trees));
                questions.add(createQuestion("Maximum Depth of Binary Tree",
                                "https://leetcode.com/problems/maximum-depth-of-binary-tree/",
                                "https://neetcode.io/problems/depth-of-binary-tree/question?list=neetcode150", null,
                                Difficulty.EASY, trees));
                questions.add(createQuestion("Diameter of Binary Tree",
                                "https://leetcode.com/problems/diameter-of-binary-tree/",
                                "https://neetcode.io/problems/binary-tree-diameter/question?list=neetcode150", null,
                                Difficulty.EASY, trees));
                questions.add(createQuestion("Balanced Binary Tree",
                                "https://leetcode.com/problems/balanced-binary-tree/",
                                "https://neetcode.io/problems/balanced-binary-tree/question?list=neetcode150", null,
                                Difficulty.EASY, trees));
                questions.add(createQuestion("Same Tree", "https://leetcode.com/problems/same-tree/",
                                "https://neetcode.io/problems/same-binary-tree/question?list=neetcode150", null,
                                Difficulty.EASY, trees));
                questions.add(createQuestion("Subtree of Another Tree",
                                "https://leetcode.com/problems/subtree-of-another-tree/",
                                "https://neetcode.io/problems/subtree-of-a-binary-tree/question?list=neetcode150", null,
                                Difficulty.EASY, trees));
                questions.add(createQuestion("Lowest Common Ancestor of a Binary Search Tree",
                                "https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/",
                                "https://neetcode.io/problems/lowest-common-ancestor-in-binary-search-tree/question?list=neetcode150",
                                null, Difficulty.MEDIUM, trees));
                questions.add(createQuestion("Binary Tree Level Order Traversal",
                                "https://leetcode.com/problems/binary-tree-level-order-traversal/",
                                "https://neetcode.io/problems/level-order-traversal-of-binary-tree/question?list=neetcode150",
                                null, Difficulty.MEDIUM, trees));
                questions.add(createQuestion("Binary Tree Right Side View",
                                "https://leetcode.com/problems/binary-tree-right-side-view/",
                                "https://neetcode.io/problems/binary-tree-right-side-view/question?list=neetcode150",
                                null, Difficulty.MEDIUM, trees));
                questions.add(createQuestion("Count Good Nodes in Binary Tree",
                                "https://leetcode.com/problems/count-good-nodes-in-binary-tree/",
                                "https://neetcode.io/problems/count-good-nodes-in-binary-tree/question?list=neetcode150",
                                null, Difficulty.MEDIUM, trees));
                questions.add(createQuestion("Validate Binary Search Tree",
                                "https://leetcode.com/problems/validate-binary-search-tree/",
                                "https://neetcode.io/problems/valid-binary-search-tree/question?list=neetcode150", null,
                                Difficulty.MEDIUM, trees));
                questions.add(createQuestion("Kth Smallest Element in a BST",
                                "https://leetcode.com/problems/kth-smallest-element-in-a-bst/",
                                "https://neetcode.io/problems/kth-smallest-integer-in-bst/question?list=neetcode150",
                                null, Difficulty.MEDIUM, trees));
                questions.add(createQuestion("Construct Binary Tree from Preorder and Inorder Traversal",
                                "https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/",
                                "https://neetcode.io/problems/binary-tree-from-preorder-and-inorder-traversal/question?list=neetcode150",
                                null, Difficulty.MEDIUM, trees));
                questions.add(createQuestion("Binary Tree Maximum Path Sum",
                                "https://leetcode.com/problems/binary-tree-maximum-path-sum/",
                                "https://neetcode.io/problems/binary-tree-maximum-path-sum/question?list=neetcode150",
                                null, Difficulty.HARD, trees));
                questions.add(createQuestion("Serialize and Deserialize Binary Tree",
                                "https://leetcode.com/problems/serialize-and-deserialize-binary-tree/",
                                "https://neetcode.io/problems/serialize-and-deserialize-binary-tree/question?list=neetcode150",
                                null, Difficulty.HARD, trees));

                // Heap / Priority Queue
                Topic heap = topicMap.get("Heap / Priority Queue");
                questions.add(createQuestion("Kth Largest Element in a Stream",
                                "https://leetcode.com/problems/kth-largest-element-in-a-stream/",
                                "https://neetcode.io/problems/kth-largest-integer-in-a-stream/question?list=neetcode150",
                                null, Difficulty.EASY, heap));
                questions.add(createQuestion("Last Stone Weight", "https://leetcode.com/problems/last-stone-weight/",
                                "https://neetcode.io/problems/last-stone-weight/question?list=neetcode150", null,
                                Difficulty.EASY, heap));
                questions.add(createQuestion("K Closest Points to Origin",
                                "https://leetcode.com/problems/k-closest-points-to-origin/",
                                "https://neetcode.io/problems/k-closest-points-to-origin/question?list=neetcode150",
                                null, Difficulty.MEDIUM, heap));
                questions.add(createQuestion("Kth Largest Element in an Array",
                                "https://leetcode.com/problems/kth-largest-element-in-an-array/",
                                "https://neetcode.io/problems/kth-largest-element-in-an-array/question?list=neetcode150",
                                null, Difficulty.MEDIUM, heap));
                questions.add(createQuestion("Task Scheduler", "https://leetcode.com/problems/task-scheduler/",
                                "https://neetcode.io/problems/task-scheduling/question?list=neetcode150", null,
                                Difficulty.MEDIUM, heap));
                questions.add(createQuestion("Design Twitter", "https://leetcode.com/problems/design-twitter/",
                                "https://neetcode.io/problems/design-twitter-feed/question?list=neetcode150", null,
                                Difficulty.MEDIUM, heap));
                questions.add(createQuestion("Find Median from Data Stream",
                                "https://leetcode.com/problems/find-median-from-data-stream/",
                                "https://neetcode.io/problems/find-median-in-a-data-stream/question?list=neetcode150",
                                null, Difficulty.HARD, heap));

                // Backtracking
                Topic backtracking = topicMap.get("Backtracking");
                questions.add(createQuestion("Subsets", "https://leetcode.com/problems/subsets/",
                                "https://neetcode.io/problems/subsets/question?list=neetcode150", null,
                                Difficulty.MEDIUM, backtracking));
                questions.add(createQuestion("Combination Sum", "https://leetcode.com/problems/combination-sum/",
                                "https://neetcode.io/problems/combination-target-sum/question?list=neetcode150", null,
                                Difficulty.MEDIUM, backtracking));
                questions.add(createQuestion("Permutations", "https://leetcode.com/problems/permutations/",
                                "https://neetcode.io/problems/permutations/question?list=neetcode150", null,
                                Difficulty.MEDIUM, backtracking));
                questions.add(createQuestion("Subsets II", "https://leetcode.com/problems/subsets-ii/",
                                "https://neetcode.io/problems/subsets-ii/question?list=neetcode150", null,
                                Difficulty.MEDIUM, backtracking));
                questions.add(createQuestion("Combination Sum II", "https://leetcode.com/problems/combination-sum-ii/",
                                "https://neetcode.io/problems/combination-target-sum-ii/question?list=neetcode150",
                                null, Difficulty.MEDIUM, backtracking));
                questions.add(createQuestion("Word Search", "https://leetcode.com/problems/word-search/",
                                "https://neetcode.io/problems/search-for-word/question?list=neetcode150", null,
                                Difficulty.MEDIUM, backtracking));
                questions.add(createQuestion("Palindrome Partitioning",
                                "https://leetcode.com/problems/palindrome-partitioning/",
                                "https://neetcode.io/problems/palindrome-partitioning/question?list=neetcode150", null,
                                Difficulty.MEDIUM, backtracking));
                questions.add(createQuestion("Letter Combinations of a Phone Number",
                                "https://leetcode.com/problems/letter-combinations-of-a-phone-number/",
                                "https://neetcode.io/problems/combinations-of-a-phone-number/question?list=neetcode150",
                                null, Difficulty.MEDIUM, backtracking));
                questions.add(createQuestion("N-Queens", "https://leetcode.com/problems/n-queens/",
                                "https://neetcode.io/problems/n-queens/question?list=neetcode150", null,
                                Difficulty.HARD, backtracking));

                // Tries
                Topic tries = topicMap.get("Tries");
                questions.add(createQuestion("Implement Trie (Prefix Tree)",
                                "https://leetcode.com/problems/implement-trie-prefix-tree/",
                                "https://neetcode.io/problems/implement-prefix-tree/question?list=neetcode150", null,
                                Difficulty.MEDIUM, tries));
                questions.add(createQuestion("Design Add and Search Words Data Structure",
                                "https://leetcode.com/problems/design-add-and-search-words-data-structure/",
                                "https://neetcode.io/problems/design-word-search-data-structure/question?list=neetcode150",
                                null, Difficulty.MEDIUM, tries));
                questions.add(createQuestion("Word Search II", "https://leetcode.com/problems/word-search-ii/",
                                "https://neetcode.io/problems/search-for-word-ii/question?list=neetcode150", null,
                                Difficulty.HARD, tries));

                // Graphs
                Topic graphs = topicMap.get("Graphs");
                questions.add(createQuestion("Number of Islands", "https://leetcode.com/problems/number-of-islands/",
                                "https://neetcode.io/problems/count-number-of-islands/question?list=neetcode150", null,
                                Difficulty.MEDIUM, graphs));
                questions.add(createQuestion("Max Area of Island", "https://leetcode.com/problems/max-area-of-island/",
                                "https://neetcode.io/problems/max-area-of-island/question?list=neetcode150", null,
                                Difficulty.MEDIUM, graphs));
                questions.add(createQuestion("Clone Graph", "https://leetcode.com/problems/clone-graph/",
                                "https://neetcode.io/problems/clone-graph/question?list=neetcode150", null,
                                Difficulty.MEDIUM, graphs));
                questions.add(createQuestion("Walls and Gates", "https://leetcode.com/problems/walls-and-gates/",
                                "https://neetcode.io/problems/islands-and-treasure/question?list=neetcode150", null,
                                Difficulty.MEDIUM, graphs));
                questions.add(createQuestion("Rotting Oranges", "https://leetcode.com/problems/rotting-oranges/",
                                "https://neetcode.io/problems/rotting-fruit/question?list=neetcode150", null,
                                Difficulty.MEDIUM, graphs));
                questions.add(createQuestion("Pacific Atlantic Water Flow",
                                "https://leetcode.com/problems/pacific-atlantic-water-flow/",
                                "https://neetcode.io/problems/pacific-atlantic-water-flow/question?list=neetcode150",
                                null, Difficulty.MEDIUM, graphs));
                questions.add(createQuestion("Surrounded Regions", "https://leetcode.com/problems/surrounded-regions/",
                                "https://neetcode.io/problems/surrounded-regions/question?list=neetcode150", null,
                                Difficulty.MEDIUM, graphs));
                questions.add(createQuestion("Course Schedule", "https://leetcode.com/problems/course-schedule/",
                                "https://neetcode.io/problems/course-schedule/question?list=neetcode150", null,
                                Difficulty.MEDIUM, graphs));
                questions.add(createQuestion("Course Schedule II", "https://leetcode.com/problems/course-schedule-ii/",
                                "https://neetcode.io/problems/course-schedule-ii/question?list=neetcode150", null,
                                Difficulty.MEDIUM, graphs));
                questions.add(createQuestion("Graph Valid Tree", "https://leetcode.com/problems/graph-valid-tree/",
                                "https://neetcode.io/problems/valid-tree/question?list=neetcode150", null,
                                Difficulty.MEDIUM, graphs));
                questions.add(createQuestion("Number of Connected Components in an Undirected Graph",
                                "https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/",
                                "https://neetcode.io/problems/count-connected-components/question?list=neetcode150",
                                null, Difficulty.MEDIUM, graphs));
                questions.add(createQuestion("Redundant Connection",
                                "https://leetcode.com/problems/redundant-connection/",
                                "https://neetcode.io/problems/redundant-connection/question?list=neetcode150", null,
                                Difficulty.MEDIUM, graphs));
                questions.add(createQuestion("Word Ladder", "https://leetcode.com/problems/word-ladder/",
                                "https://neetcode.io/problems/word-ladder/question?list=neetcode150", null,
                                Difficulty.HARD, graphs));

                // Advanced Graphs
                Topic advGraphs = topicMap.get("Advanced Graphs");
                questions.add(createQuestion("Network Delay Time", "https://leetcode.com/problems/network-delay-time/",
                                "https://neetcode.io/problems/network-delay-time/question?list=neetcode150", null,
                                Difficulty.MEDIUM, advGraphs));
                questions.add(createQuestion("Reconstruct Itinerary",
                                "https://leetcode.com/problems/reconstruct-itinerary/",
                                "https://neetcode.io/problems/reconstruct-flight-path/question?list=neetcode150", null,
                                Difficulty.HARD, advGraphs));
                questions.add(createQuestion("Min Cost to Connect All Points",
                                "https://leetcode.com/problems/min-cost-to-connect-all-points/",
                                "https://neetcode.io/problems/min-cost-to-connect-points/question?list=neetcode150",
                                null, Difficulty.MEDIUM, advGraphs));
                questions.add(createQuestion("Cheapest Flights Within K Stops",
                                "https://leetcode.com/problems/cheapest-flights-within-k-stops/",
                                "https://neetcode.io/problems/cheapest-flight-path/question?list=neetcode150", null,
                                Difficulty.MEDIUM, advGraphs));
                questions.add(createQuestion("Swim in Rising Water",
                                "https://leetcode.com/problems/swim-in-rising-water/",
                                "https://neetcode.io/problems/swim-in-rising-water/question?list=neetcode150", null,
                                Difficulty.HARD, advGraphs));
                questions.add(createQuestion("Alien Dictionary", "https://leetcode.com/problems/alien-dictionary/",
                                "https://neetcode.io/problems/foreign-dictionary/question?list=neetcode150", null,
                                Difficulty.HARD, advGraphs));

                // 1-D DP
                Topic dp1 = topicMap.get("1-D Dynamic Programming");
                questions.add(createQuestion("Climbing Stairs", "https://leetcode.com/problems/climbing-stairs/",
                                "https://neetcode.io/problems/climbing-stairs/question?list=neetcode150", null,
                                Difficulty.EASY, dp1));
                questions.add(createQuestion("Min Cost Climbing Stairs",
                                "https://leetcode.com/problems/min-cost-climbing-stairs/",
                                "https://neetcode.io/problems/min-cost-climbing-stairs/question?list=neetcode150", null,
                                Difficulty.EASY, dp1));
                questions.add(createQuestion("House Robber", "https://leetcode.com/problems/house-robber/",
                                "https://neetcode.io/problems/house-robber/question?list=neetcode150", null,
                                Difficulty.MEDIUM, dp1));
                questions.add(createQuestion("House Robber II", "https://leetcode.com/problems/house-robber-ii/",
                                "https://neetcode.io/problems/house-robber-ii/question?list=neetcode150", null,
                                Difficulty.MEDIUM, dp1));
                questions.add(createQuestion("Longest Palindromic Substring",
                                "https://leetcode.com/problems/longest-palindromic-substring/",
                                "https://neetcode.io/problems/longest-palindromic-substring/question?list=neetcode150",
                                null, Difficulty.MEDIUM, dp1));
                questions.add(createQuestion("Palindromic Substrings",
                                "https://leetcode.com/problems/palindromic-substrings/",
                                "https://neetcode.io/problems/palindromic-substrings/question?list=neetcode150", null,
                                Difficulty.MEDIUM, dp1));
                questions.add(createQuestion("Decode Ways", "https://leetcode.com/problems/decode-ways/",
                                "https://neetcode.io/problems/decode-ways/question?list=neetcode150", null,
                                Difficulty.MEDIUM, dp1));
                questions.add(createQuestion("Coin Change", "https://leetcode.com/problems/coin-change/",
                                "https://neetcode.io/problems/coin-change/question?list=neetcode150", null,
                                Difficulty.MEDIUM, dp1));
                questions.add(createQuestion("Maximum Product Subarray",
                                "https://leetcode.com/problems/maximum-product-subarray/",
                                "https://neetcode.io/problems/maximum-product-subarray/question?list=neetcode150", null,
                                Difficulty.MEDIUM, dp1));
                questions.add(createQuestion("Word Break", "https://leetcode.com/problems/word-break/",
                                "https://neetcode.io/problems/word-break/question?list=neetcode150", null,
                                Difficulty.MEDIUM, dp1));
                questions.add(createQuestion("Longest Increasing Subsequence",
                                "https://leetcode.com/problems/longest-increasing-subsequence/",
                                "https://neetcode.io/problems/longest-increasing-subsequence/question?list=neetcode150",
                                null, Difficulty.MEDIUM, dp1));
                questions.add(createQuestion("Partition Equal Subset Sum",
                                "https://leetcode.com/problems/partition-equal-subset-sum/",
                                "https://neetcode.io/problems/partition-equal-subset-sum/question?list=neetcode150",
                                null, Difficulty.MEDIUM, dp1));

                // 2-D DP
                Topic dp2 = topicMap.get("2-D Dynamic Programming");
                questions.add(createQuestion("Unique Paths", "https://leetcode.com/problems/unique-paths/",
                                "https://neetcode.io/problems/count-paths/question?list=neetcode150", null,
                                Difficulty.MEDIUM, dp2));
                questions.add(createQuestion("Longest Common Subsequence",
                                "https://leetcode.com/problems/longest-common-subsequence/",
                                "https://neetcode.io/problems/longest-common-subsequence/question?list=neetcode150",
                                null, Difficulty.MEDIUM, dp2));
                questions.add(createQuestion("Best Time to Buy and Sell Stock with Cooldown",
                                "https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/",
                                "https://neetcode.io/problems/buy-and-sell-crypto-with-cooldown/question?list=neetcode150",
                                null, Difficulty.MEDIUM, dp2));
                questions.add(createQuestion("Coin Change II", "https://leetcode.com/problems/coin-change-ii/",
                                "https://neetcode.io/problems/coin-change-ii/question?list=neetcode150", null,
                                Difficulty.MEDIUM, dp2));
                questions.add(createQuestion("Target Sum", "https://leetcode.com/problems/target-sum/",
                                "https://neetcode.io/problems/target-sum/question?list=neetcode150", null,
                                Difficulty.MEDIUM, dp2));
                questions.add(createQuestion("Interleaving String",
                                "https://leetcode.com/problems/interleaving-string/",
                                "https://neetcode.io/problems/interleaving-string/question?list=neetcode150", null,
                                Difficulty.MEDIUM, dp2));
                questions.add(createQuestion("Longest Increasing Path in a Matrix",
                                "https://leetcode.com/problems/longest-increasing-path-in-a-matrix/",
                                "https://neetcode.io/problems/longest-increasing-path-in-matrix/question?list=neetcode150",
                                null, Difficulty.HARD, dp2));
                questions.add(createQuestion("Distinct Subsequences",
                                "https://leetcode.com/problems/distinct-subsequences/",
                                "https://neetcode.io/problems/count-subsequences/question?list=neetcode150", null,
                                Difficulty.HARD, dp2));
                questions.add(createQuestion("Edit Distance", "https://leetcode.com/problems/edit-distance/",
                                "https://neetcode.io/problems/edit-distance/question?list=neetcode150", null,
                                Difficulty.HARD, dp2));
                questions.add(createQuestion("Burst Balloons", "https://leetcode.com/problems/burst-balloons/",
                                "https://neetcode.io/problems/burst-balloons/question?list=neetcode150", null,
                                Difficulty.HARD, dp2));
                questions.add(createQuestion("Regular Expression Matching",
                                "https://leetcode.com/problems/regular-expression-matching/",
                                "https://neetcode.io/problems/regular-expression-matching/question?list=neetcode150",
                                null, Difficulty.HARD, dp2));

                // Greedy
                Topic greedy = topicMap.get("Greedy");
                questions.add(createQuestion("Maximum Subarray", "https://leetcode.com/problems/maximum-subarray/",
                                "https://neetcode.io/problems/maximum-subarray/question?list=neetcode150", null,
                                Difficulty.MEDIUM, greedy));
                questions.add(createQuestion("Jump Game", "https://leetcode.com/problems/jump-game/",
                                "https://neetcode.io/problems/jump-game/question?list=neetcode150", null,
                                Difficulty.MEDIUM, greedy));
                questions.add(createQuestion("Jump Game II", "https://leetcode.com/problems/jump-game-ii/",
                                "https://neetcode.io/problems/jump-game-ii/question?list=neetcode150", null,
                                Difficulty.MEDIUM, greedy));
                questions.add(createQuestion("Gas Station", "https://leetcode.com/problems/gas-station/",
                                "https://neetcode.io/problems/gas-station/question?list=neetcode150", null,
                                Difficulty.MEDIUM, greedy));
                questions.add(createQuestion("Hand of Straights", "https://leetcode.com/problems/hand-of-straights/",
                                "https://neetcode.io/problems/hand-of-straights/question?list=neetcode150", null,
                                Difficulty.MEDIUM, greedy));
                questions.add(createQuestion("Merge Triplets to Form Target Triplet",
                                "https://leetcode.com/problems/merge-triplets-to-form-target-triplet/",
                                "https://neetcode.io/problems/merge-triplets-to-form-target/question?list=neetcode150",
                                null, Difficulty.MEDIUM, greedy));
                questions.add(createQuestion("Partition Labels", "https://leetcode.com/problems/partition-labels/",
                                "https://neetcode.io/problems/partition-labels/question?list=neetcode150", null,
                                Difficulty.MEDIUM, greedy));
                questions.add(createQuestion("Valid Parenthesis String",
                                "https://leetcode.com/problems/valid-parenthesis-string/",
                                "https://neetcode.io/problems/valid-parenthesis-string/question?list=neetcode150", null,
                                Difficulty.MEDIUM, greedy));

                // Intervals
                Topic intervals = topicMap.get("Intervals");
                questions.add(createQuestion("Insert Interval", "https://leetcode.com/problems/insert-interval/",
                                "https://neetcode.io/problems/insert-new-interval/question?list=neetcode150", null,
                                Difficulty.MEDIUM, intervals));
                questions.add(createQuestion("Merge Intervals", "https://leetcode.com/problems/merge-intervals/",
                                "https://neetcode.io/problems/merge-intervals/question?list=neetcode150", null,
                                Difficulty.MEDIUM, intervals));
                questions.add(createQuestion("Non-overlapping Intervals",
                                "https://leetcode.com/problems/non-overlapping-intervals/",
                                "https://neetcode.io/problems/non-overlapping-intervals/question?list=neetcode150",
                                null, Difficulty.MEDIUM, intervals));
                questions.add(createQuestion("Meeting Rooms", "https://leetcode.com/problems/meeting-rooms/",
                                "https://neetcode.io/problems/meeting-schedule/question?list=neetcode150", null,
                                Difficulty.MEDIUM, intervals));
                questions.add(createQuestion("Meeting Rooms II", "https://leetcode.com/problems/meeting-rooms-ii/",
                                "https://neetcode.io/problems/meeting-schedule-ii/question?list=neetcode150", null,
                                Difficulty.MEDIUM, intervals));
                questions.add(createQuestion("Minimum Interval to Include Each Query",
                                "https://leetcode.com/problems/minimum-interval-to-include-each-query/",
                                "https://neetcode.io/problems/minimum-interval-including-query/question?list=neetcode150",
                                null, Difficulty.HARD, intervals));

                // Math & Geometry
                Topic math = topicMap.get("Math & Geometry");
                questions.add(createQuestion("Rotate Image", "https://leetcode.com/problems/rotate-image/",
                                "https://neetcode.io/problems/rotate-matrix/question?list=neetcode150", null,
                                Difficulty.MEDIUM, math));
                questions.add(createQuestion("Spiral Matrix", "https://leetcode.com/problems/spiral-matrix/",
                                "https://neetcode.io/problems/spiral-matrix/question?list=neetcode150", null,
                                Difficulty.MEDIUM, math));
                questions.add(createQuestion("Set Matrix Zeroes", "https://leetcode.com/problems/set-matrix-zeroes/",
                                "https://neetcode.io/problems/set-zeroes-in-matrix/question?list=neetcode150", null,
                                Difficulty.MEDIUM, math));
                questions.add(createQuestion("Happy Number", "https://leetcode.com/problems/happy-number/",
                                "https://neetcode.io/problems/non-cyclical-number/question?list=neetcode150", null,
                                Difficulty.EASY, math));
                questions.add(createQuestion("Plus One", "https://leetcode.com/problems/plus-one/",
                                "https://neetcode.io/problems/plus-one/question?list=neetcode150", null,
                                Difficulty.EASY, math));
                questions.add(createQuestion("Pow(x, n)", "https://leetcode.com/problems/powx-n/",
                                "https://neetcode.io/problems/pow-x-n/question?list=neetcode150", null,
                                Difficulty.MEDIUM, math));
                questions.add(createQuestion("Multiply Strings", "https://leetcode.com/problems/multiply-strings/",
                                "https://neetcode.io/problems/multiply-strings/question?list=neetcode150", null,
                                Difficulty.MEDIUM, math));
                questions.add(createQuestion("Detect Squares", "https://leetcode.com/problems/detect-squares/",
                                "https://neetcode.io/problems/count-squares/question?list=neetcode150", null,
                                Difficulty.MEDIUM, math));

                // Bit Manipulation
                Topic bit = topicMap.get("Bit Manipulation");
                questions.add(createQuestion("Single Number", "https://leetcode.com/problems/single-number/",
                                "https://neetcode.io/problems/single-number/question?list=neetcode150", null,
                                Difficulty.EASY, bit));
                questions.add(createQuestion("Number of 1 Bits", "https://leetcode.com/problems/number-of-1-bits/",
                                "https://neetcode.io/problems/number-of-one-bits/question?list=neetcode150", null,
                                Difficulty.EASY, bit));
                questions.add(createQuestion("Counting Bits", "https://leetcode.com/problems/counting-bits/",
                                "https://neetcode.io/problems/counting-bits/question?list=neetcode150", null,
                                Difficulty.EASY, bit));
                questions.add(createQuestion("Reverse Bits", "https://leetcode.com/problems/reverse-bits/",
                                "https://neetcode.io/problems/reverse-bits/question?list=neetcode150", null,
                                Difficulty.EASY, bit));
                questions.add(createQuestion("Missing Number", "https://leetcode.com/problems/missing-number/",
                                "https://neetcode.io/problems/missing-number/question?list=neetcode150", null,
                                Difficulty.EASY, bit));
                questions.add(createQuestion("Sum of Two Integers",
                                "https://leetcode.com/problems/sum-of-two-integers/",
                                "https://neetcode.io/problems/sum-of-two-integers/question?list=neetcode150", null,
                                Difficulty.MEDIUM, bit));
                questions.add(createQuestion("Reverse Integer", "https://leetcode.com/problems/reverse-integer/",
                                "https://neetcode.io/problems/reverse-integer/question?list=neetcode150", null,
                                Difficulty.MEDIUM, bit));

                questionRepository.saveAll(questions);
        }

        private Question createQuestion(String title, String url, String neetcodeUrl, String description,
                        Difficulty difficulty, Topic topic) {
                Question question = Question.builder()
                                .title(title)
                                .url(url)
                                .neetcodeUrl(neetcodeUrl)
                                .description(description)
                                .difficulty(difficulty)
                                .topic(topic)
                                .sourceLists(DefaultConfigConstants.DEFAULT_QUESTION_SOURCE_LIST)
                                .build();

                Question existing = questionRepository
                                .findBySourceList(DefaultConfigConstants.DEFAULT_QUESTION_SOURCE_LIST).stream()
                                .filter(q -> q.getUrl().equals(url)).findFirst().orElse(null);

                if (existing == null) {
                        questionRepository.save(question);
                        return question;
                } else {
                        existing.setNeetcodeUrl(neetcodeUrl);
                        existing.setDescription(description);
                        questionRepository.save(existing);
                        return existing;
                }
        }
}
