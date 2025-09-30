import java.util.ArrayList;
import java.util.Date;
import java.security.MessageDigest;

// (Block)
class Block {
    public int index;
    public long timestamp;
    public String data;
    public String previousHash;
    public String hash;
    public int nonce;

    // Constructor
    public Block(int index, String data, String previousHash) {
        this.index = index;
        this.data = data;
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        this.nonce = 0;
        this.hash = calculateHash();
    }

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

    //(Proof of Work)
    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined! : " + hash);
    }
}
class CentralBlockchain {
    public ArrayList<Block> chain;
    public int difficulty = 4;

    public CentralBlockchain() {
        chain = new ArrayList<>();
        chain.add(createGenesisBlock());
    }

    // (Genesis Block)
    private Block createGenesisBlock() {
        Block genesisBlock = new Block(0, "Genesis Block", "0");
        genesisBlock.mineBlock(difficulty);
        return genesisBlock;
    }

    // إضافة كتلة جديدة
    public void addBlock(String data) {
        Block previousBlock = chain.get(chain.size() - 1);
        Block newBlock = new Block(previousBlock.index + 1, data, previousBlock.hash);
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
    }

    // الحصول على كتلة معينة حسب index
    public Block getBlock(int index) {
        if (index < chain.size()) {
            return chain.get(index);
        }
        return null;
    }

    // استعراض كل الكتل
    public void exploreBlocks() {
        for (Block block : chain) {
            System.out.println("Block #" + block.index);
            System.out.println("Timestamp: " + block.timestamp);
            System.out.println("Data: " + block.data);
            System.out.println("Previous Hash: " + block.previousHash);
            System.out.println("Hash: " + block.hash);
            System.out.println("Nonce: " + block.nonce);
            System.out.println("----------------------------");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        CentralBlockchain myBlockchain = new CentralBlockchain();

        myBlockchain.addBlock("Block 1 Data");
        myBlockchain.addBlock("Block 2 Data");

        // استعراض كل الكتل
        myBlockchain.exploreBlocks();

        // استرجاع كتلة معينة
        Block block1 = myBlockchain.getBlock(1);
        System.out.println("Retrieved Block 1 Data: " + block1.data);
    }
}
