package geneticAlgorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import grader.Grader;
import music.Chord;
import music.Note;
import soundPlayer.SoundPlayer;

public class GeneticAlgorithm {

	private Organism[] population;
	private boolean[] targetHashes;
	private RandomNumberGenerator rng;
	
	private static StringBuilder sb = new StringBuilder();

	private static String line = "ab-cd";
	private static String filePath = line + "/" + line;

	// idea: incremental learning; different training phases; generalized ->
	// specialized
	public static void main(String[] args) {

		// Testing a single strand...
		// line a: 17712, line b: 20983, line c: 19822, line d: 16483
		// line ab: 10612, line ac: 10924, line ad: 10022, line bc: 14159, line bd:
		// 9660, line cd: 9653
		// line ab-cd: 6403, line ac-bd: 6517, line ad-bc: 9017
		GeneticAlgorithm ga = new GeneticAlgorithm(1, filePath);
		Dataset data = new ProgressionDataset("prog1000.dat");
//		Dataset data = new HashDataset("fullHash.dat");
		
//		sb.append("gen");
//		sb.append(",");
//		sb.append("error");
//		sb.append("\n");
//		
//		ga.runGenerations(400, 50, 0, data, 10);
//		ga.savePopulation(filePath);
//		System.out.println("SAVED");

		// Test organism
//		ga.initializeTargetHashes(data);
//		ga.testOrganism(ga.population[0], data, false);
//		System.out.println(ga.population[0].getFitness());

		// Cross-breeding different strands
//		Organism[] par = new Organism[] { new Organism("a/a0.org"), new Organism("b/b0.org"), new Organism("c/c0.org"),
//				new Organism("d/d0.org") };
//		String[] parNames = new String[] {"a", "b", "c", "d"};
//		
//		GeneticAlgorithm ga = new GeneticAlgorithm(0);
//		ga.initializeTargetHashes(data);
//		ga.initializeRNG(1000);
//
//		
//		for (int i = 0; i < par.length; i++) {
//			for (int j = i + 1; j < par.length; j++) {
//				System.out.println(parNames[i] + parNames[j]);
//				Organism[] ch = new Organism[2000];
//				
//				for (int c = 0; c < ch.length; c++) {
//					ch[c] = ga.reproduce(par[i], par[j], 0);
//				}
//				ga.savePopulation(ch, "test/" + parNames[i] + parNames[j] + "/" + parNames[i] + parNames[j]);
//			}
//		}
//		
//		ga.closeRNG();
//		System.out.println("DONE");

		// Cross-breeding secondary strands
//		Organism[][] par = new Organism[][] {
//			{new Organism("ab/ab0.org"), new Organism("cd/cd0.org")},
//			{new Organism("ac/ac0.org"), new Organism("bd/bd0.org")},
//			{new Organism("ad/ad0.org"), new Organism("bc/bc0.org")}
//		};
//		
//		String[][] parNames = new String[][] {
//			{"ab", "cd"},
//			{"ac", "bd"},
//			{"ad", "bc"}
//		};
//		
//		GeneticAlgorithm ga = new GeneticAlgorithm(0);
//		ga.initializeTargetHashes(data);
//		ga.initializeRNG(1000);
//		
//		for(int i =0; i < par.length; i++) {
//			String lineName = parNames[i][0] + "-" + parNames[i][1];
//			System.out.println(lineName);
//			
//			Organism[] ch = new Organism[2000];
//			for(int c =0; c < ch.length; c++) {
//				ch[c] = ga.reproduce(par[i][0], par[i][1], 0);
//			}
//			ga.savePopulation(ch, "test/" + lineName + "/" + lineName);
//		}
//		
//		ga.closeRNG();
//		System.out.println("DONE");

		// Playing harmonies
		Scanner sc = new Scanner(System.in);
		SoundPlayer sp = new SoundPlayer("piano 1");
		for (int i = 0; i < data.size(); i++) {

			System.out.println(Arrays.toString(data.getChordProg(i)));

			Note[][] harmony = ga.population[0].generateHarmony(data.getInitialNotes(i), data.getByteProg(i),
					data.getBassLine(i));

			System.out.println(Grader.gradeBasic(harmony, data.getChordProg(i), data.getByteProg(i)));

			System.out.println(Arrays.deepToString(harmony));

			for (Note[] ch : harmony) {
				sp.playChord(new Chord(ch).toPlayedChord(1000));
			}

			sc.next();
		}
		sc.close();

	}

	public GeneticAlgorithm(int popSize) {
		initializeRandomPopulation(popSize);
	}

	public GeneticAlgorithm(int popSize, String filePrefix) {
		population = new Organism[popSize];

		// Load organisms - takes about 2.5 seconds for 4000 organisms
		for (int i = 0; i < population.length; i++) {
			population[i] = new Organism(filePrefix + i + ".org");
		}
	}

	public void runGenerations(int numGenerations, int numSelected, int mutationPercent, Dataset data, int saveRate) {
		runGenerations(this.population, numGenerations, numSelected, mutationPercent, data, saveRate);
	}

	public void runGenerations(Organism[] population, int numGenerations, int numSelected, int mutationPercent,
			Dataset data, int saveRate) {

		initializeTargetHashes(data);
		initializeRNG(1000);

		for (int g = 0; g < numGenerations; g++) {
			// Timers
			long start = System.currentTimeMillis();

			// Print gen. number
			System.out.println("Generation " + g + ": ");

			// Test all organisms
			for (int oi = 0; oi < population.length; oi++) {
				testOrganism(population[oi], data, false);
			}

			// Sort population
			Arrays.sort(population);

			// Gen. Stats
			System.out.println("\tBEST: " + population[0].getFitness());
			System.out.println("\tMEDIAN: " + population[population.length / 2].getFitness());
			System.out.println("\tWORST: " + population[population.length - 1].getFitness());
			
			sb.append(g);
			sb.append(",");
			sb.append(population[0].getFitness());
			sb.append("\n");

			// Reproduction
			long startReproducing = System.currentTimeMillis();
			int childrenPerPair = (2 * (population.length / numSelected)) - 2;

			ArrayList<Organism> temp = new ArrayList<Organism>();

//			for (int i = 0; i < numSelected - 1; i += 2) {
//				Organism A = population[i];
//				Organism B = population[i + 1];
//
//				temp.add(new Organism(A.getGenome()));
//				temp.add(new Organism(B.getGenome()));
//				for (int j = 0; j < childrenPerPair; j++) {
//					temp.add(reproduce(A, B, mutationPercent));
//				}
//			}
			
			for (int i = 0; i < numSelected / 2; i++) {
				Organism A = population[i];
				Organism B = population[numSelected - i - 1];

				temp.add(new Organism(A.getGenome()));
				temp.add(new Organism(B.getGenome()));
				for (int j = 0; j < childrenPerPair; j++) {
					temp.add(reproduce(A, B, mutationPercent));
				}
			}

			while (temp.size() < population.length) {
				temp.add(new Organism());
			}

			for (int i = 0; i < temp.size(); i++) {
				population[i] = temp.get(i);
			}
			long reproducingTime = (System.currentTimeMillis() - startReproducing);

			System.out.println("\t(" + (System.currentTimeMillis() - start) + " / " + reproducingTime + ")");

			if ((g % saveRate) == saveRate - 1) {
				savePopulation(population, filePath);
				
				try (PrintWriter writer = new PrintWriter(new File("log4.csv"))) {
					writer.write(sb.toString());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				System.out.println("SAVED");
			}
		}

		closeRNG();

		System.out.println("END");
	}

	public void initializeTargetHashes(Dataset data) {
		targetHashes = new boolean[Organism.getInputSpace()];
		for (int i = 0; i < data.getHashes().length; i++) {
			targetHashes[data.getHash(i)] = true;
		}
	}

	public void initializeRNG(int len) {
		rng = new RandomNumberGenerator(len);
		rng.start();
	}

	public void closeRNG() {
		rng.exit();
		rng.interrupt();
	}

	public void savePopulation(String filePrefix) {
		savePopulation(filePrefix, population.length);
	}

	public void savePopulation(Organism[] population, String filePrefix) {
		savePopulation(population, filePrefix, population.length);
	}

	public void savePopulation(String filePrefix, int numOrgs) {
		savePopulation(population, filePrefix, numOrgs);
	}

	public void savePopulation(Organism[] population, String filePrefix, int numOrgs) {
		Arrays.sort(population);
		for (int i = 0; i < numOrgs; i++) {
			population[i].save(filePrefix + i + ".org");
		}
	}

	// Test an organism against a Dataset - updates the Organism's fitness attribute
	// Only set usesHash true if using HashDataset
	public void testOrganism(Organism o, Dataset data, boolean useAdvanced) {
		for (int i = 0; i < data.size(); i++) {
			Note[][] harmony = o.generateHarmony(data.getInitialNotes(i), data.getByteProg(i), data.getBassLine(i));

			int score = Integer.MAX_VALUE;
			if (useAdvanced) {
				score = Grader.gradeAdvanced(harmony, data.getChordProg(i), data.getByteProg(i));
			} else {
				score = Grader.gradeBasic(harmony, data.getChordProg(i), data.getByteProg(i));
			}
			o.addFitness(score);
		}
	}
	
	//
	public Organism reproduce(Organism a, Organism b, int mutationPercent) {
		byte[][] genomeA = a.getGenome();
		byte[][] genomeB = b.getGenome();
		
		// 50/50 method
		byte[][] childGenome = new byte[genomeA.length][genomeA[0].length];
		
		for (int i = 0; i < childGenome.length; i++) {
			for (int j = 0; j < childGenome[i].length; j++) {
				if (targetHashes == null || targetHashes[j]) {
					
					int r = rng.getRand();
					if (r < 500) {
						childGenome[i][j] = genomeA[i][j];
					} else {
						childGenome[i][j] = genomeB[i][j];
					}

					mutate(childGenome, i, j, mutationPercent);
				}
			}
		}

		// cross-over method // using "childGenome2" for now - CHANGE LATER!
//		byte[][] childGenome2 = new byte[genomeA.length][genomeA[0].length];
//		int crossPoint = (int) (Math.random() * genomeA[0].length);
//
//		// loop through rows and columns
//		for (int r = 0; r < childGenome2.length; r++) {
//			for (int c = 0; c < childGenome2[0].length; c++) {
//
//				if (c < crossPoint) {
//					// if this is before the crosspoint, get genes from parent 1
//					childGenome2[r][c] = genomeA[r][c];
//				} else {
//					// otherwise get genes from parent 2
//					childGenome2[r][c] = genomeB[r][c];
//				}
//
//				mutate(childGenome2, r, c);
//
//			}
//		}

		return new Organism(childGenome);

	}

	public void mutate(byte[][] genome, int i, int j, int percent) {
		int r = rng.getRand();

		if (r < percent * Organism.ranges[i + 1].length) {
			genome[i][j] = (byte) (r / percent);
		}
	}

	public void initializeRandomPopulation(int populationSize) {
		population = new Organism[populationSize];
		for (int i = 0; i < population.length; i++) {
			population[i] = new Organism();
		}
	}
}
