package location;

public class LocationRange {
	private Location start;
	private Location end;

	public LocationRange() {
		this.start = new Location(0, 0);
		this.end = new Location(0, 0);
	}

	public LocationRange(Location start, Location end) {
		this.start = start;
		this.end = end;
	}

	public LocationRange(LocationRange locationRange) {
		this.start = locationRange.getStart();
		this.end = locationRange.getEnd();
	}

	public Location getStart() {
		return start;
	}

	public void setStart(Location start) {
		this.start = start;
	}

	public void updateStart(int x, int y) {
		this.start.setX(x);
		this.start.setY(y);
	}

	public Location getEnd() {
		return end;
	}

	public void setEnd(Location end) {
		this.end = end;
	}

	public void updateEnd(int x, int y) {
		this.end.setX(x);
		this.end.setY(y);
	}

	public int getNumberOfLines() {
		return this.end.getY() - this.start.getY();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocationRange other = (LocationRange) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "start: " + start + ", end: " + end;
	}
}
