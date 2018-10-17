import java.io.*;

public class TicTacToe {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int depth;
		
		GameTree gtree = new GameTree();					// ゲーム木
		Symbol player = Symbol.E, routine = Symbol.E;		// プレイヤーとCPUの記号を保存する
		Symbol turn = Symbol.X;								// 現在の手番を表す(先手をXとする)
		int eval=0;											// プレイヤーにとっての評価値

		gtree.setRoot(turn);								// gtreeの初期設定をおこなう
		gtree.getCurrentNode().printBoard();
		try{
			// プレイヤーの記号を標準入力で選択する
			BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Input CPU's Game-tree-search depth(easy:1 normal:2 hard:3) >");
			depth = Integer.valueOf(buf.readLine()).intValue();
			if(depth <= 0){
				System.out.println("Invalid input");
				System.exit(1);
			}
			System.out.print("Select your turn(X:first, O:second) >");
			String x;
			x = buf.readLine();
			if(x.equals("X")){
				player = Symbol.X;
				routine = Symbol.O;
			}else if(x.equals("O")){
				player = Symbol.O;
				routine = Symbol.X;
			}else{
				System.out.println("Invalid input");
				System.exit(1);
			}
			// 勝敗が決まるまでループする
			while(!gtree.getCurrentNode().isStateFinished()){
				gtree.getCurrentNode().printBoard();
				if(turn == player){
					System.out.println("\nPlayer turn(Symbol:" + player + ")");
					// プレイヤーが次に配置する場所を標準入力で受け付ける
					System.out.print("Select number of \"E\" on board >");
					int num = Integer.valueOf(buf.readLine()).intValue();
					
					if(num < Node.range*Node.range && gtree.getCurrentNode().getBoardElement(num) == Symbol.E){
						// 次のノードへ遷移する
						gtree.transitNextNode(num);
						eval = gtree.getCurrentNode().getBoardEval(player, routine);
						turn = routine;
					}else{
						// 盤面のサイズより大きな値または"E"ではない場所を指定したら再入力
						System.out.println("The " + num + " is not \"E\" \nSelect number again");
					}
				}else if(turn == routine){
					System.out.println("\nCPU turn(Symbol:" + routine + ", Level " + depth + ")");
					// ミニマックス法による探索で配置場所を決定する
					gtree.searchOfMinimax(routine, player, depth);
					eval = gtree.getCurrentNode().getBoardEval(player, routine);
					/////////////////////////////////////////////////////////////////////////
					//gtree.printNodes();
					//System.out.println(eval);
					/////////////////////////////////////////////////////////////////////////
					turn = player;
				}
			}
			// 対戦結果の出力
			gtree.getCurrentNode().printBoard();
			if(eval > 0){
				System.out.println("You win!");
			}else if(eval < 0){
				System.out.println("You lose");
			}else{
				System.out.println("Draw");
			}
		}catch(IOException e){
			System.out.println("Invalid input");
			System.exit(1);
		}
	}

}
