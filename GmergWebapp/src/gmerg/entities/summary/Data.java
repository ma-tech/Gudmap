package gmerg.entities.summary;

public class Data {
    	String text;
    	String value;
    	
    	public Data(String text, String value) {
    		this.text = text;
    		this.value = value;
    	}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
    	
    	
}
