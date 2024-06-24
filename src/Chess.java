import javax.swing.*;
import java.util.Arrays;
import java.util.Scanner;

public class Chess{
    static String[][] board  = {
        {"r","k","b","q","a","b","k","r"},
        {"p","p","p","p","p","p","p","p"},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {" "," "," "," "," "," "," "," "},
        {"P","P","P","P","P","P","P","P"},
        {"R","K","B","Q","A","B","K","R"}
    };
    static int whiteKingPosition, blackKingPosition;
    static int GLOBALDEPTH = 4;
    public static void main(String[] args){
        while ('A' != board[whiteKingPosition/8][whiteKingPosition%8].charAt(0)) {
            whiteKingPosition++;            
        }
        while ('a' != board[blackKingPosition/8][blackKingPosition%8].charAt(0)) {
            blackKingPosition++;            
        }
        // JFrame frame = new JFrame("The Jimmy Destroyer");
        // frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // frame.add(new ChessUI());
        // frame.setSize(500,500);
        // frame.setResizable(false);
        // frame.setVisible(true);
        
        /*
        for (int i = 0; i < 8; i++) {
            System.out.println(Arrays.toString(board[i]));
        }*/
        // System.out.println(possibleMoves());
        System.out.println(alphaBeta(GLOBALDEPTH, Integer.MAX_VALUE, Integer.MIN_VALUE, "", 0));
    }  

    public static String alphaBeta(int depth, int beta, int alpha, String move, int player){
        String list  = possibleMoves();
        if (depth == 0 || list.length() == 0) return move+(rating()*(player*2-1));
        player = 1 - player;
        for (int i = 0; i < list.length(); i+=5) {
            String sub = list.substring(i,i+5);
            makeMove(sub);
            flipBoard();
            String returnString = alphaBeta(depth-1, beta, alpha, sub, player);
            int value = Integer.parseInt(returnString.substring(5));
            flipBoard();
            undoMove(sub);
            if (player == 0) {
                if (value <= beta) {
                    beta = value;
                    if (depth == GLOBALDEPTH) {
                        move = returnString.substring(0, 5);
                    }
                }   
            } else if (value > alpha) {
                alpha = value;
                if (depth == GLOBALDEPTH) {
                    move = returnString.substring(0, 5);
                }
            } 
            if (alpha >= beta){
                if (player == 0) {
                    return move+beta;
                }
                else{
                    return move + alpha;
                }
            }
        }
        if (player == 0 ) {
            return move + beta;
        }
        else{
            return move + alpha;
        }
    }
    
    public static void flipBoard(){

    }

    public static int rating(){
        return 0;
    }
    
    public static void makeMove(String move) {
        int x1 = Character.getNumericValue(move.charAt(0));
        int y1 = Character.getNumericValue(move.charAt(1));
        int x2 = Character.getNumericValue(move.charAt(2));
        int y2 = Character.getNumericValue(move.charAt(3));
        if(move.charAt(4)!='P'){
            board[x2][y2]=board[x1][y1];
            board[x1][y1]=" ";
        }
        else{
            board[1][x1]=" ";
            board[0][y1]=Integer.toString(y2);
        }
    }
    public static void undoMove(String move) {
        int x1 = Character.getNumericValue(move.charAt(0));
        int y1 = Character.getNumericValue(move.charAt(1));
        int x2 = Character.getNumericValue(move.charAt(2));
        int y2 = Character.getNumericValue(move.charAt(3));
        if(move.charAt(4)!='P'){
            board[x1][y1]=board[x2][y2];
            board[x2][y2]=move.substring(4);
        }
        else{
            board[1][x1]="P";
            board[0][y1]=Integer.toString(x2);
        }
    }
    
    public static String possibleMoves(){
        StringBuilder list = new StringBuilder();
        for(int i = 0; i < 64; i++){
            switch(board[i/8][i%8]){
                case "P":  
                    list.append(possibleP(i));
                    break;
                case "R":
                    list.append(possibleR(i));
                    break;
                case "K":
                    list.append(possibleK(i));
                    break;
                case "B":
                    list.append(possibleB(i));
                    break;
                case "Q":
                    list.append(possibleQ(i));
                    break;
                case "A":
                    list.append(possibleA(i));
                    break;
                default:
                    break;
            }
        }
        return list.toString();
    }
    
    private static String possibleA(int i) {
        StringBuilder list = new StringBuilder();
        String oldPiece = "";
        int r=i/8, c = i%8;
        for (int j = 0; j < 9; j++) {
            if (j!=4) {
                try{
                    if (Character.isLowerCase(board[r-1+j/3][c-1+j%3].charAt(0)) || ' '==board[r-1+j/3][c-1+j%3].charAt(0)) {
                        oldPiece = board[r-1+j/3][c-1+j%3];
                        board[r][c] = " ";
                        board[r-1+j/3][c-1+j%3] = "A";
                        int kingTemp = whiteKingPosition;
                        whiteKingPosition = i+(j/3)*8+j%3-9;
                        if(kingSafe()){
                                list.append(r);
                                list.append(c);
                                list.append(r-1+j/3);
                                list.append(c-1+j%3);
                                list.append(oldPiece);
                        }
                        board[r][c] = "A";
                        board[r-1+j/3][c-1+j%3] = oldPiece;
                        whiteKingPosition = kingTemp;
                    }
                }
                catch(Exception e){}
            }
        }
        return list.toString();
    }
    private static boolean kingSafe() {
        int temp = 1;
        for (int i = -1; i <= 1; i+=2) {
            for (int j = -1; j <= 1; j+=2) {
                try {
                    while (' '==board[whiteKingPosition/8+temp*i][whiteKingPosition%8+temp*j].charAt(0)){temp++;}
                    if('b'==board[whiteKingPosition/8+temp*i][whiteKingPosition%8+temp*j].charAt(0)||
                       'q'==board[whiteKingPosition/8+temp*i][whiteKingPosition%8+temp*j].charAt(0) ){
                        return false;
                    }
                } catch (Exception e) {}
                temp=1;
            }
        }
        for (int i = -1; i <= 1; i+=2) {
            try {
                if('r'==board[whiteKingPosition/8][whiteKingPosition%8+temp*i].charAt(0)||
                    'q'==board[whiteKingPosition/8][whiteKingPosition%8+temp*i].charAt(0) ){
                    return false;
                }
            } catch (Exception e) {}
            try {
                if('r'==board[whiteKingPosition/8+temp*i][whiteKingPosition%8].charAt(0)||
                    'q'==board[whiteKingPosition/8+temp*i][whiteKingPosition%8].charAt(0) ){
                    return false;
                }
            } catch (Exception e) {}
        }
        for (int i = -1; i <= 1; i+=2) {
            for (int j = -1; j <= 1; j+=2) {
                try {
                    if('k'==board[whiteKingPosition/8+i][whiteKingPosition%8+2*j].charAt(0))
                        return false;
                } catch (Exception e) {}
                try {
                    if('k'==board[whiteKingPosition/8+2*i][whiteKingPosition%8+j].charAt(0))
                        return false;
                } catch (Exception e) {}
            }
        }
        if (whiteKingPosition>=16) {
            try {
                if ('p'==board[whiteKingPosition/8-1][whiteKingPosition%8-1].charAt(0)) {
                    return false;
                }
            } catch (Exception e) {}
            try {
                if ('p'==board[whiteKingPosition/8-1][whiteKingPosition%8+1].charAt(0)) {
                    return false;
                }
            } catch (Exception e) {}
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if(i==0&&j==0)continue;
                    try {
                        if('a'==board[whiteKingPosition/8+i][whiteKingPosition%8+j].charAt(0))
                            return false;
                    } catch (Exception e) {}
                }
            }
        }
        return true;
    }
    private static String possibleQ(int i) {
        StringBuilder list = new StringBuilder();
        String oldPiece = "";
        int temp = 1;
        int r=i/8;
        int c = i%8;
        for (int j = -1; j <= 1; j++) {
            for (int j2 = -1; j2 <= 1; j2++) {
                if(j==0 && j2==0)continue;
                try {
                    while (' '==board[r+temp*j][c+temp*j2].charAt(0)) {
                        oldPiece = board[r+temp*j][c+temp*j2];
                        board[r][c] = " ";
                        board[r+temp*j][c+temp*j2] = "Q";
                        if(kingSafe()){
                            list.append(r);
                            list.append(c);
                            list.append(r+temp*j);
                            list.append(c+temp*j2);
                            list.append(oldPiece);
                        }
                        board[r][c] = "Q";
                        board[r+temp*j][c+temp*j2] = oldPiece;
                        temp++;
                    }
                    if (Character.isLowerCase(board[r+temp*j][c+temp*j2].charAt(0))) {
                        oldPiece = board[r+temp*j][c+temp*j2];
                        board[r][c] = " ";
                        board[r+temp*j][c+temp*j2] = "Q";
                        if(kingSafe()){
                            list.append(r);
                            list.append(c);
                            list.append(r+temp*j);
                            list.append(c+temp*j2);
                            list.append(oldPiece);
                        }
                        board[r][c] = "Q";
                        board[r+temp*j][c+temp*j2] = oldPiece;
                    }
                } catch (Exception e) {}
                temp=1;
            }
        }
        return list.toString();
    }
    private static Object possibleB(int i) {
        StringBuilder list = new StringBuilder();
        String oldPiece = "";
        int temp = 1;
        int r=i/8;
        int c = i%8;
        for (int j = -1; j <= 1; j+=2) {
            for (int j2 = -1; j2 <= 1; j2+=2) {
                try {
                    while (' '==board[r+temp*j][c+temp*j2].charAt(0)) {
                        oldPiece = board[r+temp*j][c+temp*j2];
                        board[r][c] = " ";
                        board[r+temp*j][c+temp*j2] = "B";
                        if(kingSafe()){
                            list.append(r);
                            list.append(c);
                            list.append(r+temp*j);
                            list.append(c+temp*j2);
                            list.append(oldPiece);
                        }
                        board[r][c] = "B";
                        board[r+temp*j][c+temp*j2] = oldPiece;
                        temp++;
                    }
                    if (Character.isLowerCase(board[r+temp*j][c+temp*j2].charAt(0))) {
                        oldPiece = board[r+temp*j][c+temp*j2];
                        board[r][c] = " ";
                        board[r+temp*j][c+temp*j2] = "B";
                        if(kingSafe()){
                            list.append(r);
                            list.append(c);
                            list.append(r+temp*j);
                            list.append(c+temp*j2);
                            list.append(oldPiece);
                        }
                        board[r][c] = "B";
                        board[r+temp*j][c+temp*j2] = oldPiece;
                    }
                } catch (Exception e) {}
                temp=1;
            }
        }
        return list.toString();
    }
    private static String possibleR(int i) {
        StringBuilder list = new StringBuilder();
        String oldPiece;
        int temp = 1;
        int r=i/8;
        int c = i%8;
        for (int j = -1; j <=1 ; j+=2) {
            try {
                while(' ' == board[r][c+temp*j].charAt(0)){
                    oldPiece = board[r][c+temp*j];
                    board[r][c] = " ";
                    board[r][c+temp*j] = "R";
                    if(kingSafe()){
                        list.append(r);
                        list.append(c);
                        list.append(r);
                        list.append(c+temp*j);
                        list.append(oldPiece);
                    }
                    board[r][c] = "R";
                    board[r][c+temp*j] = oldPiece;
                    temp++;
                }
                if (Character.isLowerCase(board[r][c+temp*j].charAt(0))) {
                    oldPiece = board[r][c+temp*j];
                    board[r][c] = " ";
                    board[r][c+temp*j] = "R";
                    if(kingSafe()){
                        list.append(r);
                        list.append(c);
                        list.append(r);
                        list.append(c+temp*j);
                        list.append(oldPiece);
                    }
                    board[r][c] = "R";
                    board[r][c+temp*j] = oldPiece;
                }
            } catch (Exception e) {}
            temp = 1;
            try {
                while(' ' == board[r+temp*j][c].charAt(0)){
                    oldPiece = board[r+temp*j][c];
                    board[r][c] = " ";
                    board[r+temp*j][c] = "R";
                    if(kingSafe()){
                        list.append(r);
                        list.append(c);
                        list.append(r+temp*j);
                        list.append(c);
                        list.append(oldPiece);
                    }
                    board[r][c] = "R";
                    board[r+temp*j][c] = oldPiece;
                    temp++;
                }
                if (Character.isLowerCase(board[r+temp*j][c].charAt(0))) {
                    oldPiece = board[r+temp*j][c];
                    board[r][c] = " ";
                    board[r+temp*j][c] = "R";
                    if(kingSafe()){
                        list.append(r);
                        list.append(c);
                        list.append(r+temp*j);
                        list.append(c);
                        list.append(oldPiece);
                    }
                    board[r][c] = "R";
                    board[r+temp*j][c] = oldPiece;
                }
            } catch (Exception e) {}
            temp=1;
        }
        return list.toString();
    }
    private static String possibleK(int i) {
        StringBuilder list = new StringBuilder();
        String oldPiece;
        int r=i/8;
        int c = i%8;
        for (int j = -1; j <=1; j+=2) {
            for (int j2 = -1; j2 <= 1; j2+=2) {
                try {
                    char ch = board[r+j][c+j2*2].charAt(0);
                    if (Character.isLowerCase(ch)||' ' == ch) {
                        oldPiece = board[r+j][c+j2*2];
                        board[r][c] = " ";
                        board[r+j][c+j2*2] = "K";
                        if (kingSafe()) {
                           list.append(r);
                           list.append(c);
                           list.append(r+j);
                           list.append(c+2*j2);
                           list.append(oldPiece);
                        }
                        board[r][c] = "K";
                        board[r+j][c+j2*2] = oldPiece;
                    }
                } catch (Exception e) {}
                try {
                    char ch = board[r+j*2][c+j2].charAt(0);
                    if (Character.isLowerCase(ch)||' ' == ch) {
                        oldPiece = board[r+j*2][c+j2];
                        board[r][c] = " ";
                        board[r+j*2][c+j2] = "K";
                        if (kingSafe()) {
                           list.append(r);
                           list.append(c);
                           list.append(r+j*2);
                           list.append(c+j2);
                           list.append(oldPiece);
                        }
                        board[r][c] = "K";
                        board[r+j*2][c+j2] = oldPiece;
                    }
                } catch (Exception e) {}
            }
        }
        return list.toString();
    }
    private static String possibleP(int i) {
        StringBuilder list = new StringBuilder();
        String oldPiece;
        int r=i/8;
        int c = i%8;
        for (int j = -1; j <= 1; j+=2) {
            try {//Captures(not en passant)
                if(Character.isLowerCase(board[r-1][c+j].charAt(0)) && i>=16){
                    oldPiece = board[r-1][c+j];
                    board[r][c] = " ";
                    board[r-1][c+j] = "P";
                    if (kingSafe()) {
                       list.append(r);
                       list.append(c);
                       list.append(r-1);
                       list.append(c+j);
                       list.append(oldPiece);
                    }
                    board[r][c] = "P";
                    board[r-1][c+j] = oldPiece; 
                }
            } catch (Exception e) {}
            try {//Promotion with capture
                if(Character.isLowerCase(board[r-1][c+j].charAt(0)) && i<16){
                    String[] temp = {"Q","R","B","K"};
                    for (int k = 0; k < temp.length; k++) {
                        oldPiece = board[r-1][c+j];
                        board[r][c] = " ";
                        board[r-1][c+j] = temp[k];
                        if (kingSafe()) {
                            list.append(c);
                            list.append(c+j);
                            list.append(oldPiece);
                            list.append(temp[k]);
                            list.append("P");
                        }
                        board[r][c] = "P";
                        board[r-1][c+j] = oldPiece; 
                    }
                    
                }
            } catch (Exception e) {}
        }
        try {//Single up
            if(' '==board[r-1][c].charAt(0) && i>=16){
                oldPiece = board[r-1][c];
                board[r][c] = " ";
                board[r-1][c] = "P";
                if (kingSafe()) {
                   list.append(r);
                   list.append(c);
                   list.append(r-1);
                   list.append(c);
                   list.append(oldPiece);
                }
                board[r][c] = "P";
                board[r-1][c] = oldPiece; 
            }
        } catch (Exception e) {}
        try {//Single up promo
            if(' '==board[r-1][c].charAt(0) && i<16){
                String[] temp = {"Q","R","B","K"};
                for (int k = 0; k < temp.length; k++) {
                    oldPiece = board[r-1][c];
                    board[r][c] = " ";
                    board[r-1][c] = temp[k];
                    if (kingSafe()) {
                        list.append(c);
                        list.append(c);
                        list.append(oldPiece);
                        list.append(temp[k]);
                        list.append("P");
                    }
                    board[r][c] = "P";
                    board[r-1][c] = oldPiece; 
                } 
            }
        } catch (Exception e) {}
        try {//Double up
            if(i>=48 && ' '==board[r-1][c].charAt(0) && ' '==board[r-2][c].charAt(0)){
                oldPiece = board[r-2][c];
                board[r][c] = " ";
                board[r-2][c] = "P";
                if (kingSafe()) {
                   list.append(r);
                   list.append(c);
                   list.append(r-2);
                   list.append(c);
                   list.append(oldPiece);
                }
                board[r][c] = "P";
                board[r-2][c] = oldPiece; 
            }
        } catch (Exception e) {}
        return list.toString();
    }
}