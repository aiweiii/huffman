public class BinarySearchTree {

    public Node root;

    public static class Node {
        int data;
        Node left;
        Node right;

        // Initialize constructor of Node class
        public Node(int data) {
            this.data = data;
            left = null;
            right = null;
        }
    }

    // public BinarySearchTree() { root = null; }

    public static Node insert(Node root, int data) {
        if (root == null) {
            return new Node(data);
        }
        if (data < root.data) {
            root.left = insert(root.left, data);
        } else if (data > root.data) {
            root.right = insert(root.right, data);
        }
        return root;
    }

    public static boolean search(Node root, int data) {
        if (root == null) {
            return false;
        }

        if (root.data == data) {
            return true;
        }

        if (data < root.data) {
            return search(root.left, data);
        } else if (data > root.data) {
            return search(root.right, data);
        }
        
        return false;
    }

    public static void display(Node root) {
        if (root != null) {
            display(root.left);
            System.out.println(root.data);
            display(root.right);
        }
    }
    
    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();

        tree.root = new Node(6);
        tree.root = insert(tree.root, 2);
        tree.root = insert(tree.root, 10);
        tree.root = insert(tree.root, 12);
        tree.root = insert(tree.root, 3);
        tree.root = insert(tree.root, 4);
        tree.root = insert(tree.root, 1);

        display(tree.root);
    }

}