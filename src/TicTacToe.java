import java.io.*;

public class TicTacToe {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int depth;
		
		GameTree gtree = new GameTree();					// �Q�[����
		Symbol player = Symbol.E, routine = Symbol.E;		// �v���C���[��CPU�̋L����ۑ�����
		Symbol turn = Symbol.X;								// ���݂̎�Ԃ�\��(����X�Ƃ���)
		int eval=0;											// �v���C���[�ɂƂ��Ă̕]���l

		gtree.setRoot(turn);								// gtree�̏����ݒ�������Ȃ�
		gtree.getCurrentNode().printBoard();
		try{
			// �v���C���[�̋L����W�����͂őI������
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
			// ���s�����܂�܂Ń��[�v����
			while(!gtree.getCurrentNode().isStateFinished()){
				gtree.getCurrentNode().printBoard();
				if(turn == player){
					System.out.println("\nPlayer turn(Symbol:" + player + ")");
					// �v���C���[�����ɔz�u����ꏊ��W�����͂Ŏ󂯕t����
					System.out.print("Select number of \"E\" on board >");
					int num = Integer.valueOf(buf.readLine()).intValue();
					
					if(num < Node.range*Node.range && gtree.getCurrentNode().getBoardElement(num) == Symbol.E){
						// ���̃m�[�h�֑J�ڂ���
						gtree.transitNextNode(num);
						eval = gtree.getCurrentNode().getBoardEval(player, routine);
						turn = routine;
					}else{
						// �Ֆʂ̃T�C�Y���傫�Ȓl�܂���"E"�ł͂Ȃ��ꏊ���w�肵����ē���
						System.out.println("The " + num + " is not \"E\" \nSelect number again");
					}
				}else if(turn == routine){
					System.out.println("\nCPU turn(Symbol:" + routine + ", Level " + depth + ")");
					// �~�j�}�b�N�X�@�ɂ��T���Ŕz�u�ꏊ�����肷��
					gtree.searchOfMinimax(routine, player, depth);
					eval = gtree.getCurrentNode().getBoardEval(player, routine);
					/////////////////////////////////////////////////////////////////////////
					//gtree.printNodes();
					//System.out.println(eval);
					/////////////////////////////////////////////////////////////////////////
					turn = player;
				}
			}
			// �ΐ팋�ʂ̏o��
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
