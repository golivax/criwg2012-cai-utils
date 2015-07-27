package br.usp.ime.lapessc.criwgcai.util;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ChainedTransformer;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class GraphVisualizer {

	public static void visualizeGraph(Graph graph){
	    // The Layout<V, E> is parameterized by the vertex and edge types
	    Layout<Integer, String> layout = new CircleLayout(graph);
	   
	    layout.setSize(new Dimension(1280,800)); // sets the initial size of the space
	     // The BasicVisualizationServer<V,E> is parameterized by the edge types
	     VisualizationViewer<Integer,String> vv = 
	              new VisualizationViewer<Integer,String>(layout);
	     vv.setPreferredSize(new Dimension(1280,800)); //Sets the viewing area size

	     //Adjust font size
	     
	     vv.setBackground(Color.WHITE);

	     Transformer labelTransformer = new ChainedTransformer<String,String>(new Transformer[]{
	             new ToStringLabeller<String>(),
	             new Transformer<String,String>() {
	             public String transform(String input) {
	                 return "<html><font size=\"6\">"+input;
	             }}});
	     
	     vv.getRenderContext().setVertexLabelTransformer(labelTransformer);
	     vv.getRenderer().getVertexLabelRenderer().setPosition(Position.AUTO);
	     
	     DefaultModalGraphMouse gm = buildGraphMouse();
		 vv.setGraphMouse(gm);
	     
	     JFrame frame = new JFrame("Simple Graph View");
	     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     frame.getContentPane().add(vv); 
	     frame.pack();
	     frame.setVisible(true);       		
	}

	private static DefaultModalGraphMouse buildGraphMouse() {
		DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		gm.setMode(ModalGraphMouse.Mode.PICKING);
		return gm;
	}
}