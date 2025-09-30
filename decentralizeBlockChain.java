import java.util.ArrayList;
import java.util.Date;
import java.security.MessageDigest;

// تعريف الكتلة (Block)
class Block {
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

    // دالة حساب الهاش باستخدام SHA-256
    public String calculateHash() {
        try {
            String input = index + Long.toString(timestamp) + data + previousHash + nonce;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // دالة التعدين (Proof of Work)
    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined! : " + hash);
    }
}

// تعريف العقدة (Node) في النظام اللامركزي
class Node {
    public ArrayList<Block> blockchain;
    public int difficulty = 4;

    public Node() {
        blockchain = new ArrayList<>();
        blockchain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() {
        Block genesis = new Block(0, "Genesis Block", "0");
        genesis.mineBlock(difficulty);
        return genesis;
    }

    // إضافة كتلة جديدة للعقدة
    public void addBlock(String data) {
        Block previousBlock = blockchain.get(blockchain.size() - 1);
        Block newBlock = new Block(previousBlock.index + 1, data, previousBlock.hash);
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    // التحقق من صحة السلسلة
    public boolean isChainValid() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block current = blockchain.get(i);
            Block previous = blockchain.get(i - 1);

            if (!current.hash.equals(current.calculateHash())) {
                return false;
            }
            if (!current.previousHash.equals(previous.hash)) {
                return false;
            }
        }
        return true;
    }

    // استعراض الكتل
    public void exploreBlocks() {
        for (Block block : blockchain) {
            System.out.println("Block #" + block.index);
            System.out.println("Timestamp: " + block.timestamp);
            System.out.println("Data: " + block.data);
            System.out.println("Previous Hash: " + block.previousHash);
            System.out.println("Hash: " + block.hash);
            System.out.println("Nonce: " + block.nonce);
            System.out.println("---------------------------");
        }
    }
}

// النظام اللامركزي (Decentralized Blockchain)
class DecentralizedBlockchain {
    public ArrayList<Node> nodes;

    public DecentralizedBlockchain() {
        nodes = new ArrayList<>();
    }

    // إضافة عقدة جديدة للنظام
    public void addNode(Node node) {
        nodes.add(node);
    }

    // مزامنة العقدة: نسخة العقدة الأقوى (الأطول) تصبح مرجعاً
    public void synchronize() {
        Node longestChainNode = nodes.get(0);
        for (Node node : nodes) {
            if (node.blockchain.size() > longestChainNode.blockchain.size()) {
                longestChainNode = node;
            }
        }
        // مزامنة كل العقد مع أطول سلسلة
        for (Node node : nodes) {
            node.blockchain = new ArrayList<>(longestChainNode.blockchain);
        }
        System.out.println("All nodes synchronized to the longest chain.");
    }
}

// اختبار النظام اللامركزي
public class Main {
    public static void main(String[] args) {
        DecentralizedBlockchain network = new DecentralizedBlockchain();

        Node node1 = new Node();
        Node node2 = new Node();
        Node node3 = new Node();

        network.addNode(node1);
        network.addNode(node2);
        network.addNode(node3);

        // إضافة كتل مختلفة لكل عقدة
        node1.addBlock("Node1 - Block1");
        node2.addBlock("Node2 - Block1");
        node3.addBlock("Node3 - Block1");

        // التحقق من صحة كل سلسلة قبل المزامنة
        System.out.println("Node1 valid? " + node1.isChainValid());
        System.out.println("Node2 valid? " + node2.isChainValid());
        System.out.println("Node3 valid? " + node3.isChainValid());

        // مزامنة العقد
        network.synchronize();

        // عرض الكتل بعد المزامنة
        System.out.println("\nNode1 Blockchain after sync:");
        node1.exploreBlocks();

        System.out.println("\nNode2 Blockchain after sync:");
        node2.exploreBlocks();
    }
}
