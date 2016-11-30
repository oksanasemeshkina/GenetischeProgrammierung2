/**
 * Created by Oksana on 16.11.2016.
 */
class GenetischeProgrammierung {
    public static void main(String[] args) {
        ArrayList<VM> population = new ArrayList<>()
        100.times {
            VM vm = new VM()
            population.add(vm)
        }

        100000.times {
            population.each {
                it.reset()
                it.simulate()
            }

            population.sort(new Comparator<VM>() {
                @Override
                int compare(VM o1, VM o2) {
                    o2.primeNumbers.size() - o1.primeNumbers.size()
                }
            })

            println("\n" + "-" * 20 + "population" + "-" * 20)
            population.each {
                print "${it.primeNumbers.size()} "
            }

            //copy first half of the population into the second half
            population = population.subList(0, 50)
            ArrayList<VM> population2 = new ArrayList<>()
            population.each {
                population2.add(it.clone())
            }
            population = population + population2

            /*println("\n" + "-" * 20 + "1/2 population + 1/2 population" + "-" * 20)
            population.each {
                print "${it.primeNumbers.size()} "
            }*/

            //mutate second half
            def mutatePopulation = population.subList(50, 100)
            mutatePopulation.each {
                it.mutate(30)
            }
        }

        println "\nPrimzahlen des Besten:"
        population[0].primeNumbers.each {
            print "$it "
        }

        println "\nENDE"
    }
}
