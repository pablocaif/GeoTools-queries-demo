package org.geotools.example;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

import java.net.URL;

/**
 * @author Pablo Caif | Shine Technologies
 */
public class GeoToolsQueries {

    private FileDataStore dataStore;
    private SimpleFeatureSource featureSource;

    //Constructor
    public GeoToolsQueries()  {
        try {
            initDataStore();
            initFeatureSource();
        } catch (Exception ex) {
           throw new IllegalStateException("Error initializing Geotools queries", ex);
        }
    }

    //Geospatial queries
    public SimpleFeatureCollection findFeaturesBySuburb() throws Exception {
        Filter filter = ECQL.toFilter("SA2_NAME11 = 'Melbourne'");
        SimpleFeatureCollection result =  featureSource.getFeatures(filter);
        printFeatureCollection(result);

        return result;
    }

    public SimpleFeatureCollection findFeaturesGreaterThanArea() throws Exception {
        Filter filter = ECQL.toFilter("ALBERS_SQM > 1400000000");
        SimpleFeatureCollection result =  featureSource.getFeatures(filter);
        printFeatureCollection(result);

        return result;
    }

    public SimpleFeatureCollection findFeaturesContainingPoint() throws Exception {
        Filter filter = ECQL.toFilter("CONTAINS (the_geom, POINT(146.38582171 -39.01418414))");
        SimpleFeatureCollection result =  featureSource.getFeatures(filter);
        printFeatureCollection(result);

        return result;
    }

    public SimpleFeatureCollection findFeaturesIntersectingBindingBox() throws Exception {
        //                                            Minimum x,y point       Maximum x,y point
        Filter filter = ECQL.toFilter("BBOX(the_geom, 145.044937, -37.903511, 145.079613, -37.938448)");
        SimpleFeatureCollection result =  featureSource.getFeatures(filter);
        printFeatureCollection(result);

        return result;
    }

    private void printFeatureCollection(SimpleFeatureCollection result) {
        SimpleFeatureIterator featureIterator = result.features();

        while (featureIterator.hasNext()) {
            SimpleFeature feature = featureIterator.next();

            System.out.printf("Polygon found  SA1_MAIN11=%s, SA2_NAME11=%s, SA3_NAME11=%s, STE_NAME11=%s, ALBERS_SQM=%s\n",
                    feature.getAttribute("SA1_MAIN11"),
                    feature.getAttribute("SA2_NAME11"),
                    feature.getAttribute("SA3_NAME11"),
                    feature.getAttribute("STE_NAME11"),
                    feature.getAttribute("ALBERS_SQM")
            );
        }
        featureIterator.close();
    }

    //Connect to the Shapefiles
    private void initDataStore() throws Exception{
        ClassLoader classLoader = GeoToolsQueries.class.getClassLoader();
        //We load the files from the ABS
        URL fileUrl = classLoader.getResource("Victoria ABS SHP/MB_2011_VIC.shp");

        dataStore = FileDataStoreFinder.getDataStore(fileUrl);
    }

    private void initFeatureSource() throws Exception {
        featureSource = dataStore.getFeatureSource();
    }


    public SimpleFeatureSource getFeatureSource() {
        return featureSource;
    }
}
