# Portfolio
# ––––––––––
Currently, the following portfolio contains one solution from my coursework (MyLZW.java) and one side project I had undertaken
last year.
# ––––––––––
# MyLZW.java
# ––––––––––
The key functionality of LZW compression is to transform stretches of bits in a file into code words of a certain 
(hopefully shorter) bit length, and store those code words in a codebook.  We then use those codewords to map subsequent 
equal bit strings to the same shorter bitstring.  

This was the end result of one of the more complicated projects I had been assigned during my coursework.  The objective was 
to modify a provided source file which handled LZW compression and expansion to support both a variable length codeword 
(anywhere from 8 to 16 bits) and three paradigms by which to handle the codebook.  

An incredible amount of observation and debugging was necessary to produce the desired compression, and to be able to reverse 
that compression and reproduce the original file.  Perhaps the most interesting challenge about this project was the need to 
perform specific math in a very specific order, and then determining how exactly how to reverse those instructions in order to 
expand the compressed file.  
# ––––––––––
# game
# ––––––––––
This was an early attempt at creating a general AI capable of both understanding "sensory" input, reacting based on that 
understanding, and communicating that understanding.  This implementation sought to use Java's Javascript engine to construct 
patterned, increasingly complex js files which mapped a word to both the actor's concept of that word (relevance, associated 
memories, etc.) and a specific action which was constructed from more fundamental actions.  

These "fundamental" actions are represented by bend.js and extend.js, the insight here being that any human action, no matter 
how complex, is nothing more than a certain combination of muscles relaxing and contracting in order.  I thought that if we 
can generalize these basic actions to almost any task, an AI could be trained to do the same.  

The primary issue with this implementation is that I had attempted to treat several distinct tasks (understanding, language, and motor program generation) as one, and the end result became convoluted.  From here, I began to study machine learning in order to clarify this problem.  
