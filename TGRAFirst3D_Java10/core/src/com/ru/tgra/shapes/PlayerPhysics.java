package com.ru.tgra.shapes;

import java.nio.Buffer;
import java.util.ArrayDeque;
import java.util.Queue;

/*
 * Very simple player physics that calculates Z rotation
 * from steadiness of course direction
 * */

public class PlayerPhysics {
	private Queue<Integer> series;
	private int MAX;
	private int _insert;
	
	public PlayerPhysics() {
		series = new ArrayDeque<Integer>();
		MAX = 100;
		_insert = 0;
	}
	
	private void buf() {
		if (series.size() >= MAX) {
			series.remove();
		}
	}
	public void left() {
		
		_insert+=1;
		if (_insert >= 1) {
			buf();
			series.add(-1);
			_insert = 0;
		}
		
	}
	
	public void right() {
		_insert+=1;
		if (_insert >= 1) {
			buf();
			series.add(1);
			_insert = 0;
		}
		
	}
	
	public void forward() {
		_insert+=1;
		if (_insert >= 5) {
			buf();
			series.add(0);
			_insert = 0;
		}
	}
	
	public float avg() {
		if (series.size() == 0) return 0;
		float res = 0;
		
		for(Integer i : series) {
			res += i;
		}
		
		res/= series.size();
		
		return res;
	}
	
	
}
