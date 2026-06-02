public class AgroChainAVL {

    static class Node {
        int batchId, height;
        String crop;
        Node left, right;

        Node(int batchId, String crop) {
            this.batchId = batchId;
            this.crop = crop;
            this.height = 1;
        }
    }

    Node root;

    int height(Node n) {
        return (n == null) ? 0 : n.height;
    }

    int getBalance(Node n) {
        return (n == null) ? 0 : height(n.left) - height(n.right);
    }

    Node rightRotate(Node y) {
        System.out.println("LL Rotation Performed at Node " + y.batchId);

        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        y.left = t2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    Node leftRotate(Node x) {
        System.out.println("RR Rotation Performed at Node " + x.batchId);

        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        x.right = t2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    Node insert(Node node, int batchId, String crop) {

        if (node == null) {
            System.out.println("Inserted Batch " + batchId + " - " + crop);
            return new Node(batchId, crop);
        }

        if (batchId < node.batchId)
            node.left = insert(node.left, batchId, crop);
        else if (batchId > node.batchId)
            node.right = insert(node.right, batchId, crop);
        else
            return node;

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        // LL Case
        if (balance > 1 && batchId < node.left.batchId)
            return rightRotate(node);

        // RR Case
        if (balance < -1 && batchId > node.right.batchId)
            return leftRotate(node);

        // LR Case
        if (balance > 1 && batchId > node.left.batchId) {
            node.left = leftRotate(node.left);
            System.out.println("LR Rotation Performed");
            return rightRotate(node);
        }

        // RL Case
        if (balance < -1 && batchId < node.right.batchId) {
            node.right = rightRotate(node.right);
            System.out.println("RL Rotation Performed");
            return leftRotate(node);
        }

        return node;
    }

    Node search(Node node, int key) {

        if (node == null || node.batchId == key)
            return node;

        if (key < node.batchId)
            return search(node.left, key);

        return search(node.right, key);
    }

    void inorder(Node node) {
        if (node != null) {
            inorder(node.left);
            System.out.println(
                    "Batch ID: " + node.batchId +
                    " | Crop: " + node.crop);
            inorder(node.right);
        }
    }

    public static void main(String[] args) {

        AgroChainAVL tree = new AgroChainAVL();

        System.out.println("===== AGROCHAIN INVENTORY MANAGEMENT =====");

        tree.root = tree.insert(tree.root, 30, "Rice");
        tree.root = tree.insert(tree.root, 20, "Wheat");
        tree.root = tree.insert(tree.root, 10, "Corn");      // LL Rotation

        tree.root = tree.insert(tree.root, 40, "Cotton");
        tree.root = tree.insert(tree.root, 50, "Sugarcane"); // RR Rotation

        System.out.println("\n===== SORTED INVENTORY =====");

        tree.inorder(tree.root);

        System.out.println("\n===== SEARCH OPERATION =====");

        Node result = tree.search(tree.root, 40);

        if (result != null) {
            System.out.println("Record Found");
            System.out.println("Batch ID: " + result.batchId);
            System.out.println("Crop: " + result.crop);
        }

        System.out.println("\n===== AVL TREE STATISTICS =====");

        System.out.println("Tree Height: " +
                tree.height(tree.root));

        System.out.println("Root Node: " +
                tree.root.batchId);

        System.out.println("Balance Factor: " +
                tree.getBalance(tree.root));
    }
}
