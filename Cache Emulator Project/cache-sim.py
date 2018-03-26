import random
import math
from sys import argv

class RAM:

    def __init__(self):
        self.cache_size = cache_size
        self.block_size = block_size
        self.RAM_data = {}

    def populate_RAM_block(self, byte_address):
        RAM_tagIndex = Address(byte_address).getTagIndex()
        dataBlock = DataBlock()
        self.RAM_data[str(RAM_tagIndex)] = dataBlock.populate_DataBlock()

    def getBlock(self, tag_index, eviction_counter):
        # Reads in eviction counter because you can't update a tuple so needs to be updated by recalling the block and updating the count
        # Block address RAM is looking for
        target_block_address = tag_index
        target_block = self.RAM_data[str(target_block_address)]

        # Returns a tuple to the cache so we can look up the tag to confirm the block is in the cache
        return (target_block_address, target_block, eviction_counter)

    def setDouble(self, tag_index, offset_index, value):
        # Updates block when a new value is written to RAM
        target_block_address = tag_index
        target_block_offset_index = offset_index
        # Creates a copy of the original block
        block = self.RAM_data[str(target_block_address)]
        # Updates value of the correct offset index of the block
        block[str(target_block_offset_index)] = value
        # Updates the block in RAM
        self.RAM_data[str(target_block_address)] = block

class DataBlock:

    def __init__(self):
        # Number of elements(Doubles) in data block
        self.size = block_size / 8

    def populate_DataBlock(self):
        block = {}
        for i in range(self.size):
            block[str(i)] = random.randint(0,10)
        return block

class Address:

    def __init__(self, address):
        self.address = address
        self.offset_size = int(math.log(block_size,2))
        self.index_size = 0

    # Creates the binary address from the decimal
    def decimal_to_binary(self):
        binary_address_list = []
        for i in range(32):
            binary_address_list.append('0')
        address_calculation = self.address
        bit_number = 31
        while address_calculation > 0:
            binary_digit = address_calculation % 2
            binary_address_list[bit_number] = str(binary_digit)
            address_calculation = address_calculation / 2
            bit_number -= 1
        binary_address = ''.join(binary_address_list)
        return binary_address

    # Returns the offset index from the binary address for finding the right double in a block
    def getOffset(self):
        binary_address = self.decimal_to_binary()
        offset_binary_address = binary_address[(31-self.offset_size+1):]
        offset_address = 0
        for i in range(self.offset_size-1, -1, -1):
            offset_address += (2 ** i) * int(offset_binary_address[self.offset_size - 1 - i])
        return offset_address / 8

    # Returns cache block/set number that index field of binary address maps to
    def getCacheIndex(self):
        # Direct mapped
        if associativity == 0:
            num_of_blocks = cache_size / block_size
            RAM_block_address = self.getTagIndex()
            return RAM_block_address % num_of_blocks
        # Fully associative
        elif associativity == 1:
            return 0
        # N-way set associative
        else:
            num_of_sets = (cache_size / block_size) / associativity
            RAM_block_address = self.getTagIndex()
            return RAM_block_address % num_of_sets

    def getTagIndex(self):
        binary_address = self.decimal_to_binary()
        tag_index_size = 32 - self.offset_size
        tag_index_binary_address = binary_address[:32-self.offset_size]
        tag_index_address = 0
        for i in range(0, tag_index_size):
            tag_index_address += (2 ** (tag_index_size - 1 - i)) * int(tag_index_binary_address[i])
        return tag_index_address

class Cache:

    def __init__(self):
        self.num_of_blocks = cache_size / block_size
        self.read_hits = 0
        self.read_misses = 0
        self.write_hits = 0
        self.write_misses = 0
        self.cache = {}
        self.eviction_counter = 0

    def create_cache_structure(self):
        # Direct Mapped
        if associativity == 0:
            for i in range(self.num_of_blocks):
                # Creating an empty cache block with the None object
                self.cache[str(i)] = None
        # Fully Assciotive
        elif associativity == 1:
            self.cache['0'] = [None] * self.num_of_blocks
        # N-Way Assciotive
        else:
            num_of_sets = self.num_of_blocks / associativity
            for i in range(num_of_sets):
                self.cache[str(i)] = [None] * associativity
            self.num_of_blocks = associativity

    def getReadHits(self):
        return self.read_hits

    def getReadMisses(self):
        return self.read_misses

    def getWriteMisses(self):
        return self.write_misses

    def getWriteHits(self):
        return self.write_hits

    def getDouble(self, address):
        # Gathers all needed info (address of block, cache block, offset index of block needed and tag+index of address for RAM to look up)
        target_address = Address(address)
        target_cache_block = target_address.getCacheIndex()
        target_address_tagIndex = target_address.getTagIndex()
        offset = target_address.getOffset()

        # N-way or fully associative
        if associativity > 0:

            # For loop below: Checks cache block to see if the tag is there and if it is returns the block

            # Creates a reference of the cache block needed to be looked at
            cache_block_list = self.cache[str(target_cache_block)]

            # Loops through target cache block to search for tag+index
            for i in range(self.num_of_blocks):
                # Only performs action on block if the cache block tag+index matches with the address tag+index
                if cache_block_list[i] is not None:
                    if target_address_tagIndex == cache_block_list[i][0]:

                        self.read_hits += 1

                        # Only incriments eviction counter if LRU is selected because this value isn't used in FIFO
                        if eviction_method == 0:
                            # Updates blocks eviction counter, calls the RAM getBlock method because you can't update a tuple
                            # so because the RAM getBlock method takes in the eviction count, it is the psuedo way to update the eviction
                            # counter in the cache, NOTE: the RAM does not store eviction counter, only lets it pass through function as way
                            # of updating eviction counter in cache
                            temp_block = myRAM.getBlock(target_address_tagIndex, self.eviction_counter)
                            self.cache[str(target_cache_block)][i] = temp_block
                            self.eviction_counter += 1

                        # Returns the data value desired
                        return cache_block_list[i][1][str(offset)]

            # This happens if it is a read miss
            self.read_misses += 1

            # Read block needed from RAM
            block = myRAM.getBlock(target_address_tagIndex, self.eviction_counter)
            self.eviction_counter += 1

            # Writes block from RAM to block in cache assuming eviction is not needed, otherwise a flag is activated
            for i in range(self.num_of_blocks):
                eviction_flag = True
                if self.cache[str(target_cache_block)][i] == None:
                    eviction_flag = False
                    self.cache[str(target_cache_block)][i] = block
                    # Returns value after logging read miss without logging another read hit
                    return block[1][str(offset)]

            # Only activated if the block is full and a block needs to be evicted
            if eviction_flag:

                # LRU or FIFO because eviction_method == 0 if statement only increiments eviction counter when block is used when LRU is set
                if eviction_method == 0 or eviction_method == 1:
                    evicted_block_counter = float("inf")
                    # Loops through every block in the set and selects the LRU block (by lowest eviction counter)
                    # and replaces it with current block being read in from RAM
                    for i in range(self.num_of_blocks):
                        if self.cache[str(target_cache_block)][i][2] < evicted_block_counter:
                            evicted_block_counter = self.cache[str(target_cache_block)][i][2]
                            # The index in the list of the block that is going to be evicted
                            evicted_block_index = i
                    self.cache[str(target_cache_block)][evicted_block_index] = block
                    # Returns value after logging read miss without logging another read hit
                    return block[1][str(offset)]

                # Random
                if eviction_method == 2:
                    # Randomly selects block to evict
                    evicted_block_index = random.randint(0, self.num_of_blocks-1)
                    self.cache[str(target_cache_block)][evicted_block_index] = block
                    # Returns value after logging read miss without logging another read hit
                    return block[1][str(offset)]

        # Direct mapped
        else:
            # Reference of target cache block
            cache_block = self.cache[str(target_cache_block)]
            # If cache block isn't empty, checks to see if tag+index matches to check if block is in cache and returns value
            if cache_block is not None:
                if target_address_tagIndex == cache_block[0]:
                    self.read_hits += 1
                    return cache_block[1][str(offset)]
            # If not, grabs the block from RAM, eviction counter always stays 0 and never used in direct mapped
            self.read_misses += 1
            block = myRAM.getBlock(target_address_tagIndex, self.eviction_counter)
            self.cache[str(target_cache_block)] = block
            return self.cache[str(target_cache_block)][1][str(offset)]

    def setDouble(self, address, value):
        # Gathers all needed info (address of block, cache block, offset index of block needed and tag+index of address for RAM to look up)
        # As well as the value that will be updated
        target_address = Address(address)
        target_cache_block = target_address.getCacheIndex()
        target_address_tagIndex = target_address.getTagIndex()
        offset = target_address.getOffset()

        # Fully or n-way set associative
        if associativity > 0:
            # Reference of cache block
            cache_block_list = self.cache[str(target_cache_block)]

            # Loops through target cache block to search for tag+index
            for i in range(self.num_of_blocks):
                # Only performs action on block if the cache block tag+index matches with the address tag+index
                if cache_block_list[i] is not None:
                    # Write hit
                    if target_address_tagIndex == cache_block_list[i][0]:
                        self.write_hits += 1
                        # Only incriments eviction counter if LRU is selected because this value isn't used in FIFO
                        if eviction_method == 0:
                            # Updates blocks eviction counter, calls the RAM getBlock method because you can't update a tuple
                            # so because the RAM getBlock method takes in the eviction count, it is the psuedo way to update the eviction
                            # counter in the cache, NOTE: the RAM does not store eviction counter, only lets it pass through function as way
                            # of updating eviction counter in cache
                            temp_block = myRAM.getBlock(target_address_tagIndex, self.eviction_counter)
                            self.cache[str(target_cache_block)][i] = temp_block
                            self.eviction_counter += 1

                        # Writes new value to cache
                        self.cache[str(target_cache_block)][i][1][str(offset)] = value
                        # Writes new value to RAM (write through policy)
                        myRAM.setDouble(target_address_tagIndex, offset, value)
                        return

            # Write miss
            self.write_misses += 1
            # Read block needed from RAM
            block = myRAM.getBlock(target_address_tagIndex, self.eviction_counter)
            self.eviction_counter += 1

            # Writes block from RAM to block in cache assuming eviction is not needed, otherwise a flag is activated
            for i in range(self.num_of_blocks):
                eviction_flag = True
                if self.cache[str(target_cache_block)][i] == None:
                    eviction_flag = False
                    # Writes block fetched from RAM to cache
                    self.cache[str(target_cache_block)][i] = block
                    # Writes updated value to cache
                    self.cache[str(target_cache_block)][i][1][str(offset)] = value
                    # Writes new value to RAM (write through policy)
                    myRAM.setDouble(target_address_tagIndex, offset, value)
                    return

            # Only activated if the block is full and a block needs to be evicted
            if eviction_flag:

                # LRU or FIFO because eviction_method == 0 if statement only increiments eviction counter when block is used when LRU is set
                if eviction_method == 0 or eviction_method == 1:
                    evicted_block_counter = float("inf")
                    # Loops through every block in the set and selects the LRU block (by lowest eviction counter)
                    # and replaces it with current block being read in from RAM
                    for i in range(self.num_of_blocks):
                        if self.cache[str(target_cache_block)][i][2] < evicted_block_counter:
                            evicted_block_counter = self.cache[str(target_cache_block)][i][2]
                            # The index in the list of the block that is going to be evicted
                            evicted_block_index = i
                    # Writes block from RAM to evcited block spot
                    self.cache[str(target_cache_block)][evicted_block_index] = block
                    # Writes updated value to both cache and RAM
                    self.cache[str(target_cache_block)][evicted_block_index][1][str(offset)] = value
                    myRAM.setDouble(target_address_tagIndex, offset, value)

                # Random
                if eviction_method == 2:
                    # Randomly selects block to evict
                    evicted_block_index = random.randint(0, self.num_of_blocks-1)
                    # Writes block from RAM to evcited block spot
                    self.cache[str(target_cache_block)][evicted_block_index] = block
                    # Writes updated value to both cache and RAM
                    self.cache[str(target_cache_block)][evicted_block_index][1][str(offset)] = value
                    myRAM.setDouble(target_address_tagIndex, offset, value)

        # Direct mapped
        else:
            # Reference of target cache block
            cache_block = self.cache[str(target_cache_block)]
            # If cache block isn't empty, checks to see if tag+index matches to check if block is in cache and returns value
            if cache_block is not None:
                if target_address_tagIndex == cache_block[0]:
                    self.write_hits += 1
                    # Writes new value to cache
                    self.cache[str(target_cache_block)][1][str(offset)] = value
                    # Writes new value to RAM (write through policy)
                    myRAM.setDouble(target_address_tagIndex, offset, value)
                    return
            # If not, grabs the block from RAM, eviction counter always stays 0 and never used in direct mapped
            self.write_misses += 1
            block = myRAM.getBlock(target_address_tagIndex, self.eviction_counter)
            self.cache[str(target_cache_block)] = block
            # Writes new value to cache
            self.cache[str(target_cache_block)][1][str(offset)] = value
            # Writes new value to RAM (write through policy)
            myRAM.setDouble(target_address_tagIndex, offset, value)

    def printCache(self):
        print self.cache

class CPU:

    def __init__(self):
        self.instruction_count = 0

    def loadDouble(self, address):
        self.instruction_count += 1
        return myCache.getDouble(address)

    def storeDouble(self, address, value):
        self.instruction_count += 1
        myCache.setDouble(address, value)

    def addDouble(self, value1, value2):
        self.instruction_count += 1
        return value1 + value2

    def multiDouble(self, value1, value2):
        self.instruction_count += 1
        return value1 * value2

    def printMetrics(self):
        print "Instruction Count: " + str(self.instruction_count)
        print "Read Hits: " + str(myCache.getReadHits())
        print "Read Misses: " + str(myCache.getReadMisses())
        print "Write Hits: " + str(myCache.getWriteHits())
        print "Write Misses: " + str(myCache.getWriteMisses())
        print "Hit Ratio: " + str(((myCache.getReadHits() + myCache.getWriteHits()) / 1.0) / (myCache.getReadHits() + myCache.getWriteHits() + myCache.getReadMisses() + myCache.getWriteMisses()))

def RAM_Cache_Set_Up():
    # Inializes RAM and Cache for CPU to use
    global myRAM
    myRAM = RAM()

    global myCache
    myCache = Cache()
    myCache.create_cache_structure()

def populate_RAM_block(byte_address):
    myRAM.populate_RAM_block(byte_address)

def getopts(argv):
    # Creates dictionary of commandline tags for variables
    opts = {}
    while argv:
        if argv[0][0] == '-':
            opts[argv[0]] = argv[1]
        argv = argv[1:]
    return opts

if __name__ == '__main__':

    # Default values for command line options
    global cache_size
    cache_size = 2 ** 16
    global block_size
    block_size = 2 ** 8
    global associativity
    associativity = 2      # 2 or higher = n way, 1 = fully assciotive, 0 = direct mapped
    global eviction_method
    eviction_method = 0     # 0 = LRU, 1 = FIFO, 2 = Random
    global algorithm
    algorithm = 0           # 0 = mxm matrix, 1 = dot product, 2 = mxm with blocking

    # Sets variables parsed from the command line
    myargs = getopts(argv)
    if myargs.has_key('-c'):
        cache_size = int(myargs['-c'])
    if myargs.has_key('-b'):
        block_size = int(myargs['-b'])
    if myargs.has_key('-n'):
        # I had wrote 1 in my program to be fully assciotive and 0 is direct mapped
        if myargs['-n'] == '1':
            associativity = 0
        elif myargs['-n'] == '0':
            associativity = 1
        else:
            associativity = int(myargs['-n'])
    if myargs.has_key('-r'):
        replacement = myargs['-r']
        if replacement == "random":
            eviction_method = 2
        if replacement == "LRU":
            eviction_method = 0
        if replacement == "FIFO":
            eviction_method = 1
    if myargs.has_key('-a'):
        alg = myargs['-a']
        if alg == "dot":
            algorithm = 1
        if alg == "mxm":
            algorithm = 0
        if alg == "mxm_block":
            algorithm = 2

    RAM_Cache_Set_Up()
    myCPU = CPU()

    # Dot Product
    if algorithm == 1:

        a = []
        b = []
        c = []

        array_size = (2 ** 8) * (cache_size)

        for i in range(array_size*2):
            a_address = i * 8
            b_address = i * 8 + 19384       # Random number to make sure addresses aren't close in RAM
            c_address = i * 8 + 2 * 19384
            a.append(a_address)
            b.append(b_address)
            c.append(c_address)
            populate_RAM_block(a_address)
            populate_RAM_block(b_address)
            populate_RAM_block(c_address)

        for i in range(array_size):
            register1 = myCPU.loadDouble(a[i])
            register2 = myCPU.loadDouble(b[i])
            register3 = myCPU.addDouble(register1, register2)
            myCPU.storeDouble(c[i], register3)

        register5 = 0
        for i in range(array_size):
            register4 = myCPU.loadDouble(c[i])
            register5 = myCPU.addDouble(register5, register4)

        myCPU.printMetrics()

    # mxm matrix
    if algorithm == 0:

        matrix_size = (2**7) * (cache_size)
        num_of_rows_columns_float = (matrix_size ** 0.5)
        num_of_rows_columns = int(num_of_rows_columns_float) # Doesnt matter if it is row or columns

        a = [] # Assume a is 1-d array in column format
        b = [] # Assume b is a 1-d array in column format
        c = [] # c will be 1-d array in column format

        # Creates address and then creates a RAM data value for them
        for i in range(matrix_size*2):
            a_address = i * 8
            b_address = i * 8 + 19384       # Random number to make sure addresses aren't close in RAM
            c_address = i * 8 + 2 * 19384
            a.append(a_address)
            b.append(b_address)
            c.append(c_address)
            populate_RAM_block(a_address)
            populate_RAM_block(b_address)
            populate_RAM_block(c_address)

        for i in range(num_of_rows_columns):
            for j in range(num_of_rows_columns):
                register4 = 0
                for k in range(num_of_rows_columns):
                    register1 = myCPU.loadDouble(a[i + k * num_of_rows_columns])
                    register2 = myCPU.loadDouble(b[j * num_of_rows_columns + k])
                    register3 = myCPU.multiDouble(register1, register2)
                    register4 = myCPU.addDouble(register3, register4)
                myCPU.storeDouble(c[i + j * num_of_rows_columns], register4)

        myCPU.printMetrics()

    # mxm with blocking
    if algorithm == 2:

        matrix_size = (2**7) * (cache_size)
        num_of_rows_columns_float = (matrix_size ** 0.5)
        num_of_rows_columns = int(num_of_rows_columns_float) # Doesnt matter if it is row or columns

        a = [] # Assume a is 1-d array in column format
        b = [] # Assume b is a 1-d array in column format
        c = [] # c will be 1-d array in

        # Creates address and then creates a RAM data value for them
        for i in range(matrix_size*2):
            a_address = i * 8
            b_address = i * 8 + 19384       # Random number to make sure addresses aren't close in RAM
            c_address = i * 8 + 2 * 19384
            a.append(a_address)
            b.append(b_address)
            c.append(c_address)
            populate_RAM_block(a_address)
            populate_RAM_block(b_address)
            populate_RAM_block(c_address)

        a = [a[i:i+num_of_rows_columns] for i in range(0,len(a),num_of_rows_columns)]
        b = [b[i:i+num_of_rows_columns] for i in range(0,len(b),num_of_rows_columns)]
        c = [c[i:i+num_of_rows_columns] for i in range(0,len(c),num_of_rows_columns)]

        for si in range(0, num_of_rows_columns, block_size):
            for sj in range(0, num_of_rows_columns, block_size):
                for sk in range(0, num_of_rows_columns, block_size):
                    for i in range(si, min(si+block_size, num_of_rows_columns)):
                        for j in range(sj, min(sj+block_size, num_of_rows_columns)):
                            register4 = 0
                            for k in range(sk, min(sk+block_size, num_of_rows_columns)):
                                register1 = myCPU.loadDouble(a[i][k])
                                register2 = myCPU.loadDouble(b[k][j])
                                register3 = myCPU.multiDouble(register1, register2)
                                register4 = myCPU.addDouble(register4, register3)
                            myCPU.storeDouble(c[i][j], register4)

        myCPU.printMetrics()
