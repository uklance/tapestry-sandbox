package com.github.uklance.domain;

public class ScoredItem implements Comparable<ScoredItem> {
	private double score;
	private Item item;
	
	public ScoredItem(Item item, double score) {
		super();
		this.item = item;
		this.score = score;
	}
	public int compareTo(ScoredItem other) {
		return (score == other.score) ? 0 : ((score < other.score) ? -1 : 1);
	}
	public double getScore() {
		return score;
	}
	public Item getItem() {
		return item;
	}
}
