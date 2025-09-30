import java.util.ArrayList;
import java.util.Date;

class Block {
	// proparity using to create blockchain 
    public int index;
    public long timestamp;
    public String data;
    public String previousHash;
    public String hash;
    public int nonce; 

    public Block(int index, String data, String previousHash) {
        this.index = index;
        this.data = data;
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        this.nonce = 0;
        this.hash = calculateHash();
    }

    // دالة تعدين الكتلة ببحث فيها عن هاش بعدد معين من الاصفار 
    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }
}

// Defined blockchain by arrayList
class Blockchain {
    public ArrayList<Block> chain;
    public int difficulty = 4; // بحدد هان مستوى الصعوبة الي رح استخدنه لتعدين ال

    public Blockchain() {
        chain = new ArrayList<>();
       
    }

    // دالة إضافة كتلة جديدة
    public void setBlock(String data) {
        Block previousBlock = chain.get(chain.size() - 1);
        Block newBlock = new Block(previousBlock.index + 1, data, previousBlock.hash);
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
    }

    // دالة الحصول على كتلة محددة حسب الـ index
    public Block getBlock(int index) {
        if (index < chain.size()) {
            return chain.get(index);
        }
        return null;
    }

    // دالة عرض كل الكتل
    public void blocksExplorer() {
        for (Block block : chain) {
            System.out.println("Block #" + block.index);
            System.out.println("Timestamp: " + block.timestamp);
            System.out.println("Data: " + block.data);
            System.out.println("Previous Hash: " + block.previousHash);
            System.out.println("Hash: " + block.hash);
            System.out.println("Nonce: " + block.nonce);
            System.out.println("-------------------------------------");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Blockchain myBlockchain = new Blockchain();

        myBlockchain.setBlock("First Block Data");
        myBlockchain.setBlock("Second Block Data");

        // عرض كل الكتل 
        myBlockchain.blocksExplorer();

        // استرجاع كتلة معينة
        Block block1 = myBlockchain.getBlock(1);
        System.out.println("Retrieved Block 1 Data: " + block1.data);
    }
}
