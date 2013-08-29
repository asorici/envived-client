package com.envived.android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.envived.android.GCMIntentService;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class Utils {

	public static long DAY_SCALE = 86400000;
	public static long HOUR_SCALE = 1440000;
	public static long MINUTE_SCALE = 60000;

	public static String calendarToString(Calendar c, String formatPattern) {
		// default is "yyyy-MM-dd HH:mm:ss"
		if (formatPattern == null) {
			formatPattern = "yyyy-MM-dd HH:mm:ss";
		}

		SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
		return sdf.format(c.getTime());
	}

	public static Calendar stringToCalendar(String timestamp,
			String formatPattern) throws ParseException {
		// default is "yyyy-MM-dd'T'HH:mm:ssZ"
		if (formatPattern == null) {
			formatPattern = "yyyy-MM-dd'T'HH:mm:ssZ";
		}

		SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(timestamp));

		return c;
	}

	public static void sendGCMStatusMessage(Context context, String message) {
		Intent intent = new Intent(GCMIntentService.ACTION_DISPLAY_GCM_MESSAGE);
		intent.putExtra(GCMIntentService.EXTRA_GCM_MESSAGE, message);
		context.sendBroadcast(intent);
	}

	// ============================== Version utilities
	// ======================================= //

	/*
	 * @TargetApi(11) public static void enableStrictMode() { if
	 * (Utils.hasGingerbread()) { StrictMode.ThreadPolicy.Builder
	 * threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder() .detectAll()
	 * .penaltyLog(); StrictMode.VmPolicy.Builder vmPolicyBuilder = new
	 * StrictMode.VmPolicy.Builder() .detectAll() .penaltyLog();
	 * 
	 * if (Utils.hasHoneycomb()) { threadPolicyBuilder.penaltyFlashScreen();
	 * vmPolicyBuilder .setClassInstanceLimit(ImageGridActivity.class, 1)
	 * .setClassInstanceLimit(ImageDetailActivity.class, 1); }
	 * StrictMode.setThreadPolicy(threadPolicyBuilder.build());
	 * StrictMode.setVmPolicy(vmPolicyBuilder.build()); } }
	 */

	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	public static String[] suggestedWords = { "wicca", "atheist-agnostic",
			"buddhism", "catholic", "christianity", "hinduism", "judaism",
			"islam", "mormon", "orthodox", "paganism", "protestant",
			"religion", "scientology", "spirituality", "sufism", "sunni",
			"vintage-cars", "billiards", "board-games", "car-parts",
			"card-games", "cars", "chess", "collecting", "cigars", "crafts",
			"crochet", "dolls-puppets", "gambling", "guns", "humor",
			"knitting", "magic-illusions", "memorabilia", "motorcycles",
			"poker", "photo-gear", "puzzles", "quilting", "quizzes",
			"roleplaying-games", "satire", "scrapbooking", "sewing", "usa",
			"africa", "asia", "australia", "brazil", "canada", "caribbean",
			"central-america", "china", "europe", "france", "germany", "india",
			"ireland", "israel", "italy", "japan", "korea", "middle-east",
			"mexico", "netherlands", "new-york", "oceania", "russia",
			"south-america", "spain", "uk", "yoga", "aids", "aging",
			"alternative-health", "anatomy", "arthritis", "asthma", "beauty",
			"bodybuilding", "brain-disorders", "cancer", "dentistry",
			"diabetes", "disabilities", "doctors-surgeons", "eating-disorders",
			"ergonomics", "fitness", "forensics", "glaucoma", "health",
			"heart-conditions", "learning-disorders", "medical-science",
			"mental-health", "nursing", "nutrition", "physical-therapy",
			"psychiatry", "self-improvement", "sexual-health",
			"substance-abuse", "spas", "weight-loss", "wine",
			"alcoholic-drinks", "antiques", "babies", "beer", "beverages",
			"birds", "cats", "coffee", "divorce", "dogs",
			"entertaining-guests", "exotic-pets", "family", "fish",
			"food-cooking", "for-kids", "gardening", "genealogy",
			"homebrewing", "home-improvement", "homemaking", "homeschooling",
			"landscaping", "kids", "married-life", "parenting", "pets",
			"pregnancy-birth", "relationships", "restoration", "restaurants",
			"scouting", "teen-parenting", "tea", "teen-life", "weddings",
			"vegetarian", "skydiving", "agriculture", "animals",
			"bird-watching", "boating", "camping", "canoeing-kayaking",
			"climbing", "fishing", "flyfishing", "forestry", "hiking",
			"nature", "outdoors", "running", "scuba-diving", "womens-issues",
			"african-americans", "activism", "anarchism", "astrology-psychics",
			"biographies", "babes", "bisexual-culture", "bizarre-oddities",
			"career-planning", "celebrities", "christmas", "communism",
			"counterculture", "culture-ethnicity", "crime",
			"conservative-politics", "conspiracies", "continuing-education",
			"drugs", "dating-tips", "education", "feminism", "gay-culture",
			"goth-culture", "government", "hedonism", "intl-development",
			"hotels", "humanitarianism", "iraq", "law", "lefthanded",
			"lesbian-culture", "liberal-politics", "liberties-rights",
			"matchmaking", "military", "mens-issues", "native-americans",
			"new-age", "news(general)", "nonprofit-charity", "nightlife",
			"personal-sites", "paranormal", "politics", "rave-culture",
			"senior-citizens", "socialism", "shopping", "stumblers",
			"subculture", "survivalist", "tattoos-piercing", "travel",
			"terrorism", "ufos", "university-college", "writing",
			"alternative-news", "advertising", "american-literature",
			"animation", "british-literature", "books", "cartoons",
			"childrens-books", "comic-books", "fantasy-books", "journalism",
			"library-resources", "literature", "mystery-novels", "poetry",
			"radio-broadcasts", "romance-novels", "science-fiction",
			"soap-operas", "shakespeare", "television", "video-equipment",
			"vocal-music", "alternative-rock", "ambient-music",
			"action-movies", "audio-equipment", "blues-music", "britpop",
			"celtic-music", "christian-music", "classic-rock", "classic-films",
			"classical-music", "comedy-movies", "country-music", "cult-films",
			"djs-mixing", "dance-music", "drama-movies", "disco", "drumnbass",
			"electronica-idm", "ethnic-music", "film-noir", "folk-music",
			"filmmaking", "foreign-films", "funk", "gospel-music",
			"hiphop-rap", "guitar", "heavy-metal", "horror-movies",
			"indie-rock-pop", "house-music", "independent-film",
			"industrial-music", "ipod", "karaoke", "jazz", "latin-music",
			"lounge-music", "movies", "music", "music-instruments",
			"music-theory", "musicals", "musician-resources", "oldies-music",
			"opera", "percussion", "pop-music", "recording-gear", "punk-rock",
			"reggae", "rock-music", "soul-r&amp;b", "soundtracks", "techno",
			"trance", "triphop-downtempo", "zoology", "a-i-", "amateur-radio",
			"alternative-energy", "astronomy", "anthropology", "antiaging",
			"archaeology", "aviation-aerospace", "biology", "biomechanics",
			"biotech", "botany", "chemistry", "chaos-complexity",
			"chemical-eng-", "cognitive-science", "civil-engineering",
			"computer-science", "ecology", "economics", "electrical-eng-",
			"electronic-parts", "environment", "evolution", "genetics",
			"futurism", "geoscience", "gadgets", "geography", "kinesiology",
			"linguistics", "machinery", "marine-biology", "mathematics",
			"mechanical-eng-", "meteorology", "microbiology",
			"mining-metallurgy", "neuroscience", "nanotech", "nuclear-science",
			"paleontology", "physics", "physiology", "pharmacology",
			"political-science", "psychology", "research", "robotics",
			"science", "semiconductors", "sociology", "statistics",
			"space-exploration", "trains-railroads", "technology",
			"transportation", "virtual-reality", "windows-dev", "c-a-d-",
			"computer-graphics", "computer-hardware", "cyberculture",
			"computer-security", "computers", "databases", "encryption",
			"embedded-systems", "facebook", "firefox", "forums", "hacking",
			"instant-messaging", "it", "internet", "internet-tools", "java",
			"macos", "linux-unix", "mobile-computing", "multimedia",
			"network-security", "operating-systems", "open-source", "php",
			"online-games", "p2p", "peripheral-devices", "perl", "programming",
			"proxy", "search", "shareware", "stumbleupon", "software",
			"supercomputing", "webhosting", "web-development", "video-games",
			"windows", "weblogs", "telecom", "accounting", "banking",
			"bargains-coupons", "business", "cell-phones", "capitalism",
			"clothing", "consumer-info", "construction", "daytrading",
			"ecommerce", "electronic-devices", "entrepreneurship",
			"energy-industry", "financial-planning", "home-business",
			"insurance", "investing", "jewelry", "luxury", "management-hr",
			"manufacturing", "marketing", "mutual-funds", "options-futures",
			"petroleum", "real-estate", "seo", "taxation", "toys",
			"woodworking", "acting", "american-history", "arts",
			"ancient-history", "anime", "art-history", "architecture",
			"ballet", "classical-studies", "cold-war", "dancing", "design",
			"desktop-publishing", "drawing", "eastern-studies", "ethics",
			"fashion", "fine-arts", "history", "graphic-design", "humanities",
			"industrial-design", "interior-design", "logic", "live-theatre",
			"medieval-history", "music-composition", "mythology", "painting",
			"performing-arts", "philosophy", "photoshop", "photography",
			"postmodernism", "quotes", "sculpting", "songwriting", "wrestling",
			"american-football", "baseball", "badminton", "basketball",
			"bicycling", "bowling", "boxing", "cheerleading", "cricket",
			"equestrian-horses", "extreme-sports", "figure-skating", "golf",
			"hockey", "gymnastics", "hunting", "martial-arts", "motor-sports",
			"racquetball", "rodeo", "sailing", "rugby", "soccer",
			"skateboarding", "skiing", "snowboarding", "sports(general)",
			"squash", "surfing", "swimming", "track-field", "tennis",
			"volleyball", "water-sports", "windsurfing" };
}
