import matplotlib.pyplot as plt
import numpy as np
import matplotlib.image as mpimg

# Create a simple illustration of a kitten
def draw_kitten():
    fig, ax = plt.subplots()

    # Draw head
    head = plt.Circle((0.5, 0.5), 0.3, color='orange', ec='black')
    ax.add_artist(head)

    # Draw eyes
    left_eye = plt.Circle((0.4, 0.6), 0.05, color='white', ec='black')
    right_eye = plt.Circle((0.6, 0.6), 0.05, color='white', ec='black')
    ax.add_artist(left_eye)
    ax.add_artist(right_eye)

    # Draw pupils
    left_pupil = plt.Circle((0.4, 0.6), 0.02, color='black')
    right_pupil = plt.Circle((0.6, 0.6), 0.02, color='black')
    ax.add_artist(left_pupil)
    ax.add_artist(right_pupil)

    # Draw nose
    nose = plt.Polygon([[0.5, 0.5], [0.48, 0.45], [0.52, 0.45]], color='pink')
    ax.add_artist(nose)

    # Draw mouth
    mouth_left = plt.Line2D([0.48, 0.45], [0.45, 0.42], color='black')
    mouth_right = plt.Line2D([0.52, 0.55], [0.45, 0.42], color='black')
    ax.add_line(mouth_left)
    ax.add_line(mouth_right)

    # Draw ears
    left_ear = plt.Polygon([[0.35, 0.75], [0.4, 0.6], [0.45, 0.75]], color='orange', ec='black')
    right_ear = plt.Polygon([[0.55, 0.75], [0.6, 0.6], [0.65, 0.75]], color='orange', ec='black')
    ax.add_artist(left_ear)
    ax.add_artist(right_ear)

    # Draw whiskers
    whisker_left1 = plt.Line2D([0.3, 0.45], [0.55, 0.5], color='black')
    whisker_left2 = plt.Line2D([0.3, 0.45], [0.45, 0.4], color='black')
    whisker_right1 = plt.Line2D([0.7, 0.55], [0.55, 0.5], color='black')
    whisker_right2 = plt.Line2D([0.7, 0.55], [0.45, 0.4], color='black')
    ax.add_line(whisker_left1)
    ax.add_line(whisker_left2)
    ax.add_line(whisker_right1)
    ax.add_line(whisker_right2)

    # Set limits and hide axes
    ax.set_xlim(0, 1)
    ax.set_ylim(0, 1)
    ax.set_aspect('equal')
    plt.axis('off')

    plt.show()

draw_kitten()
