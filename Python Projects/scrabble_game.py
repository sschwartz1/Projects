import itertools
import re

def letter_input():
    """ Prompts the user to enter in their tiles, must be letters or it will reprompt"""
    while True:
        tiles = raw_input("Please enter the letters in your rack: ").lower()
        if re.search(r'[^a-zA-Z \s ,]', tiles):
            print "All tiles must be letters! \n"
            continue
        else:
            letters = re.findall(r'[a-zA-Z]', tiles)
            return letters
            break


def possible_words(letters):
    """Iterates through all combinations and permutations of inputed list of letters and creates list of all possible words"""
    possible_words = []
    s = 0
    n = len(letters)
    while s < len(letters):
        for c in itertools.combinations(letters, n):
            for p in itertools.permutations(c):
                if p not in possible_words:
                    possible_words.append(''.join(p))
        s += 1
        n -= 1
    return possible_words


def allowable_words():
    """Reads in file of scrabble allowable words and puts them into a list"""
    allowable_words = []
    scrabble_words = open('scrabble_words.txt', 'r')
    for line in scrabble_words:
        clean_line = line.strip().strip('\n').lower()
        allowable_words.append(clean_line)
    scrabble_words.close()
    return allowable_words


def scoring(possible_words, allowable_words):
    """Takes input lists of both possible words and allowable words and return a dictionary of possible words with their scores"""
    tile_score = {"a": 1, "c": 3, "b": 3, "e": 1, "d": 2, "g": 2,
                 "f": 4, "i": 1, "h": 4, "k": 5, "j": 8, "m": 3,
                 "l": 1, "o": 1, "n": 1, "q": 10, "p": 3, "s": 1,
                 "r": 1, "u": 1, "t": 1, "w": 4, "v": 4, "y": 4,
                 "x": 8, "z": 10}
    count = {}
    #Iterates through every word in possible_words to see if it is a scrabble word, and if it is then it scores the word and puts it in the dictionary count
    for i in range(0, len(possible_words)):
        # Prevent overcounting of words so scores aren't larger than they should be
        if possible_words[i] in allowable_words and possible_words[i] not in count.keys():
            for letter in range(0, len(possible_words[i])):
                if possible_words[i][letter] in tile_score.keys():
                    count[possible_words[i]] = count.get(possible_words[i], 0) + tile_score[possible_words[i][letter]]
    return count


def sort(count):
    """Takes a dictionary of words and score values and prints them out in order from highest to lowest score"""
    # Creates a list of tuples (values, keys) that will be used to sort and print scrabble word options
    count_list = []
    for keys, values in count.items():
        count_list.append((values, keys))
    # Sorts list from highest to lowest word score and then prints out the options
    count_list.sort()
    count_list.reverse()
    if len(count_list) == 0:
        print "Sorry you can't make any words :("
    for values, keys in count_list:
        print "You get %d points for %s" % (values, keys)

def scrabble_word_options():
    """Will print all possible scrabble words with point scores from current tiles"""
    t = letter_input()
    a = allowable_words()
    p = possible_words(t)
    c = scoring(p, a)
    sort(c)

def main():
    scrabble_word_options()


if __name__ == '__main__':
    main()
