package com.fachri.mapsdirection;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directions.v5.MapboxDirections;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.api.directions.v5.models.DirectionsRoute;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.services.Constants.PRECISION_6;

/**
 * Created by fachrifebrian on 6/9/17.
 */

public class MapUtil {
    public static void getRoute(List<Position> positions, final PolylineRoute listener) {
        MapboxDirections client = new MapboxDirections.Builder()
                .setCoordinates(positions)
                .setOverview(DirectionsCriteria.OVERVIEW_FULL)
                .setProfile(DirectionsCriteria.PROFILE_DRIVING)
                .setAccessToken("pk.eyJ1IjoiZmFjaHJpZmF1bCIsImEiOiJjajFidjNobzcwMDRqMzJwZnF2djR5Ym5jIn0.CdJvUAb3FBCUynFb1YmizA")
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                System.out.println(call.request().url().toString());

                // You can get the generic HTTP info about the response
                Timber.d("Response code: " + response.code());
                if (response.body() == null) {
                    Timber.e("No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().getRoutes().size() < 1) {
                    Timber.e("No routes found");
                    return;
                }

                // Print some info about the route
                DirectionsRoute currentRoute = response.body().getRoutes().get(0);
                Timber.d("Distance: " + currentRoute.getDistance() + " Meter");

                // Draw the route on the map
                PolylineOptions polylineOptions = new PolylineOptions()
                        .add(drawRoute(currentRoute))
                        .color(Color.parseColor("#009688"))
                        .width(5);

                listener.route(polylineOptions);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Timber.e("Error: " + throwable.getMessage());

            }
        });
    }

    private static LatLng[] drawRoute(DirectionsRoute route) {
        // Convert LineString coordinates into LatLng[]
        LineString lineString = LineString.fromPolyline(route.getGeometry(), PRECISION_6);
        List<Position> coordinates = lineString.getCoordinates();
        LatLng[] points = new LatLng[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            points[i] = new LatLng(coordinates.get(i).getLatitude(), coordinates.get(i).getLongitude());
        }
        return points;
    }

    public interface PolylineRoute {
        void route(PolylineOptions lineOptions);
    }

}
