public class BinarySearchTreeObject {

    public Node root;

    public static class Node {
        int key;
        int value;
        Node left;
        Node right;

        // Initialize constructor of Node class
        public Node(int key, int value) {
            this.key = key;
            this.value = value;
            left = null;
            right = null;
        }
    }

    public static Node insert(Node root, int key, int value) {
        if (root == null) {
            return new Node(key, value);
        }
        if (value < root.value) {
            root.left = insert(root.left, key, value);
        } else if (value > root.value) {
            root.right = insert(root.right, key, value);
        }
        return root;
    }

    public static boolean search(Node root, int key, int value) {
        if (root == null) {
            return false;
        }

        if (root.value == value) {
            return true;
        }

        if (value < root.value) {
            return search(root.left, key, value);
        } else if (value > root.value) {
            return search(root.right, key, value);
        }
        
        return false;
    }

    public static void display(Node root) {
        if (root != null) {
            display(root.left);
            System.out.println(root.key + "=" + root.value);
            display(root.right);
        }
    }
    
    public static void main(String[] args) {
        BinarySearchTreeObject tree = new BinarySearchTreeObject();

        // { -10671056=2, -16766208=11, -16700672=5, -16635136=2, -16635135=2, -16569600=7, -16504064=5, -16372990=2, -16438528=4}
        tree.root = new Node(-10671056, 2);
        tree.root = insert(tree.root, -16766208, 11);
        tree.root = insert(tree.root, -16700672, 5);
        tree.root = insert(tree.root, -16635136, 2);
        tree.root = insert(tree.root, -16635135, 2);
        tree.root = insert(tree.root, -16569600, 7);
        tree.root = insert(tree.root, -16504064, 5);
        tree.root = insert(tree.root, -16372990, 2);
        tree.root = insert(tree.root, -16438528, 4);

        display(tree.root);
    }

}