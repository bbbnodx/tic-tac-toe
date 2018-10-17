import java.util.*;

public class GameTree {
	ArrayList<Node> nodes = new ArrayList<Node>();	// ゲーム木を表すノードの動的配列
	Node 			current;						// 現在注目しているノード
	
	// 現在ノードを引数で指定したノードに設定
	public void setCurrentNode(int curIdx){
		if(nodes.size() > curIdx){
			current = nodes.get(curIdx);
		}
	}
	
	public Node getCurrentNode(){
		return current;
	}
	
	// ゲーム木の初期設定をおこなう
	public void setRoot(Symbol first){
		if(!nodes.isEmpty()){
			System.out.println("現在のグラフを削除します");
			nodes.clear();
		}
		current = new Node(0, 0, 0, first);			// (idx, from, level, turn)
		nodes.add(current);
	}

	// idxで指定されたノードを配列から削除する
	public void deleteNode(int idx){
		// 親ノードからのエッジを削除する
		nodes.get(nodes.get(idx).getFrom()).removeTo(idx);
		nodes.remove(idx);
	}
	
	// nodesの情報をすべて表示する
	public void printNodes(){
		System.out.println("Print Nodes(current idx = " + current.getIdx() + ")");
		for(int i=0; i<nodes.size(); i++){
			nodes.get(i).printNode();
		}
		System.out.println("End");
	}
	
	// 指定された盤面の場所に記号を追加した状態のノードを作成し，木に追加するメソッド
	public void transitNextNode(int num){
		Symbol nextTurn;
		if(current.getTurn() == Symbol.X)
			nextTurn = Symbol.O;
		else
			nextTurn = Symbol.X;
		Node nextNode = new Node(nodes.size(), current.getIdx(), current.getLevel()+1,
										nextTurn, current.getBoard());
		nextNode.setBoardElement(num, current.getTurn());
		current.addTo(current.getIdx());
		nodes.add(nextNode);
		current = nextNode;
	}
	
	// ミニマックス法による探索をおこない，次に遷移するノードのindexを返すメソッド
	public int searchOfMinimax(Symbol self, Symbol opponent, int depth){
		int currentIdx = current.getIdx();				// currentノードを保存しておくindex
		int curlev = 0;									// currentからの相対的な深さを表す変数
		int deplev = depth;								// 探索する深さを表す変数
		int nextIdx = nodes.size();						// 次に生成するノードのindexを格納する
		int emptynum = 0;								// currentの盤面の空き(E)の数を格納する
		
		// 評価値の配列を保存する
		// 1次indexは現在ノードからの深さを示す
		// 最終的には，evals[1]に評価値が格納される(evals[2]以降は途中でclearされる)
		ArrayList<ArrayList<Integer>> evals = new ArrayList<ArrayList<Integer>>();
		
		// 探索不能なら現在ノードのindexを返して終了
		if(current.isNodeExpanded())
			return current.getIdx();
		
		// 盤面の空きの数がdepthより小さい場合，deplevを丸める
		for(int eid=0; eid<Node.range*Node.range; eid++){
			if(current.getBoardElement(eid) == Symbol.E){
				evals.add(new ArrayList<Integer>());
				emptynum++;
			}
		}
		evals.add(new ArrayList<Integer>());
		if(emptynum < depth)
			deplev = emptynum;
		
		// currentノードから深さdepthまで探索する
		while(!nodes.get(currentIdx).isNodeExpanded() || curlev > 0){
			// 現在ノードをすべて展開するまでループ
			while(!current.isNodeExpanded()){
				int eval=0;
				// 深さdeplevまで展開する
				for(; curlev<deplev; curlev++){
					current = current.generateNextNode(nextIdx++);
					////////////////////////////////////////////////////////////////////////
					//current.printNode();
					////////////////////////////////////////////////////////////////////////
					nodes.add(current);
					eval = current.getBoardEval(self, opponent);
					// 途中で決着がついたらそれ以降は展開しない
					if(Math.abs(eval) == Node.win)
						break;
				}
				
				///////////////////////////////////////////////////////////////////////////////
				//System.out.println("currentIdx:" + current.getIdx() + " eval:" + eval + " deplev:" + deplev
				//		+ " next:" + current.getNext() );
				///////////////////////////////////////////////////////////////////////////////

				// ノードの評価値を保存し，親ノードへ戻る
				if(curlev == deplev)
					evals.get(curlev).add(eval);
				else
					evals.get(curlev+1).add(eval);
				///////////////////////////////////////////////////////////////////////////////
				//System.out.println("Return to parent.");
				///////////////////////////////////////////////////////////////////////////////
				current = nodes.get(current.getFrom());
				curlev = current.getLevel() - nodes.get(currentIdx).getLevel();
			
				/////////////////////////////////////////////////////////////////////////////////
				//System.out.println("currentIdx:" + current.getIdx() + " eval:" + eval + " curlev:" + curlev
				//		+ " next:" + current.getNext() );
				//System.out.println(curlev+1+""+evals.get(curlev+1));
				/////////////////////////////////////////////////////////////////////////////////
			}
			
			// curlevが偶数なら子ノードのEvalsの最大値を現在ノードのEvalとして採択する
			if(curlev%2 == 0){
				evals.get(curlev).add(Collections.max(evals.get(curlev+1)));
				// 現在ノードより深いノードの場合，子ノードの評価値を消去する
				if(curlev != 0)
					evals.get(curlev+1).clear();
			// curlevが奇数なら現在レベルのEvalsの最小値を親ノードのEvalとして採択する
			}else{
				evals.get(curlev).add(Collections.min(evals.get(curlev+1)));
				evals.get(curlev+1).clear();
			}
			
			
			// 親ノードへ戻る
			/////////////////////////////////////////////////////////////////////////////
			//System.out.println(curlev +""+evals.get(curlev));
			//System.out.println("Return to parent.");
			/////////////////////////////////////////////////////////////////////////////
			if(curlev > 0){
				current = nodes.get(current.getFrom());
				curlev--;
			}
		}
		/////////////////////////////////////////////////////////////////////////////////
		// デバッグ用パラメータ出力
		//System.out.println("currentIdx:" + current.getIdx() +"currentIdx(true):" + currentIdx
		//		+ " curlev:" + curlev + " deplev:" + deplev);
		//for(int i=0; i<evals.size(); i++){
		//	System.out.print("["+i+"]");
		//	for(int j=0; j<evals.get(i).size(); j++){
		//		System.out.print(evals.get(i).get(j) + " ");
		//	}
		//	System.out.println();
		//}
		//System.out.println(current.getToAll());
		////////////////////////////////////////////////////////////////////////////////////
		// 現在ノードの直下ノード群から最大の評価値を得るノードを選択し，遷移する
		current = nodes.get(nodes.get(currentIdx).getTo(evals.get(curlev+1).indexOf(Collections.max(evals.get(curlev+1)))));
		return current.getIdx();
	}
}
