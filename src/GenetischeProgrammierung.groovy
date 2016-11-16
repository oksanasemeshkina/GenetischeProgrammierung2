/**
 * Created by Oksana on 16.11.2016.
 */
class GenetischeProgrammierung {
    public static void main(String[] args) {
        VM vm = new VM()
        vm.simulate()
        println "Fitness: ${vm.calculateFitness()}"
        //vm.anzahlBefehle()

        VM[] population = new VM[100]
        for (int i = 0; i < population.size(); i++) {
            population[i] = new VM()
            population[i].simulate()
        }

        
        println "ENDE"
    }
}
