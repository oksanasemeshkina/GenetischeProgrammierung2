/**
 * Created by Oksana on 16.11.2016.
 */
class GenetischeProgrammierung {
    public static void main(String[] args) {
        //VM vm = new VM()
        //vm.simulate()
        //println "Fitness: ${vm.calculateFitness()}"
        //vm.anzahlBefehle()

        ArrayList<VM> population = new ArrayList<>()
        100.times {
            VM vm = new VM()
            population.add(vm)

        }

        //schleife
        1000.times {

            population.each {
                it.reset()
                it.simulate()
            }

            population.sort(new Comparator<VM>() {
                @Override
                int compare(VM o1, VM o2) {
                    o2.calculateFitness() - o1.calculateFitness()
                }
            })

            println("\n" + "-" * 20 + "population" + "-" * 20)
            population.each {
                print "${it.calculateFitness()} "
            }

            //copy first half of the population into the second half
            population = population.subList(0, 50)
            ArrayList<VM> population2 = new ArrayList<>()
            population.each {
                population2.add(it.clone())
            }
            population = population + population2

            println("\n" + "-" * 20 + "1/2 population + 1/2 population" + "-" * 20)
            population.each {
                print "${it.calculateFitness()} "
            }

            //mutate second half
            def mutatePopulation = population.subList(50, 100)
            mutatePopulation.each {
                it.mutate(10)
            }
        }

        println "\nPrimzahlen des Besten:"
        population[0].stack.each {
            if (VM.istPrimzahl(it))
                print "$it "
        }

        println "\nENDE"

    }
}
