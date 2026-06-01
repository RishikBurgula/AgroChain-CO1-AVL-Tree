class Node {
    int batchId;
    String cropName;
    int quantity;
    int height;
    Node left, right;

    Node(int batchId, String cropName, int quantity) {
        this.batchId = batchId;
        this.cropName = cropName;
        this.quantity = quantity;
        this.height = 1;
    }
}

public class AgroChainAVL {

    Node root;

    int height(Node node) {
        return node == null ? 0 : node.height;
    }

    int getBalance(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    Node rightRotate(Node y) {
        Node x = y.left;
        Node t2 = x.right;

        x.right = y;
        y.left = t2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    Node leftRotate(Node x) {
        Node y = x.right;
        Node t2 = y.left;

        y.left = x;
        x.right = t2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    Node insert(Node node, int batchId, String cropName, int quantity) {

        if (node == null)
            return new Node(batchId, cropName, quantity);

        if (batchId < node.batchId)
            node.left = insert(node.left, batchId, cropName, quantity);
        else if (batchId > node.batchId)
            node.right = insert(node.right, batchId, cropName, quantity);
        else
            return node;

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && batchId < node.left.batchId)
            return rightRotate(node);

        if (balance < -1 && batchId > node.right.batchId)
            return leftRotate(node);

        if (balance > 1 && batchId > node.left.batchId) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && batchId < node.right.batchId) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    Node search(Node node, int batchId) {
        if (node == null || node.batchId == batchId)
            return node;

        if (batchId < node.batchId)
            return search(node.left, batchId);

        return search(node.right, batchId);
    }

    void inorder(Node node) {
        if (node != null) {
            inorder(node.left);
            System.out.println(
                "Batch ID: " + node.batchId +
                ", Crop: " + node.cropName +
                ", Quantity: " + node.quantity
            );
            inorder(node.right);
        }
    }

    public static void main(String[] args) {

        AgroChainAVL tree = new AgroChainAVL();

        tree.root = tree.insert(tree.root, 150, "Corn", 450);
        tree.root = tree.insert(tree.root, 101, "Rice", 500);
        tree.root = tree.insert(tree.root, 200, "Sugarcane", 600);
        tree.root = tree.insert(tree.root, 120, "Wheat", 300);
        tree.root = tree.insert(tree.root, 180, "Cotton", 250);

        System.out.println("Crop Inventory Report:");
        tree.inorder(tree.root);

        Node result = tree.search(tree.root, 120);

        if (result != null) {
            System.out.println("\nCrop Found:");
            System.out.println("Batch ID: " + result.batchId);
            System.out.println("Crop: " + result.cropName);
            System.out.println("Quantity: " + result.quantity);
        }
    }
}
