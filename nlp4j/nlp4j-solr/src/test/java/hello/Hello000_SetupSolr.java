package hello;

public class Hello000_SetupSolr {

	public static void main(String[] args) {

		System.out.println(">docker run -d -p 8983:8983 --name my_solr solr");

		System.out.println(">docker exec -it my_solr bin/solr create -c sandbox");

		System.out.println("$curl -X POST -H 'Content-type:application/json' " //
				+ "--data-binary '{\"add-field-type\":{" //
				+ "\"name\":\"vector1024\"," //
				+ "\"class\":\"solr.DenseVectorField\"," //
				+ "\"vectorDimension\":1024," //
				+ "\"similarityFunction\":\"cosine\"}}' " //
				+ "http://localhost:8983/solr/sandbox/schema");

		System.out.println("$curl -X POST -H 'Content-type:application/json' " //
				+ "--data-binary '{ \"add-field\":{ " //
				+ "\"name\":\"vector\", " //
				+ "\"type\":\"vector1024\", " //
				+ "\"indexed\":true, " //
				+ "\"stored\":true " //
				+ "} }' " //
				+ "http://localhost:8983/solr/sandbox/schema");

	}

}
