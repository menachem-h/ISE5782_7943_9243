package scene;

import lighting.AmbientLight;
import geometries.Geometries;
import primitives.Color;

public class Scene {

    private final String name;

    private final Color background;
    private final AmbientLight ambientLight;
    private final Geometries geometries;

    public String getName() {
        return name;
    }

    public Color getBackground() {
        return background;
    }

    public AmbientLight getAmbientLight() {
        return ambientLight;
    }

    public Geometries getGeometries() {
        return geometries;
    }

    private Scene(SceneBuilder builder){
        name = builder.name;
        background = builder.background;
        geometries = builder.geometries;
        ambientLight = builder.ambientLight;
    }

    public static class SceneBuilder {

        private final String name;
        private Color background = Color.BLACK;
        private AmbientLight ambientLight = new AmbientLight();
        private Geometries geometries = new Geometries();

        public SceneBuilder(String name) {
            this.name = name;
        }


        public SceneBuilder setBackground(Color background) {
            this.background = background;
            return this;
        }

        public SceneBuilder setAmbientLight(AmbientLight ambientLight) {
            this.ambientLight = ambientLight;
            return this;
        }

        public SceneBuilder setGeometries(Geometries geometries) {
            this.geometries = geometries;
            return this;
        }

        public Scene build(){
            Scene scene = new Scene(this);
            return scene;
        }
    }


}
