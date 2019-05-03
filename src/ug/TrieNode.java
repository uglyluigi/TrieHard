package ug;


import java.util.LinkedList;
import java.util.ListIterator;

public class TrieNode implements Comparable<TrieNode> {
	public static TrieNode TERMINATOR_NODE = new TrieNode('\0');

	//Sets enforce elemental uniqueness, trees enforce order. Perfect...
	private final LinkedList<TrieNode> children = new LinkedList<>();
	private Character character;

	public TrieNode(char c) {
		this.character = c;
	}

	public Character getCharacter() {
		return this.character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public char getChar() {
		return this.character;
	}

	public boolean hasChildren() {
		return !this.children.isEmpty();
	}


	public boolean hasChild(TrieNode n) {
		return this.children.contains(n);
	}

	public boolean hasChild(char c) {
		return this.children.contains(new TrieNode(c));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TrieNode) {
			return this.character.compareTo(((TrieNode) obj).getCharacter()) == 0;
		}

		return false;
	}

	public boolean addChild(TrieNode n) {
		ListIterator<TrieNode> iterator = this.children.listIterator();

		while (true) {
			if (!iterator.hasNext()) {
				iterator.add(n);
				return true;
			}

			TrieNode nodeInList = iterator.next();
			int res = n.compareTo(nodeInList);

			if (res < 0) {
				iterator.previous();
				iterator.add(n);
				return true;
			} else if (res == 0) {
				return false;
			}
		}
	}

	public LinkedList<TrieNode> getChildren() {
		return this.children;
	}

	@Override
	public String toString() {
		return this.character + "";
	}

	@Override
	public int compareTo(TrieNode o) {
		return this.character.compareTo(o.getCharacter());
	}

	public int indexOf(char c) {
		return this.getChildren().indexOf(new TrieNode(c));
	}
}
