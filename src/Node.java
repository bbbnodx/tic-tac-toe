import java.util.*;

public class Node {
	enum State{
		FINISHED,
		UNFINISHED
	}
	int								idx;							// ノードを一意識別するindex
	int								from;							// 親ノードを指すindex
	ArrayList<Integer>				to = new ArrayList<Integer>();	// 子ノードを指すindexの配列
	int								level;							// ノードの深さを表す
	int								next  = 0;						// 次に記入する盤面の場所を指す
	Symbol							turn;							// 手番を表す
	State							state = State.UNFINISHED;		// 決着がついたかの状態を表す
	
	final static int				range =  3;						// 盤面の大きさ
	final static int				win   =  100000;				// 勝利時の評価値
	final static int				lose  = -100000;				// 敗北時の評価値
	
	// 盤面を表す2次元配列
	// 正方行列であるとする
	ArrayList<Symbol>	board = new ArrayList<Symbol>();
	
	
	// コンストラクタ
	public Node(int id){
		idx = id;
		// boardをEMPTYで初期化
		for(int i=0; i<range * range; i++){
			board.add(Symbol.E);
		}
	}
	
	public Node(int id, int fm, int lv, Symbol tn){
		idx = id;
		from = fm;
		level = lv;
		turn = tn;
		
		// boardをEMPTYで初期化
		for(int i=0; i<range*range; i++){
			board.add(Symbol.E);
		}
	}
	
	public Node(int id, int fm, int lv, Symbol tn, ArrayList<Symbol> bd){
		idx = id;
		from = fm;
		level = lv;
		turn = tn;
		board = bd;
	}
	
	public void setIdx(int id){
		idx = id;
	}
	
	public int getIdx(){
		return idx;
	}
	
	public void setFrom(int fm){
		from = fm;
	}
	
	public int getFrom(){
		return from;
	}
	
	public void addTo(int t){
		to.add(t);
	}
	
	// 指定されたノードを指すエッジを削除する
	public void removeTo(int nodeId){
		for(int i=0; i<to.size(); i++)
			if(to.get(i) == nodeId)
				to.remove(i);
	}
	
	public void clearTo(){
		to.clear();
	}
	
	public int getTo(int tid){
		return to.get(tid);
	}
	
	// エッジのArrayListを返す
	public ArrayList<Integer> getToAll(){
		return to;
	}
	
	public void setLevel(int lv){
		level = lv;
	}
	
	public int getLevel(){
		return level;
	}
	
	public void setNext(int nx){
		next = nx;
	}
	
	public int getNext(){
		return next;
	}

	public void setTurn(Symbol tn){
		turn = tn;
	}
	
	public Symbol getTurn(){
		return turn;
	}
	
	public boolean isStateFinished(){
		return state == State.FINISHED ? true : false;
	}
	
	public void setBoard(ArrayList<Symbol> bd){
		board = bd;
	}
	
	// eid = 3 * line + row
	public void setBoardElement(int eid, Symbol sym){
		board.set(eid, sym);
	}
	
	public ArrayList<Symbol> getBoard(){
		return board;
	}
	
	public Symbol getBoardElement(int eid){
		return board.get(eid);
	}
	
	
	// 指定した行の記号の数を連想配列で返す
	public HashMap<Symbol, Integer> getSymbolNumOfLine(int line){
		HashMap<Symbol, Integer> symNum = new HashMap<Symbol, Integer>(); 
		int xnum=0, onum=0, emptynum=0;
		
		for(int row=0; row<range; row++){
			if(board.get(3*line + row) == Symbol.X)
				xnum++;
			else if(board.get(3*line + row) == Symbol.O)
				onum++;
			else
				emptynum++;
		}
		symNum.put(Symbol.X, xnum);
		symNum.put(Symbol.O, onum);
		symNum.put(Symbol.E, emptynum);
		
		return symNum;
	}
	
	// 指定した列の記号の数を連想配列で返す
	public HashMap<Symbol, Integer> getSymbolNumOfRow(int row){
		HashMap<Symbol, Integer> symNum = new HashMap<Symbol, Integer>(); 
		int xnum=0, onum=0, emptynum=0;
		
		for(int line=0; line<range; line++){
			if(board.get(3*line + row) == Symbol.X)
				xnum++;
			else if(board.get(3*line + row) == Symbol.O)
				onum++;
			else
				emptynum++;
		}
		symNum.put(Symbol.X, xnum);
		symNum.put(Symbol.O, onum);
		symNum.put(Symbol.E, emptynum);
		
		return symNum;
	}
	
	
	// 左上から右下への対角線の記号の数を連想配列で返す
	public HashMap<Symbol, Integer> getSymbolNumOfDiagonalFromUpperLeftToLowerRight(){
		HashMap<Symbol, Integer> symNum = new HashMap<Symbol, Integer>(); 
		int xnum=0, onum=0, emptynum=0;
		
		for(int i=0; i<range; i++){
			if(board.get(3*i + i) == Symbol.X)
				xnum++;
			else if(board.get(3*i + i) == Symbol.O)
				onum++;
			else
				emptynum++;
		}
		symNum.put(Symbol.X, xnum);
		symNum.put(Symbol.O, onum);
		symNum.put(Symbol.E, emptynum);
		
		return symNum;
	}
	
	// 右上から左下への対角線の記号の数を連想配列で返す
	public HashMap<Symbol, Integer> getSymbolNumOfDiagonalFromUpperRightToLowerLeft(){
		HashMap<Symbol, Integer> symNum = new HashMap<Symbol, Integer>(); 
		int xnum=0, onum=0, emptynum=0;
		
		for(int i=0; i<range; i++){
			if(board.get(3*i + range-1 - i) == Symbol.X)
				xnum++;
			else if(board.get(3*i + range-1 - i) == Symbol.O)
				onum++;
			else
				emptynum++;
		}
		symNum.put(Symbol.X, xnum);
		symNum.put(Symbol.O, onum);
		symNum.put(Symbol.E, emptynum);
		
		return symNum;
	}
	
	// 特定の行・列・対角線の記号の数を表す連想配列から評価値を返すメソッド
	public int getLineEval(HashMap<Symbol, Integer> symNum, Symbol selfSym, Symbol opponentSym){
		if(symNum.get(opponentSym) == 0){
			if(symNum.get(selfSym) == 2)
				return 3;
			else if(symNum.get(selfSym) == 1)
				return 1;
			else
				return 0;
		}else if(symNum.get(selfSym) == 0){
			if(symNum.get(opponentSym) == 2)
				return -3;
			else if(symNum.get(opponentSym) == 1)
				return -1;
			else
				return 0;
		}else
			return 0;
	}
	
	// ノードの盤面から評価値を返すメソッド
	// 評価値 Eval =  1		:勝利
	//             = -1		:敗北
	//             =  0		:引き分け
	//             =  3X_2 + X_1 - 3O_2 - O_1  :ゲームの途中(自分をXとする)
	// X_nはXがn個かつOが0個の行・列・対角線の数を表す
	public int getBoardEval(Symbol selfSym, Symbol opponentSym){
		int eval = 0;
		HashMap<Symbol, Integer> symNum = new HashMap<Symbol, Integer>();
		
		// 各行について調べる
		for(int line=0; line<range; line++){
			symNum = getSymbolNumOfLine(line);
			// 自分の記号がrangeと等しいなら勝利なので1を返す
			if(symNum.get(selfSym) == range){
				state = State.FINISHED;
				return win;
			// 対戦相手の記号がrangeと等しいなら敗北なので-1を返す
			}else if(symNum.get(opponentSym) == range){
				state = State.FINISHED;
				return lose;
			}else{
				eval += getLineEval(symNum, selfSym, opponentSym);
			}
		}
		// 各列について
		for(int row=0; row<range; row++){
			symNum = getSymbolNumOfRow(row);
			// 自分の記号がrangeと等しいなら勝利なので1を返す
			if(symNum.get(selfSym) == range){
				state = State.FINISHED;
				return win;
			// 対戦相手の記号がrangeと等しいなら敗北なので-1を返す
			}else if(symNum.get(opponentSym) == range){
				state = State.FINISHED;
				return lose;
			}else{
				eval += getLineEval(symNum, selfSym, opponentSym);
			}
		}
		// 対角線について調べる
		symNum = getSymbolNumOfDiagonalFromUpperLeftToLowerRight();
		// 自分の記号がrangeと等しいなら勝利なので1を返す
		if(symNum.get(selfSym) == range){
			state = State.FINISHED;
			return win;
		// 対戦相手の記号がrangeと等しいなら敗北なので-1を返す
		}else if(symNum.get(opponentSym) == range){
			state = State.FINISHED;
			return lose;
		}else{
			eval += getLineEval(symNum, selfSym, opponentSym);
		}
		
		symNum = getSymbolNumOfDiagonalFromUpperRightToLowerLeft();
		// 自分の記号がrangeと等しいなら勝利なので1を返す
		if(symNum.get(selfSym) == range){
			state = State.FINISHED;
			return win;
		// 対戦相手の記号がrangeと等しいなら敗北なので-1を返す
		}else if(symNum.get(opponentSym) == range){
			state = State.FINISHED;
			return lose;
		}else{
			eval += getLineEval(symNum, selfSym, opponentSym);
		}
		
		// 勝敗が決まっていなく，盤面が埋まっているなら引き分けとして0を返す
		// 盤面に空きがまだあるときは評価値evalを返す
		return isBoardFilled() ? 0 : eval;
	}
	
	// 盤面がすべてX or Oで埋まっているかどうかを返す
	public boolean isBoardFilled(){
		for(int line=0; line<range; line++){
			for(int row=0; row<range; row++){
				if(board.get(3*line + row) == Symbol.E){
					return false;
				}
			}
		}
		state = State.FINISHED;
		return true;
	}
	
	// 子ノードをすべて展開済みか判定する
	public boolean isNodeExpanded(){
		// 盤面が埋まっている(木の終端)なら展開済みとする
		if(isBoardFilled()){
			return true;
		}else if(state == State.FINISHED){
			return true;
		}else{
			int limit;
			for(limit=range*range; limit>0 && board.get(limit-1) != Symbol.E; limit--);
			if(next >= limit){
				return true;
			}else{
				return false;
			}
		}
	}
	
	// 子ノードを一つ展開して返す
	public Node generateNextNode(int id)
	{
		Symbol nextTurn;
		
		if(isBoardFilled())
			throw new RuntimeException();
		
		if(turn == Symbol.X)
			nextTurn = Symbol.O;
		else
			nextTurn = Symbol.X;
		
		while(next < range*range){
			if(board.get(next) == Symbol.E){
				ArrayList<Symbol> nextBoard = new ArrayList<Symbol>(board);
				nextBoard.set(next++, turn);
				///////////////////////////////////////////////////////////////////////////
				//System.out.println("nextboard"+nextBoard);
				///////////////////////////////////////////////////////////////////////////
				to.add(id);
				return new Node(id, idx, level+1, nextTurn, nextBoard);
			}else{
				next++;
			}
		}
		throw new RuntimeException();
	}
	
	// 各フィールドの値を出力する
	public void printNode(){
		for(int i=0; i<level; i++)
			System.out.print("\t");
		
		System.out.println("idx:" + idx + " from:" + from + " to:" + to);
		for(int line=0; line<range; line++){
			for(int j=0; j<level+1; j++)
				System.out.print("\t");
			for(int row=0; row<range; row++){
				System.out.print(board.get(3*line + row) + " ");
			}
			System.out.println();
		}
	}
	
	// 盤面の状況を出力する
	public void printBoard(){
		for(int line=0; line<range; line++){
			for(int row=0; row<range; row++){
				System.out.print(3*line+row + ":" + board.get(3*line + row) + " ");
			}
			System.out.println();
		}
	}
}
