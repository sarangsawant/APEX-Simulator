package com.binghamton.cs520.constants;

public enum Tokens {
	SPACE(" "),COLON(":"),REGISTER_PREFIX("R"),LITERAL("literal");
	
	
	private String string;
	
	Tokens(String string) {
        this.string = string;
    }

    public String getToken() {
        return string;
    }
	
}
