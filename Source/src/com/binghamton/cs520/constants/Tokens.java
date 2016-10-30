package com.binghamton.cs520.constants;

public enum Tokens {
	SPACE(" ");
	
	private String string;
	
	Tokens(String string) {
        this.string = string;
    }

    public String getToken() {
        return string;
    }
	
}
