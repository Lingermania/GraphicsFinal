package com.ru.tgra.shapes;

import java.nio.Buffer;
import java.util.ArrayDeque;
import java.util.Queue;

/*
 * Very simple player physics that calculates Z rotation
 * from steadiness of course direction
 * */

public class PlayerPhysics {
	private Queue<Integer> zSeries;
	private Queue<Integer> speedSeries;
	private int MAX;
	private int _insert;
	
	public PlayerPhysics() {
		zSeries = new ArrayDeque<Integer>();
		speedSeries = new ArrayDeque<Integer>();
		MAX = 100;
		_insert = 0;
	}
	
	private void bufZ() {
		if (zSeries.size() >= MAX) {
			zSeries.remove();
		}
	}
	
	private void bufSpeed() {
		if (speedSeries.size() >= MAX) {
			speedSeries.remove();
		}
	}
	
	public void left() {
		
		_insert+=1;
		if (_insert >= 1) {
			bufZ();
			zSeries.add(-1);
			_insert = 0;
		}
		
	}
	
	public void right() {
		_insert+=1;
		if (_insert >= 1) {
			bufZ();
			zSeries.add(1);
			_insert = 0;
		}
		
	}
	
	public void neutralSpeed() {
		_insert+=1;
		if (_insert >= 1) {
			bufSpeed();
			speedSeries.add(0);
			_insert = 0;
		}
	}
	
	public void neutralZ() {
		_insert+=1;
		if (_insert >= 1) {
			bufZ();
			zSeries.add(0);
			_insert = 0;
		}
	}
	
	public void forward() {
		_insert+=1;
		if (_insert >= 1) {
			bufSpeed();
			speedSeries.add(1);
			_insert = 0;
		}
	}
	
	public void backwards() {
		_insert+=1;
		if (_insert >= 1) {
			bufSpeed();
			speedSeries.add(-1);
			_insert = 0;
		}
	}
	
	public float avgZ() {
		if (zSeries.size() == 0) return 0;
		float res = 0;
		
		for(Integer i : zSeries) {
			res += i;
		}
		
		res/= zSeries.size();
		
		return res;
	}
	
	public float avgSpeed() {
		if (speedSeries.size() == 0) return 0;
		
		float res = 0;
		for (Integer i : speedSeries) {
			res += i;
		}
		
		res /= speedSeries.size();
		
		return -res;
	}
	
}
