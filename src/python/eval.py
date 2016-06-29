from nltk.corpus import wordnet as wn

def synonyms(word, lch_threshold=2.26):
  for net1 in wn.synsets(word):
    for net2 in wn.all_synsets():
      try:
        lch = net1.lch_similarity(net2)
      except:
        continue
      if lch >= lch_threshold:
        yield (net1, net2, lch)



'''
word = wn.synset("black.a.01") 

print "Hypernyms:"
print word.hypernyms()

print "\nHyponyms:"
print word.hyponyms()

print "\n  Lemmas:"
print "  ",word.lemmas(),":"
for lemma in word.lemmas():
  print "    Antonyms:"
  print "    ",lemma.antonyms()

#'''


print "\n\nAll:"
synsets = wn.synsets("magazine") 
for synset in synsets:
  print "\n",synset
  print "  Hypernyms:"
  print "  ",synset.hypernyms()

  print "  Hyponyms:"
  print "  ",synset.hyponyms()

#for word in synonyms("dog"):
#  print word
# '''
