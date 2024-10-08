import java.util.*;

class Solution {
	static Character[][] game;
	static int boardHeight, boardWidth;
	static final char DELETED = '-';

	static class Pos {
		int r;
		int c;

		public Pos(int r, int c) {
			this.r = r;
			this.c = c;
		}

		public String toString() {
			return "(" + r + ", " + c + ")";
		}

		// Set 동등 비교를 위한 equals와 hashCode 재정의
		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}

			if (o == null || this.getClass() != o.getClass()) {
				return false;
			}

			Pos p = (Pos) o;

			return this.r == p.r && this.c == p.c;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.r, this.c);
		}
	}

	public void init(int h, int w, String[] b) {
		boardHeight = h;
		boardWidth = w;

		game = new Character[h][w];

		for (int r = 0; r < h; r++) {
			char[] line = b[r].toCharArray();
			for (int c = 0; c < w; c++) {
				game[r][c] = line[c];
			}
		}
	}

	/**
	 * 지워지는 블럭이 있는지 확인하고, 있으면 Set에 추가하는 메서드.
	 *
	 * @return 지워질 블럭의 좌표를 담은 Set
	 */
	public Set<Pos> check() {
		Set<Pos> result = new HashSet<>();

		for (int r = 0; r < boardHeight - 1; r++) {
			for (int c = 0; c < boardWidth - 1; c++) {
				char ch = game[r][c];
				if (ch == DELETED) {
					continue;
				}
				if (game[r][c + 1] == ch && game[r + 1][c] == ch && game[r + 1][c + 1] == ch) {
					result.addAll(
							Arrays.asList(new Pos(r, c), new Pos(r, c + 1), new Pos(r + 1, c), new Pos(r + 1, c + 1)));
				}
			}
		}

		return result;
	}

	/**
	 * 주어진 위치를 제거하는 메서드
	 *
	 * @param targets 지울 위치를 담은 Set
	 * @return 지운 블럭의 총 개수
	 */
	public int boom(Set<Pos> targets) {
		int cnt = 0;

		for (Pos pos : targets) {
			game[pos.r][pos.c] = DELETED;
			cnt += 1;
		}

		return cnt;
	}

	/**
	 * 공중에 떠 있는 블럭을 아래로 내리는 메서드
	 */
	public void drop() {
		for (int r = boardHeight - 2; r >= 0; r--) {
			for (int c = 0; c < boardWidth; c++) {
				if (game[r][c] != DELETED) {
					int tmp = r;
					while (tmp < boardHeight - 1 && game[tmp + 1][c] == DELETED) {
						game[tmp + 1][c] = game[tmp][c];
						game[tmp++][c] = DELETED;
					}
				}
			}
		}
	}

	public int play() {
		int answer = 0;

		while (true) {
			Set<Pos> set = check();

			if (set.isEmpty()) {
				break;
			}

			answer += boom(set);
			drop();
		}

		return answer;
	}

	public int solution(int m, int n, String[] board) {
		init(m, n, board);

		return play();
	}
}
