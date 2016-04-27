package bot;

import java.util.HashMap;

/**
 * Contains all bot problems.
 */
public enum Problem 
{
	NO_CONNECTION,
	NEED_CAPTCHA,
	NEED_LOGIN_DATA,
	CONFIRM_PHONE,
	UNKNOWN;
	
	static final HashMap<Problem, String> problems = new HashMap <Problem, String> ()
			{
				private static final long serialVersionUID = -4225278749001973390L;
				{
					put (Problem.NO_CONNECTION, Bot.resources.getString("Bot.problem.noConnection"));
					put (Problem.NEED_CAPTCHA, Bot.resources.getString("Bot.problem.captcha"));
					put (Problem.NEED_LOGIN_DATA, Bot.resources.getString("Bot.problem.invalidData"));
					put (Problem.CONFIRM_PHONE, Bot.resources.getString("Bot.problem.confirmPhone"));
					put (Problem.UNKNOWN, Bot.resources.getString("Bot.problem.unknown"));
				}
			};
}
