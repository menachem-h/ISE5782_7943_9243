package scene;

import elelments.AmbientLight;
import geometries.Geometries;
import primitives.Color;

public class Scene {

    private final String _name;

    private final Color _background ;
    private final AmbientLight _ambientLight ;
    private final Geometries _geometries ;

    public String get_name() {
        return _name;
    }

    public Color get_background() {
        return _background;
    }

    public AmbientLight get_ambientLight() {
        return _ambientLight;
    }

    public Geometries get_geometries() {
        return _geometries;
    }

    private Scene(SceneBuilder builder){
        _name = builder._name;
        _background = builder._background;
        _geometries = builder._geometries;
        _ambientLight = builder._ambientLight;
    }

    public static class SceneBuilder {

        private final String _name;
        private Color _background = Color.BLACK;
        private AmbientLight _ambientLight = new AmbientLight();
        private Geometries _geometries = new Geometries();

        public SceneBuilder(String name) {
            _name = name;
        }


        public SceneBuilder set_background(Color _background) {
            this._background = _background;
            return this;
        }

        public SceneBuilder set_ambientLight(AmbientLight _ambientLight) {
            this._ambientLight = _ambientLight;
            return this;
        }

        public SceneBuilder set_geometries(Geometries _geometries) {
            this._geometries = _geometries;
            return this;
        }

        public Scene build(){
            Scene scene = new Scene(this);
            return scene;
        }
    }


}
