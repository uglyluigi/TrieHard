package ug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		StandardTrie trie5 = initTrie(new File("words_5.txt"));
		StandardTrie trie6 = initTrie(new File("words_6.txt"));
		StandardTrie trie7 = initTrie(new File("words_7.txt"));

		System.out.println("------------------------------------------");
		System.out.println();

		while (true) {
			Scanner scanner = new Scanner(System.in);
			System.out.print("Prefix of word: (1-3 characters): ");
			String prefix = scanner.nextLine();
			System.out.print("Length of desired matches (5-7 characters): ");
			int length = scanner.nextInt();

			StandardTrie trieToUse = null;

			switch (length) {
				case 5:
					trieToUse = trie5;
					break;
				case 6:
					trieToUse = trie6;
					break;
				case 7:
					trieToUse = trie7;
					break;
				default:
					System.out.println("Invalid selection.");
					System.exit(-1);
					break;
			}

			Optional<List<String>> possibleMatches = trieToUse.autoComplete(prefix, length);


			if (possibleMatches.isPresent()) {
				System.out.printf("Possible matches for words prefixed with %s of length %s: \n", prefix, length);
				possibleMatches.get().stream().forEach(System.out::println);

			} else {
				System.out.printf("No matches found for a prefix of %s of length %s\n", prefix, length);
			}
		}
	}

	public static StandardTrie initTrie(File f) throws IOException {
		StandardTrie trie = new StandardTrie();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String[] words = reader.readLine().toLowerCase().split(",");

		for (String s : words) {
			trie.insert(s);
		}

		long baseTime = System.nanoTime();
		for (String s : words) {
			testFind(trie, s);
		}
		long elapsedTime = System.nanoTime() - baseTime;
		System.out.printf("All tests took %s ns (%s ms)\n", elapsedTime, elapsedTime / 1000000);
		return trie;
	}

	public static void testFind(StandardTrie trie, String word) {
		long baseTime = System.nanoTime();
		boolean b = trie.find(word);
		long elapsedTime = System.nanoTime() - baseTime;
		System.out.printf("It took %sns (%sms) to %s %s\n", elapsedTime, (double) elapsedTime / 1000000, b ? "find" : "not find", word);
	}
}
