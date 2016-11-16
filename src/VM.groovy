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
    final int MAX = 1000

    ArrayList<Integer> memory = new ArrayList<>()
    ArrayList<Integer> stack = new ArrayList<>()
    int programCounter, stackPointer, register
    Random random = new Random()

    public VM() {
        fillMemoryRandom()
    }

    void push(int x) {
        if (stackPointer < 999)
            stack[stackPointer++] = x
        else
            stack[stackPointer] = x
    }

    int pop() {
        if (stackPointer >= 1)
            stackPointer--
        return stack[stackPointer]
    }

    void simulate() {
        int i = 0
        while (programCounter < MAX && (stackPointer > 0 || i < 10)) {
            switch (memory[programCounter] & 7) {
                case befehl.LOAD.wert:
                    register = memory[programCounter] >> 3
                    programCounter++
                    break
                case befehl.PUSH.wert:
                    push(register)
                    programCounter++
                    break
                case befehl.POP.wert:
                    register = pop()
                    programCounter++
                    break
                case befehl.MUL.wert:
                    register = register * pop()
                    programCounter++
                    break
                case befehl.DIV.wert:
                    def wert = pop()
                    if (wert != 0) {
                        register = register / wert
                    }
                    programCounter++
                    break
                case befehl.ADD.wert:
                    register = register + pop()
                    programCounter++
                    break
                case befehl.SUB.wert:
                    register = register - pop()
                    programCounter++
                    break
                case befehl.JIH.wert:
                    if (register > 0) {
                        programCounter = Math.abs((programCounter + pop())) % MAX
                    } else {
                        programCounter++
                    }
                    break
            }
            i++
        }
    }

    def fillMemoryRandom() {
        int zahl

        //3 Randomzahlen ins Memory schreiben mit PUSH-Befehl
        100.times {
            zahl = random.nextInt(10 << 3)
            zahl = (zahl & (Integer.MAX_VALUE - 7)) + (int) befehl.LOAD.wert
            //          println zahl & 7
            memory.add(zahl)

            zahl = random.nextInt(10 << 3)
            zahl = (zahl & (Integer.MAX_VALUE - 7)) + (int) befehl.PUSH.wert
            //            println zahl & 7
            memory.add(zahl)
        }

        //Rest bis 1000 mit beliebigen Befehlen auffüllen
        while (memory.size() < 1000) {
            zahl = random.nextInt(10 << 3)
            memory.add(zahl)
        }
    }

    int calculateFitness() {
        HashSet<Integer> prims = new HashSet<>()
        println "Stackgröße: ${stack.size()}"
        stack.each {
            if (istPrimzahl(it)) {
                if (prims.add(it)) {
                    print "$it "
                }
            }
        }
        println()
        prims.size()
    }

    boolean istPrimzahl(int zahl) {
        if (zahl > 1) {
            boolean isPrimzahl = true;
            for (int i = 2; i < zahl && isPrimzahl; i++) {
                if ((zahl % i) == 0) {
                    isPrimzahl = false;
                }
            }
            return isPrimzahl
        } else {
            return false
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
}

