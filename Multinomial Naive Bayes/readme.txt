The folder consists of two python files:

It uses various packages:
from __future__ import division
from collections import Counter
from collections import defaultdict
from stop_words import get_stop_words
import sys
import os
import string as st
import re
import datetime
import time
import math
import numpy as np

1) NaiveBayes.py
--------------------
This python file reads the path of the folder of trainingDataset at arg[1] and the path of the folder of the testDataset at arg[2]. This calculates
the accuracy of the model on the testDataset.

For 5 datasets:

The accuracy of the model is : 78.020

Assumptions:
1) Tokenized the words by using the regular expression.
2) Used stop_words package to consider the stop_words. and exclude it from the datasets.