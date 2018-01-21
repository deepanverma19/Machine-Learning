##USING THE FOLLOWING LIBRARIES

library("jpeg")
library("ggplot2")

##READING THE IMAGE SOURCE USING READJPEG FUNCTION
image1 <- readJPEG("C:/Users/dverma/Desktop/dxv160430_Assignment6/Part 3/image1.jpg")
##CALCULATING THE DIMENSIONS OF IMAGE
imageDim1 <- dim(image1)
image1
##SETTING THE IMAGERGB USING THE FOLLOWWING VALUES
imageRGB <- data.frame(
  x = rep(1:imageDim1[2], each = imageDim1[1]),
  y = rep(imageDim1[1]:1, imageDim1[2]),
  R = as.vector(image1[,,1]),
  G = as.vector(image1[,,2]),
  B = as.vector(image1[,,3])
)

##CREATING A FUNCTION CALLED THAT PERFORMS VARIOUS TASKS AND STROING IT INTO VARIABLE CALLED PLOTFUNCTION
plotFunction <- function() {
  theme(
    panel.background = element_rect(
      size = 3,
      colour = "black",
      fill = "white"),
    axis.ticks = element_line(
      size = 2),
    panel.grid.major = element_line(
      colour = "gray80",
      linetype = "dotted"),
    panel.grid.minor = element_line(
      colour = "gray90",
      linetype = "dashed"),
    axis.title.x = element_text(
      size = rel(1.2),
      face = "bold"),
    axis.title.y = element_text(
      size = rel(1.2),
      face = "bold"),
    plot.title = element_text(
      size = 20,
      face = "bold",
      vjust = 1.5)
  )
}

##USING GGPLOT FUNCTION TO CREATE THE CLUSTERED IMAGE
ggplot(data = imageRGB, aes(x = x, y = y)) + 
  geom_point(colour = rgb(imageRGB[c("R", "G", "B")])) +
  labs(title = "Original Image") +
  xlab("x") +
  ylab("y") +
  plotFunction()


##SETTING THE NUMBER OF CLUSTERS AS 5 FOR IMAGE 1
noOfClusters <- 5
##APPLYING THE KMEANS ALGO
kMeans <- kmeans(imageRGB[, c("R", "G", "B")], centers = noOfClusters)
##USING THE RGB COLOURS FOR CLUSTERED IMAGE COLOR
kColours <- rgb(kMeans$centers[kMeans$cluster,])

##GGPLOT FUNCTION FOR CLUSTERED IMAGE
gp=ggplot(data = imageRGB, aes(x = x, y = y)) + 
  geom_point(colour = kColours) +
  labs(title = paste("k-Means Clustering of", noOfClusters, "Colours")) +
  xlab("x") +
  ylab("y") + 
  plotFunction()

##SAVING THE CLUSTERED IMAGE AT THE MENTIONED PATH
ggsave(gp, file = "C:/Users/dverma/Desktop/dxv160430_Assignment6/Part 3/clusteredimage1.jpg")
#############################################################################################

##READING THE IMAGE SOURCE USING READJPEG FUNCTION
image3 <- readJPEG("C:/Users/dverma/Desktop/dxv160430_Assignment6/Part 3/image3.jpg")

##CALCULATING THE DIMENSIONS OF IMAGE
imageDim3 <- dim(image3)
image3

##SETTING THE IMAGERGB USING THE FOLLOWWING VALUES
imageRGB <- data.frame(
  x = rep(1:imageDim3[2], each = imageDim3[1]),
  y = rep(imageDim3[1]:1, imageDim3[2]),
  R = as.vector(image3[,,1]),
  G = as.vector(image3[,,2]),
  B = as.vector(image3[,,3])
)

##CREATING A FUNCTION CALLED THAT PERFORMS VARIOUS TASKS AND STROING IT INTO VARIABLE CALLED PLOTFUNCTION
plotFunction <- function() {
  theme(
    panel.background = element_rect(
      size = 3,
      colour = "black",
      fill = "white"),
    axis.ticks = element_line(
      size = 2),
    panel.grid.major = element_line(
      colour = "gray80",
      linetype = "dotted"),
    panel.grid.minor = element_line(
      colour = "gray90",
      linetype = "dashed"),
    axis.title.x = element_text(
      size = rel(1.2),
      face = "bold"),
    axis.title.y = element_text(
      size = rel(1.2),
      face = "bold"),
    plot.title = element_text(
      size = 20,
      face = "bold",
      vjust = 1.5)
  )
}

##USING GGPLOT FUNCTION TO CREATE THE CLUSTERED IMAGE
ggplot(data = imageRGB, aes(x = x, y = y)) + 
  geom_point(colour = rgb(imageRGB[c("R", "G", "B")])) +
  labs(title = "Original Image") +
  xlab("x") +
  ylab("y") +
  plotFunction()


##SETTING THE NUMBER OF CLUSTERS AS 10 FOR IMAGE 3
noOfClusters <- 10
##APPLYING THE KMEANS ALGO
kMeans <- kmeans(imageRGB[, c("R", "G", "B")], centers = noOfClusters)
##USING THE RGB COLOURS FOR CLUSTERED IMAGE COLOR
kColours <- rgb(kMeans$centers[kMeans$cluster,])

##GGPLOT FUNCTION FOR CLUSTERED IMAGE
gp=ggplot(data = imageRGB, aes(x = x, y = y)) + 
  geom_point(colour = kColours) +
  labs(title = paste("k-Means Clustering of", noOfClusters, "Colours")) +
  xlab("x") +
  ylab("y") + 
  plotFunction()

##SAVING THE CLUSTERED IMAGE AT THE MENTIONED PATH
ggsave(gp, file = "C:/Users/dverma/Desktop/dxv160430_Assignment6/Part 3/clusteredimage3.jpg")
##############################################################################################

##READING THE IMAGE SOURCE USING READJPEG FUNCTION
image5 <- readJPEG("C:/Users/dverma/Desktop/dxv160430_Assignment6/Part 3/image5.jpg")
##CALCULATING THE DIMENSIONS OF IMAGE
imageDim5 <- dim(image5)
image5
##SETTING THE IMAGERGB USING THE FOLLOWWING VALUES
imageRGB <- data.frame(
  x = rep(1:imageDim5[2], each = imageDim5[1]),
  y = rep(imageDim5[1]:1, imageDim5[2]),
  R = as.vector(image5[,,1]),
  G = as.vector(image5[,,2]),
  B = as.vector(image5[,,3])
)

##CREATING A FUNCTION CALLED THAT PERFORMS VARIOUS TASKS AND STROING IT INTO VARIABLE CALLED PLOTFUNCTION
plotFunction <- function() {
  theme(
    panel.background = element_rect(
      size = 3,
      colour = "black",
      fill = "white"),
    axis.ticks = element_line(
      size = 2),
    panel.grid.major = element_line(
      colour = "gray80",
      linetype = "dotted"),
    panel.grid.minor = element_line(
      colour = "gray90",
      linetype = "dashed"),
    axis.title.x = element_text(
      size = rel(1.2),
      face = "bold"),
    axis.title.y = element_text(
      size = rel(1.2),
      face = "bold"),
    plot.title = element_text(
      size = 20,
      face = "bold",
      vjust = 1.5)
  )
}

##USING GGPLOT FUNCTION TO CREATE THE CLUSTERED IMAGE
ggplot(data = imageRGB, aes(x = x, y = y)) + 
  geom_point(colour = rgb(imageRGB[c("R", "G", "B")])) +
  labs(title = "Original Image") +
  xlab("x") +
  ylab("y") +
  plotFunction()

##SETTING THE NUMBER OF CLUSTERS AS 3 FOR IMAGE 5
noOfClusters <- 3
##APPLYING THE KMEANS ALGO
kMeans <- kmeans(imageRGB[, c("R", "G", "B")], centers = noOfClusters)
##USING THE RGB COLOURS FOR CLUSTERED IMAGE COLOR
kColours <- rgb(kMeans$centers[kMeans$cluster,])

##GGPLOT FUNCTION FOR CLUSTERED IMAGE
gp=ggplot(data = imageRGB, aes(x = x, y = y)) + 
  geom_point(colour = kColours) +
  labs(title = paste("k-Means Clustering of", noOfClusters, "Colours")) +
  xlab("x") +
  ylab("y") + 
  plotFunction()

##SAVING THE CLUSTERED IMAGE AT THE MENTIONED PATH
ggsave(gp, file = "C:/Users/dverma/Desktop/dxv160430_Assignment6/Part 3/clusteredimage5.jpg")
#############################################################################################