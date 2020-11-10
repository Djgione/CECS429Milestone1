package cecs429.weights;

import java.util.List;
import java.util.Map;

public class DocumentValuesModel {
	private Map<Integer, Map<String, Integer>> map;
	private List<Long> byteSizes;
	private List<Integer> docLengths;
	private List<Double> docAverageTFDs;
	private List<Double> docWeights;
	
	public DocumentValuesModel()
	{
		
	}
	
	public DocumentValuesModel(Map<Integer, Map<String, Integer>> map, List<Long> byteSizes,
			List<Integer> docLengths) {
		super();
		this.map = map;
		this.byteSizes = byteSizes;
		this.docLengths = docLengths;
	}

	public DocumentValuesModel(Map<Integer, Map<String, Integer>> map, List<Long> byteSizes,
			List<Integer> docLengths, List<Double> docAverageTFDs, List<Double> docWeights) {
		super();
		this.map = map;
		this.byteSizes = byteSizes;
		this.docLengths = docLengths;
		this.docAverageTFDs = docAverageTFDs;
		this.docWeights = docWeights;
	}
	
	

	public DocumentValuesModel(List<Long> byteSizes, List<Integer> docLengths, List<Double> docAverageTFDs,
			List<Double> docWeights) {
		super();
		this.byteSizes = byteSizes;
		this.docLengths = docLengths;
		this.docAverageTFDs = docAverageTFDs;
		this.docWeights = docWeights;
	}

	public Map<Integer, Map<String, Integer>> getMap() {
		return map;
	}

	public void setMap(Map<Integer, Map<String, Integer>> map) {
		this.map = map;
	}

	public List<Long> getByteSizes() {
		return byteSizes;
	}

	public void setByteSizes(List<Long> byteSizes) {
		this.byteSizes = byteSizes;
	}

	public List<Integer> getDocLengths() {
		return docLengths;
	}

	public void setDocLengths(List<Integer> docLengths) {
		this.docLengths = docLengths;
	}
	
	public void setDocAverageTFDs(List<Double> ave)
	{
		docAverageTFDs = ave;
	}
	
	public List<Double> getDocAverageTFDs()
	{
		return docAverageTFDs;
	}
	
	public List<Double> getDocWeights()
	{
		return docWeights;
	}
	
	public void setDocWeights(List<Double> docWeights)
	{
		this.docWeights = docWeights;
	}
	
	
	
}
