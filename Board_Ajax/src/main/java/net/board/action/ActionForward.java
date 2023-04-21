package net.board.action;

class ActionForward {
	
	private boolean redirect ;
	private String path;
	 
	
	public ActionForward() {
		
	}
	
	

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}



	public boolean isRedirect() {
		return redirect;
	}



	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}
	

	
	
	
}
