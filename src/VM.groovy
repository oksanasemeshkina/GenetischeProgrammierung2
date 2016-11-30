/**
 * Created by Oksana on 10.11.2016.
 */
public enum befehl {
    LOAD((byte) 0), // Reg = #1234
    PUSH((byte) 1), // push(Reg)
    POP((byte) 2), // Reg = pop()
    MUL((byte) 3), // Reg = register*pop()
    DIV((byte) 4), // Reg = Reg/pop()
    ADD((byte) 5), // Reg = Reg+pop()
    SUB((byte) 6), // Reg = Reg-pop()
    JIH((byte) 7) // if Reg>0 then programCounter = programCounter + pop()

    def wert

    private befehl(byte wert) {
        this.wert = wert;
    }
}

public class VM {
    static final int MAX = 1000
    static final int MEM_WERTE = 10 << 3
    static final int MIN_SIZE_OF_THE_PRIME = 0
    static final int MAX_OPERATIONS_PER_VM_SIMULATION = 1000

    ArrayList<Integer> memory = new ArrayList<>()
    ArrayList<Integer> stack = new ArrayList<>()
    ArrayList<Integer> primeNumbers = new ArrayList<>()
    int programCounter, stackPointer, register
    Random random = new Random()

    public VM() {
        reset()
        fillMemoryRandom()
    }

    void push(int x) {
        if (stackPointer >= 0 && stackPointer < MAX) {
            stack[stackPointer++] = x;
            addIfPrimeToPrimeNumbers(x)
        }
    }

    int pop() {
        if (stackPointer >= 1)
            stackPointer--
        return stack[stackPointer]
    }

    public void simulate() {
        int pop
        int counter = 0;
        while (programCounter < MAX && programCounter >= 0 && counter < MAX_OPERATIONS_PER_VM_SIMULATION && stackPointer >= 0) {
            counter++;
            try {
                switch (memory[programCounter] & 7) {
                    case befehl.LOAD.wert:
                        register = memory[programCounter] >> 3;
                        programCounter++
                        break
                    case befehl.PUSH.wert:
                        push(register);
                        programCounter++
                        break
                    case befehl.POP.wert:
                        register = this.pop()
                        programCounter++
                        break
                    case befehl.MUL.wert:
                        pop = this.pop();
                        register = register * pop;
                        push(register);
                        programCounter++
                        break
                    case befehl.DIV.wert:
                        pop = this.pop();
                        if (pop != 0) {
                            register = register / pop;
                        }
                        push(register);
                        programCounter++
                        break
                    case befehl.ADD.wert:
                        pop = this.pop();
                        register = register + pop;
                        push(register);
                        programCounter++
                        break
                    case befehl.SUB.wert:
                        pop = this.pop();
                        register = register - pop;
                        push(register);
                        programCounter++
                        break
                    case befehl.JIH.wert:
                        if (register > 0) {
                            pop = this.pop();
                            if (pop != 0 && ((programCounter + pop) > 0)) {
                                programCounter = ((programCounter + pop) % MAX);
                            }
                            programCounter++;
                        } else {
                            programCounter++;
                        }
                        break
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Something is wrong - check your vm");
            }
        }
    }

    def fillMemoryRandom() {
        int zahl

        //3 Randomzahlen ins Memory schreiben mit PUSH-Befehl
        /* 100.times {
             zahl = random.nextInt(MEM_WERTE)
             zahl = (zahl & (Integer.MAX_VALUE - 7)) + (int) befehl.LOAD.wert
             //          println zahl & 7
             memory.add(zahl)

             zahl = random.nextInt(MEM_WERTE)
             zahl = (zahl & (Integer.MAX_VALUE - 7)) + (int) befehl.PUSH.wert
             //            println zahl & 7
             memory.add(zahl)
         }*/

        //Rest bis 1000 mit beliebigen Befehlen auffüllen
        while (memory.size() < 1000) {
            zahl = random.nextInt(MEM_WERTE)
            memory.add(zahl)
        }
    }

    private void addIfPrimeToPrimeNumbers(Integer elem) {
        // avoid negative numbers
        Integer elemAbs = Math.abs(elem);
        // only prime numbers bigger than the defined minSizeOfThePrime will be added
        if (elem > MIN_SIZE_OF_THE_PRIME) {
            // check if elem is prime
            if (isPrime(elemAbs)) {
                // add only new prime numbers
                if (!primeNumbers.contains(elemAbs)) {
                    // new prime number --> add
                    primeNumbers.add(elemAbs);
                } else {
                    // prime is known in primeNumbers
                    // nothing to do
                }
            }
        }
    }

    private boolean isPrime(Integer elem) {
        //check if elem is a multiple of 2
        if (elem % 2 == 0) return false;
        //if not, then just check the odds
        for (int i = 3; i * i <= elem; i += 2) {
            if (elem % i == 0)
                return false;
        }
        return true;
    }


    def mutate(int amount) {
        amount.times {
            int newCmd = random.nextInt(MEM_WERTE)
            int mutateCmdIndex = random.nextInt(memory.size())
            memory[mutateCmdIndex] = newCmd
        }
    }

    def anzahlBefehle() {
        HashMap<befehl, Integer> map = new HashMap()

        memory.each {
            def cmd
            switch (it & 7) {
                case befehl.LOAD.wert:
                    cmd = befehl.LOAD
                    break
                case befehl.PUSH.wert:
                    cmd = befehl.PUSH
                    break
                case befehl.POP.wert:
                    cmd = befehl.POP
                    break
                case befehl.MUL.wert:
                    cmd = befehl.MUL
                    break
                case befehl.DIV.wert:
                    cmd = befehl.DIV
                    break
                case befehl.ADD.wert:
                    cmd = befehl.ADD
                    break
                case befehl.SUB.wert:
                    cmd = befehl.SUB
                    break
                case befehl.JIH.wert:
                    cmd = befehl.JIH
                    break
            }

            if (map.containsKey(cmd)) {
                int wert = map.get(cmd)
                wert++
                map.put(cmd, wert)
            } else {
                map.put(cmd, 1)
            }
        }

        map.each {
            key, value -> println "$key: $value"
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        VM vm = new VM()
        vm.primeNumbers = this.primeNumbers.clone()
        vm.memory = this.memory.clone()
        return vm
    }

    def reset() {
        this.stack = new int[MAX];
        this.primeNumbers = new ArrayList<Integer>();
        this.programCounter = 0
        this.stackPointer = 0
        this.register = 0
    }
}

