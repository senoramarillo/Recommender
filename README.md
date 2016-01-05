# Recommender Systems

1. Implement a simple collaborative filtering recommender system using the algorithm
described in the lecture. As user similarity metric use the Pearson correlation coefficient
which can be found in the following paper (page 12, eq 5).

2. Test your implementation using the MovieLens data set consisting of 100K user ratings,
you can download it from here . Information about the structure of the data set can be
found here . Train your recommender with the u1.base data set.

3. Examine the impact of different neighbourhoods sizes (5-200 neighbours) on the quality
of the recommendations. For this use the u1.test data set and plot the quality of the
recommendations using the “MAE” metric (see Exercise slides). Discuss the shape of the
resulting function plot.

4. Implement a simple web interface which allows the user to browse and search the data.
Furthermore, the user should be able to rate the movies to generate a personal preference
profile. Based on this profile the user should be presented with recommendations consisting
of 20 other movies he might be interested in.
