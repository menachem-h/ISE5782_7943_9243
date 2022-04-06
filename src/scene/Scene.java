package scene;

import lighting.AmbientLight;
import geometries.Geometries;
import primitives.Color;

/**
 * Graphical scene
 */
public class Scene {

    /**
     * name of scene
     */
    private final String name;

    /**
     * background color of scene
     */
    private final Color background;

    /**
     * basic ambient lighting of scene
     */
    private final AmbientLight ambientLight;

    /**
     * collection of graphical geometric shapes in the scene
     */
    private final Geometries geometries;


    /**
     * constructor (uses builder pattern)
     * @param builder {@link SceneBuilder} inner class instance
     */
    private Scene(SceneBuilder builder){
        name = builder.name;
        background = builder.background;
        geometries = builder.geometries;
        ambientLight = builder.ambientLight;
    }
    /**
     * getter for name field
     * @return name of scene
     */
    public String getName() {
        return name;
    }

    /**
     * getter for background field
     * @return basic background {@link  Color} of scene
     */
    public Color getBackground() {
        return background;
    }

    /**
     * getter for ambientLight field
     * @return basic {@link  AmbientLight} of scene
     */
    public AmbientLight getAmbientLight() {
        return ambientLight;
    }

    /**
     * getter for geometries field
     * @return collection of {@link Geometries} in scene
     */
    public Geometries getGeometries() {
        return geometries;
    }


    /**
     * inner class, responsible to create new instances of Scene objects
     * implements builder pattern
     */
    public static class SceneBuilder {

        // builder class fields are set to default values at definition

        /**
         * name of scene
         */
        private final String name;

        /**
         * background color of scene
         */
        private  Color background = Color.BLACK;

        /**
         * basic ambient lighting of scene
         */
        private  AmbientLight ambientLight = new AmbientLight();

        /**
         * collection of graphical geometric shapes in the scene
         */
        private Geometries geometries = new Geometries();

        /**
         * constructor (only name parameter is mandatory at instantiation)
         * @param name name of scene
         */
        public SceneBuilder(String name) {
            this.name = name;
        }

        // chaining methods for builder pattern

        /**
         * setter for background color of scene
         * @param background {@link Color} of background
         * @return this {@link SceneBuilder} instance
         */
        public SceneBuilder setBackground(Color background) {
            this.background = background;
            return this;
        }

        /**
         * setter for ambient lighting of scene
         * @param ambientLight {@link AmbientLight} of scene
         * @return this {@link SceneBuilder} instance
         */
        public SceneBuilder setAmbientLight(AmbientLight ambientLight) {
            this.ambientLight = ambientLight;
            return this;
        }

        /**
         * setter for geometrical shapes in scene
         * @param geometries collection of {@link  Geometries}
         * @return this {@link SceneBuilder} instance
         */
        public SceneBuilder setGeometries(Geometries geometries) {
            this.geometries = geometries;
            return this;
        }

        /**
         * build function for builder pattern - initializes new Scene object
         * with builder object
         * @return new {@link Scene} object
         */
        public Scene build(){
            Scene scene = new Scene(this);
            return scene;
        }
    }


}
