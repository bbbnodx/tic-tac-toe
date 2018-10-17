import java.util.*;

public class GameTree {
	ArrayList<Node> nodes = new ArrayList<Node>();	// �Q�[���؂�\���m�[�h�̓��I�z��
	Node 			current;						// ���ݒ��ڂ��Ă���m�[�h
	
	// ���݃m�[�h�������Ŏw�肵���m�[�h�ɐݒ�
	public void setCurrentNode(int curIdx){
		if(nodes.size() > curIdx){
			current = nodes.get(curIdx);
		}
	}
	
	public Node getCurrentNode(){
		return current;
	}
	
	// �Q�[���؂̏����ݒ�������Ȃ�
	public void setRoot(Symbol first){
		if(!nodes.isEmpty()){
			System.out.println("���݂̃O���t���폜���܂�");
			nodes.clear();
		}
		current = new Node(0, 0, 0, first);			// (idx, from, level, turn)
		nodes.add(current);
	}

	// idx�Ŏw�肳�ꂽ�m�[�h��z�񂩂�폜����
	public void deleteNode(int idx){
		// �e�m�[�h����̃G�b�W���폜����
		nodes.get(nodes.get(idx).getFrom()).removeTo(idx);
		nodes.remove(idx);
	}
	
	// nodes�̏������ׂĕ\������
	public void printNodes(){
		System.out.println("Print Nodes(current idx = " + current.getIdx() + ")");
		for(int i=0; i<nodes.size(); i++){
			nodes.get(i).printNode();
		}
		System.out.println("End");
	}
	
	// �w�肳�ꂽ�Ֆʂ̏ꏊ�ɋL����ǉ�������Ԃ̃m�[�h���쐬���C�؂ɒǉ����郁�\�b�h
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
	
	// �~�j�}�b�N�X�@�ɂ��T���������Ȃ��C���ɑJ�ڂ���m�[�h��index��Ԃ����\�b�h
	public int searchOfMinimax(Symbol self, Symbol opponent, int depth){
		int currentIdx = current.getIdx();				// current�m�[�h��ۑ����Ă���index
		int curlev = 0;									// current����̑��ΓI�Ȑ[����\���ϐ�
		int deplev = depth;								// �T������[����\���ϐ�
		int nextIdx = nodes.size();						// ���ɐ�������m�[�h��index���i�[����
		int emptynum = 0;								// current�̔Ֆʂ̋�(E)�̐����i�[����
		
		// �]���l�̔z���ۑ�����
		// 1��index�͌��݃m�[�h����̐[��������
		// �ŏI�I�ɂ́Cevals[1]�ɕ]���l���i�[�����(evals[2]�ȍ~�͓r����clear�����)
		ArrayList<ArrayList<Integer>> evals = new ArrayList<ArrayList<Integer>>();
		
		// �T���s�\�Ȃ猻�݃m�[�h��index��Ԃ��ďI��
		if(current.isNodeExpanded())
			return current.getIdx();
		
		// �Ֆʂ̋󂫂̐���depth��菬�����ꍇ�Cdeplev���ۂ߂�
		for(int eid=0; eid<Node.range*Node.range; eid++){
			if(current.getBoardElement(eid) == Symbol.E){
				evals.add(new ArrayList<Integer>());
				emptynum++;
			}
		}
		evals.add(new ArrayList<Integer>());
		if(emptynum < depth)
			deplev = emptynum;
		
		// current�m�[�h����[��depth�܂ŒT������
		while(!nodes.get(currentIdx).isNodeExpanded() || curlev > 0){
			// ���݃m�[�h�����ׂēW�J����܂Ń��[�v
			while(!current.isNodeExpanded()){
				int eval=0;
				// �[��deplev�܂œW�J����
				for(; curlev<deplev; curlev++){
					current = current.generateNextNode(nextIdx++);
					////////////////////////////////////////////////////////////////////////
					//current.printNode();
					////////////////////////////////////////////////////////////////////////
					nodes.add(current);
					eval = current.getBoardEval(self, opponent);
					// �r���Ō����������炻��ȍ~�͓W�J���Ȃ�
					if(Math.abs(eval) == Node.win)
						break;
				}
				
				///////////////////////////////////////////////////////////////////////////////
				//System.out.println("currentIdx:" + current.getIdx() + " eval:" + eval + " deplev:" + deplev
				//		+ " next:" + current.getNext() );
				///////////////////////////////////////////////////////////////////////////////

				// �m�[�h�̕]���l��ۑ����C�e�m�[�h�֖߂�
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
			
			// curlev�������Ȃ�q�m�[�h��Evals�̍ő�l�����݃m�[�h��Eval�Ƃ��č̑�����
			if(curlev%2 == 0){
				evals.get(curlev).add(Collections.max(evals.get(curlev+1)));
				// ���݃m�[�h���[���m�[�h�̏ꍇ�C�q�m�[�h�̕]���l����������
				if(curlev != 0)
					evals.get(curlev+1).clear();
			// curlev����Ȃ猻�݃��x����Evals�̍ŏ��l��e�m�[�h��Eval�Ƃ��č̑�����
			}else{
				evals.get(curlev).add(Collections.min(evals.get(curlev+1)));
				evals.get(curlev+1).clear();
			}
			
			
			// �e�m�[�h�֖߂�
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
		// �f�o�b�O�p�p�����[�^�o��
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
		// ���݃m�[�h�̒����m�[�h�Q����ő�̕]���l�𓾂�m�[�h��I�����C�J�ڂ���
		current = nodes.get(nodes.get(currentIdx).getTo(evals.get(curlev+1).indexOf(Collections.max(evals.get(curlev+1)))));
		return current.getIdx();
	}
}
