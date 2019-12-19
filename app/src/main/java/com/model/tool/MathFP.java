package com.model.tool;

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

public abstract class MathFP {

	private static int _fbits = 12;
	// private static int _fbits = 8;
	private static int _digits = 4;
	private static int _one;
	private static int _fmask = 4095;
	private static int _dmul = 10000;
	private static int _flt = 0;
	private static int _pi;
	private static int e[];
	public static int PI;
	public static int E;
	public static final int MAX_VALUE = 0x7fffffff;
	public static final int MIN_VALUE = 0x80000001;

	public MathFP() {
	}

	private final static int[] sqrtTab = { 0, 16, 22, 27, 32, 35, 39, 42, 45,
			48, 50, 53, 55, 57, 59, 61, 64, 65, 67, 69, 71, 73, 75, 76, 78, 80,
			81, 83, 84, 86, 87, 89, 90, 91, 93, 94, 96, 97, 98, 99, 101, 102,
			103, 104, 106, 107, 108, 109, 110, 112, 113, 114, 115, 116, 117,
			118, 119, 120, 121, 122, 123, 124, 125, 126, 128, 128, 129, 130,
			131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143,
			144, 144, 145, 146, 147, 148, 149, 150, 150, 151, 152, 153, 154,
			155, 155, 156, 157, 158, 159, 160, 160, 161, 162, 163, 163, 164,
			165, 166, 167, 167, 168, 169, 170, 170, 171, 172, 173, 173, 174,
			175, 176, 176, 177, 178, 178, 179, 180, 181, 181, 182, 183, 183,
			184, 185, 185, 186, 187, 187, 188, 189, 189, 190, 191, 192, 192,
			193, 193, 194, 195, 195, 196, 197, 197, 198, 199, 199, 200, 201,
			201, 202, 203, 203, 204, 204, 205, 206, 206, 207, 208, 208, 209,
			209, 210, 211, 211, 212, 212, 213, 214, 214, 215, 215, 216, 217,
			217, 218, 218, 219, 219, 220, 221, 221, 222, 222, 223, 224, 224,
			225, 225, 226, 226, 227, 227, 228, 229, 229, 230, 230, 231, 231,
			232, 232, 233, 234, 234, 235, 235, 236, 236, 237, 237, 238, 238,
			239, 240, 240, 241, 241, 242, 242, 243, 243, 244, 244, 245, 245,
			246, 246, 247, 247, 248, 248, 249, 249, 250, 250, 251, 251, 252,
			252, 253, 253, 254, 254, 255 };

	private static long adjustment(long x, long xn) {
		long xn2 = xn * xn;

		long comparitor0 = xn2 - x;
		if (comparitor0 < 0) {
			comparitor0 = -comparitor0;
		}

		long twice_xn = xn << 1;

		long comparitor1 = x - xn2 + twice_xn - 1;
		if (comparitor1 < 0) {
			comparitor1 = -comparitor1;
		}

		long comparitor2 = xn2 + twice_xn + 1 - x;
		if (comparitor0 > comparitor1) {
			return (comparitor1 > comparitor2) ? ++xn : --xn;
		}

		return (comparitor0 > comparitor2) ? ++xn : xn;
	}

	public static long sqrts(long x) {
		long xn;

		if (x >= 0x10000) {
			if (x >= 0x1000000) {
				if (x >= 0x10000000) {
					if (x >= 0x40000000)
						xn = sqrtTab[(int) (x >> 24)] << 8;
					else
						xn = sqrtTab[(int) (x >> 22)] << 7;
				} else {
					if (x >= 0x4000000)
						xn = sqrtTab[(int) (x >> 20)] << 6;
					else
						xn = sqrtTab[(int) (x >> 18)] << 5;
				}
				xn = (xn + 1 + (x / xn)) >> 1;
				xn = (xn + 1 + (x / xn)) >> 1;
				return adjustment(x, xn);
			} else {
				if (x >= 0x100000) {
					if (x >= 0x400000)
						xn = (long) sqrtTab[(int) (x >> 16)] << 4;
					else
						xn = (long) sqrtTab[(int) (x >> 14)] << 3;
				} else {
					if (x >= 0x40000)
						xn = (long) sqrtTab[(int) (x >> 12)] << 2;
					else
						xn = (long) sqrtTab[(int) (x >> 10)] << 1;
				}
				xn = (xn + 1 + (x / xn)) >> 1;

				return adjustment(x, xn);
			}
		} else {
			if (x >= 0x100) {
				if (x >= 0x1000) {
					if (x >= 0x4000) {
						xn = sqrtTab[(int) (x >> 8)] + 1;
					} else {
						xn = (sqrtTab[(int) (x >> 6)] >> 1) + 1;
					}
				} else {
					if (x >= 0x400) {
						xn = (sqrtTab[(int) (x >> 4)] >> 2) + 1;
					} else {
						xn = (sqrtTab[(int) (x >> 2)] >> 3) + 1;
					}
				}

				return adjustment(x, xn);
			} else {
				if (x >= 0) {
					return adjustment(x, sqrtTab[(int) x] >> 4);
				}
			}
		}
		return -1;
	}

	public static int setPrecision(int i) {
		if (i > 12 || i < 0)
			return _digits;
		_fbits = i;
		_one = 1 << i;
		_flt = 12 - i;
		_digits = 0;
		_dmul = 1;
		_fmask = _one - 1;
		PI = _pi >> _flt;
		E = e[1] >> _flt;
		for (int j = _one; j != 0;) {
			j /= 10;
			_digits++;
			_dmul *= 10;
		}

		return _digits;
	}

	public static int getPrecision() {
		return _fbits;
	}

	public static int toInt(int i) {
		i = round(i, 0);
		return i >> _fbits;
	}

	public static int toFP(int i) {
		return i << _fbits;
	}

	public static int convert(int i, int j) {
		byte byte0 = ((byte) (i >= 0 ? 1 : -1));
		if (abs(j) < 13)
			if (_fbits < j)
				i = i + byte0 * (1 << (j - _fbits >> 1)) >> j - _fbits;
			else
				i <<= _fbits - j;
		return i;
	}

	public static int toFP(String s) {
		int i = 0;
		if (s.charAt(0) == '-')
			i = 1;
		String s1 = "-1";
		int j = s.indexOf('.');
		if (j >= 0) {
			for (s1 = s.substring(j + 1, s.length()); s1.length() < _digits; s1 = s1
					+ "0")
				;
			if (s1.length() > _digits)
				s1 = s1.substring(0, _digits);
		} else {
			j = s.length();
		}
		int k = 0;
		if (i != j)
			k = Integer.parseInt(s.substring(i, j));
		int l = Integer.parseInt(s1) + 1;
		int i1 = (k << _fbits) + (l << _fbits) / _dmul;
		if (i == 1)
			i1 = -i1;
		return i1;
	}

	public static String toString(int i) {
		boolean flag = false;
		if (i < 0) {
			flag = true;
			i = -i;
		}
		int j = i >> _fbits;
		int k = _dmul * (i & _fmask) >> _fbits;
		String s;
		for (s = Integer.toString(k); s.length() < _digits; s = "0" + s)
			;
		return (flag ? "-" : "") + Integer.toString(j) + "." + s;
	}

	public static String toString(int i, int j) {
		if (j > _digits)
			j = _digits;
		String s = toString(round(i, j));
		return s.substring(0, (s.length() - _digits) + j);
	}

	public static int max(int i, int j) {
		return i >= j ? i : j;
	}

	public static int min(int i, int j) {
		return j >= i ? i : j;
	}

	public static int round(int i, int j) {
		int k = 10;
		for (int l = 0; l < j; l++)
			k *= 10;

		k = div(toFP(5), toFP(k));
		if (i < 0)
			k = -k;
		return i + k;
	}

	public static int mul(int i, int j) {
		boolean flag = false;
		int k = _fbits;
		int l = _fmask;
		if ((i & l) == 0)
			return (i >> k) * j;
		if ((j & l) == 0)
			return i * (j >> k);
		if (i < 0 && j > 0 || i > 0 && j < 0)
			flag = true;
		if (i < 0)
			i = -i;
		if (j < 0)
			j = -j;
		for (; max(i, j) >= 1 << 31 - k; k--) {
			i >>= 1;
			j >>= 1;
			l >>= 1;
		}

		int i1 = (i >> k) * (j >> k) << k;
		int j1 = (i & l) * (j & l) >> k;
		j1 += (i & ~l) * (j & l) >> k;
		i1 = i1 + j1 + ((i & l) * (j & ~l) >> k) << _fbits - k;
		if (i1 < 0)
			throw new ArithmeticException("Overflow");
		else
			return flag ? -i1 : i1;
	}

	public static int div(int i, int j) {
		boolean flag = false;
		int k = _fbits;
		if (j == _one)
			return i;
		if ((j & _fmask) == 0)
			return i / (j >> k);
		if (i < 0 && j > 0 || i > 0 && j < 0)
			flag = true;
		if (i < 0)
			i = -i;
		if (j < 0)
			j = -j;
		for (; max(i, j) >= 1 << 31 - k; k--) {
			i >>= 1;
			j >>= 1;
		}

		int l = (i << k) / j << _fbits - k;
		return flag ? -l : l;
	}

	public static int add(int i, int j) {
		return i + j;
	}

	public static int sub(int i, int j) {
		return i - j;
	}

	public static int abs(int i) {
		if (i < 0)
			return -i;
		else
			return i;
	}

	public static int sqrt(int i, int j) {

		System.out.println();
		if (i < 0)
			throw new ArithmeticException("Bad Input");
		if (i == 0)
			return 0;
		int k = i + _one >> 1;
		for (int l = 0; l < j; l++)
			k = k + div(i, k) >> 1;

		if (k < 0)
			throw new ArithmeticException("Overflow");
		else
			return k;
	}

	public static int sqrt(int i) {
		return sqrt(i, 16);
	}

	public static int sin(int i) {
		int j = mul(i, div(toFP(180), PI));
		j %= toFP(360);
		if (j < 0)
			j = toFP(360) + j;
		int k = j;
		if (j >= toFP(90) && j < toFP(270))
			k = toFP(180) - j;
		else if (j >= toFP(270) && j < toFP(360))
			k = -(toFP(360) - j);
		int l = k / 90;
		int i1 = mul(l, l);
		int j1 = mul(mul(mul(mul(-18 >> _flt, i1) + (326 >> _flt), i1)
				- (2646 >> _flt), i1)
				+ (6434 >> _flt), l);
		return j1;
	}

	public static int asin(int i) {
		if (abs(i) > _one)
			throw new ArithmeticException("Bad Input");
		boolean flag = i < 0;
		if (i < 0)
			i = -i;
		int j = mul(mul(mul(mul(35 >> _flt, i) - (146 >> _flt), i)
				+ (347 >> _flt), i)
				- (877 >> _flt), i)
				+ (6434 >> _flt);
		int k = PI / 2 - mul(sqrt(_one - i), j);
		return flag ? -k : k;
	}

	public static int cos(int i) {
		return sin(PI / 2 - i);
	}

	public static int acos(int i) {
		return PI / 2 - asin(i);
	}

	public static int tan(int i) {
		return div(sin(i), cos(i));
	}

	public static int cot(int i) {
		return div(cos(i), sin(i));
	}

	public static int atan(int i) {
		return asin(div(i, sqrt(_one + mul(i, i))));
	}

	public static int exp(int i) {
		if (i == 0)
			return _one;
		boolean flag = i < 0;
		i = abs(i);
		int j = i >> _fbits;
		int k = _one;
		for (int l = 0; l < j / 4; l++)
			k = mul(k, e[4] >> _flt);

		if (j % 4 > 0)
			k = mul(k, e[j % 4] >> _flt);
		i &= _fmask;
		if (i > 0) {
			int i1 = _one;
			int j1 = 0;
			int k1 = 1;
			for (int l1 = 0; l1 < 16; l1++) {
				j1 += i1 / k1;
				i1 = mul(i1, i);
				k1 *= l1 + 1;
				if (k1 > i1 || i1 <= 0 || k1 <= 0)
					break;
			}

			k = mul(k, j1);
		}
		if (flag)
			k = div(_one, k);
		return k;
	}

	public static int log(int i) {
		if (i <= 0)
			throw new ArithmeticException("Bad Input");
		int j = 0;
		boolean flag = false;
		int l;
		for (l = 0; i >= _one << 1; l++)
			i >>= 1;

		int i1 = l * (2839 >> _flt);
		int j1 = 0;
		if (i < _one)
			return -log(div(_one, i));
		i -= _one;
		for (int k1 = 1; k1 < 20; k1++) {
			int k;
			if (j == 0)
				k = i;
			else
				k = mul(j, i);
			if (k == 0)
				break;
			j1 += ((k1 % 2 != 0 ? 1 : -1) * k) / k1;
			j = k;
		}

		return i1 + j1;
	}

	public static int pow(int i, int j) {
		boolean flag = j < 0;
		int k = _one;
		j = abs(j);
		for (int l = j >> _fbits; l-- > 0;)
			k = mul(k, i);

		if (k < 0)
			throw new ArithmeticException("Overflow");
		if (i != 0)
			k = mul(k, exp(mul(log(i), j & _fmask)));
		else
			k = 0;
		if (flag)
			return div(_one, k);
		else
			return k;
	}

	public static int atan2(int i, int j) {
		int k = 0;
		if (j > 0)
			k = atan(div(i, j));
		else if (j < 0) {
			k = (j >= 0 ? PI : -PI) - atan(abs(div(i, j)));
		} else {
			if (j == 0 && i == 0)
				throw new ArithmeticException("Bad Input");
			k = (j >= 0 ? PI : -PI) / 2;
		}
		return k;
	}

	static {
		_one = 4096;
		_pi = 12868;
		e = (new int[] { _one, 11134, 30266, 0x1415e, 0x36994 });
		PI = _pi;
		E = e[1];
	}

	public static void  main(String[] args){
		int radians= MathFP.div(MathFP.toFP(3), MathFP.toFP(4));
		int deltaX = MathFP.mul(MathFP.toFP(5), MathFP.cos(radians));
		int deltaY = MathFP.mul(MathFP.toFP(5), MathFP.sin(radians));
        System.out.println(deltaX+"###########"+deltaY);
	}
}
