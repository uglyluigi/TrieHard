package ug;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.Semaphore;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static ug.TrieNode.TERMINATOR_NODE;

public class StandardTrie {

	private final TrieNode root = new TrieNode('\0');

	private boolean insert_s(String s) {
		TrieNode cursor = this.root;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int indexOfChildInList = cursor.indexOf(c);

			if (indexOfChildInList == -1) {
				String everythingToInsert = s.substring(i);

				for (char c_ : everythingToInsert.toCharArray()) {
					TrieNode newNode = new TrieNode(c_);
					cursor.addChild(newNode);
					cursor = newNode;
				}

				cursor.addChild(TERMINATOR_NODE);
				return true;
			} else {
				cursor = cursor.getChildren().get(indexOfChildInList);
			}
		}

		return false;
	}

	public void traverse(BiConsumer<Integer, TrieNode> consumer) {
		this.traverse(this.root, consumer, 0);
	}

	public boolean find(String text) {
		return find(this.root, text);
	}

	private boolean find(TrieNode n, String text) {
		LinkedList<TrieNode> nodeChildren = n.getChildren();

		if (text.isEmpty()) {
			return nodeChildren.contains(TERMINATOR_NODE);
		}

		char c = text.charAt(0);

		if (n != TERMINATOR_NODE) {
			TrieNode relevantCharNode = new TrieNode(c);
			int indexInNodeChildren = nodeChildren.indexOf(relevantCharNode);

			if (indexInNodeChildren != -1) {
				return this.find(n.getChildren().get(indexInNodeChildren), text.substring(1));
			}
		} else {
			return true;
		}

		return false;
	}

	public boolean insert(String text) {
		for (String s : text.split("\\s+")) {
			if (!this.insert_s(s)) {
				return false;
			}
		}

		return true;
	}

	public Optional<List<String>> autoComplete(String prefix, int length) {
		TrieNode cursor = this.root;

		for (char c : prefix.toCharArray()) {
			Optional<TrieNode> moveResult = this.tryNext(cursor, c);

			if (!moveResult.isPresent()) {
				return Optional.empty();
			}

			cursor = moveResult.get();
		}

		LinkedList<String> matches = new LinkedList<>();
		Stack<Character> wordStack = new Stack<>();
		int newLen = Math.abs(prefix.length() - length) - 1;

		for (TrieNode child : cursor.getChildren()) {
			buildMatch(child, newLen, wordStack, matches);
			wordStack.pop();
		}

		return Optional.of(matches.stream().map(prefix::concat).collect(Collectors.toList()));
	}

	public void buildMatch(TrieNode node, int length, Stack<Character> builder, LinkedList<String> words) {
		builder.push(node.getChar());

		if (length == 0) {
			if (node.hasChild(TERMINATOR_NODE)) {
				StringBuilder sb = new StringBuilder(builder.size());
				builder.forEach(sb::append);
				words.add(sb.toString());
			}

			return;
		}

		for (TrieNode child : node.getChildren()) {
			buildMatch(child, length - 1, builder, words);
			builder.pop();
		}
	}

	/**
	 * Tries to move to a child node of n containing char c.
	 *
	 * @param c
	 * @return the TrieNode if it successfully moves, null if not.
	 */
	public Optional<TrieNode> tryNext(TrieNode n, char c) {
		LinkedList<TrieNode> children = n.getChildren();
		int index = children.indexOf(new TrieNode(c));

		if (index > -1) {
			return Optional.of(children.get(index));
		} else {
			return Optional.empty();
		}
	}

	private void traverse(TrieNode n, BiConsumer<Integer, TrieNode> consumer, int level) {
		if (n == TERMINATOR_NODE) {
			return;
		}

		if (n != this.root) {
			consumer.accept(level, n);
		}

		for (TrieNode node : n.getChildren()) {
			this.traverse(node, consumer, level++);
		}
	}
}
