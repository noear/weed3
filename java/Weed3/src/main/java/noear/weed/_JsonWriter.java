package noear.weed;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by noear on 14-6-18.
 */
class _JsonWriter
{
	public _JsonWriter()
	{
		_Writer = new StringBuilder();
	}

	public String toJson(){
		return _Writer.toString();
	}

	private boolean _LastIsEnd = false;
	private StringBuilder _Writer;
	public final void WriteObjectStart()
	{
		if (_LastIsEnd)
		{
			_Writer.append(',');
		}

		_Writer.append('{');
		_LastIsEnd = false;
	}
	public final void WriteObjectEnd()
	{
		_Writer.append('}');
		_LastIsEnd = true;
	}
	public final void WriteArrayStart()
	{
		if (_LastIsEnd)
		{
			_Writer.append(',');
		}

		_Writer.append('[');
		_LastIsEnd = false;
	}

	public final void WriteArrayEnd()
	{
		_Writer.append(']');
		_LastIsEnd = true;
	}

	public final void WritePropertyName(String name)
	{
		if (_LastIsEnd)
		{
			_Writer.append(',');
		}

		_Writer.append('\"');
		_Writer.append(name);
		_Writer.append('\"');
		_Writer.append(':');

		_LastIsEnd = false;
	}

	private void OnWriteBef()
	{
		if (_LastIsEnd) //是否上一个元素已结束
		{
			_Writer.append(',');
		}
	}

	public final void WriteNull(){
		WriteValue((String)null);
	}

	public final void WriteValue(String val)
	{
		OnWriteBef();

		if (val == null)
		{
			_Writer.append("null");
		}
		else
		{
			_Writer.append('\"');

			int n = val.length();
			char c;
			for (int i = 0; i < n; i++)
			{
				c = val.charAt(i);
				switch (c)
				{
					case '\\':
						_Writer.append("\\\\"); //20110809
						break;

					case '\"':
						_Writer.append("\\\"");
                        break;

					case '\n':
						_Writer.append("\\n");
                        break;

					case '\r':
						_Writer.append("\\r");
                        break;

					case '\t':
						_Writer.append("\\t");
                        break;

					case '\f':
						_Writer.append("\\f");
                        break;

					case '\b':
						_Writer.append("\\b");
                        break;

					default:
						_Writer.append(c);
                        break;
				}
			}

			_Writer.append('\"');
		}
		_LastIsEnd = true;
	}

	public final void WriteValue(boolean val) {
		OnWriteBef();
		_Writer.append(val ? "true" : "false");
		_LastIsEnd = true;
	}

	public final void WriteValue(double val)
	{
		OnWriteBef();
		_Writer.append(val);
		_LastIsEnd = true;
	}

	public final void WriteValue(int val)
	{
		OnWriteBef();
		_Writer.append(val);
		_LastIsEnd = true;
	}

	public final void WriteValue(long val)
	{
		OnWriteBef();
		_Writer.append(val);
		_LastIsEnd = true;
	}

	final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final void WriteValue(Date val)
	{
		OnWriteBef();
		_Writer.append('\"');
		_Writer.append(df.format(val));
		_Writer.append('\"');
		_LastIsEnd = true;
	}
}