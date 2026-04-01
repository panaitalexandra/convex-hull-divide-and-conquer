# convex-hull-divide-and-conquer
# Convex Hull Determination (Divide and Conquer)

## 1. Requirement

Given a set of **N** distinct points in a 2D plane, the objective is to compute the **Convex Hull (CH)** using the **Divide and Conquer** paradigm. 

The algorithm recursively splits the point set into subsets of approximately equal size, computes their individual hulls, and merges them efficiently to form the final convex polygon.

---

## 2. Methodology

The algorithm follows the classic recursive structure:

### Phase I: Divide
* **Sorting:** Points are initially sorted by their **X-coordinates** to facilitate easy splitting.
* **Splitting:** The set $S$ is divided into $S_1$ and $S_2$ such that $S = S_1 \cup S_2$ and $S_1 \cap S_2 = \emptyset$.

### Phase II: Conquer (Recursive Step)
* The algorithm recursively computes $CH(S_1)$ and $CH(S_2)$.
* **Base Case:** For small subsets (e.g., $n \le 3$), a simple Graham Scan or direct construction is applied.

### Phase III: Combine (The Merge Step)
This is the most critical part of the algorithm, performed in $O(n)$ time. A reference point **O** is chosen inside $CH(S_1)$, and its position relative to $CH(S_2)$ is checked using the **Wedge Method** (logarithmic point-in-convex-polygon test).



#### Case A: Point O is INSIDE $CH(S_2)$
If $O$ is internal to both hulls, both lists of vertices ($L_1$ and $L_2$) are already angularly sorted around $O$. 
1. **Interleaving:** Merge the two sorted lists of vertices.
2. **Refining:** Apply a single Graham-style scan to eliminate any new concavities.

#### Case B: Point O is OUTSIDE $CH(S_2)$
If $O$ is external to $CH(S_2)$, the algorithm identifies the two **tangent lines** from $O$ to $CH(S_2)$.
1. **Tangent Identification:** Find the vertices in $CH(S_2)$ that define the tangents from $O$.
2. **Merging:** Interleave the points from the visible and invisible arcs and process them to obtain the final merged hull.

---

## 3. Position Verification (Wedge Method)

The localization of point **O** relative to $CH(S_2)$ is based on the determinant test against the edges of the polygon:

| Position of Point O | Algorithm Action |
| :--- | :--- |
| **O ∈ Interior(CH(S₂))** | Direct merging of $L_1, L_2$ + Graham Scan |
| **O ∉ Interior(CH(S₂))** | Find tangents from O to $CH(S_2)$ + Merge |

---

## 4. Complexity
* **Time Complexity:** $O(N \log N)$ total. The recurrence relation is $T(n) = 2T(n/2) + O(n)$.
* **Space Complexity:** $O(N)$ to store the points and the recursive stack.
