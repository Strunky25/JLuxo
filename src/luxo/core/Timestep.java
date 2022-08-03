package luxo.core;


public class Timestep {
    
    private float time;
    
    public Timestep() { time = 0; }
    public Timestep(float time) { this.time = time; }
    
    public float getSeconds() { return time; }
    public float getMilliseconds() { return time * 1000; }
}
