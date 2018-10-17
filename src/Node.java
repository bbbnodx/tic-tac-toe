import java.util.*;

public class Node {
	enum State{
		FINISHED,
		UNFINISHED
	}
	int								idx;							// �m�[�h����ӎ��ʂ���index
	int								from;							// �e�m�[�h���w��index
	ArrayList<Integer>				to = new ArrayList<Integer>();	// �q�m�[�h���w��index�̔z��
	int								level;							// �m�[�h�̐[����\��
	int								next  = 0;						// ���ɋL������Ֆʂ̏ꏊ���w��
	Symbol							turn;							// ��Ԃ�\��
	State							state = State.UNFINISHED;		// �������������̏�Ԃ�\��
	
	final static int				range =  3;						// �Ֆʂ̑傫��
	final static int				win   =  100000;				// �������̕]���l
	final static int				lose  = -100000;				// �s�k���̕]���l
	
	// �Ֆʂ�\��2�����z��
	// �����s��ł���Ƃ���
	ArrayList<Symbol>	board = new ArrayList<Symbol>();
	
	
	// �R���X�g���N�^
	public Node(int id){
		idx = id;
		// board��EMPTY�ŏ�����
		for(int i=0; i<range * range; i++){
			board.add(Symbol.E);
		}
	}
	
	public Node(int id, int fm, int lv, Symbol tn){
		idx = id;
		from = fm;
		level = lv;
		turn = tn;
		
		// board��EMPTY�ŏ�����
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
	
	// �w�肳�ꂽ�m�[�h���w���G�b�W���폜����
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
	
	// �G�b�W��ArrayList��Ԃ�
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
	
	
	// �w�肵���s�̋L���̐���A�z�z��ŕԂ�
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
	
	// �w�肵����̋L���̐���A�z�z��ŕԂ�
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
	
	
	// ���ォ��E���ւ̑Ίp���̋L���̐���A�z�z��ŕԂ�
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
	
	// �E�ォ�獶���ւ̑Ίp���̋L���̐���A�z�z��ŕԂ�
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
	
	// ����̍s�E��E�Ίp���̋L���̐���\���A�z�z�񂩂�]���l��Ԃ����\�b�h
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
	
	// �m�[�h�̔Ֆʂ���]���l��Ԃ����\�b�h
	// �]���l Eval =  1		:����
	//             = -1		:�s�k
	//             =  0		:��������
	//             =  3X_2 + X_1 - 3O_2 - O_1  :�Q�[���̓r��(������X�Ƃ���)
	// X_n��X��n����O��0�̍s�E��E�Ίp���̐���\��
	public int getBoardEval(Symbol selfSym, Symbol opponentSym){
		int eval = 0;
		HashMap<Symbol, Integer> symNum = new HashMap<Symbol, Integer>();
		
		// �e�s�ɂ��Ē��ׂ�
		for(int line=0; line<range; line++){
			symNum = getSymbolNumOfLine(line);
			// �����̋L����range�Ɠ������Ȃ珟���Ȃ̂�1��Ԃ�
			if(symNum.get(selfSym) == range){
				state = State.FINISHED;
				return win;
			// �ΐ푊��̋L����range�Ɠ������Ȃ�s�k�Ȃ̂�-1��Ԃ�
			}else if(symNum.get(opponentSym) == range){
				state = State.FINISHED;
				return lose;
			}else{
				eval += getLineEval(symNum, selfSym, opponentSym);
			}
		}
		// �e��ɂ���
		for(int row=0; row<range; row++){
			symNum = getSymbolNumOfRow(row);
			// �����̋L����range�Ɠ������Ȃ珟���Ȃ̂�1��Ԃ�
			if(symNum.get(selfSym) == range){
				state = State.FINISHED;
				return win;
			// �ΐ푊��̋L����range�Ɠ������Ȃ�s�k�Ȃ̂�-1��Ԃ�
			}else if(symNum.get(opponentSym) == range){
				state = State.FINISHED;
				return lose;
			}else{
				eval += getLineEval(symNum, selfSym, opponentSym);
			}
		}
		// �Ίp���ɂ��Ē��ׂ�
		symNum = getSymbolNumOfDiagonalFromUpperLeftToLowerRight();
		// �����̋L����range�Ɠ������Ȃ珟���Ȃ̂�1��Ԃ�
		if(symNum.get(selfSym) == range){
			state = State.FINISHED;
			return win;
		// �ΐ푊��̋L����range�Ɠ������Ȃ�s�k�Ȃ̂�-1��Ԃ�
		}else if(symNum.get(opponentSym) == range){
			state = State.FINISHED;
			return lose;
		}else{
			eval += getLineEval(symNum, selfSym, opponentSym);
		}
		
		symNum = getSymbolNumOfDiagonalFromUpperRightToLowerLeft();
		// �����̋L����range�Ɠ������Ȃ珟���Ȃ̂�1��Ԃ�
		if(symNum.get(selfSym) == range){
			state = State.FINISHED;
			return win;
		// �ΐ푊��̋L����range�Ɠ������Ȃ�s�k�Ȃ̂�-1��Ԃ�
		}else if(symNum.get(opponentSym) == range){
			state = State.FINISHED;
			return lose;
		}else{
			eval += getLineEval(symNum, selfSym, opponentSym);
		}
		
		// ���s�����܂��Ă��Ȃ��C�Ֆʂ����܂��Ă���Ȃ���������Ƃ���0��Ԃ�
		// �Ֆʂɋ󂫂��܂�����Ƃ��͕]���leval��Ԃ�
		return isBoardFilled() ? 0 : eval;
	}
	
	// �Ֆʂ����ׂ�X or O�Ŗ��܂��Ă��邩�ǂ�����Ԃ�
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
	
	// �q�m�[�h�����ׂēW�J�ς݂����肷��
	public boolean isNodeExpanded(){
		// �Ֆʂ����܂��Ă���(�؂̏I�[)�Ȃ�W�J�ς݂Ƃ���
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
	
	// �q�m�[�h����W�J���ĕԂ�
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
	
	// �e�t�B�[���h�̒l���o�͂���
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
	
	// �Ֆʂ̏󋵂��o�͂���
	public void printBoard(){
		for(int line=0; line<range; line++){
			for(int row=0; row<range; row++){
				System.out.print(3*line+row + ":" + board.get(3*line + row) + " ");
			}
			System.out.println();
		}
	}
}
