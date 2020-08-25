import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesSimple;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;
import weka.filters.unsupervised.attribute.NumericToNominal;

import java.awt.Font;
import java.io.File;
import javax.swing.SpringLayout;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class AccuracyPredictionClassifiers {

	private JFrame frame;
	private JTextField txtSelectFile;
	JTextArea textArea, textArea_1;
	
	String fileID;
	String numberofAttributes;
	float splitRatio;
	JScrollPane scrollPane_1;
	private JTable table;
	String[] columnNames = {"Classifier", "Accuracy", "Precision(PPV)", "Recall(TPR)", "TNR", "FPR", "FNR"};
	DefaultTableModel model;
	 
	NaiveBayesSimple nbclassifier;
	J48 tree;
	MultilayerPerceptron mlp;
	
	Instances trainSet, testSet;
	Evaluation evaluation;
	double accuracy, ppv, recall, tnr, fpr, fnr;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AccuracyPredictionClassifiers window = new AccuracyPredictionClassifiers();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AccuracyPredictionClassifiers() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 420, 340);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JLabel lblAccuracyPredictionOf = new JLabel("Analysis of Classifiers with Machine Learning based Thyroid Disease Prediction System");
		springLayout.putConstraint(SpringLayout.WEST, lblAccuracyPredictionOf, 300, SpringLayout.WEST, frame.getContentPane());
		lblAccuracyPredictionOf.setHorizontalAlignment(SwingConstants.CENTER);
		springLayout.putConstraint(SpringLayout.NORTH, lblAccuracyPredictionOf, 10, SpringLayout.NORTH, frame.getContentPane());
		lblAccuracyPredictionOf.setFont(new Font("Tahoma", Font.BOLD, 15));
		frame.getContentPane().add(lblAccuracyPredictionOf);
		
		JLabel lblUploadDatasetFile = new JLabel("Upload Dataset File");
		springLayout.putConstraint(SpringLayout.NORTH, lblUploadDatasetFile, 50, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblUploadDatasetFile, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, lblUploadDatasetFile, -274, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(lblUploadDatasetFile);
		
		txtSelectFile = new JTextField();
		springLayout.putConstraint(SpringLayout.WEST, txtSelectFile, 216, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, txtSelectFile, -305, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, txtSelectFile, 69, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, txtSelectFile, 50, SpringLayout.NORTH, frame.getContentPane());
		txtSelectFile.setColumns(5);
		frame.getContentPane().add(txtSelectFile);
		
		
		JButton btnNewButton = new JButton("Choose File");
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton, -4, SpringLayout.NORTH, lblUploadDatasetFile);
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton, 42, SpringLayout.EAST, lblUploadDatasetFile);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == btnNewButton)
			    {
					JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.home") + "\\Downloads")); //Downloads Directory as default
					fileChooser.setDialogTitle("Select Location");
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			        fileChooser.setAcceptAllFileFilterUsed(false);

			        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION)
			        { 
			             fileID = fileChooser.getSelectedFile().getPath();
			             txtSelectFile.setText(fileID);
			        }
			    }
				numberofAttributes = JOptionPane.showInputDialog("Enter Number of Attributes for Diabetes Dataset= 8 and Heart Disease Dataset = 14 ");
			}
		});
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Select Classifier");
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 6, SpringLayout.SOUTH, lblUploadDatasetFile);
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, 0, SpringLayout.WEST, lblUploadDatasetFile);
		springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel, 29, SpringLayout.SOUTH, lblUploadDatasetFile);
		springLayout.putConstraint(SpringLayout.EAST, lblNewLabel, 0, SpringLayout.EAST, txtSelectFile);
		frame.getContentPane().add(lblNewLabel);
		
		JRadioButton rdbtnNaiveBayes = new JRadioButton("Naive Bayes Simple");
		springLayout.putConstraint(SpringLayout.NORTH, rdbtnNaiveBayes, 35, SpringLayout.SOUTH, lblUploadDatasetFile);
		frame.getContentPane().add(rdbtnNaiveBayes);
		
		JRadioButton rdbtnMultiLayerPerceptron = new JRadioButton("Multi Layer Perceptron");
		springLayout.putConstraint(SpringLayout.NORTH, rdbtnMultiLayerPerceptron, 31, SpringLayout.SOUTH, rdbtnNaiveBayes);
		springLayout.putConstraint(SpringLayout.WEST, rdbtnMultiLayerPerceptron, 82, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, rdbtnNaiveBayes, 0, SpringLayout.WEST, rdbtnMultiLayerPerceptron);
		frame.getContentPane().add(rdbtnMultiLayerPerceptron);
		
		JRadioButton rdbtnCjDecision = new JRadioButton("C4.5 (J48) Decision Tree");
		springLayout.putConstraint(SpringLayout.NORTH, rdbtnCjDecision, 6, SpringLayout.SOUTH, rdbtnNaiveBayes);
		springLayout.putConstraint(SpringLayout.WEST, rdbtnCjDecision, 0, SpringLayout.WEST, rdbtnNaiveBayes);
		frame.getContentPane().add(rdbtnCjDecision);
		
		JButton btnTrainTheClassifier = new JButton("Train");
		btnTrainTheClassifier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnNaiveBayes.isSelected()) {
					trainNavieBayes();
				}
				else if(rdbtnMultiLayerPerceptron.isSelected()) {
					trainMLP();
				}
				else if(rdbtnCjDecision.isSelected()) {
					trainTreeJ48();
				}
				else {}
			}

		});
		springLayout.putConstraint(SpringLayout.NORTH, btnTrainTheClassifier, 21, SpringLayout.SOUTH, rdbtnMultiLayerPerceptron);
		springLayout.putConstraint(SpringLayout.WEST, btnTrainTheClassifier, 0, SpringLayout.WEST, lblUploadDatasetFile);
		frame.getContentPane().add(btnTrainTheClassifier);
		
		JButton btnEvaluate = new JButton("Evaluate");
		btnEvaluate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnNaiveBayes.isSelected()) {
					evaluateNavieBayes();
				}
				else if(rdbtnMultiLayerPerceptron.isSelected()) {
					evaluateMLP();
				}
				else if(rdbtnCjDecision.isSelected()) {
					evaluateTreeJ48();
				}
				else {}
			}
		});
		springLayout.putConstraint(SpringLayout.WEST, btnEvaluate, 6, SpringLayout.EAST, btnTrainTheClassifier);
		springLayout.putConstraint(SpringLayout.SOUTH, btnEvaluate, 0, SpringLayout.SOUTH, btnTrainTheClassifier);
		frame.getContentPane().add(btnEvaluate);
		
		JButton btnTest = new JButton("Test");
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnNaiveBayes.isSelected()) {
					testNavieBayes();
				}
				else if(rdbtnMultiLayerPerceptron.isSelected()) {
					testMLP();
				}
				else if(rdbtnCjDecision.isSelected()) {
					testTreeJ48();
				}
				else {}
			}
			
		});
		springLayout.putConstraint(SpringLayout.WEST, btnTest, 6, SpringLayout.EAST, btnEvaluate);
		springLayout.putConstraint(SpringLayout.SOUTH, btnTest, 0, SpringLayout.SOUTH, btnTrainTheClassifier);
		frame.getContentPane().add(btnTest);
		
		JButton btnAnalyse = new JButton("Analyse");
		btnAnalyse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnNaiveBayes.isSelected()) {
					barGraph("Naive Bayes Simple", accuracy, ppv, recall, tnr, fpr, fnr);
					rdbtnNaiveBayes.setSelected(false);
				}
				else if(rdbtnMultiLayerPerceptron.isSelected()) {
					barGraph("Multilayer Perceptron", accuracy, ppv, recall, tnr, fpr, fnr);
					rdbtnMultiLayerPerceptron.setSelected(false);
				}
				else if(rdbtnCjDecision.isSelected()) {
					barGraph("C4.5 (J48) Decision Tree", accuracy, ppv, recall, tnr, fpr, fnr);
					rdbtnCjDecision.setSelected(false);
				}
				else {}				
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnAnalyse, 0, SpringLayout.NORTH, btnTrainTheClassifier);
		springLayout.putConstraint(SpringLayout.WEST, btnAnalyse, 8, SpringLayout.EAST, btnTest);
		springLayout.putConstraint(SpringLayout.EAST, btnAnalyse, 97, SpringLayout.WEST, txtSelectFile);
		frame.getContentPane().add(btnAnalyse);
		
		JScrollPane scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 6, SpringLayout.SOUTH, btnTrainTheClassifier);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, lblUploadDatasetFile);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 180, SpringLayout.EAST, btnTrainTheClassifier);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scrollPane);
		
		//JTextArea textArea = new JTextArea();
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		//JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1 = new JScrollPane();
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane_1, -31, SpringLayout.SOUTH, btnTrainTheClassifier);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane_1, 70, SpringLayout.SOUTH, lblAccuracyPredictionOf);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane_1, 37, SpringLayout.EAST, rdbtnCjDecision);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane_1, -14, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(scrollPane_1);
		
		table = new JTable();
		/*table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Accuracy", "Precision", "Recall", "Recall", "TPR", "TNR", "FPR", "FNR"
				}
			));
				
		scrollPane_1.setViewportView(table);*/
		
		model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        table.setModel(model);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        //table.setFillsViewportHeight(true);
        scrollPane_1.setViewportView(table);
        
        JButton btnExit = new JButton("Exit");
        btnExit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		System.exit(0);
        	}
        });
        springLayout.putConstraint(SpringLayout.NORTH, btnExit, -4, SpringLayout.NORTH, lblUploadDatasetFile);
        springLayout.putConstraint(SpringLayout.EAST, btnExit, -39, SpringLayout.EAST, frame.getContentPane());
        frame.getContentPane().add(btnExit);
        
        JScrollPane scrollPane_2 = new JScrollPane();
        springLayout.putConstraint(SpringLayout.NORTH, scrollPane_2, 6, SpringLayout.SOUTH, btnAnalyse);
        springLayout.putConstraint(SpringLayout.WEST, scrollPane_2, 0, SpringLayout.WEST, scrollPane_1);
        springLayout.putConstraint(SpringLayout.SOUTH, scrollPane_2, -10, SpringLayout.SOUTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, scrollPane_2, 0, SpringLayout.EAST, scrollPane_1);
        scrollPane_2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane_2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(scrollPane_2);
        
        //JTextArea textArea_1 = new JTextArea();
        textArea_1 = new JTextArea();
        scrollPane_2.setViewportView(textArea_1);
		
				
	}

	protected void testMLP() {
		// TODO Auto-generated method stub
		for(int i=0;i<testSet.numInstances();i++)
	    {
	          double value;
			try {
				value = mlp.classifyInstance(testSet.instance(i));
				String prediction=testSet.classAttribute().value((int)value); 
		        System.out.println(testSet.instance(i)+"............Prediction.......... "+prediction);
		        textArea_1.append(testSet.instance(i)+"............Prediction.......... "+prediction+"\n");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	          
	    }
		
	}

	protected void evaluateMLP() {
		// TODO Auto-generated method stub
		try {
			evaluation = new Evaluation(trainSet);
		
			evaluation.evaluateModel(mlp, testSet);
		 
			// We can check some statistics and create Prediction objects
			System.out.println(evaluation.toSummaryString());
			fnr = evaluation.falseNegativeRate(1);
			System.out.println("False negative rate: " + fnr);
			fpr = evaluation.falsePositiveRate(1);
			System.out.println("False possitive rate: " + fpr);
			tnr = evaluation.trueNegativeRate(1);
			System.out.println("True negative rate: " + tnr);
			recall = evaluation.truePositiveRate(1);
			System.out.println("True positive rate(Recall): " + recall);
			accuracy = (evaluation.numTruePositives(1)+evaluation.numTrueNegatives(1))/(evaluation.numTruePositives(1)+evaluation.numTrueNegatives(1)+evaluation.numFalsePositives(1)+evaluation.numFalseNegatives(1));
			System.out.println("Accuracy: " + accuracy );
			ppv = evaluation.numTruePositives(1)/(evaluation.numTruePositives(1)+evaluation.numFalsePositives(1));
			System.out.println("Positive Predictive Value(Precision): " + ppv );
			System.out.println(evaluation.toMatrixString()); // Confusion matrix
			
			
            model.addRow(new Object[]{"Multilayer Perceptron", accuracy, ppv, recall, tnr, fpr, fnr});
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	protected void trainMLP() {
		// TODO Auto-generated method stub
		try {
			// First we need to load our data into Instances object
			
			/*Dataset dataset = FileHandler.loadDataset(new File("playtennis/thyroid0387.data"), 4, ",");
			Instances data = new Instances((Reader) dataset);	*/
			
			/*DataSource source = new DataSource("playtennis/heartdiesease.arff");
			Instances data = source.getDataSet();*/
			
			CSVLoader loader = new CSVLoader();
			//loader.setSource(new File("playtennis/diabetes.csv"));
			loader.setSource(new File(fileID));
		    Instances data = loader.getDataSet();
					
			// We can check some statistics
			/*System.out.println("Number of data instances " + data.numInstances());
			System.out.println("Number of data attributes " + data.numAttributes());*/
			textArea.setText("Number of data instances " + data.numInstances() + "\n"+ "Number of attributes " + data.numAttributes() + "\n");
					
			// Delete the id column
			data.deleteAttributeAt(0);
					
			// Change numeric format of Class attribute into actual values
			NumericToNominal convert = new NumericToNominal();
			String[] options = new String[2];
			options[0] = "-R";   //replace
			//options[1] = "8"; // range of variables, here we start counting from 1!
			options[1] = numberofAttributes; // range of variables, here we start counting from 1!
			
			
			convert.setOptions(options);
			convert.setInputFormat(data);
			data = Filter.useFilter(data, convert); // Run the conversion
				
			// We need to tell Weka explicitly which column is our class column
			data.setClassIndex(data.numAttributes() - 1);
			
			/*Resample resample = new Resample();
			resample.setBiasToUniformClass(1.0);
			resample.setInputFormat(data);
			resample.setNoReplacement(false);
			resample.setSampleSizePercent(100);
			data = Filter.useFilter(data, resample);
			*/
			//System.out.println("Number of classes " +  data.numClasses());
			textArea.append("Number of classes " +  data.numClasses());
					
			// Define train set size
			//int trainSize = (int) Math.round(data.numInstances() * 0.8);
			splitRatio = Float.parseFloat(JOptionPane.showInputDialog("Enter Dataset Split Ratio as 0.9, 0.8, 0.6, 0.5, 0.3"));
			int trainSize = (int) Math.round(data.numInstances() * splitRatio);
			 
			// Create NavieBayesSimple classifier
			mlp = new MultilayerPerceptron();
			mlp.setLearningRate(0.1);
			mlp.setMomentum(0.6);
			mlp.setTrainingTime(6000);
			mlp.setHiddenLayers("3");
						
			 
			// Build classifier
			int testSize = data.numInstances() - trainSize;
			//Instances trainSet = new Instances(data,0,trainSize);
			//Instances testSet = new Instances(data, trainSize, testSize);
			trainSet = new Instances(data,0,trainSize);
			testSet = new Instances(data, trainSize, testSize);
			 
			
			mlp.buildClassifier(trainSet);
			System.out.println(mlp);
			textArea.append("\n" + mlp.toString());
								
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	protected void testNavieBayes() {
		// TODO Auto-generated method stub
		for(int i=0;i<testSet.numInstances();i++)
	    {
	          double value;
			try {
				value = nbclassifier.classifyInstance(testSet.instance(i));
				String prediction=testSet.classAttribute().value((int)value); 
		        System.out.println(testSet.instance(i)+"............Prediction.......... "+prediction);
		        textArea_1.append(testSet.instance(i)+"............Prediction.......... "+prediction+"\n");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	          
	    }
		
	}

	protected void evaluateNavieBayes() {
		// TODO Auto-generated method stub
		try {
			evaluation = new Evaluation(trainSet);
		
			evaluation.evaluateModel(nbclassifier, testSet);
		 
			// We can check some statistics and create Prediction objects
			System.out.println(evaluation.toSummaryString());
			fnr = evaluation.falseNegativeRate(1);
			System.out.println("False negative rate: " + fnr);
			fpr = evaluation.falsePositiveRate(1);
			System.out.println("False possitive rate: " + fpr);
			tnr = evaluation.trueNegativeRate(1);
			System.out.println("True negative rate: " + tnr);
			recall = evaluation.truePositiveRate(1);
			System.out.println("True positive rate(Recall): " + recall);
			accuracy = (evaluation.numTruePositives(1)+evaluation.numTrueNegatives(1))/(evaluation.numTruePositives(1)+evaluation.numTrueNegatives(1)+evaluation.numFalsePositives(1)+evaluation.numFalseNegatives(1));
			System.out.println("Accuracy: " + accuracy );
			ppv = evaluation.numTruePositives(1)/(evaluation.numTruePositives(1)+evaluation.numFalsePositives(1));
			System.out.println("Positive Predictive Value(Precision): " + ppv );
			System.out.println(evaluation.toMatrixString()); // Confusion matrix
			
			
            model.addRow(new Object[]{"Naive Bayes Simple", accuracy, ppv, recall, tnr, fpr, fnr});
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	protected void trainNavieBayes() {
		// TODO Auto-generated method stub
		try {
			// First we need to load our data into Instances object
			
			/*Dataset dataset = FileHandler.loadDataset(new File("playtennis/thyroid0387.data"), 4, ",");
			Instances data = new Instances((Reader) dataset);	*/
			
			/*DataSource source = new DataSource("playtennis/heartdiesease.arff");
			Instances data = source.getDataSet();*/
			
			CSVLoader loader = new CSVLoader();
			//loader.setSource(new File("playtennis/diabetes.csv"));
			loader.setSource(new File(fileID));
		    Instances data = loader.getDataSet();
					
			// We can check some statistics
			/*System.out.println("Number of data instances " + data.numInstances());
			System.out.println("Number of data attributes " + data.numAttributes());*/
			textArea.setText("Number of data instances " + data.numInstances() + "\n"+ "Number of attributes " + data.numAttributes() + "\n");
					
			// Delete the id column
			data.deleteAttributeAt(0);
					
			// Change numeric format of Class attribute into actual values
			NumericToNominal convert = new NumericToNominal();
			String[] options = new String[2];
			options[0] = "-R";   //replace
			//options[1] = "8"; // range of variables, here we start counting from 1!
			options[1] = numberofAttributes; // range of variables, here we start counting from 1!
			
			
			convert.setOptions(options);
			convert.setInputFormat(data);
			data = Filter.useFilter(data, convert); // Run the conversion
				
			// We need to tell Weka explicitly which column is our class column
			data.setClassIndex(data.numAttributes() - 1);
			
			
			
			//System.out.println("Number of classes " +  data.numClasses());
			textArea.append("Number of classes " +  data.numClasses());
					
			// Define train set size
			//int trainSize = (int) Math.round(data.numInstances() * 0.8);
			splitRatio = Float.parseFloat(JOptionPane.showInputDialog("Enter Dataset Split Ratio as 0.9, 0.8, 0.6, 0.5, 0.3"));
			int trainSize = (int) Math.round(data.numInstances() * splitRatio);
			 
			// Create NavieBayesSimple classifier
			nbclassifier = new NaiveBayesSimple();
						
			 
			// Build classifier
			int testSize = data.numInstances() - trainSize;
			//Instances trainSet = new Instances(data,0,trainSize);
			//Instances testSet = new Instances(data, trainSize, testSize);
			trainSet = new Instances(data,0,trainSize);
			testSet = new Instances(data, trainSize, testSize);
			 
			
			nbclassifier.buildClassifier(trainSet);
			System.out.println(nbclassifier);
			textArea.append("\n" + nbclassifier.toString());
								
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	protected void barGraph(String classifier, double accuracy, double precision, double recall, double tnr, double fpr, double fnr) {
		// TODO Auto-generated method stub
		DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
		dataset.setValue(accuracy, "Percentage", "Accuracy");
		dataset.setValue(precision, "Percentage", "Precision(PPV)");
		dataset.setValue(recall, "Percentage", "Recall(TPR)");
		dataset.setValue(tnr, "Percentage", "TNR");
		dataset.setValue(fpr, "Percentage", "FPR");
		dataset.setValue(fnr, "Percentage", "FNR");
		
		JFreeChart jfreeChart = ChartFactory.createBarChart(classifier, "Performance Measures", "Percentage", dataset, PlotOrientation.VERTICAL, true, true, false);
		CategoryPlot plot = jfreeChart.getCategoryPlot();
		plot.setRangeGridlinePaint(Color.BLACK);
		
		ChartFrame chartFrame = new ChartFrame(classifier, jfreeChart, true);
		chartFrame.setSize(500, 400);
		chartFrame.setVisible(true);
	}

	protected void testTreeJ48() {
		// TODO Auto-generated method stub
		for(int i=0;i<testSet.numInstances();i++)
	    {
	          double value;
			try {
				value = tree.classifyInstance(testSet.instance(i));
				String prediction=testSet.classAttribute().value((int)value); 
		        System.out.println(testSet.instance(i)+"............Prediction.......... "+prediction);
		        textArea_1.append(testSet.instance(i)+"............Prediction.......... "+prediction+"\n");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	          
	    }
	}

	protected void evaluateTreeJ48() {
		// TODO Auto-generated method stub
		//Evaluation evaluation = new Evaluation(trainSet);
		try {
			evaluation = new Evaluation(trainSet);
		
			evaluation.evaluateModel(tree, testSet);
		 
			// We can check some statistics and create Prediction objects
			System.out.println(evaluation.toSummaryString());
			fnr = evaluation.falseNegativeRate(1);
			System.out.println("False negative rate: " + fnr);
			fpr = evaluation.falsePositiveRate(1);
			System.out.println("False possitive rate: " + fpr);
			tnr = evaluation.trueNegativeRate(1);
			System.out.println("True negative rate: " + tnr);
			recall = evaluation.truePositiveRate(1);
			System.out.println("True positive rate(Recall): " + recall);
			accuracy = (evaluation.numTruePositives(1)+evaluation.numTrueNegatives(1))/(evaluation.numTruePositives(1)+evaluation.numTrueNegatives(1)+evaluation.numFalsePositives(1)+evaluation.numFalseNegatives(1));
			System.out.println("Accuracy: " + accuracy );
			ppv = evaluation.numTruePositives(1)/(evaluation.numTruePositives(1)+evaluation.numFalsePositives(1));
			System.out.println("Positive Predictive Value(Precision): " + ppv );
			System.out.println(evaluation.toMatrixString()); // Confusion matrix
			
			
            model.addRow(new Object[]{"C4.5 (J48) Decision Tree", accuracy, ppv, recall, tnr, fpr, fnr});
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void trainTreeJ48() {
		// TODO Auto-generated method stub
		try {
			// First we need to load our data into Instances object
			
			/*Dataset dataset = FileHandler.loadDataset(new File("playtennis/thyroid0387.data"), 4, ",");
			Instances data = new Instances((Reader) dataset);	*/
			
			/*DataSource source = new DataSource("playtennis/heartdiesease.arff");
			Instances data = source.getDataSet();*/
			
			CSVLoader loader = new CSVLoader();
			//loader.setSource(new File("playtennis/diabetes.csv"));
			loader.setSource(new File(fileID));
		    Instances data = loader.getDataSet();
					
			// We can check some statistics
			/*System.out.println("Number of data instances " + data.numInstances());
			System.out.println("Number of data attributes " + data.numAttributes());*/
			textArea.setText("Number of data instances " + data.numInstances() + "\n"+ "Number of attributes " + data.numAttributes() + "\n");
					
			// Delete the id column
			data.deleteAttributeAt(0);
					
			// Change numeric format of Class attribute into actual values
			NumericToNominal convert = new NumericToNominal();
			String[] options = new String[2];
			options[0] = "-R";   //replace
			//options[1] = "8"; // range of variables, here we start counting from 1!
			options[1] = numberofAttributes; // range of variables, here we start counting from 1!
			
			
			convert.setOptions(options);
			convert.setInputFormat(data);
			data = Filter.useFilter(data, convert); // Run the conversion
			
			// We need to tell Weka explicitly which column is our class column
			data.setClassIndex(data.numAttributes() - 1);
			
			
			
			//System.out.println("Number of classes " +  data.numClasses());
			textArea.append("Number of classes " +  data.numClasses());
					
			// Define train set size
			//int trainSize = (int) Math.round(data.numInstances() * 0.8);
			splitRatio = Float.parseFloat(JOptionPane.showInputDialog("Enter Dataset Split Ratio as 0.9, 0.8, 0.6, 0.5, 0.3"));
			int trainSize = (int) Math.round(data.numInstances() * splitRatio);
			 
			// Create an unprunned J48 tree classifier
			// Note: “J” for Java, 48 for C4.8 algorithm, hence the J48 name
			//J48 tree = new J48();
			tree = new J48();
			//tree.setUnpruned(true);
			tree.setUnpruned(false);
			
			 
			// Build classifier
			int testSize = data.numInstances() - trainSize;
			//Instances trainSet = new Instances(data,0,trainSize);
			//Instances testSet = new Instances(data, trainSize, testSize);
			trainSet = new Instances(data,0,trainSize);
			testSet = new Instances(data, trainSize, testSize);
			 
			
				tree.buildClassifier(trainSet);
				System.out.println(tree);
				textArea.append("\n" + tree.toString());
								
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
}
