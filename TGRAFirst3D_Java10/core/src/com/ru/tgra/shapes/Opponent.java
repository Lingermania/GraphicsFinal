package com.ru.tgra.shapes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import com.badlogic.gdx.graphics.Texture;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.shapes.g3djmodel.MeshModel;

class Node implements Comparable<Node>{
	public Point3D position;
	public Vector3D direction;
	public String move;
	public float input;
	public Node parent;
	public float score;
	public float depth;
	public float xAngle;
	
	public Node(Point3D position, Vector3D direction) {
		this.position = position;
		this.direction = direction;
	}
	

	@Override
    public int compareTo(Node o) {
		Node n = (Node)o;
		
        if (this.score < n.score) {
            return 1;
        }
        if (this.score > n.score)
            return -1;
        return 0;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) return true;
        
        Node o = (Node)obj;
        
        return (o.position.x == this.position.x &&
        		o.position.y == this.position.y &&
        		o.position.z == this.position.z &&
        		o.direction.x == this.direction.x &&
        		o.direction.y == this.direction.y &&
        		o.direction.z == this.direction.z); 
    }

    //Idea from effective Java : Item 9
    @Override
    public int hashCode() {
        String res = this.position.x + "," + this.position.y + "," + this.position.z +
        		     this.direction.x + "," + this.direction.y + "," + this.direction.z;
        
        return res.hashCode();
    }
}

public class Opponent extends Player{

	private Shader shader;
	private MeshModel model;
	private Player target;
	public Opponent(Point3D position, Vector3D direction, Shader shader, Player target, World world) {
		
		super(position, direction, world);
		this.shader = shader;
		this.target = target;
		
		model = G3DJModelLoader.loadG3DJFromFile("TIEfighter.g3dj");
	}
	
	public void shoot() {
		lasers.add(new Laser(new Point3D(position.x, position.y, position.z),
				  Vector3D.scale(direction, 80), 
				  new Point3D(angleX, angleY, angleZ), 
				  shader));
	}
	
	public void drawLasers() {
		for(Laser l : lasers) {
			l.draw();
		}
	}
	
	private float score(Node node) {
		float res = 0;
		
		float minDist = 1000000;
		
		for(Opponent o : this.world.opponents) {
			if (o == this) continue;
			float dist = Vector3D.difference(o.position, node.position).length();
			
			minDist = Math.min(minDist, dist);
		}
		
		if (minDist > 4) {
			minDist = 1;
		}
		
		res = Vector3D.difference(node.position, target.position).length();
		
		return -res/minDist;
	}
	
	public ArrayList<Node> expand(Node node, float dt) {
		float xAngle = 90.0f*dt;
		float yAngle = 90.0f*dt;
		ArrayList<Node> res = new ArrayList<Node>();
		
		//Can either move or rotate on X or Y axis
		
		Node moveNode = new Node(new Point3D(node.position.x, node.position.y, node.position.z), 
						new Vector3D(node.direction.x, node.direction.y, node.direction.z));
		moveNode.move = "MOV";
		moveNode.input = 1.0f;
		moveNode.parent = node;
		moveNode.depth = node.depth + 1;
		
		Node xNode = new Node(new Point3D(node.position.x, node.position.y, node.position.z), 
				new Vector3D(node.direction.x, node.direction.y, node.direction.z));
		
		xNode.move = "ROTX";
		xNode.input = -1;
		xNode.parent = node;
		xNode.depth = node.depth +1;
		xNode.xAngle = node.xAngle;
		
		Node xNodeD = new Node(new Point3D(node.position.x, node.position.y, node.position.z), 
				new Vector3D(node.direction.x, node.direction.y, node.direction.z));
		
		xNodeD.move = "ROTX";
		xNodeD.input = 1;
		xNodeD.parent = node;
		xNodeD.depth = node.depth +1;
		xNodeD.xAngle = node.xAngle;
		
		Node yNode = new Node(new Point3D(node.position.x, node.position.y, node.position.z), 
				new Vector3D(node.direction.x, node.direction.y, node.direction.z));
		
		yNode.move = "ROTY";
		yNode.input = yAngle;
		yNode.parent = node;
		yNode.depth = yNode.depth;
		yNode.xAngle = xAngle;
		
		float avgPhy = phys.avgSpeed()*0.6f;
		moveNode.position.x -= moveNode.direction.x;//*dt*avgPhy;
		moveNode.position.y -= moveNode.direction.y;//*dt*avgPhy;
		moveNode.position.z -= moveNode.direction.z;//*dt*avgPhy;
		
		moveNode.score =  score(moveNode);
		moveNode.xAngle = xAngle;
		
		
		float radians = (angleZ * (float)Math.PI / 180.0f);
		float c       = (float)Math.cos(radians);
		float s       = -(float)Math.sin(radians);
		
		
		radians = (xAngle * (float)Math.PI / 180.0f);
		c       = (float)Math.cos(radians);
		s       = -(float)Math.sin(radians);
		
		xNode.direction = new Vector3D(xNode.direction.x,
									  c*xNode.direction.y + s*xNode.direction.z,
				 					  -s*xNode.direction.y + c*xNode.direction.z);
		xNode.score =  score(xNode);
		
		radians = (-xAngle * (float)Math.PI / 180.0f);
		c       = (float)Math.cos(radians);
		s       = -(float)Math.sin(radians);
		
		xNodeD.direction = new Vector3D(xNodeD.direction.x,
									  c*xNodeD.direction.y + s*xNodeD.direction.z,
				 					  -s*xNodeD.direction.y + c*xNodeD.direction.z);
		
		xNodeD.score =  score(xNodeD);
		xNodeD.xAngle = node.xAngle;
		
		radians = (yAngle * (float)Math.PI / 180.0f);
		c       = (float)Math.cos(radians);
		s       = -(float)Math.sin(radians);
		
		yNode.direction = new Vector3D(c*yNode.direction.x - s*yNode.direction.z,
									   yNode.direction.y,
									   s*yNode.direction.x + c*yNode.direction.z);
		yNode.score = score(yNode);
		
		res.add(moveNode);
		xNode.xAngle -= xAngle;
		xNodeD.xAngle += xAngle;
		
		if (xNode.xAngle > -90f) {
			//TODO fix this if we want x rotation for AI
			//System.out.println("XNODE: " + xNode.input);
			//res.add(xNode);
		}
		if (xNodeD.xAngle < 90f) {
			//TODO fix this if we want x rotation for AI
			//System.out.println("XNODED: " + xNodeD.input);
			//res.add(xNodeD);
		}
		//res.add(xNode);
		//res.add(xNodeD);
		res.add(yNode);
		
		return res;
	}
	
	@Override
	public void updatePhysics(float dt, boolean movePhysics) {
		this.angleZ = phys.avgZ()*90;
		this.angleY -= phys.avgZ()*45*dt;
		
		float physSpeed = phys.avgSpeed()*0.1f;
		//System.out.println(physSpeed);
		
		if(movePhysics) {
			position.x += direction.x*physSpeed;
			position.y += direction.y*physSpeed;
			position.z += direction.z*physSpeed;
		}
		
	}
	

	public void setExplosion() {
		explosion = new Explosion(position, shader);
	}
	
	public Node search(float dt) {
		//dt is not accounted for in the search
		Node startNode = new Node(new Point3D(position.x, position.y, position.z), new Vector3D(direction.x, direction.y, direction.z));
		startNode.xAngle = angleX;
		//System.out.println("Starting at angleX: " + angleX);
		//System.out.println(score(startNode));
		startNode.move = "None";
		startNode.input = -1;
		startNode.score = score(startNode);
		startNode.parent = null;
		
		Set<Node> closedSet = new HashSet<Node>();
		//List<Node> openSet   = new ArrayList<Node>();
		//PriorityQueue<Node> openSet = new PriorityQueue<Node>();
		Queue<Node> openSet = new ArrayDeque<Node>();
		openSet.add(startNode);
		
		int maxIter = 128;
		int iter = 0;
		
		while(!openSet.isEmpty()) {
			
			//Node curr = openSet.remove();
			
			Node curr = openSet.remove();
			//openSet.remove(0);
			
			if (closedSet.contains(curr)) {
				continue;
			}
			closedSet.add(curr);
			
			for(Node n : expand(curr, dt)) {
				//System.out.println(n.score + ", " + curr.score);
				if (openSet.contains(n) || closedSet.contains(n) || n.score < curr.score - 0.2) {
					continue;
				}
				
				openSet.add(n);
				
			}
			iter++;
			if(iter >= maxIter) {
				break;
			}
		}
		
		float maxScore = -1000000f;
		Node maxNode = startNode;
		//System.out.println(openSet.size());
		for(Node n : closedSet) {
			//System.out.println(n.move + ", " + n.score);
			//System.out.println(n.score +",,," + maxScore);
			//System.out.println(maxNode.parent);
			if (n.score > maxScore ) {
				
				maxNode = n;
				maxScore = n.score;
			}
		}
		//System.out.println("MaxNode score: " + maxNode.score);
		int i = 0;
		while(maxNode.parent != null) {
			//System.out.println("MOVE: " + maxNode.move +", " + maxNode.input);
			i++;
			if (maxNode.parent.parent == null) {
				break;
			}
			maxNode = maxNode.parent;
		}
		return maxNode;
		
	}
	
	public void simulate(float dt) {
		
		//System.out.println(search().move);
		if (!this.alive) {
			explode();
			return;
		}
		Node mov = search(dt);
		while (mov.parent != null) {
			//System.out.println(mov.move + ", " + mov.input);
			if (mov.move.equals("MOV")) {
				move(-dt);
				forward();
				neutralZ();
				
			}
			else if (mov.move.equals("ROTX")) {
				
				rotateX(90 * mov.input, dt);
				System.out.println("ROTX: " + mov.input);
			}
			else if(mov.move.equals("ROTY")) {
				rotateY(90, dt);
				left();
			}
			
			mov = mov.parent;
		}
		updatePhysics(dt, true);
		rotateXYZ();
		
	
	}
	
	public void draw() {
		ModelMatrix.main.loadIdentityMatrix();
		ModelMatrix.main.pushMatrix();
		

		shader.setLightPosition(position.x, position.y + 2, position.z, 1.0f);


		//shader.setSpotDirection(s2, -0.3f, c2, 0.0f);
		//shader.setSpotDirection(-cam.n.x, -cam.n.y, -cam.n.z, 0.0f);
		shader.setSpotExponent(0.0f);
		shader.setConstantAttenuation(1.0f);
		shader.setLinearAttenuation(0.00f);
		shader.setQuadraticAttenuation(0.00f);

		//shader.setLightColor(s2, 0.4f, c2, 1.0f);
		shader.setLightColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		shader.setGlobalAmbient(1f, 1f,1f, 1);

		//shader.setMaterialDiffuse(s, 0.4f, c, 1.0f);
		shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		//shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0, 0, 0, 1);
		shader.setShininess(50.0f);
		
	
		ModelMatrix.main.addTranslation(position.x, position.y, position.z);
		
		ModelMatrix.main.addRotationY(angleY);
		ModelMatrix.main.addRotationX(angleX);
		ModelMatrix.main.addRotationZ(angleZ);
		ModelMatrix.main.addTranslation(-1.4616f, 0, 2.8852f);

		
		ModelMatrix.main.addScale(0.01f, 0.01f, 0.01f);

		
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		model.draw(shader);
		drawLasers();

		
		ModelMatrix.main.popMatrix();
	}
}
