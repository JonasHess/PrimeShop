package eu.Blockup.PrimeShop.ChestShop;

import java.util.List;


public class Chest_Page {

		
		public List<Item_Supply> listOfItems;
		public int index_of_this_page;
		public int max_count_of_pages;
		public static int amount_of_items_fitting__in_one_page = 26;
		
		
		public Chest_Page(List<Item_Supply> listOfItems, int index_of_this_page, int max_count_of_pages) {
			super();
			this.listOfItems = listOfItems;
			this.index_of_this_page = index_of_this_page;
			this.max_count_of_pages = max_count_of_pages;
		}
	}
