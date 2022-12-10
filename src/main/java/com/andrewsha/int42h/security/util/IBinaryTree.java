package com.andrewsha.int42h.security.util;

import java.io.Serializable;
import java.util.Map;

public interface IBinaryTree {
	public Serializable getId();

	public Map.Entry<String, Serializable> getRoot();

	public Map.Entry<String, Serializable> getParent();
}
