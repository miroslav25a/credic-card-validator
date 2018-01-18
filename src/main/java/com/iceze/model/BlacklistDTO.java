package com.iceze.model;

import java.util.List;

public class BlacklistDTO {
		private List<String> blacklist;

		public BlacklistDTO() {
			super();
		}
		
		public BlacklistDTO(final List<String> blacklist) {
			super();
			this.blacklist = blacklist;
		}



		public List<String> getBlacklist() {
			return blacklist;
		}

		public void setBlacklist(final List<String> blacklist) {
			this.blacklist = blacklist;
		}
}
