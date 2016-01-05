package engine;

public class StringsRU extends Strings{
	protected String TITLE = "Gameloft 111";
	protected String VERSION = "v0.9";
	protected String AUTHOR = "Лазарев Сергей";
	protected String YEAR = "2016";
	protected String LANGUAGES_LBL_TEXT = "Язык";
	protected String LANGUAGES_LBL_ENG = "Английский";
	protected String LANGUAGES_LBL_RUS = "Русский";
	protected String CONTINUE_BTN_TEXT = "ПРОДОЛЖИТЬ";
	protected String OPTIONS_BTN_TEXT = "НАСТРОЙКИ";
	protected String START_BTN_TEXT = "НОВАЯ ИГРА";
	protected String NEXT_BTN_TEXT = "ДАЛЕЕ";
	protected String EXIT_BTN_TEXT = "ВЫХОД";
	protected String BACK_BTN_TEXT = "НАЗАД";
	protected String APPLY_BTN_TEXT = "ПРИНЯТЬ";
	protected String SCORE_LBL_TEXT = "ОЧКИ";
	protected String DEFAULT_PLAYER_NAME = "ИГРОК";
	protected String CHAIN_COMBO_STRING = "ККККОМБО";
	protected String GAME_SPEED_LBL = "Скорость игры";
	protected String GAME_OVER_MSG = "Конец игры \n Ваш счет ";
	protected String[] GAME_SPEED = {"Нормальная", "Медленная"};
	
	public String getTitle() { return TITLE;}
	public String getVersion() { return VERSION;}
	public String getAuthor() {return AUTHOR;}
	public String getYear() {return YEAR;}
	public String getLanguagesLblText() {return LANGUAGES_LBL_TEXT;}
	public String getLanguagesLblEng() {return LANGUAGES_LBL_ENG;}
	public String getLanguagesLblRus() {return LANGUAGES_LBL_RUS;}
	public String getContinueBtnText() {return CONTINUE_BTN_TEXT;}
	public String getStartBtnText() { return START_BTN_TEXT;}
	public String getOptionsBtnText() { return OPTIONS_BTN_TEXT;}
	public String getNextBtnText() {return NEXT_BTN_TEXT;}
	public String getExitBtnText() {return EXIT_BTN_TEXT;}
	public String getBackBtnText() {return BACK_BTN_TEXT;}
	public String getApplyBtnText() {return APPLY_BTN_TEXT;}
	public String getScoreLblText() {return SCORE_LBL_TEXT;}
	public String getDefaultPlayerName() {return DEFAULT_PLAYER_NAME;}
	public String getChainComboString() {return CHAIN_COMBO_STRING;}
	public String getGameSpeedLblText() {return GAME_SPEED_LBL;}
	public String getGameOverMsg() {return GAME_OVER_MSG;}
	public String[] getGameSpeedStrings() {return GAME_SPEED;}
}
