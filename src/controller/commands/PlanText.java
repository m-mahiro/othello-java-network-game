package controller.commands;

public final class PlanText implements Command {

	public static final CommandType type = CommandType.BASIC;
	public final String content;

	private static final int headerSize = 1;

	public PlanText(String content) {
		this.content = content;
	}

	public static PlanText parse(String text) {
		CommandType type;
		String content;

		String[] args = text.split(" ");

		// メッセージタイプのエラーハンドリング
		try {
			type = Command.getTypeFrom(text);
			if (type != PlanText.type) throw CommandException.illegalCommandType(type);
		} catch (IllegalArgumentException e) {
			throw CommandException.noSuchCommandType(args[0]);
		}

		// bodyの抽出
		char[] charArray = text.toCharArray();
		int count = 0;
		int bodyIndex = -1;
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == ' ') {
				count++;
			}
			if (count == headerSize) {
				bodyIndex = i;
				break;
			}
		}
		if (count != headerSize) throw CommandException.invalidCommandFormat(text);
		content = text.substring(bodyIndex + 1);

		return new PlanText(content);
	}

	@Override
	public String format() {
		String str = "";
		str += PlanText.type.toString() + " ";
		str += this.content;
		return str;
	}

	@Override
	public CommandType getType() {
		return PlanText.type;
	}

	public String toString() {
		return format();
	}
}
