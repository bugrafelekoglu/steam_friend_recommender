public class Game {
	
	private int app_id;
	private int playtime;
	
	public Game(int app_id, int playtime) {
		this.app_id = app_id;
		this.playtime = playtime;
	}
	
	public int getAppID() {
		return app_id;
	}
	
	public int getPlaytime() {
		return playtime;
	}
	
	public void setAppID(int app_id) {
		this.app_id = app_id;
	}
	
	public void setPlaytime(int playtime) {
		this.playtime = playtime;
	}
}
